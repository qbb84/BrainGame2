package me.qbb84.braingame2.Test;

import me.qbb84.braingame2.BrainGame2;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TestEvent implements Listener {

    @EventHandler
    public void onMove(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        try {
            Class<?> getGameClass = Class.forName("me.qbb84.braingame2.Game.Game");
            Field[] fields = getGameClass.getFields();
            Constructor<?>[] constructor = getGameClass.getConstructors();
            Method[] methods = getGameClass.getMethods();

            printReflectionData(player, fields, constructor, methods);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (event.getItem().getItemMeta().getDisplayName().contains("The Harvester")
                && event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            new LootChestEvent(player).start(event.getItem());
            player.getInventory().remove(player.getItemInHand());
        }
    }

    @SafeVarargs
    private static <T extends AccessibleObject, P extends Entity> void printReflectionData(
            P playerEntity, T[]... reflectionType) {
        for (T[] tLoop : reflectionType) {
            for (AccessibleObject reflectionObject : tLoop) {
                if (reflectionObject instanceof Field) {
                    playerEntity.sendMessage("Fields: " + ((Field) reflectionObject).getType().getName());
                } else if (reflectionObject instanceof Constructor) {
                    playerEntity.sendMessage("Constructor: " + ((Constructor<?>) reflectionObject).getName());
                } else if (reflectionObject instanceof Method) {
                    playerEntity.sendMessage("Methods: " + ((Method) reflectionObject).getName());
                }
            }
        }
    }

    @EventHandler
    public void onEvent(AsyncPlayerChatEvent event) {
        var message = "test";
        Player player = event.getPlayer();

        double playerX = player.getLocation().getX(),
                playerY = player.getLocation().getY(),
                playerZ = player.getLocation().getZ();

        if (event.getMessage().equalsIgnoreCase(message)) {
            new BukkitRunnable() {
                int y1 = 0;

                @Override
                public void run() {

                    for (int degrees = 0; degrees < 360; degrees++) {
                        var radians = Math.toRadians(degrees);

                        double x = Math.sin(radians), y = Math.cos(radians);
                        player.spawnParticle(
                                Particle.DRIP_WATER,
                                new Location(player.getWorld(), playerX, playerY, playerZ).add(x, y1, y),
                                1);
                    }
                    y1 += 0.10;

                    if (y1 >= 2) {
                        y1 = 0;
                        this.cancel();
                    }
                }
            }.runTaskTimerAsynchronously(BrainGame2.getPlugin(), 0, 10);
        }
    }
}
