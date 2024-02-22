package me.qbb84.braingame2.Inventory;

import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;

@FunctionalInterface
public interface GameInventory {
    void createGameInventory(EntityPlayer player, String windowTitle);
}
