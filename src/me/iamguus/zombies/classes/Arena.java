package me.iamguus.zombies.classes;

import me.iamguus.zombies.utils.LocationUtil;
import me.iamguus.zombies.utils.PerArenaConfig;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Guus on 25-11-2015.
 */
public class Arena {

    private String name;
    private int maxplayers;
    private Location spawnLoc;
    private List<Location> zombieLocs;
    private boolean enabled;
    private Sign sign;
    private GameState state;
    private List<UUID> ingame;

    public Arena(String name, int maxplayers, Location spawnLoc, List<Location> zombieLocs, Sign sign) {
        this.name = name;
        this.maxplayers = maxplayers;
        this.spawnLoc = spawnLoc;
        this.zombieLocs = zombieLocs;
        this.sign = sign;
        this.ingame = new ArrayList<UUID>();
    }

    public Arena(String name, int maxplayers, Location spawnLoc, List<Location> zombieLocs, Sign sign, boolean enabled) {
        this(name, maxplayers, spawnLoc, zombieLocs, sign);
        this.enabled = enabled;
    }

    public String getName() { return this.name; }

    public int getMaxPlayers() { return this.maxplayers; }

    public void setMaxPlayers(int maxplayers) { this.maxplayers = maxplayers; }

    public Location getSpawnLoc() { return this.spawnLoc; }

    public void setSpawnLoc(Location spawnLoc) { this.spawnLoc = spawnLoc; }

    public List<Location> getZombieLocs() { return this.zombieLocs; }

    public void setZombieLocs(List<Location> zombieLocs) { this.zombieLocs = zombieLocs; }

    public boolean getEnabled() { return this.enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public Sign getSign() { return this.sign; }

    public void setSign(Sign sign) { this.sign = sign; }

    public GameState getGameState() { return this.state; }

    public void setState(GameState state) { this.state = state; }

    public void updateSign() {
        if (sign != null) {
            if (sign.getBlock().getState() instanceof Sign) {
                this.sign.setLine(0, "" + ChatColor.GREEN + ChatColor.BOLD + "[JOIN]");
                this.sign.setLine(1, "COMZ");
                this.sign.setLine(2, ChatColor.BLUE + "(" + ChatColor.RESET + ChatColor.BOLD + this.ingame.size() + "/" + this.maxplayers + ChatColor.BLUE + ")");
                this.sign.setLine(3, this.name);
                sign.update();
            }
        }
    }

    public void save() {
        PerArenaConfig pac = new PerArenaConfig(this);
        pac.getFile().set("name", this.name);
        pac.getFile().set("maxplayers", this.maxplayers);
        pac.getFile().set("spawnloc", LocationUtil.get().serialize(spawnLoc));
        List<String> toSave = new ArrayList<String>();
        for (Location loc : zombieLocs) {
            toSave.add(LocationUtil.get().serialize(loc));
        }
        pac.getFile().set("zombielocs", toSave);
        pac.getFile().set("sign", LocationUtil.get().serialize(this.sign.getLocation()));
        pac.getFile().set("enabled", enabled);
        pac.saveFile();
    }

    public static Arena loadArenaFromConfig(File file) {
        if (file.exists() || file.length() != 0F) {
            FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
            String name = conf.getString("name");
            int maxplayers = conf.getInt("maxplayers");
            Location spawnLoc = LocationUtil.get().deserialize(conf.getString("spawnloc"));
            List<Location> zombieLocs = new ArrayList<Location>();
            for (String s : conf.getStringList("zombielocs")) {
                zombieLocs.add(LocationUtil.get().deserialize(s));
            }
            Sign sign = (Sign) LocationUtil.get().deserialize(conf.getString("sign")).getBlock().getState();
            boolean enabled = conf.getBoolean("enabled");
            Arena out = new Arena(name, maxplayers, spawnLoc, zombieLocs, sign, enabled);
            return out;
        }
        return null;
    }
}
