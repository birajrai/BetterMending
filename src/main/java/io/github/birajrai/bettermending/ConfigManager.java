package io.github.birajrai.bettermending;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;
    private FileConfiguration lang;
    private File langFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfigs();
    }

    public void loadConfigs() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        langFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    public void reloadConfigs() {
        config = YamlConfiguration.loadConfiguration(configFile);
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String getMessage(String path) {
        String message = lang.getString(path, "&cMessage not found: " + path);
        String prefix = lang.getString("prefix", "");
        return ChatColor.translateAlternateColorCodes('&', prefix + message);
    }

    public void playSound(Player player, String path) {
        try {
            String soundName = config.getString(path + ".name");
            float volume = (float) config.getDouble(path + ".volume");
            float pitch = (float) config.getDouble(path + ".pitch");
            player.playSound(player.getLocation(), Sound.valueOf(soundName.toUpperCase()), volume, pitch);
        } catch (Exception e) {
            plugin.getLogger().warning("Invalid sound configuration for: " + path);
        }
    }
}
