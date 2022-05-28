package com.github.mrgeotech;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ClaimFlyCommand implements CommandExecutor {

    private final Map<Player, FlyRunnable> players = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("claims.fly")) {
            if (args.length == 2) {
                Player player = Bukkit.getPlayer(args[0]);
                if (player != null) {
                    if (players.containsKey(player)) {
                        try {
                            player.sendMessage(Claims.getColoredString("extend-fly-player").replaceAll("%TIME%", args[1]));
                            player.setAllowFlight(true);

                            players.get(player).addTime(Long.parseLong(args[1]));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            sender.sendMessage(Claims.getColoredString("second-argument-error"));
                        }
                    } else {
                        try {
                            player.sendMessage(Claims.getColoredString("give-fly-player").replaceAll("%TIME%", args[1]));
                            player.setAllowFlight(true);
                            player.setFlying(true);

                            FlyRunnable runnable = new FlyRunnable((Integer.parseInt(args[1]) * 20 * 60L), player);

                            runnable.setId(Bukkit.getScheduler().scheduleSyncRepeatingTask(Claims.getInstance(), runnable, 1, 1));

                            players.put(player, runnable);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            sender.sendMessage(Claims.getColoredString("second-argument-error"));
                        }
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
