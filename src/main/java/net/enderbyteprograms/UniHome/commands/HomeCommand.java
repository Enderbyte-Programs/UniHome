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

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player ex = (Player)commandSender;
            String target = ex.getUniqueId().toString();
            if (commandSender.hasPermission("unihome.admin") && strings.length > 0) {
                target = Bukkit.getPlayer(strings[0]).getUniqueId().toString();
                ex = Bukkit.getPlayer(strings[0]);
            }
            List<String> allowedworlds = Static.Configuration.getStringList("allowinworlds");
            if (!allowedworlds.contains(ex.getWorld().getName())) {
                commandSender.sendMessage(ChatColor.DARK_RED+"You may not run this command in this world."+ChatColor.RESET);
                return false;
            }
            List<HashMap<String,Object>> r = Static.HomeTable.GetWhere("uuid",target);
            if (r.size() == 0) {
                commandSender.sendMessage(ChatColor.DARK_RED+"No home set."+ChatColor.RESET);
                return false;
            }
            String rawencodedhome = r.get(0).get("location").toString();
            if (rawencodedhome.isBlank()) {
                commandSender.sendMessage(ChatColor.DARK_RED+"No home set."+ChatColor.RESET);
                return false;
            }
            String[] rehs = rawencodedhome.split(":");
            Location l = new Location(Bukkit.getWorld(rehs[0]),Double.parseDouble(rehs[1]),Double.parseDouble(rehs[2]),Double.parseDouble(rehs[3]));
            ex = (Player)commandSender;
            ex.teleport(l);
            commandSender.sendMessage("Going home...");
            return true;

        } else {
            commandSender.sendMessage(ChatColor.DARK_RED+"Only players may run this command."+ChatColor.RESET);
            return false;
        }
    }
}
