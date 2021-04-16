package com.github.imdabigboss.superfactions.shop;

import com.github.imdabigboss.superfactions.SuperFactions;

import java.util.ArrayList;
import java.util.List;

import net.jitse.npclib.NPCLib;
import net.jitse.npclib.api.NPC;
import net.jitse.npclib.api.skin.MineSkinFetcher;
import net.jitse.npclib.api.skin.Skin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ShopNPC {
    private SuperFactions plugin;
    private NPCLib npcLib;
    private NPC npc;
    private boolean npcExists = false;
    private String npcID = "";
    private String npcName = "Merchant";

    public ShopNPC(SuperFactions plugin) {
        this.plugin = plugin;
        this.npcLib = plugin.getNPC();

        if (plugin.getConfig().contains("shopNPCName")) {
            npcName = plugin.getConfig().getString("shopNPCName");
        }
    }

    /**
     * Create the shop NPC
     */
    public void createNPC() {
        if (!plugin.getConfig().contains("shopNPCLocation")) {
            plugin.getLog().severe("You need to set an NPC location using /shop setpos");
            return;
        }

        if (npcExists) {
            return;
        }

        Location location = (Location) plugin.getConfig().get("shopNPCLocation");

        List<String> text = new ArrayList<>();
        text.add(npcName);

        MineSkinFetcher.fetchSkinFromIdSync(plugin.getConfig().getInt("shopNPCSkin"), skin -> { //Skin id for mineskin.org
            npc = npcLib.createNPC(text);
            npc.setLocation(location);
            npcID = npc.getId();
            npc.setSkin(skin);
            npc.create();
            npcExists = true;
            plugin.getLog().info(String.format("[%s] Created shop NPC at: %f %f %f", plugin.getDescription().getName(), location.getX(), location.getY(), location.getZ()));

            showNPC();
        });
    }

    /**
     * Destory the shop NPC
     */
    public void destoryNPC() {
        if (!npcExists) {
            return;
        }

        npc.destroy();
        npcExists = false;
        plugin.getLog().info(String.format("[%s] Destroyed shop NPC", plugin.getDescription().getName()));
    }

    /**
     * Set the shop NPC's spawn location
     * @param location The location to spawn the NPC at
     */
    public void setPos(Location location) {
        plugin.getConfig().set("shopNPCLocation", location);
        plugin.saveConfig();
    }

    /**
     * Send the shop NPC packets to the connected players
     */
    public void showNPC() {
        if (npcExists) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                showNPC(player);
            }
        }
    }

    /**
     * Send the shop NPC packets to a player
     * @param player The player in question
     */
    public void showNPC(Player player) {
        if (npcExists) {
            npc.show(player);
        }
    }

    /**
     * Hide the shop NPC for all connected players
     */
    public void hideNPC() {
        if (!npcExists) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                hideNPC(player);
            }
        }
    }

    /**
     * Hide the shop NPC for a player
     * @param player The player in question
     */
    public void hideNPC(Player player) {
        if (npcExists) {
            npc.hide(player);
        }
    }

    /**
     * Update the NPC position by recreating it
     */
    public void updateNPCPos() {
        destoryNPC();
        createNPC();
    }

    /**
     * Get if the shop NPC exists/is created
     * @return
     */
    public boolean getNPCExists() {
        return npcExists;
    }

    /**
     * Get the shop NPC's ID
     * @return
     */
    public String getNPCID() {
        return npcID;
    }
}
