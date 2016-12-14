package net.techguard.izone;

import net.techguard.izone.MenuBuilder.inventory.InventoryListener;
import net.techguard.izone.commands.CommandManager;
import net.techguard.izone.commands.iZoneCommand;
import net.techguard.izone.commands.zmodCommand;
import net.techguard.izone.configuration.Config;
import net.techguard.izone.configuration.ConfigManager;
import net.techguard.izone.configuration.Configurable;
import net.techguard.izone.configuration.VaultConfig;
import net.techguard.izone.listeners.bListener;
import net.techguard.izone.listeners.eListener;
import net.techguard.izone.listeners.pListener;
import net.techguard.izone.listeners.wListener;
import net.techguard.izone.managers.HealthManager;
import net.techguard.izone.managers.VaultManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static net.techguard.izone.Phrases.phrase;

public class iZone extends JavaPlugin {
	public static  iZone             instance;
	public static  Minecraft.Version serverVersion;
	private static Configurable      mainConfig;
	private static VaultConfig       vaultConfig;
	public         InventoryListener inventoryListener;
	private        CommandManager    commandManager;

	public static String getPrefix() {
		return ChatColor.GOLD + "" + ChatColor.BOLD + "iZone > " + ChatColor.GRAY;
	}

	@Override
	public void onEnable() {
		instance = this;

		serverVersion = Minecraft.Version.getVersion();
		if (serverVersion == Minecraft.Version.UNKNOWN)
		{
			getLogger().info("- Error loading iZone v" + this.getDescription().getVersion() + ".");
			getLogger().info("Supported Minecraft versions are: 1.7, 1.8, 1.9, 1.10 and 1.11");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		registerEvents();
		registerCommands();

		getLogger().info("Your server is running version " + this.getDescription().getVersion());
		getLogger().info("Minecraft version is: " + serverVersion.toString());

		VaultManager.load(this);

		mainConfig = Config.getConfig();
		mainConfig.setup();
		vaultConfig = VaultConfig.getConfig();
		vaultConfig.setup();
		loadUpdate();
		loadLanguageFile();

		if (ConfigManager.isParticlesEnabled())
		{
			try
			{
				Class.forName("org.inventivetalent.particle.ParticleEffect");
			} catch (ClassNotFoundException e)
			{
				getLogger().warning("ParticleAPI not found! This will cause problems!!");
				getLogger().warning("ParticleAPI not found! This will cause problems!!");
				getLogger().warning("ParticleAPI not found! This will cause problems!!");
				getLogger().warning("ParticleAPI not found! This will cause problems!!");
				getLogger().warning("ParticleAPI not found! This will cause problems!!");
			}
		}

		if (ConfigManager.getHealthListener())
		{
			getServer().getScheduler().scheduleSyncRepeatingTask(this, new HealthManager(this), 200L, 10L);
		}

		try
		{
			new Updater(this, 23349);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();

		pm.registerEvents(inventoryListener = new InventoryListener(this), this);
		pm.registerEvents(new bListener(), this);
		pm.registerEvents(new pListener(), this);
		pm.registerEvents(new eListener(), this);
		pm.registerEvents(new wListener(), this);

		getLogger().info("- Initalizing metrics");
		try
		{
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e)
		{
			getLogger().info("- Failed to initalize metrics");
		}
	}

	private void registerCommands() {
		this.commandManager = new CommandManager();
		this.commandManager.registerCommand(new iZoneCommand(this));
		this.commandManager.registerCommand(new zmodCommand(this));
	}

	public void sendInfo(Player player, Location loc) {
		Zone zone = ZoneManager.getZone(loc);

		if (zone != null)
		{
			String flags   = "";
			String allowed = "";
			Flags  flag;
			for (Iterator<Flags> localIterator = zone.getFlags().iterator(); localIterator.hasNext(); flags = flags + ChatColor.WHITE + "" + flag.getName() + ChatColor.AQUA + ", ")
			{
				flag = localIterator.next();
			}
			if (flags.endsWith(ChatColor.AQUA + ", "))
			{
				flags = flags.substring(0, flags.length() - 4);
			}
			String s;
			for (Iterator<String> localIterator = zone.getAllowed().iterator(); localIterator.hasNext(); allowed = allowed + ChatColor.WHITE + "" + s + ChatColor.AQUA + ", ")
			{
				s = localIterator.next();
			}
			if (allowed.endsWith(ChatColor.AQUA + ", "))
			{
				allowed = allowed.substring(0, allowed.length() - 4);
			}

			player.sendMessage(getPrefix() + ChatColor.GREEN + phrase("zone_found"));
			player.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "////////////// " + ChatColor.GOLD + "[" + zone.getName() + "]" + ChatColor.GRAY + "" + ChatColor.BOLD + " //////////////");
			player.sendMessage(ChatColor.GRAY + "");
			player.sendMessage(ChatColor.GRAY + phrase("word_flags") + ": " + ChatColor.AQUA + flags);
			player.sendMessage(ChatColor.GRAY + phrase("word_allowed") + ": " + ChatColor.AQUA + allowed);
		}
		else
		{
			player.sendMessage(getPrefix() + ChatColor.RED + "" + ChatColor.BOLD + phrase("zone_not_found"));
		}
	}

	public void loadLanguageFile() {

		Locale locale = new Locale(ConfigManager.getLocale());
		Phrases.getInstance().initialize(locale);
		File overrides = new File(iZone.instance.getDataFolder(), "messages_en.properties");
		if (overrides.exists())
		{
			java.util.Properties overridesProps = new java.util.Properties();
			try
			{
				//overridesProps.load(new FileInputStream(overrides));
				overridesProps.load(new BufferedReader(new InputStreamReader(new FileInputStream(overrides), "UTF-8")));
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			Phrases.getInstance().overrides(overridesProps);
		}
		iZone.instance.getLogger().info("Loaded language: " + ConfigManager.getLocale());
	}

	public void reloadConfiguration() {
		mainConfig = Config.getConfig();
		mainConfig.load();
		vaultConfig = VaultConfig.getConfig();
		vaultConfig.load();
	}

	private boolean loadZone(File file) {
		boolean failed = true;
		try
		{
			YamlConfiguration saveFile = YamlConfiguration.loadConfiguration(file);

			Zone zone = new Zone(file.getName().replace(".yml", ""));
			zone.setSave(false);
			try
			{
				zone.setWorld(iZone.instance.getServer().getWorld(saveFile.getString("world", "world")));
			} catch (NullPointerException npe)
			{
				iZone.instance.getLogger().info("Failed to set world for '" + zone.getName() + "'! " +
				                                "The world '" + saveFile.getString("world", "world") + "' does NOT exist!");
				failed = false;
			}
			zone.setWelcome(saveFile.getString("welcome", ""));
			zone.setFarewell(saveFile.getString("farewell", ""));
			zone.setGamemode(GameMode.valueOf(saveFile.getString("gamemode", "SURVIVAL")));

			if(saveFile.getString("teleport") != null)
				zone.setTeleport(getLocationString(saveFile.getString("teleport")));

			zone.setBorder(1, new Location(zone.getWorld(), saveFile.getInt("border1.x", 0), saveFile.getInt("border1.y", 0), saveFile.getInt("border1.z", 0)));
			zone.setBorder(2, new Location(zone.getWorld(), saveFile.getInt("border2.x", 0), saveFile.getInt("border2.y", 0), saveFile.getInt("border2.z", 0)));
			for (Flags flag : Flags.values())
			{
				zone.setFlag(flag.getId(), saveFile.getBoolean("flag." + flag.toString(), false));
			}
			zone.setAllowed((ArrayList<String>) saveFile.getStringList("allowed"));
			zone.setParent(saveFile.getString("parent-zone", ""));
			zone.setCreationDate(saveFile.getLong("creation-date", 0L));
			for (Flags flag : new Flags[]{Flags.GIVEITEM_IN, Flags.GIVEITEM_OUT, Flags.TAKEITEM_IN, Flags.TAKEITEM_OUT})
			{
				List<String> items = saveFile.getStringList("inventory." + flag.toString());
				for (String key : items)
				{
					ItemStack item = getItemStack(key);
					if (item == null)
					{
						continue;
					}
					zone.addInventory(flag, item);
				}
			}

			zone.setSave(true);
			ZoneManager.add(zone);
			return true;
		} catch (Exception e)
		{
			if (failed)
			{
				e.printStackTrace();
			}
		}
		return false;
	}

	private void loadUpdate() {
		try
		{
			int  loaded = 0;
			File dir    = new File(iZone.instance.getDataFolder() + File.separator + "saves" + File.separator);
			if (!dir.exists())
			{
				dir.mkdirs();
			}

			for (File file : dir.listFiles())
			{
				if ((file.getName().endsWith(".yml")) && (loadZone(file)))
				{
					loaded++;
				}
			}

			iZone.instance.getLogger().info("Loaded " + loaded + " zones");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private ItemStack getItemStack(String value) {
		ItemStack item;
		String[]  split = value.split(", ");
		try
		{
			item = new ItemStack(Material.matchMaterial(split[0].substring(1)), Integer.parseInt(split[1]), Short.parseShort(split[2].substring(0, split[2].length() - 1)));
		} catch (Exception e)
		{
			return null;
		}
		return item;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return this.commandManager.dispatch(sender, label, args);
	}

	public Location getLocationString(final String s) {
		if (s == null || Objects.equals(s.trim(), "")) {
			return null;
		}
		final String[] parts = s.split(":");
		if (parts.length == 4) {
			final World w = Bukkit.getServer().getWorld(parts[0]);
			final int x = Integer.parseInt(parts[1]);
			final int y = Integer.parseInt(parts[2]);
			final int z = Integer.parseInt(parts[3]);
			return new Location(w, x, y, z);
		}
		return null;
	}
}