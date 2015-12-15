package me.iamguus.zombies.listeners;

import me.iamguus.zombies.classes.Arena;
import me.iamguus.zombies.utils.ArenaUtil;
import me.iamguus.zombies.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Guus on 28-11-2015.
 */
public class SignListener implements Listener {

    @EventHandler
    public void onArenaJoin(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null) {
                Block clickedBlock = event.getClickedBlock();
                if (clickedBlock.getType() == Material.WALL_SIGN || clickedBlock.getType() == Material.SIGN_POST || clickedBlock.getType() == Material.SIGN_POST) {
                    Sign s = (Sign) event.getClickedBlock().getState();
                    Arena arena = ArenaUtil.get().getArena(s);
                    if (arena != null) {
                        ArenaUtil.get().joinArena(player, arena);
                    }
                }
            }
        }
    }
}
