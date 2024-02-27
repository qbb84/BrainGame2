package me.qbb84.braingame2.Game;

import org.bukkit.inventory.ItemStack;

public interface GameData {

  String name();

  boolean visible();

  ItemStack displayedItem();
}
