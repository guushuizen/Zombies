package me.iamguus.zombies;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Guus on 25-11-2015.
 */
public class Main extends JavaPlugin {

    private static Plugin p;

    public void onEnable() {
        this.p = this;


    }

    public void onDisable() {
        this.p = null;
    }

    public static Plugin getPlugin() { return p; }
}
