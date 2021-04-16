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

    public ShopNPC(SuperFactions plugin) {
        this.plugin = plugin;
        this.npcLib = plugin.getNPC();
    }

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
        text.add("Merchant");

        MineSkinFetcher.fetchSkinFromIdSync(plugin.getConfig().getInt("shopNPCSkin"), skin -> {
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

    public void destoryNPC() {
        if (!npcExists) {
            return;
        }

        npc.destroy();
        npcExists = false;
        plugin.getLog().info(String.format("[%s] Destroyed shop NPC", plugin.getDescription().getName()));
    }

    public void setPos(Location location) {
        plugin.getConfig().set("shopNPCLocation", location);
        plugin.saveConfig();
    }

    public void showNPC() {
        if (npcExists) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                showNPC(player);
            }
        }
    }

    public void showNPC(Player player) {
        if (npcExists) {
            npc.show(player);
        }
    }

    public void hideNPC() {
        if (!npcExists) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                hideNPC(player);
            }
        }
    }

    public void hideNPC(Player player) {
        if (npcExists) {
            npc.hide(player);
        }
    }

    public void updateNPCPos() {
        destoryNPC();
        createNPC();
    }

    public boolean getNPCExists() {
        return npcExists;
    }
    public String getNPCID() {
        return npcID;
    }
}
