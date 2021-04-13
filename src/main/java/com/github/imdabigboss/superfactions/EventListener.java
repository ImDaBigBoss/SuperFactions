package com.github.imdabigboss.superfactions;

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
        plugin.getConfig().set("registeredPlayers." + player.getName(), player.getUniqueId().toString());
        plugin.saveConfig();
    }
}
