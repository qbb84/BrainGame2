package me.qbb84.braingame2.Inventory;

import me.qbb84.braingame2.Game.Game;
import net.minecraft.world.inventory.InventoryClickType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;
import java.util.Map;

public class InventoryEvents implements Listener {

    @EventHandler
    public void onInventoryClick(@NotNull InventoryClickEvent event) {

        if (event.getView().getTitle().equalsIgnoreCase(CustomInventory.brain + CustomInventory.game + CustomInventory.exponent)) {
            event.setCancelled(true);
        }
        for (InventoryItems items : Game.inventoryItems) {
            if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(items.getItemName())) {

                Bukkit.getServer().getConsoleSender().sendMessage(items.getItemName());
                Bukkit.dispatchCommand(event.getWhoClicked(),
                        Game.gameCollection.get(items.getItemName()).commandName());


            }
        }
    }

}
