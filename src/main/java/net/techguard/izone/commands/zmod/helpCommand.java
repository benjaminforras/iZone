package net.techguard.izone.commands.zmod;

import net.techguard.izone.Variables;
import net.techguard.izone.commands.zmodCommand;
import net.techguard.izone.iZone;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Class:
 *
 * @author TryHardDood
 */
public class helpCommand extends zmodBase {
	public helpCommand(iZone instance) {
		super(instance);
	}

	public void onCommand(Player player, String[] cmd) {
		player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "////////////// " + ChatColor.GOLD + "[" + "iZone" + "]" + ChatColor.GRAY + "" + ChatColor.BOLD + " //////////////");
		for (zmodBase zmod : zmodCommand.getInstance().getComs())
		{
			boolean permission = player.hasPermission(zmod.getPermission());
			if ((zmod instanceof listCommand))
			{
				permission = (permission) || (player.hasPermission(Variables.PERMISSION_LIST_ALL));
			}
			if (permission)
			{
				String[] info = zmod.getInfo();
				if (info.length == 3)
				{
					player.sendMessage("§b/zmod " + info[0] + info[1] + " §f- " + info[2]);
				}
				else if (info.length == 2)
				{
					player.sendMessage("§b/zmod " + info[0] + " §f- " + info[1]);
				}
				else if (info.length == 1)
				{
					player.sendMessage("§b/zmod " + info[0]);
				}
			}
		}
	}

	public int getLength() {
		return 1;
	}

	public String[] getInfo() {
		return new String[]{"help", "Prints a list of commands"};
	}

	public String getError(int i) {
		return "§cUsage: /zmod help";
	}

	public String getPermission() {
		return Variables.PERMISSION_INFO;
	}
}
