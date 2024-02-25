package me.qbb84.braingame2;

import me.qbb84.braingame2.Commands.*;
import me.qbb84.braingame2.Game.GameManager;
import me.qbb84.braingame2.Inventory.InventoryEvents;
import me.qbb84.braingame2.Test.GiveChest;
import me.qbb84.braingame2.Test.TestEvent;
import me.qbb84.braingame2.Utils.Color;
import org.bukkit.plugin.java.JavaPlugin;

public final class BrainGame2 extends JavaPlugin {


    private static BrainGame2 plugin = null;

    @Override
    public void onEnable() {
        plugin = this;
        GameManager.getInstance().initializeItems();
        getCommand("braingame").setExecutor(new GUICommand());
        getCommand("dualnback").setExecutor(new DualNCommand());
        getCommand("corsi").setExecutor(new CorsiCommand());
        getCommand("cwm").setExecutor(new CWMCommand());
        getCommand("mm").setExecutor(new MathCommand());
        getCommand("pasat").setExecutor(new PasatCommand());
        getCommand("m_span").setExecutor(new MemoryCommand());
        getCommand("chest").setExecutor(new GiveChest());
        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        getServer().getPluginManager().registerEvents(new TestEvent(), this);
        getServer().getConsoleSender().sendMessage(Color.to("&bBrain Game's neurons are processing..."));
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(Color.to("&8Brain Game's neurons are closing..."));
    }

    public static BrainGame2 getPlugin() {
        return plugin;
    }
}
