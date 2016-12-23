package net.techguard.izone.Configuration;

import net.techguard.izone.iZone;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/*****************************************************
 *              Created by TryHardDood on 2016. 12. 15..
 ****************************************************/
public class DisabledWorldsConfig implements Configurable {

	private static DisabledWorldsConfig config     = new DisabledWorldsConfig();
	private        Path                 configFile = Paths.get(iZone.instance.getDataFolder() + File.separator + "disabledWorlds.yml");
	private YamlConfiguration yamlConfig;

	private DisabledWorldsConfig() {
		;
	}

	public static DisabledWorldsConfig getConfig() {
		return config;
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

		get().set("useAsWhitelist", false);
		get().set("disabledWorlds", Arrays.asList("world_nether", "world_the_end"));
	}

	@Override
	public YamlConfiguration get() {
		return yamlConfig;
	}

}
