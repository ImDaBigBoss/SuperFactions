package com.github.imdabigboss.superfactions.shop;

import com.github.imdabigboss.superfactions.SuperFactions;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Shop {
    /**
     * Buy an item and put it in a player's inventory
     * @param player The player
     * @param material The item's material to buy
     * @param amount The amount of the item to buy
     */
    public static void buyItem(Player player, Material material, int amount) {
        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ChatColor.RED + "You need to free up some space in your inventory before buying items.");
            return;
        }

        ItemStack item = new ItemStack(material);
        double price = getItemBuyPrice(item, amount);
        item.setAmount(amount);
        OfflinePlayer oplayer = SuperFactions.getInstance().getOfflinePlayer(player);

        if (price <= SuperFactions.getEconomy().getPlayerBalance(oplayer)) {
            SuperFactions.getEconomy().playerWithdraw(oplayer, price);
            player.getInventory().addItem(item);
            player.sendMessage(ChatColor.AQUA + "You baught " + amount + "x " + Shop.formatItemName(item) + " for " + SuperFactions.getEconomy().formatMoney(price) + "!");
        } else {
            player.sendMessage(ChatColor.RED + "You don't have enough money to buy that!");
        }
    }

    /**
     * Get an item's buying price
     * @param item The item in question
     * @return The item's price
     */
    public static double getItemBuyPrice(ItemStack item) {
        return getItemBuyPrice(item, 1);
    }

    /**
     * Get some items' buying price
     * @param item The items in question
     * @param amount The amount of items
     * @return The items' price
     */
    public static double getItemBuyPrice(ItemStack item, int amount) {
        if (item == null) {
            return 0.0;
        }

        return amount * SuperFactions.getPrices().getBuyPrice(item);
    }

    /**
     * Sell an item form a player's inventory
     * @param player The player
     * @param material The item's material to buy
     * @param amount The amount of the item to buy
     */
    public static void sellItem(Player player, Material material, int amount) {
        ItemStack item = new ItemStack(material);

        if (!player.getInventory().containsAtLeast(item, amount)) {
            player.sendMessage(ChatColor.RED + "You don't have that item in your inventory, you can't sell it.");
            return;
        }

        double price = getItemSellPrice(item, amount);
        item.setAmount(amount);
        OfflinePlayer oplayer = SuperFactions.getInstance().getOfflinePlayer(player);

        SuperFactions.getEconomy().playerDeposit(oplayer, price);
        player.getInventory().removeItem(item);
        player.updateInventory();
        player.sendMessage(ChatColor.AQUA + "You sold " + amount + "x " + Shop.formatItemName(item) + " for " + SuperFactions.getEconomy().formatMoney(price) + "!");
    }

    /**
     * Get an item's selling price
     * @param item The item in question
     * @return The item's price
     */
    public static double getItemSellPrice(ItemStack item) {
        return getItemSellPrice(item, 1);
    }

    /**
     * Get some items' selling price
     * @param item The items in question
     * @param amount The amount of items
     * @return The items' price
     */
    public static double getItemSellPrice(ItemStack item, int amount) {
        if (item == null) {
            return 0.0;
        }

        return amount * SuperFactions.getPrices().getSellPrice(item);
    }

    /**
     * Format an ItemStack's name
     * @param item The ItemStack in question
     * @return The formatted name
     */
    public static String formatItemName(ItemStack item) {
        return formatItemName(item.getType());
    }

    /**
     * Format a Material's name
     * @param material The Material in question
     * @return The formatted name
     */
    public static String formatItemName(Material material) {
        String words[] = material.toString().replace("_"," ").toLowerCase().split("\\s");
        String capitalizeWord = "";
        for(String w : words){
            String first = w.substring(0,1);
            String afterfirst = w.substring(1);
            capitalizeWord += first.toUpperCase() + afterfirst + " ";
        }
        return capitalizeWord.trim();
    }
}
