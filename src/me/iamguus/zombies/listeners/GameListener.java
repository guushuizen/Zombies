package me.iamguus.zombies.listeners;

import me.iamguus.zombies.Main;
import me.iamguus.zombies.classes.Arena;
import me.iamguus.zombies.classes.GameState;
import me.iamguus.zombies.utils.ArenaUtil;
import me.iamguus.zombies.utils.ChatUtil;
import me.iamguus.zombies.utils.ConfigUtil;
import me.iamguus.zombies.utils.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Score;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Guus on 28-11-2015.
 */
public class GameListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Arena arena = ArenaUtil.get().getArena(player);
        if (arena != null) {
            if (arena.getGameState() == GameState.STARTING) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onZombieDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();
            event.getDrops().clear();
            Player killer = zombie.getKiller();
            Arena arena = (killer != null) ?  ArenaUtil.get().getArena(killer) : ArenaUtil.get().getArena((Zombie) zombie);
            if (arena != null) {
                // TODO: Add coins
                arena.getZombies().remove(zombie);
                System.out.println(arena.getZombies().size());
                for (UUID uuidLoop : arena.getInGame()) {
                    ScoreboardUtil.get().updateScoreboard(Bukkit.getPlayer(uuidLoop), arena);
                }
                if (arena.getZombies().size() == 0) {
                    arena.setStartTime(ConfigUtil.get().getConfig().getInt("switch-rounds"));
                    arena.setRound(arena.getRound() + 1);
                    new BukkitRunnable() {
                        public void run() {
                            for (UUID uuidLoop : arena.getInGame()) {
                                Player playerLoop = Bukkit.getPlayer(uuidLoop);
                                playerLoop.sendMessage(ChatUtil.get().sendPrefix("announcements.round-starts-in").replaceAll("%timeleft%", arena.getStartTime() + "").replaceAll("%round%", arena.getRound() + ""));
                                playerLoop.setLevel(arena.getStartTime());
                                ScoreboardUtil.get().updateScoreboard(playerLoop, arena);
                            }
                            arena.setStartTime(arena.getStartTime() - 1);
                            if (arena.getStartTime() == 0) {
                                this.cancel();
                                for (Location loc : arena.getZombieLocs()) {
                                    ArenaUtil.get().spawnZombies(loc, arena.getRound(), arena);
                                }
                                for (UUID uuidLoop : arena.getInGame()) {
                                    Player playerLoop = Bukkit.getPlayer(uuidLoop);
                                    playerLoop.setLevel(arena.getRound());
                                    playerLoop.sendMessage(ChatUtil.get().sendPrefix("announcements.round-begin").replaceAll("%round%", arena.getRound() + ""));
                                    ScoreboardUtil.get().updateScoreboard(playerLoop, arena);
                                }
                            }
                        }
                    }.runTaskTimer(Main.getPlugin(), 0L, 20L);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Arena arena = ArenaUtil.get().getArena(player);
            if (arena != null) {
                arena.getInGame().remove(player.getUniqueId());
                arena.updateSign();
                ScoreboardUtil.get().removeScoreboard(player);
                if (arena.getInGame().size() == 0) {
                    // STOP GAME
                    for (Zombie zombie : arena.getZombies()) {
                        zombie.remove();
                    }
                    arena.setState(GameState.WAITING);
                    arena.setRound(0);
                    arena.setZombies(new ArrayList<Zombie>());
                    arena.updateSign();
                }
            }
        }
    }
}
