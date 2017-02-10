package net.techguard.izone.Configuration;

import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.Zones.Flags;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*****************************************************
 *              Created by TryHardDood on 2016. 11. 04..
 ****************************************************/
public class Config implements Configurable {
	private static Config config     = new Config();
	private        Path   configFile = Paths.get(iZone.instance.getDataFolder() + File.separator + "config.yml");
	private YamlConfiguration yamlConfig;

	private Config() {
		;
	}

	public static Config getConfig() {
		return config;
	}

	private static Vector getVector(String value) {
		Vector   vector = new Vector(0, 0, 0);
		String[] split  = value.split(", ");
		try
		{
			vector.setX(Double.parseDouble(split[0].substring(1)));
			vector.setY(Double.parseDouble(split[1]));
			vector.setZ(Double.parseDouble(split[2].substring(0, split[2].length() - 1)));
		} catch (Exception e)
		{
			return null;
		}
		return vector;
	}

	@Override
	public void setup() {
		if (!Files.exists(configFile))
		{
			try
			{
				final Path path = configFile.getParent();
				if (path != null)
					Files.createDirectories(path);

				Files.createFile(configFile);
				load();
				populate();
				save();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			load();
		}
	}

	@Override
	public void load() {
		yamlConfig = YamlConfiguration.loadConfiguration(new File(configFile.toString()));

		if(get().getConfigurationSection("restriction.size") != null)
		{
			for (String key : get().getConfigurationSection("restriction.size").getKeys(false))
			{
				Vector size = getVector(key);
				if (size == null)
				{
					continue;
				}
				String permission = get().getString("restriction.size." + key, "none");
				if (permission.equals("none"))
				{
					continue;
				}
				Variables.PERMISSION_MAX_SIZE.put(permission, size);
			}
		}
		if(get().getConfigurationSection("restriction.zone") != null)
		{

			for (String key : get().getConfigurationSection("restriction.zone").getKeys(false))
			{
				int size = 0;
				try
				{
					size = Integer.parseInt(key);
				} catch (Exception e)
				{
					continue;
				}
				String permission = get().getString("restriction.zone." + key, "none");
				if (permission.equals("none"))
				{
					continue;
				}
				Variables.PERMISSION_MAX_ZONE.put(permission, size);
			}
		}

		for (Flags flag : Flags.values())
		{
			ConfigManager.autoFlag[flag.getId()] = get().getBoolean("on-create." + flag.toString(), false);
		}
	}

	@Override
	public void save() {
		try
		{
			yamlConfig.save(configFile.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void populate() {
		yamlConfig = YamlConfiguration.loadConfiguration(new File(configFile.toString()));

		for (Flags flag : Flags.values())
		{
			get().set("on-create." + flag.toString(), (flag == Flags.PROTECTION) || (flag == Flags.INTERACT));
		}

		get().set("locale", "en");
		get().set("listeners.healthListener", true);

		get().set("tools.check", Material.WOOD_SWORD.toString());
		get().set("tools.define", Material.WOOD_SPADE.toString());

		get().set("particles.enabled", false);
		get().set("particles.particle", "FIREWORKS_SPARK");

		get().set("healing.time", 3);
		get().set("healing.amount", 1);
		get().set("hurting.time", 7);
		get().set("hurting.amount", 2);

		get().set("restriction.size.(-1, -1, -1)", "izone.zone.max-size.unlimited");
		get().set("restriction.size.(50, 256, 50)", "izone.zone.max-size.1");

		get().set("restriction.zone.-1", "izone.zone.max-zone.unlimited");
		get().set("restriction.zone.3", "izone.zone.max-zone.1");

		get().set("messages.title.fadeIn", 2);
		get().set("messages.title.stay", 20);
		get().set("messages.title.fadeOut", 2);
	}

	@Override
	public YamlConfiguration get() {
		return yamlConfig;
	}
}
