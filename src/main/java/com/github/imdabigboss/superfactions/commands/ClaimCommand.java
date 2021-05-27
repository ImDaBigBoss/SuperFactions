package com.github.imdabigboss.superfactions.commands;

import com.github.imdabigboss.superfactions.SuperFactions;
import com.github.imdabigboss.superfactions.YMLUtils;
import com.github.imdabigboss.superfactions.claims.ChunkData;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
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
        }

        if (args[0].equalsIgnoreCase("add")) {
            if (args.length != 1) {
                sendHelp(sender);
                return true;
            }

            int chunkAmount = getChunkAmount(player);
            if (chunkAmount < 1) {
                sender.sendMessage(ChatColor.RED + "You don't have any chunks left to claim! Get more with \"/claim buy\" for " + SuperFactions.getEconomy().formatMoney(SuperFactions.chunkPrice));
                return true;
            }

            int chunkX = player.getLocation().getBlockX() >> 4;
            int chunkZ = player.getLocation().getBlockZ() >> 4;
            String configName = chunkX + "|" + chunkZ + "|" + player.getLocation().getWorld().getName();

            if (yml.getConfig().contains(configName)) {
                if (yml.getConfig().getBoolean(configName + ".isReserved")) {
                    sender.sendMessage(ChatColor.RED + "This chunk is reserved, you can't claim it!");
                    return true;
                }
                String playerName = plugin.getOfflinePlayer(UUID.fromString(yml.getConfig().getString(configName + ".owner"))).getName();

                sender.sendMessage(ChatColor.RED + "This chunk is already claimed by " + playerName);
                return true;
            }

            chunkAmount -= 1;
            plugin.getConfig().set("claims." + player.getUniqueId() + ".chunkAmount", chunkAmount);
            plugin.saveConfig();

            List<String> invited;
            if (plugin.getConfig().contains("claims." + player.getUniqueId() + ".default")) {
                invited = plugin.getConfig().getStringList("claims." + player.getUniqueId() + ".default");
            } else {
                invited = new ArrayList<>();
            }

            yml.getConfig().set(configName + ".owner", player.getUniqueId().toString());
            yml.getConfig().set(configName + ".invited", invited);
            yml.getConfig().set(configName + ".isReserved", false);
            yml.saveConfig();

            SuperFactions.chunkDataMap.replace(configName, new ChunkData(player.getUniqueId().toString(), player.getLocation().getWorld().getName(), invited));

            sender.sendMessage(ChatColor.AQUA + "You have just claimed this chunk, and now have " + chunkAmount + " chunk(s) left to claim.");
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length != 1) {
                sendHelp(sender);
                return true;
            }

            int chunkX = player.getLocation().getBlockX() >> 4;
            int chunkZ = player.getLocation().getBlockZ() >> 4;
            String configName = chunkX + "|" + chunkZ + "|" + player.getLocation().getWorld().getName();

            if (!yml.getConfig().contains(configName)) {
                sender.sendMessage(ChatColor.RED + "This chunk is not claimed.");
                return true;
            }
            boolean isReserved = yml.getConfig().getBoolean(configName + ".isReserved");
            if (isReserved) {
                if (!plugin.isAdmin(sender)) {
                    sender.sendMessage(ChatColor.RED + "You can't remove reserved chunks if you are not admin!");
                    return true;
                }
            } else {
                if (!yml.getConfig().getString(configName + ".owner").equalsIgnoreCase(player.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.RED + "You don't own this chunk.");
                    return true;
                }
            }

            yml.getConfig().set(configName, null);
            yml.saveConfig();
            SuperFactions.chunkDataMap.replace(configName, new ChunkData(false, false, player.getLocation().getWorld().getName()));

            if (!isReserved) {
                int chunkAmount = getChunkAmount(player) + 1;
                plugin.getConfig().set("claims." + player.getUniqueId() + ".chunkAmount", chunkAmount);
                plugin.saveConfig();

                sender.sendMessage(ChatColor.AQUA + "You have unclaimed this chunk, and now have " + chunkAmount + " chunk(s) left to claim.");
            } else {
                sender.sendMessage(ChatColor.AQUA + "You un-reserved this chunk.");
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length != 1) {
                sendHelp(sender);
                return true;
            }

            int chunkX = player.getLocation().getBlockX() >> 4;
            int chunkZ = player.getLocation().getBlockZ() >> 4;
            String configName = chunkX + "|" + chunkZ + "|" + player.getLocation().getWorld().getName();

            if (!SuperFactions.chunkDataMap.get(configName).isClaimed()) {
                sender.sendMessage("This chunk is not claimed.");
                return true;
            }

            if (SuperFactions.chunkDataMap.get(configName).isReserved()) {
                sender.sendMessage("This chunk is reserved.");
                return true;
            }

            String owner = plugin.getOfflinePlayer(UUID.fromString(SuperFactions.chunkDataMap.get(configName).getOwner())).getName();
            List<String> invitedList = SuperFactions.chunkDataMap.get(configName).getInvited();
            String invited = "";
            for (String uuid : invitedList) {
                invited += plugin.getOfflinePlayer(UUID.fromString(uuid)).getName() + "; ";
            }

            sender.sendMessage("Chunk X/Z: " + chunkX + "/" + chunkZ + "\nOwner: " + owner + "\nInvited: " + invited);
        } else if (args[0].equalsIgnoreCase("buy")) {
            if (args.length != 1) {
                sendHelp(sender);
                return true;
            }

            OfflinePlayer offlinePlayer = plugin.getOfflinePlayer(player);
            if (!SuperFactions.getEconomy().playerCanAfford(offlinePlayer, SuperFactions.chunkPrice)) {
                sender.sendMessage(ChatColor.RED + "You can't afford to buy this chunk. You need at least " + SuperFactions.getEconomy().formatMoney(SuperFactions.chunkPrice));
                return true;
            }

            SuperFactions.getEconomy().playerWithdraw(offlinePlayer, SuperFactions.chunkPrice);
            plugin.getConfig().set("claims." + player.getUniqueId() + ".chunkAmount", getChunkAmount(player) + 1);
            plugin.saveConfig();

            sender.sendMessage(ChatColor.AQUA + "You bought a chunk for " + SuperFactions.getEconomy().formatMoney(SuperFactions.chunkPrice) + "!");
        } else if (args[0].equalsIgnoreCase("chunkamount")) {
            if (args.length != 1) {
                sendHelp(sender);
                return true;
            }

            sender.sendMessage("You have " + getChunkAmount(player) + " chunk(s) left to claim.");
        } else if (args[0].equalsIgnoreCase("reserve")) {
            if (!plugin.isAdmin(sender)) {
                sendHelp(sender);
                return true;
            }

            if (args.length != 1) {
                sendHelp(sender);
                return true;
            }

            int chunkX = player.getLocation().getBlockX() >> 4;
            int chunkZ = player.getLocation().getBlockZ() >> 4;
            String world = player.getLocation().getWorld().getName();
            String configName = chunkX + "|" + chunkZ + "|" + world;

            if (yml.getConfig().contains(configName)) {
                if (yml.getConfig().getBoolean(configName + ".isReserved")) {
                    sender.sendMessage(ChatColor.RED + "This chunk is already reserved!");
                    return true;
                }
                String playerName = plugin.getOfflinePlayer(UUID.fromString(yml.getConfig().getString(configName + ".owner"))).getName();

                sender.sendMessage(ChatColor.RED + "This chunk is already claimed by " + playerName);
                return true;
            }

            yml.getConfig().set(configName + ".isReserved", true);
            yml.saveConfig();

            SuperFactions.chunkDataMap.replace(configName, new ChunkData(true, true, world));

            sender.sendMessage(ChatColor.AQUA + "This chunk is now reserved! To un-reserve it, use \"/claim remove\".");
        } else if (args[0].equalsIgnoreCase("invite")) {
            if (args.length != 2) {
                sendHelp(sender);
                return true;
            }

            int chunkX = player.getLocation().getBlockX() >> 4;
            int chunkZ = player.getLocation().getBlockZ() >> 4;
            String configName = chunkX + "|" + chunkZ + "|" + player.getLocation().getWorld().getName();

            if (!yml.getConfig().contains(configName)) {
                sender.sendMessage(ChatColor.RED + "This chunk is not claimed.");
                return true;
            }
            if (!yml.getConfig().getString(configName + ".owner").equalsIgnoreCase(player.getUniqueId().toString())) {
                sender.sendMessage(ChatColor.RED + "You don't own this chunk.");
                return true;
            }

            OfflinePlayer offlinePlayer = plugin.getOfflinePlayer(args[1]);
            if (offlinePlayer == null) {
                sender.sendMessage(ChatColor.RED + "That is not a registered player.");
                return true;
            }
            String uuid = offlinePlayer.getUniqueId().toString();

            List<String> invited = yml.getConfig().getStringList(configName + ".invited");
            if (invited.contains(uuid)) {
                sender.sendMessage(ChatColor.RED + "That player is already invited.");
                return true;
            }
            invited.add(uuid);
            yml.getConfig().set(configName + ".invited", invited);
            yml.saveConfig();
            SuperFactions.chunkDataMap.get(configName).addInvited(uuid);

            sender.sendMessage(ChatColor.AQUA + "You invited " + args[1] + " to edit this chunk.");
        } else if (args[0].equalsIgnoreCase("revoke")) {
            if (args.length != 2) {
                sendHelp(sender);
                return true;
            }

            int chunkX = player.getLocation().getBlockX() >> 4;
            int chunkZ = player.getLocation().getBlockZ() >> 4;
            String configName = chunkX + "|" + chunkZ + "|" + player.getLocation().getWorld().getName();

            if (!yml.getConfig().contains(configName)) {
                sender.sendMessage(ChatColor.RED + "This chunk is not claimed.");
                return true;
            }
            if (!yml.getConfig().getString(configName + ".owner").equalsIgnoreCase(player.getUniqueId().toString())) {
                sender.sendMessage(ChatColor.RED + "You don't own this chunk.");
                return true;
            }

            OfflinePlayer offlinePlayer = plugin.getOfflinePlayer(args[1]);
            if (offlinePlayer == null) {
                sender.sendMessage(ChatColor.RED + "That is not a registered player.");
                return true;
            }
            String uuid = offlinePlayer.getUniqueId().toString();

            List<String> invited = yml.getConfig().getStringList(configName + ".invited");
            if (!invited.contains(uuid)) {
                sender.sendMessage(ChatColor.RED + "That player is not already invited.");
                return true;
            }

            invited.remove(uuid);
            yml.getConfig().set(configName + ".invited", invited);
            yml.saveConfig();
            SuperFactions.chunkDataMap.get(configName).removeInvited(uuid);

            sender.sendMessage(ChatColor.AQUA + "You revoked " + args[1] + "'s invite to edit this chunk.");
        } else if (args[0].equalsIgnoreCase("particles")) {
            if (args.length != 2) {
                sendHelp(sender);
                return true;
            }

            if (args[1].equalsIgnoreCase("show")) {
                plugin.getConfig().set("claims." + player.getUniqueId() + ".particles", "show");
                plugin.saveConfig();
                SuperFactions.particlesShow.replace(player.getName(), 2);
                player.sendMessage(ChatColor.AQUA + "Showing all claim particles.");
            } else if (args[1].equalsIgnoreCase("hide")) {
                plugin.getConfig().set("claims." + player.getUniqueId() + ".particles", "hide");
                plugin.saveConfig();
                SuperFactions.particlesShow.replace(player.getName(), 0);
                player.sendMessage(ChatColor.AQUA + "Not showing any claim particles.");
            } else if (args[1].equalsIgnoreCase("minimal")) {
                plugin.getConfig().set("claims." + player.getUniqueId() + ".particles", "minimal");
                plugin.saveConfig();
                SuperFactions.particlesShow.replace(player.getName(), 8);
                player.sendMessage(ChatColor.AQUA + "Only showing minimal particles.");
            } else {
                sendHelp(sender);
                return true;
            }
        } else if (args[0].equalsIgnoreCase("defaultperms") && args[1].equalsIgnoreCase("list")) {
            if (args.length < 1) {
                sendHelp(sender);
                return true;
            }

            if (plugin.getConfig().contains("claims." + player.getUniqueId() + ".default")) {
                List<String> invited = plugin.getConfig().getStringList("claims." + player.getUniqueId() + ".default");
                String edit = "";

                for (String name : invited) {
                    edit += plugin.getOfflinePlayer(UUID.fromString(name)).getName() + "; ";
                }

                sender.sendMessage("These people can edit your new chunks: " + edit);
            } else {
                sender.sendMessage("Nobody can edit your new chunks by default.");
            }
        } else if (args[0].equalsIgnoreCase("defaultperms")) {
            OfflinePlayer offlinePlayer = plugin.getOfflinePlayer(args[2]);
            if (offlinePlayer == null) {
                sender.sendMessage(ChatColor.RED + "That is not a registered player.");
                return true;
            }
            String uuid = offlinePlayer.getUniqueId().toString();

            if (args[1].equalsIgnoreCase("invite")) {
                List<String> invited;
                if (plugin.getConfig().contains("claims." + player.getUniqueId() + ".default")) {
                    invited = plugin.getConfig().getStringList("claims." + player.getUniqueId() + ".default");
                } else {
                    invited = new ArrayList<>();
                }

                if (invited.contains(uuid)) {
                    sender.sendMessage(ChatColor.RED + "That player is already invited.");
                    return true;
                }

                invited.add(uuid);
                plugin.getConfig().set("claims." + player.getUniqueId() + ".default", invited);
                plugin.saveConfig();

                sender.sendMessage(ChatColor.AQUA + "You invited " + args[2] + " to edit your new chunks.");
            } else if (args[1].equalsIgnoreCase("revoke")) {
                List<String> invited;
                if (plugin.getConfig().contains("claims." + player.getUniqueId() + ".default")) {
                    invited = plugin.getConfig().getStringList("claims." + player.getUniqueId() + ".default");
                } else {
                    invited = new ArrayList<>();
                }

                if (!invited.contains(uuid)) {
                    sender.sendMessage(ChatColor.RED + "That player is not already invited.");
                    return true;
                }

                invited.remove(uuid);
                plugin.getConfig().set("claims." + player.getUniqueId() + ".default", invited);
                plugin.saveConfig();

                sender.sendMessage(ChatColor.AQUA + "You revoked " + args[2] + "'s invite to edit your new chunks.");
            } else {
                sendHelp(sender);
            }
        } else if (args[0].equalsIgnoreCase("bypass")) {
            if (SuperFactions.claimBypass.contains(player.getName())) {
                SuperFactions.claimBypass.remove(player.getName());

                sender.sendMessage(ChatColor.AQUA + "You can no longer bypass claim restrictions.");
            } else {
                SuperFactions.claimBypass.add(player.getName());

                sender.sendMessage(ChatColor.AQUA + "You can now bypass claim restrictions!");
            }
        } else {
            sendHelp(sender);
        }
        return true;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage("The correct usage is:" +
                "\n - add -> Claim a chunk" +
                "\n - remove -> Un-claim a chunk" +
                "\n - invite <player> -> Allow a player to edit in your chunk" +
                "\n - revoke <player> -> Disallow a player to edit in your chunk" +
                "\n - defaultperms invite -> Add a player who can edit your chunks by default" +
                "\n - defaultperms revoke -> Remove a player who can edit your chunks by default" +
                "\n - buy -> Buy a chunk for " + SuperFactions.getEconomy().formatMoney(SuperFactions.chunkPrice) +
                "\n - chunkamount -> Get the number of chunks you have left to claim." +
                "\n - particles <show/hide/minimal> -> Show less or no particles to remove lag");
    }

    public int getChunkAmount(Player player) {
        int amount = 0;

        if (plugin.getConfig().contains("claims." + player.getUniqueId() + ".chunkAmount")) {
            amount = plugin.getConfig().getInt("claims." + player.getUniqueId() + ".chunkAmount");
        }

        return amount;
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
            cmd.add("defaultperms");
            cmd.add("buy");
            cmd.add("chunkamount");
            cmd.add("particles");
            if (plugin.isAdmin(sender)) {
                cmd.add("reserve");
                cmd.add("bypass");
            }
            return cmd;
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("invite")) {
                return plugin.getAllPlayers();
            } else if (args[0].equalsIgnoreCase("revoke")) {
                return plugin.getAllPlayers();
            } else if (args[0].equalsIgnoreCase("defaultperms")) {
                List<String> cmd = new ArrayList<>();
                cmd.add("invite");
                cmd.add("revoke");
                cmd.add("list");
                return cmd;
            } else if (args[0].equalsIgnoreCase("particles")) {
                List<String> cmd = new ArrayList<>();
                cmd.add("show");
                cmd.add("hide");
                cmd.add("minimal");
                return cmd;
            }
        } else if (args.length == 3) {
            if (args[1].equalsIgnoreCase("invite")) {
                return plugin.getAllPlayers();
            } else if (args[1].equalsIgnoreCase("revoke")) {
                return plugin.getAllPlayers();
            }
        }

        return new ArrayList<>();
    }
}
