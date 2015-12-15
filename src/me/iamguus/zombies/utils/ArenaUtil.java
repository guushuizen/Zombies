package me.iamguus.zombies.utils;

import me.iamguus.zombies.Main;
import me.iamguus.zombies.classes.Arena;
import me.iamguus.zombies.classes.GameState;
import me.iamguus.zombies.guns.Gun;
import me.iamguus.zombies.guns.GunUtil;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.io.File;
import java.util.*;

/**
 * Created by Guus on 26-11-2015.
 */
public class ArenaUtil {

    private static ArenaUtil instance;

//    private List<UUID> starting = new ArrayList<UUID>();

    private HashMap<UUID, Integer> money = new HashMap<UUID, Integer>();

    private List<Arena> allArenas = new ArrayList<Arena>();

    public List<Arena> getAllArenas() { return this.allArenas; }

    public Arena getArena(String name) {
        for (Arena loop : allArenas) {
            if (loop.getName().equalsIgnoreCase(name)) {
                return loop;
            }
        }

        return null;
    }

    public Arena getArena(Sign s) {
        Location sLoc = s.getLocation();
        for (Arena loop : allArenas) {
            Location loopLoc = loop.getSign().getLocation();
            if (sLoc.getBlockX() == loopLoc.getBlockX() && sLoc.getBlockY() == loopLoc.getBlockY() && sLoc.getBlockZ() == loopLoc.getBlockZ()) {
                return loop;
            }
        }
        return null;
    }

    public Arena getArena(Player player) {
        for (Arena loop : allArenas) {
            if (loop.getInGame().contains(player.getUniqueId())) {
                return loop;
            }
        }

        return null;
    }

    public Arena getArena(Zombie zombie) {
        for (Arena loop : allArenas) {
            if (loop.getZombies().contains(zombie)) {
                return loop;
            }
        }

        return null;
    }

