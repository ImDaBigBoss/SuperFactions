package com.github.imdabigboss.superfactions;

import java.util.*;
import java.util.logging.Logger;

import com.github.imdabigboss.superfactions.claims.ChunkData;
import com.github.imdabigboss.superfactions.commands.*;
import com.github.imdabigboss.superfactions.shop.ItemPrices;
import com.github.imdabigboss.superfactions.shop.ShopNPC;

import net.jitse.npclib.NPCLib;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperFactions extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");
    private static SuperFactions instance = null;
    private static Economy_SuperFactions economy = null;
    private static ItemPrices prices = null;
    private static NPCLib npcLib;
    private static ShopNPC shopNPC;
    private static YMLUtils claimsYML = null;

    public static String currencyPrefix = "$";
    public static String currencySuffix = "";
    public static String currencyName = "";
    public static String currencyNamePlural = "";
    public static double chunkPrice = 100;
    public static Map<String, ChunkData> chunkDataMap = new HashMap<>();
    public static Map<String, Integer> particlesShow = new HashMap<>();
    public static List<String> claimBypass = new ArrayList<>();

    private Particle.DustOptions claimDustOptions = new Particle.DustOptions(Color.fromRGB(0, 255, 0), 2);
    private Particle.DustOptions reservedClaimDustOptions = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 2);

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        log.info(String.format("[%s] Enabled Version %s", getDescription().getName(), getDescription().getVersion()));

        economy = new Economy_SuperFactions(this);
        prices = new ItemPrices(this);
        npcLib = new NPCLib(this);
        shopNPC = new ShopNPC(this);
        claimsYML = new YMLUtils("claims.yml");

        if (getConfig().contains("version")) {
            if (!getConfig().getString("version").equalsIgnoreCase(getDescription().getVersion())) {
                log.warning("Your config is not up to date, please regenerate it! You are using " + getConfig().getString("version") + " and the current version is " + getDescription().getVersion());
            }
        } else {
            log.warning("Your config has no version string, please regenerate it!");
        }

        if (getConfig().contains("currencyPrefix")) {
            currencyPrefix = getConfig().getString("currencyPrefix");
        }
        if (getConfig().contains("currencySuffix")) {
            currencySuffix = getConfig().getString("currencySuffix");
        }

        if (getConfig().contains("currencyName")) {
            currencyName = getConfig().getString("currencyName");
        }
        if (getConfig().contains("currencyNamePlural")) {
            currencyNamePlural = getConfig().getString("currencyNamePlural");
        }

        if (getConfig().contains("chunkPrice")) {
            chunkPrice = getConfig().getDouble("chunkPrice");
        }
        getServer().getPluginManager().registerEvents(new EventListener(this), this);

        this.getCommand("superfactions").setExecutor(new SuperFactionsCommand(this));
        this.getCommand("balance").setExecutor(new BalanceCommand(this));
        this.getCommand("money").setExecutor(new MoneyCommand(this));
        this.getCommand("shop").setExecutor(new ShopCommand(this));
        this.getCommand("dailycrate").setExecutor(new DailyCrateCommand(this));
        this.getCommand("opencrate").setExecutor(new OpenCrateCommand(this));
        this.getCommand("givecrate").setExecutor(new GiveCrateCommand(this));
        this.getCommand("claim").setExecutor(new ClaimCommand(this));

        Plugin vault = getServer().getPluginManager().getPlugin("Vault");
        if (vault != null) {
            log.info(String.format("[%s] Enabled Vault connector. Vault version: %s", getDescription().getName(), vault.getDescription().getVersion()));
            getServer().getServicesManager().register(Economy.class, new VaultConnector(this), this, ServicePriority.Highest);
        }
        shopNPC.createNPC();

        for (Player player : getServer().getOnlinePlayers()) {
            if (this.getConfig().contains("claims." + player.getUniqueId() + ".particles")) {
                String showing = this.getConfig().getString("claims." + player.getUniqueId() + ".particles");
                if (showing == "show") {
                    particlesShow.put(player.getName(), 2);
                } else if (showing == "hide") {
                    particlesShow.put(player.getName(), 0);
                } else if(showing == "minimal") {
                    particlesShow.put(player.getName(), 8);
                } else {
                    SuperFactions.particlesShow.put(player.getName(), 2);
                }
            } else {
                particlesShow.put(player.getName(), 2);
            }
        }

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                if (chunkDataMap.size() == 10000) {
                    chunkDataMap.clear();
                }

                for (Player player : getServer().getOnlinePlayers()) {
                    if (particlesShow.containsKey(player.getName())) {
                        if (particlesShow.get(player.getName()) == 0) {
                            int chunkX = player.getLocation().getBlockX() >> 4;
                            int chunkZ = player.getLocation().getBlockZ() >> 4;
                            String world = player.getLocation().getWorld().getName();

                            for (int cx = chunkX - 1; cx <= chunkX + 1; cx++) {
                                for (int cz = chunkZ - 1; cz <= chunkZ + 1; cz++) {
                                    String chunkName = cx + "|" + cz + "|" + world;
                                    loadChunkToClaimMap(chunkName, world);
                                }
                            }
                            continue;
                        }
                    }

                    int chunkX = player.getLocation().getBlockX() >> 4;
                    int chunkZ = player.getLocation().getBlockZ() >> 4;
                    String world = player.getLocation().getWorld().getName();

                    for (int cx = chunkX - 4; cx <= chunkX + 4; cx++) {
                        for (int cz = chunkZ - 4; cz <= chunkZ + 4; cz++) {
                            String chunkName = cx + "|" + cz + "|" + world;

                            loadChunkToClaimMap(chunkName, world);

                            boolean isReserved = chunkDataMap.get(chunkName).isReserved();

                            if (!isReserved) {
                                if (!chunkDataMap.get(chunkName).isClaimed()) {
                                    continue;
                                }
                            }

                            int minX = cx * 16;
                            int maxX = minX + 16;

                            int minY = player.getLocation().getBlockY() - 10;
                            int maxY = minY + 20;

                            int minZ = cz * 16;
                            int maxZ = minZ + 16;

                            String currentOwner = chunkDataMap.get(chunkName).getOwner();
                            int particlesAmount = 2;
                            if (particlesShow.containsKey(player.getName())) {
                                particlesAmount = particlesShow.get(player.getName());
                            }

                            if (isReserved) {
                                loadChunkToClaimMap(cx, cz - 1, world);
                                if (!chunkDataMap.get(cx + "|" + (cz - 1) + "|" + world).isReserved()) {
                                    for (int x = minX; x <= maxX; x += particlesAmount) {
                                        for (int y = minY; y <= maxY; y += particlesAmount) {
                                            player.spawnParticle(Particle.REDSTONE, x, y, minZ, 1, reservedClaimDustOptions);
                                        }
                                    }
                                }

                                loadChunkToClaimMap(cx, cz + 1, world);
                                if (!chunkDataMap.get(cx + "|" + (cz + 1) + "|" + world).isReserved()) {
                                    for (int x = minX; x <= maxX; x += particlesAmount) {
                                        for (int y = minY; y <= maxY; y += particlesAmount) {
                                            player.spawnParticle(Particle.REDSTONE, x, y, maxZ, 1, reservedClaimDustOptions);
                                        }
                                    }
                                }

                                loadChunkToClaimMap(cx - 1, cz, world);
                                if (!chunkDataMap.get((cx - 1) + "|" + cz + "|" + world).isReserved()) {
                                    for (int z = minZ; z <= maxZ; z += particlesAmount) {
                                        for (int y = minY; y <= maxY; y += particlesAmount) {
                                            player.spawnParticle(Particle.REDSTONE, minX, y, z, 1, reservedClaimDustOptions);
                                        }
                                    }
                                }

                                loadChunkToClaimMap(cx + 1, cz, world);
                                if (!chunkDataMap.get((cx + 1) + "|" + cz + "|" + world).isReserved()) {
                                    for (int z = minZ; z <= maxZ; z += particlesAmount) {
                                        for (int y = minY; y <= maxY; y += particlesAmount) {
                                            player.spawnParticle(Particle.REDSTONE, maxX, y, z, 1, reservedClaimDustOptions);
                                        }
                                    }
                                }
                            } else {
                                loadChunkToClaimMap(cx, cz - 1, world);
                                if ((!chunkDataMap.get(cx + "|" + (cz - 1) + "|" + world).getOwner().equalsIgnoreCase(currentOwner))) {
                                    for (int x = minX; x <= maxX; x += particlesAmount) {
                                        for (int y = minY; y <= maxY; y += particlesAmount) {
                                            player.spawnParticle(Particle.REDSTONE, x, y, minZ, 1, claimDustOptions);
                                        }
                                    }
                                }

                                loadChunkToClaimMap(cx, cz + 1, world);
                                if ((!chunkDataMap.get(cx + "|" + (cz + 1) + "|" + world).getOwner().equalsIgnoreCase(currentOwner))) {
                                    for (int x = minX; x <= maxX; x += particlesAmount) {
                                        for (int y = minY; y <= maxY; y += particlesAmount) {
                                            player.spawnParticle(Particle.REDSTONE, x, y, maxZ, 1, claimDustOptions);
                                        }
                                    }
                                }

                                loadChunkToClaimMap(cx - 1, cz, world);
                                if ((!chunkDataMap.get((cx - 1) + "|" + cz + "|" + world).getOwner().equalsIgnoreCase(currentOwner))) {
                                    for (int z = minZ; z <= maxZ; z += particlesAmount) {
                                        for (int y = minY; y <= maxY; y += particlesAmount) {
                                            player.spawnParticle(Particle.REDSTONE, minX, y, z, 1, claimDustOptions);
                                        }
                                    }
                                }

                                loadChunkToClaimMap(cx + 1, cz, world);
                                if ((!chunkDataMap.get((cx + 1) + "|" + cz + "|" + world).getOwner().equalsIgnoreCase(currentOwner))) {
                                    for (int z = minZ; z <= maxZ; z += particlesAmount) {
                                        for (int y = minY; y <= maxY; y += particlesAmount) {
                                            player.spawnParticle(Particle.REDSTONE, maxX, y, z, 1, claimDustOptions);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }, 20l, 10l);
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
        shopNPC.destoryNPC();
        economy.saveEconomy();
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    /**
     * Load a chunk to the claim map
     * @param chunkX Chunk X coordinate
     * @param chunkZ Chunk Y coordinate
     */
    public void loadChunkToClaimMap(int chunkX, int chunkZ, String world) {
        loadChunkToClaimMap(chunkX + "|" + chunkZ + "|" + world, world);
    }

    /**
     * Load a chunk to the claim map
     * @param chunkName "x|z" coordinates
     */
    public void loadChunkToClaimMap(String chunkName, String world) {
        if (!chunkDataMap.containsKey(chunkName)) {
            if (claimsYML.getConfig().contains(chunkName)) {
                if (claimsYML.getConfig().get(chunkName) == null) {
                    chunkDataMap.put(chunkName, new ChunkData(false, false, world));
                } else {
                    if (claimsYML.getConfig().getBoolean(chunkName + ".isReserved")) {
                        chunkDataMap.put(chunkName, new ChunkData(true, true, world));
                    } else {
                        List<String> invited = claimsYML.getConfig().getStringList(chunkName + ".invited");
                        ChunkData data = new ChunkData(claimsYML.getConfig().getString(chunkName + ".owner"), world, invited);
                        chunkDataMap.put(chunkName, data);
                    }
                }
            } else {
                chunkDataMap.put(chunkName, new ChunkData(false, false, world));
            }
        }
    }

    /**
     * Get if a command sender has the admin permissions or if they are the console
     * @param sender The command sender
     * @return true if the player is an admin, false if not
     */
    public boolean isAdmin(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        } else if (sender instanceof Player) {
            if (((Player)sender).hasPermission("superfactions.admin")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get a list of all online players
     * @return A list of strings
     */
    public List<String> getAllPlayers() {
        List<String> players = new ArrayList<>();
        for (Player player : getServer().getOnlinePlayers()) {
            players.add(player.getName());
        }
        return players;
    }

    /**
     * Get an offline player by a Bukkit Player class
     * @param player The player in question
     * @return The offline player
     */
    public OfflinePlayer getOfflinePlayer(Player player) {
        return getServer().getOfflinePlayer(player.getUniqueId());
    }

    /**
     * Get an offline player from a UUID
     * @param uuid The player's UUID
     * @return The offline player (can be null)
     */
    public OfflinePlayer getOfflinePlayer(UUID uuid) {
        return getServer().getOfflinePlayer(uuid);
    }

    /**
     * Get an offline player from a String
     * This looks in the regiestered players in the config
     * @param name The player's name
     * @return The offline player (will be null if the player is not registered)
     */
    public OfflinePlayer getOfflinePlayer(String name) {
        if (!getConfig().contains("registeredPlayers." + name)) {
            return null;
        }

        return getOfflinePlayer(UUID.fromString(getConfig().getString("registeredPlayers." + name)));
    }

    public static Logger getLog() {
        return log;
    }
    public static Economy_SuperFactions getEconomy() {
        return economy;
    }
    public static ItemPrices getPrices() {
        return prices;
    }
    public static NPCLib getNPC() {
        return npcLib;
    }
    public static ShopNPC getShopNPC() {
        return shopNPC;
    }
    public static YMLUtils getClaimsYML() {
        return claimsYML;
    }

    public static SuperFactions getInstance() {
        return instance;
    }
}
