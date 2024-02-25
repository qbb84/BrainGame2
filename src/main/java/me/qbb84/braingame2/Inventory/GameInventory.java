package me.qbb84.braingame2.Inventory;

import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface GameInventory {
    void createGameInventory(ServerPlayer player, String windowTitle);
}
