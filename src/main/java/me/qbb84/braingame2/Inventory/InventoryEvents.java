package me.qbb84.braingame2.Inventory;

import me.qbb84.braingame2.Game.Game;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class InventoryEvents implements Listener {

    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null) return;
        String title = "Get ready!";
        if (event.getView().getTitle().equalsIgnoreCase(CustomInventory.brain + CustomInventory.game + CustomInventory.exponent)
        || event.getView().getTitle().equalsIgnoreCase(title)) {
            event.setCancelled(true);
        }


        for (InventoryItems items : Game.inventoryItems) {
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(items.getItemName())) {
                Bukkit.getServer().getConsoleSender().sendMessage(items.getItemName());
                Bukkit.dispatchCommand(event.getWhoClicked(), Game.gameCollection.get(items.getItemName()).commandName());
            }
        }
    }



}
