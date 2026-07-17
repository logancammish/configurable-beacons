package io.github.adytech99.configurablebeacons;

import io.github.adytech99.configurablebeacons.beacondata.BeaconLocationsFileManager;
import io.github.adytech99.configurablebeacons.beacondata.BeaconTicker;
import io.github.adytech99.configurablebeacons.config.ConfigurableBeaconsConfig;
import io.github.adytech99.configurablebeacons.event.BeaconBreakEventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurableBeacons implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("configurable-beacons");

	@Override
	public void onInitialize() {
		ConfigurableBeaconsConfig.HANDLER.load();
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
				ConfigurableBeaconsCommands.register(dispatcher));

		ServerLifecycleEvents.SERVER_STARTED.register(BeaconLocationsFileManager::loadFromFile);

		ServerTickEvents.START_SERVER_TICK.register(BeaconTicker::efficientTick);
		PlayerBlockBreakEvents.AFTER.register(BeaconBreakEventHandler::blockBreakEventHandler);
		ServerLifecycleEvents.SERVER_STOPPING.register(BeaconTicker::saveAndStop);

		LOGGER.info("Let there be beams!");
	}
}
