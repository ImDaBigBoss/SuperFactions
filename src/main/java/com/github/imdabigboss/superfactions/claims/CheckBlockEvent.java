package com.github.imdabigboss.superfactions.claims;

import com.github.imdabigboss.superfactions.SuperFactions;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;


public class CheckBlockEvent {
    public static boolean checkBlockEvent(Player player, Chunk chunk) {
        if (player == null) {
            return false;
        }

        String chunkName = chunk.getX() + "|" + chunk.getZ() + "|" + player.getLocation().getWorld().getName();

        if (SuperFactions.chunkDataMap.get(chunkName).isClaimed()) {
            if (SuperFactions.chunkDataMap.get(chunkName).isReserved()) {
                return false;
            }
            return !SuperFactions.chunkDataMap.get(chunkName).hasPermissions(player);
        } else {
            return false;
        }
    }
}
