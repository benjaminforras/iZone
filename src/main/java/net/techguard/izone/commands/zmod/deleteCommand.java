package net.techguard.izone.Commands.zmod;

import net.milkbowl.vault.economy.Economy;
import net.techguard.izone.Variables;
import net.techguard.izone.Configuration.ConfigManager;
import net.techguard.izone.iZone;
import net.techguard.izone.Managers.VaultManager;
import net.techguard.izone.Managers.ZoneManager;
import net.techguard.izone.Zones.Zone;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;

import static net.techguard.izone.Utils.Localization.I18n.tl;

public class deleteCommand extends zmodBase {
	public deleteCommand(iZone instance) {
		super(instance);
	}

	public void onCommand(Player player, String[] cmd) {
		String name = cmd[2];

		if (ZoneManager.getZone(name) != null)
		{
			Zone zone = ZoneManager.getZone(name);
			if ((!zone.getOwners().contains(player.getName())) && (!player.hasPermission(Variables.PERMISSION_OWNER)))
			{
				player.sendMessage(iZone.getPrefix() + tl("zone_notowner"));
				return;
			}
			if (ConfigManager.isVaultEnabled())
			{
				Economy vault = VaultManager.instance;

				if (vault.has(Bukkit.getOfflinePlayer(player.getUniqueId()), ConfigManager.getDeleteZonePrice()))
				{
					vault.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), ConfigManager.getDeleteZonePrice());
				}
				else
				{
					player.sendMessage(iZone.getPrefix() + tl("notenough_money", vault.format(ConfigManager.getDeleteZonePrice())));
					return;
				}
			}
			ZoneManager.delete(zone);
			File file = zone.getSaveFile();
			file.delete();
			player.sendMessage(iZone.getPrefix() + tl("zone_deleted", zone.getName()));
		}
		else
		{
			player.sendMessage(iZone.getPrefix() + tl("zone_not_found"));
		}
	}

	public int getLength() {
		return 3;
	}

	public String[] getInfo() {
		return new String[]{"delete", " <name>", "Delete a zone"};
	}

	public String getError(int i) {
		return "§cUsage: /zmod delete <name>";
	}

	public String getPermission() {
		return Variables.PERMISSION_DELETE;
	}
}