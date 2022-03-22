package com.github.mrgeotech;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.List;
import java.util.Objects;

public class Claims extends JavaPlugin implements Listener {

    public List<Claim> claims;

    public static Claims getInstance() {
        return (Claims) Bukkit.getPluginManager().getPlugin("claims");
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getColoredString(String path) {
        return color(getInstance().getConfig().getString(path));
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, Claims.getInstance());
    }

    @Override
    public void onDisable() {
        ParticleHandler.removeAllShapes();
    }

    @EventHandler
    public void onJoin(PlayerSpawnLocationEvent event) {
        ParticleHandler.createLine(0, 0, Objects.requireNonNull(event.getSpawnLocation().getWorld()));
    }

}
