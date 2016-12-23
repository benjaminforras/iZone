package net.techguard.izone.Configuration;

import net.techguard.izone.iZone;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/*****************************************************
 *              Created by TryHardDood on 2016. 11. 04..
 ****************************************************/
public class VaultConfig implements Configurable {
	private static VaultConfig config     = new VaultConfig();
	private        Path        configFile = Paths.get(iZone.instance.getDataFolder() + File.separator + "vault.yml");
	private YamlConfiguration yamlConfig;

	private VaultConfig() {
		;
	}

	public static VaultConfig getConfig() {
		return config;
	}

	@Override
	public void setup() {
		if (!Files.exists(configFile))
		{
			try
			{
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
		get().set("enabled", false);
		get().set("create-zone", 0);
		get().set("delete-zone", 0);
		get().set("allow-player", 0);
		get().set("disallow-player", 0);
	}

	@Override
	public YamlConfiguration get() {
		return yamlConfig;
	}
}
