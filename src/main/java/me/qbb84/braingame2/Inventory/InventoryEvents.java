package me.qbb84.braingame2.Inventory;

import java.util.ArrayList;
import java.util.UUID;
import me.qbb84.braingame2.Game.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;

public class InventoryEvents implements Listener {

  public static ArrayList<UUID> isInInventory = new ArrayList<>();

  @EventHandler
  public void onInventoryClick(@NotNull InventoryClickEvent event) {
    if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
    String title = "Get ready!";
    if (event
            .getView()
            .getTitle()
            .equalsIgnoreCase(
                CustomInventory.brain + CustomInventory.game + CustomInventory.exponent)
        || event.getView().getTitle().equalsIgnoreCase(title)) {
      event.setCancelled(true);
    }

    for (InventoryItems items : Game.inventoryItems) {
      if (event
          .getCurrentItem()
          .getItemMeta()
          .getDisplayName()
          .equalsIgnoreCase(items.getItemName())) {
        Bukkit.dispatchCommand(
            event.getWhoClicked(), Game.gameCollection.get(items.getItemName()).commandName());
      }
    }
  }

  @EventHandler
  public void onInventoryOpen(InventoryOpenEvent event) {
    isInInventory.forEach(s -> Bukkit.getServer().broadcastMessage(s.toString()));
    Player player = (Player) event.getPlayer();
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent event) {
    Player player = (Player) event.getPlayer();

    if (isInInventory.contains(player.getUniqueId())) {
      isInInventory.remove(event.getPlayer().getUniqueId());
    }

    isInInventory.forEach(s -> Bukkit.getServer().broadcastMessage(s.toString()));
    Bukkit.broadcastMessage(String.valueOf(isInInventory.size()));
  }
}
