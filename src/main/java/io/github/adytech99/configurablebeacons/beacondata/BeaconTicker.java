package io.github.adytech99.configurablebeacons.beacondata;

import io.github.adytech99.configurablebeacons.config.ConfigurableBeaconsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;

import java.util.ArrayList;
public class BeaconTicker{
    public static void saveAndStop(MinecraftServer server){
        BeaconLocationsFileManager.saveToFile(server);
    }

    public static void efficientTick(MinecraftServer server){
        if(ConfigurableBeaconsConfig.HANDLER.instance().force_load_beacons) {
            ArrayList<GlobalPos> globalPosList = BeaconLocationsFileManager.getMainBeaconLocationList();
            for (GlobalPos globalPos : globalPosList) {
                Level world = server.getLevel(globalPos.dimension());
                if (world == null) continue;
                BlockPos blockPos = globalPos.pos();
                if (world.getChunkAt(blockPos).getBlockEntity(blockPos) instanceof BeaconBlockEntity beaconBlockEntity) {
                    BeaconBlockEntity.tick(world, blockPos, world.getBlockState(blockPos), beaconBlockEntity);
                } else {
                    BeaconLocationsFileManager.removeBlockPosFromWorld(world, blockPos);
                }

            }
        }
    }

}
