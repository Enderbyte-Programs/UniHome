package net.enderbyteprograms.unihome.commands;

import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.database.Comparison;
import net.enderbyteprograms.database.ResultRow;
import net.enderbyteprograms.database.ResultSet;
import net.enderbyteprograms.unihome.structures.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player issuingPlayer = (Player)commandSender;
            String targetUUID = issuingPlayer.getUniqueId().toString();
            if (commandSender.hasPermission("unihome.admin") && strings.length > 0) {
                targetUUID = Data.getUUIDFromName(strings[0]).toString();

            }
            List<String> allowedworlds = Data.configuration.getStringList("allowinworlds");
            if (!allowedworlds.contains(issuingPlayer.getWorld().getName()) && !commandSender.hasPermission("unihome.admin")) {
                commandSender.sendMessage(ChatColor.DARK_RED+"You may not run this command in this world."+ChatColor.RESET);
                return false;
            }

            //ResultSet result = Data.homeTable.select(new Comparison("uuid",targetUUID,false));

            if (!Data.playerInformation.get(UUID.fromString(targetUUID)).hasHome()) {
                commandSender.sendMessage(ChatColor.DARK_RED+"No home set."+ChatColor.RESET);
                return false;
            }

            //ResultRow targetRow = result.get(0);
            PlayerInfo targetRow = Data.playerInformation.get(UUID.fromString(targetUUID));

            Location l = new Location(Bukkit.getWorld(targetRow.homeWorld),targetRow.homeX,targetRow.homeY,targetRow.homeZ);
            issuingPlayer.teleport(l);
            commandSender.sendMessage("Going home...");
            return true;

        } else {
            commandSender.sendMessage(ChatColor.DARK_RED+"Only players may run this command."+ChatColor.RESET);
            return false;
        }
    }
}
