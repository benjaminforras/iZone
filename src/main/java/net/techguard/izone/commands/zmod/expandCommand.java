package net.techguard.izone.commands.zmod;

import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Settings;
import net.techguard.izone.zones.Zone;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static net.techguard.izone.Phrases.phrase;

public class expandCommand extends zmodBase {
	public expandCommand(iZone instance) {
		super(instance);
	}

	public void onCommand(Player player, String[] cmd) {
		String name = cmd[2];

		if (ZoneManager.getZone(name) != null)
		{
			Zone zone = ZoneManager.getZone(name);
			if ((!zone.getOwners().contains(player.getName())) && (!player.hasPermission(Variables.PERMISSION_OWNER)))
			{
				player.sendMessage(iZone.getPrefix() + phrase("zone_notowner"));
				return;
			}
			BlockFace  dir          = BlockFace.SELF;
			Location[] newerBorders = {zone.getBorder1(), zone.getBorder2()};
			Integer    size         = Integer.parseInt(cmd[3]);

			if (cmd[4].equalsIgnoreCase("up"))
			{
				dir = BlockFace.UP;
			}
			else if (cmd[4].equalsIgnoreCase("down"))
			{
				dir = BlockFace.DOWN;
			}
			else if (cmd[4].equalsIgnoreCase("north"))
			{
				dir = BlockFace.NORTH;
			}
			else if (cmd[4].equalsIgnoreCase("east"))
			{
				dir = BlockFace.EAST;
			}
			else if (cmd[4].equalsIgnoreCase("south"))
			{
				dir = BlockFace.SOUTH;
			}
			else if (cmd[4].equalsIgnoreCase("west"))
			{
				dir = BlockFace.WEST;
			}
			else
			{
				player.sendMessage(getError(0));
			}
			if (dir == BlockFace.UP)
			{
				int i = zone.getBorder2().getBlockY() + size;
				if (i > zone.getWorld().getMaxHeight())
				{
					i = zone.getWorld().getMaxHeight();
					size = size - (i - zone.getWorld().getMaxHeight());
				}
				Location cur = zone.getBorder2().clone();
				cur.setY(i);
				newerBorders[1] = cur;
			}
			else if (dir == BlockFace.DOWN)
			{
				int i = zone.getBorder1().getBlockY() - size;
				if (i < 0)
				{
					i = 0;
					size = size - Math.abs(i);
				}
				Location cur = zone.getBorder1().clone();
				cur.setY(i);
				newerBorders[0] = cur;
			}
			else if (dir == BlockFace.NORTH)
			{
				int      i   = zone.getBorder1().getBlockX() - size;
				Location cur = zone.getBorder1().clone();
				cur.setX(i);
				newerBorders[0] = cur;
			}
			else if (dir == BlockFace.EAST)
			{
				int      i   = zone.getBorder1().getBlockZ() - size;
				Location cur = zone.getBorder1().clone();
				cur.setZ(i);
				newerBorders[0] = cur;
			}
			else if (dir == BlockFace.SOUTH)
			{
				int      i   = zone.getBorder2().getBlockX() + size;
				Location cur = zone.getBorder2().clone();
				cur.setX(i);
				newerBorders[1] = cur;
			}
			else if (dir == BlockFace.WEST)
			{
				int      i   = zone.getBorder2().getBlockZ() + size;
				Location cur = zone.getBorder2().clone();
				cur.setZ(i);
				newerBorders[1] = cur;
			}
			String permission = ZoneManager.checkSizePermission(player, newerBorders);
			if (permission.startsWith("size"))
			{
				Vector maxSize = Settings.getSett(player).getMaxSize();
				player.sendMessage(iZone.getPrefix() + phrase("zone_expand_big", permission.split(":")[1], (int) maxSize.getX(), (int) maxSize.getY(), (int) maxSize.getZ()));
				return;
			}
			zone.setBorder(1, newerBorders[0]);
			zone.setBorder(2, newerBorders[1]);
			player.sendMessage(iZone.getPrefix() + phrase("zone_expand_success", zone.getName(), size, dir));
		}
		else
		{
			player.sendMessage(iZone.getPrefix() + phrase("zone_not_found"));
		}
	}

	public int getLength() {
		return 5;
	}

	public String[] getInfo() {
		return new String[]{"expand", " <zone> <size> <direction>", "Expand the borders up or down"};
	}

	public String getError(int i) {
		return "§cUsage: /zmod expand <zone> <size> <direction>";
	}

	public String getPermission() {
		return Variables.PERMISSION_EXPAND;
	}
}