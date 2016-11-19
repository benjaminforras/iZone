package net.techguard.izone.managers;

import net.techguard.izone.iZone;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PvPManager {

	public static boolean onPlayerAttack(Player defender, Player damager) {
		if ((damager != null) && (defender != null))
		{
			if ((damager.isOp()) && (!defender.isOp()))
			{
				return false;
			}
		}
		Zone defend_zone = ZoneManager.getZone(defender.getLocation());
		Zone attack_zone = ZoneManager.getZone(damager.getLocation());
		if (((defend_zone != null) && (defend_zone.hasFlag(Flags.PVP))) || ((attack_zone != null) && (attack_zone.hasFlag(Flags.PVP))))
		{
			damager.sendMessage(iZone.getPrefix() + ChatColor.RED + "You can't PVP here!");
			return true;
		}
		return false;
	}
}