package me.iamguus.zombies.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Guus on 1-12-2015.
 */
public class ItemStackUtil {

    private static ItemStackUtil instance;

    public String serialize(ItemStack item) {
        StringBuilder sb = new StringBuilder();

        sb.append("id:" + item.getTypeId() + " ");

        if (item.getAmount() > 1) {
            sb.append("amount:" + item.getAmount() + " ");
        }

        if (item.getDurability() != 0) {
            sb.append("data:" + item.getDurability() + " ");
        }

        if (item.hasItemMeta()) {
            sb.append("name:" + item.getItemMeta().getDisplayName() + " ");
            if (!item.getItemMeta().getLore().isEmpty()) {
                // bla,bla,bla
                String lore = "";
                for (String s : item.getItemMeta().getLore()) {
                    lore += s + ",";
                }
                sb.append("lore:" + lore.substring(0, lore.length() - 1) + " ");
            }
        }
        return sb.toString().trim();
    }

    public ItemStack deserialize(String s) {
        String[] splitted = s.split(" ");
        ItemStack item;

        String id = "";
        String amount = "";
        String data = "";
        String name = "";
        String lore = "";

        System.out.println("check-1");
        for (String loop : splitted) {
            System.out.println("check0, " + loop);
            if (loop.startsWith("id")) { id = loop; System.out.println("check1"); }
            if (loop.startsWith("amount")) { amount = loop; }
            if (loop.startsWith("data")) { data = loop; }
            if (loop.startsWith("name")) { name = loop; }
            if (loop.startsWith("lore")) { lore = loop; }
        }

        int idInt = 0;
        int amountInt = 1;
        short dataInt = 0;
        String nameOut = "";
        String loreOut = "";

        System.out.println("before check2, " + id);
        if (!id.equalsIgnoreCase("")) {
            System.out.println("check2");
            idInt = Integer.parseInt(id.split(":")[1]);
        }

        if (!amount.equals("")) {
            amountInt = Integer.parseInt(amount.split(":")[1]);
        }

        if (!data.equals("")) {
            dataInt = Short.parseShort(data.split(":")[1]);
        }

        if (!name.equals("")) {
            nameOut = name.split(":")[1];
        }

        if (!lore.equals("")) {
            loreOut = lore.split(":")[1];
        }

        item = new ItemStack(Material.getMaterial(idInt), amountInt);
        ItemMeta im = item.getItemMeta();
        if (dataInt != 0) {
            item.setDurability(dataInt);
        }
        if (!nameOut.equals("")) {
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', nameOut));
        }
        if (!loreOut.equals("")) {
            List<String> toLore = new ArrayList<String>();
            for (String loop : lore.split(",")) {
                toLore.add(ChatColor.translateAlternateColorCodes('&', loop));
            }
            im.setLore(toLore);
        }
        item.setItemMeta(im);

        return item;
    }

    public static ItemStackUtil get() {
        if (instance == null) {
            instance = new ItemStackUtil();
        }
        return instance;
    }
}
