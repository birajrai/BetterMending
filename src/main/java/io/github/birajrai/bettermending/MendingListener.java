package io.github.birajrai.bettermending;

import io.github.birajrai.bettermending.api.PlayerMendingRepairEvent;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class MendingListener implements Listener {

    private final Main plugin;

    public MendingListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!plugin.getConfigManager().getConfig().getBoolean("enabled", true)) {
            return;
        }

        Player player = event.getPlayer();

        if (!plugin.isMendingEnabled(player)) {
            return;
        }

        if (!player.hasPermission("bettermending.use")) {
            return;
        }

        if (!player.isSneaking() || (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if (!(meta instanceof Damageable) || !meta.hasEnchant(Enchantment.MENDING)) {
            return;
        }

        Damageable damageable = (Damageable) meta;
        int damage = damageable.getDamage();

        if (damage == 0) {
            return;
        }

        int playerXp = player.getTotalExperience();

        if (playerXp == 0) {
            player.sendMessage(plugin.getConfigManager().getMessage("no_experience"));
            return;
        }

        int durabilityPerXp = plugin.getConfigManager().getConfig().getInt("durability-per-xp", 2);
        if (durabilityPerXp <= 0) {
            durabilityPerXp = 1; // Avoid division by zero or negative values
        }

        int xpCostForFullRepair = damage / durabilityPerXp;
        if (xpCostForFullRepair == 0 && damage > 0) {
            xpCostForFullRepair = 1;
        }

        if (playerXp >= xpCostForFullRepair) {
            player.giveExp(-xpCostForFullRepair);
            damageable.setDamage(0);
            item.setItemMeta(meta);
            plugin.getConfigManager().playSound(player, "sounds.full_repair");
            player.sendMessage(plugin.getConfigManager().getMessage("item_repaired"));

            // Fire custom event
            PlayerMendingRepairEvent repairEvent = new PlayerMendingRepairEvent(player, item, damage, xpCostForFullRepair);
            plugin.getServer().getPluginManager().callEvent(repairEvent);

        } else {
            int durabilityToRestore = playerXp * durabilityPerXp;
            damageable.setDamage(damage - durabilityToRestore);
            item.setItemMeta(meta);
            player.giveExp(-playerXp);
            plugin.getConfigManager().playSound(player, "sounds.partial_repair");
            player.sendMessage(plugin.getConfigManager().getMessage("item_partially_repaired"));

            // Fire custom event
            PlayerMendingRepairEvent repairEvent = new PlayerMendingRepairEvent(player, item, durabilityToRestore, playerXp);
            plugin.getServer().getPluginManager().callEvent(repairEvent);
        }
    }
}
