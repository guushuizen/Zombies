package me.iamguus.zombies.data;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guus on 25-11-2015.
 */
public class Arena {

    private String name;
    private int maxplayers;
    private Location spawnLoc;
    private List<Location> zombieLocs;
    private boolean enabled;

    public Arena(String name, int maxplayers, Location spawnLoc, List<Location> zombieLocs) {
        this.name = name;
        this.maxplayers = maxplayers;
        this.spawnLoc = spawnLoc;
        this.zombieLocs = zombieLocs;
    }

    public Arena(String name, int maxplayers, Location spawnLoc, List<Location> zombieLocs, boolean enabled) {
        this(name, maxplayers, spawnLoc, zombieLocs);
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
        pac.getFile().set("enabled", enabled);
        pac.saveFile();
    }
}
