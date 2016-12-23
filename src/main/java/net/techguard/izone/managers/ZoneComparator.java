package net.techguard.izone.Managers;

import net.techguard.izone.Zones.Zone;

import java.util.Comparator;

class ZoneComparator<T> implements Comparator<Zone> {
	public int compare(Zone z1, Zone z2) {
		if (z1.getCreationDate() < z2.getCreationDate())
		{
			return -1;
		}
		if (z1.getCreationDate() > z2.getCreationDate())
		{
			return 1;
		}
		return 0;
	}
}