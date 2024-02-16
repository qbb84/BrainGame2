package me.qbb84.braingame2.Game;

import java.util.HashMap;

public abstract class Game implements GameData {

    private final String gameName;
    public final boolean visible;

    public static HashMap<String, Game> gameCollection;

    static {
        gameCollection = new HashMap<>();
    }

    public Game(String gameName, boolean visible) {
        this.gameName = gameName;
        this.visible = visible;
        gameCollection.put(gameName, this);
    }

    @Override
    public String name() {
        return this.gameName;
    }

    @Override
    public boolean visible() {
        return this.visible;
    }
}
