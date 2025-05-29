package io.github.adytech99.configurablebeacons.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.autogen.AutoGen;
import dev.isxander.yacl3.config.v2.api.autogen.Boolean;
import dev.isxander.yacl3.config.v2.api.autogen.IntField;
import dev.isxander.yacl3.config.v2.api.autogen.TickBox;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import io.github.adytech99.configurablebeacons.mixin.BeaconDistanceMixin;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class ConfigurableBeaconsConfig {
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("ConfigurableBeaconsConfig.json");

    public static final ConfigClassHandler<ConfigurableBeaconsConfig> HANDLER = ConfigClassHandler.createBuilder(ConfigurableBeaconsConfig.class)
            .id(Identifier.of("configurable-beacons", "config"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(CONFIG_PATH)
                    .build())
            .build();


    @SerialEntry(comment = "default = 20")
    @AutoGen(category = "beacon_radius")
    @IntField(min = 0, max = (int) WorldBorder.MAX_CENTER_COORDINATES)
    public Integer level_one_beacon_radius = 20;

    @SerialEntry(comment = "default = 30")
    @AutoGen(category = "beacon_radius")
    @IntField(min = 0, max = (int) WorldBorder.MAX_CENTER_COORDINATES)
    public Integer level_two_beacon_radius = 30;

    @SerialEntry(comment = "default = 40")
    @AutoGen(category = "beacon_radius")
    @IntField(min = 0, max = (int) WorldBorder.MAX_CENTER_COORDINATES)
    public Integer level_three_beacon_radius = 40;

    @SerialEntry(comment = "default = 50")
    @AutoGen(category = "beacon_radius")
    @IntField(min = 0, max = (int) WorldBorder.MAX_CENTER_COORDINATES)
    public Integer level_four_beacon_radius = 50;

    @SerialEntry(comment = "Should beacons be force-loaded? This will allow them to work even if they are outside the render distance")
    @AutoGen(category = "beacon_radius")
    @TickBox
    public boolean force_load_beacons = false;

    @SerialEntry(comment = "default 220")
    @AutoGen(category = "beacon_duration")
    @IntField
    public int level_one_effects_duration = 220;

    @SerialEntry(comment = "default 260")
    @AutoGen(category = "beacon_duration")
    @IntField
    public int level_two_effects_duration = 260;

    @SerialEntry(comment = "default 220")
    @AutoGen(category = "beacon_duration")
    @IntField
    public int level_three_effects_duration = 300;


    @SerialEntry(comment = "default 220")
    @AutoGen(category = "beacon_duration")
    @IntField
    public int level_four_effects_duration = 340;

    public static Screen createScreen(@Nullable Screen parent) {
        return HANDLER.generateGui().generateScreen(parent);
    }
    public Screen createConfigScreen(Screen parent) {
        if (FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3")) {
            return createScreen(parent);
        }
        return null;
    }
}
