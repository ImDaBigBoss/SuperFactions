package com.github.imdabigboss.superfactions.claims;

import com.github.imdabigboss.superfactions.SuperFactions;
import com.github.imdabigboss.superfactions.YMLUtils;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CheckBlockEvent {
    public static Map<String, ChunkData> chunkDataMap = new HashMap<>();

    public static boolean checkBlockEvent(Player player, Chunk chunk) {
        if (player == null) {
            return false;
        }

        UUID playerUUID = player.getUniqueId();
        String chunkName = chunk.getX() + "|" + chunk.getZ();
        YMLUtils yml = SuperFactions.getClaimsYML();

        if (chunkDataMap.containsKey(chunkName)) {
            return !chunkDataMap.get(chunkName).hasPermissions(playerUUID.toString());
        }

        if (yml.getConfig().contains(chunkName)) {
            if (yml.getConfig().get(chunkName) == null) {
                return false;
            }

            List<String> invited = yml.getConfig().getStringList(chunkName + ".invited");
            ChunkData data = new ChunkData(yml.getConfig().getString(chunkName + ".owner"), yml.getConfig().getInt(chunkName + ".chunkX"), yml.getConfig().getInt(chunkName + ".chunkZ"), invited);

            if (chunkDataMap.size() > 1000) {
                chunkDataMap.clear();
            }
            chunkDataMap.put(chunkName, data);

            return !data.hasPermissions(playerUUID.toString());
        } else {
            return false;
        }
    }
}
