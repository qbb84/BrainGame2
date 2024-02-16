package me.qbb84.braingame2.Inventory;

import me.qbb84.braingame2.BrainGame2;
import me.qbb84.braingame2.Game.Game;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomInventory {

    public static final String brain = Color.to("&d&lBrain");
    public static final String game = Color.to(" &d&lGame");
    private static final String expo = "²";
    public static final String exponent = Color.to("&5" + expo);

    private static final Integer[] GUIExclusion = {
                    4,
            11, 12, 13, 14, 15,
            20, 21, 22, 23, 24,
            29, 30, 31, 32, 33,
                    40,
    };

    private static final ArrayList<Integer> exclusionList = new ArrayList(Arrays.asList(GUIExclusion));


    public CustomInventory(Player holder) {
        Inventory inventory = Bukkit.createInventory(holder, 45, brain + game + exponent);

        new BukkitRunnable() {
            @Override
            public void run() {
                updateInventoryContents(inventory);
            }
        }.runTaskTimerAsynchronously(BrainGame2.getPlugin(), 0, 20);

        setInventoryContents(inventory);
        holder.openInventory(inventory);


    }

    private void updateInventoryContents(@NotNull Inventory inventory) {
        int centerX = inventory.getSize() / 2;
        int centerY = inventory.getSize() / 2;

        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (exclusionList.contains(i)) continue;

            int distance = Math.abs(centerX - i) + Math.abs(centerY - i);

            inventory.setItem(i, coloredGlassPane(distance, currentTime));
        }
    }

    private @NotNull ItemStack coloredGlassPane(int distance, long currentTime) {
        Material[] randomMaterial = {
                Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                Material.RED_STAINED_GLASS_PANE,
                Material.BLUE_STAINED_GLASS_PANE,
                Material.CYAN_STAINED_GLASS_PANE,
                Material.YELLOW_STAINED_GLASS_PANE,
        };

        int centerX = 4;
        int centerY = 4;

        double progressX = Math.abs(centerX - distance) / (double) centerX;
        double progressY = Math.abs(centerY - distance) / (double) centerY;

        double frequencyX = 2.0;
        double amplitudeX = 0.5;
        double frequencyY = 2.0;
        double amplitudeY = 0.5;
        double timeFactor = (currentTime % 10000) / 10000.0;

        double waveX = amplitudeX * Math.sin(frequencyX * (progressX + timeFactor) * Math.PI);
        double waveY = amplitudeY * Math.sin(frequencyY * (progressY + timeFactor) * Math.PI);

        progressX = 0.5 + waveX;
        progressY = 0.5 + waveY;

        progressX = Math.max(0.0, Math.min(1.0, progressX));
        progressY = Math.max(0.0, Math.min(1.0, progressY));

        int colorIndex = (int) ((progressX + progressY) / 2 * (randomMaterial.length - 1));

        return new ItemStack(randomMaterial[colorIndex]);
    }

    private void setInventoryContents(Inventory inventory) {
        if (Game.inventoryItems.size() > 9 ) {
            Bukkit.getServer().getConsoleSender().sendMessage("Inventory items is > 9. Item overflowed will not be added.");
        }
        var row1 = 12;
        var row2 = 21;
        var row3 = 30;

        for (int i = 0; i < Game.inventoryItems.size(); i++) {
            if (Game.inventoryItems.get(i) == null) continue;
            if (i < 3) {
                inventory.setItem(row1, Game.inventoryItems.get(i).getCreatedItem());
                row1++;
            } else if (i > 3) {
                inventory.setItem(row2, Game.inventoryItems.get(i).getCreatedItem());
                row2++;
            } else {
                inventory.setItem(row3, Game.inventoryItems.get(i).getCreatedItem());
                row3++;
            }
        }
    }
}
