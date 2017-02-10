package net.techguard.izone.Configuration;

import org.bukkit.configuration.file.YamlConfiguration;

public interface Configurable {


	void setup();

	void load();

	void save();

	void populate();

	YamlConfiguration get();
}
