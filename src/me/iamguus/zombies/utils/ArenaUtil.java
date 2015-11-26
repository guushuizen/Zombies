package me.iamguus.zombies.utils;

import me.iamguus.zombies.classes.Arena;
import org.bukkit.entity.Player;

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

    public static ArenaUtil get() {
        if (instance == null) {
            instance = new ArenaUtil();
        }

        return instance;
    }
}
