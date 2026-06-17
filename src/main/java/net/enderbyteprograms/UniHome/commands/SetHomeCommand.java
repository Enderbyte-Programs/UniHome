package net.enderbyteprograms.UniHome.commands;

import net.enderbyteprograms.UniHome.Data;
import net.enderbyteprograms.database.Comparison;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player issuingPlayer = (Player)commandSender;
            OfflinePlayer targettingPlayer = Bukkit.getOfflinePlayer(issuingPlayer.getUniqueId());

            if (commandSender.hasPermission("unihome.admin") && strings.length > 0) {
                UUID uuidAttempt = Data.getUUIDFromName(strings[0]);
                if (uuidAttempt == null) {
                    commandSender.sendMessage(ChatColor.DARK_RED+"Invalid player name");
                    return false;
                }
                targettingPlayer = Bukkit.getOfflinePlayer(uuidAttempt);//Operate on other player
            }else if (strings.length > 0) {
                commandSender.sendMessage(ChatColor.DARK_RED+"Insufficient Priviliges"+ ChatColor.RESET);
                return false;
            }

            List<String> allowedworlds = Data.Configuration.getStringList("allowinworlds");
            if (!allowedworlds.contains(issuingPlayer.getWorld().getName())) {
                commandSender.sendMessage(ChatColor.DARK_RED+"Home may not be in this world."+ChatColor.RESET);
                return false;
            }

            boolean foreignIssuer = !(issuingPlayer.getUniqueId().equals(targettingPlayer.getUniqueId()));

            Location l = issuingPlayer.getLocation();

            /*
            String fn = String.format("%s:%s:%s:%s",l.getWorld().getName(),l.getBlockX(),l.getBlockY(),l.getBlockZ());
            HashMap<String,Object> row = new HashMap<>();
            row.put("uuid",ex.getUniqueId().toString());
            row.put("location",fn);
            Data.oldHomeTable.DeleteWhere("uuid",ex.getUniqueId().toString());
            Data.oldHomeTable.Insert(row);
            */

            Data.homeTable.delete(new Comparison("uuid",targettingPlayer.getUniqueId().toString(),false));
            Data.homeTable.insert(Map.of("uuid",targettingPlayer.getUniqueId().toString(),"world",l.getWorld().getName(),"x",l.getX(),"y",l.getY(),"z",l.getZ()));

            if (foreignIssuer) {
                commandSender.sendMessage("This player's home has been set to *your* location.");
            } else {
                commandSender.sendMessage("Set home successfully. Use /home to go there.");
            }
            return true;
        } else {
            commandSender.sendMessage(ChatColor.DARK_RED+"Only players may use this command."+ ChatColor.RESET);
            return false;
        }
    }
}
