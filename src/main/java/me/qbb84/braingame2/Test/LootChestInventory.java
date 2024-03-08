package me.qbb84.braingame2.Test;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class LootChestInventory {

    @Getter
    private final Inventory inventory;
    @Getter
    private final UUID uuid;

    @Getter
    private static final HashMap<UUID, List<ItemStack>> getLootChestInventory;

    static {
        getLootChestInventory = new HashMap<>();
    }


    public LootChestInventory(UUID uuid, Inventory inventory) {
        this.inventory = inventory;
        this.uuid = uuid;


        Bukkit.getPlayer(uuid).openInventory(inventory);

    }

    public boolean checkIsEmpty(Inventory inventory) {
        return inventory.getContents().length == 0;
    }


}
