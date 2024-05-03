package io.github.adytech99.configurablebeacons;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.adytech99.configurablebeacons.config.ConfigurableBeaconsConfig;


public class ModMenuAPIImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigurableBeaconsConfig::createScreen;
    }
}
