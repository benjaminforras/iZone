package net.techguard.izone.commands.zmod;

import net.milkbowl.vault.economy.Economy;
import net.techguard.izone.Variables;
import net.techguard.izone.configuration.ConfigManager;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.VaultManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Zone;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static net.techguard.izone.Phrases.phrase;

public class disallowCommand extends zmodBase {
	public disallowCommand(iZone instance) {
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
				player.sendMessage(iZone.getPrefix() + phrase("zone_notowner"));
				return;
			}
			if (zone.getAllowed().contains(target))
			{
				if (ConfigManager.isVaultEnabled())
				{
					Economy vault = VaultManager.instance;

					if (vault.has(Bukkit.getOfflinePlayer(player.getUniqueId()), ConfigManager.getDisallowPlayerPrice()))
					{
						vault.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), ConfigManager.getDisallowPlayerPrice());
					}
					else
					{
						player.sendMessage(iZone.getPrefix() + phrase("notenough_money", vault.format(ConfigManager.getDisallowPlayerPrice())));
						return;
					}
				}
				zone.Remove(target);
				player.sendMessage(iZone.getPrefix() + phrase("zone_removeuser", target));
			}
			else
			{
				player.sendMessage(iZone.getPrefix() + phrase("zone_cantremoveuser"));
			}
		}
		else
		{
			player.sendMessage(iZone.getPrefix() + phrase("zone_not_found"));
		}
	}

	public int getLength() {
		return 4;
	}

	public String[] getInfo() {
		return new String[]{"disallow", " <zone> <player>", "Remove player from zone list"};
	}

	public String getError(int i) {
		return "§cUsage: /zmod disallow <zone> <player>";
	}

	public String getPermission() {
		return Variables.PERMISSION_DISALLOW;
	}
}