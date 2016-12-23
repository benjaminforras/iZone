package net.techguard.izone.Zones;

import net.techguard.izone.Variables;
import net.techguard.izone.Managers.ZoneManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Settings {
	private static final HashMap<String, Settings> setts = new HashMap<>();
	private Player   player;
	private Location border1;
	private Location border2;

	private Settings(Player arg0, Location arg1, Location arg2) {
		this.player = arg0;
		this.border1 = arg1;
		this.border2 = arg2;
	}

	public static Settings getSett(Player player) {
		if (!setts.containsKey(player.getName()))
		{
			Settings sett = new Settings(player, null, null);
			setts.put(player.getName(), sett);
			return sett;
		}
		Settings sett = setts.get(player.getName());
		sett.setPlayer(player);
		return sett;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player a) {
		this.player = a;
	}

	public Location getBorder1() {
		if ((this.border1 != null) && (this.border2 != null))
		{
			World world = this.border1.getWorld();
			int   x0    = this.border1.getBlockX();
			int   y0    = this.border1.getBlockY();
			int   z0    = this.border1.getBlockZ();

			int x1 = this.border2.getBlockX();
			int y1 = this.border2.getBlockY();
			int z1 = this.border2.getBlockZ();

			return new Location(world, Math.min(x0, x1), Math.min(y0, y1), Math.min(z0, z1));
		}
		return this.border1;
	}

	public Location getBorder2() {
		if ((this.border1 != null) && (this.border2 != null))
		{
			int x0 = this.border1.getBlockX();
			int y0 = this.border1.getBlockY();
			int z0 = this.border1.getBlockZ();

			World world = this.border2.getWorld();
			int   x1    = this.border2.getBlockX();
			int   y1    = this.border2.getBlockY();
			int   z1    = this.border2.getBlockZ();

			return new Location(world, Math.max(x0, x1), Math.max(y0, y1), Math.max(z0, z1));
		}
		return this.border2;
	}

	public int getOwnedZones() {
		int owned = 0;
		for (Zone zone : ZoneManager.getZones())
		{
			for (String s : zone.getOwners())
			{
				if (!s.equalsIgnoreCase(this.player.getName()))
				{
					continue;
				}
				owned++;
			}
		}

		return owned;
	}

	public int getMaxZones() {
		int max = -1;

		for (String perm : Variables.PERMISSION_MAX_ZONE.keySet())
		{
			if (this.player.hasPermission(perm))
			{
				int size = Variables.PERMISSION_MAX_ZONE.get(perm);
				if ((size != -1) && (size <= max))
				{
					continue;
				}
				max = size;
			}
		}
		return max;
	}

	public Vector getMaxSize() {
		Vector max = new Vector(-1, -1, -1);

		for (String perm : Variables.PERMISSION_MAX_SIZE.keySet())
		{
			if (this.player.hasPermission(perm))
			{
				max = Variables.PERMISSION_MAX_SIZE.get(perm);
			}
		}
		return max;
	}

	public void setBorder(int a, Location b) {
		if (a == 1)
		{
			this.border1 = b;
		}
		else if (a == 2)
		{
			this.border2 = b;
		}
	}
}