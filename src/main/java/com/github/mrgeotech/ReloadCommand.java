package com.github.mrgeotech;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("claims.reload")) {
            Claims.getInstance().reloadConfig();
        }
        return true;
    }
}
