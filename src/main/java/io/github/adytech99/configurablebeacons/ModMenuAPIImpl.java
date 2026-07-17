package io.github.adytech99.configurablebeacons;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import io.github.adytech99.configurablebeacons.config.ConfigurableBeaconsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ModMenuAPIImpl implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            YetAnotherConfigLib generated = ConfigurableBeaconsConfig.HANDLER.generateGui();
            Component title = Minecraft.getInstance().hasSingleplayerServer()
                    ? Component.translatable("configurable-beacons.config.title.singleplayer")
                    : Component.translatable("configurable-beacons.config.title.multiplayer");
            return YetAnotherConfigLib.createBuilder()
                    .title(title)
                    .categories(generated.categories())
                    .save(generated.saveFunction())
                    .build()
                    .generateScreen(parent);
        };
    }
}
