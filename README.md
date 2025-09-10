# BetterMending

BetterMending is a Spigot plugin for Minecraft servers that allows players to repair their Mending-enchanted items using experience points by simply sneaking and right-clicking.

## Features

- **Intuitive Repair:** Sneak + Right-Click with a Mending-enchanted item in hand to repair it.
- **Experience-Based:** Repairs consume experience points, similar to vanilla Mending mechanics.
- **Configurable XP Cost:** Adjust the durability repaired per experience point.
- **Toggle Feature:** Players can toggle the repair functionality on/off for themselves.
- **Reload Command:** Admins can reload the plugin's configuration without restarting the server.
- **Customizable Messages:** All plugin messages can be configured via `lang.yml`.
- **Configurable Sounds:** Customize the sounds played on successful repairs.
- **Developer API:** Provides an API for other plugins to interact with BetterMending.

## Commands & Permissions

| Command                 | Description                                     | Permission                 | Default    |
| :---------------------- | :---------------------------------------------- | :------------------------- | :--------- |
| `/bettermending`        | Main command for BetterMending.                 |                            |            |
| `/bettermending reload` | Reloads the plugin's configuration files.       | `bettermending.reload`     | `op`       |
| `/bettermending toggle` | Toggles the repair feature for yourself.        | `bettermending.toggle`     | `true`     |

- `bettermending.use`: Allows players to use the shift-right-click repair feature. (Default: `true`)

## Configuration (`config.yml`)

```yaml
# BetterMending Plugin Configuration
# Toggle the entire plugin on or off.
enabled: true

# Amount of durability points to be repaired per one experience point.
# Default vanilla mending rate is 2.
durability-per-xp: 2

# Should the repair feature be on for players by default?
# If true, players can use /bm toggle to disable it.
# If false, players must use /bm toggle to enable it.
toggle_default_on: true

# Sounds played on repair.
sounds:
  full_repair:
    name: "ENTITY_EXPERIENCE_ORB_PICKUP"
    volume: 1.0
    pitch: 1.0
  partial_repair:
    name: "ENTITY_EXPERIENCE_ORB_PICKUP"
    volume: 1.0
    pitch: 0.8
```

## Language (`lang.yml`)

```yaml
# Language configuration for BetterMending
prefix: "&8[&6BetterMending&8] "
item_repaired: "&aYour item has been fully repaired!"
item_partially_repaired: "&eYour item has been partially repaired."
no_experience: "&cYou don't have any experience to repair this item."
no_permission: "&cYou don't have permission to do that."
plugin_reloaded: "&aPlugin reloaded successfully."
plugin_toggled_on: "&aMending repair is now enabled for you."
plugin_toggled_off: "&cMending repair is now disabled for you."
```

## For Developers (API)

BetterMending provides a simple API for other plugins to interact with its functionality.

### Getting the API

The API is accessible via static methods in the `io.github.birajrai.bettermending.api.BetterMendingAPI` class.

### Checking Mending Status

To check if the BetterMending repair feature is enabled for a specific player:

```java
import io.github.birajrai.bettermending.api.BetterMendingAPI;
import org.bukkit.entity.Player;

// ... in your plugin code

Player player = // ... get your player instance
if (BetterMendingAPI.isMendingEnabled(player)) {
    // BetterMending is enabled for this player
}
```

### Toggling Mending Status

To programmatically toggle the BetterMending repair feature for a player:

```java
import io.github.birajrai.bettermending.api.BetterMendingAPI;
import org.bukkit.entity.Player;

// ... in your plugin code

Player player = // ... get your player instance
BetterMendingAPI.toggleMending(player);
// The player's mending status has been toggled.
```

### Listening to Repair Events

YouYou can listen to the `io.github.birajrai.bettermending.api.PlayerMendingRepairEvent` to be notified when a player repairs an item using BetterMending.

```java
import io.github.birajrai.bettermending.api.PlayerMendingRepairEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class MyRepairListener implements Listener {

    @EventHandler
    public void onPlayerMendingRepair(PlayerMendingRepairEvent event) {
        Player player = event.getPlayer();
        ItemStack repairedItem = event.getItem();
        int durabilityRepaired = event.getDurabilityRepaired();
        int xpConsumed = event.getXpConsumed();

        // Do something with the repair information
        player.sendMessage("You just repaired " + repairedItem.getType().name() + " by " + durabilityRepaired + " durability!");
    }
}
```

Remember to register your listener in your plugin's `onEnable()` method:

```java
getServer().getPluginManager().registerEvents(new MyRepairListener(), this);
```

## Building from Source

To build the plugin from source, you will need Java Development Kit (JDK) 8 or newer and Gradle.

1. Clone the repository:
   ```bash
   git clone https://github.com/birajrai/BetterMending.git
   cd BetterMending
   ```
2. Build the plugin:
   ```bash
   ./gradlew build
   ```
   The compiled JAR file will be located in the `build/libs/` directory.

## Developer API Documentation

To generate the full Javadoc API documentation, run the following command:

```bash
./gradlew javadoc
```

The generated documentation will be available in the `build/docs/javadoc/` directory.
