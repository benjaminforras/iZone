package net.techguard.izone.managers;

import net.techguard.izone.configuration.ConfigManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Settings;
import net.techguard.izone.zones.Zone;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class ZoneManager {
	private static final ArrayList<Zone> Zones = new ArrayList<>();

	public static ArrayList<Zone> getZones() {
		return Zones;
	}

	public static Zone add(String a, Location[] c) {
		Zone zone = new Zone(a);
		zone.setSave(true);

		zone.setBorder(1, c[0]);
		zone.setBorder(2, c[1]);
		zone.setCreationDate(System.currentTimeMillis());
		for (int i = 0; i < ConfigManager.getAutoFlags().length; i++)
		{
			if (!ConfigManager.getAutoFlags()[i])
			{
				continue;
			}
			zone.setFlag(i, true);
		}

		Zones.add(zone);
		return zone;
	}

	public static void add(Zone a) {
		Zones.add(a);
	}

	public static void delete(Zone a) {
		Zones.remove(a);
	}

	public static Zone getZone(String a) {
		for (Zone b : Zones)
		{
			if (b.getName().equalsIgnoreCase(a))
			{
				return b;
			}
		}
		return null;
	}

	public static Zone getZone(Location a) {
		int             X     = (int) a.getX();
		int             Y     = (int) a.getY();
		int             Z     = (int) a.getZ();
		ArrayList<Zone> found = new ArrayList<Zone>();

		for (Zone zone : Zones)
		{
			if (zone.getWorld() != a.getWorld())
			{
				continue;
			}
			if ((X < zone.getBorder1().getX()) || (X > zone.getBorder2().getX()) || (Y < zone.getBorder1().getY()) ||
			    (Y > zone.getBorder2().getY()) || (Z < zone.getBorder1().getZ()) || (Z > zone.getBorder2().getZ()))
			{
				continue;
			}
			found.add(zone);
		}

		if (!found.isEmpty())
		{
			Zone[] sorted = found.toArray(new Zone[found.size()]);
			Arrays.sort(sorted, new ZoneComparator<Zone>());

			if (sorted.length > 0)
			{
				return sorted[0];
			}
		}
		return null;
	}

	public static Zone getZone(World a, int b, int c, int d) {
		return getZone(new Location(a, b, c, d));
	}

	public static boolean checkPermission(Zone a, Player b, Flags flag) {
		return checkGeneral(a, b) || !a.hasFlag(flag);

	}

	private static boolean checkGeneral(Zone a, Player b) {
		return b.hasPermission("izone.zone.restriction.ignoreowner") || a.getOwners().contains(b.getName()) || a.getAllowed().contains(b.getName());
	}

	public static boolean checkZoneInside(Location[] zone) {
		if (zone.length != 2)
		{
			return true;
		}
		int xMin = zone[0].getBlockX();
		int xMax = zone[1].getBlockX();
		int zMin = zone[0].getBlockZ();
		int zMax = zone[1].getBlockZ();
		if (xMin > xMax)
		{
			int tmp = xMin;
			xMin = xMax;
			xMax = tmp;
		}
		if (zMin > zMax)
		{
			int tmp = zMin;
			zMin = zMax;
			zMax = tmp;
		}
		for (Zone oZone : ZoneManager.Zones)
		{
			int oXMin = oZone.getBorder1().getBlockX();
			int oXMax = oZone.getBorder2().getBlockX();
			int oZMin = oZone.getBorder1().getBlockZ();
			int oZMax = oZone.getBorder2().getBlockZ();
			if (((oXMin >= xMin && oXMin <= xMax) || (oXMax >= xMin && oXMax <= xMax)) && ((oZMin >= zMin && oZMin <= zMax) || (oZMax >= zMin && oZMax <= zMax)))
			{
				return true;
			}
		}
		return false;
	}

	public static String canBuildZone(Player player, Location[] border) {
		if (checkOwnsPermission(player))
		{
			return "max";
		}
		return checkSizePermission(player, border);
	}

	private static boolean checkOwnsPermission(Player player) {
		Settings sett = Settings.getSett(player);

		return (sett.getOwnedZones() >= sett.getMaxZones()) && (sett.getMaxZones() != -1);
	}

	public static String checkSizePermission(Player player, Location[] border) {
		Settings sett = Settings.getSett(player);

		Location min = new Location(border[0].getWorld(), Math.min(border[0].getX(), border[1].getX()), Math.min(border[0].getY(), border[1].getY()), Math.min(border[0].getZ(), border[1].getZ()));
		Location max = new Location(border[1].getWorld(), Math.max(border[0].getX(), border[1].getX()), Math.max(border[0].getY(), border[1].getY()), Math.max(border[0].getZ(), border[1].getZ()));

		int    sizeX   = max.getBlockX() - min.getBlockX() + 1;
		int    sizeZ   = max.getBlockZ() - min.getBlockZ() + 1;
		int    sizeY   = max.getBlockY() - min.getBlockY() + 1;
		Vector maxSize = sett.getMaxSize();

		if (((sizeX > maxSize.getX()) && (maxSize.getX() != -1.0D)) ||
		    ((sizeZ > maxSize.getZ()) && (maxSize.getZ() != -1.0D)) || ((sizeY > maxSize.getY()) && (maxSize.getY() != -1.0D)))
		{
			return "size:(" + sizeX + ", " + sizeY + ", " + sizeZ + ")";
		}
		return "";
	}
}