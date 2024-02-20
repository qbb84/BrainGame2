package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Commands.CommandWrapper;
import me.qbb84.braingame2.Inventory.InventoryItems;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@CommandWrapper(commandName = "corsi")
public final class Corsi extends Game<Corsi> {

    public static final String commandName;
    public static final String itemDisplayName;

    static {
        commandName = "corsi";
        itemDisplayName = "corsi";
    }

    public Corsi() {
            super(Color.to(itemDisplayName), true, new InventoryItems(itemDisplayName, Material.DIAMOND, Color.to("&6Click to Play!")));

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
