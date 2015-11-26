package me.iamguus.zombies.commands;

import me.iamguus.zombies.classes.Arena;
import me.iamguus.zombies.utils.ArenaUtil;
import me.iamguus.zombies.utils.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Guus on 26-11-2015.
 */
public class AdminCommands implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatUtil.get().sendNoPrefix("general.not-a-player"));
            return false;
        }

        Player player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("zombies")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("admin")) {
                    if (player.hasPermission("zombies.admin")) {
                        if (args.length > 1) {
                            if (args[1].equalsIgnoreCase("create")) {
                                if (args.length == 3) {
                                    String name = args[2];
                                    Arena arena = new Arena(name, 6, null, null, null);
                                    arena.save();
                                    player.sendMessage(ChatUtil.get().sendPrefix("commands.create.complete").replaceAll("%%arena%%", arena.getName()));
                                    return true;
                                } else {
                                    player.sendMessage(ChatUtil.get().sendPrefix("commands.create.syntax"));
                                    return false;
                                }
                            } else
                            if (args[1].equalsIgnoreCase("maxplayers")) {
                                if (args.length == 3) {
                                    Arena arena = ArenaUtil.get().getArena(args[1]);
                                    if (arena != null) {
                                        if (isInt(args[2])) {
                                            int newMaxPlayers = Integer.parseInt(args[2]);
                                            arena.setMaxPlayers(newMaxPlayers);
                                            player.sendMessage(ChatUtil.get().sendPrefix("commands.maxplayers.complete").replaceAll("%%name%%", arena.getName()).replaceAll("%%maxplayers%%", "" + newMaxPlayers));
                                            return true;
                                        } else {
                                            player.sendMessage(ChatUtil.get().sendNoPrefix("general.prefix") + " " + ChatColor.RED + args[2] + " is not an integer!");
                                            return false;
                                        }
                                    } else {
                                        player.sendMessage(ChatUtil.get().sendPrefix("commands.maxplayers.arena-not-found"));
                                        return false;
                                    }
                                } else {
                                    player.sendMessage(ChatUtil.get().sendPrefix("commands.maxplayers.syntax"));
                                    return false;
                                }
                            }
                        }
                    } else {
                        ChatUtil.get().sendPrefix("general.no-permission");
                    }
                }
            }
        }
        return false;
    }

    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}
