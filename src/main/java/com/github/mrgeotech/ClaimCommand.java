package com.github.mrgeotech;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            if (Claims.getInstance().claims.stream().filter(claim -> claim.getOwner() == player).toList().size() == 0)
                ClaimBuilder.giveClaimItem(player);
            else
                sender.sendMessage(ChatColor.RED + "Only one claim is allowed per player!");
        } else
            sender.sendMessage(ChatColor.RED + "Only players can execute this command!");
        return true;
    }

}
