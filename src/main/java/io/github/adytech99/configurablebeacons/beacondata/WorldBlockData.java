package io.github.adytech99.configurablebeacons.beacondata;

import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import java.io.Serializable;

public class WorldBlockData implements Serializable {
    private static final long serialVersionUID = 1L;
    private World world;
    private BlockPos blockPos;

    public WorldBlockData(World world, BlockPos blockPos) {
        this.world = world;
        this.blockPos = blockPos;
    }

    // Getter methods
    public World getWorld() {
        return world;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
