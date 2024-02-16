package me.qbb84.braingame2.Game.BrainGames;


import me.qbb84.braingame2.Game.Game;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DualNBack extends Game {


    public DualNBack() {
        super("DualNBack", true);
    }

    @Override
    public ItemStack displayedItem() {
        return new ItemStack(Material.BEACON);
    }

}
