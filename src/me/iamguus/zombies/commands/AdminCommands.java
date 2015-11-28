package me.iamguus.zombies.commands;

import me.iamguus.zombies.classes.Arena;
import me.iamguus.zombies.utils.ArenaUtil;
import me.iamguus.zombies.utils.ChatUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

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
                                    ArenaUtil.get().loadArena(arena);
                                    player.sendMessage(ChatUtil.get().sendPrefix("commands.create.complete").replaceAll("%arena%", arena.getName()));
                                    return true;
                                } else {
                                    player.sendMessage(ChatUtil.get().sendPrefix("commands.create.syntax"));
                                    return false;
                                }
                            } else
                            // zombies admin maxplayers Dangerous 4
                            if (args[1].equalsIgnoreCase("maxplayers")) {
                                if (args.length == 4) {
                                    Arena arena = ArenaUtil.get().getArena(args[2]);
                                    if (arena != null) {
                                        if (isInt(args[3])) {
                                            int newMaxPlayers = Integer.parseInt(args[3]);
                                            arena.setMaxPlayers(newMaxPlayers);
                                            player.sendMessage(ChatUtil.get().sendPrefix("commands.maxplayers.complete").replaceAll("%arena%", arena.getName()).replaceAll("%maxplayers%", "" + newMaxPlayers));
                                            return true;
                                        } else {
                                            player.sendMessage(ChatUtil.get().sendNoPrefix("general.prefix") + " " + ChatColor.RED + args[3] + " is not an integer!");
                                            return false;
                                        }
                                    } else {
                                        player.sendMessage(ChatUtil.get().sendNoPrefix("general.prefix") + ChatColor.RED + " The arena " + args[2] + " was not found!");
                                        return false;
                                    }
                                } else {
                                    player.sendMessage(ChatUtil.get().sendPrefix("commands.maxplayers.syntax"));
                                    return false;
                                }
                            } else
                            if (args[1].equalsIgnoreCase("sign")) {
                                if (args.length == 3) {
                                    Arena arena = ArenaUtil.get().getArena(args[2]);
                                    if (arena != null) {
                                        Block targetBlock = player.getTargetBlock((Set<Material>) null, 8);
                                        if (targetBlock.getType() == Material.SIGN || targetBlock.getType() == Material.SIGN_POST || targetBlock.getType() == Material.WALL_SIGN) {
                                            Sign s = (Sign) targetBlock.getState();
                                            if (arena.getEnabled()) {
                                                arena.setSign(s);
                                                arena.updateSign();
                                                arena.save();
                                                player.sendMessage(ChatUtil.get().sendPrefix("commands.sign.complete"));
                                                return true;
                                            } else {
                                                player.sendMessage(ChatUtil.get().sendNoPrefix("general.prefix") + ChatColor.RED + " The arena is not enabled and so no sign can be added.");
                                                return false;
                                            }
                                        } else {
                                            player.sendMessage(ChatUtil.get().sendNoPrefix("general.prefix") + ChatColor.RED + " The block you are looking at is not a valid sign!");
                                            return false;
                                        }
                                    } else {
                                        player.sendMessage(ChatUtil.get().sendNoPrefix("general.prefix") + ChatColor.RED + " The arena " + args[2] + " was not found!");
                                        return false;
                                    }
                                } else {
                                    player.sendMessage(ChatUtil.get().sendPrefix("commands.sign.syntax"));
                                    return false;
                                }
                            } else
                            if (args[1].equalsIgnoreCase("spawn")) {
                                if (args.length == 3) {
                                    Arena arena = ArenaUtil.get().getArena(args[2]);
                                    if (arena != null) {
                                        arena.setSpawnLoc(player.getLocation());
                                        arena.save();
                                        player.sendMessage(ChatUtil.get().sendPrefix("commands.spawn.complete").replaceAll("%name%", arena.getName()));
                                        return true;
                                    } else {
                                        player.sendMessage(ChatUtil.get().sendNoPrefix("general.prefix") + ChatColor.RED + " Arena " + args[2] + " was not found!");
                                        return false;
                                    }
                                } else {
                                    player.sendMessage(ChatUtil.get().sendPrefix("commands.spawn.syntax"));
                                    return false;
                                }
                            } else
                            if (args[1].equalsIgnoreCase("zombiespawns")) {
                                // zombies admin zombiespawns (arena) (add/remove/set) [id]
                                if (args.length == 4 || args.length == 5) {
                                    Arena arena = ArenaUtil.get().getArena(args[2]);
                                    if (arena != null) {
                                        if (args[3].equalsIgnoreCase("add")) {
                                            if (args.length == 4) {
                                                List<Location> zombieSpawns = arena.getZombieLocs();
                                                zombieSpawns.add(player.getLocation());
                                                arena.setZombieLocs(zombieSpawns);
                                                arena.save();
                                                player.sendMessage(ChatUtil.get().sendPrefix("commands.zombiespawns.add-complete").replaceAll("%arena%", arena.getName()).replaceAll("%id%", zombieSpawns.indexOf(player.getLocation()) + ""));
                                                return true;
                                            } else {
                                                player.sendMessage(ChatUtil.get().sendPrefix("commands.zombiespawns.syntax"));
                                                return false;
                                            }
                                        } else
                                        if (args[3].equalsIgnoreCase("remove")) {
                                            if (args.length == 5) {
                                                if (isInt(args[4])) {
                                                    int ID = Integer.parseInt(args[4]);
                                                    List<Location> zombieSpawns = arena.getZombieLocs();
                                                    if (zombieSpawns.get(ID) != null) {
                                                        zombieSpawns.remove(ID);
                                                        arena.setZombieLocs(zombieSpawns);
                                                        arena.save();
                                                        player.sendMessage(ChatUtil.get().sendPrefix("commands.zombiespawns.remove-complete").replaceAll("%arena%", arena.getName()).replaceAll("%id%", ID + ""));
                                                        return true;
                                                    } else {
                                                        player.sendMessage(ChatUtil.get().sendNoPrefix("general.prefix") + " The location at index " + ID + " could not be found.");
                                                        return false;
                                                    }
                                                } else {
                                                    player.sendMessage(ChatUtil.get().sendNoPrefix("general.prefix") + ChatColor.RED + " The integer " + args[4] + " is not a correct integer!");
                                                    return false;
                                                }
                                            } else {
                                                player.sendMessage(ChatUtil.get().sendPrefix("commands.zombiespawns.syntax"));
                                                return false;
                                            }
                                        } else
                                        if (args[3].equalsIgnoreCase("set")) {
                                            if (args.length == 5) {
                                                if (isInt(args[4])) {
                                                    int ID = Integer.parseInt(args[4]);
                                                    List<Location> zombieSpawns = arena.getZombieLocs();
                                                    if (zombieSpawns.get(ID) != null) {
                                                        zombieSpawns.set(ID, player.getLocation());
                                                        arena.setZombieLocs(zombieSpawns);
                                                        arena.save();
                                                        player.sendMessage(ChatUtil.get().sendPrefix("commands.zombiespawns.set-complete").replaceAll("%arena%", arena.getName()).replaceAll("%id%", ID + ""));
                                                        return true;
                                                    } else {
                                                        player.sendMessage(ChatUtil.get().sendNoPrefix("general.prefix") + " The location at index " + ID + " was not found and therefore could not be replaced.");
                                                        return false;
                                                    }
                                                } else {
                                                    player.sendMessage(ChatUtil.get().sendNoPrefix("general.prefix") + ChatColor.RED + " The integer " + args[4] + " is not a correct integer!");
                                                    return false;
                                                }
                                            } else {
                                                player.sendMessage(ChatUtil.get().sendPrefix("commands.zombiespawns.syntax"));
                                            }
                                        }
                                    } else {
                                        player.sendMessage(ChatUtil.get().sendNoPrefix("general.prefix") + ChatColor.RED + " Arena " + args[2] + " was not found!");
                                        return false;
                                    }
                                } else {
                                    player.sendMessage(ChatUtil.get().sendPrefix("commands.zombiespawns.syntax"));
                                    return false;
                                }
                            }
                            else {
                                //TODO: onCommand for PlayerCommands
                                return false;
                            }
                        }
                    } else {
                        player.sendMessage(ChatUtil.get().sendPrefix("general.no-permission"));
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                player.sendMessage("Coming soon");
                return false;
            }
            return false;
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
