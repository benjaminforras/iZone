package net.techguard.izone;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class Variables {
	public static final String PERMISSION_DEFINE   = "izone.tool.define";
	public static final String PERMISSION_CHECK    = "izone.tool.check";
	public static final String PERMISSION_OWNER    = "izone.zone.restriction.ignoreowner";
	public static final String PERMISSION_ALLOW    = "izone.zone.allow";
	public static final String PERMISSION_CREATE   = "izone.zone.create";
	public static final String PERMISSION_DISALLOW = "izone.zone.disallow";
	public static final String PERMISSION_DELETE   = "izone.zone.delete";
	public static final String PERMISSION_EXPAND   = "izone.zone.expand";
	public static final String PERMISSION_FLAG     = "izone.zone.flag";
	public static final String PERMISSION_LIST     = "izone.zone.list";
	public static final String PERMISSION_LIST_ALL = "izone.zone.list.all";
	public static final String PERMISSION_INFO     = "izone.zone.info";
	public static final String PERMISSION_WHO      = "izone.zone.who";
	public static final String PERMISSION_PARENT   = "izone.zone.parent";
	public static final String PERMISSION_FLAGS    = "izone.zone.flag.";
	public static final String PERMISSION_FLY      = "izone.zone.fly.";
	public static final String RELOAD_FLAG         = "izone.reload";

	public static final HashMap<String, Vector>  PERMISSION_MAX_SIZE = new HashMap<>();
	public static final HashMap<String, Integer> PERMISSION_MAX_ZONE = new HashMap<>();

	private static ItemStack myHouseItem;

	public static ItemStack getMyHouseItem() {
		if (myHouseItem == null)
			myHouseItem = new ItemStack(Material.WOOD_DOOR);
		return myHouseItem;
	}
}