package com.github.imdabigboss.superfactions.commands;

import com.github.imdabigboss.superfactions.SuperFactions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class GiveCrateCommand implements CommandExecutor, TabExecutor {
    SuperFactions plugin;

    public GiveCrateCommand(SuperFactions plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.getConfig().getBoolean("enableCrates")) {
            sender.sendMessage("Crates are not enabled on this server.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage("The correct usage is /givecrate <player> <crate> [number]");
            return true;
        }

        if (!plugin.getConfig().contains("registeredPlayers." + args[0])) {
            sender.sendMessage(ChatColor.RED + "That player is not registered.");
            return true;
        }
        String playerUUID = plugin.getConfig().getString("registeredPlayers." + args[0]);

        int increaseBy = 1;
        if (args.length >= 3) {
            try {
                increaseBy = Integer.parseInt(args[2]);
            } catch (NumberFormatException ex) {
                sender.sendMessage(ChatColor.RED + "That is not a number.");
                return true;
            }
        }

        if (args[1].equalsIgnoreCase("daily")) {
            int crateCount = 0;
            if (plugin.getConfig().contains("crates." + playerUUID + ".dailyCount")) {
                crateCount = plugin.getConfig().getInt("crates." + playerUUID + ".dailyCount");
            }
            crateCount += increaseBy;
            plugin.getConfig().set("crates." + playerUUID + ".dailyCount", crateCount);
            plugin.saveConfig();

            sender.sendMessage(ChatColor.AQUA + "You gave " + args[0] + " " + increaseBy + " daily crate(s).");
        } else {
            sender.sendMessage(ChatColor.RED + "That is not an existing crate.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return plugin.getAllPlayers();
        } else if (args.length == 2) {
            List<String> cmds = new ArrayList<>();
            cmds.add("daily");
            return cmds;
        }

        return new ArrayList<>();
    }
}
