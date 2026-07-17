# Configurable Beacons 3.1.0

Configurable Beacons 3.1.0 is a fork of the original [AdyTech99 project](https://github.com/AdyTech99/configurable-beacons), updated for the Minecraft 26.0–26.2 Fabric series.

## Highlights

- Added Minecraft 26.0–26.2 compatibility metadata and handling for the newer command-permission API.
- Made the Mod Menu title unambiguous: it now identifies singleplayer integrated-server settings separately from multiplayer local settings.
- Added server owner/operator commands to change beacon ranges, effect durations, and force-loading immediately:
  - `/configurablebeacons set radius <level> <blocks>`
  - `/configurablebeacons set duration <level> <ticks>`
  - `/configurablebeacons set force-load <true|false>`
- Added `/configurablebeacons config` to print the configuration path and `/configurablebeacons reload` to apply manually edited JSON without a server restart.
- Improved beacon-location tracking by removing unsafe background world access and saving the tracked location list when the server stops.
- Updated project metadata, documentation, and dependency guidance.

## Requirements

- Minecraft 26.0–26.2
- Fabric Loader 0.19.3+
- Java 25+
- Fabric API and YetAnotherConfigLib v3 (YACL)

Mod Menu is optional and only needed for the client configuration-screen integration.
