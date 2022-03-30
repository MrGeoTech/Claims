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
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        Bukkit.getScheduler().scheduleSyncRepeatingTask(Claims.getInstance(), new Runnable() {
                            private int count = 0;
                            private boolean outside = false;

                            @Override
                            public void run() {
                                System.out.println(count);
                                count++;
                                if (outside) {
                                    if (Claims.getInstance().claims.stream().filter(claim -> claim.contains(player.getLocation())).toList().size() != 0)
                                        outside = false;
                                } else {
                                    if (count == Long.parseLong(args[1]) * 20 * 60)
                                        player.setFlying(false);
                                    else if (Claims.getInstance().claims.stream().filter(claim -> claim.contains(player.getLocation())).toList().size() == 0) {
                                        player.setFlying(false);
                                        player.setInvisible(true);
                                        outside = true;
                                        Bukkit.getScheduler().scheduleSyncDelayedTask(Claims.getInstance(), () -> player.setInvisible(false), 200);
                                    }
                                }
                            }
                        }, 1, Integer.parseInt(args[1]) * 20 * 60L);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        sender.sendMessage(ChatColor.RED + "Second argument must be a number!");
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Could not find player!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /flyclaim <user> <minutes>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Sorry but you do not have permission to execute this command!");
        }
        return true;
    }

}
