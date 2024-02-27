package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class DualNBack extends Game<DualNBack> {

  public static final String commandName = "dualnback";

  public DualNBack() {
    super(
        Color.to("Dual N-Back"),
        true,
        new InventoryItems("Dual N-Back", Material.BEACON, Color.to("&6Click to Play!")));
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
