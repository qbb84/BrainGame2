package me.qbb84.braingame2.Game;

import me.qbb84.braingame2.Inventory.InventoryItems;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public abstract sealed class Game<T extends Game<?>> implements GameData permits DualNBack, Corsi, CWM, Memory,
        MentalMath, PASAT {

    public static LinkedHashMap<String, Game<?>> gameCollection;
    public static LinkedList<InventoryItems> inventoryItems;

    static {
        gameCollection = new LinkedHashMap<>();
        inventoryItems = new LinkedList<>();
    }

    public final boolean visible;
    private final String gameName;
    private T t;

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

    public T getClassType() {
        return t;
    }
}
