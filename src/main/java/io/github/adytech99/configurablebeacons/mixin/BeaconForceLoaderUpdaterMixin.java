package io.github.adytech99.configurablebeacons.mixin;

import io.github.adytech99.configurablebeacons.beacondata.BeaconLocationsFileManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconBlockEntity.class)
public class BeaconForceLoaderUpdaterMixin {

    @Inject(at = @At("TAIL"), method = "updateBase")
    private static void updateBase(Level world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        if (!world.isClientSide()) {
            if (cir.getReturnValue() == 0) {
                BeaconLocationsFileManager.removeBlockPosFromWorld(world, new BlockPos(x, y, z));
            }

        }
    }
}
