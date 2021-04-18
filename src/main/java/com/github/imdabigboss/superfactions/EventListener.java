package com.github.imdabigboss.superfactions;

import com.github.imdabigboss.superfactions.shop.ShopGUI;

import net.jitse.npclib.api.events.NPCInteractEvent;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
            plugin.saveConfig();
        }

        SuperFactions.getShopNPC().showNPC(player);
    }

    @EventHandler
    public void onNPCInteract(NPCInteractEvent event) {
        if (event.getNPC().getId().equalsIgnoreCase(plugin.getShopNPC().getNPCID())) {
            event.getWhoClicked().sendMessage(ChatColor.AQUA + "Welcome to the shop!");
            ShopGUI.openShop(event.getWhoClicked());
        }
    }
}
