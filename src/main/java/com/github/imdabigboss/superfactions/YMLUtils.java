package com.github.imdabigboss.superfactions;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class YMLUtils {
    private String configName;
    private FileConfiguration configuration;
    private File file;

    public YMLUtils(String configName) {
        this.configName = configName;

        this.file = new File(SuperFactions.getInstance().getDataFolder(), this.configName);
        if (!this.file.exists()) {
            SuperFactions.getInstance().saveResource(configName, false);
        }

        this.configuration = new YamlConfiguration();

        try {
            this.configuration.load(this.file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return configuration;
    }

    public void saveConfig() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            SuperFactions.getLog().warning("Unable to save " + configName); // shouldn't really happen, but save throws the exception
        }
    }
}
