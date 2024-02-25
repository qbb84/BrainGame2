package me.qbb84.braingame2.Test;

import me.qbb84.braingame2.BrainGame2;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.List;

//Throwable chest that is throwable
//On hit the chest spawns a larger chest
//Sequence starts by larger chest blowing up, spawning event bow
// Picking up bow freezes the player and starts the sequence of X chests spawning in a circle
//Chests then rise and move around like annoying bees with particles
//CHESTS ARE SHOOTABLE TO OBTAIN LOOT
//BOW DOESN'T HAVE DRAWBACK //NMS//
//LOOT FALLS OUT CHESTS LIKE PIÑATAS
//LOOT CONTAINS NAME TAGS OF ITEMS

public class TestEvent implements Listener {

    double y1 = 0, y2 = 0;

    @EventHandler
    public void onMove(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("5-Star Chest")
                && event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            player.sendMessage("hey");


            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_PEARL_THROW, 1, 1);

            Egg chestEgg = player.launchProjectile(Egg.class);
            chestEgg.setItem(event.getItem());
            chestEgg.setBounce(false);
            chestEgg.setInvulnerable(true);


            List<Location> trailLocations = new ArrayList<>();

            new BukkitRunnable() {

                @Override
                public void run() {
                    if (chestEgg.isDead()) {
                        scene2(chestEgg.getLocation(), player, event.getItem(), trailLocations.get(trailLocations.size() - 1));
                        trailLocations.clear();
                        cancel();
                    }
                    Location currentLocation = chestEgg.getLocation();
                    trailLocations.add(currentLocation.clone());

                    if (trailLocations.size() > 20) {
                        trailLocations.remove(0);
                    }

                    for (Location location : trailLocations) {
                        location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location, 1);


                    }
                    player.playNote(chestEgg.getLocation(), Instrument.PLING, Note.sharp(1, Note.Tone.A));
                }
            }.runTaskTimer(BrainGame2.getPlugin(), 0, 1);




            event.setCancelled(true);
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

                @Override
                public void run() {

                    for (int degrees = 0; degrees < 360; degrees++) {
                        var radians = Math.toRadians(degrees);

                        double x = Math.sin(radians), y = Math.cos(radians);
                        player.spawnParticle(Particle.DRIP_WATER, new Location(player.getWorld(), playerX, playerY, playerZ)
                                .add(x, y1, y), 1);

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

    public void scene2(Location location, Player player, ItemStack chestItem, Location endingLocation) {

        ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setHelmet(chestItem);
        player.getWorld().createExplosion(location, 0);
        player.getWorld().playEffect(location, Effect.DRAGON_BREATH, 5);

        stand.setCanPickupItems(false);

        new BukkitRunnable() {
            @Override
            public void run() {
                double offsetX = Math.random() * 0.2 - 0.1; // Random offset between -0.1 and 0.1
                double offsetY = Math.random() * 0.2 - 0.1;
                double offsetZ = Math.random() * 0.2 - 0.1;

                double angle = Math.random() * 2 * Math.PI;
                double radius = 0.3;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);

                stand.setHeadPose(new EulerAngle(offsetX, offsetY, offsetZ));
                Location chestLocation = stand.getLocation();
                double particleHeight = Math.random() * 0.5;
                Location particleLocation = new Location(chestLocation.getWorld(),
                        chestLocation.getX() + x, chestLocation.getY() + 1 + particleHeight, chestLocation.getZ() + z);
                chestLocation.getWorld().spawnParticle(Particle.CRIT, particleLocation, 5);

                player.playSound(chestLocation, Sound.BLOCK_STONE_BREAK, 1, 2);


                y1 += 0.5;

                if (y1 >= 40) {
                    stand.setHeadPose(new EulerAngle(0, 0, 0));
                    ItemStack chestItem = stand.getHelmet();
                    ItemStack openedChest = new ItemStack(Material.CHEST);
                    endingLocation.getBlock().setType(Material.CHEST);
                    var x1 = endingLocation.getBlockX();
                    var y1 = endingLocation.getBlockY();
                    var z1 = endingLocation.getBlockZ();

                    BlockPos pos = new BlockPos(x1, y1, z1);
                    ClientboundBlockEventPacket packet = new ClientboundBlockEventPacket(pos, Blocks.CHEST, 1, 1);
                    ((CraftPlayer) player).getHandle().connection.send(packet);
                    stand.setHelmet(openedChest);

                    cancel();
                }
            }
        }.runTaskTimer(BrainGame2.getPlugin(), 0, 3); // Yo



    }

    public void spawnCircle(Player player, Location location) {
        new BukkitRunnable() {

            @Override
            public void run() {

                for (int degrees = 0; degrees < 360; degrees++) {
                    var radians = Math.toRadians(degrees);

                    double x = Math.sin(radians), y = Math.cos(radians);
                    player.spawnParticle(Particle.DRIPPING_HONEY, new Location(player.getWorld(), location.getX(), location.getY(), location.getZ())
                            .add(x, y2, y), 1);

                }
                y2 += 0.10;

                if (y2 >= 2) {
                    y2 = 0;
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(BrainGame2.getPlugin(), 0, 10);

    }

}



