package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class PASAT extends Game<PASAT> {

    public static final String commandName;
    public static final String itemDisplayName;

    static {
        commandName = "PASAT";
        itemDisplayName = "PASAT";
    }

    public PASAT() {
        super(Color.to(itemDisplayName), true, new InventoryItems(itemDisplayName, Material.COMPASS, Color.to("&6Click to Play!")));
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
