package com.github.imdabigboss.superfactions.claims;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChunkData {
    private boolean isClaimed;
    private boolean isReserved;
    private String owner;
    private String world;
    private List<String> invited;

    public ChunkData(boolean isClaimed, boolean isReserved, String world) {
        this.isClaimed = isClaimed;
        this.isReserved = isReserved;
        this.owner = "";
        this.world = world;
        this.invited = new ArrayList<>();
    }

    public ChunkData(String owner, String world, List<String> invited) {
        this.isClaimed = true;
        this.isReserved = false;
        this.owner = owner;
        this.world = world;
        this.invited = invited;
    }

    public String getOwner() {
        return owner;
    }

    public String getWorld() {
        return world;
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

    public List<String> getInvited() {
        return invited;
    }

    public boolean isClaimed() {
        return isClaimed;
    }

    public boolean isReserved() {
        return isReserved;
    }
}
