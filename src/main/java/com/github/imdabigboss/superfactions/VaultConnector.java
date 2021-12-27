package com.github.imdabigboss.superfactions;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class VaultConnector implements Economy {
    private SuperFactions plugin;
    private Economy_SuperFactions economy;
    private String name = "SuperFactions";

    public VaultConnector(SuperFactions plugin) {
        this.plugin = plugin;
        economy = plugin.getEconomy();
    }

    @Override
    public boolean isEnabled() {
        return plugin != null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double amount) {
        return economy.formatMoney(amount);
    }

    @Override
    public String currencyNamePlural() {
        return SuperFactions.currencyNamePlural;
    }

    @Override
    public String currencyNameSingular() {
        return SuperFactions.currencyName;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return hasAccount(plugin.getOfflinePlayer(playerName));
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return economy.playerHasAccount(player);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(plugin.getOfflinePlayer(playerName));
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String playerName) {
        return getBalance(plugin.getOfflinePlayer(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return economy.getPlayerBalance(player);
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(plugin.getOfflinePlayer(playerName));
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String playerName, double amount) {
        return has(plugin.getOfflinePlayer(playerName), amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return economy.playerCanAfford(player, amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(plugin.getOfflinePlayer(playerName), amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    private EconomyResponse withdraw(OfflinePlayer player, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0 , 0, EconomyResponse.ResponseType.FAILURE, "Cannot withdraw negative funds");
        }
        if (!economy.playerCanAfford(player, amount)) {
            return new EconomyResponse(0 , economy.getPlayerBalance(player), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
        }

        economy.playerWithdraw(player, amount);
        return new EconomyResponse(amount, economy.getPlayerBalance(player), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(plugin.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        return withdraw(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(plugin.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    private EconomyResponse deposit(OfflinePlayer player, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0 , 0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds");
        }

        economy.playerDeposit(player, amount);
        return new EconomyResponse(amount, economy.getPlayerBalance(player), EconomyResponse.ResponseType.SUCCESS, "");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(plugin.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        return deposit(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(plugin.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "This feature is not implemented in SuperFactions");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "This feature is not implemented in SuperFactions");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "This feature is not implemented in SuperFactions");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "This feature is not implemented in SuperFactions");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "This feature is not implemented in SuperFactions");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "This feature is not implemented in SuperFactions");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "This feature is not implemented in SuperFactions");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "This feature is not implemented in SuperFactions");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "This feature is not implemented in SuperFactions");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "This feature is not implemented in SuperFactions");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "This feature is not implemented in SuperFactions");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<String>();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return createPlayerAccount(plugin.getOfflinePlayer(playerName));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        economy.createPlayerAccount(player);
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(plugin.getOfflinePlayer(playerName));
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }
}
