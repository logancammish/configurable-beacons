package io.github.adytech99.configurablebeacons.mixin;

import io.github.adytech99.configurablebeacons.beacondata.BeaconForceLoader;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(BeaconBlockEntity.class)
public class BeaconForceLoaderUpdaterMixin {

    @Inject(at = @At("HEAD"), method = "updateLevel")
    private static void Performance(World world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        if (!world.isClient) {
            //if(world.isChunkLoaded(new BlockPos(x, y, z))) return;
            //else cir.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "updateLevel")
    private static void updateLevel(World world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        if (!world.isClient) {
            if (cir.getReturnValue() == 0) {
                BeaconForceLoader.unForceLoadBeacon(world, Objects.requireNonNull(world.getBlockEntity(new BlockPos(x, y, z))));
            }

        }
    }
}
