package io.github.birajrai.bettermending.api;

import io.github.birajrai.bettermending.Main;
import org.bukkit.entity.Player;

public class BetterMendingAPI {

    private static Main plugin;

    public static void register(Main main) {
        plugin = main;
    }

    public static boolean isMendingEnabled(Player player) {
        if (plugin == null) {
            return false;
        }
        return plugin.isMendingEnabled(player);
    }

    public static void toggleMending(Player player) {
        if (plugin == null) {
            return;
        }
        plugin.toggleMending(player);
    }
}
