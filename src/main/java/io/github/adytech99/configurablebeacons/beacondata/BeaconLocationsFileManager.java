package io.github.adytech99.configurablebeacons.beacondata;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BeaconLocationsFileManager {
    private static final String FILE_PATH = "ConfigurableBeacons_beacon_locations.txt";
    private static List<GlobalPos> mainBeaconLocationList = new ArrayList<>();

    private BeaconLocationsFileManager() {
        // Private constructor to prevent instantiation
    }

    public static void addBlockPosToWorld(World world, BlockPos blockPos) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            if(!mainBeaconLocationList.contains(GlobalPos.create(world.getRegistryKey(), blockPos))) mainBeaconLocationList.add(GlobalPos.create(world.getRegistryKey(), blockPos));

        });
        executor.shutdown();
        //saveToFile(Objects.requireNonNull(world.getServer()));
    }

    public static void removeBlockPosFromWorld(World world, BlockPos blockPos) {
        mainBeaconLocationList.removeIf(globalPos -> globalPos.pos().equals(blockPos) && globalPos.dimension() == world.getRegistryKey());
        //saveToFile(Objects.requireNonNull(world.getServer()));
    }

    public static void saveToFile(MinecraftServer server) {
        try (DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(new File(server.getSavePath(WorldSavePath.ROOT).toFile(), FILE_PATH)))) {
            for (GlobalPos globalPos : mainBeaconLocationList) {
                writeGlobalPos(outputStream, globalPos);
            }
            System.out.println("World data saved successfully.");
            loadFromFile(server);
        } catch (IOException e) {
            System.err.println("Error saving world data to file. Printing Stack Trace");
            e.printStackTrace();
            System.err.println("Error saving world data to file. Printed Stack Trace");
        }
    }

    public static void loadFromFile(MinecraftServer server) {
        try (DataInputStream inputStream = new DataInputStream(new FileInputStream(new File(server.getSavePath(WorldSavePath.ROOT).toFile(), FILE_PATH)))) {
            mainBeaconLocationList.clear();
            while (inputStream.available() > 0) {
                GlobalPos globalPos = readGlobalPos(inputStream);
                mainBeaconLocationList.add(globalPos);
            }
            System.out.println("World data loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading world data from file. Initializing empty data.");
            mainBeaconLocationList = new ArrayList<>();
        }
    }

    public static List<BlockPos> getBeaconLocationMap(World world) {
        List<BlockPos> blockPosList = new ArrayList<>();
        for (GlobalPos globalPos : mainBeaconLocationList) {
            if (globalPos.dimension() == world.getRegistryKey()) {
                blockPosList.add(globalPos.pos());
            }
        }
        return blockPosList;
    }

    public static ArrayList<GlobalPos> getMainBeaconLocationList(){
        return new ArrayList<>(mainBeaconLocationList);
    }

    public static List<BlockPos> getProcessedBeaconLocationMap(MinecraftServer server, RegistryKey<World> registryKey) {
        List<BlockPos> blockPosList = new ArrayList<>();
        for (GlobalPos globalPos : mainBeaconLocationList) {
            if (globalPos.dimension() == registryKey) {
                blockPosList.add(globalPos.pos());
            }
        }
        return blockPosList;
    }

    private static void writeGlobalPos(DataOutputStream outputStream, GlobalPos globalPos) throws IOException {
        outputStream.writeUTF(globalPos.dimension().getValue().toString());
        outputStream.writeInt(globalPos.pos().getX());
        outputStream.writeInt(globalPos.pos().getY());
        outputStream.writeInt(globalPos.pos().getZ());
    }

    private static GlobalPos readGlobalPos(DataInputStream inputStream) throws IOException {
        RegistryKey<World> dimension = RegistryKey.of(RegistryKeys.WORLD, new Identifier(inputStream.readUTF()));
        int x = inputStream.readInt();
        int y = inputStream.readInt();
        int z = inputStream.readInt();
        return GlobalPos.create(dimension, new BlockPos(x, y, z));
    }
}
