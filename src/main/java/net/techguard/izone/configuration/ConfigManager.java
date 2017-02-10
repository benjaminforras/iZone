package net.techguard.izone.Configuration;

import net.techguard.izone.Zones.Flags;
import org.bukkit.Bukkit;
import org.bukkit.Material;

/*****************************************************
 *              Created by TryHardDood on 2016. 11. 04..
 ****************************************************/
public class ConfigManager {

	public static  boolean[]    autoFlag    = new boolean[Flags.values().length];
	private static Configurable mainConfig  = Config.getConfig();
	private static Configurable vaultConfig = VaultConfig.getConfig();
	private static Configurable disabledWorldsConfig = DisabledWorldsConfig.getConfig();

	public static int getViewDistance() {
		return Bukkit.getViewDistance();
	}

	/**
	 * ==========
	 * GETTERS
	 * ==========
	 * <p>
	 * VAULT
	 */
	public static boolean isVaultEnabled() {
		return vaultConfig.get().getBoolean("enabled", false);
	}

	public static double getCreateZonePrice() {
		return vaultConfig.get().getDouble("create-zone", 0);
	}

	public static double getDeleteZonePrice() {
		return vaultConfig.get().getDouble("delete-zone", 0);
	}

	public static double getAllowPlayerPrice() {
		return vaultConfig.get().getDouble("allow-player", 0);
	}

	public static double getDisallowPlayerPrice() {
		return vaultConfig.get().getDouble("disallow-player", 0);
	}

	/**
	 * ==========
	 * GETTERS
	 * ==========
	 * <p>
	 * Main
	 */

	public static Integer getTitleFadeIn()
	{
		return mainConfig.get().getInt("messages.title.fadeIn", 2);
	}

	public static Integer getTitleStay()
	{
		return mainConfig.get().getInt("messages.title.stay", 20);
	}

	public static Integer getTitleFadeOut()
	{
		return mainConfig.get().getInt("messages.title.fadeOut", 2);
	}

	public static String getCheckTool() {
		return mainConfig.get().getString("tools.check", Material.WOOD_SWORD.toString());
	}

	public static String getDefineTool() {
		return mainConfig.get().getString("tools.define", Material.WOOD_SPADE.toString());
	}

	public static int getHealingTime() {
		return mainConfig.get().getInt("healing.time", 3);
	}

	public static int getHealingAmount() {
		return mainConfig.get().getInt("healing.amount", 1);
	}

	public static int getHurtingTime() {
		return mainConfig.get().getInt("hurting.time", 7);
	}

	public static int getHurtingAmount() {
		return mainConfig.get().getInt("hurting.amount", 2);
	}

	public static String getLocale() {
		return mainConfig.get().getString("locale", "en");
	}

	public static void setLocale(String locale) {
		mainConfig.get().set("locale", locale);
	}

	public static boolean getHealthListener() {
		return mainConfig.get().getBoolean("listeners.healthListener", true);
	}

	public static boolean[] getAutoFlags() {
		return autoFlag;
	}

	public static boolean isParticlesEnabled() {
		return mainConfig.get().getBoolean("particles.enabled", false);
	}

	public static String getParticle() {
		return mainConfig.get().getString("particles.particle", "FIREWORKS_SPARK");
	}


	public static boolean useAsWhiteList()
	{
		return disabledWorldsConfig.get().getBoolean("useAsWhitelist", false);
	}

	public static boolean containsWorld(String world)
	{
		return disabledWorldsConfig.get().getStringList("disabledWorlds").contains(world);
	}
}
