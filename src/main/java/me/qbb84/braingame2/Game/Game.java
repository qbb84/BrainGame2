package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public abstract class Game<T extends Game<?>> implements GameData {

    private final String gameName;
    public final boolean visible;

    public static LinkedHashMap<String, Game> gameCollection;
    public static LinkedList<InventoryItems> inventoryItems;

    static {
        gameCollection = new LinkedHashMap<>();
        inventoryItems = new LinkedList<>();
    }

    public Game(String gameName, boolean visible, InventoryItems items) {
        this.gameName = gameName;
        this.visible = visible;
        gameCollection.put(gameName, this);
        inventoryItems.add(items);
    }

    @Override
    public String name() {
        return this.gameName;
    }

    @Override
    public boolean visible() {
        return this.visible;
    }

    public abstract String commandName();

}
