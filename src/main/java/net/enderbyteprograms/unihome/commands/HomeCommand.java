package net.enderbyteprograms.unihome.commands;

import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.database.Comparison;
import net.enderbyteprograms.database.ResultRow;
import net.enderbyteprograms.database.ResultSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player issuingPlayer = (Player)commandSender;
            String targetUUID = issuingPlayer.getUniqueId().toString();
            if (commandSender.hasPermission("unihome.admin") && strings.length > 0) {
                targetUUID = Data.getUUIDFromName(strings[0]).toString();

            }
            List<String> allowedworlds = Data.Configuration.getStringList("allowinworlds");
            if (!allowedworlds.contains(issuingPlayer.getWorld().getName()) && !commandSender.hasPermission("unihome.admin")) {
                commandSender.sendMessage(ChatColor.DARK_RED+"You may not run this command in this world."+ChatColor.RESET);
                return false;
            }

            ResultSet result = Data.homeTable.select(new Comparison("uuid",targetUUID,false));

            if (result.size() == 0) {
                commandSender.sendMessage(ChatColor.DARK_RED+"No home set."+ChatColor.RESET);
                return false;
            }

            ResultRow targetRow = result.get(0);

            Location l = new Location(Bukkit.getWorld(targetRow.getString("world")),targetRow.getDouble("x"),targetRow.getDouble("y"),targetRow.getDouble("z"));
            issuingPlayer.teleport(l);
            commandSender.sendMessage("Going home...");
            return true;

        } else {
            commandSender.sendMessage(ChatColor.DARK_RED+"Only players may run this command."+ChatColor.RESET);
            return false;
        }
    }
}
