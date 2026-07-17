package io.github.adytech99.configurablebeacons.beacondata;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import java.io.Serializable;

public class WorldBlockData implements Serializable {
    private static final long serialVersionUID = 1L;
    private Level world;
    private BlockPos blockPos;

    public WorldBlockData(Level world, BlockPos blockPos) {
        this.world = world;
        this.blockPos = blockPos;
    }

    // Getter methods
    public Level getWorld() {
        return world;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
