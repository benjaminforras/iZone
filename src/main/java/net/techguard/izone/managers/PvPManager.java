package net.techguard.izone.Managers;

import net.techguard.izone.iZone;
import net.techguard.izone.Zones.Flags;
import net.techguard.izone.Zones.Zone;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static net.techguard.izone.Utils.Localization.I18n.tl;

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
			damager.sendMessage(iZone.getPrefix() + ChatColor.RED + tl("pvp_disabled"));
			return true;
		}
		return false;
	}
}