package com.github.imdabigboss.superfactions.claims;

import org.bukkit.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.util.*;

public class ChunkData {
    private boolean isClaimed;
    private String owner = "";
    private int chunkX = 0;
    private int chunkZ = 0;
    private String world;
    private List<String> invited;

    public ChunkData(boolean isClaimed, String world) {
        this.isClaimed = isClaimed;
        this.world = world;
        this.invited = new ArrayList<>();
    }

    public ChunkData(String owner, int chunkX, int chunkZ, String world) {
        new ChunkData(owner, chunkX, chunkZ, world, new ArrayList<>());
    }

    public ChunkData(String owner, int chunkX, int chunkZ, String world, List<String> invited) {
        this.isClaimed = true;
        this.owner = owner;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.world = world;
        this.invited = invited;
    }

    public String getOwner() {
        return owner;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public String getWorld() {
        return world;
    }

    public String getChunkName() {
        return chunkX + "|" + chunkZ;
    }

    public void addInvited(Player player) {
        addInvited(player.getUniqueId().toString());
    }

    public void addInvited(String playerUUID) {
        invited.add(playerUUID);
    }

    public void removeInvited(Player player) {
        removeInvited(player.getUniqueId().toString());
    }

    public void removeInvited(String playerUUID) {
        invited.remove(playerUUID);
    }

    public boolean isOwner(Player player) {
        return isOwner(player.getUniqueId().toString());
    }

    public boolean isOwner(String playerUUID) {
        return owner.equalsIgnoreCase(playerUUID);
    }

    public boolean hasPermissions(Player player) {
        return hasPermissions(player.getUniqueId().toString());
    }

    public boolean hasPermissions(String playerUUID) {
        return invited.contains(playerUUID) || owner.equalsIgnoreCase(playerUUID);
    }

    public boolean isClaimed() {
        return isClaimed;
    }

    public void setIsClaimed(boolean claimed) {
        isClaimed = claimed;
    }
}
