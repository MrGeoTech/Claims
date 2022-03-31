package com.github.mrgeotech;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimFlyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("claims.fly")) {
            if (args.length == 2) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player != null) {
                    try {
                        player.sendMessage(Claims.getColoredString("give-fly-player").replaceAll("%TIME%", args[1]));
                        player.setAllowFlight(true);
                        player.setFlying(true);

                        FlyRunnable runnable = new FlyRunnable((Integer.parseInt(args[1]) * 20 * 60L) - 1, player);

                        runnable.setId(Bukkit.getScheduler().scheduleSyncRepeatingTask(Claims.getInstance(), runnable, 1, 1));
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Claims.getInstance(), () -> player.setFlying(false), Integer.parseInt(args[1]) * 20 * 60L);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        sender.sendMessage(Claims.getColoredString("second-argument-error"));
                    }
                } else {
                    sender.sendMessage(Claims.getColoredString("could-not-find-player-error"));
                }
            } else {
                sender.sendMessage(Claims.getColoredString("incorrect-fly-claim-usage"));
            }
        } else {
            sender.sendMessage(Claims.getColoredString("player-no-permission"));
        }
        return true;
    }

}
