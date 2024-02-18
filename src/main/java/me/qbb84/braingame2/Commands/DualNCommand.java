package me.qbb84.braingame2.Commands;

import me.qbb84.braingame2.BrainGame2;
import me.qbb84.braingame2.Game.DualNBack;
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

public class DualNCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {


        if (commandSender instanceof Player player && s.equalsIgnoreCase(DualNBack.commandName)) {
            Inventory inventory = Bukkit.createInventory(player, 45, "Get ready!");

            player.openInventory(inventory);
            startCountdown(player, inventory);
            return true;
        }
        return true;
    }

    private void startCountdown(Player player, Inventory inventory) {
        new BukkitRunnable() {
            int seconds = 3;

            @Override
            public void run() {
                if (!InventoryEvents.isInInventory.contains(player.getUniqueId())) {cancel(); return;}

                if (seconds > 0) {
                    InventoryCountdown.getInstance().updateInventory(inventory, seconds);
                    player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(1, Note.Tone.E));
                    seconds--;
                } else {
                    player.closeInventory();
                    player.playNote(player.getLocation(), Instrument.PLING, Note.flat(1, Note.Tone.G));
                    cancel();
                }
            }
        }.runTaskTimer(BrainGame2.getPlugin(), 0, 20);
    }


}
