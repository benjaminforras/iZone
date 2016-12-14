package net.techguard.izone.commands.zmod;

import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import static net.techguard.izone.Phrases.phrase;

public class flagCommand extends zmodBase {
	public flagCommand(iZone instance) {
		super(instance);
	}

	public void onCommand(Player player, String[] cmd) {
		String name = cmd[2];
		String flag = cmd[3];

		if (ZoneManager.getZone(name) != null)
		{
			Zone zone = ZoneManager.getZone(name);
			if ((!zone.getOwners().contains(player.getName())) && (!player.hasPermission(Variables.PERMISSION_OWNER)))
			{
				player.sendMessage(iZone.getPrefix() + phrase("zone_notowner"));
				return;
			}
			String text = "";
			if (cmd.length >= 5)
			{
				for (int i = 4; i < cmd.length; i++)
				{
					text = text + cmd[i] + " ";
				}
				if (text.endsWith(" "))
				{
					text = text.substring(0, text.length() - 1);
				}
			}

			for (Flags flag2 : Flags.values())
			{
				if (flag2.toString().toLowerCase().startsWith(flag.toLowerCase()))
				{
					if (!player.hasPermission(Variables.PERMISSION_FLAGS + flag2.toString()))
					{
						player.sendMessage(iZone.getPrefix() + phrase("zone_flag_no_permission"));
						return;
					}
					if ((flag2 == Flags.WELCOME) || (flag2 == Flags.FAREWELL))
					{
						if (text.length() > 0)
						{
							if (flag2 == Flags.WELCOME)
							{
								zone.setWelcome(text);
							}
							if (flag2 == Flags.FAREWELL)
							{
								zone.setFarewell(text);
							}
							player.sendMessage(iZone.getPrefix() + phrase("zone_flag_data"));
							if (zone.hasFlag(flag2))
							{
								return;
							}
						}
						else if (!zone.hasFlag(flag2))
						{
							player.sendMessage(iZone.getPrefix() + phrase("zone_flag_data_hint"));
						}
					}
					short data;
					int   amount;
					if ((flag2 == Flags.GIVEITEM_IN) || (flag2 == Flags.GIVEITEM_OUT) || (flag2 == Flags.TAKEITEM_IN) || (flag2 == Flags.TAKEITEM_OUT))
					{
						if (text.length() > 0)
						{
							if (text.startsWith("+ "))
							{
								ItemStack item = getItemStack(text = text.replaceFirst("\\+ ", ""));

								if (item != null)
								{
									zone.addInventory(flag2, item);
									Material type = item.getType();
									data = item.getData().getData();
									amount = item.getAmount();
									player.sendMessage(iZone.getPrefix() + phrase("zone_flag_data_add", flag2.getName(), (type == Material.AIR ? "All" : type.name()), (data == -1 ? "All" : Short.valueOf(data)), (amount == -1 ? "All" : Integer.valueOf(amount))));
								}
								else
								{
									player.sendMessage(iZone.getPrefix() + phrase("zone_flag_data_error", text));
								}
							}
							else if (text.startsWith("- "))
							{
								ItemStack item = getItemStack(text = text.replaceFirst("\\- ", ""));

								if (item != null)
								{
									zone.removeInventory(flag2, item);
									Material type = item.getType();
									data = item.getData().getData();
									amount = item.getAmount();
									player.sendMessage(iZone.getPrefix() + phrase("zone_flag_data_remove", flag2.getName(), (type == Material.AIR ? "All" : type.name()), (data == -1 ? "All" : Short.valueOf(data)), (amount == -1 ? "All" : Integer.valueOf(amount))));
								}
								else
								{
									player.sendMessage(iZone.getPrefix() + phrase("zone_flag_data_error", text));
								}
							}
							else
							{
								player.sendMessage(iZone.getPrefix() + phrase("zone_flag_data_error2"));
							}
							return;
						}
						if (!zone.hasFlag(flag2))
						{
							player.sendMessage(iZone.getPrefix() + phrase("zone_flag_data_hint2"));
						}
					}
					if (flag2 == Flags.GAMEMODE)
					{
						if (text.length() > 0)
						{
							GameMode   gm = null;
							GameMode[] arrayOfGameMode;
							amount = (arrayOfGameMode = GameMode.values()).length;
							for (data = 0; data < amount; data++)
							{
								GameMode mode = arrayOfGameMode[data];
								if (mode.name().toLowerCase().startsWith(text.toLowerCase()))
								{
									gm = mode;
								}
							}
							if (gm != null)
							{
								zone.setGamemode(gm);
								player.sendMessage(iZone.getPrefix() + phrase("zone_flag_gamemode_change", gm.name().toLowerCase()));
								if (zone.hasFlag(flag2))
								{
									return;
								}
							}
							else
							{
								player.sendMessage(iZone.getPrefix() + phrase("zone_flag_gamemode_error"));
							}
						}
						else if (!zone.hasFlag(flag2))
						{
							player.sendMessage(iZone.getPrefix() + phrase("zone_flag_gamemode_hint"));
						}
					}

					if(flag2 == Flags.TELEPORT)
					{
						Location location = player.getLocation();

						Border border = new Border(zone.getBorder1().toVector(), zone.getBorder2().toVector());
						if(!border.contains(player.getLocation())) {
							player.sendMessage(iZone.getPrefix() + ChatColor.RED + phrase("flag_teleport_not_in_zone"));
							return;
						}

						if(isSafeLocation(location))
						{
							zone.setTeleport(location);
							player.sendMessage(iZone.getPrefix() + ChatColor.GOLD + phrase("flag_teleport_set"));
							if (zone.hasFlag(flag2))
							{
								return;
							}
						}
						else player.sendMessage(iZone.getPrefix() + ChatColor.RED + phrase("flag_teleport_notsafe"));
					}

					zone.setFlag(flag2.getId(), !zone.hasFlag(flag2));
					player.sendMessage(iZone.getPrefix() + phrase("flag_set", flag2.getName(), (zone.hasFlag(flag2) ? ChatColor.GREEN + "" + ChatColor.BOLD + "ON" : ChatColor.RED + "" + ChatColor.BOLD + "OFF")));
				}
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
		return new String[]{"flag", " <zone> <flag> [flag data]", "Toggle a zone flag"};
	}

	public String getError(int i) {
		return getError(null, i);
	}

	@SuppressWarnings({"SameReturnValue", "UnusedParameters"})
	public String getError(Player player, int i) {
		if (player != null)
		{
			String flags = "";
			for (Flags f : Flags.values())
			{
				if (player.hasPermission(Variables.PERMISSION_FLAGS + f.toString()))
				{
					flags = flags + "§f" + f.toString() + "§c, ";
				}
			}
			if (flags.endsWith("§c, "))
			{
				flags = flags.substring(0, flags.length() - 4);
			}
			player.sendMessage("§cAvailable Flags: " + flags);
		}
		return "§cUsage: /zmod flag <zone> <flag>";
	}

	public String getPermission() {
		return Variables.PERMISSION_FLAG;
	}

	private ItemStack getItemStack(String text) {
		String item   = text;
		String data   = "";
		String amount = "";

		if (text.equals("*"))
		{
			return new ItemStack(Material.AIR, -1, (short) -1);
		}

		if (text.contains(","))
		{
			String[] split = item.split(",");
			item = split[0];
			amount = split[1];
		}
		if (item.contains(":"))
		{
			String[] split = item.split(":");
			item = split[0];
			data = split[1];
		}
		Material item0 = Material.matchMaterial(item);
		if ((item.equals("*")) || (item.equals("-1")))
		{
			item0 = Material.AIR;
		}
		if (item0 == null)
		{
			return null;
		}
		short data0   = 0;
		int   amount0 = 1;
		try
		{
			if ((data.equals("*")) || (data.equals("-1")))
			{
				data0 = -1;
			}
			else if (!data.equals(""))
			{
				data0 = (short) Integer.parseInt(data);
			}
		} catch (Exception ignored)
		{
		}
		try
		{
			if ((amount.equals("*")) || (amount.equals("-1")))
			{
				amount0 = -1;
			}
			else if (!amount.equals(""))
			{
				amount0 = Integer.parseInt(amount);
			}
		} catch (Exception ignored)
		{
		}
		return new ItemStack(item0, amount0, data0);
	}

	private boolean isSafeLocation(Location location) {

		Block feet = location.getBlock();
		if (!feet.getType().isTransparent() && !feet.getLocation().add(0, 1, 0).getBlock().getType().isTransparent()) {
			return false;
		}

		Block head = feet.getRelative(BlockFace.UP);
		if (!head.getType().isTransparent()) {
			return false;
		}

		Block ground = feet.getRelative(BlockFace.DOWN);
		if (!ground.getType().isSolid()) {
			return false;
		}
		return true;
	}

	public class Border {

		private Vector p1;
		private Vector p2;


		public Border(Vector p1, Vector p2) {
			int x1 = Math.min(p1.getBlockX(), p2.getBlockX());
			int y1 = Math.min(p1.getBlockY(), p2.getBlockY());
			int z1 = Math.min(p1.getBlockZ(), p2.getBlockZ());
			int x2 = Math.max(p1.getBlockX(), p2.getBlockX());
			int y2 = Math.max(p1.getBlockY(), p2.getBlockY());
			int z2 = Math.max(p1.getBlockZ(), p2.getBlockZ());
			this.p1 = new Vector(x1, y1, z1);
			this.p2 = new Vector(x2, y2, z2);
		}

		public boolean contains(Location loc) {
			if (loc == null)
			{
				return false;
			}
			return loc.getBlockX() >= p1.getBlockX() && loc.getBlockX() <= p2.getBlockX()
			       && loc.getBlockY() >= p1.getBlockY() && loc.getBlockY() <= p2.getBlockY()
			       && loc.getBlockZ() >= p1.getBlockZ() && loc.getBlockZ() <= p2.getBlockZ();
		}

	}
}