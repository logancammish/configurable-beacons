package io.github.adytech99.configurablebeacons.mixin;

import io.github.adytech99.configurablebeacons.ConfigurableBeacons;
import io.github.adytech99.configurablebeacons.beacondata.BeaconLocationsFileManager;
import io.github.adytech99.configurablebeacons.config.ConfigurableBeaconsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Mixin(BeaconBlockEntity.class)
public class BeaconDistanceMixin {

	private static double getRadius(int beaconLevel) {
		if(beaconLevel == 1) return ConfigurableBeaconsConfig.HANDLER.instance().level_one_beacon_radius;
		if(beaconLevel == 2) return ConfigurableBeaconsConfig.HANDLER.instance().level_two_beacon_radius;
		if(beaconLevel == 3) return ConfigurableBeaconsConfig.HANDLER.instance().level_three_beacon_radius;
		if(beaconLevel == 4) return ConfigurableBeaconsConfig.HANDLER.instance().level_four_beacon_radius;
		else ConfigurableBeacons.LOGGER.info("[ConfigurableBeacons] Warning, beacon level does not match vanilla limits, defaulting to regular radius formula");
		return beaconLevel * 10 + 10;

	}

	private static int getEffectsDuration(int beaconLevel){
		if(beaconLevel == 1) return ConfigurableBeaconsConfig.HANDLER.instance().level_one_effects_duration;
		if(beaconLevel == 2) return ConfigurableBeaconsConfig.HANDLER.instance().level_two_effects_duration;
		if(beaconLevel == 3) return ConfigurableBeaconsConfig.HANDLER.instance().level_three_effects_duration;
		if(beaconLevel == 4) return ConfigurableBeaconsConfig.HANDLER.instance().level_four_effects_duration;
		else ConfigurableBeacons.LOGGER.info("[ConfigurableBeacons] Warning, beacon level does not match vanilla limits, defaulting to regular radius formula");
		return (9 + beaconLevel * 2) * 20;
	}

	@Inject(at = @At("HEAD"), method = "applyEffects", cancellable = true)
	private static void applyEffects(Level world, BlockPos pos, int beaconLevel, @Nullable Holder<MobEffect> primaryEffect, @Nullable Holder<MobEffect> secondaryEffect, CallbackInfo ci){
		ci.cancel();
		if (!world.isClientSide() && primaryEffect != null) {
			double d = getRadius(beaconLevel);
			int i = 0;
			if (beaconLevel >= 4 && Objects.equals(primaryEffect, secondaryEffect)) {
				i = 1;
			}
			int j = getEffectsDuration(beaconLevel);
			AABB box = (new AABB(pos)).inflate(d).expandTowards(0.0, world.getHeight(), 0.0);
			List<Player> list = world.getEntitiesOfClass(Player.class, box, player -> !player.isSpectator());
			Iterator var11 = list.iterator();

			Player playerEntity;
			while(var11.hasNext()) {
				playerEntity = (Player)var11.next();
				playerEntity.addEffect(new MobEffectInstance(primaryEffect, j, i, true, true));
			}

			if (beaconLevel >= 4 && !Objects.equals(primaryEffect, secondaryEffect) && secondaryEffect != null) {
				var11 = list.iterator();

				while(var11.hasNext()) {
					playerEntity = (Player)var11.next();
				playerEntity.addEffect(new MobEffectInstance(secondaryEffect, j, 0, true, true));
				}
			}

		}
		if(ConfigurableBeaconsConfig.HANDLER.instance().force_load_beacons) BeaconLocationsFileManager.addBlockPosToWorld(world, pos);
	}
}