    public void joinArena(Player player, Arena arena) {
        if (arena.getGameState() == GameState.WAITING) {
            if (arena.getInGame().size() < arena.getMaxPlayers()) {
                if (ArenaUtil.get().getArena(player) == null) {
                    for (UUID uuidLoop : arena.getInGame()) {
                        Bukkit.getPlayer(uuidLoop).sendMessage(ChatUtil.get().sendPrefix("announcements.player-join").replaceAll("%player%", player.getName()).replaceAll("%currentplayers", arena.getInGame().size() + "").replaceAll("%maxplayers%", arena.getMaxPlayers() + ""));
                    }
                    player.sendMessage(ChatUtil.get().sendPrefix("announcements.joined-arena").replaceAll("%arena%", arena.getName()));
                    ScoreboardUtil.get().setScoreboard(arena, player);
                    // Join
                    arena.getInGame().add(player.getUniqueId());
                    player.teleport(arena.getLobbyLoc());
                    arena.updateSign();
                    //Checks
                    if (arena.getInGame().size() == ConfigUtil.get().getConfig().getInt("start-players")) {
                        arena.setStartTime(ConfigUtil.get().getConfig().getInt("count-down-start"));
                        int[] announcements = ConfigUtil.get().deserializeArray(ConfigUtil.get().getConfig().getString("announcement-time"));
                        new BukkitRunnable() {
                            public void run() {
                                if (arena.getStartTime() > 0) {
                                    arena.setStartTime(arena.getStartTime() - 1);
                                    for (int loop : announcements) {
                                        for (UUID uuidLoop : arena.getInGame()) {
                                            Player loopPlayer = Bukkit.getPlayer(uuidLoop);
                                            ScoreboardUtil.get().updateScoreboard(player, arena);
                                            if (loop == arena.getStartTime()) {
                                                loopPlayer.sendMessage(ChatUtil.get().sendPrefix("announcements.game-starting-in").replaceAll("%timeleft%", arena.getStartTime() + ""));
                                            }
                                            if (ConfigUtil.get().getConfig().getBoolean("xp-bar")) {
                                                loopPlayer.setLevel(arena.getStartTime());
                                            }
                                        }

                                    }
                                }
                                if (arena.getStartTime() == 0) {
                                    startGame(arena);
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Main.getPlugin(), 20L, 20L);
                    }

                } else {
                    player.sendMessage(ChatUtil.get().sendPrefix("actions.join-sign.already-in-arena"));
                }
            } else {
                player.sendMessage(ChatUtil.get().sendPrefix("actions.join-sign.arena-full"));
            }
        }
    }

    public void startGame(Arena arena) {
        for (UUID uuidLoop : arena.getInGame()) {
            Player loop = Bukkit.getPlayer(uuidLoop);
            loop.teleport(arena.getSpawnLoc());
            loop.sendMessage(ChatUtil.get().sendPrefix("announcements.round-starts-in").replaceAll("%timeleft%", 10 + "").replaceAll("%round%", arena.getName()));
            loop.setLevel(10);
            ScoreboardUtil.get().updateScoreboard(loop, arena);
        }
        arena.setStartTime(ConfigUtil.get().getConfig().getInt("switch-rounds"));
        arena.setState(GameState.STARTING);
        arena.setRound(1);

        new BukkitRunnable() {
            public void run() {
                if (arena.getStartTime() > 0) {
                    arena.setStartTime(arena.getStartTime() - 1);
                    for (UUID uuidLoop : arena.getInGame()) {
                        Player loop = Bukkit.getPlayer(uuidLoop);
                        loop.sendMessage(ChatUtil.get().sendPrefix("announcements.round-starts-in").replaceAll("%timeleft%", arena.getStartTime() + "").replaceAll("%round%", arena.getRound() + ""));
                        loop.setLevel(arena.getStartTime());
                        ScoreboardUtil.get().updateScoreboard(loop, arena);
                    }
                    if (arena.getStartTime() == 0) {
                        arena.setState(GameState.INGAME);
                        this.cancel();
                        for (Location loc : arena.getZombieLocs()) {
                            spawnZombies(loc, arena.getRound(), arena);

                        }
                        for (UUID uuidLoop : arena.getInGame()) {
                            Player playerLoop = Bukkit.getPlayer(uuidLoop);
                            playerLoop.setLevel(arena.getRound());
                            playerLoop.sendMessage(ChatUtil.get().sendPrefix("announcements.round-begin").replaceAll("%round%", arena.getRound() + ""));
                            ScoreboardUtil.get().updateScoreboard(playerLoop, arena);
                            for (Gun g : GunUtil.get().getAllGuns()) {
                                if (g.getStandard()) {
                                    playerLoop.getInventory().addItem(GunUtil.get().getGunItem(g));
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(), 20L, 20L);
    }

    public void spawnZombies(Location loc, int amount, Arena arena) {
        for (int i = 0; i < amount; i++) {
            Zombie zombie = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
            if (Math.random() > 0.99) {
                zombie.setCustomName(ChatColor.RED + "Nazi Zombie");
                setLeatherArmor(zombie);
                zombie.setMaxHealth(40D);
                zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            }
            arena.getZombies().add(zombie);
        }
    }

    private void setLeatherArmor(Zombie zombie) {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(Color.GRAY);
        helmet.setItemMeta(helmetMeta);

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestMeta = (LeatherArmorMeta) chestplate.getItemMeta();
        chestMeta.setColor(Color.GRAY);
        chestplate.setItemMeta(chestMeta);

        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
        leggingsMeta.setColor(Color.GRAY);
        leggings.setItemMeta(leggingsMeta);

        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(Color.GRAY);
        boots.setItemMeta(bootsMeta);

        zombie.getEquipment().setHelmet(helmet);
        zombie.getEquipment().setChestplate(chestplate);
        zombie.getEquipment().setLeggings(leggings);
        zombie.getEquipment().setBoots(boots);
    }

    public void loadArena(Arena arena) {
        allArenas.add(arena);
        arena.setState(GameState.WAITING);
    }

    public void loadAllArenas() {
        File arenaDir = new File(Main.getPlugin().getDataFolder(), "arenas" + File.separator);
        if (arenaDir.exists()) {
            if (arenaDir.listFiles().length > 0) {
                for (File loop : arenaDir.listFiles()) {
                    loadArena(Arena.loadArenaFromConfig(loop));
                }
            }
        }
    }

    public HashMap<UUID, Integer> getMoneyMap() { return this.money; }

    public static ArenaUtil get() {
        if (instance == null) {
            instance = new ArenaUtil();
        }

        return instance;
    }
}
