package com.github.mrgeotech;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Claims extends JavaPlugin {

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
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }



}
