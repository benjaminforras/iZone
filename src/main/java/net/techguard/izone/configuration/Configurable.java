package net.techguard.izone.configuration;

import org.bukkit.configuration.file.YamlConfiguration;

/*****************************************************
 *              Created by TryHardDood on 2016. 11. 04..
 ****************************************************/
public interface Configurable {

	void setup();

	void load();

	void save();

	void populate();

	YamlConfiguration get();
}