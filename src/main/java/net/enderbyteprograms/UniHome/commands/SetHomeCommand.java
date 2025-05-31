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
import java.util.Map;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player ex = (Player)commandSender;

            if (commandSender.hasPermission("unihome.admin") && strings.length > 0) {
                ex = Bukkit.getPlayer(strings[0]);//Operate on other player
            }else if (strings.length > 0) {
                commandSender.sendMessage(ChatColor.DARK_RED+"Insufficient Priviliges"+ ChatColor.RESET);
                return false;
            }

            List<String> allowedworlds = Static.Configuration.getStringList("allowinworlds");
            if (!allowedworlds.contains(ex.getWorld().getName())) {
                commandSender.sendMessage(ChatColor.DARK_RED+"Home may not be in this world."+ChatColor.RESET);
                return false;
            }
            Location l = ex.getLocation();
            String fn = String.format("%s:%s:%s:%s",l.getWorld().getName(),l.getBlockX(),l.getBlockY(),l.getBlockZ());
            HashMap<String,Object> row = new HashMap<>();
            row.put("uuid",ex.getUniqueId().toString());
            row.put("location",fn);
            Static.HomeTable.DeleteWhere("uuid",ex.getUniqueId().toString());
            Static.HomeTable.Insert(row);
            commandSender.sendMessage("Set home successfully. Use /home to go there.");
            return true;
        } else {
            commandSender.sendMessage(ChatColor.DARK_RED+"Only players may use this command."+ ChatColor.RESET);
            return false;
        }
    }
}
