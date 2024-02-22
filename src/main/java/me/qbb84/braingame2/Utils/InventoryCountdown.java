package me.qbb84.braingame2.Utils;

import io.netty.buffer.ByteBuf;
import me.qbb84.braingame2.BrainGame2;
import me.qbb84.braingame2.Inventory.GameInventory;
import me.qbb84.braingame2.Inventory.InventoryEvents;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBase;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.Instrument;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class InventoryCountdown {


    public InventoryCountdown() {}

    public void updateInventory(@NotNull Inventory inventory, int seconds) {
        inventory.clear();

        switch (seconds) {
            case 3:
                addNumberGlassPanes(inventory,
                        " #### \n     # \n #### \n     # \n #### ", seconds);  // Glass panes for number 3
                break;
            case 2:
                addNumberGlassPanes(inventory,
                        " #### \n     # \n  #### \n #      \n ##### ", seconds);  // Glass panes for number 2
                break;
            case 1:
                addNumberGlassPanes(inventory,
                        "   #   \n  ##   \n   #   \n   #   \n  ###  ", seconds);  // Glass panes for number 1
                break;
        }
    }

    private void addNumberGlassPanes(@NotNull Inventory inventory, @NotNull String number, int countDown) {
        String[] lines = number.split("\n");

        int startRow = (inventory.getSize() / 9 - lines.length) / 2;
        int startCol = (9 - lines[0].length()) / 2;

        for (int row = 0; row < lines.length; row++) {
            String line = lines[row];
            for (int col = 0; col < line.length(); col++) {
                int slot = (startRow + row) * 9 + startCol + col;
                char c = line.charAt(col);
                if (c == '#') {
                    addGlassPane(inventory, slot, countDown);
                }
            }
        }
    }

    private void addGlassPane(Inventory inventory, int slot, int countDownTime) {
        ItemStack glassPanes = null;
        switch (countDownTime) {
            case 3 -> {
                glassPanes = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            }
            case 2 -> {
                glassPanes = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
            }
            case 1 -> {
                glassPanes = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
            }
        }

        inventory.setItem(slot, glassPanes);
    }

    public void startCountdown(Player player, Inventory inventory) {
        new BukkitRunnable() {
            int seconds = 3;

            @Override
            public void run() {
                if (!InventoryEvents.isInInventory.contains(player.getUniqueId())) {cancel(); return;}

                if (seconds > 0) {
                    updateInventory(inventory, seconds);
                    player.playNote(player.getLocation(), Instrument.PLING, Note.sharp(1, Note.Tone.E));
                    seconds--;
                } else {
                    player.closeInventory();
                    player.playNote(player.getLocation(), Instrument.PLING, Note.flat(1, Note.Tone.G));

                    GameInventory newInv = (craftPlayer, windowTitle) -> {

                    };

                    newInv.createGameInventory(((CraftPlayer) player).getHandle(), "test");
                    cancel();
                }
            }
        }.runTaskTimer(BrainGame2.getPlugin(), 0, 20);
    }
}
