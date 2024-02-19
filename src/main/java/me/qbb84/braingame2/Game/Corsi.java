package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Corsi extends Game<Corsi> {

    public static String commandName = "corsi";

    public Corsi() {
            super(Color.to("corsi"), true, new InventoryItems("corsi", Material.DIAMOND, Color.to("&6Click to Play!")));

    }

    @Override
    public String commandName() {
        return commandName;
    }

    @Override
    public ItemStack displayedItem() {
        return null;
    }
}
