package co.alimad.client;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class shadowchakraEffects {

    public static boolean chakra = false;
    private static int fireTickCounter = 0;
    private static int ticksRemaining = 0;
    private static ServerPlayerEntity thePlayer;

    private static void summonFire() {
        World world = thePlayer.getWorld();
        BlockPos pos = thePlayer.getBlockPos().down();

        if (world.getBlockState(pos).isAir()) {
            world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 3);
        }

        if (world.getRandom().nextFloat() < 0.05f) {
            int radius = 12;
            int dx = world.getRandom().nextBetween(-radius, radius);
            int dz = world.getRandom().nextBetween(-radius, radius);
            int x = pos.getX() + dx;
            int y = pos.getY();
            int z = pos.getZ() + dz;

            LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world, SpawnReason.TRIGGERED);
            if (lightning != null) {
                lightning.refreshPositionAfterTeleport(x + 0.5, y, z + 0.5);
                world.spawnEntity(lightning);
            }
        }
    }

    public static void activateOpMode(World world, ServerPlayerEntity player) {
        chakra = true;
        ticksRemaining = 20 * 120;
        fireTickCounter = 0;

        corruptArea(world, player.getBlockPos(), 12);

        player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, ticksRemaining, 5, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, ticksRemaining, 3, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, ticksRemaining, 2, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, ticksRemaining, 2, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, ticksRemaining, 2, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, ticksRemaining, 2, false, false, false));
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, ticksRemaining, 1, false, false, false));

        thePlayer = player;
    }
    public static void deactivateOpMode() {
        chakra = false;
        ticksRemaining = 0;
        fireTickCounter = 0;
    }
    public static void registerTickHandler() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            if (!chakra) return;

            ticksRemaining--;
            if (ticksRemaining <= 0) {
                deactivateOpMode();
                return;
            }

            fireTickCounter++;
            if (fireTickCounter >= 2) {
                fireTickCounter = 0;
                summonFire();
            }
        });
    }
    public static void doSomething(World world, ServerPlayerEntity player) {
        activateOpMode(world, player);
    }
    public static void corruptArea(World world, BlockPos center, int radius) {
        Random randi = world.getRandom();
        int min = -radius;
        int max = radius;

        for(int i = 0; i < 12; i++) {
            int xo = randi.nextInt((max - min) + 1) + min;
            int yo = randi.nextInt((max - min) + 1) + min;
            LightningEntity lightning = EntityType.LIGHTNING_BOLT.create(world, SpawnReason.TRIGGERED);
            if (lightning != null) {
                lightning.refreshPositionAfterTeleport(center.getX() + xo, center.getY() + yo, center.getZ());
                world.spawnEntity(lightning);
            }
        }

        Random rand = world.getRandom();
        BlockPos.Mutable mut = new BlockPos.Mutable();

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    double distSq = dx * dx + dz * dz;
                    if (distSq <= radius * radius) {
                        mut.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                        var state = world.getBlockState(mut);
                        Block block = state.getBlock();

                        if (rand.nextFloat() < 0.8f) {
                            if (isNaturalBlock(block)) {
                                world.setBlockState(mut, Blocks.NETHERRACK.getDefaultState(), 3);
                            } else if (block == Blocks.WATER || block == Blocks.WATER_CAULDRON) {
                                world.setBlockState(mut, Blocks.LAVA.getDefaultState(), 3);
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isNaturalBlock(Block block) {
        return block == Blocks.GRASS_BLOCK
                || block == Blocks.DIRT
                || block == Blocks.STONE
                || block == Blocks.SAND
                || block == Blocks.GRAVEL
                || block == Blocks.COARSE_DIRT
                || block == Blocks.PODZOL;
    }
}
