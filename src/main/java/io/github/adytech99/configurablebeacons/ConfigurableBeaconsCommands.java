package io.github.adytech99.configurablebeacons;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.adytech99.configurablebeacons.config.ConfigurableBeaconsConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/** Commands intended for dedicated-server operators. */
public final class ConfigurableBeaconsCommands {
    private ConfigurableBeaconsCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("configurablebeacons")
                .requires(ConfigurableBeaconsCommands::hasConfigurationPermission)
                .then(Commands.literal("reload")
                        .executes(context -> reload(context.getSource())))
                .then(Commands.literal("config")
                        .executes(context -> showConfigPath(context.getSource())))
                .then(Commands.literal("set")
                        .then(Commands.literal("radius")
                                .then(Commands.argument("level", IntegerArgumentType.integer(1, 4))
                                        .then(Commands.argument("blocks", IntegerArgumentType.integer(0, 29_999_984))
                                                .executes(ConfigurableBeaconsCommands::setRadius))))
                        .then(Commands.literal("duration")
                                .then(Commands.argument("level", IntegerArgumentType.integer(1, 4))
                                        .then(Commands.argument("ticks", IntegerArgumentType.integer(1, 1_200_000))
                                                .executes(ConfigurableBeaconsCommands::setDuration))))
                        .then(Commands.literal("force-load")
                                .then(Commands.argument("enabled", BoolArgumentType.bool())
                                        .executes(ConfigurableBeaconsCommands::setForceLoad))))
                .executes(context -> showHelp(context.getSource())));
    }

    /**
     * Minecraft 26.2 replaced numeric command levels with permission nodes. Reflection keeps this
     * command usable on the earlier 26.x releases without bundling version-specific source sets.
     */
    private static boolean hasConfigurationPermission(CommandSourceStack source) {
        try {
            Method permissions = source.getClass().getMethod("permissions");
            Object permissionSet = permissions.invoke(source);
            Class<?> permissionsClass = Class.forName("net.minecraft.server.permissions.Permissions");
            Field gameMasterPermission = permissionsClass.getField("COMMANDS_GAMEMASTER");
            Object permission = gameMasterPermission.get(null);
            for (Method method : permissionSet.getClass().getMethods()) {
                if (method.getName().equals("hasPermission") && method.getParameterCount() == 1) {
                    return (boolean) method.invoke(permissionSet, permission);
                }
            }
        } catch (ReflectiveOperationException ignored) {
            // Pre-26.2 uses the classic numeric operator level API below.
        }

        try {
            return (boolean) source.getClass().getMethod("hasPermission", int.class).invoke(source, 2);
        } catch (ReflectiveOperationException exception) {
            ConfigurableBeacons.LOGGER.warn("Unable to determine command permissions", exception);
            return false;
        }
    }

    private static int reload(CommandSourceStack source) {
        if (ConfigurableBeaconsConfig.HANDLER.load()) {
            source.sendSuccess(() -> Component.literal("Configurable Beacons settings reloaded."), true);
            return 1;
        }
        source.sendFailure(Component.literal("Configurable Beacons settings could not be reloaded; keeping the current settings."));
        return 0;
    }

    private static int showConfigPath(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("Configurable Beacons config: " + ConfigurableBeaconsConfig.CONFIG_PATH), false);
        return 1;
    }

    private static int showHelp(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("Configurable Beacons: use /configurablebeacons set, config, or reload. Run /configurablebeacons set radius|duration|force-load for server-side changes."), false);
        return 1;
    }

    private static int setRadius(CommandContext<CommandSourceStack> context) {
        int level = IntegerArgumentType.getInteger(context, "level");
        int blocks = IntegerArgumentType.getInteger(context, "blocks");
        ConfigurableBeaconsConfig config = ConfigurableBeaconsConfig.HANDLER.instance();
        switch (level) {
            case 1 -> config.level_one_beacon_radius = blocks;
            case 2 -> config.level_two_beacon_radius = blocks;
            case 3 -> config.level_three_beacon_radius = blocks;
            case 4 -> config.level_four_beacon_radius = blocks;
            default -> throw new IllegalArgumentException("Unsupported beacon level: " + level);
        }
        ConfigurableBeaconsConfig.HANDLER.save();
        context.getSource().sendSuccess(() -> Component.literal("Set level " + level + " beacon radius to " + blocks + " blocks."), true);
        return 1;
    }

    private static int setDuration(CommandContext<CommandSourceStack> context) {
        int level = IntegerArgumentType.getInteger(context, "level");
        int ticks = IntegerArgumentType.getInteger(context, "ticks");
        ConfigurableBeaconsConfig config = ConfigurableBeaconsConfig.HANDLER.instance();
        switch (level) {
            case 1 -> config.level_one_effects_duration = ticks;
            case 2 -> config.level_two_effects_duration = ticks;
            case 3 -> config.level_three_effects_duration = ticks;
            case 4 -> config.level_four_effects_duration = ticks;
            default -> throw new IllegalArgumentException("Unsupported beacon level: " + level);
        }
        ConfigurableBeaconsConfig.HANDLER.save();
        context.getSource().sendSuccess(() -> Component.literal("Set level " + level + " beacon effect duration to " + ticks + " ticks."), true);
        return 1;
    }

    private static int setForceLoad(CommandContext<CommandSourceStack> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        ConfigurableBeaconsConfig.HANDLER.instance().force_load_beacons = enabled;
        ConfigurableBeaconsConfig.HANDLER.save();
        context.getSource().sendSuccess(() -> Component.literal("Beacon force-loading is now " + (enabled ? "enabled" : "disabled") + "."), true);
        return 1;
    }
}
