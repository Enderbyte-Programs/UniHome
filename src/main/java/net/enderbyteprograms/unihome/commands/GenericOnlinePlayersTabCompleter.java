package net.enderbyteprograms.unihome.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GenericOnlinePlayersTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> f = new ArrayList<>();

        boolean useChopper = strings.length > 0;
        String chopperValue = null;

        if (useChopper) {
            chopperValue = List.of(strings).getLast();
        }

        if (commandSender.hasPermission("unihome.admin")) {
            for (Player p:Bukkit.getOnlinePlayers()) {
                if (!useChopper || (p.getName().startsWith(chopperValue))) {
                    f.add(p.getName());
                }
            }
        }
        return f;
    }
}
