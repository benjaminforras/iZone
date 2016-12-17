package net.techguard.izone.listeners;

import net.techguard.izone.configuration.ConfigManager;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.PvPManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static net.techguard.izone.Phrases.phrase;

public class eListener implements Listener {
	private final HashMap<String, ItemStack[][]> safeDeath = new HashMap<>();

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		Entity entity = event.getEntity();
		if (ConfigManager.useAsWhiteList())
		{
			if (!ConfigManager.containsWorld(entity.getWorld().getName()))
			{
				return;
			}
		}
		else
		{
			if (ConfigManager.containsWorld(entity.getWorld().getName()))
			{
				return;
			}
		}

		Zone   zone   = ZoneManager.getZone(event.getLocation());

		if (zone != null)
		{
			if ((entity instanceof Monster))
			{
				if (zone.hasFlag(Flags.MONSTER))
				{
					event.setCancelled(true);
				}
			}
			else if (((entity instanceof Animals)) && (zone.hasFlag(Flags.ANIMAL)))
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event) {
		Entity      entity = event.getEntity();
		if (ConfigManager.useAsWhiteList())
		{
			if (!ConfigManager.containsWorld(entity.getWorld().getName()))
			{
				return;
			}
		}
		else
		{
			if (ConfigManager.containsWorld(entity.getWorld().getName()))
			{
				return;
			}
		}

		List<Block> blocks = new ArrayList<Block>();
		for (Block b : event.blockList())
		{
			Zone zone = ZoneManager.getZone(b.getLocation());
			if ((zone == null) || ((!zone.hasFlag(Flags.EXPLOSION)) && ((!(entity instanceof Creeper)) || (!zone.hasFlag(Flags.CREEPER))) && ((!(entity instanceof TNTPrimed)) || (!zone.hasFlag(Flags.TNT)))))
			{
				continue;
			}
			blocks.add(b);
		}
		Block b;
		for (Iterator<Block> it = blocks.iterator(); it.hasNext(); event.blockList().remove(b))
		{
			b = it.next();
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		if (ConfigManager.useAsWhiteList())
		{
			if (!ConfigManager.containsWorld(event.getBlock().getWorld().getName()))
			{
				return;
			}
		}
		else
		{
			if (ConfigManager.containsWorld(event.getBlock().getWorld().getName()))
			{
				return;
			}
		}

		if ((event.isCancelled()) || (event.getEntityType() != EntityType.ENDERMAN))
		{
			return;
		}
		Block b    = event.getBlock();
		Zone  zone = ZoneManager.getZone(b.getLocation());

		if ((zone != null) && (zone.hasFlag(Flags.ENDERMAN)))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
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

		Zone   zone   = ZoneManager.getZone(player.getLocation());

		if (zone != null)
		{
			if ((zone.hasFlag(Flags.DEATHDROP)) || (zone.hasFlag(Flags.SAFEDEATH)))
			{
				event.getDrops().clear();
			}

			if (zone.hasFlag(Flags.SAFEDEATH))
			{
				this.safeDeath.put(player.getName(), new ItemStack[][]{player.getInventory().getArmorContents(), player.getInventory().getContents()});
				event.setDroppedExp(0);
				event.setKeepLevel(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
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

		if (this.safeDeath.containsKey(player.getName()))
		{
			ItemStack[][] drops = this.safeDeath.remove(player.getName());
			player.getInventory().setArmorContents(drops[0]);
			player.getInventory().setContents(drops[1]);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event) {
		Entity defender = event.getEntity();
		if (ConfigManager.useAsWhiteList())
		{
			if (!ConfigManager.containsWorld(defender.getWorld().getName()))
			{
				return;
			}
		}
		else
		{
			if (ConfigManager.containsWorld(defender.getWorld().getName()))
			{
				return;
			}
		}

		Zone   zone     = ZoneManager.getZone(defender.getLocation());

		if ((event instanceof EntityDamageByEntityEvent))
		{
			Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
			if ((defender instanceof Player))
			{
				if ((damager instanceof Arrow))
				{
					ProjectileSource projSource = ((Arrow) damager).getShooter();
					if ((projSource instanceof Player))
					{
						damager = (Player) projSource;
						if (PvPManager.onPlayerAttack((Player) defender, (Player) damager))
						{
							event.setCancelled(true);
							return;
						}
					}
				}
				if (((damager instanceof Player)) && (PvPManager.onPlayerAttack((Player) defender, (Player) damager)))
				{
					event.setCancelled(true);
					return;
				}
			}
		}

		if (zone != null)
		{
			if ((event instanceof EntityDamageByEntityEvent))
			{
				Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
				if (((defender instanceof Player)) && ((damager instanceof Monster)) && (zone.hasFlag(Flags.MONSTER)))
				{
					event.setCancelled(true);
					return;
				}
				if (((defender instanceof ItemFrame)) || ((defender instanceof ArmorStand)) ||
				    ((defender instanceof EnderCrystal)) || (((defender instanceof Villager)) && (zone.hasFlag(Flags.PROTECTION))))
				{
					boolean damagerIsOp = false;
					if ((damager instanceof Player))
					{
						Player pDmg = (Player) damager;
						damagerIsOp = ZoneManager.checkPermission(zone, pDmg, Flags.PROTECTION);
						if (!damagerIsOp) pDmg.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
					}
					if (!damagerIsOp)
					{
						event.setCancelled(true);
						return;
					}
				}
				if (((damager instanceof Snowball)) && (zone.hasFlag(Flags.PVP)))
				{
					event.setCancelled(true);
					return;
				}
				if (((damager instanceof FishHook)) && (zone.hasFlag(Flags.PVP)))
				{
					event.setCancelled(true);
					return;
				}
				if (((damager instanceof Egg)) && (zone.hasFlag(Flags.PVP)))
				{
					event.setCancelled(true);
					return;
				}
				if (((damager instanceof EnderPearl)) && (zone.hasFlag(Flags.PVP)))
				{
					EnderPearl ep = (EnderPearl) damager;
					if (ep.getShooter() != defender)
					{
						event.setCancelled(true);
						return;
					}
				}

				if (((damager instanceof TNTPrimed)) && (zone.hasFlag(Flags.TNT)))
				{
					event.setCancelled(true);
					return;
				}

				if (((damager instanceof Explosive)) && (zone.hasFlag(Flags.EXPLOSION)))
				{
					event.setCancelled(true);
					return;
				}
			}

			if (((defender instanceof Player)) && (zone.hasFlag(Flags.GOD)))
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onHangingBreak(HangingBreakEvent event) {
		if (ConfigManager.useAsWhiteList())
		{
			if (!ConfigManager.containsWorld(event.getEntity().getWorld().getName()))
			{
				return;
			}
		}
		else
		{
			if (ConfigManager.containsWorld(event.getEntity().getWorld().getName()))
			{
				return;
			}
		}

		if ((event instanceof HangingBreakByEntityEvent))
		{
			onHangingBreakByEntity((HangingBreakByEntityEvent) event);
			return;
		}
		Hanging h    = event.getEntity();
		Zone    zone = ZoneManager.getZone(h.getLocation());
		if ((zone != null) && (zone.hasFlag(Flags.PROTECTION)))
		{
			event.setCancelled(true);
		}
	}

	public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
		Entity  remover = event.getRemover();
		Hanging h       = event.getEntity();
		Zone    zone    = ZoneManager.getZone(h.getLocation());
		if ((remover instanceof Player))
		{
			Player player = (Player) remover;
			if ((zone != null) && (!ZoneManager.checkPermission(zone, player, Flags.PROTECTION)))
			{
				event.setCancelled(true);
				player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
			}
		}
		else if ((zone != null) && (zone.hasFlag(Flags.PROTECTION)))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onHangingPlace(HangingPlaceEvent event) {
		Player  player = event.getPlayer();
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

		Hanging h      = event.getEntity();
		Zone    zone   = ZoneManager.getZone(h.getLocation());
		if ((zone != null) && (!ZoneManager.checkPermission(zone, player, Flags.PROTECTION)))
		{
			event.setCancelled(true);
			player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityInteract(PlayerInteractEntityEvent event) {
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

		Entity entity = event.getRightClicked();
		if ((player == null) || (entity == null))
		{
			return;
		}
		if (((entity instanceof Painting)) || ((entity instanceof ItemFrame)) || ((entity instanceof ArmorStand)) || ((entity instanceof EnderCrystal)))
		{
			Zone zone = ZoneManager.getZone(entity.getLocation());
			if ((zone != null) && (!ZoneManager.checkPermission(zone, player, Flags.PROTECTION)))
			{
				event.setCancelled(true);
				player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onArmorStand(PlayerArmorStandManipulateEvent event) {
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

		Entity entity = event.getRightClicked();
		if ((player == null) || (entity == null))
		{
			return;
		}
		Zone zone = ZoneManager.getZone(entity.getLocation());
		if ((zone != null) && (!ZoneManager.checkPermission(zone, player, Flags.PROTECTION)))
		{
			event.setCancelled(true);
			player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onEntityBlockFormEvent(EntityBlockFormEvent event) {
		Entity player = event.getEntity();
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

		if ((player instanceof Player))
		{
			Zone zone = ZoneManager.getZone(player.getLocation());
			if ((zone != null) && (!ZoneManager.checkPermission(zone, (Player) player, Flags.PROTECTION)))
			{
				event.setCancelled(true);
			}
		}
	}
}