package io.github.adytech99.configurablebeacons;

import io.github.adytech99.configurablebeacons.beacondata.BeaconLocationsFileManager;
import io.github.adytech99.configurablebeacons.beacondata.BeaconTicker;
import io.github.adytech99.configurablebeacons.config.ConfigurableBeaconsConfig;
import io.github.adytech99.configurablebeacons.event.BeaconBreakEventHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigurableBeacons implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("configurable-beacons");

	@Override
	public void onInitialize() {
		ConfigurableBeaconsConfig.HANDLER.load();

		ServerLifecycleEvents.SERVER_STARTED.register(BeaconLocationsFileManager::loadFromFile);

		ServerTickEvents.START_SERVER_TICK.register(BeaconTicker::efficientTick);
		PlayerBlockBreakEvents.AFTER.register(BeaconBreakEventHandler::blockBreakEventHandler);
		ServerTickEvents.END_SERVER_TICK.register(server -> {
			if(ConfigurableBeaconsConfig.HANDLER.instance().force_load_beacons && server.getTicks() % 80 == 0){
				Iterable<ServerWorld> serverWorlds = server.getWorlds();
				//for(ServerWorld serverWorld : serverWorlds) BeaconForceLoader.runFullBeaconScan(serverWorld);
			}
		});

		ServerLifecycleEvents.SERVER_STOPPING.register(BeaconTicker::cancelExecutor);

		LOGGER.info("Let there be beams!");
	}
}