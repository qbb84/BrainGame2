package me.qbb84.braingame2.Test;

import java.util.ArrayList;
import java.util.List;
import me.qbb84.braingame2.BrainGame2;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
import net.minecraft.server.level.ServerLevel;
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
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class LootChestEvent {

  private final List<Location> trailLocations;
  private double y1, y2, y3;
  private final Player player;

  public LootChestEvent(Player player) {
    y1 = 0;
    y2 = 0;
    y3 = 0;

    this.player = player;
    trailLocations = new ArrayList<>();
  }

  public void start(ItemStack item) {
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
      private float angleNew = 0;
      private final float rotationSpeed = 90;

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

        if (y1 % 3 == 0) stand.setRotation(angleNew * 2, (float) angle);

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
    //        stand.getWorld().createExplosion(stand.getLocation(), 0);

    ItemStack openedChest = new ItemStack(Material.CHEST);
    Location chestLocation =
        new Location(
            stand.getWorld(),
            stand.getLocation().getX(),
            stand.getLocation().getY() + 1,
            stand.getLocation().getZ());

    stand.getLocation().add(0, 1, 0).getBlock().setType(Material.CHEST);
    stand.setHeadPose(new EulerAngle(0, 0, 0));
    setBlock(stand.getLocation().add(0, 1, 0).getBlock(), Material.CHEST, stand.getFacing());

    new BukkitRunnable() {
      @Override
      public void run() {
        if (y3 == 1) {
          packetOpenChest(stand.getLocation().add(0, 1, 0), player);
          stand.remove();

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

  public void spawnCircle(Player player, Location location) {
    new BukkitRunnable() {

      @Override
      public void run() {

        for (int degrees = 0; degrees < 360; degrees++) {
          var radians = Math.toRadians(degrees);

          double x = Math.sin(radians), y = Math.cos(radians);
          player.spawnParticle(
              Particle.DRIPPING_HONEY,
              new Location(player.getWorld(), location.getX(), location.getY(), location.getZ())
                  .add(x, y2, y),
              1);
        }
        y2 += 0.10;

        if (y2 >= 2) {
          y2 = 0;
          this.cancel();
        }
      }
    }.runTaskTimerAsynchronously(BrainGame2.getPlugin(), 0, 10);
  }

  private void packetOpenChest(Location location, Player player) {
    var x1 = location.getBlockX();
    var y1 = location.getBlockY();
    var z1 = location.getBlockZ();

    BlockPos pos = new BlockPos(x1, y1, z1);
    ClientboundBlockEventPacket packet = new ClientboundBlockEventPacket(pos, Blocks.CHEST, 1, 1);
    ((CraftPlayer) player).getHandle().connection.send(packet);
  }
}
