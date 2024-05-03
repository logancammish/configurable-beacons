package io.github.adytech99.configurablebeacons.event;

import io.github.adytech99.configurablebeacons.beacondata.BeaconLocationsFileManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;


public class BeaconBreakEventHandler {
    public static void blockBreakEventHandler(World world, @Nullable PlayerEntity player, @Nullable BlockPos pos, @Nullable BlockState blockState, @Nullable BlockEntity blockEntity) {
        if(blockEntity != null){
            if(blockEntity instanceof BeaconBlockEntity){
                BeaconLocationsFileManager.removeBlockPosFromWorld(world, blockEntity.getPos());
            }
        }
    }
}
