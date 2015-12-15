package me.iamguus.zombies.guns;

import me.iamguus.zombies.utils.ConfigUtil;
import me.iamguus.zombies.utils.ItemStackUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Guus on 1-12-2015.
 */
public class Gun {

    private String name;
    private ItemStack item;
    private int magazine;
    private int magazines;
    private int reloadTime;
    private int price;
    private boolean standard;
    private int damage;

    public Gun(String name, ItemStack item, int magazine, int magazines, int reloadTime, int price, boolean standard, int damage) {
        this.name = name;
        this.item = item;
        this.magazine = magazine;
        this.magazines = magazines;
        this.reloadTime = reloadTime;
        this.price = price;
        this.standard = standard;
        this.damage = damage;
    }

    public String getName() { return this.name; }

    public ItemStack getItem() { return this.item; }

    public void setItem(ItemStack item) { this.item = item; }

    public int getMagazines() { return this.magazines; }

    public void setMagazines(int magazines) { this.magazines = magazines; }

    public int getMagazine() { return this.magazine; }

    public void setMagazine(int magazine) { this.magazine = magazine; }

    public int getReloadTime() { return this.reloadTime; }

    public void setReloadTime(int reloadTime) { this.reloadTime = reloadTime; }

    public int getPrice() { return this.price; }

    public void setPrice(int price) { this.price = price; }

    public boolean getStandard() { return this.standard; }

    public void setStandard(boolean standard) { this.standard = standard; }

    public int getDamage() { return this.damage; }

    public void save(FileConfiguration config) {
        config.set("guns." + this.name + ".name", this.name);
        config.set("guns." + this.name + ".item", ItemStackUtil.get().serialize(this.item));
        config.set("guns." + this.name + ".magazine", this.magazine);
        config.set("guns." + this.name + ".magazines", this.magazines);
        config.set("guns." + this.name + ".reloadtime", this.reloadTime);
        config.set("guns." + this.name + ".price", this.price);
        config.set("guns." + this.name + ".standard", this.standard);
        config.set("guns." + this.name + ".damage", this.damage);
        ConfigUtil.get().saveGunsFile();
    }

    public static Gun loadFromConfig(ConfigurationSection section) {
        String name = section.getString("name");
        ItemStack item = ItemStackUtil.get().deserialize(section.getString("item"));
        int magazine = section.getInt("magazine");
        int magazines = section.getInt("magazines");
        int reloadTime = section.getInt("reloadtime");
        int price = section.getInt("price");
        boolean standard = section.getBoolean("standard");
        int damage = section.getInt("damage");

        return new Gun(name, item, magazine, magazines, reloadTime, price, standard, damage);
    }
}
