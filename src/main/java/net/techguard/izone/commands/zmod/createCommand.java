package net.techguard.izone.Commands.zmod;

import net.milkbowl.vault.economy.Economy;
import net.techguard.izone.Variables;
import net.techguard.izone.Configuration.ConfigManager;
import net.techguard.izone.iZone;
import net.techguard.izone.Managers.VaultManager;
import net.techguard.izone.Managers.ZoneManager;
import net.techguard.izone.Zones.Settings;
import net.techguard.izone.Zones.Zone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static net.techguard.izone.Utils.Localization.I18n.tl;

public class createCommand extends zmodBase {
	public createCommand(iZone instance) {
		super(instance);
	}

	public void onCommand(Player player, String[] cmd) {
		Settings settings = Settings.getSett(player);
		String   name     = cmd[2];

		if (ZoneManager.getZone(name) == null)
		{
			Location[] c = {settings.getBorder1(), settings.getBorder2()};
			if ((c[0] == null) || (c[1] == null))
			{
				player.sendMessage(iZone.getPrefix() + tl("zone_create_border"));
				player.sendMessage(iZone.getPrefix() + tl("zone_create_hint", Material.getMaterial(ConfigManager.getDefineTool()).name()));
				return;
			}

			if (!player.isOp())
			{
				String permission = ZoneManager.canBuildZone(player, c);

				if (!permission.equals(""))
				{
					if (permission.equals("max"))
					{
						String size = settings.getOwnedZones() + "/" + settings.getMaxZones();
						player.sendMessage(iZone.getPrefix() + tl("zone_create_many", size));
					}
					if (permission.startsWith("size"))
					{
						Vector maxSize = settings.getMaxSize();
						player.sendMessage(iZone.getPrefix() + tl("zone_create_big", permission.split(":")[1], maxSize.getX(), maxSize.getY(), maxSize.getZ()));
					}
					return;
				}
			}

			if (ConfigManager.isVaultEnabled())
			{
				Economy vault = VaultManager.instance;

				if (vault.has(Bukkit.getOfflinePlayer(player.getUniqueId()), ConfigManager.getCreateZonePrice()))
				{
					vault.withdrawPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()), ConfigManager.getCreateZonePrice());
				}
				else
				{
					player.sendMessage(iZone.getPrefix() + tl("notenough_money", vault.format(ConfigManager.getCreateZonePrice())));
					return;
				}
			}

			if(c[0].getY() == c[1].getY())
			{
				String permission = ZoneManager.canBuildZone(player, c);
				//			size:(5, 5, 5)
				int maxHeight = Integer.valueOf(permission.split(", ")[1]);
				if(maxHeight == -1)
				{
					maxHeight = player.getWorld().getMaxHeight();
				}
				c[0].setY(maxHeight);
			}

			if (ZoneManager.checkZoneInside(c))
			{
				player.sendMessage(iZone.getPrefix() + tl("zone_create_error"));
				return;
			}

			Zone zone = ZoneManager.add(name, c);
			zone.Add("o:" + player.getName());

			player.sendMessage(iZone.getPrefix() + tl("zone_create_success", zone.getName()));
		}
		else
		{
			player.sendMessage(iZone.getPrefix() + tl("zone_create_error2"));
		}
	}

	public int getLength() {
		return 3;
	}

	public String[] getInfo() {
		return new String[]{"create", " <name>", "Create a new zone"};
	}

	public String getError(int i) {
		return "§cUsage: /zmod create <name>";
	}

	public String getPermission() {
		return Variables.PERMISSION_CREATE;
	}
}