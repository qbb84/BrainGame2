package me.qbb84.braingame2.Commands;

import me.qbb84.braingame2.BrainGame2;
import me.qbb84.braingame2.Game.Corsi;
import me.qbb84.braingame2.Game.DualNBack;
import me.qbb84.braingame2.Game.GameManager;
import me.qbb84.braingame2.Inventory.InventoryEvents;
import me.qbb84.braingame2.Utils.InventoryCountdown;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;


public class CorsiCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player player && s.equalsIgnoreCase(Corsi.commandName)) {
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
