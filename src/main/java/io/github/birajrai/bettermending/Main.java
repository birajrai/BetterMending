package io.github.birajrai.bettermending;

import io.github.birajrai.bettermending.api.BetterMendingAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class Main extends JavaPlugin {

    private ConfigManager configManager;
    private DatabaseManager databaseManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        databaseManager = new DatabaseManager(this);
        databaseManager.connect();

        getServer().getPluginManager().registerEvents(new MendingListener(this), this);
        CommandManager commandManager = new CommandManager(this);
        getCommand("bettermending").setExecutor(commandManager);
        getCommand("bettermending").setTabCompleter(commandManager);

        BetterMendingAPI.register(this);

        getLogger().info("BetterMending has been enabled!");
    }

    @Override
    public void onDisable() {
        databaseManager.disconnect();
        getLogger().info("BetterMending has been disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public boolean isMendingEnabled(Player player) {
        return databaseManager.getPlayerToggle(player.getUniqueId());
    }

    public void toggleMending(Player player) {
        boolean currentSetting = databaseManager.getPlayerToggle(player.getUniqueId());
        databaseManager.setPlayerToggle(player.getUniqueId(), !currentSetting);
    }
}
