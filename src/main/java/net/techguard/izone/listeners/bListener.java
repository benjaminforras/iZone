package net.techguard.izone.listeners;

import net.techguard.izone.iZone;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static net.techguard.izone.Phrases.phrase;

public class bListener implements Listener {
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block  b      = event.getBlock();
		Zone   zone   = ZoneManager.getZone(b.getLocation());

		if ((zone != null) && (!ZoneManager.checkPermission(zone, player, Flags.PROTECTION)))
		{
			event.setCancelled(true);
			player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		Block  b      = event.getBlock();
		Zone   zone   = ZoneManager.getZone(b.getLocation());

		if (b.getType() == Material.SAND || b.getState().getType() == Material.GRAVEL)
		{
			World world = b.getWorld();
			int   x     = b.getX();
			int   z     = b.getZ();
			for (int i = b.getY() - 1; i > 0; i--)
			{
				if (world.getBlockAt(x, i, z).getType() != Material.AIR)
				{
					i++;
					zone = ZoneManager.getZone(world.getBlockAt(x, i + 1, z).getLocation());
					if ((zone != null) && (!ZoneManager.checkPermission(zone, player, Flags.PROTECTION)))
					{
						event.setCancelled(true);
						player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
					}
					break;
				}
			}
		}

		if ((zone != null) && (!ZoneManager.checkPermission(zone, player, Flags.PROTECTION)))
		{
			event.setCancelled(true);
			player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
		Block bl     = event.getBlock();
		Zone  zone   = ZoneManager.getZone(bl.getLocation());
		Zone  zoneIn = null;

		BlockFace dir = event.getDirection();
		for (Block b : event.getBlocks())
		{
			Location loc = b.getRelative(dir).getLocation();
			Zone     f   = ZoneManager.getZone(loc);
			if (f != null)
			{
				zoneIn = f;
				break;
			}
		}
		if ((zone != zoneIn) && (zoneIn != null) && (zoneIn.hasFlag(Flags.PROTECTION)))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockPistonRetract(BlockPistonRetractEvent event) {
		if (!event.isSticky())
		{
			return;
		}
		if (event.getBlocks().isEmpty())
		{
			return;
		}
		Block b      = event.getBlocks().get(0);
		Zone  zone   = ZoneManager.getZone(event.getBlock().getLocation());
		Zone  zoneIn = ZoneManager.getZone(b.getLocation());
		if ((zone != zoneIn) && (zoneIn != null) && (zoneIn.hasFlag(Flags.PROTECTION)))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBurn(BlockBurnEvent event) {
		Block b    = event.getBlock();
		Zone  zone = ZoneManager.getZone(b.getLocation());

		if ((zone != null) && (zone.hasFlag(Flags.FIRE)))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPotionSplash(PotionSplashEvent event) {
		for (LivingEntity e : event.getAffectedEntities())
		{
			if (e instanceof Player)
			{
				Location loc  = e.getLocation();
				Zone     zone = ZoneManager.getZone(loc);

				if ((zone != null) && (zone.hasFlag(Flags.PVP)))
				{
					boolean doCancel = false;
					for (PotionEffect eff : event.getPotion().getEffects())
					{
						PotionEffectType effType = eff.getType();
						if ((effType.equals(PotionEffectType.BLINDNESS)) ||
						    (effType.equals(PotionEffectType.CONFUSION)) ||
						    (effType.equals(PotionEffectType.HARM)) ||
						    (effType.equals(PotionEffectType.POISON)) ||
						    (effType.equals(PotionEffectType.SLOW)) ||
						    (effType.equals(PotionEffectType.SLOW_DIGGING)))
						{
							doCancel = true;
							break;
						}
					}
					if (doCancel)
					{
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockIgnite(BlockIgniteEvent event) {
		Block                        b     = event.getBlock();
		BlockIgniteEvent.IgniteCause cause = event.getCause();
		Zone                         zone  = ZoneManager.getZone(b.getLocation());

		if (zone != null)
		{
			if (cause == BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL)
			{
				Player player = event.getPlayer();
				if (player == null)
				{
					if (event.getIgnitingBlock() == null)
					{
						return;
					}
					if (ZoneManager.getZone(event.getIgnitingBlock().getLocation()) != zone)
					{
						event.setCancelled(true);
						return;
					}
					return;
				}
				if (zone.hasFlag(Flags.PROTECTION))
				{
					if (!ZoneManager.checkPermission(zone, player, Flags.PROTECTION))
					{
						event.setCancelled(true);
					}
				}
				else if ((zone.hasFlag(Flags.FIRE)) && (!ZoneManager.checkPermission(zone, player, Flags.FIRE)))
				{
					event.setCancelled(true);
				}
				if (event.isCancelled())
				{
					player.sendMessage(iZone.getPrefix() + phrase("zone_protected"));
				}
			}
			else if (zone.hasFlag(Flags.FIRE))
			{
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockSpread(BlockSpreadEvent event) {
		Block    b        = event.getSource();
		Material newState = event.getNewState().getType();
		Zone     zone     = ZoneManager.getZone(b.getLocation());

		if ((zone != null) && (newState == Material.FIRE) && (zone.hasFlag(Flags.FIRE)))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockFade(BlockFadeEvent event) {
		Block      before = event.getBlock();
		BlockState after  = event.getNewState();
		Zone       zone   = ZoneManager.getZone(before.getLocation());

		if ((zone != null) && (zone.hasFlag(Flags.MELT)) && (((before.getType() == Material.SNOW) && (after.getType() == Material.AIR)) || ((before.getType() == Material.ICE) && (after.getType() == Material.STATIONARY_WATER))))
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onBlockFromTo(BlockFromToEvent event) {
		Block block = event.getBlock();
		if (!block.isLiquid())
		{
			return;
		}

		Zone zone   = ZoneManager.getZone(event.getToBlock().getLocation());
		Zone isZone = ZoneManager.getZone(block.getLocation());
		if (zone != null)
		{
			if (isZone != null && isZone == zone) return;

			if ((block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) && zone.hasFlag(Flags.WATER_FLOW))
			{
				event.setCancelled(true);
			}
			if ((block.getType() == Material.LAVA || block.getType() == Material.STATIONARY_LAVA) && zone.hasFlag(Flags.LAVA_FLOW))
			{
				event.setCancelled(true);
			}
		}
	}
}