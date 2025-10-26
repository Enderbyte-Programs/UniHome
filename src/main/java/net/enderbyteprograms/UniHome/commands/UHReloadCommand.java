package net.enderbyteprograms.UniHome.commands;

import net.enderbyteprograms.UniHome.Static;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UHReloadCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("unihome.admin")) {
            Static.Configuration = Static.Plugin.getConfig();
            commandSender.sendMessage("Reloaded Successfully.");
        }
        return true;
    }
}
