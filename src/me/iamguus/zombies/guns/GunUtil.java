package me.iamguus.zombies.guns;

import me.iamguus.zombies.utils.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guus on 1-12-2015.
 */
public class GunUtil {

    private static GunUtil instance;

    private List<Gun> allGuns = new ArrayList<Gun>();

    public List<Gun> getAllGuns() { return this.allGuns; }

    public void loadAllGuns() {
        for (String s : ConfigUtil.get().getGuns().getConfigurationSection("guns").getKeys(false)) {
            Gun gun = Gun.loadFromConfig(ConfigUtil.get().getGuns().getConfigurationSection("guns." + s));
            System.out.println(gun.getItem().getType());
            allGuns.add(gun);
        }
    }

    public Gun getGun(String name) {
        for (Gun gun : getAllGuns()) {
            if (gun.getName().contains(name)) {
                return gun;
            }
        }

        return null;
    }

    public Gun getGun(ItemStack item) {
        for (Gun gun : getAllGuns()) {
            if (gun.getItem().getType() == item.getType()) {
                if (item.hasItemMeta()) {
                    if (item.getItemMeta().getDisplayName().contains(gun.getName())) {
                        return gun;
                    }
                }
            }
        }

        return null;
    }

    public ItemStack getGunItem(Gun g) {
        ItemStack item = new ItemStack(g.getItem());
        ItemMeta im = item.getItemMeta();

        System.out.println("" + (item == null) + " " + g.getName() + ", " + g.getMagazine() + ", " + g.getMagazines());
        im.setDisplayName(ChatColor.RED + g.getName() + "  " + g.getMagazine() + " / " + (g.getMagazines() * g.getMagazine()));
        item.setItemMeta(im);

        return item;
    }

    public static GunUtil get() {
        if (instance == null) {
            instance = new GunUtil();
        }

        return instance;
    }
}
