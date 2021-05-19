package com.github.imdabigboss.superfactions.commands;

import com.github.imdabigboss.superfactions.SuperFactions;
import com.github.imdabigboss.superfactions.YMLUtils;
import com.github.imdabigboss.superfactions.claims.CheckBlockEvent;
import com.github.imdabigboss.superfactions.claims.ChunkData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClaimCommand implements CommandExecutor, TabExecutor {
    private SuperFactions plugin;
    private YMLUtils yml;

    public ClaimCommand(SuperFactions plugin) {
        super();
        this.plugin = plugin;
        this.yml = SuperFactions.getClaimsYML();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
            return true;
        }

        Player player = ((Player) sender).getPlayer();

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("add")) {
                int chunkX = player.getLocation().getBlockX() >> 4;
                int chunkZ = player.getLocation().getBlockZ() >> 4;
                String configName = chunkX + "|" + chunkZ;

                if (yml.getConfig().contains(configName)) {
                    String playerName = plugin.getOfflinePlayer(UUID.fromString(yml.getConfig().getString(configName + ".owner"))).getName();

                    sender.sendMessage(ChatColor.RED + "This chunk is already claimed by " + playerName);
                    return true;
                }

                yml.getConfig().set(configName + ".owner", player.getUniqueId().toString());
                yml.getConfig().set(configName + ".chunkX", chunkX);
                yml.getConfig().set(configName + ".chunkZ", chunkZ);
                yml.getConfig().set(configName + ".invited", new ArrayList<>());
                yml.saveConfig();

                sender.sendMessage(ChatColor.AQUA + "You have just claimed this chunk!");
            } else if (args[0].equalsIgnoreCase("remove")) {
                int chunkX = player.getLocation().getBlockX() >> 4;
                int chunkZ = player.getLocation().getBlockZ() >> 4;
                String configName = chunkX + "|" + chunkZ;

                if (!yml.getConfig().contains(configName)) {
                    sender.sendMessage(ChatColor.RED + "This chunk is not claimed.");
                    return true;
                }
                if (!yml.getConfig().getString(configName + ".owner").equalsIgnoreCase(player.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.RED + "You don't own this chunk.");
                    return true;
                }

                yml.getConfig().set(configName, null);
                yml.saveConfig();
                CheckBlockEvent.chunkDataMap.remove(configName);

                sender.sendMessage(ChatColor.AQUA + "You have unclaimed this chunk.");
            } else if (args[0].equalsIgnoreCase("invite")) {
                sendHelp(sender);
            } else if (args[0].equalsIgnoreCase("revoke")) {
                sendHelp(sender);
            } else if (args[0].equalsIgnoreCase("info")) {
                int chunkX = player.getLocation().getBlockX() >> 4;
                int chunkZ = player.getLocation().getBlockZ() >> 4;
                String configName = chunkX + "|" + chunkZ;

                if (!yml.getConfig().contains(configName)) {
                    sender.sendMessage("This chunk is not claimed.");
                    return true;
                }

                String owner = plugin.getOfflinePlayer(UUID.fromString(yml.getConfig().getString(configName + ".owner"))).getName();
                List<String> invitedList = yml.getConfig().getStringList(configName + ".invited");
                String invited = "";
                for (String uuid : invitedList) {
                    invited += plugin.getOfflinePlayer(UUID.fromString(uuid)).getName() + "; ";
                }

                sender.sendMessage("Chunk X/Z: " + chunkX + "/" + chunkZ + "\nOwner: " + owner + "\nInvited: " + invited);
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite")) {
                int chunkX = player.getLocation().getBlockX() >> 4;
                int chunkZ = player.getLocation().getBlockZ() >> 4;
                String configName = chunkX + "|" + chunkZ;

                if (!yml.getConfig().contains(configName)) {
                    sender.sendMessage(ChatColor.RED + "This chunk is not claimed.");
                    return true;
                }
                if (!yml.getConfig().getString(configName + ".owner").equalsIgnoreCase(player.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.RED + "You don't own this chunk.");
                    return true;
                }
                String uuid = plugin.getOfflinePlayer(args[1]).getUniqueId().toString();

                List<String> invited = yml.getConfig().getStringList(configName + ".invited");
                if (invited.contains(uuid)) {
                    sender.sendMessage(ChatColor.RED + "That player is already invited.");
                    return true;
                }
                invited.add(uuid);
                yml.getConfig().set(configName + ".invited", invited);
                yml.saveConfig();
                CheckBlockEvent.chunkDataMap.remove(configName);

                sender.sendMessage(ChatColor.AQUA + "You invited " + args[1] + " to edit this chunk.");
            } else if (args[0].equalsIgnoreCase("revoke")) {
                int chunkX = player.getLocation().getBlockX() >> 4;
                int chunkZ = player.getLocation().getBlockZ() >> 4;
                String configName = chunkX + "|" + chunkZ;

                if (!yml.getConfig().contains(configName)) {
                    sender.sendMessage(ChatColor.RED + "This chunk is not claimed.");
                    return true;
                }
                if (!yml.getConfig().getString(configName + ".owner").equalsIgnoreCase(player.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.RED + "You don't own this chunk.");
                    return true;
                }
                String uuid = plugin.getOfflinePlayer(args[1]).getUniqueId().toString();

                List<String> invited = yml.getConfig().getStringList(configName + ".invited");
                if (!invited.contains(uuid)) {
                    sender.sendMessage(ChatColor.RED + "That player is not already invited.");
                    return true;
                }

                invited.remove(uuid);
                yml.getConfig().set(configName + ".invited", invited);
                yml.saveConfig();
                CheckBlockEvent.chunkDataMap.remove(configName);

                sender.sendMessage(ChatColor.AQUA + "You revoked " + args[1] + "'s invite to edit this chunk.");
            } else {
                sendHelp(sender);
            }
        }
        return true;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage("The correct usage is:\n - add\n - remove\n - invite <player>\n - revoke <player>");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> cmd = new ArrayList<>();
            cmd.add("add");
            cmd.add("remove");
            cmd.add("invite");
            cmd.add("revoke");
            cmd.add("info");
            return cmd;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite")) {
                return plugin.getAllPlayers();
            } else if (args[0].equalsIgnoreCase("revoke")) {
                return plugin.getAllPlayers();
            }
        }

        return new ArrayList<>();
    }
}
