package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class PASAT extends Game<PASAT> {


    public PASAT() {
        super(Color.to("PASAT"), true, new InventoryItems("PASAT", Material.COMPASS, Color.to("&6Click to Play!")));
    }

    @Override
    public String commandName() {
        return "PASAT";
    }

    @Override
    public ItemStack displayedItem() {
        return null;
    }
}
