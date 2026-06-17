package net.enderbyteprograms.UniHome.commands;

import net.enderbyteprograms.UniHome.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UHReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("unihome.admin")) {
            Data.Plugin.reloadConfig();
            Data.Configuration = Data.Plugin.getConfig();
            commandSender.sendMessage("Reloaded Successfully.");
        }
        return true;
    }
}
