package me.qbb84.braingame2.Test;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import me.qbb84.braingame2.BrainGame2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestEvent implements Listener {

    @EventHandler
    public void onMove(PlayerInteractEvent event) {
        Player player = event.getPlayer();


        if (event.getItem().getItemMeta().getDisplayName().contains("The Harvester")
                && event.getAction().equals(Action.RIGHT_CLICK_AIR) && !player.isSneaking()) {
            new LootChestEvent(player).start(event.getItem());
            player.getInventory().remove(player.getItemInHand());
        } else if (event.getItem().getItemMeta().getDisplayName().contains("The Harvester")
                && event.getAction().equals(Action.RIGHT_CLICK_AIR) && player.isSneaking()) {
            new LootChestInventory(player.getUniqueId(), Bukkit.createInventory(player, InventoryType.CHEST,
                    "Harvester Insertion"));
        }

    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        if (event.getView().getTitle().equals("Harvester Insertion")) {

            if (LootChestInventory.getGetLootChestInventory().get(player.getUniqueId()) == null) {
                LootChestInventory.getGetLootChestInventory().put(player.getUniqueId(),
                        new ArrayList<>(Arrays.asList(event.getInventory().getStorageContents())
                                .stream()
                                .filter(item -> item != null && item.getType() != Material.AIR)
                                .toList()));
            } else {
                List<ItemStack> newList = LootChestInventory.getGetLootChestInventory().get(player.getUniqueId());
                newList.addAll(Arrays.asList(event.getInventory().getStorageContents())
                        .stream()
                        .filter(item -> item != null && item.getType() != Material.AIR)
                        .toList());
                LootChestInventory.getGetLootChestInventory().put(player.getUniqueId(), newList);
            }


            LootChestInventory.getGetLootChestInventory().forEach((key, value) -> {
                Bukkit.broadcastMessage("key " + key + " value" + value);
            });


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

    @EventHandler
    public void onBlackPlace(BlockPlaceEvent event) {
        if (event.getBlock().getType().equals(Material.STONE)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Location loc = LootChestEvent.rayTraceToNearestUnoccupiedBlockOfType(event.getBlock().getLocation(),
                            Material.FARMLAND);
                    assert loc != null;
                    loc.getBlock().getLocation().clone().add(0, 1, 0).getBlock().applyBoneMeal(BlockFace.UP);

                }
            }.runTaskTimer(BrainGame2.getPlugin(), 0, 20);
        }
    }

    List<BlockVector3> regionLocations = new ArrayList<>();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Location blockLocation = event.getClickedBlock().getLocation();
            BlockVector3 blockLocationToVec = BlockVector3.at(blockLocation.getX(), blockLocation.getY(),
                    blockLocation.getZ());


            if (regionLocations.isEmpty()) {
                regionLocations.add(0, blockLocationToVec);
                Bukkit.broadcastMessage("Selected point 1" + blockLocationToVec);
            } else {
                regionLocations.add(1, blockLocationToVec);
                Bukkit.broadcastMessage("Selected point 2" + blockLocationToVec);
            }


            if (regionLocations.get(0) != null && regionLocations.get(1) != null) {
                CuboidRegion region = new CuboidRegion(regionLocations.get(0), regionLocations.get(1));
                org.bukkit.World bukkitWorld = event.getPlayer().getWorld();

                Vector3 minPoint = region.getBoundingBox().getPos1().toVector3();
                Vector3 maxPoint = region.getBoundingBox().getPos2().toVector3();

                // Visualize the corners of the region with particle effects
                bukkitWorld.spawnParticle(Particle.REDSTONE, minPoint.getX(), minPoint.getY(), minPoint.getZ(), 1);
                bukkitWorld.spawnParticle(Particle.REDSTONE, maxPoint.getX(), minPoint.getY(), minPoint.getZ(), 1);
                bukkitWorld.spawnParticle(Particle.REDSTONE, minPoint.getX(), minPoint.getY(), maxPoint.getZ(), 1);
                bukkitWorld.spawnParticle(Particle.REDSTONE, maxPoint.getX(), minPoint.getY(), maxPoint.getZ(), 1);

                bukkitWorld.spawnParticle(Particle.REDSTONE, minPoint.getX(), maxPoint.getY(), minPoint.getZ(), 1);
                bukkitWorld.spawnParticle(Particle.REDSTONE, maxPoint.getX(), maxPoint.getY(), minPoint.getZ(), 1);
                bukkitWorld.spawnParticle(Particle.REDSTONE, minPoint.getX(), maxPoint.getY(), maxPoint.getZ(), 1);
                bukkitWorld.spawnParticle(Particle.REDSTONE, maxPoint.getX(), maxPoint.getY(), maxPoint.getZ(), 1);

            }
        }
    }

}
