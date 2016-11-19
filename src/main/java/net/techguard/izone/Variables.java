package net.techguard.izone;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

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
	private static ItemStack houseItem;

	public static ItemStack getMyHouseItem() {
		if (myHouseItem == null)
			myHouseItem = new ItemStack(Material.WOOD_DOOR);
					//getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTI3MTgwOWJhOTFiNDJmYjQ4NzVhZjRiYTI5OGU1ZTU1ZjQ1ZWQ3MzcyMWJjZWE4NWE0NWRiOTI2Mjg1NzRmIn19fQ==");
		return myHouseItem;
	}

	public static ItemStack getHouseItem() {
		if (houseItem == null)
			houseItem = getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzVhMzViNWNhMTUyNjg2ODVjNDY2MDUzNWU1ODgzZDIxYTVlYzU3YzU1ZDM5NzIzNDI2OWFjYjVkYzI5NTRmIn19fQ==");
		return houseItem;
	}

	private static ItemStack getSkull(String base64) {
		ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

		ItemMeta    skullMeta = skull.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties().put("textures", new Property("textures", base64));
		Field profileField = null;

		try
		{
			profileField = skullMeta.getClass().getDeclaredField("profile");
		} catch (NoSuchFieldException | SecurityException e)
		{
			e.printStackTrace();
		}

		profileField.setAccessible(true);
		try
		{
			profileField.set(skullMeta, profile);
		} catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		skull.setItemMeta(skullMeta);
		return skull;
	}
}