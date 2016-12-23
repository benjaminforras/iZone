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

import static net.techguard.izone.Utils.Localization.I18n.tl;

public class allowCommand extends zmodBase {
	public allowCommand(iZone instance) {
		super(instance);
	}

	public void onCommand(Player player, String[] cmd) {
		String name   = cmd[2];
		String target = cmd[3];

		if (ZoneManager.getZone(name) != null)
		{
			Zone zone = ZoneManager.getZone(name);
			if ((!zone.getOwners().contains(player.getName())) && (!player.hasPermission(Variables.PERMISSION_OWNER)))
			{
				player.sendMessage(iZone.getPrefix() + tl("zone_notowner"));
				return;
			}
			if (!zone.getAllowed().contains(target))
			{
				if (ConfigManager.isVaultEnabled())
				{
					Economy vault = VaultManager.instance;

					if (vault.has(Bukkit.getOfflinePlayer(player.getUniqueId()), ConfigManager.getAllowPlayerPrice()))
					{
						vault.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), ConfigManager.getAllowPlayerPrice());
					}
					else
					{
						player.sendMessage(iZone.getPrefix() + tl("notenough_money", vault.format(ConfigManager.getAllowPlayerPrice())));
						return;
					}
				}
				zone.Add(target);
				player.sendMessage(iZone.getPrefix() + tl("zone_adduser", player));
			}
			else
			{
				player.sendMessage(iZone.getPrefix() + tl("zone_cantadduser"));
			}
		}
		else
		{
			player.sendMessage(iZone.getPrefix() + tl("zone_not_found"));
		}
	}

	public int getLength() {
		return 4;
	}

	public String[] getInfo() {
		return new String[]{"allow", " <zone> <player>", "Add player to zone list"};
	}

	public String getError(int i) {
		return "§cUsage: /zmod allow <zone> <player>";
	}

	public String getPermission() {
		return Variables.PERMISSION_ALLOW;
	}
}