package com.github.mrgeotech;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.List;

public class Claims extends JavaPlugin implements Listener {

    public List<Claim> claims;

    public static Claims getInstance() {
        return (Claims) Bukkit.getPluginManager().getPlugin("Claims");
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getColoredString(String path) {
        return color(getInstance().getConfig().getString(path));
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("Claims"));
    }

    @Override
    public void onDisable() {
        ParticleHandler.removeAllShapes();
    }

    @EventHandler
    public void onJoin(PlayerSpawnLocationEvent event) {
        Claim claim = new Claim(event.getPlayer());
        claim.setWorld(event.getPlayer().getWorld());
        claim.setX1(3);
        claim.setX2(-3);
        claim.setY1(-3);
        claim.setY2(3);
        claim.complete();
        ParticleHandler.showClaim(claim);
    }

}
