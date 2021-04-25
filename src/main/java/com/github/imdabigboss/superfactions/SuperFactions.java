package com.github.imdabigboss.superfactions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.github.imdabigboss.superfactions.commands.*;
import com.github.imdabigboss.superfactions.shop.ItemPrices;
import com.github.imdabigboss.superfactions.shop.ShopNPC;

import net.jitse.npclib.NPCLib;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperFactions extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");
    private static SuperFactions instance = null;
    private static Economy_SuperFactions economy = null;
    private static ItemPrices prices = null;
    private static NPCLib npcLib;
    private static ShopNPC shopNPC;

    public static String currencyPrefix = "$";
    public static String currencySuffix = "";
    public static String currencyName = "";
    public static String currencyNamePlural = "";

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        log.info(String.format("[%s] Enabled Version %s", getDescription().getName(), getDescription().getVersion()));
        economy = new Economy_SuperFactions(this);
        prices = new ItemPrices(this);
        npcLib = new NPCLib(this);
        shopNPC = new ShopNPC(this);

        if (getConfig().contains("version")) {
            if (!getConfig().getString("version").equalsIgnoreCase(getDescription().getVersion())) {
                log.warning("Your config is not up to date, please regenerate it! You are using " + getConfig().getString("version") + " and the current version is " + getDescription().getVersion());
            }
        } else {
            log.warning("Your config has no version string, please regenerate it!");
        }

        if (getConfig().contains("currencyPrefix")) {
            currencyPrefix = getConfig().getString("currencyPrefix");
        }
        if (getConfig().contains("currencySuffix")) {
            currencySuffix = getConfig().getString("currencySuffix");
        }

        if (getConfig().contains("currencyName")) {
            currencyName = getConfig().getString("currencyName");
        }
        if (getConfig().contains("currencyNamePlural")) {
            currencyNamePlural = getConfig().getString("currencyNamePlural");
        }
        getServer().getPluginManager().registerEvents(new EventListener(this), this);

        this.getCommand("superfactions").setExecutor(new SuperFactionsCommand(this));
        this.getCommand("balance").setExecutor(new BalanceCommand(this));
        this.getCommand("money").setExecutor(new MoneyCommand(this));
        this.getCommand("shop").setExecutor(new ShopCommand(this));
        this.getCommand("dailycrate").setExecutor(new DailyCrateCommand(this));
        this.getCommand("opencrate").setExecutor(new OpenCrateCommand(this));
        this.getCommand("givecrate").setExecutor(new GiveCrateCommand(this));

        Plugin vault = getServer().getPluginManager().getPlugin("Vault");
        if (vault != null) {
            log.info(String.format("[%s] Enabled Vault connector. Vault version: %s", getDescription().getName(), vault.getDescription().getVersion()));
            getServer().getServicesManager().register(Economy.class, new VaultConnector(this), this, ServicePriority.Highest);
        }
        shopNPC.createNPC();
    }

    @Override
    public void onDisable() {
        shopNPC.destoryNPC();
        economy.saveEconomy();
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    /**
     * Get if a command sender has the admin permissions or if they are the console
     * @param sender The command sender
     * @return true if the player is an admin, false if not
     */
    public boolean isAdmin(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        } else if (sender instanceof Player) {
            if (((Player)sender).hasPermission("superfactions.admin")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a list of all online players
     * @return A list of strings
     */
    public List<String> getAllPlayers() {
        List<String> players = new ArrayList<>();
        for (Player player : getServer().getOnlinePlayers()) {
            players.add(player.getName());
        }
        return players;
    }

    /**
     * Get an offline player by a Bukkit Player class
     * @param player The player in question
     * @return The offline player
     */
    public OfflinePlayer getOfflinePlayer(Player player) {
        return getServer().getOfflinePlayer(player.getUniqueId());
    }

    /**
     * Get an offline player from a UUID
     * @param uuid The player's UUID
     * @return The offline player (can be null)
     */
    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        return getServer().getOfflinePlayer(uuid);
    }

    /**
     * Get an offline player from a String
     * This looks in the regiestered players in the config
     * @param name The player's name
     * @return The offline player (will be null if the player is not registered)
     */
    public OfflinePlayer getOfflinePlayer(String name) {
        if (!getConfig().contains("registeredPlayers." + name)) {
            return null;
        }

        return getOfflinePlayer(UUID.fromString(getConfig().getString("registeredPlayers." + name)));
    }

    public static Logger getLog() {
        return log;
    }
    public static Economy_SuperFactions getEconomy() {
        return economy;
    }
    public static ItemPrices getPrices() {
        return prices;
    }
    public static NPCLib getNPC() {
        return npcLib;
    }
    public static ShopNPC getShopNPC() {
        return shopNPC;
    }
    public static SuperFactions getInstance() {
        return instance;
    }
}
