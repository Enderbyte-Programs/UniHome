package net.enderbyteprograms.UniHome.commands;

import net.enderbyteprograms.UniHome.Static;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PvpOnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player target = null;
        if (strings.length > 0 && commandSender.hasPermission("unihome.admin")) {
            target = Bukkit.getPlayer(strings[0]);
        }
        else if (commandSender instanceof Player) {
            target = (Player)commandSender;
        } else {
            commandSender.sendMessage(ChatColor.DARK_RED+"CLI cannot run this"+ChatColor.RESET);
            return false;
        }

        Static.PvPTable.DeleteWhere("uuid",target.getUniqueId().toString());
        HashMap<String,Object> newrow = new HashMap<>();
        newrow.put("uuid",target.getUniqueId().toString());
        newrow.put("enabled",true);
        Static.PvPTable.Insert(newrow);
        commandSender.sendMessage(ChatColor.GREEN+"Enabled PVP against you"+ChatColor.RESET);

        return true;
    }
}
