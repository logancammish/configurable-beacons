# Configurable Beacons

Configurable Beacons is a Fabric mod that lets a server or singleplayer world customise each beacon level's effect range and duration. It can also keep registered beacons active outside normal player range.

This project is a fork of the original [Configurable Beacons by AdyTech99](https://github.com/AdyTech99/configurable-beacons).

## Compatibility

| Component | Requirement |
| --- | --- |
| Minecraft | 26.0 through 26.2 |
| Mod loader | Fabric Loader 0.19.3 or newer |
| Java | Java 25 or newer |
| Environment | Client, singleplayer, or dedicated Fabric server |

Use matching Minecraft-version files for every dependency. This mod is Fabric-only; it does not run on Forge or NeoForge.

## Dependencies

These are required on every installation that uses Configurable Beacons:

- [Fabric Loader](https://fabricmc.net/use/installer/) — installs the Fabric Minecraft profile/server loader.
- [Fabric API](https://modrinth.com/mod/fabric-api) — Fabric's shared API library.
- [YetAnotherConfigLib v3 (YACL)](https://modrinth.com/mod/yacl) — stores and displays this mod's settings.

[Mod Menu](https://modrinth.com/mod/modmenu) is optional and client-side. Install it if you want a button in Minecraft's Mods screen that opens the configuration screen. Server commands and JSON configuration work without Mod Menu.

## Installation

### Client or singleplayer

1. Install Java 25 and the Fabric Loader profile for your Minecraft version.
2. Download the Fabric versions of Fabric API and YACL that match your Minecraft version. Optionally download Mod Menu too.
3. Put this mod's JAR and the required dependency JARs in your Minecraft instance's `mods` folder.
4. Start Minecraft with the Fabric profile.

### Dedicated server

1. Set up a Fabric server for the matching Minecraft version, using Java 25.
2. Put this mod's JAR, Fabric API, and YACL in the server's `mods` folder.
3. Start the server once. It creates `config/ConfigurableBeaconsConfig.json`.
4. Configure it with the commands below or by editing the JSON file, then reload it.

Clients do not need this mod to connect solely for server-side beacon behaviour. Clients who want the Mod Menu configuration screen need both this mod and its required client dependencies installed locally.

## Configuration

The configuration file is:

```text
config/ConfigurableBeaconsConfig.json
```

It controls the range and effect duration for beacon levels one through four, plus the `force_load_beacons` setting. Effect duration is measured in game ticks (20 ticks = 1 second).

In singleplayer, Mod Menu is labelled **Singleplayer server settings** because changing it affects the integrated server. In multiplayer, a server's configuration is authoritative; players should ask the server owner or operator to change it.

### Server owner/operator commands

These commands require the server's gamemaster/operator permission and can also be run from the dedicated-server console:

```text
/configurablebeacons
/configurablebeacons config
/configurablebeacons reload
/configurablebeacons set radius <level> <blocks>
/configurablebeacons set duration <level> <ticks>
/configurablebeacons set force-load <true|false>
```

Examples:

```text
/configurablebeacons set radius 4 100
/configurablebeacons set duration 4 600
/configurablebeacons set force-load true
```

`reload` applies manual JSON edits without restarting the server. Command changes are saved immediately. Use force-loading sparingly: it may load chunks beyond normal player range and can increase server work on worlds with many registered beacons.

## Building from source

Install JDK 25, then run:

```bash
./gradlew build
```

The uploadable mod file is created at:

```text
build/libs/configurable-beacons-3.1.0.jar
```

## Release notes

See [RELEASE_NOTES.md](RELEASE_NOTES.md) for the 3.1.0 release summary.

## License

This project is licensed under [CC0-1.0](LICENSE).
