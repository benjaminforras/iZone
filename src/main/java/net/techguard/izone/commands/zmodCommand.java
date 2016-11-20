package net.techguard.izone.commands;

import net.milkbowl.vault.economy.Economy;
import net.techguard.izone.MenuBuilder.ItemBuilder;
import net.techguard.izone.MenuBuilder.PageInventory;
import net.techguard.izone.MenuBuilder.inventory.InventoryMenuBuilder;
import net.techguard.izone.MenuBuilder.inventory.InventoryMenuListener;
import net.techguard.izone.Variables;
import net.techguard.izone.commands.zmod.*;
import net.techguard.izone.configuration.ConfigManager;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.VaultManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static net.techguard.izone.Phrases.phrase;

public class zmodCommand extends BaseCommand {
	private static zmodCommand instance;
	private final ArrayList<zmodBase> coms = new ArrayList<>();

	private Zone                  zone;
	private InventoryMenuListener settingsMenuListener;
	private InventoryMenuListener flagsMenuListener;
	private InventoryMenuListener membersMenuListener;

	public zmodCommand(iZone instance) {
		super(instance);
		this.coms.add(new listCommand(instance));
		this.coms.add(new whoCommand(instance));
		this.coms.add(new infoCommand(instance));
		this.coms.add(new createCommand(instance));
		this.coms.add(new deleteCommand(instance));
		this.coms.add(new helpCommand(instance));
		this.coms.add(new allowCommand(instance));
		this.coms.add(new disallowCommand(instance));
		this.coms.add(new flagCommand(instance));
		this.coms.add(new parentCommand(instance));
		this.coms.add(new expandCommand(instance));
		this.coms.add(new visualiseCommand(instance));

		zmodCommand.instance = this;

		settingsMenuListener = (player, action, event) ->
		{
			ItemStack item = event.getInventory().getItem(event.getSlot());
			if (item == null || item.getType() == Material.AIR)
			{
				return;
			}

			if (item.getType() == Material.SIGN)
			{
				InventoryMenuBuilder imb = new InventoryMenuBuilder(InventoryType.PLAYER).withTitle("Zone Management - Flags");

				int i = 0;
				for (Flags flag : zone.getAllFlags())
				{
					imb.withItem(i, new ItemBuilder(Material.SIGN).setTitle(ChatColor.WHITE + "" + ChatColor.BOLD + flag.getName()).addLore(ChatColor.GREEN + "" + ChatColor.BOLD + (zone.hasFlag(flag) ? "ON" : ChatColor.RED + "" + ChatColor.BOLD + "OFF")).build());
					i++;
				}

				imb.show(player);
				imb.onInteract(flagsMenuListener, ClickType.LEFT);
			}
			else if (item.getType() == Material.SKULL_ITEM)
			{
				InventoryMenuBuilder imb = new InventoryMenuBuilder(InventoryType.PLAYER).withTitle("Zone Management - Allowed players");

				int i = 0;
				for (String member : zone.getAllowed())
				{
					if (member.startsWith("o:"))
					{
						continue;
					}
					imb.withItem(i, new ItemBuilder(Material.SKULL_ITEM, (short) 3).setTitle(ChatColor.WHITE + "" + ChatColor.BOLD + member).addLore(ChatColor.RED + "" + ChatColor.BOLD + "Right click to remove this member").build());
					i++;
				}

				imb.show(player);
				imb.onInteract(membersMenuListener, ClickType.RIGHT);
			}
			else if (item.getType() == Material.LAVA_BUCKET || item.getType() == Material.BARRIER)
			{
				player.closeInventory();
				Bukkit.dispatchCommand(player, "zmod delete " + zone.getName());
			}
		};

		flagsMenuListener = (player, action, event) ->
		{
			ItemStack flagItem = event.getCurrentItem();
			if (flagItem == null || flagItem.getType() == Material.AIR)
			{
				return;
			}

			for (Flags flag : zone.getAllFlags())
			{
				if (flag.getName().equals(ChatColor.stripColor(flagItem.getItemMeta().getDisplayName())))
				{
					if (!player.hasPermission(Variables.PERMISSION_FLAGS + flag.toString()))
					{
						player.sendMessage(iZone.getPrefix() + phrase("zone_flag_no_permission"));
						return;
					}

					if (zone.hasFlag(flag))
					{
						zone.setFlag(flag.getId(), false);
					}
					else
					{
						zone.setFlag(flag.getId(), true);
					}

					if (flag.getName().equalsIgnoreCase("gamemode") && zone.hasFlag(flag))
					{
						player.sendMessage(iZone.getPrefix() + phrase("flag_gamemode_default"));
						player.sendMessage(iZone.getPrefix() + phrase("flag_gamemode_values"));
						player.sendMessage(iZone.getPrefix() + phrase("flag_gamemode_help", "/zmod flag " + zone.getName() + " gamemode YourGamemode"));
						zone.setGamemode(GameMode.SURVIVAL);
					}

					if (flag.getName().equalsIgnoreCase("welcome") && zone.hasFlag(flag))
					{
						player.sendMessage(iZone.getPrefix() + phrase("flag_welcome_default"));
						player.sendMessage(iZone.getPrefix() + phrase("flag_welcome_help", "/zmod flag " + zone.getName() + " welcome YourMessage"));
						zone.setWelcome("Welcome to my zone");
					}

					if (flag.getName().equalsIgnoreCase("farewell") && zone.hasFlag(flag))
					{
						player.sendMessage(iZone.getPrefix() + phrase("flag_farewell_default"));
						player.sendMessage(iZone.getPrefix() + phrase("flag_farewell_help", "/zmod flag " + zone.getName() + " farewell YourMessage"));
						zone.setFarewell("See you soon");
					}

					player.sendMessage(iZone.getPrefix() + phrase("flag_set", flag.getName(), (zone.hasFlag(flag) ? ChatColor.GREEN + "" + ChatColor.BOLD + "ON" : ChatColor.RED + "" + ChatColor.BOLD + "OFF")));
					event.getInventory().setItem(event.getSlot(), new ItemBuilder(Material.SIGN).setTitle(ChatColor.WHITE + "" + ChatColor.BOLD + flag.getName()).addLore(ChatColor.GREEN + "" + ChatColor.BOLD + (zone.hasFlag(flag) ? "ON" : ChatColor.RED + "" + ChatColor.BOLD + "OFF")).build());

					player.updateInventory();
				}
			}
		};

		membersMenuListener = (player, action, event) ->
		{
			ItemStack memberItem = event.getCurrentItem();
			if (memberItem == null || memberItem.getType() == Material.AIR)
			{
				return;
			}

			String target = ChatColor.stripColor(memberItem.getItemMeta().getDisplayName());

			if (zone.getAllowed().contains(target))
			{
				if (ConfigManager.isVaultEnabled())
				{
					Economy vault = VaultManager.instance;

					if (vault.has(Bukkit.getOfflinePlayer(player.getUniqueId()), ConfigManager.getDisallowPlayerPrice()))
					{
						vault.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), ConfigManager.getDisallowPlayerPrice());
					}
					else
					{
						player.sendMessage(iZone.getPrefix() + phrase("notenough_money", vault.format(ConfigManager.getDisallowPlayerPrice())));
						return;
					}
				}
				zone.Remove(target);
				event.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
				player.updateInventory();
				player.sendMessage(iZone.getPrefix() + phrase("zone_removeuser", target));
			}
			else
			{
				player.sendMessage(iZone.getPrefix() + phrase("zone_cantremoveuser"));
			}
		};
	}

	public static zmodCommand getInstance() {
		return instance;
	}

	public ArrayList<zmodBase> getComs() {
		return this.coms;
	}

	public void onPlayerCommand(Player player, String[] cmd) {
		if (cmd.length == 1)
		{
			ArrayList<Zone> zones = ZoneManager.getZones().stream().filter(zone -> zone.getOwners().contains(player.getName())).collect(Collectors.toCollection(ArrayList::new));

			ArrayList<ItemStack> items = new ArrayList<>();
			for (int i = 0; i < zones.size(); i++)
			{
				items.add(new ItemBuilder(Variables.getMyHouseItem()).setTitle(ChatColor.WHITE + "" + ChatColor.BOLD + zones.get(i).getName()).addLore("§8", ChatColor.GRAY + "" + ChatColor.BOLD + "[LEFT CLICK]" + ChatColor.GREEN + " To manage the zone.", ChatColor.GRAY + "" + ChatColor.BOLD + "[RIGHT CLICK]" + ChatColor.GREEN + " To teleport to the zone border.").build());
			}

			PageInventory pageInventory = new PageInventory("iZone - v" + plugin.getDescription().getVersion(), items);
			pageInventory.show(player);

			pageInventory.onInteract((player1, action, event) ->
			{
				ItemStack item = pageInventory.getInventory().getItem(event.getSlot());
				if (item == null || item.getType() == Material.AIR || item.getType() == Material.STAINED_GLASS_PANE)
				{
					return;
				}

				int newPage = 0;
				if (item.equals(pageInventory.getBackPage()))
				{
					newPage = -1;
				}
				else if (item.equals(pageInventory.getForwardsPage()))
				{
					newPage = 1;
				}
				if (newPage != 0)
				{
					pageInventory.setPage(pageInventory.getCurrentPage() + newPage);
					return;
				}

				zone = ZoneManager.getZone(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
				if (zone == null) return;

				if(action == ClickType.RIGHT)
				{
					Location loc = zone.getTeleport();
					if(loc == null)
					{
						player.sendMessage(iZone.getPrefix() + phrase("zone_teleport_not_set"));
						return;
					}

					if(!isSafeLocation(loc))
					{
						player.teleport(loc);
					}
					else
					{
						player.teleport(player.getWorld().getHighestBlockAt(loc.getBlockX(), loc.getBlockZ()).getLocation());
					}
					player.closeInventory();
					return;
				}

				InventoryMenuBuilder imb = new InventoryMenuBuilder(9, "Zone Management");
				imb.withItem(0, new ItemBuilder(Material.SIGN).setTitle(ChatColor.WHITE + "" + ChatColor.BOLD + "Flags").addLore(ChatColor.GREEN + "" + ChatColor.BOLD + "Click here to set flags").build());
				imb.withItem(4, new ItemBuilder(Material.SKULL_ITEM, (short) 3).setTitle(ChatColor.WHITE + "" + ChatColor.BOLD + "Allowed players").addLore(ChatColor.GREEN + "" + ChatColor.BOLD + "Click here to add members to this zone.").build());

				if (iZone.serverVersion.newerThan(net.techguard.izone.Minecraft.Version.v1_8_R1))
				{
					imb.withItem(8, new ItemBuilder(Material.BARRIER).setTitle(ChatColor.RED + "" + ChatColor.BOLD + "Delete zone").addLore(ChatColor.RED + "" + ChatColor.BOLD + "Click here to remove this zone").build());
				}
				else
				{
					imb.withItem(8, new ItemBuilder(Material.LAVA_BUCKET).setTitle(ChatColor.RED + "" + ChatColor.BOLD + "Delete zone").addLore(ChatColor.RED + "" + ChatColor.BOLD + "Click here to remove this zone").build());
				}
				imb.show(player);
				imb.onInteract(settingsMenuListener, ClickType.LEFT);
			}, ClickType.LEFT, ClickType.RIGHT);

			player.getInventory().addItem(Variables.getMyHouseItem());
			player.sendMessage(iZone.getPrefix() + phrase("chat_help", "/zmod help"));
		}
		else
		{
			for (zmodBase zmod : this.coms)
			{
				if (zmod.getInfo()[0].equalsIgnoreCase(cmd[1]))
				{
					boolean permission = player.hasPermission(zmod.getPermission());
					if ((zmod instanceof listCommand))
					{
						permission = (permission) || (player.hasPermission(Variables.PERMISSION_LIST_ALL));
					}
					if (permission)
					{
						if (cmd.length < zmod.getLength())
						{
							if ((zmod instanceof flagCommand))
							{
								player.sendMessage(((flagCommand) zmod).getError(player, cmd.length));
							}
							else
							{
								player.sendMessage(zmod.getError(cmd.length));
							}
						}
						else
						{
							zmod.onCommand(player, cmd);
						}
					}
					else
					{
						player.sendMessage(iZone.getPrefix() + phrase("chat_nopermission"));
					}
				}
			}
		}
	}

	public void onSystemCommand(ConsoleCommandSender player, String[] cmd) {
		player.sendMessage(phrase("only_ingame"));
	}

	protected String[] getUsage() {
		return new String[]{"zmod"};
	}

	private boolean isSafeLocation(Location location) {

		Block feet = location.getBlock();
		if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
			return false;
		}

		Block head = feet.getRelative(BlockFace.UP);
		if (!head.getType().isTransparent()) {
			return false;
		}

		Block ground = feet.getRelative(BlockFace.DOWN);
		if (!ground.getType().isSolid()) {
			return false;
		}
		return true;
	}
}