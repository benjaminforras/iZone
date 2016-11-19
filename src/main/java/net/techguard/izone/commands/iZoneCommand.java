package net.techguard.izone.commands;

import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.ZoneManager;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import static net.techguard.izone.Phrases.phrase;

public class iZoneCommand extends BaseCommand {
	public iZoneCommand(iZone instance) {
		super(instance);
	}

	public void onPlayerCommand(Player player, String[] cmd) {
		if (cmd.length == 2)
		{
			if (cmd[1].equalsIgnoreCase("reload"))
			{
				if (player.hasPermission(Variables.RELOAD_FLAG))
				{
					ZoneManager.getZones().clear();
					iZone.instance.reloadConfiguration();
					player.sendMessage(iZone.getPrefix() + phrase("chat_reload", iZone.instance.getDescription().getVersion()));
				}
				else
				{
					player.sendMessage(iZone.getPrefix() + phrase("chat_nopermission"));
				}
			}
		}
		player.sendMessage(iZone.getPrefix() + phrase("chat_version", this.plugin.getDescription().getName(), (this.plugin.getDescription().getAuthors().get(0) + ", " + this.plugin.getDescription().getAuthors().get(1))));
	}

	public void onSystemCommand(ConsoleCommandSender player, String[] cmd) {
		if (cmd.length == 2)
		{
			if (cmd[1].equalsIgnoreCase("reload"))
			{
				ZoneManager.getZones().clear();
				iZone.instance.reloadConfiguration();
				player.sendMessage(iZone.getPrefix() + phrase("chat_reload", iZone.instance.getDescription().getVersion()));
			}
		}
		player.sendMessage(iZone.getPrefix() + phrase("chat_version", this.plugin.getDescription().getName(), (this.plugin.getDescription().getAuthors().get(0) + ", " + this.plugin.getDescription().getAuthors().get(1))));
	}

	protected String[] getUsage() {
		return new String[]{"izone"};
	}
}