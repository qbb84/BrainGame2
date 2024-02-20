package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;
import me.qbb84.braingame2.Utils.Color;
import net.minecraft.network.protocol.game.PacketListenerPlayIn;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class MentalMath extends Game<MentalMath> {

    public static final String commandName;
    public static final String itemDisplayName;

    static {
        commandName = "mm";
        itemDisplayName = "Mental Math";
    }

    public MentalMath() {
            super(Color.to(itemDisplayName), true, new InventoryItems(itemDisplayName, Material.ENCHANTED_BOOK, Color.to("&6Click to Play!")));
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
