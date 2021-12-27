package com.github.imdabigboss.superfactions.commands;

import com.github.imdabigboss.superfactions.SuperFactions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OpenCrateCommand implements CommandExecutor, TabExecutor {
    SuperFactions plugin;

    public OpenCrateCommand(SuperFactions plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
            return true;
        }

        if (!plugin.getConfig().getBoolean("enableCrates")) {
            sender.sendMessage("Crates are not enabled on this server.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            int dailyCrateCount = 0;
            if (plugin.getConfig().contains("crates." + player.getUniqueId().toString() + ".dailyCount")) {
                dailyCrateCount = plugin.getConfig().getInt("crates." + player.getUniqueId().toString() + ".dailyCount");
            }

            sender.sendMessage(ChatColor.AQUA + "You have " + dailyCrateCount + " daily crates.");
            sender.sendMessage(ChatColor.AQUA + "To open a crate, enter a crate name.");
            return true;
        }

        if (args[0].equalsIgnoreCase("daily")) {
            openDailyCrate(player);
        }

        return true;
    }

    private void openDailyCrate(Player player) {
        int crateCount = 0;
        if (plugin.getConfig().contains("crates." + player.getUniqueId().toString() + ".dailyCount")) {
            crateCount = plugin.getConfig().getInt("crates." + player.getUniqueId().toString() + ".dailyCount");
        }

        if (crateCount < 1) {
            player.sendMessage(ChatColor.RED + "You don't have a crate to open...");
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ChatColor.RED + "You need to free up some space in your inventory before claiming daily crates.");
            return;
        }

        crateCount -= 1;
        plugin.getConfig().set("crates." + player.getUniqueId().toString() + ".dailyCount", crateCount);
        plugin.saveConfig();

        Random r = new Random();
        int itemChance = r.nextInt(100);
        if (itemChance < 50) { //Random tool 50%
            List<ItemStack> items = getRandomTools();
            ItemStack item = items.get(r.nextInt(items.size()));

            player.getInventory().addItem(item);
            player.sendMessage(ChatColor.AQUA + "You got a " + ChatColor.GREEN + item.getItemMeta().getDisplayName() + ChatColor.AQUA + " from the daily crate!");
        } else if (itemChance < 75) { //Random armor 25%
            List<ItemStack> items = getRandomArmor();
            ItemStack item = items.get(r.nextInt(items.size()));

            player.getInventory().addItem(item);
            player.sendMessage(ChatColor.AQUA + "You got a " + ChatColor.GREEN + item.getItemMeta().getDisplayName() + ChatColor.AQUA + " from the daily crate!");
        } else if (itemChance < 76) { //Spawner 1%
            ItemStack item = new ItemStack(Material.SPAWNER);
            BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
            CreatureSpawner spawner = (CreatureSpawner) meta.getBlockState();

            EntityType entityType = getRandomSpawnerEntity();
            String entityName = entityType.toString().toLowerCase();
            spawner.setSpawnedType(entityType);

            String itemName = entityName.substring(0, 1).toUpperCase() + entityName.substring(1) + " spawner";
            meta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + itemName);
            meta.setBlockState(spawner);
            item.setItemMeta(meta);

            player.getInventory().addItem(item);
            player.sendMessage(ChatColor.AQUA + "You got a " + ChatColor.GREEN + itemName + ChatColor.AQUA + " from the daily crate!");
        } else if (itemChance < 79) { //Mending 3%
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
            meta.addStoredEnchant(Enchantment.MENDING, 1, true);
            book.setItemMeta(meta);

            player.getInventory().addItem(book);
            player.sendMessage(ChatColor.AQUA + "You got a " + ChatColor.GREEN + "Mending Book" + ChatColor.AQUA + " from the daily crate!");
        } else if (itemChance < 82) { //Slik touch 3%
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
            meta.addStoredEnchant(Enchantment.SILK_TOUCH, 1, true);
            book.setItemMeta(meta);

            player.getInventory().addItem(book);
            player.sendMessage(ChatColor.AQUA + "You got a " + ChatColor.GREEN + "Silk Touch Book" + ChatColor.AQUA + " from the daily crate!");
        } else if (itemChance < 85) { //Infinity 3%
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
            meta.addStoredEnchant(Enchantment.ARROW_INFINITE, 1, true);
            book.setItemMeta(meta);

            player.getInventory().addItem(book);
            player.sendMessage(ChatColor.AQUA + "You got an " + ChatColor.GREEN + "Infinity Book" + ChatColor.AQUA + " from the daily crate!");
        } else if (itemChance < 96) { //$20 11%
            SuperFactions.getEconomy().playerDeposit(plugin.getOfflinePlayer(player), 20);
            player.sendMessage(ChatColor.AQUA + "You got " + ChatColor.GREEN + SuperFactions.getEconomy().formatMoney(20) + ChatColor.AQUA + " from the daily crate!");
        } else if (itemChance < 99) { //$100 3%
            SuperFactions.getEconomy().playerDeposit(plugin.getOfflinePlayer(player), 100);
            player.sendMessage(ChatColor.AQUA + "You got " + ChatColor.GREEN + SuperFactions.getEconomy().formatMoney(100) + ChatColor.AQUA + " from the daily crate!");
        } else if (itemChance < 100) { //$1K 1%
            SuperFactions.getEconomy().playerDeposit(plugin.getOfflinePlayer(player), 1000);
            player.sendMessage(ChatColor.AQUA + "You got " + ChatColor.GREEN + SuperFactions.getEconomy().formatMoney(1000) + ChatColor.AQUA + " from the daily crate!");
        }
    }

    private List<ItemStack> getRandomTools() {
        List<ItemStack> items = new ArrayList<>();

        List<String> lore = new ArrayList<>();
        lore.add("Don't waste your good");
        lore.add("tools on your daily");
        lore.add("jobs, use me!");

        //Daily Axe
        ItemStack axe = new ItemStack(Material.GOLDEN_AXE);
        ItemMeta axeMeta = axe.getItemMeta();
        axeMeta.addEnchant(Enchantment.DURABILITY, 6, true);
        axeMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
        axeMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Daily Axe");
        axeMeta.setLore(lore);
        axe.setItemMeta(axeMeta);
        items.add(axe);

        //Daily Pickaxe
        ItemStack pickaxe = new ItemStack(Material.GOLDEN_PICKAXE);
        ItemMeta pickaxeMeta = axe.getItemMeta();
        pickaxeMeta.addEnchant(Enchantment.DURABILITY, 6, true);
        pickaxeMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
        pickaxeMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Daily Pickaxe");
        pickaxeMeta.setLore(lore);
        pickaxe.setItemMeta(pickaxeMeta);
        items.add(pickaxe);

        //Daily Shovel
        ItemStack shovel = new ItemStack(Material.GOLDEN_SHOVEL);
        ItemMeta shovelMeta = axe.getItemMeta();
        shovelMeta.addEnchant(Enchantment.DURABILITY, 6, true);
        shovelMeta.addEnchant(Enchantment.DIG_SPEED, 3, true);
        shovelMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Daily Shovel");
        shovelMeta.setLore(lore);
        shovel.setItemMeta(shovelMeta);
        items.add(shovel);

        return items;
    }

    private List<ItemStack> getRandomArmor() {
        List<ItemStack> items = new ArrayList<>();

        //Daily Chestplate
        ItemStack chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE);
        ItemMeta chestplateMeta = chestplate.getItemMeta();
        chestplateMeta.addEnchant(Enchantment.DURABILITY, 6, true);
        chestplateMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
        chestplateMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Daily Chestplate");
        chestplate.setItemMeta(chestplateMeta);
        items.add(chestplate);

        //Daily Boots
        ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS);
        ItemMeta bootsMeta = boots.getItemMeta();
        bootsMeta.addEnchant(Enchantment.DURABILITY, 6, true);
        bootsMeta.addEnchant(Enchantment.PROTECTION_FALL, 2, true);
        bootsMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Daily Boots");
        boots.setItemMeta(bootsMeta);
        items.add(boots);

        //Daily Leggings
        ItemStack leggings = new ItemStack(Material.GOLDEN_LEGGINGS);
        ItemMeta leggingsMeta = leggings.getItemMeta();
        leggingsMeta.addEnchant(Enchantment.DURABILITY, 6, true);
        leggingsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
        leggingsMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Daily Leggings");
        leggings.setItemMeta(leggingsMeta);
        items.add(leggings);

        //Daily Helmet
        ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET);
        ItemMeta helmetMeta = helmet.getItemMeta();
        helmetMeta.addEnchant(Enchantment.DURABILITY, 6, true);
        helmetMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
        helmetMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Daily Helmet");
        helmet.setItemMeta(helmetMeta);
        items.add(helmet);

        return items;
    }

    public EntityType getRandomSpawnerEntity() {
        List<EntityType> entityTypes = new ArrayList<>();
        entityTypes.add(EntityType.PIG);
        entityTypes.add(EntityType.COW);
        entityTypes.add(EntityType.CHICKEN);
        entityTypes.add(EntityType.SHEEP);
        entityTypes.add(EntityType.SPIDER);
        entityTypes.add(EntityType.CREEPER);
        entityTypes.add(EntityType.ZOMBIE);

        Random r = new Random();
        EntityType entity = entityTypes.get(r.nextInt(entityTypes.size()));

        return entity;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> cmds = new ArrayList<>();
        cmds.add("daily");
        return cmds;
    }
}
