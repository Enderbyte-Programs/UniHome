package net.enderbyteprograms.UniHome.commands;

import net.enderbyteprograms.UniHome.Static;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class DelHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player ex = (Player)commandSender;

            if (commandSender.hasPermission("unihome.admin") && strings.length > 0) {
                ex = Bukkit.getPlayer(strings[0]);//Operate on other player
            } else if (strings.length > 0) {
                commandSender.sendMessage(ChatColor.DARK_RED+"Insufficient Priviliges"+ ChatColor.RESET);
                return false;
            }

            Static.HomeTable.DeleteWhere("uuid",ex.getUniqueId().toString());
            commandSender.sendMessage("Deleted home successfully.");
            return true;
        } else {
            commandSender.sendMessage(ChatColor.DARK_RED+"Only players may use this command."+ ChatColor.RESET);
            return false;
        }
    }
}
