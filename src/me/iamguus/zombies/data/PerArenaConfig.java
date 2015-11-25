package me.iamguus.zombies.data;

import me.iamguus.zombies.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Guus on 25-11-2015.
 */
public class PerArenaConfig {

    File f;
    FileConfiguration conf;

    public PerArenaConfig(Arena arena) {
        this.f = new File(Main.getPlugin().getDataFolder(), "arenas" + File.separator + arena.getName() + ".yml");
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        conf = YamlConfiguration.loadConfiguration(f);
    }

    public FileConfiguration getFile() { return this.conf; }

    public void saveFile() {
        try {
            conf.save(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
