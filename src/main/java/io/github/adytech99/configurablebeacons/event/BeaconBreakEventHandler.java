package io.github.adytech99.configurablebeacons.event;

import io.github.adytech99.configurablebeacons.beacondata.BeaconLocationsFileManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;


public class BeaconBreakEventHandler {
    public static void blockBreakEventHandler(Level world, @Nullable Player player, @Nullable BlockPos pos, @Nullable BlockState blockState, @Nullable BlockEntity blockEntity) {
        if(blockEntity != null){
            if(blockEntity instanceof BeaconBlockEntity){
                BeaconLocationsFileManager.removeBlockPosFromWorld(world, blockEntity.getBlockPos());
            }
        }
    }
}
