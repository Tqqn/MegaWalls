package dev.tqqn.megawalls.utils;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

public class WorldUtils {

    public static void asyncCopy(String world) {
        CompletableFuture.runAsync(() -> WorldUtils.copyWorld(world));
    }

    public static void copyWorld(String worldName) {
        try {
            copy(new File(Bukkit.getWorldContainer().getPath() + "/" + worldName), new File(Bukkit.getWorldContainer().getPath() + "/temp_" + worldName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteWorld(String worldName) throws IOException {
        File worldFile = new File(Bukkit.getWorldContainer().getPath() + "/" + worldName);
        File worldPlayerData = new File(Bukkit.getWorldContainer().getPath() + "/world/playerdata)");

        if (worldPlayerData.exists()) FileUtils.delete(worldPlayerData);

        if (!worldFile.exists()) return;

        FileUtils.deleteDirectory(worldFile);
    }

    private static void copy(File src, File dst) throws IOException {
        if (src.getName().equalsIgnoreCase("playerdata")) return;
        if (src.isDirectory()) {
            if (dst.exists()) {
                dst.delete();
            }

            if (!dst.exists()) {
                dst.mkdirs();
            } else if (!dst.isDirectory()) {
                throw new IllegalArgumentException("src is a directory, dst is not");
            }
            File[] sub = src.listFiles();
            for (File file : src.listFiles()) {
                copy(file, new File(dst, file.getName()));
            }
            return;
        }

        if (dst.isDirectory()) {
            throw new IllegalArgumentException("dst is a directory, src is not");
        }

        Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

}
