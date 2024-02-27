package me.qbb84.braingame2.Test;

import me.qbb84.braingame2.BrainGame2;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;


public class TestEvent implements Listener {

    double y1 = 0, y2 = 0, y3 = 0;

    @SafeVarargs
    private static <T extends AccessibleObject, P extends Entity> void printReflectionData(P playerEntity, T[]... reflectionType) {
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


        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("5-Star Chest") && event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
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
                        Location centerOfBlock = chestEgg.getLocation();
                        Location bottomCenterOfBlock = new Location(centerOfBlock.getWorld(), centerOfBlock.getBlockX() + 0.5, centerOfBlock.getBlockY() + 1, centerOfBlock.getBlockZ() + 0.5);

                        scene2(bottomCenterOfBlock, player, event.getItem(), trailLocations.get(trailLocations.size() - 1));
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
                double offsetX = Math.random() * 0.2 - 0.1;
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


//                    var x1 = endingLocation.getBlockX();
//                    var y1 = endingLocation.getBlockY();
//                    var z1 = endingLocation.getBlockZ();
//
////                    BlockPos pos = new BlockPos(x1, y1, z1);
////                    ClientboundBlockEventPacket packet = new ClientboundBlockEventPacket(pos, Blocks.CHEST, 1, 1);
////                    ((CraftPlayer) player).getHandle().connection.send(packet);


                    scene3(stand);
                    cancel();

                }
            }
        }.runTaskTimer(BrainGame2.getPlugin(), 0, 3);



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

    public void scene3(ArmorStand stand) {
//        stand.getWorld().createExplosion(stand.getLocation(), 0);

        ItemStack openedChest = new ItemStack(Material.CHEST);
        Location chestLocation = stand.getLocation().add(0, 1, 0);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (y3 == 0) {
//                    stand.getWorld().createExplosion(stand.getLocation(), 0);
                    stand.setHeadPose(new EulerAngle(0, 0, 0));
                    chestLocation.getBlock().setType(openedChest.getType());
                    setBlock(chestLocation.getBlock(), Material.CHEST, stand.getFacing());
                    playChestAction(chestLocation, true);
                } else if (y3 == 2) {
                    stand.getWorld().createExplosion(chestLocation, 0);
                    Giant giant = (Giant) stand.getWorld().spawnEntity(stand.getLocation(), EntityType.GIANT);

                    giant.setAI(false);
                    giant.setInvisible(true);
                    giant.setCollidable(false);
                    giant.setInvulnerable(true);
                    giant.getEquipment().setItemInMainHand(openedChest);
                }

                y3++;

                if (y3 > 3) {

                    y3 = 0;
                    cancel();
                }
            }
        }.runTaskTimerAsynchronously(BrainGame2.getPlugin(), 0, 20);

    }

    public void playChestAction(Location location, boolean open) {
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPos position = new BlockPos((int) location.getX(), (int) location.getY(), (int) location.getZ());
        ChestBlockEntity tileChest = (ChestBlockEntity) world.getBlockEntity(position);
        world.blockEvent(position, tileChest.getBlockState().getBlock(), 1, open ? 1 : 0);
    }

    private static Axis convertBlockFaceToAxis(BlockFace face) {
        switch (face) {
            case NORTH:
            case SOUTH:
                return Axis.Z;
            case EAST:
            case WEST:
                return Axis.X;
            case UP:
            case DOWN:
                return Axis.Y;
            default:
                return Axis.X;
        }
    }

    public static void setBlock(Block block, Material material, BlockFace blockFace) {
        block.setType(material);
        BlockData blockData = block.getBlockData();
        if (blockData instanceof Directional) {
            ((Directional) blockData).setFacing(blockFace);
            block.setBlockData(blockData);
        }
        if (blockData instanceof Orientable) {
            ((Orientable) blockData).setAxis(convertBlockFaceToAxis(blockFace));
            block.setBlockData(blockData);
        }
        if (blockData instanceof Rotatable) {
            ((Rotatable) blockData).setRotation(blockFace);
            block.setBlockData(blockData);
        }
    }

}



