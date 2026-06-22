package net.enderbyteprograms.UniHome.commands;

import net.enderbyteprograms.UniHome.Data;
import net.enderbyteprograms.database.Comparison;
import net.enderbyteprograms.database.ResultSet;
import net.enderbyteprograms.database.Updater;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class SpecialAdminCommand implements CommandExecutor {

    public static String[] availableActions = {"reload","importjd","importls","syncnames"};

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length > 0 && commandSender.hasPermission("unihome.admin")) {
            String xcommand = strings[0];

            if (xcommand.equals("reload")) {

                Data.Plugin.reloadConfig();
                Data.Configuration = Data.Plugin.getConfig();
                commandSender.sendMessage("Reloaded Successfully.");

            } else if (xcommand.equals("importjd")) {

            } else if (xcommand.equals("importls")) {

            } else if (xcommand.equals("syncnames")) {
                commandSender.sendMessage("Synchronizing server cache and name list...");

                int recordsadded = 0;
                for (OfflinePlayer p: Bukkit.getOfflinePlayers()) {
                    //Are we already storing this in the list
                    if (p.getName() == null) {
                        continue;
                    }
                    ResultSet tgt = Data.nameAliasTable.select(new Comparison("uuid",p.getUniqueId().toString(),false));

                    if (tgt.isEmpty()) {
                        recordsadded++;
                        Data.nameAliasTable.insert(Map.of("uuid",p.getUniqueId().toString(),"name",p.getName(),"nname",p.getName().toLowerCase()));
                    } else {

                        if (tgt.getFirst().getString("name").toLowerCase().contains("unknown")) {
                            Data.nameAliasTable.update(new Comparison("uuid", p.getUniqueId().toString(), false), new Updater("name", p.getName()).add("nname", p.getName().toLowerCase()));
                            recordsadded++;
                        }

                    }
                }
                commandSender.sendMessage(String.format("Added %d new records",recordsadded));
            }
        } else {

            commandSender.sendMessage("Insufficient permission or invalid command");
            return false;

        }

        return true;
    }
}
