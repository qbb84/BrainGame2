package me.qbb84.braingame2.Game.BrainGames;


import me.qbb84.braingame2.Game.Game;
import me.qbb84.braingame2.Inventory.InventoryItems;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DualNBack extends Game<DualNBack> {

    public static final String commandName = "dualnback";

    public DualNBack() {
        super("Dual N Back", true, new InventoryItems("Dual N Back", Material.APPLE, "Dual N Back Is a..."));
    }


    @Override
    public ItemStack displayedItem() {
        return new ItemStack(Material.BEACON);
    }

    @Override
     public String commandName() {
      return commandName;
    }


}
