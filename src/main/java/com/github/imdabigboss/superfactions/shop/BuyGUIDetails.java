package com.github.imdabigboss.superfactions.shop;

import org.bukkit.Material;

public class BuyGUIDetails {
    private Material[] items;
    private Material thumnalItem;
    private String name;

    public BuyGUIDetails(Material[] items, Material thumnalItem, String name) {
        this.items = items;
        this.thumnalItem = thumnalItem;
        this.name = name;
    }

    public BuyGUIDetails(Material[] items, String name) {
        this.items = items;
        if (items.length >= 1) {
            this.thumnalItem = items[0];
        } else {
            this.thumnalItem = Material.AIR;
        }
        this.name = name;
    }

    public Material[] getItems() {
        return items;
    }
    public void setItems(Material[] items) {
        this.items = items;
    }

    public Material getThumnalItem() {
        return thumnalItem;
    }
    public void setThumnalItem(Material thumnalItem) {
        this.thumnalItem = thumnalItem;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
