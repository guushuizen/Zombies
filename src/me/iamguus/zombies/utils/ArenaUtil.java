package me.iamguus.zombies.utils;

import me.iamguus.zombies.Main;
import me.iamguus.zombies.classes.Arena;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guus on 26-11-2015.
 */
public class ArenaUtil {

    private static ArenaUtil instance;

    private List<Arena> allArenas = new ArrayList<Arena>();

    public List<Arena> getAllArenas() { return this.allArenas; }

    public void getPlayerArena(Player player) {

    }

    public Arena getArena(String name) {
        for (Arena loop : allArenas) {
            if (loop.getName().equalsIgnoreCase(name)) {
                return loop;
            }
        }

        return null;
    }

    public void loadArena(Arena arena) { allArenas.add(arena); }

    public void loadAllArenas() {
        File arenaDir = new File(Main.getPlugin().getDataFolder(), "arenas" + File.separator);
        if (arenaDir.exists()) {
            if (arenaDir.listFiles().length > 0) {
                for (File loop : arenaDir.listFiles()) {
                    loadArena(Arena.loadArenaFromConfig(loop));
                }
            }
        }
    }

    public static ArenaUtil get() {
        if (instance == null) {
            instance = new ArenaUtil();
        }

        return instance;
    }
}
