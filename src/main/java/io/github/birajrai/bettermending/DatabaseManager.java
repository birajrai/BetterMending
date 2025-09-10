package io.github.birajrai.bettermending;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseManager {

    private final Main plugin;
    private Connection connection;
    private final String databasePath;

    public DatabaseManager(Main plugin) {
        this.plugin = plugin;
        this.databasePath = "jdbc:h2:" + plugin.getDataFolder().getAbsolutePath() + File.separator + "player_data";
    }

    public void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(databasePath);
            plugin.getLogger().info("Database connection established.");
            createTable();
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not connect to database: " + e.getMessage());
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                plugin.getLogger().info("Database connection closed.");
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not close database connection: " + e.getMessage());
        }
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS player_toggles (" +
                     "uuid VARCHAR(36) PRIMARY KEY," +
                     "enabled BOOLEAN NOT NULL" +
                     ");";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            plugin.getLogger().info("Table 'player_toggles' created or already exists.");
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not create table: " + e.getMessage());
        }
    }

    public boolean getPlayerToggle(UUID uuid) {
        String sql = "SELECT enabled FROM player_toggles WHERE uuid = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("enabled");
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not get player toggle for " + uuid + ": " + e.getMessage());
        }
        // Default to true if not found, assuming default is enabled
        return plugin.getConfigManager().getConfig().getBoolean("toggle_default_on", true);
    }

    public void setPlayerToggle(UUID uuid, boolean enabled) {
        String sql = "MERGE INTO player_toggles (uuid, enabled) KEY (uuid) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setBoolean(2, enabled);
            statement.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not set player toggle for " + uuid + ": " + e.getMessage());
        }
    }

    public Set<UUID> getAllToggledPlayers() {
        Set<UUID> toggledPlayers = new HashSet<>();
        String sql = "SELECT uuid, enabled FROM player_toggles;";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                boolean enabled = resultSet.getBoolean("enabled");
                // Only add to the set if it deviates from the default
                boolean defaultOn = plugin.getConfigManager().getConfig().getBoolean("toggle_default_on", true);
                if (defaultOn && !enabled) { // Default is ON, player has it OFF
                    toggledPlayers.add(uuid);
                } else if (!defaultOn && enabled) { // Default is OFF, player has it ON
                    toggledPlayers.add(uuid);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not get all toggled players: " + e.getMessage());
        }
        return toggledPlayers;
    }
}
