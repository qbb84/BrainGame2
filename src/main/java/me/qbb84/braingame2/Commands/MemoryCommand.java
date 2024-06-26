package me.qbb84.braingame2.Commands;

import me.qbb84.braingame2.Game.Memory;
import me.qbb84.braingame2.Inventory.InventoryEvents;
import me.qbb84.braingame2.Utils.InventoryCountdown;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class MemoryCommand implements CommandExecutor {

  @Override
  public boolean onCommand(
      @NotNull CommandSender commandSender,
      @NotNull Command command,
      @NotNull String s,
      @NotNull String[] strings) {

    if (commandSender instanceof Player player && s.equalsIgnoreCase(Memory.commandName)) {
      Inventory inventory = Bukkit.createInventory(player, 45, "Get ready!");

      player.openInventory(inventory);
      new InventoryCountdown().startCountdown(player, inventory);

      if (!InventoryEvents.isInInventory.contains(player.getUniqueId())) {
        InventoryEvents.isInInventory.add(player.getUniqueId());
      }
      return true;
    }
    return true;
  }
}
