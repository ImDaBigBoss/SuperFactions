package com.github.imdabigboss.superfactions.commands;

import com.github.imdabigboss.superfactions.SuperFactions;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MoneyCommand implements CommandExecutor, TabExecutor {
    SuperFactions plugin;

    public MoneyCommand(SuperFactions plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sendHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("balance")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command!");
                return true;
            }
            OfflinePlayer player = plugin.getOfflinePlayer((Player) sender);
            double balance = SuperFactions.getEconomy().getPlayerBalance(player);
            sender.sendMessage(ChatColor.AQUA + "You have a balance of " + SuperFactions.getEconomy().formatMoney(balance));
        } else if (args[0].equalsIgnoreCase("send")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command!");
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage("You must enter a player name and an amount!");
                return true;
            }

            try {
                double amount = Double.valueOf(args[2]);
                OfflinePlayer player = plugin.getOfflinePlayer(args[1]);
                if (player != null) {
                    if (!SuperFactions.getEconomy().playerCanAfford(plugin.getOfflinePlayer((Player)sender), amount)) {
                        sender.sendMessage(ChatColor.RED + "You can't afford that amount of money!");
                        return true;
                    }

                    SuperFactions.getEconomy().playerWithdraw(plugin.getOfflinePlayer((Player)sender), amount);
                    SuperFactions.getEconomy().playerDeposit(player, amount);
                    sender.sendMessage(ChatColor.AQUA + "You sent " + SuperFactions.getEconomy().formatMoney(amount) + " to " + args[1] + "'s account!");
                    Player p = plugin.getServer().getPlayer(args[1]);
                    if (p != null) {
                        p.sendMessage(ChatColor.AQUA + sender.getName() + " sent you " + SuperFactions.getEconomy().formatMoney(amount));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "That player is not registered!");
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "The amount entered is not a number");
            }
        } else if (args[0].equalsIgnoreCase("get")) {
            if (!plugin.isAdmin(sender)) {
                sendHelp(sender);
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage("You must enter a player name!");
                return true;
            }

            OfflinePlayer player = plugin.getOfflinePlayer(args[1]);
            if (player != null) {
                double balance = SuperFactions.getEconomy().getPlayerBalance(player);
                sender.sendMessage(ChatColor.AQUA + args[1] + " has a balance of " + SuperFactions.getEconomy().formatMoney(balance));
            } else {
                sender.sendMessage(ChatColor.RED + "That player is not registered!");
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            if (!plugin.isAdmin(sender)) {
                sendHelp(sender);
                return true;
            }
            if (args.length < 3) {
                sender.sendMessage("You must enter a player name and a balance!");
                return true;
            }

            try {
                double balance = Double.valueOf(args[2]);
                OfflinePlayer player = plugin.getOfflinePlayer(args[1]);
                if (player != null) {
                    SuperFactions.getEconomy().setPlayerBalance(player, balance);
                    sender.sendMessage(ChatColor.AQUA + args[1] + " now has a balance of " + SuperFactions.getEconomy().formatMoney(balance));
                } else {
                    sender.sendMessage(ChatColor.RED + "That player is not registered!");
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "The new balance entered is not a number");
            }
        } else {
            sendHelp(sender);
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        String adminCommands = "";
        if (plugin.isAdmin(sender)) {
            adminCommands = ", get, set";
        }
        sender.sendMessage("Choose one of the following options: send, balance" + adminCommands);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> cmds = new ArrayList<>();
            cmds.add("send");
            cmds.add("balance");

            if (plugin.isAdmin(sender)) {
                cmds.add("get");
                cmds.add("set");
            }
            return cmds;
        }

        if (args.length == 2) {
            if (plugin.isAdmin(sender) && (args[0].equalsIgnoreCase("get") || args[0].equalsIgnoreCase("set"))) {
                return plugin.getAllPlayers();
            } else if (args[0].equalsIgnoreCase("send")) {
                return plugin.getAllPlayers();
            }
        }

        return new ArrayList<>();
    }
}