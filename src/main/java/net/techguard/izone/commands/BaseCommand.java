package net.techguard.izone.Commands;

import net.techguard.izone.iZone;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("ALL")
public abstract class BaseCommand {
	public final iZone plugin;

	public BaseCommand(iZone instance) {
		this.plugin = instance;
	}

	public abstract void onPlayerCommand(Player paramPlayer, String[] paramArrayOfString);

	@SuppressWarnings("UnusedParameters")
	public abstract void onSystemCommand(ConsoleCommandSender paramConsoleCommandSender, String[] paramArrayOfString);

	protected abstract String[] getUsage();
}