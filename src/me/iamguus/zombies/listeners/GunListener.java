package me.iamguus.zombies.listeners;

import me.iamguus.zombies.Main;
import me.iamguus.zombies.guns.Gun;
import me.iamguus.zombies.guns.GunUtil;
import me.iamguus.zombies.utils.ArenaUtil;
import me.iamguus.zombies.utils.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Guus on 1-12-2015.
 */
public class GunListener implements Listener {

    int bullets = 0;
    int magazines = 0;

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        Gun g = GunUtil.get().getGun(item);
        if (g != null) {
//                 M4A1  10 / 120
            if (item.hasItemMeta()) {
                if (!item.getItemMeta().getDisplayName().split("  ")[1].contains("Reloading") && !item.getItemMeta().getDisplayName().split("  ")[1].contains("Empty")) {
                    bullets = Integer.parseInt(item.getItemMeta().getDisplayName().split("  ")[1].split(" / ")[0]);
                    magazines = Integer.parseInt(item.getItemMeta().getDisplayName().split("  ")[1].split(" / ")[1]);
                    if (bullets > 0) {
                        ItemMeta im = item.getItemMeta();
                        bullets = bullets - 1;
                        shoot(player, g.getDamage());
                        im.setDisplayName(ChatColor.RED + g.getName() + "  " + bullets + " / " + magazines);
                        item.setItemMeta(im);
                        if (bullets == 0) {
                            magazines = magazines - g.getMagazine();
                            if (magazines != 0) {
                                bullets = g.getMagazine();
                                im.setDisplayName(ChatColor.RED + g.getName() + "  Reloading");
                                item.setItemMeta(im);
                                System.out.println(g.getReloadTime());
                                new BukkitRunnable() {
                                    public void run() {
                                        im.setDisplayName(ChatColor.RED + g.getName() + "  " + bullets + " / " + magazines);
                                        item.setItemMeta(im);
                                        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), item);
                                        player.updateInventory();
                                        System.out.println("fired");
                                    }
                                }.runTaskLater(Main.getPlugin(), 20L);
                            } else {
                                im.setDisplayName(ChatColor.RED + g.getName() + "  Empty");
                                item.setItemMeta(im);
                            }
                        }
                    }

                }
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        String[] lines = event.getLines();
        if (lines[0].contains("[Gun]")) {
            String gunName = lines[1];
            Gun g = GunUtil.get().getGun(gunName);
            if (g != null) {
                event.setLine(0, ChatColor.GRAY + "[" + ChatColor.RED + "Gun" + ChatColor.GRAY + "]");
                event.setLine(1, g.getName());
                event.setLine(2, "$" + g.getPrice());
            } else {
                event.setLine(1, "Gun not found!");
            }
        }
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().name().contains("BLOCK")) {
            if (event.getClickedBlock() != null) {
                Block block = event.getClickedBlock();
                if (block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                    Sign s = (Sign) block.getState();
                    if (s.getLine(0).contains("Gun")) {
                        if (!s.getLine(1).contains("Gun not found")) {
                            Gun g = GunUtil.get().getGun(s.getLine(1));
                            if (g != null) {
                                player.getInventory().addItem(GunUtil.get().getGunItem(g));
                                player.updateInventory();
                                HashMap<UUID, Integer> money = ArenaUtil.get().getMoneyMap();
                                if (money.containsKey(player.getUniqueId())) {
                                    if (money.get(player.getUniqueId()) >= g.getPrice()) {
                                        money.put(player.getUniqueId(), money.get(player.getUniqueId()) - g.getPrice());
                                        player.sendMessage(ChatUtil.get().sendPrefix("actions.gun-sign.bought-gun").replaceAll("%gun%", g.getName()));
                                    } else {
                                        player.sendMessage(ChatUtil.get().sendPrefix("actions.gun-sign.not-enough-money"));
                                    }
                                } else {
                                    player.sendMessage(ChatUtil.get().sendPrefix("actions.gun-sign.not-enough-money"));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHitEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();
            if (event.getDamager() instanceof Snowball) {
                Snowball r = (Snowball) event.getDamager();
                if (r.getShooter() instanceof Player) {
                    Player player = (Player) r.getShooter();
                    if (r.getCustomName().startsWith("snowball")) {
                        int damage = Integer.parseInt(r.getCustomName().split(" ")[1]);
                        event.setDamage(damage);
                    }
                }
            }
        }
    }


    private void shoot(Player player, int damage) {
        Snowball snowball = (Snowball) player.launchProjectile(Snowball.class, player.getLocation().getDirection().multiply(2));
//        snowball.setVelocity(player.getEyeLocation().getDirection().multiply(2));
        snowball.setShooter(player);
        snowball.setCustomName("snowball " + damage);
    }
}
