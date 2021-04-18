package com.github.imdabigboss.superfactions.commands;

import com.github.imdabigboss.superfactions.SuperFactions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class SuperFactionsCommand implements CommandExecutor, TabExecutor {
    SuperFactions plugin;

    public SuperFactionsCommand(SuperFactions plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            sendHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("save")) {
            SuperFactions.getEconomy().saveEconomy();
            sender.sendMessage(ChatColor.AQUA + "Economy saved!");
        } else if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            SuperFactions.getEconomy().loadEconomy();
            if (plugin.getConfig().contains("currencyPrefix")) {
                plugin.currencyPrefix = plugin.getConfig().getString("currencyPrefix");
            }
            if (plugin.getConfig().contains("currencySuffix")) {
                plugin.currencySuffix = plugin.getConfig().getString("currencySuffix");
            }

            if (plugin.getConfig().contains("currencyName")) {
                plugin.currencyName = plugin.getConfig().getString("currencyName");
            }
            if (plugin.getConfig().contains("currencyNamePlural")) {
                plugin.currencyNamePlural = plugin.getConfig().getString("currencyNamePlural");
            }
            sender.sendMessage(ChatColor.AQUA + "Economy reloaded!");
        } else if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(String.format("%s: version: %s", plugin.getDescription().getName(), plugin.getDescription().getVersion()));
        } else {
            sendHelp(sender);
        }
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("Choose one of the following options: save, reload, info");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> cmds = new ArrayList<>();
        cmds.add("save");
        cmds.add("reload");
        cmds.add("info");
        return cmds;
    }
}
