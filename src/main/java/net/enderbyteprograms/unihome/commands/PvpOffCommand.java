package net.enderbyteprograms.unihome.commands;

import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.database.Comparison;
import net.enderbyteprograms.database.Updater;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvpOffCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        String targetUUID;
        if (strings.length > 0 && commandSender.hasPermission("unihome.admin")) {
            targetUUID = Data.getUUIDFromName(strings[0]).toString();
        }
        else if (commandSender instanceof Player) {
            targetUUID = ((Player) commandSender).getUniqueId().toString();
        } else {
            commandSender.sendMessage(ChatColor.DARK_RED+"CLI cannot run this"+ChatColor.RESET);
            return false;
        }

        if (targetUUID == null) {
            commandSender.sendMessage("Invalid player");
            return false;
        }

        //Data.pvpTable.update(new Comparison("uuid",targetUUID,false),new Updater("enabled",false));
        Data.playerInformation.get(UUID.fromString(targetUUID)).pvpEnabled = false;

        commandSender.sendMessage(ChatColor.GREEN+"Disabled PVP against you"+ChatColor.RESET);

        return true;
    }
}
