package me.iamguus.zombies.data;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Guus on 25-11-2015.
 */
public class LocationUtil {

    private static LocationUtil instance;

    public String serialize(Location loc) {
        StringBuilder sb = new StringBuilder();

        sb.append("w:" + loc.getWorld().getName() + " ");
        sb.append("x:" + loc.getBlockX() + " ");
        sb.append("y:" + loc.getBlockY() + " ");
        sb.append("z:" + loc.getBlockZ());

        return sb.toString();
    }

    public Location deserialize(String s) {
        String[] parts = s.split(" ");

        String wS = parts[0].split(":")[1];
        String xS = parts[1].split(":")[1];
        String yS = parts[2].split(":")[1];
        String zS = parts[3].split(":")[1];

        World w = Bukkit.getWorld(wS);
        int x = Integer.parseInt(xS);
        int y = Integer.parseInt(yS);
        int z = Integer.parseInt(zS);

        Location loc = new Location(w, x, y, z);
        return loc;
    }

    public static LocationUtil get() {
        if (instance == null) {
            instance = new LocationUtil();
        }

        return instance;
    }
}
