package net.techguard.izone.listeners;

import net.techguard.izone.Minecraft;
import net.techguard.izone.Title;
import net.techguard.izone.Variables;
import net.techguard.izone.configuration.ConfigManager;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.InvManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Settings;
import net.techguard.izone.zones.Zone;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.techguard.izone.Phrases.phrase;

public class pListener implements Listener {

	private CopyOnWriteArrayList<UUID> rightClickPlayers = new CopyOnWriteArrayList<>();

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (iZone.serverVersion.newerThan(Minecraft.Version.v1_9_R1) && event.getHand() != EquipmentSlot.HAND)
		{
			return;
		}

		Player    player = event.getPlayer();
		if (ConfigManager.useAsWhiteList())
		{
			if (!ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}
		else
		{
			if (ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}

		Settings  sett   = Settings.getSett(player);
		ItemStack inHand = iZone.serverVersion.newerThan(Minecraft.Version.v1_9_R1) ? player.getInventory().getItemInMainHand() : player.getItemInHand();
		if (inHand == null)
		{
			return;
		}

		Location clicked = event.getClickedBlock().getLocation();
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			if ((player.hasPermission(Variables.PERMISSION_CHECK)) && (inHand.getType() == Material.matchMaterial(ConfigManager.getCheckTool())))
			{
				if (rightClickPlayers.contains(player.getUniqueId()))
				{
					event.setCancelled(true);
					return;
				}

				rightClickPlayers.add(player.getUniqueId());
				iZone.instance.getServer().getScheduler().runTaskLaterAsynchronously(iZone.instance, () -> rightClickPlayers.remove(player.getUniqueId()), 20L);

				iZone.instance.sendInfo(player, clicked);
				event.setCancelled(true);
			}
			else if ((player.hasPermission(Variables.PERMISSION_DEFINE)) && (inHand.getType() == Material.matchMaterial(ConfigManager.getDefineTool())))
			{
				sett.setBorder(2, clicked);
				player.sendMessage(iZone.getPrefix() + phrase("zone_position_2_set", clicked.getBlockX(), clicked.getBlockY(), clicked.getBlockZ()));
				event.setCancelled(true);
			}
		}
		else if ((event.getAction() == Action.LEFT_CLICK_BLOCK) &&
		         (player.hasPermission(Variables.PERMISSION_DEFINE)) && (inHand.getType() == Material.matchMaterial(ConfigManager.getDefineTool())))
		{
			sett.setBorder(1, clicked);
			player.sendMessage(iZone.getPrefix() + phrase("zone_position_1_set", clicked.getBlockX(), clicked.getBlockY(), clicked.getBlockZ()));
			event.setCancelled(true);
		}

		Zone zone = ZoneManager.getZone(clicked);

		if (zone != null)
		{
			if (!ZoneManager.checkPermission(zone, player, Flags.INTERACT))
			{
				event.setCancelled(true);
				player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
			}

			if (event.getAction() == Action.PHYSICAL && !ZoneManager.checkPermission(zone, player, Flags.PROTECTION))
			{
				Block block = event.getClickedBlock();
				if (block == null)
				{
					return;
				}
				if (block.getType() == Material.SOIL)
				{
					player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
					event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
					event.setCancelled(true);

					block.setTypeIdAndData(block.getType().getId(), block.getData(), true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player   player = event.getPlayer();
		if (ConfigManager.useAsWhiteList())
		{
			if (!ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}
		else
		{
			if (ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}


		Location from   = event.getFrom();
		from = new Location(from.getWorld(), from.getBlockX(), from.getBlockY(), from.getBlockZ(), from.getYaw(), from.getPitch());
		Location to = event.getTo();
		to = new Location(to.getWorld(), to.getBlockX(), to.getBlockY(), to.getBlockZ(), to.getYaw(), to.getPitch());

		Zone fzone = ZoneManager.getZone(from);
		Zone tzone = ZoneManager.getZone(to);

		if ((tzone != fzone) && (tzone != null) && (tzone.hasFlag(Flags.RESTRICTION)) &&
		    (!ZoneManager.checkPermission(tzone, player, Flags.RESTRICTION)))
		{
			player.sendMessage(iZone.getPrefix() + phrase("zone_restricted"));
			event.setCancelled(true);
			return;
		}

		if ((fzone != tzone) && (fzone != null) && (fzone.hasFlag(Flags.JAIL)) &&
		    (!ZoneManager.checkPermission(fzone, player, Flags.JAIL)))
		{
			player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
			event.setCancelled(true);
			return;
		}

		if ((fzone != tzone) && (fzone != null))
		{
			if (fzone.hasFlag(Flags.FAREWELL))
			{
				String s = fzone.getFarewell();
				player.sendMessage(ChatColor.GOLD + "" + ChatColor.GRAY + fzone.getName() + " > " + ChatColor.YELLOW + s);
			}
			if ((fzone.hasFlag(Flags.GAMEMODE)) && (player.getServer().getDefaultGameMode() != player.getGameMode()))
			{
				player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + fzone.getName() + "> " + ChatColor.GRAY + phrase("zone_gamemode"));
				player.setGameMode(player.getServer().getDefaultGameMode());
			}

			if (fzone.hasFlag(Flags.GIVEITEM_OUT))
			{
				ArrayList<ItemStack> inventory = fzone.getInventory(Flags.GIVEITEM_OUT);
				for (ItemStack item : inventory)
				{
					InvManager.addToInventory(player.getInventory(), item);
				}
			}
			if (fzone.hasFlag(Flags.TAKEITEM_OUT))
			{
				ArrayList<ItemStack> inventory = fzone.getInventory(Flags.TAKEITEM_OUT);
				for (ItemStack item : inventory)
				{
					InvManager.removeFromInventory(player.getInventory(), item);
				}
			}
		}
		if ((tzone != fzone) && (tzone != null))
		{
			if (tzone.hasFlag(Flags.WELCOME))
			{
				String s = tzone.getWelcome();
				player.sendMessage(ChatColor.GOLD + "" + ChatColor.GRAY + tzone.getName() + " > " + ChatColor.YELLOW + s);
			}
			if ((tzone.hasFlag(Flags.GAMEMODE)) && (tzone.getGamemode() != player.getGameMode()))
			{
				player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + tzone.getName() + "> " + ChatColor.GRAY + phrase("zone_gamemode"));
				player.setGameMode(tzone.getGamemode());
			}

			if (tzone.hasFlag(Flags.GIVEITEM_IN))
			{
				ArrayList<ItemStack> inventory = tzone.getInventory(Flags.GIVEITEM_IN);
				for (ItemStack item : inventory)
				{
					InvManager.addToInventory(player.getInventory(), item);
				}
			}
			if (tzone.hasFlag(Flags.TAKEITEM_IN))
			{
				ArrayList<ItemStack> inventory = tzone.getInventory(Flags.TAKEITEM_IN);
				for (ItemStack item : inventory)
				{
					InvManager.removeFromInventory(player.getInventory(), item);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player   player = event.getPlayer();
		if (ConfigManager.useAsWhiteList())
		{
			if (!ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}
		else
		{
			if (ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}


		Location from   = event.getFrom();
		from = new Location(from.getWorld(), from.getBlockX(), from.getBlockY(), from.getBlockZ(), from.getYaw(), from.getPitch());
		Location to = event.getTo();
		to = new Location(to.getWorld(), to.getBlockX(), to.getBlockY(), to.getBlockZ(), to.getYaw(), to.getPitch());

		if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ())
		{
			return;
		}
		Zone fzone = ZoneManager.getZone(from);
		Zone tzone = ZoneManager.getZone(to);

		if (tzone != fzone && tzone != null && tzone.hasFlag(Flags.RESTRICTION) && !ZoneManager.checkPermission(tzone, player, Flags.RESTRICTION))
		{
			player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
			from.setX(from.getBlockX() + 0.5D);
			from.setY(from.getBlockY() + 0.0D);
			from.setZ(from.getBlockZ() + 0.5D);
			player.teleport(from);
			return;
		}

		if (fzone != tzone && fzone != null && fzone.hasFlag(Flags.JAIL) && !ZoneManager.checkPermission(fzone, player, Flags.JAIL))
		{
			player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
			from.setX(from.getBlockX() + 0.5D);
			from.setY(from.getBlockY() + 0.0D);
			from.setZ(from.getBlockZ() + 0.5D);
			player.teleport(from);
			return;
		}

		if ((fzone != tzone) && (fzone != null))
		{
			if (fzone.hasFlag(Flags.FAREWELL))
			{
				String s = fzone.getFarewell();
				player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + fzone.getName() + " > " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', s));
				if (iZone.serverVersion.newerThan(Minecraft.Version.v1_8_R1))
				{
					Title.sendTitle(player, 2, 20, 2, "", ChatColor.YELLOW + s);
				}
			}
			if (fzone.hasFlag(Flags.GAMEMODE) && player.getServer().getDefaultGameMode() != player.getGameMode())
			{
				player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + fzone.getName() + "> " + ChatColor.GRAY + phrase("zone_gamemode"));
				player.setGameMode(player.getServer().getDefaultGameMode());
			}
			if (fzone.hasFlag(Flags.FLY) && player.isFlying() && (!player.isOp() && (!player.hasPermission(Variables.PERMISSION_FLY + fzone.getName()) || !player.hasPermission(Variables.PERMISSION_FLY + "*"))))
			{
				player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + fzone.getName() + "> " + ChatColor.GRAY + phrase("zone_fly"));
				player.setFlying(false);
			}

			if (fzone.hasFlag(Flags.GIVEITEM_OUT))
			{
				ArrayList<ItemStack> inventory = fzone.getInventory(Flags.GIVEITEM_OUT);
				for (ItemStack item : inventory)
				{
					InvManager.addToInventory(player.getInventory(), item);
				}
			}
			if (fzone.hasFlag(Flags.TAKEITEM_OUT))
			{
				ArrayList<ItemStack> inventory = fzone.getInventory(Flags.TAKEITEM_OUT);
				for (ItemStack item : inventory)
				{
					InvManager.removeFromInventory(player.getInventory(), item);
				}
			}
		}
		if (tzone != fzone && tzone != null)
		{
			if (tzone.hasFlag(Flags.WELCOME))
			{
				String s = tzone.getWelcome();
				player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + tzone.getName() + " > " + ChatColor.YELLOW + ChatColor.translateAlternateColorCodes('&', s));
				if (iZone.serverVersion.newerThan(Minecraft.Version.v1_8_R1))
				{
					Title.sendTitle(player, 2, 20, 2, "", ChatColor.YELLOW + s);
				}
			}
			if (tzone.hasFlag(Flags.GIVEITEM_IN))
			{
				ArrayList<ItemStack> inventory = tzone.getInventory(Flags.GIVEITEM_IN);
				for (ItemStack item : inventory)
				{
					InvManager.addToInventory(player.getInventory(), item);
				}
			}
			if (tzone.hasFlag(Flags.TAKEITEM_IN))
			{
				ArrayList<ItemStack> inventory = tzone.getInventory(Flags.TAKEITEM_IN);
				for (ItemStack item : inventory)
				{
					InvManager.removeFromInventory(player.getInventory(), item);
				}
			}
		}
		if (tzone != null && tzone.hasFlag(Flags.GAMEMODE) && tzone.getGamemode() != player.getGameMode())
		{
			player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + tzone.getName() + "> " + ChatColor.GRAY + phrase("zone_gamemode"));
			player.setGameMode(tzone.getGamemode());
		}
		if (tzone != null && tzone.hasFlag(Flags.FLY) && player.isFlying() && (!player.isOp() || !player.hasPermission(Variables.PERMISSION_FLY + tzone.getName()) || !player.hasPermission(Variables.PERMISSION_FLY + "*")))
		{
			player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + tzone.getName() + "> " + ChatColor.GRAY + phrase("zone_fly"));
			player.setFlying(false);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (ConfigManager.useAsWhiteList())
		{
			if (!ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}
		else
		{
			if (ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}

		Item   item   = event.getItemDrop();
		Zone   zone   = ZoneManager.getZone(item.getLocation());

		if ((zone != null) && (!ZoneManager.checkPermission(zone, player, Flags.DROP)))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerPickupItem(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if (ConfigManager.useAsWhiteList())
		{
			if (!ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}
		else
		{
			if (ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}

		Item   item   = event.getItem();
		Zone   zone   = ZoneManager.getZone(item.getLocation());

		if ((zone != null) && (!ZoneManager.checkPermission(zone, player, Flags.DROP)))
		{
			event.getItem().remove();
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		if (ConfigManager.useAsWhiteList())
		{
			if (!ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}
		else
		{
			if (ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}

		Block  b      = event.getBlockClicked().getRelative(event.getBlockFace());
		Zone   zone   = ZoneManager.getZone(b.getLocation());

		if ((zone != null) && (!ZoneManager.checkPermission(zone, player, Flags.PROTECTION)))
		{
			event.setCancelled(true);
			player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		Player player = event.getPlayer();
		if (ConfigManager.useAsWhiteList())
		{
			if (!ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}
		else
		{
			if (ConfigManager.containsWorld(player.getWorld().getName()))
			{
				return;
			}
		}

		Block  b      = event.getBlockClicked().getRelative(event.getBlockFace());
		Zone   zone   = ZoneManager.getZone(b.getLocation());

		if ((zone != null) && (!ZoneManager.checkPermission(zone, player, Flags.PROTECTION)))
		{
			event.setCancelled(true);
			player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
		}
	}
}
