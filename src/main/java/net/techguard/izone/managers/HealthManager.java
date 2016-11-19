package net.techguard.izone.managers;

import net.techguard.izone.Minecraft;
import net.techguard.izone.configuration.ConfigManager;
import net.techguard.izone.iZone;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HealthManager implements Runnable {

	private int healing = 0;
	private int hurting = 0;
	private JavaPlugin plugin;

	public HealthManager(JavaPlugin plugin) {
		this.plugin = plugin;
		this.healing = ConfigManager.getHealingTime();
		this.hurting = ConfigManager.getHurtingTime();
	}

	@Override
	public void run() {
		if (--this.healing < 0)
		{
			this.healing = ConfigManager.getHealingTime();
		}
		if (--this.hurting < 0)
		{
			this.hurting = ConfigManager.getHurtingTime();
		}
		for (Player p2 : plugin.getServer().getOnlinePlayers())
		{
			Zone zone = ZoneManager.getZone(p2.getLocation());
			if (zone != null)
			{
				if ((this.healing == 0) && (zone.hasFlag(Flags.HEAL)) && (p2.getGameMode().equals(GameMode.SURVIVAL) || p2.getGameMode().equals(GameMode.ADVENTURE)))
				{
					int health = (int) (p2.getHealth() + ConfigManager.getHealingAmount());
					if (health < 0)
					{
						health = 0;
					}
					p2.setHealth(health > 20 ? 20 : health);
				}
				if ((this.hurting == 0) && (zone.hasFlag(Flags.HURT)) && (p2.getGameMode().equals(GameMode.SURVIVAL) || p2.getGameMode().equals(GameMode.ADVENTURE)))
				{
					p2.damage(ConfigManager.getHurtingAmount());

					if (iZone.serverVersion.newerThan(Minecraft.Version.v1_8_R1))
					{
						p2.playSound(p2.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 0);
					}
				}
			}
		}
	}
}