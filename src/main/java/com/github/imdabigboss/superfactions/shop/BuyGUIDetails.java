package com.github.imdabigboss.superfactions.shop;

import org.bukkit.Material;

public class BuyGUIDetails {
    private Material[] items;
    private Material thumbnailItem;
    private String name;

    public BuyGUIDetails(Material[] items, Material thumbnailItem, String name) {
        this.items = items;
        this.thumbnailItem = thumbnailItem;
        this.name = name;
    }

    public BuyGUIDetails(Material[] items, String name) {
        this.items = items;
        if (items.length >= 1) {
            this.thumbnailItem = items[0];
        } else {
            this.thumbnailItem = Material.AIR;
        }
        this.name = name;
    }

    public Material[] getItems() {
        return items;
    }
    public void setItems(Material[] items) {
        this.items = items;
    }

    public Material getThumbnailItem() {
        return thumbnailItem;
    }
    public void setThumbnailItem(Material thumbnailItem) {
        this.thumbnailItem = thumbnailItem;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
