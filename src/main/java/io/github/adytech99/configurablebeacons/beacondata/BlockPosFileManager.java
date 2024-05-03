package io.github.adytech99.configurablebeacons.beacondata;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Deprecated
public class BlockPosFileManager {
    private static final String FILE_PATH = "configurable-beacons-blockpos_data.dat";
    private static List<BlockPos> mainBlockPosList;

    public static List<BlockPos> getBlockPosList(World world){
        return mainBlockPosList;
    }

    public static void saveBlockPosList(World world, List<BlockPos> blockPosList) {

        if(MinecraftClient.getInstance().player != null) MinecraftClient.getInstance().player.sendMessage(Text.literal("Reached the Saver."));

        MinecraftServer server = world.getServer();
        List<int[]> serializedList = new ArrayList<>();
        for (BlockPos pos : blockPosList) {
            serializedList.add(new int[]{pos.getX(), pos.getY(), pos.getZ()});
        }
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(Objects.requireNonNull(server).getSavePath(WorldSavePath.ROOT) + FILE_PATH))) {
        //try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(FabricLoader.getInstance().getGameDir().resolve((Path) WorldSavePath.ROOT) + FILE_PATH))) {
            outputStream.writeObject(serializedList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<BlockPos> loadBlockPosList(World world) {
        MinecraftServer server = world.getServer();
        List<BlockPos> blockPosList = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(Objects.requireNonNull(server).getSavePath(WorldSavePath.ROOT) + FILE_PATH))) {
            List<int[]> serializedList = (List<int[]>) inputStream.readObject();
            for (int[] data : serializedList) {
                blockPosList.add(new BlockPos(data[0], data[1], data[2]));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        mainBlockPosList = blockPosList;
        return blockPosList;
    }

    public static void deleteBlockPos(World world, BlockPos blockPosToRemove) {
        List<BlockPos> blockPosList = loadBlockPosList(world);
        blockPosList.removeIf(blockPos -> blockPos.equals(blockPosToRemove));
        saveBlockPosList(world, blockPosList);

        if(MinecraftClient.getInstance().player != null) MinecraftClient.getInstance().player.sendMessage(Text.literal("Deleter."));

    }

    public static void addBlockPos(World world, BlockPos blockPosToAdd) {
        List<BlockPos> blockPosList = loadBlockPosList(world);

        if(MinecraftClient.getInstance().player != null) MinecraftClient.getInstance().player.sendMessage(Text.literal("Attempt add."));


        if(!getBlockPosList(world).contains(blockPosToAdd)) {
            blockPosList.add(blockPosToAdd);
            saveBlockPosList(world, blockPosList);
            loadBlockPosList(world);

            if(MinecraftClient.getInstance().player != null) MinecraftClient.getInstance().player.sendMessage(Text.literal("Actually Add."));

        }
    }
}
