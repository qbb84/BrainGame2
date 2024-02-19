package me.qbb84.braingame2.Commands;


import me.qbb84.braingame2.Game.GameManager;
import me.qbb84.braingame2.Inventory.InventoryEvents;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class GUICommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s,  String[] strings) {

        if (sender instanceof Player player && s.equalsIgnoreCase("braingame")) {
            GameManager.getInstance().openInventory(player);
            return false;
        }
        return true;
    }
}
