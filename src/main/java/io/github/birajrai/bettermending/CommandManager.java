package io.github.birajrai.bettermending;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public CommandManager(Main plugin) {
        this.plugin = plugin;
    }

        @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(plugin.getConfigManager().getMessage("command_usage"));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        if (subCommand.equals("reload")) {
            if (!sender.hasPermission("bettermending.reload")) {
                sender.sendMessage(plugin.getConfigManager().getMessage("no_permission"));
                return true;
            }
            plugin.getConfigManager().reloadConfigs();
            sender.sendMessage(plugin.getConfigManager().getMessage("plugin_reloaded"));
            return true;
        }

        if (subCommand.equals("toggle")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be run by a player.");
                return true;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("bettermending.toggle")) {
                player.sendMessage(plugin.getConfigManager().getMessage("no_permission"));
                return true;
            }

            plugin.toggleMending(player);

            if (plugin.isMendingEnabled(player)) {
                player.sendMessage(plugin.getConfigManager().getMessage("plugin_toggled_on"));
            } else {
                player.sendMessage(plugin.getConfigManager().getMessage("plugin_toggled_off"));
            }
            return true;
        }

        sender.sendMessage(plugin.getConfigManager().getMessage("command_usage"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> subCommands = new ArrayList<>();
            if (sender.hasPermission("bettermending.reload")) {
                subCommands.add("reload");
            }
            if (sender.hasPermission("bettermending.toggle")) {
                subCommands.add("toggle");
            }
            return subCommands;
        }
        return null;
    }
}
