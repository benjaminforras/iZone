package net.techguard.izone.commands.zmod;

import net.techguard.izone.iZone;
import org.bukkit.entity.Player;

public abstract class zmodBase {
	private final iZone plugin;

	@SuppressWarnings("WeakerAccess")
	public zmodBase(iZone instance) {
		this.plugin = instance;
	}

	public abstract void onCommand(Player paramPlayer, String[] paramArrayOfString);

	public abstract int getLength();

	public abstract String[] getInfo();

	public abstract String getError(int paramInt);

	public abstract String getPermission();
}