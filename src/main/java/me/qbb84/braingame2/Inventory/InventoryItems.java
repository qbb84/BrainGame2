package me.qbb84.braingame2.Inventory;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class InventoryItems {

    public enum UpdateType {
        REMOVAL,
        UPDATE,
    }

    private final ItemStack createdItem;
    private final ItemMeta itemMeta;

    private final Material itemMaterial;
    private final String itemName;
    private final List<String> itemData;

    public InventoryItems(@NotNull String itemName, @NotNull Material itemMaterial, @NotNull String... itemData) {
        this.itemName = itemName;
        this.itemMaterial = itemMaterial;
        this.itemData = Arrays.stream(itemData).toList();

        this.createdItem = new ItemStack(itemMaterial);
        this.itemMeta = createdItem.getItemMeta();

        assert itemMeta != null;
        itemMeta.setLore(this.itemData);
        itemMeta.setDisplayName(itemName);
        createdItem.setItemMeta(itemMeta);
    }

    public ItemStack getCreatedItem() {
        return createdItem;
    }

    public Material getItemMaterial() {
        return itemMaterial;
    }

    public String getItemName() {
        return itemName;
    }

    public List<String> getItemData() {
        return itemData;
    }

    public ItemMeta getItemMeta() {
        return itemMeta;
    }

    public void updateItemName(String newName) {
        this.getItemMeta().setDisplayName(newName);
        this.createdItem.setItemMeta(this.getItemMeta());
    }

    public void updateItemLore(String text, @NotNull UpdateType updateType)  {
        switch (updateType) {
            case UPDATE -> {
                this.getItemData().add(text);
            }
            case REMOVAL -> {
                this.getItemData().remove(text);
            }
        }
        this.itemMeta.setLore(this.getItemData());
        this.getCreatedItem().setItemMeta(this.itemMeta);

    }


}
