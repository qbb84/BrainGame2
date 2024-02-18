package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class CWM extends Game<CWM> {


    public CWM() {
        super(Color.to("cwm"), true, new InventoryItems("Complex Working Memory", Material.INK_SAC, Color.to("&6Click to Play!")));
    }

    @Override
    public String commandName() {
        return "cwm";
    }

    @Override
    public ItemStack displayedItem() {
        return null;
    }
}
