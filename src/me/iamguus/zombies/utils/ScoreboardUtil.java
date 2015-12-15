package me.iamguus.zombies.utils;

import me.iamguus.zombies.classes.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.UUID;

/**
 * Created by Guus on 4-12-2015.
 */
public class ScoreboardUtil {

    private static ScoreboardUtil instance;

    public void setScoreboard(Arena arena, Player player) {
        Scoreboard scoreboard = player.getScoreboard();

        Objective obj = (scoreboard.getObjective(ChatUtil.get().sendNoPrefix("scoreboard.title")) == null) ? scoreboard.registerNewObjective(ChatUtil.get().sendNoPrefix("scoreboard.title"), "dummy") : scoreboard.getObjective(ChatUtil.get().sendNoPrefix("scoreboard.title"));

        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        int score = 1;

        Score round = obj.getScore(ChatUtil.get().sendNoPrefix("scoreboard.round").replaceAll("%round%", arena.getRound() + ""));
        round.setScore(score);
        score++;

        Score space1 = obj.getScore(" ");
        space1.setScore(score);
        score++;

        Score sep1 = obj.getScore(ChatUtil.get().sendNoPrefix("scoreboard.separator"));
        sep1.setScore(score);
        score++;

        Score space2 = obj.getScore("  ");
        space2.setScore(score);
        score++;

        if (arena.getStartTime() != 0) {
            Score timeRemaining = obj.getScore(ChatUtil.get().sendNoPrefix("scoreboard.timeleft").replaceAll("%timeleft%", arena.getStartTime() + ""));
            timeRemaining.setScore(score);
            score++;
        } else {
            Score zombiesLeft = obj.getScore(ChatUtil.get().sendNoPrefix("scoreboard.zombies").replaceAll("%zombies%", arena.getZombies().size() + ""));
            zombiesLeft.setScore(score);
            score++;
        }

        Score space3 = obj.getScore("   ");
        space3.setScore(score);
        score++;

        Score sep2 = obj.getScore(ChatUtil.get().sendNoPrefix("scoreboard.separator") + " ");
        sep2.setScore(score);
        score++;

        Score space4 = obj.getScore("    ");
        space4.setScore(score);
        score++;

        Score money = obj.getScore(ChatUtil.get().sendNoPrefix("scoreboard.money").replaceAll("%money%", ((ArenaUtil.get().getMoneyMap().get(player.getUniqueId()) != null) ? ArenaUtil.get().getMoneyMap().get(player.getUniqueId()) : 0) + ""));
        money.setScore(score);

        player.setScoreboard(scoreboard);
    }

    public void removeScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public void updateScoreboard(Player player, Arena arena) {
        removeScoreboard(player);
        setScoreboard(arena, player);
    }

    public static ScoreboardUtil get() {
        if (instance == null) {
            instance = new ScoreboardUtil();
        }

        return instance;
    }
}
