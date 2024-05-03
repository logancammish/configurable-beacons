package io.github.adytech99.configurablebeacons.beacondata;

import java.io.*;

public class WorldBlockSerializer {
    public static void serializeToFile(WorldBlockData data, String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        }
    }

    public static WorldBlockData deserializeFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (WorldBlockData) ois.readObject();
        }
    }
}
