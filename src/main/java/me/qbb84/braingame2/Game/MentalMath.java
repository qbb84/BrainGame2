package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class MentalMath extends Game<MentalMath> {

  public static final String commandName;
  public static final String itemDisplayName;

  static {
    commandName = "mm";
    itemDisplayName = "Mental Math";
  }

  public MentalMath() {
    super(
        Color.to(itemDisplayName),
        true,
        new InventoryItems(itemDisplayName, Material.ENCHANTED_BOOK, Color.to("&6Click to Play!")));
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
