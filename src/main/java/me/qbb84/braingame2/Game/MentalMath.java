package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class MentalMath extends Game<MentalMath> {


    public MentalMath() {
            super(Color.to("mm"), true, new InventoryItems("Mental Math", Material.ENCHANTED_BOOK, Color.to("&6Click to Play!")));
    }

    @Override
    public String commandName() {
        return "mm";
    }

    @Override
    public ItemStack displayedItem() {
        return null;
    }
}
