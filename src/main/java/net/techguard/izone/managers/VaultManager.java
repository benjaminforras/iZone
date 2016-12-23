package net.techguard.izone.Managers;

import net.milkbowl.vault.economy.Economy;
import net.techguard.izone.iZone;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultManager {
	public static  Economy instance = null;
	private static boolean setup    = false;
	private final iZone plugin;

	private VaultManager(iZone instance) {
		this.plugin = instance;

		if (this.plugin.getServer().getPluginManager().isPluginEnabled("Vault"))
		{
			setup = setupEconomy();
		}
	}

	public static void load(iZone instance) {
		new VaultManager(instance);
	}

	public static boolean isEnabled() {
		return setup;
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = this.plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null)
		{
			instance = economyProvider.getProvider();
		}

		return instance != null;
	}
}