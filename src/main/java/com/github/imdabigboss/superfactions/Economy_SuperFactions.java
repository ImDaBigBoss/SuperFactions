package com.github.imdabigboss.superfactions;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Economy_SuperFactions {
    private SuperFactions plugin;
    private Map<UUID, Double> playerBalances = new HashMap<>();

    public Economy_SuperFactions(SuperFactions plugin) {
        this.plugin = plugin;
        loadEconomy();
    }

    /**
     * Set a player's balance
     * @param player The offline player
     * @param balance The balance to set the player's account at
     */
    public void setPlayerBalance(OfflinePlayer player, double balance) {
        createPlayerAccount(player);
        playerBalances.replace(player.getUniqueId(), balance);
        saveEconomy(player);
    }

    /**
     * Get a player's balance
     * @param player The offline player
     * @return The player's balance
     */
    public double getPlayerBalance(OfflinePlayer player) {
        createPlayerAccount(player);
        return playerBalances.get(player.getUniqueId());
    }

    /**
     * Add money to a player's balance
     * @param player The offline player
     * @param amount The amount to add
     */
    public void playerDeposit(OfflinePlayer player, double amount) {
        createPlayerAccount(player);
        playerBalances.replace(player.getUniqueId(), getPlayerBalance(player) + amount);
        saveEconomy(player);
    }

    /**
     * Subtract money from the player's balance
     * @param player The offline player
     * @param amount The amount to remove
     */
    public void playerWithdraw(OfflinePlayer player, double amount) {
        createPlayerAccount(player);
        playerBalances.replace(player.getUniqueId(), getPlayerBalance(player) - amount);
        saveEconomy(player);
    }

    /**
     * Get if a player can afford an amount of money
     * @param player The offline player
     * @param amount The amount to test
     * @return true if the player can afford, false if not
     */
    public boolean playerCanAfford(OfflinePlayer player, double amount) {
        createPlayerAccount(player);
        if (amount > getPlayerBalance(player)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Create a player account if it doesn't exist alreday
     * @param player The offline player
     * @return true if the player has an account, false if not
     */
    public boolean createPlayerAccount(OfflinePlayer player) {
        if (!playerHasAccount(player)) {
            playerBalances.put(player.getUniqueId(), plugin.getConfig().getDouble("playerFirstJoinAmount"));
            saveEconomy(player);
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get if a player has an account
     * @param player The offline player
     * @return true if the player has an acconut, false if not
     */
    public boolean playerHasAccount(OfflinePlayer player) {
        if (player == null) {
            SuperFactions.getLog().severe("Unable to get a player account! playerHasAccount()");
            return false;
        }
        return playerBalances.containsKey(player.getUniqueId());
    }

    /**
     * Formate a double to a string with the currency prefix/suffix
     * @param money The amount of money
     * @return The formatted string
     */
    public String formatMoney(double money) {
        return plugin.currencyPrefix + ((double)Math.round(money * 100) / 100) + plugin.currencySuffix;
    }

    /**
     * Load player balances from config
     */
    public void loadEconomy() {
        playerBalances.clear();

        if (plugin.getConfig().contains("economy")) {
            Map<String, Object> values = plugin.getConfig().getConfigurationSection("economy").getValues(true);
            for (String string : values.keySet()) {
                OfflinePlayer player = plugin.getOfflinePlayer(UUID.fromString(string));
                playerBalances.put(player.getUniqueId(), plugin.getConfig().getDouble("economy." + string));
            }
        }
    }

    /**
     * Save ALL player balances to config (can be slow if there are a lot of players)
     */
    public void saveEconomy() {
        for (UUID uuid : playerBalances.keySet()) {
            plugin.getConfig().set("economy." + uuid, playerBalances.get(uuid));
        }
        plugin.saveConfig();
    }

    /**
     * Save one player's balance to config
     * @param player The offline player
     */
    public void saveEconomy(OfflinePlayer player) {
        plugin.getConfig().set("economy." + player.getUniqueId(), playerBalances.get(player.getUniqueId()));
        plugin.saveConfig();
    }
}