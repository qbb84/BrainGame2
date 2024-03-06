package me.qbb84.braingame2.Test;

import com.mojang.authlib.GameProfile;
import me.qbb84.braingame2.BrainGame2;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LootChestEvent {

    private final List<Location> trailLocations;
    private final Player player;
    private double y1;
    private final double y2;
    private double y3;

    int finishedIteration = 0;


    private static final Supplier<Integer> randomToMax = () -> (int) (Math.random() * Integer.MAX_VALUE);
    private static final Function<Integer, Double> randomToX = i -> Math.random() * i;
    private static final BiFunction<Player, BlockPos, Location> locationFromBlockPos =
            (p, l) -> new Location(p.getWorld(), l.getX(), l.getY(), l.getZ());

    private ArmorStand progressStand;
    private ArrayList<BlockPos> blockListToArrayList;

    private final ArrayList<Integer> progressBarSize = new ArrayList<>();
    private List<BlockPos> newListWithoutAirBlocks;
    private static int size = 0;

    private List<ItemStack> chestContents;

    public LootChestEvent(Player player) {
        y1 = 0;
        y2 = 0;
        y3 = 0;

        this.player = player;
        trailLocations = new ArrayList<>();

//        Arrays.stream(LootChestInventory.getGetLootChestInventory().get(player.getUniqueId()).getContents())
//        .forEach(itemStack -> chestContents.add(itemStack));
    }

    public void start(ItemStack item) {
        chestContents = new ArrayList<>();
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_PEARL_THROW, 1, 1);

        Egg chestEgg = player.launchProjectile(Egg.class);

        chestEgg.setItem(item);
        chestEgg.setBounce(false);
        chestEgg.setInvulnerable(true);


        new BukkitRunnable() {

            @Override
            public void run() {
                if (chestEgg.isDead()) {
                    Location centerOfBlock = chestEgg.getLocation();
                    Location bottomCenterOfBlock =
                            new Location(
                                    centerOfBlock.getWorld(),
                                    centerOfBlock.getBlockX() + 0.5,
                                    centerOfBlock.getBlockY() + 1,
                                    centerOfBlock.getBlockZ() + 0.5);

                    scene2(bottomCenterOfBlock, player, item, trailLocations.get(trailLocations.size() - 1));
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
    }

    public void scene2(
            Location location, Player player, ItemStack chestItem, Location endingLocation) {

        ArmorStand stand = (ArmorStand) player.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        stand.setVisible(false);
        stand.setGravity(false);
        stand.setHelmet(chestItem);
        player.getWorld().createExplosion(location, 0);
        player.getWorld().playEffect(location, Effect.DRAGON_BREATH, 5);

        stand.setCanPickupItems(false);

        new BukkitRunnable() {
            private final float rotationSpeed = 250;
            private float angleNew = 0;

            @Override
            public void run() {
                double offsetX = Math.random() * 0.2 - 0.1;
                double offsetY = Math.random() * 0.2 - 0.1;
                double offsetZ = Math.random() * 0.2 - 0.1;

                double angle = Math.random() * 2 * Math.PI;
                double radius = 0.3;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);

                angleNew += rotationSpeed;
                stand.setHeadPose(new EulerAngle(offsetX, offsetY, offsetZ));

                if (y1 % 4 == 0) {
                    angleNew += rotationSpeed;
                    stand.setRotation(angleNew, (float) angle);
                }

                Location chestLocation = stand.getLocation();
                double particleHeight = Math.random() * 0.5;
                Location particleLocation =
                        new Location(
                                chestLocation.getWorld(),
                                chestLocation.getX() + x,
                                chestLocation.getY() + 1 + particleHeight,
                                chestLocation.getZ() + z);
                chestLocation.getWorld().spawnParticle(Particle.CRIT, particleLocation, 5);

                player.playSound(chestLocation, Sound.BLOCK_STONE_BREAK, 1, 2);

                y1 += 0.5;

                if (y1 >= 20) {

                    scene3(stand);
                    this.cancel();
                }
            }
        }.runTaskTimer(BrainGame2.getPlugin(), 0, 3);
    }

    public void scene3(ArmorStand stand) {
        stand.getWorld().createExplosion(stand.getLocation(), 0);

        ItemStack openedChest = new ItemStack(Material.CHEST);
        Location chestLocation =
                new Location(
                        stand.getWorld(),
                        stand.getLocation().getX(),
                        stand.getLocation().getY() + 1,
                        stand.getLocation().getZ());

        stand.getLocation().add(0, 1, 0).getBlock().setType(Material.CHEST);
        Block block = stand.getWorld().getBlockAt(stand.getLocation().add(0, 1, 0));

        stand.setHeadPose(new EulerAngle(0, 0, 0));
        setBlock(stand.getLocation().add(0, 1, 0).getBlock(), Material.CHEST, stand.getFacing());

        new BukkitRunnable() {
            @Override
            public void run() {
                if (y3 == 1) {
                    stand.getWorld().createExplosion(stand.getLocation(), 0);
                    packetOpenChest(stand.getLocation().add(0, 1, 0), player);

                    stand.remove();

                } else if (y3 == 2) {
                    stand.getWorld().createExplosion(chestLocation, 0);
                    Giant giant = (Giant) stand.getWorld().spawnEntity(stand.getLocation().add(1.5, -5, 4.5),
                            EntityType.GIANT);


                    giant.setAI(false);
                    giant.setInvisible(true);
                    giant.setCollidable(false);
                    giant.setInvulnerable(true);
                    giant.getEquipment().setItemInMainHand(openedChest);
                    giant.setSilent(true);

                    Location location = giant.getLocation();
                    location.setYaw(53f);
                    location.setPitch(53f);
                    location.setDirection(new Vector(3, 4, 10));
                    giant.teleport(location);

                    spawnZombieCorpse(player, stand.getLocation().add(1.5, -5, 4.5), block.getLocation().add(0.5, 0.5
                            , 0.5));

                }
                y3++;
                if (y3 > 3) {

                    y3 = 0;
                    cancel();
                }
            }
        }.runTaskTimer(BrainGame2.getPlugin(), 0, 20);
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

    private void packetOpenChest(Location location, Player player) {
        var x1 = location.getBlockX();
        var y1 = location.getBlockY();
        var z1 = location.getBlockZ();

        BlockPos pos = new BlockPos(x1, y1, z1);
        ClientboundBlockEventPacket packet = new ClientboundBlockEventPacket(pos, Blocks.CHEST, 1, 1);
        ((CraftPlayer) player).getHandle().connection.send(packet);
    }

    private static Axis convertBlockFaceToAxis(BlockFace face) {
        return switch (face) {
            case NORTH, SOUTH -> Axis.Z;
            case EAST, WEST -> Axis.X;
            case UP, DOWN -> Axis.Y;
            default -> Axis.X;
        };
    }

    public void playChestAction(Location location, boolean open) {
        ServerLevel world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPos position =
                new BlockPos((int) location.getX(), (int) location.getY(), (int) location.getZ());
        world.updateNeighborsAt(position, Blocks.CHEST);
        BlockState blockState = world.getBlockState(position);

        if (blockState.getBlock() instanceof ChestBlock) {
            ChestBlockEntity tileChest = (ChestBlockEntity) world.getBlockEntity(position);

            if (tileChest == null) {
                location.getBlock().setType(Material.CHEST);
                tileChest = (ChestBlockEntity) world.getBlockEntity(position);
            }

            if (tileChest != null) {
                world.blockEvent(position, blockState.getBlock(), 1, open ? 1 : 0);
            }
        } else {
            Bukkit.getServer()
                    .broadcastMessage(
                            "The block at "
                                    + location
                                    + " is not a chest block."
                                    + " It's: "
                                    + blockState.getBlock().toString());
        }
    }

    public void spawnCircle(Player player, Location location) {


        for (int degrees = 0; degrees < 360; degrees++) {
            var radians = Math.toRadians(degrees);

            double x = Math.sin(radians), y = Math.cos(radians);
            player.spawnParticle(
                    Particle.WATER_BUBBLE,
                    new Location(player.getWorld(), location.getX(), location.getY(), location.getZ())
                            .add(x, 1, y),
                    1);

            player.spawnParticle(
                    Particle.WATER_BUBBLE,
                    new Location(player.getWorld(), location.getX(), location.getY(), location.getZ())
                            .add(y, x + 1, 0),
                    1);


        }

    }

    public void spawnZombieCorpse(Player player, Location spawnLocation, Location chestLocation) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), player.getName());
        ServerPlayer entity = new ServerPlayer(
                ((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) player.getWorld()).getHandle(),
                profile);

//        ServerLevel world = ((Level) Bukkit.getServer()).getWorld().getHandle();
//        net.minecraft.world.entity.Entity giantZombie =
//                new net.minecraft.world.entity.monster.Giant(net.minecraft.world.entity.EntityType.GIANT, world);
//
//
//        //Giant Entity
//        ServerEntity giantChestEntity = new ServerEntity(world, giantZombie, 20, true, null, null);


        var x = spawnLocation.getX();
        var y = spawnLocation.getY();
        var z = spawnLocation.getZ();

        entity.setPos(x, y, z);
        entity.setPose(net.minecraft.world.entity.Pose.SLEEPING);
        entity.setItemInHand(InteractionHand.MAIN_HAND, new net.minecraft.world.item.ItemStack(Blocks.CHEST));

        ServerPlayer connection = ((CraftPlayer) player).getHandle();


        var size1 = 10;
        AtomicInteger height = new AtomicInteger();

        LootChestInventory.getGetLootChestInventory().get(player.getUniqueId()).forEach(item -> height.addAndGet(item.getAmount()));


        BlockPos pos1 = new BlockPos((int) x - size1 / 2, (int) y + 10, (int) z - size1 / 2);
        BlockPos pos2 = new BlockPos((int) x + size1 / 2, (int) y + 10, (int) z + size1 / 2);
        Iterable<BlockPos> blockList = BlockPos.betweenClosed(pos1, pos2);


        blockListToArrayList = new ArrayList<>();

        for (BlockPos pos : blockList) {
            blockListToArrayList.add(pos.immutable());
            size++;
        }

//        for (BlockPos pos : blockListToArrayList) player.sendMessage(pos.toString());

        int centerX = (pos1.getX() + pos2.getX()) / 2;
        int centerY = (pos1.getY() + pos2.getY()) / 2;
        int centerZ = (pos1.getZ() + pos2.getZ()) / 2;

        Location centerLocation = new Location(player.getWorld(), centerX, centerY + 20, centerZ);

        List<ItemStack> inputtedItems = LootChestInventory.getGetLootChestInventory().get(player.getUniqueId());


        new BukkitRunnable() {
            @Override
            public void run() {
                BlockPos pos = blockListToArrayList.get(finishedIteration);
                Location blockLocation = new Location(player.getWorld(), pos.getX(), pos.getY(), pos.getZ());
                blockLocation.getBlock().setType(getNextItemInChest(player, inputtedItems).getType());
                spawnParticleLine(player, chestLocation, blockLocation, Particle.ELECTRIC_SPARK);
                spawnParticlesAroundLocation(blockLocation, Particle.ELECTRIC_SPARK);
                spawnParticlesAroundLocation(chestLocation, Particle.ELECTRIC_SPARK);
                player.getWorld().playSound(blockLocation, Sound.BLOCK_STONE_PLACE, 1, 1);

                finishedIteration++;
                if (finishedIteration >= size) {
                    for (BlockPos pos1 : blockList) {
                        Location blockLoc = new Location(player.getWorld(), pos1.getX(), pos1.getY(), pos1.getZ());
                        spawnParticlesAroundLocation(blockLoc, Particle.VILLAGER_HAPPY);
                    }
                    player.getWorld().playSound(blockLocation, Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1);
                    executeSecondRunnable(player, centerLocation, blockListToArrayList, connection);
                    cancel();
                }

            }
        }.runTaskTimer(BrainGame2.getPlugin(), 0, 2);


    }


    private void executeSecondRunnable(Player player, Location centerLocation, List<BlockPos> blockListToArrayList,
                                       ServerPlayer connection) {
        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

        List<BlockPos> blockListClone = new ArrayList<>(blockListToArrayList.stream().toList());

        Location location = centerLocation.clone().subtract(0, 10, 4);
        spawnGiantZombie(location.add(1.5, 0, 0));

        progressStand = (ArmorStand) player.getWorld().spawnEntity(centerLocation.clone().subtract(0, 9, 0),
                EntityType.ARMOR_STAND);
        progressStand.setVisible(false);
        progressStand.setGravity(false);
        double percentageComplete = (double) progressBarSize.size() / size;
        progressStand.setCustomName(getProgressBar(percentageComplete));
        progressStand.setCustomNameVisible(true);

        newListWithoutAirBlocks = blockListClone.stream()
                .filter(pos -> locationFromBlockPos.apply(player, pos).getBlock().getType() != Material.AIR)
                .toList();

        new BukkitRunnable() {
            int blockUpdate = 1;
            BlockPos pos = newListWithoutAirBlocks.get(new Random().nextInt(newListWithoutAirBlocks.size()));
            int randID = new Random().nextInt(Integer.MAX_VALUE);


            @Override
            public void run() {
                Location blockLocation = new Location(player.getWorld(), pos.getX(), pos.getY(), pos.getZ());


                if (blockUpdate >= 9) {
                    moveBlockTowardCenter(blockLocation, centerLocation, blockLocation);
                    connection.connection.send(new ClientboundBlockDestructionPacket(randID, pos,
                            -1));

                    blockLocation.getBlock().setType(Material.AIR);


                    if (newListWithoutAirBlocks.size() == 1) {
                        this.cancel();
                    }

                    List<Material> materials = new ArrayList<>(blockListClone.stream()
                            .map(pos -> player.getWorld().getBlockAt(new Location(
                                            player.getWorld(),
                                            pos.getX(),
                                            pos.getY(),
                                            pos.getZ()))
                                    .getType())
                            .collect(Collectors.toList()));


                    List<Material> rotatedMaterials = Rotation.rotateList(materials, Rotation._90_DEGREES_RIGHT);

                    for (BlockPos pos : blockListClone) {
                        Block block = player.getWorld().getBlockAt(new Location(
                                player.getWorld(),
                                pos.getX(),
                                pos.getY(),
                                pos.getZ()));
                        block.setType(Material.AIR);
                    }

                    blockListToArrayList.clear();
                    blockListToArrayList.addAll(Rotation.rotateList(blockListClone, Rotation._90_DEGREES_RIGHT));

                    for (int i = 0; i < blockListClone.size(); i++) {
                        BlockPos pos = blockListClone.get(i);
                        Block block = player.getWorld().getBlockAt(new Location(
                                player.getWorld(),
                                pos.getX(),
                                pos.getY(),
                                pos.getZ()));
                        block.setType(rotatedMaterials.get(i));
                    }

                    newListWithoutAirBlocks = blockListClone.stream()
                            .filter(pos -> locationFromBlockPos.apply(player, pos).getBlock().getType() != Material.AIR)
                            .toList();
                    player.sendMessage(String.valueOf(newListWithoutAirBlocks.size()));
                    pos = newListWithoutAirBlocks.get(new Random().nextInt(newListWithoutAirBlocks.size()));
                    randID = new Random().nextInt(Integer.MAX_VALUE);
                    blockUpdate = 1;


                }
                Block block = player.getWorld().getBlockAt(blockLocation);

                spawnParticlesAroundLocation(blockLocation, Particle.ELECTRIC_SPARK);
                connection.connection.send(new ClientboundBlockDestructionPacket(randID, pos,
                        blockUpdate));

                spawnCircle(player, blockLocation.clone().add(0.5, 0.5, 0.5));

                spawnParticleLine(player, block.getLocation().add(0.5, 0.5, 0.5), centerLocation,
                        Particle.ELECTRIC_SPARK);

                player.getWorld().playSound(new Location(player.getWorld(), pos.getX(), pos.getY(), pos.getZ()),
                        Sound.BLOCK_STONE_BREAK, 1, 1);
                blockUpdate++;
            }
        }.runTaskTimer(BrainGame2.getPlugin(), 0, 3);
    }

    private void spawnParticleLine(Player player, Location start, Location end, Particle particle) {
        int points = 15;
        double intervalX = (end.getX() - start.getX()) / points;
        double intervalY = (end.getY() - start.getY()) / points;
        double intervalZ = (end.getZ() - start.getZ()) / points;

        for (int i = 0; i < points; i++) {
            double x = start.getX() + (i + 0.5) * intervalX;  // Adjusted to center of interval
            double y = start.getY() + (i + 0.5) * intervalY;  // Adjusted to center of interval
            double z = start.getZ() + (i + 0.5) * intervalZ;  // Adjusted to center of interval

            player.getWorld().spawnParticle(particle, x, y, z, 1, 0, 0, 0, 0);
        }
    }


    public void spawnParticlesAroundLocation(Location location, Particle particle) {
        location.clone().add(0.5, 0, 0.5);
        double angle = Math.random() * 2 * Math.PI;
        double radius = 0.3;
        double x = radius * Math.cos(angle);
        double z = radius * Math.sin(angle);

        double particleHeight = Math.random() * 0.5;
        Location particleLocation =
                new Location(
                        location.getWorld(),
                        location.getX() + x,
                        location.getY() + 1 + particleHeight,
                        location.getZ() + z);
        location.getWorld().spawnParticle(particle, particleLocation, 5);


    }

    public void spawnGiantZombie(Location location) {
        Giant giant = (Giant) location.getWorld().spawnEntity(location,
                EntityType.GIANT);


        giant.setAI(false);
        giant.setInvisible(true);
        giant.setCollidable(false);
        giant.setInvulnerable(true);
        giant.getEquipment().setItemInMainHand(new ItemStack(Material.CHEST));
        giant.setSilent(true);

        giant.setCustomName("YOOOOOOOOO");
        giant.setCustomNameVisible(true);

    }


    private org.bukkit.block.BlockFace getFacingDirection(Location from, Location to) {
        double deltaX = to.getX() - from.getX();
        double deltaZ = to.getZ() - from.getZ();

        double angle = Math.toDegrees(Math.atan2(deltaZ, deltaX)) + 180;

        if (0 <= angle && angle < 45) {
            return org.bukkit.block.BlockFace.EAST;
        } else if (45 <= angle && angle < 135) {
            return org.bukkit.block.BlockFace.SOUTH;
        } else if (135 <= angle && angle < 225) {
            return org.bukkit.block.BlockFace.WEST;
        } else {
            return org.bukkit.block.BlockFace.NORTH;
        }
    }


    private void moveBlockTowardCenter(Location particleLocation, Location start, Location end) {
        Block block = particleLocation.getBlock();

        double duration = 18.0;  // Adjust this value based on the desired movement speed
        int ticks = 60;  // Number of ticks (1 second = 20 ticks)
        double interval = duration / ticks;

        Vector direction = start.toVector().subtract(end.toVector()).normalize();
        Vector step = direction.clone().multiply(interval);

        Location currentLocation = particleLocation.clone();


        ArmorStand armorStand = particleLocation.getWorld().spawn(currentLocation.add(0.5, 0, 0.5),
                ArmorStand.class);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setMarker(true);

        armorStand.setHelmet(new ItemStack(Material.STONE));


        new BukkitRunnable() {
            int tickCount = 0;

            final int particlesToSpawn = 60;
            int particlesSpawned = 0;

            @Override
            public void run() {
                currentLocation.add(step);

                if (++particlesSpawned <= particlesToSpawn) {
                    Location particleLineLocation = currentLocation.clone().subtract(0.1, 0, 0.0);
                    particleLineLocation.getWorld().spawnParticle(Particle.SMOKE_NORMAL, particleLineLocation, 1, 0
                            , 0, 0,
                            0);
                }

//                end.getBlock().setType(Material.AIR);

                float rotation = (float) (360 * tickCount / ticks);
                armorStand.setHeadPose(new EulerAngle(Math.toRadians(rotation), Math.toRadians(rotation), 0));


                armorStand.teleport(currentLocation);

                currentLocation.getWorld().playSound(currentLocation, Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);


                if (++tickCount >= ticks) {
                    // Ensure the final location is set correctly
                    end.getBlock().getWorld().playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

                    progressBarSize.add(1);

                    double percentageComplete = (double) progressBarSize.size() / size;
                    progressStand.setCustomName(getProgressBar(percentageComplete));

                    player.sendMessage(progressBarSize.size() + " / " + size);

                    spawnParticlesInDirections(start.clone().subtract(0, 2, 0), 2, Particle.VILLAGER_HAPPY);


                    // Remove the ArmorStand
                    armorStand.remove();

                    cancel();
                }
            }
        }.runTaskTimer(BrainGame2.getPlugin(), 0, 1);  // Run the task every tick for smoother animation
    }


    private void spawnParticlesInDirections(Location location, double spread, Particle particle) {
        int particleCount = 50;

        for (int i = 0; i < particleCount; i++) {
            double offsetX = (Math.random() * 2 - 1) * spread;
            double offsetY = Math.random() * spread;
            double offsetZ = (Math.random() * 2 - 1) * spread;

            location.getWorld().spawnParticle(particle, location.getX() + offsetX,
                    location.getY() + offsetY,
                    location.getZ() + offsetZ, 1, 0, 0, 0, 0);
        }
    }

    private String getProgressBar(double percentage) {
        int bars = 10;
        int progress = (int) (percentage * 100 / bars);
        int remaining = bars - progress;

        StringBuilder progressBar = new StringBuilder(ChatColor.GOLD + "" + ChatColor.BOLD + "[");
        for (int i = 0; i < progress; i++) {
            progressBar.append(ChatColor.GREEN + "█");
        }
        for (int i = 0; i < remaining; i++) {
            progressBar.append(ChatColor.RED + "░");
        }
        progressBar.append(ChatColor.RESET).append(ChatColor.GOLD + "" + ChatColor.BOLD + "] ");

        // Display percentage
        progressBar.append(ChatColor.GOLD + "" + ChatColor.BOLD + String.format("%.2f%%", percentage * 100));

        return progressBar.toString();
    }

    private static <T> List<T> rotateList(List<T> list, int k) {
        List<T> rotated = new ArrayList<>(list.size());

        for (int i = 0; i < list.size(); i++) {
            rotated.add(list.get((i + k) % list.size()));
        }

        return rotated;
    }

    private static int[] shiftArray(int[] arr, int k) {
        int[] shifted = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            shifted[(i + k) % arr.length] = arr[i];
        }

        return shifted;
    }

    private ItemStack getNextItemInChest(Player player, List<ItemStack> itemStacks) {
        ItemStack found_item = null;
        for (ItemStack item : itemStacks) {
            found_item = item;
            break;
        }

        var deduction = found_item.getAmount() - 1;
        if (deduction > 1) {
            List<ItemStack> new_list = new ArrayList<>(itemStacks);
            new_list.remove(found_item);
            found_item.setAmount(deduction);
            new_list.add(found_item);

            LootChestInventory.getGetLootChestInventory().put(player.getUniqueId(), new_list);
        } else {
            List<ItemStack> new_list = new ArrayList<>(itemStacks);
            new_list.remove(found_item);
            LootChestInventory.getGetLootChestInventory().put(player.getUniqueId(), new_list);
            return found_item;
        }
        return found_item;
    }


    public static Location rayTraceToNearestUnoccupiedBlockOfType(Location blockLocation, Material targetType) {
        World world = blockLocation.getWorld();
        double radius = 5;
        int numPoints = 36;

        Location nearestUnoccupiedBlock = null;
        double nearestDistanceSquared = Double.MAX_VALUE;

        for (int i = 0; i < numPoints; i++) {
            double angle = 2 * Math.PI * i / numPoints;
            double xOffset = radius * Math.cos(angle);
            double zOffset = radius * Math.sin(angle);

            Vector direction = new Vector(xOffset, 0, zOffset);
            Location startLocation = blockLocation.clone().add(0.5, 0.5, 0.5).add(direction);

            RayTraceResult result = world.rayTrace(
                    startLocation,
                    new Vector(0, -1, 0),
                    10,
                    FluidCollisionMode.ALWAYS,
                    true,
                    0.2,
                    null);

            world.spawnParticle(Particle.VILLAGER_HAPPY, startLocation, 1);

            if (result != null && result.getHitBlock() != null && result.getHitBlock().getType() == targetType) {
                double distanceSquared = blockLocation.distanceSquared(result.getHitBlock().getLocation());

                // Check if the current hit block is unoccupied
                if (!result.getHitBlock().getLocation().getBlock().isPassable()) {
                    if (distanceSquared < nearestDistanceSquared) {
                        nearestDistanceSquared = distanceSquared;
                        nearestUnoccupiedBlock = result.getHitBlock().getLocation().add(0.5, 0.5, 0.5);
                    }
                }
            }
        }

        return nearestUnoccupiedBlock;
    }

    public boolean isSquare(int amount) {
        return Math.cbrt(amount) == (int) Math.cbrt(amount) || Math.sqrt(amount) == (int) Math.sqrt(amount);
    }

}
