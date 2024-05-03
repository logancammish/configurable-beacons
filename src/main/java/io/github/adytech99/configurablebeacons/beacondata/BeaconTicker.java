package io.github.adytech99.configurablebeacons.beacondata;

import io.github.adytech99.configurablebeacons.config.ConfigurableBeaconsConfig;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class BeaconTicker{


    private static final AtomicBoolean isRunning = new AtomicBoolean(false);


    public static void startServerTick(MinecraftServer server) {
        if(server.getTicks() % 1000 == 0) BeaconLocationsFileManager.saveToFile(server);
        if (!isRunning.getAndSet(true) && ConfigurableBeaconsConfig.HANDLER.instance().force_load_beacons) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                efficientTick(server);
                isRunning.set(false); // Reset the flag when the method finishes
            });
            executor.shutdown();
        }
        cancelExecutor(server);
    }

    public static void cancelExecutor(MinecraftServer server){
        isRunning.set(false);
        BeaconLocationsFileManager.saveToFile(server);
    }

    public static void efficientTick(MinecraftServer server){
        if(ConfigurableBeaconsConfig.HANDLER.instance().force_load_beacons) {
            ArrayList<GlobalPos> globalPosList = BeaconLocationsFileManager.getMainBeaconLocationList();
            for (GlobalPos globalPos : globalPosList) {
                World world = server.getWorld(globalPos.dimension());
                BlockPos blockPos = globalPos.pos();
                if (world.getWorldChunk(blockPos).getBlockEntity(blockPos) instanceof BeaconBlockEntity beaconBlockEntity) {
                    BeaconBlockEntity.tick(world, blockPos, world.getBlockState(blockPos), beaconBlockEntity);
                } else {
                    BeaconLocationsFileManager.removeBlockPosFromWorld(world, blockPos);
                }

            }
        }
    }

}
