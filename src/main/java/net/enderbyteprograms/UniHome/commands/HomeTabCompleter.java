package net.enderbyteprograms.UniHome.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomeTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> f = new ArrayList<>();
        if (commandSender.hasPermission("unihome.admin")) {
            for (Player p:Bukkit.getOnlinePlayers()) {
                f.add(p.getDisplayName());
            }
        }
        return f;
    }
}
