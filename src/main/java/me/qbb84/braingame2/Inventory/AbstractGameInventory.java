package me.qbb84.braingame2.Inventory;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.inventory.Containers;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;

public abstract class AbstractGameInventory implements GameInventory {

    @Override
    public void createGameInventory(EntityPlayer player, String windowTitle) {
        PacketPlayOutOpenWindow window = new PacketPlayOutOpenWindow(45, Containers.a, IChatBaseComponent.a(windowTitle));
        player.c.a(window);
    }
}
