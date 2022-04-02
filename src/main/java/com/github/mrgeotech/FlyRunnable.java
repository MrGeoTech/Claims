package com.github.mrgeotech;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.TimeZone;

public class FlyRunnable implements Runnable {
    private long count = 0;
    private int id;
    private long intCount;
    private final Player player;
    private final BossBar bossBar;

    public FlyRunnable(long count, Player player) {
        this.intCount = count - 1;
        this.player = player;
        Date d = new Date(((intCount - count) / 20) * 1000L);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = df.format(d);
        bossBar = Bukkit.createBossBar(Claims.getColoredString("boss-bar-text").replaceAll("%TIME_LEFT%", time), BarColor.PURPLE, BarStyle.SOLID);
        bossBar.addPlayer(player);
    }

    @Override
    public void run() {
        if (!player.isOnline()) Bukkit.getScheduler().cancelTask(id);
        count++;
        if (intCount > count) {
            if (Claims.getInstance().claims.stream().filter(claim -> claim.contains(player.getLocation())).toList().size() == 0) {
                if (player.isFlying()) {
                    player.setFlying(false);
                    player.setInvulnerable(true);
                    System.out.println(player.isInvulnerable());
                    Bukkit.getScheduler().scheduleSyncDelayedTask(Claims.getInstance(), () -> player.setInvulnerable(false), 200);
                }
                player.setAllowFlight(false);
            } else {
                player.setInvulnerable(false);
                player.setAllowFlight(true);
            }
            if (count % 20 == 0) {
                Date d = new Date(((intCount - count) / 20) * 1000L);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss"); // HH for 0-23
                df.setTimeZone(TimeZone.getTimeZone("GMT"));
                String time = df.format(d);
                bossBar.setTitle(Claims.getColoredString("boss-bar-text").replaceAll("%TIME_LEFT%", time));
            }
        } else {
            player.sendMessage(Claims.getColoredString("run-out-of-fly-time-message"));
            Bukkit.getScheduler().cancelTask(id);
            player.setFlying(false);
            player.setAllowFlight(false);
            player.setInvulnerable(true);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Claims.getInstance(), () -> {
                player.setInvulnerable(false);
            }, 200);
            bossBar.removeAll();
            Bukkit.getScheduler().scheduleSyncDelayedTask(Claims.getInstance(), () -> player.setInvulnerable(false), 200);
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addTime(long time) {
        intCount += time * 20 * 60;
    }
}
