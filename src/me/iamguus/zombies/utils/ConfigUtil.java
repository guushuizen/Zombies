package me.iamguus.zombies.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Guus on 28-11-2015.
 */
public class ConfigUtil {

    private static ConfigUtil instance;

    File configFile;
    FileConfiguration config;

    File gunsFile;
    FileConfiguration guns;

    public void setup(Plugin p) {
        this.configFile = new File(p.getDataFolder(), "config.yml");
        if (!this.configFile.exists()) {
            p.saveResource("config.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(configFile);

        this.gunsFile = new File(p.getDataFolder(), "guns.yml");
        if (!this.gunsFile.exists()) {
            try {
                gunsFile.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        this.guns = YamlConfiguration.loadConfiguration(gunsFile);
    }

    public FileConfiguration getConfig() { return this.config; }

    public File getConfigFile() { return this.configFile; }

    public void saveConfig() {
        try {
            this.config.save(this.configFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public FileConfiguration getGuns() { return this.guns; }

    public File getGunsFile() { return this.gunsFile; }

    public void saveGunsFile() {
        try {
            this.guns.save(this.gunsFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String serializeArray(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int loop : array) {
            sb.append(loop + ", ");
        }
        return sb.toString().substring(0, sb.toString().length() - 2).trim();
    }

    public int[] deserializeArray(String array) {
        String[] split = array.split(", ");
        int[] out = new int[] {};
        for (String s : split) {
            int toAdd = Integer.parseInt(s);
            out = append(out, toAdd);
        }
        return out;
    }

    private int[] append(int[] arr, int element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }

    public static ConfigUtil get() {
        if (instance == null) {
            instance = new ConfigUtil();
        }

        return instance;
    }
}
