package net.enderbyteprograms.UniHome.commands;

import net.enderbyteprograms.UniHome.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpecialAdminCommand implements CommandExecutor {

    public static String[] availableActions = {"reload","importjd","importls"};

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length > 0 && commandSender.hasPermission("unihome.admin")) {
            String xcommand = strings[0];

            if (xcommand.equals("reload")) {

                Data.Plugin.reloadConfig();
                Data.Configuration = Data.Plugin.getConfig();
                commandSender.sendMessage("Reloaded Successfully.");

            } else if (xcommand.equals("importjd")) {

            } else if (xcommand.equals("importls")) {

            }
        } else {

            commandSender.sendMessage("Insufficient permission or invalid command");
            return false;

        }

        return true;
    }
}
