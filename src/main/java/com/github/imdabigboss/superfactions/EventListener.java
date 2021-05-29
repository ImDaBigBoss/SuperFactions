package com.github.imdabigboss.superfactions;

import com.github.imdabigboss.superfactions.claims.CheckBlockEvent;
import com.github.imdabigboss.superfactions.shop.ShopGUI;

import net.jitse.npclib.api.events.NPCInteractEvent;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
    private SuperFactions plugin;

    public EventListener(SuperFactions plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getConfig().contains("registeredPlayers." + player.getName())) {
            plugin.getConfig().set("registeredPlayers." + player.getName(), player.getUniqueId().toString());
            plugin.getConfig().set("claims." + player.getUniqueId() + ".chunkAmount", 4);
            plugin.saveConfig();
        }

        if (plugin.getConfig().contains("claims." + player.getUniqueId() + ".particles")) {
            String showing = plugin.getConfig().getString("claims." + player.getUniqueId() + ".particles");
            if (showing == "show") {
                SuperFactions.particlesShow.put(player.getName(), 2);
            } else if (showing == "hide") {
                SuperFactions.particlesShow.put(player.getName(), 0);
            } else if(showing == "minimal") {
                SuperFactions.particlesShow.put(player.getName(), 8);
            } else {
                SuperFactions.particlesShow.put(player.getName(), 2);
            }
        } else {
            SuperFactions.particlesShow.put(player.getName(), 2);
        }

        SuperFactions.getShopNPC().forceShowNPC(player);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        OfflinePlayer offlinePlayer = plugin.getOfflinePlayer(player);
        if (offlinePlayer != null) {
            SuperFactions.getEconomy().saveEconomy(offlinePlayer);
        }
    }

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event) {
        if (event.getNPC().getId().equalsIgnoreCase(plugin.getShopNPC().getNPCID())) {
            event.getWhoClicked().sendMessage(ChatColor.AQUA + "Welcome to the shop!");
            ShopGUI.openShop(event.getWhoClicked());
        }
    }

    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        if (SuperFactions.claimsEnabled) {
            event.setCancelled(CheckBlockEvent.checkBlockEvent(event.getPlayer(), event.getBlock().getChunk()));
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (SuperFactions.claimsEnabled) {
            event.setCancelled(CheckBlockEvent.checkBlockEvent(event.getPlayer(), event.getBlock().getChunk()));
        }
    }

    @EventHandler
    public void onBlockFertilizeEvent(BlockFertilizeEvent event){
        if (SuperFactions.claimsEnabled) {
            event.setCancelled(CheckBlockEvent.checkBlockEvent(event.getPlayer(), event.getBlock().getChunk()));
        }
    }

    @EventHandler
    public void onIgniteEvent(BlockIgniteEvent event) {
        if (SuperFactions.claimsEnabled) {
            event.setCancelled(CheckBlockEvent.checkBlockEvent(event.getPlayer(), event.getBlock().getChunk()));
        }
    }
}
