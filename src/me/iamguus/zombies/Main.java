package me.iamguus.zombies;

import me.iamguus.zombies.commands.AdminCommands;
import me.iamguus.zombies.guns.Gun;
import me.iamguus.zombies.guns.GunUtil;
import me.iamguus.zombies.listeners.GameListener;
import me.iamguus.zombies.listeners.GunListener;
import me.iamguus.zombies.listeners.SignListener;
import me.iamguus.zombies.utils.ArenaUtil;
import me.iamguus.zombies.utils.ChatUtil;
import me.iamguus.zombies.utils.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * Created by Guus on 25-11-2015.
 */
public class Main extends JavaPlugin {

    private static Plugin p;

    public void onEnable() {
        this.p = this;

        ChatUtil.get().setup(p);

        ConfigUtil.get().setup(p);

        Gun gun = new Gun("M4A1", new ItemStack(Material.STICK), 12, 10, 1, 100, true, 3);

        gun.save(ConfigUtil.get().getGuns());

        GunUtil.get().loadAllGuns();

        this.getCommand("zombies").setExecutor(new AdminCommands());

        registerListeners(p, new SignListener(), new GameListener(), new GunListener());

        ArenaUtil.get().loadAllArenas();
    }

    public void onDisable() {
        this.p = null;
    }

    public static Plugin getPlugin() { return p; }

    public void registerListeners(Plugin p, Listener... listeners) {
        for (Listener list : listeners) {
            Bukkit.getPluginManager().registerEvents(list, p);
        }
    }
}
