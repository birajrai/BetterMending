package io.github.birajrai.bettermending;

import io.github.birajrai.bettermending.api.BetterMendingAPI;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private ConfigManager configManager;
    private final Set<UUID> toggledPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);

        getServer().getPluginManager().registerEvents(new MendingListener(this), this);
        CommandManager commandManager = new CommandManager(this);
        getCommand("bettermending").setExecutor(commandManager);
        getCommand("bettermending").setTabCompleter(commandManager);

        BetterMendingAPI.register(this);

        getLogger().info("BetterMending has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BetterMending has been disabled!");
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public boolean isMendingEnabled(Player player) {
        boolean defaultOn = getConfigManager().getConfig().getBoolean("toggle_default_on", true);
        if (defaultOn) {
            return !toggledPlayers.contains(player.getUniqueId());
        } else {
            return toggledPlayers.contains(player.getUniqueId());
        }
    }

    public void toggleMending(Player player) {
        if (toggledPlayers.contains(player.getUniqueId())) {
            toggledPlayers.remove(player.getUniqueId());
        } else {
            toggledPlayers.add(player.getUniqueId());
        }
    }
}
