package com.github.imdabigboss.superfactions.commands;

import com.github.imdabigboss.superfactions.SuperFactions;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {
    SuperFactions plugin;

    public BalanceCommand(SuperFactions plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
            return true;
        }

        OfflinePlayer player = plugin.getOfflinePlayer((Player) sender);
        double balance = SuperFactions.getEconomy().getPlayerBalance(player);
        sender.sendMessage(ChatColor.AQUA + "You have a balance of " + SuperFactions.getEconomy().formatMoney((double) Math.round(balance * 100) / 100));
        return true;
    }
}