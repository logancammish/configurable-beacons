package io.github.adytech99.configurablebeacons.beacondata;

import io.github.adytech99.configurablebeacons.config.ConfigurableBeaconsConfig;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

public class BeaconForceLoader {

    @Deprecated
    public static void runFullBeaconScan(World world){
        List<BlockPos> blockPosList = BlockPosFileManager.getBlockPosList(world);
        for (BlockPos pos : blockPosList) {
            if(world.getBlockEntity(pos) != null) {
                if (world.getBlockEntity(pos) instanceof BeaconBlockEntity) {
                    if(BeaconBlockEntity.updateLevel(world, pos.getX(), pos.getY(), pos.getZ()) > 0){
                        BeaconForceLoader.forceLoadBeacon(world, Objects.requireNonNull(world.getBlockEntity(pos)));
                    }
                }
            }
        }
    }

    @Deprecated
    public static void forceLoadBeacon(World world, BlockEntity beacon){
        ServerWorld serverWorld = (ServerWorld) world;
        if(!BlockPosFileManager.getBlockPosList(world).contains(beacon.getPos())){
            BlockPosFileManager.addBlockPos(world, beacon.getPos());
        }
        updateBeacon(serverWorld, beacon);
    }

    @Deprecated
    public static void unForceLoadBeacon(World world, BlockEntity beacon){
        ServerWorld serverWorld = (ServerWorld) world;
        serverWorld.setChunkForced(serverWorld.getWorldChunk(beacon.getPos()).getPos().x, serverWorld.getWorldChunk(beacon.getPos()).getPos().z, false);
        BeaconLocationsFileManager.removeBlockPosFromWorld(world, beacon.getPos());
        updateBeacon(serverWorld, beacon);
    }

    @Deprecated
    public static void updateBeacon(ServerWorld world, BlockEntity beacon){
        if(!ConfigurableBeaconsConfig.HANDLER.instance().force_load_beacons)
            world.setChunkForced(world.getChunk(beacon.getPos()).getPos().x, world.getChunk(beacon.getPos()).getPos().z, false);
    }
}
