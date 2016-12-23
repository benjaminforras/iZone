package net.techguard.izone.Commands.zmod;

import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.Managers.ZoneManager;
import net.techguard.izone.Zones.Flags;
import net.techguard.izone.Zones.Zone;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Iterator;

import static net.techguard.izone.Utils.Localization.I18n.tl;

public class infoCommand extends zmodBase {
	public infoCommand(iZone instance) {
		super(instance);
	}

	public void onCommand(Player player, String[] cmd) {
		Zone zone = ZoneManager.getZone(cmd[2]);

		if (zone != null)
		{
			String flags   = "";
			String allowed = "";
			Flags  flag;
			for (Iterator<Flags> localIterator = zone.getFlags().iterator(); localIterator.hasNext(); flags = flags + "§f" + flag.getName() + "§b, ")
			{
				flag = localIterator.next();
			}
			if (flags.endsWith("§b, "))
			{
				flags = flags.substring(0, flags.length() - 4);
			}
			String s;
			for (Iterator<String> localIterator = zone.getAllowed().iterator(); localIterator.hasNext(); allowed = allowed + "§f" + s + "§b, ")
			{
				s = localIterator.next();
			}
			if (allowed.endsWith("§b, "))
			{
				allowed = allowed.substring(0, allowed.length() - 4);
			}

			player.sendMessage(iZone.getPrefix() + ChatColor.GREEN + tl("zone_found"));
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "////////////// " + ChatColor.GOLD + "[" + zone.getName() + "]" + ChatColor.GRAY + "" + ChatColor.BOLD + " //////////////");
			player.sendMessage(ChatColor.GRAY + tl("word_flags") + ": " + ChatColor.AQUA + flags);
			player.sendMessage(ChatColor.GRAY + tl("word_allowed") + ": " + ChatColor.AQUA + allowed);
		}
		else
		{
			player.sendMessage("§cZone not found");
		}
	}

	public int getLength() {
		return 3;
	}

	public String[] getInfo() {
		return new String[]{"info", " <name>", "Prints info about the zone"};
	}

	public String getError(int i) {
		return "§cUsage: /zmod info <name>";
	}

	public String getPermission() {
		return Variables.PERMISSION_INFO;
	}
}