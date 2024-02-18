package me.qbb84.braingame2.Utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InventoryCountdown {

    private static  InventoryCountdown INSTANCE = null;

    private InventoryCountdown() {}

    public static InventoryCountdown getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new InventoryCountdown();
        }
        return INSTANCE;
    }

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
}
