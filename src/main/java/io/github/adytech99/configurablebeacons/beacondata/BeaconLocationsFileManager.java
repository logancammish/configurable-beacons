package io.github.adytech99.configurablebeacons.beacondata;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class BeaconLocationsFileManager {
    private static final String FILE_PATH = "ConfigurableBeacons_beacon_locations.txt";
    private static final Set<GlobalPos> mainBeaconLocationList = new LinkedHashSet<>();

    private BeaconLocationsFileManager() {
        // Private constructor to prevent instantiation
    }

    public static void addBlockPosToWorld(Level world, BlockPos blockPos) {
        mainBeaconLocationList.add(GlobalPos.of(world.dimension(), blockPos));
    }

    public static void removeBlockPosFromWorld(Level world, BlockPos blockPos) {
        mainBeaconLocationList.removeIf(globalPos -> globalPos.pos().equals(blockPos) && globalPos.dimension().equals(world.dimension()));
        //saveToFile(Objects.requireNonNull(world.getServer()));
    }

    public static void saveToFile(MinecraftServer server) {
        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(new File(server.getWorldPath(LevelResource.ROOT).toFile(), FILE_PATH)))) {
            for (GlobalPos globalPos : mainBeaconLocationList) {
                writeGlobalPos(outputStream, globalPos);
            }

        } catch (IOException e) {
            System.err.println("Error saving world data to file. Printing Stack Trace");
            e.printStackTrace();
            System.err.println("Error saving world data to file. Printed Stack Trace");
        }
    }

    public static void loadFromFile(MinecraftServer server) {
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(new File(server.getWorldPath(LevelResource.ROOT).toFile(), FILE_PATH)))) {
            mainBeaconLocationList.clear();
            while (true) {
                try {
                    mainBeaconLocationList.add(readGlobalPos(inputStream));
                } catch (EOFException ignored) {
                    return;
                }
            }
        } catch (FileNotFoundException ignored) {
            // A world without force-loaded beacons does not have a data file yet.
        } catch (IOException e) {
            System.err.println("Error loading Configurable Beacons world data; starting with an empty list.");
            e.printStackTrace();
            mainBeaconLocationList.clear();
        }
    }

    public static List<BlockPos> getBeaconLocationMap(Level world) {
        List<BlockPos> blockPosList = new ArrayList<>();
        for (GlobalPos globalPos : mainBeaconLocationList) {
            if (globalPos.dimension().equals(world.dimension())) {
                blockPosList.add(globalPos.pos());
            }
        }
        return blockPosList;
    }

    public static ArrayList<GlobalPos> getMainBeaconLocationList(){
        return new ArrayList<>(mainBeaconLocationList);
    }

    public static List<BlockPos> getProcessedBeaconLocationMap(MinecraftServer server, ResourceKey<Level> registryKey) {
        List<BlockPos> blockPosList = new ArrayList<>();
        for (GlobalPos globalPos : mainBeaconLocationList) {
            if (globalPos.dimension().equals(registryKey)) {
                blockPosList.add(globalPos.pos());
            }
        }
        return blockPosList;
    }

    private static void writeGlobalPos(DataOutputStream outputStream, GlobalPos globalPos) throws IOException {
        outputStream.writeUTF(globalPos.dimension().identifier().toString());
        outputStream.writeInt(globalPos.pos().getX());
        outputStream.writeInt(globalPos.pos().getY());
        outputStream.writeInt(globalPos.pos().getZ());
    }

    private static GlobalPos readGlobalPos(DataInputStream inputStream) throws IOException {
        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, Identifier.parse(inputStream.readUTF()));
        int x = inputStream.readInt();
        int y = inputStream.readInt();
        int z = inputStream.readInt();
        return GlobalPos.of(dimension, new BlockPos(x, y, z));
    }
}
