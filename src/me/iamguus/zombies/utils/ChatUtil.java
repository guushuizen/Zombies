package me.iamguus.zombies.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Created by Guus on 26-11-2015.
 */
public class ChatUtil {

    private static ChatUtil instance;

    private File f;
    private FileConfiguration conf;

    public void setup(Plugin p) {
        f = new File(p.getDataFolder(), "messages.yml");
        if (!f.exists()) {
            try {
                InputStream link = p.getResource("messages.yml");
                Files.copy(link, f.getAbsoluteFile().toPath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        conf = YamlConfiguration.loadConfiguration(f);
    }

    public String sendPrefix(String path) {
        String toSend = "";
        toSend += ChatColor.translateAlternateColorCodes('&', conf.getString("general.prefix")) + " ";
        toSend += ChatColor.translateAlternateColorCodes('&', conf.getString(path));
        return toSend;
    }

    public String sendNoPrefix(String path) {
        String toSend = "";
        toSend += ChatColor.translateAlternateColorCodes('&', conf.getString(path));
        return toSend;
    }

    public static ChatUtil get() {
        if (instance == null) {
            instance = new ChatUtil();
        }

        return instance;
    }
}
