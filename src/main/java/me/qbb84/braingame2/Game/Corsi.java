package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Corsi extends Game<Corsi> {


    public Corsi() {
            super(Color.to("corsi"), true, new InventoryItems("Corsi Block Tapping", Material.DIAMOND, Color.to("&6Click to Play!")));

    }

    @Override
    public String commandName() {
        return "corsi";
    }

    @Override
    public ItemStack displayedItem() {
        return null;
    }
}
