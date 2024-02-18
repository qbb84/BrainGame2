package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Memory extends Game<Memory> {

    public Memory() {
            super(Color.to("memory"), true, new InventoryItems("Memory Span", Material.REDSTONE, Color.to("&6Click to Play!")));
    }

    @Override
    public String commandName() {
        return "memory";
    }

    @Override
    public ItemStack displayedItem() {
        return null;
    }
}
