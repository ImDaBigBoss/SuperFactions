package com.github.imdabigboss.superfactions.commands;

import com.github.imdabigboss.superfactions.SuperFactions;
import com.github.imdabigboss.superfactions.shop.ShopGUI;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ShopCommand implements CommandExecutor, TabExecutor {
    private SuperFactions plugin;

    public ShopCommand(SuperFactions plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
            return true;
        }

        if (args.length < 1) {
            ShopGUI.openShop((Player) sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("setpos")) {
            plugin.getShopNPC().setPos(((Player)sender).getLocation());
            if (plugin.getShopNPC().getNPCExists()) {
                sender.sendMessage(ChatColor.AQUA + "Shop position updated!");
                plugin.getShopNPC().updateNPCPos();
            } else {
                sender.sendMessage(ChatColor.AQUA + "Shop created!");
                plugin.getShopNPC().createNPC();
            }
        } else if (args[0].equalsIgnoreCase("open")) {
            ShopGUI.openShop((Player) sender);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> cmds = new ArrayList<>();
        cmds.add("setpos");
        cmds.add("open");
        return cmds;
    }
}
