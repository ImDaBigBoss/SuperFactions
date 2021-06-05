package com.github.imdabigboss.superfactions.shop;

import com.github.imdabigboss.superfactions.SuperFactions;

import de.themoep.inventorygui.*;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ShopGUI {
    private static final String[] buyLayout = {
            "    a    ",
            "ggggggggg",
            "ggggggggg",
            "ggggggggg",
            "ggggggggg",
            "b   h   f"
    };
    private static final String[] buyItemLayout = {
            "    a    ",
            "  o   s  ",
            "    h    "
    };
    private static final String[] mainLayout = {
            "abcdefghi",
            "    j    "
    };
    private static final String[] sellLayout = {
            "ggggggggg",
            "ggggggggg",
            "ggggggggg",
            "ggggggggg",
            "b   h   f"
    };
    private static final Material fillerMaterial = Material.GRAY_STAINED_GLASS_PANE;
    private static final Material backItem = Material.PAPER;

    private static final Material[] netherItems = {
            Material.WARPED_STEM, Material.CRIMSON_STEM, Material.WARPED_PLANKS, Material.CRIMSON_PLANKS,
            Material.WARPED_ROOTS, Material.CRIMSON_ROOTS, Material.WARPED_FUNGUS, Material.CRIMSON_FUNGUS,
            Material.WARPED_WART_BLOCK, Material.WARPED_NYLIUM, Material.CRIMSON_NYLIUM, Material.BASALT,
            Material.POLISHED_BASALT, Material.BLACKSTONE, Material.POLISHED_BLACKSTONE, Material.CRYING_OBSIDIAN,
            Material.GILDED_BLACKSTONE, Material.SHROOMLIGHT, Material.SOUL_SOIL, Material.SOUL_SAND,
            Material.TWISTING_VINES, Material.WEEPING_VINES, Material.NETHER_BRICK, Material.NETHER_BRICKS,
            Material.NETHERRACK
    };

    private static final Material[] buildingItems = {
            Material.STONE, Material.COBBLESTONE, Material.ANDESITE, Material.GRANITE, Material.DIORITE, Material.GRAVEL,
            Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.MOSSY_COBBLESTONE, Material.CRACKED_STONE_BRICKS,
            Material.SMOOTH_STONE, Material.GRASS_BLOCK, Material.PODZOL, Material.DIRT,
            Material.OAK_LOG, Material.OAK_PLANKS, Material.SPRUCE_LOG, Material.SPRUCE_PLANKS,
            Material.BIRCH_LOG, Material.BIRCH_PLANKS, Material.JUNGLE_LOG, Material.JUNGLE_PLANKS,
            Material.ACACIA_LOG, Material.ACACIA_PLANKS, Material.DARK_OAK_LOG, Material.DARK_OAK_PLANKS,
            Material.WARPED_STEM, Material.WARPED_PLANKS, Material.CRIMSON_STEM, Material.CRIMSON_PLANKS,
            Material.GLASS, Material.GLASS_PANE, Material.SAND, Material.SANDSTONE, Material.OBSIDIAN
    };

    private static final Material[] redstoneItems = {
            Material.REDSTONE, Material.REDSTONE_BLOCK, Material.REDSTONE_TORCH, Material.DISPENSER, Material.DROPPER,
            Material.PISTON, Material.STICKY_PISTON, Material.REDSTONE_LAMP, Material.TRIPWIRE_HOOK,
            Material.STONE_BUTTON, Material.OAK_BUTTON, Material.LEVER, Material.OBSERVER,
            Material.TARGET, Material.COMPARATOR, Material.REPEATER, Material.HOPPER,
            Material.RAIL, Material.POWERED_RAIL, Material.ACTIVATOR_RAIL, Material.DETECTOR_RAIL,
            Material.TRAPPED_CHEST, Material.DAYLIGHT_DETECTOR, Material.TNT, Material.SLIME_BLOCK,
            Material.HONEY_BLOCK
    };

    private static final Material[] decorationItems = {
            Material.OAK_LEAVES, Material.SPRUCE_LEAVES, Material.BIRCH_LEAVES, Material.JUNGLE_LEAVES, Material.ACACIA_LEAVES, Material.DARK_OAK_LEAVES,
            Material.FERN, Material.LARGE_FERN, Material.DEAD_BUSH, Material.SEAGRASS, Material.GRASS,
            Material.TALL_GRASS, Material.POPPY, Material.DANDELION, Material.OXEYE_DAISY, Material.CORNFLOWER,
            Material.ROSE_BUSH, Material.PEONY, Material.BLUE_ORCHID, Material.ALLIUM,
            Material.AZURE_BLUET, Material.RED_TULIP, Material.ORANGE_TULIP, Material.WHITE_TULIP,
            Material.PINK_TULIP, Material.LILY_OF_THE_VALLEY, Material.FLOWER_POT, Material.SWEET_BERRIES,
            Material.WHITE_BANNER, Material.ORANGE_BANNER, Material.MAGENTA_BANNER, Material.LIGHT_BLUE_BANNER,
            Material.YELLOW_BANNER, Material.LIME_BANNER, Material.PINK_BANNER, Material.GRAY_BANNER,
            Material.LIGHT_GRAY_BANNER, Material.CYAN_BANNER, Material.PURPLE_BANNER, Material.BLUE_BANNER,
            Material.BROWN_BANNER, Material.GREEN_BANNER, Material.RED_BANNER, Material.BLACK_BANNER,
            Material.TORCH, Material.SOUL_TORCH, Material.VINE, Material.CHAIN,
            Material.IRON_BARS, Material.LANTERN, Material.SOUL_LANTERN, Material.LOOM,
            Material.ANVIL, Material.BLAST_FURNACE, Material.BARREL, Material.CHEST,
            Material.CARTOGRAPHY_TABLE, Material.FLETCHING_TABLE, Material.SMOKER, Material.STONECUTTER,
            Material.FURNACE, Material.CRAFTING_TABLE, Material.JUKEBOX, Material.GRINDSTONE,
            Material.PAINTING, Material.BELL, Material.CAMPFIRE, Material.SOUL_CAMPFIRE
    };

    private static final Material[] colouredItems = {
            Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_BLUE_WOOL,
            Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL, Material.GRAY_WOOL,
            Material.LIGHT_GRAY_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL,
            Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL, Material.BLACK_WOOL,
            Material.WHITE_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS,
            Material.YELLOW_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.PINK_STAINED_GLASS, Material.GRAY_STAINED_GLASS,
            Material.LIGHT_GRAY_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.BLUE_STAINED_GLASS,
            Material.BROWN_STAINED_GLASS, Material.GREEN_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.BLACK_STAINED_GLASS,
            Material.WHITE_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE,
            Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS_PANE, Material.BLACK_STAINED_GLASS_PANE,
            Material.WHITE_CONCRETE_POWDER, Material.ORANGE_CONCRETE_POWDER, Material.MAGENTA_CONCRETE_POWDER, Material.LIGHT_BLUE_CONCRETE_POWDER,
            Material.YELLOW_CONCRETE_POWDER, Material.LIME_CONCRETE_POWDER, Material.PINK_CONCRETE_POWDER, Material.GRAY_CONCRETE_POWDER,
            Material.LIGHT_GRAY_CONCRETE_POWDER, Material.CYAN_CONCRETE_POWDER, Material.PURPLE_CONCRETE_POWDER, Material.BLUE_CONCRETE_POWDER,
            Material.BROWN_CONCRETE_POWDER, Material.GREEN_CONCRETE_POWDER, Material.RED_CONCRETE_POWDER, Material.BLACK_CONCRETE_POWDER,
            Material.WHITE_CONCRETE, Material.ORANGE_CONCRETE, Material.MAGENTA_CONCRETE, Material.LIGHT_BLUE_CONCRETE,
            Material.YELLOW_CONCRETE, Material.LIME_CONCRETE, Material.PINK_CONCRETE, Material.GRAY_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE, Material.CYAN_CONCRETE, Material.PURPLE_CONCRETE, Material.BLUE_CONCRETE,
            Material.BROWN_CONCRETE, Material.GREEN_CONCRETE, Material.RED_CONCRETE, Material.BLACK_CONCRETE,
            Material.WHITE_DYE, Material.ORANGE_DYE, Material.MAGENTA_DYE, Material.LIGHT_BLUE_DYE,
            Material.YELLOW_DYE, Material.LIME_DYE, Material.PINK_DYE, Material.GRAY_DYE,
            Material.LIGHT_GRAY_DYE, Material.CYAN_DYE, Material.PURPLE_DYE, Material.BLUE_DYE,
            Material.BROWN_DYE, Material.GREEN_DYE, Material.RED_DYE, Material.BLACK_DYE
    };

    private static final Material[] mineralItems = {
            Material.IRON_INGOT, Material.IRON_BLOCK, Material.GOLD_INGOT, Material.GOLD_BLOCK,
            Material.DIAMOND, Material.DIAMOND_BLOCK, Material.LAPIS_LAZULI, Material.LAPIS_BLOCK,
            Material.COAL, Material.COAL_BLOCK, Material.NETHERITE_INGOT, Material.NETHERITE_BLOCK,
            Material.QUARTZ, Material.QUARTZ_BLOCK
    };

    private static final Material[] seedsAndFoodItems = {
            Material.WHEAT, Material.WHEAT_SEEDS, Material.HAY_BLOCK, Material.MELON,
            Material.MELON_SEEDS, Material.COCOA_BEANS, Material.SUGAR_CANE, Material.SUGAR,
            Material.PUMPKIN, Material.PUMPKIN_SEEDS, Material.CACTUS, Material.NETHER_WART,
            Material.BEETROOT, Material.BEETROOT_SEEDS, Material.CARROT, Material.POTATO,
            Material.BAKED_POTATO, Material.POISONOUS_POTATO, Material.APPLE, Material.PUMPKIN_PIE,
            Material.BEEF, Material.COOKED_BEEF, Material.CHICKEN, Material.COOKED_CHICKEN,
            Material.PORKCHOP, Material.COOKED_PORKCHOP, Material.MUTTON, Material.COOKED_MUTTON,
            Material.RABBIT, Material.COOKED_RABBIT, Material.BROWN_MUSHROOM, Material.RED_MUSHROOM,
            Material.MUSHROOM_STEW, Material.OAK_SAPLING, Material.SPRUCE_SAPLING, Material.BIRCH_SAPLING,
            Material.JUNGLE_SAPLING, Material.ACACIA_SAPLING, Material.DARK_OAK_SAPLING
    };

    private static final Material[] mobDropItems = {
            Material.BONE, Material.ARROW, Material.GUNPOWDER, Material.STRING, Material.FEATHER,
            Material.RABBIT_FOOT, Material.SPIDER_EYE, Material.EGG, Material.SLIME_BALL,
            Material.INK_SAC, Material.BLAZE_ROD, Material.ENDER_PEARL, Material.PHANTOM_MEMBRANE,
            Material.MAGMA_CREAM, Material.ROTTEN_FLESH, Material.GHAST_TEAR, Material.LEATHER
    };

    private static final Material[] miscItems = {
            Material.ENCHANTING_TABLE, Material.FLINT, Material.FLINT_AND_STEEL, Material.WATER_BUCKET,
            Material.LAVA_BUCKET, Material.BREWING_STAND, Material.NAUTILUS_SHELL
    };

    /**
     * Open the main shop page for a player
     * @param player The player in question
     */
    public static void openShop(Player player) {
        InventoryGui shopGui = new InventoryGui(SuperFactions.getInstance(), player, "Shop", mainLayout);
        shopGui.setFiller(new ItemStack(Material.AIR, 1));
        shopGui.addElement(new StaticGuiElement('a',
                new ItemStack(Material.WARPED_NYLIUM),
                1,
                click -> {
                    shopGui.playClickSound();
                    openBuy(player, new BuyGUIDetails(netherItems, Material.WARPED_NYLIUM, "Nether"));
                    return true;
                },
                "Nether"
        ));
        shopGui.addElement(new StaticGuiElement('b',
                new ItemStack(Material.GRASS_BLOCK),
                1,
                click -> {
                    shopGui.playClickSound();
                    openBuy(player, new BuyGUIDetails(buildingItems, Material.GRASS_BLOCK, "Building"));
                    return true;
                },
                "Building"
        ));
        shopGui.addElement(new StaticGuiElement('c',
                new ItemStack(Material.REDSTONE),
                1,
                click -> {
                    shopGui.playClickSound();
                    openBuy(player, new BuyGUIDetails(redstoneItems, Material.REDSTONE, "Redstone"));
                    return true;
                },
                "Redstone"
        ));
        shopGui.addElement(new StaticGuiElement('d',
                new ItemStack(Material.POPPY),
                1,
                click -> {
                    shopGui.playClickSound();
                    openBuy(player, new BuyGUIDetails(decorationItems, Material.POPPY, "Decorations"));
                    return true;
                },
                "Decorations"
        ));
        shopGui.addElement(new StaticGuiElement('e',
                new ItemStack(Material.YELLOW_WOOL),
                1,
                click -> {
                    shopGui.playClickSound();
                    openBuy(player, new BuyGUIDetails(colouredItems, Material.YELLOW_WOOL, "Coloured Blocks"));
                    return true;
                },
                "Coloured Blocks"
        ));
        shopGui.addElement(new StaticGuiElement('f',
                new ItemStack(Material.DIAMOND),
                1,
                click -> {
                    shopGui.playClickSound();
                    openBuy(player, new BuyGUIDetails(mineralItems, Material.DIAMOND, "Minerals"));
                    return true;
                },
                "Minerals"
        ));
        shopGui.addElement(new StaticGuiElement('g',
                new ItemStack(Material.WHEAT_SEEDS),
                1,
                click -> {
                    shopGui.playClickSound();
                    openBuy(player, new BuyGUIDetails(seedsAndFoodItems, Material.WHEAT_SEEDS, "Seeds & Food"));
                    return true;
                },
                "Seeds & Food"
        ));
        shopGui.addElement(new StaticGuiElement('h',
                new ItemStack(Material.FEATHER),
                1,
                click -> {
                    shopGui.playClickSound();
                    openBuy(player, new BuyGUIDetails(mobDropItems, Material.FEATHER, "Mob drops"));
                    return true;
                },
                "Mob drops"
        ));
        shopGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.ENCHANTING_TABLE),
                1,
                click -> {
                    shopGui.playClickSound();
                    openBuy(player, new BuyGUIDetails(miscItems, Material.ENCHANTING_TABLE, "Miscellaneous"));
                    return true;
                },
                "Miscellaneous"
        ));
        shopGui.addElement(new StaticGuiElement('j',
                new ItemStack(Material.JUKEBOX),
                1,
                click -> {
                    shopGui.playClickSound();
                    openSell(player, true);
                    return true;
                },
                "Sell items"
        ));
        shopGui.show(player);
    }

    /**
     * Open a buying window for a player
     * @param player The player in question
     * @param details The buying details
     */
    public static void openBuy(Player player, BuyGUIDetails details) {
        InventoryGui buyGui = new InventoryGui(SuperFactions.getInstance(), null, "Buy ", buyLayout);
        GuiElementGroup group = new GuiElementGroup('g');
        for (Material material : details.getItems()) {
            ItemStack item = new ItemStack(material);
            if (Shop.getItemBuyPrice(item) == -1.0) {
                group.addElement(new StaticGuiElement('e', new ItemStack(Material.BARRIER), click -> {
                    return true;
                }, Shop.formatItemName(material), "Cannot be bought"));
            } else {
                group.addElement(new StaticGuiElement('e', item, click -> {
                    ItemStack clickedItem = click.getEvent().getCurrentItem();
                    if (clickedItem != null) {
                        buyGui.playClickSound();
                        openBuyItem(player, clickedItem, details);
                    }

                    return true;
                }, Shop.formatItemName(material), "Buy price per piece: " + SuperFactions.getEconomy().formatMoney(Shop.getItemBuyPrice(item)), "Sell price per piece: " + SuperFactions.getEconomy().formatMoney(Shop.getItemSellPrice(item))));
            }
        }
        buyGui.addElement(group);

        buyGui.addElement(new GuiPageElement('b', new ItemStack(Material.ARROW), GuiPageElement.PageAction.PREVIOUS, "Go to previous page (%prevpage%)"));
        buyGui.addElement(new GuiPageElement('f', new ItemStack(Material.ARROW), GuiPageElement.PageAction.NEXT, "Go to next page (%nextpage%)"));

        buyGui.addElement(new StaticGuiElement('a', new ItemStack(details.getThumbnailItem()), details.getName() + " items"));
        buyGui.addElement(new StaticGuiElement('h', new ItemStack(backItem), click -> {
            buyGui.playClickSound();
            buyGui.close();
            openShop(player);
            return true;
        }, "Back"));
        buyGui.setFiller(new ItemStack(fillerMaterial));

        buyGui.show(player, true);
    }

    /**
     * Open a window to buy an item for a player
     * @param player The player in question
     * @param item The item to buy
     * @param details The previous buying details, set to null to not open buy window on back pressed
     */
    public static void openBuyItem(Player player, ItemStack item, BuyGUIDetails details) {
        InventoryGui buyItemGui = new InventoryGui(SuperFactions.getInstance(), player, "Buy item", buyItemLayout);
        buyItemGui.addElement(new StaticGuiElement('o', new ItemStack(Material.RED_DYE), click -> {
            Shop.buyItem(player, item.getType(), 1);
            return true;
        }, "1x " + SuperFactions.getEconomy().formatMoney(Shop.getItemBuyPrice(item))));
        buyItemGui.addElement(new StaticGuiElement('s', new ItemStack(Material.RED_DYE), click -> {
            Shop.buyItem(player, item.getType(), 64);
            return true;
        }, "64x " + SuperFactions.getEconomy().formatMoney(Shop.getItemBuyPrice(item, 64))));

        buyItemGui.addElement(new StaticGuiElement('a', item));
        if (details == null) {
            buyItemGui.addElement(new StaticGuiElement('h', new ItemStack(backItem), click -> {
                buyItemGui.playClickSound();
                buyItemGui.close();
                return true;
            }, "Close"));
        } else {
            buyItemGui.addElement(new StaticGuiElement('h', new ItemStack(backItem), click -> {
                buyItemGui.playClickSound();
                buyItemGui.close();
                openBuy(player, details);
                return true;
            }, "Back"));
        }
        buyItemGui.setFiller(new ItemStack(fillerMaterial));

        buyItemGui.show(player, true);
    }

    /**
     * Open an item selling window for a player
     * @param player The player in question
     * @param reopenShop Weather to reopen the main shop when the close/back button is clicked
     */
    public static void openSell(Player player, boolean reopenShop) {
        InventoryGui sellGui = new InventoryGui(SuperFactions.getInstance(), player, "Sell items", sellLayout);
        GuiElementGroup group = new GuiElementGroup('g');
        sellGui.addElement(GUIElementsOfPlayerInv(player, group, reopenShop, sellGui));

        if (reopenShop) {
            sellGui.addElement(new StaticGuiElement('h', new ItemStack(backItem), click -> {
                sellGui.playClickSound();
                sellGui.close();
                openShop(player);
                return true;
            }, "Back"));
        } else {
            sellGui.addElement(new StaticGuiElement('h', new ItemStack(backItem), click -> {
                sellGui.playClickSound();
                sellGui.close();
                return true;
            }, "Close"));
        }

        sellGui.addElement(new GuiPageElement('b', new ItemStack(Material.ARROW), GuiPageElement.PageAction.PREVIOUS, "Go to previous page (%prevpage%)"));
        sellGui.addElement(new GuiPageElement('f', new ItemStack(Material.ARROW), GuiPageElement.PageAction.NEXT, "Go to next page (%nextpage%)"));
        sellGui.setFiller(new ItemStack(fillerMaterial));
        sellGui.show(player, true);
    }

    private static GuiElementGroup GUIElementsOfPlayerInv(Player player, GuiElementGroup group, boolean reopenShop, InventoryGui sellGui) {
        for (int i = 0; i <= 35; i++) {
            final int itemIndex = i;
            ItemStack item = player.getInventory().getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                if (Shop.getItemSellPrice(item) == -1.0) {
                    group.addElement(new StaticGuiElement('e', new ItemStack(Material.BARRIER), item.getAmount(), click -> {
                        return true;
                    }, Shop.formatItemName(item), "Cannot be sold"
                    ));
                } else {
                    group.addElement(new StaticGuiElement('e', item, item.getAmount(), click -> {
                        ItemStack clickedItem = click.getEvent().getCurrentItem();
                        if (clickedItem != null) {
                            Shop.sellItem(player, itemIndex);
                            group.clearElements();
                            GUIElementsOfPlayerInv(player, group, reopenShop, sellGui);
                            sellGui.show(player, true);
                        }

                        return true;
                    }, Shop.formatItemName(item), "Sell price: " + SuperFactions.getEconomy().formatMoney(Shop.getItemSellPrice(item, item.getAmount())), "Buy price: " + SuperFactions.getEconomy().formatMoney(Shop.getItemBuyPrice(item, item.getAmount()))
                    ));
                }
            }
        }
        return group;
    }
}