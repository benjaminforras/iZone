package net.techguard.izone.Commands.zmod;

import net.techguard.izone.Variables;
import net.techguard.izone.Configuration.ConfigManager;
import net.techguard.izone.iZone;
import net.techguard.izone.Managers.Visualizer;
import net.techguard.izone.Zones.Settings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import static net.techguard.izone.Utils.Localization.I18n.tl;

/**
 * Class:
 *
 * @author TryHardDood
 */
public class visualiseCommand extends zmodBase {
	public visualiseCommand(iZone instance) {
		super(instance);
	}

	public void onCommand(Player player, String[] cmd) {

		if (!ConfigManager.isParticlesEnabled())
		{
			if (player.isOp())
				player.sendMessage(iZone.getPrefix() + ChatColor.RED + tl("particles_not_enabled"));
			else player.sendMessage(iZone.getPrefix() + ChatColor.RED + tl("particles_disabled"));
			return;
		}

		Settings   settings = Settings.getSett(player);
		Location[] c        = {settings.getBorder1(), settings.getBorder2()};
		if ((c[0] == null) || (c[1] == null))
		{
			player.sendMessage(iZone.getPrefix() + tl("zone_create_border"));
			player.sendMessage(iZone.getPrefix() + tl("zone_create_hint", Material.getMaterial(ConfigManager.getDefineTool()).name()));
			return;
		}

		new Visualizer(player, c[0], c[1]).visualize();
	}

	public int getLength() {
		return 1;
	}

	public String[] getInfo() {
		return new String[]{"visualise", "Shows the selected area's borders"};
	}

	public String getError(int i) {
		return "§cUsage: /zmod visualise";
	}

	public String getPermission() {
		return Variables.PERMISSION_INFO;
	}
}
