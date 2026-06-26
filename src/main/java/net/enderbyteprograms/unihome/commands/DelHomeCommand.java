package net.enderbyteprograms.unihome.commands;

import net.enderbyteprograms.unihome.Data;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            String targetUUID;
            targetUUID = ((Player)commandSender).getUniqueId().toString();

            if (commandSender.hasPermission("unihome.admin") && strings.length > 0) {
                targetUUID = Data.getUUIDFromName(strings[0]).toString();
            } else if (strings.length > 0) {
                commandSender.sendMessage(ChatColor.DARK_RED+"Insufficient Priviliges"+ ChatColor.RESET);
                return false;
            }

            if (targetUUID == null) {
                commandSender.sendMessage(ChatColor.DARK_RED+"Invalid player");
                return false;
            }

            //Data.homeTable.delete(new Comparison("uuid",targetUUID,false));
            Data.playerInformation.get(UUID.fromString(targetUUID)).homeWorld = null;
            commandSender.sendMessage("Deleted home successfully.");
            return true;
        } else {
            commandSender.sendMessage(ChatColor.DARK_RED+"Only players may use this command."+ ChatColor.RESET);
            return false;
        }
    }
}
