package me.qbb84.braingame2.Commands;

import me.qbb84.braingame2.BrainGame2;
import me.qbb84.braingame2.Game.DualNBack;
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

public class DualNCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player player && s.equalsIgnoreCase(DualNBack.commandName)) {
            Inventory inventory = Bukkit.createInventory(player, 45, "Get ready!");

            // Open the inventory
            player.openInventory(inventory);

            // Start the countdown runnable
            startCountdown(player, inventory);

            return true;
        }
        return true;
    }

    private void startCountdown(Player player, Inventory inventory) {
        // Schedule a BukkitRunnable to run a countdown
        new BukkitRunnable() {
            int seconds = 3; // Set the initial countdown value

            @Override
            public void run() {
                if (seconds > 0) {
                    // Update the inventory with countdown values
                    InventoryCountdown.getInstance().updateInventory(inventory, seconds);
                    player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(1, Note.Tone.E));
                    seconds--;
                } else {
                    // Countdown finished, close the inventory or do other actions
                    player.closeInventory();
                    player.playNote(player.getLocation(), Instrument.PLING, Note.flat(1, Note.Tone.G));
                    cancel(); // Stop the runnable
                }
            }
        }.runTaskTimer(BrainGame2.getPlugin(), 0, 20); // Run every second (20 ticks)
    }


}
