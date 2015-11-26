package me.iamguus.zombies.classes;

import org.bukkit.ChatColor;

/**
 * Created by Guus on 26-11-2015.
 */
public enum GameState {

    WAITING("Waiting", ChatColor.GREEN), STARTING("Starting", ChatColor.YELLOW), INGAME("Ingame", ChatColor.RED), RESTARTING("Restarting", ChatColor.GRAY);

    private String name;
    private ChatColor color;

    private GameState(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() { return this.name; }

    public ChatColor getColor() { return this.color; }
}
