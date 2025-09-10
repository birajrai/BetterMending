package io.github.birajrai.bettermending.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PlayerMendingRepairEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack item;
    private final int durabilityRepaired;
    private final int xpConsumed;

    public PlayerMendingRepairEvent(Player player, ItemStack item, int durabilityRepaired, int xpConsumed) {
        this.player = player;
        this.item = item;
        this.durabilityRepaired = durabilityRepaired;
        this.xpConsumed = xpConsumed;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getDurabilityRepaired() {
        return durabilityRepaired;
    }

    public int getXpConsumed() {
        return xpConsumed;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
