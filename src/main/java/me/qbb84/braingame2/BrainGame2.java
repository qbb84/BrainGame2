package me.qbb84.braingame2;

import me.qbb84.braingame2.Utils.Color;
import org.bukkit.plugin.java.JavaPlugin;

public final class BrainGame2 extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(Color.to("&bBrain Game's neurons are processing..."));
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(Color.to("&8Brain Game's neurons are closing..."));
    }
}
