package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class Memory extends Game<Memory> {

  public static final String commandName;
  public static final String itemDisplayName;

  static {
    commandName = "m_span";
    itemDisplayName = "Memory Span";
  }

  public Memory() {
    super(
        Color.to(itemDisplayName),
        true,
        new InventoryItems(itemDisplayName, Material.REDSTONE, Color.to("&6Click to Play!")));
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
