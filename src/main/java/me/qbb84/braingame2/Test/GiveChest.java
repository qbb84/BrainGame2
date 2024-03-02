package me.qbb84.braingame2.Test;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GiveChest implements CommandExecutor {

    @Override
    public boolean onCommand(
            @NotNull CommandSender commandSender,
            @NotNull Command command,
            @NotNull String s,
            @NotNull String[] strings) {
        if (s.equalsIgnoreCase("chest") && commandSender instanceof Player player) {
            ItemStack chest = new ItemStack(Material.CHEST);
            ItemMeta meta = chest.getItemMeta();
            meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "[SLOW]" + ChatColor.AQUA + " The Harvester");

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "Type:" + ChatColor.GOLD + "THROWABLE");
            lore.add(ChatColor.GRAY + "Contains:" + ChatColor.BOLD + ChatColor.GOLD + "121 Blocks");
            meta.setLore(lore);
            chest.setItemMeta(meta);

            player.getInventory().addItem(chest);

            return true;
        }
        return false;
    }
}
