package com.github.imdabigboss.superfactions.commands;

import com.github.imdabigboss.superfactions.SuperFactions;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DailyCrateCommand implements CommandExecutor {
    SuperFactions plugin;

    public DailyCrateCommand(SuperFactions plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
            return true;
        }

        if (!plugin.getConfig().getBoolean("enableCrates")) {
            sender.sendMessage("Crates are not enabled on this server.");
            return true;
        }

        Player player = (Player) sender;

        long crateCooldown = plugin.getConfig().getLong("dailyCrateCooldown");
        long lastUsed = 0;
        if (plugin.getConfig().contains("crates." + player.getUniqueId().toString() + ".lastDaily")) {
            lastUsed = plugin.getConfig().getLong("crates." + player.getUniqueId().toString() + ".lastDaily");
        }

        if (lastUsed + crateCooldown <= System.currentTimeMillis()) {
            plugin.getConfig().set("crates." + player.getUniqueId().toString() + ".lastDaily", System.currentTimeMillis());
            int crateCount = 0;
            if (plugin.getConfig().contains("crates." + player.getUniqueId().toString() + ".dailyCount")) {
                crateCount = plugin.getConfig().getInt("crates." + player.getUniqueId().toString() + ".dailyCount");
            }
            crateCount += 1;
            plugin.getConfig().set("crates." + player.getUniqueId().toString() + ".dailyCount", crateCount);
            plugin.saveConfig();
            player.sendMessage(ChatColor.AQUA + "You claimed your daily key! You now have " + crateCount + " available key(s).");
        } else {
            Date date = new Date((lastUsed + crateCooldown) - System.currentTimeMillis());
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            player.sendMessage(ChatColor.RED + "You need to wait " + formatter.format(date));
        }

        return true;
    }
}