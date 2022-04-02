package com.github.mrgeotech;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            if (Claims.getInstance().claims.stream().filter(claim -> claim.getOwner() == player && !claim.isCompleted()).toList().size() == 0)
                ClaimBuilder.giveClaimItem(player);
            else
                sender.sendMessage(Claims.getColoredString("can-only-build-one-claim-at-a-time-error"));
        } else
            sender.sendMessage(Claims.getColoredString("only-players-can-execute-error"));
        return true;
    }

}
