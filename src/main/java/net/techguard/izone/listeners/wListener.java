package net.techguard.izone.Listeners;

import net.techguard.izone.Managers.ZoneManager;
import net.techguard.izone.Zones.Flags;
import net.techguard.izone.Zones.Zone;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

public class wListener implements Listener {
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onLightningStrike(LightningStrikeEvent event) {
		LightningStrike strike = event.getLightning();
		if (ZoneManager.IsDisabledWorld(strike.getWorld())) {
			return;
		}

		Zone            zone   = ZoneManager.getZone(strike.getLocation());

		if ((zone != null) && (zone.hasFlag(Flags.LIGHTNING)))
		{
			event.setCancelled(true);
		}
	}
}