package net.enderbyteprograms.unihome.commands;

import net.enderbyteprograms.Utilities;
import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.database.Comparison;
import net.enderbyteprograms.database.ResultSet;
import net.enderbyteprograms.database.Updater;
import net.enderbyteprograms.unihome.structures.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class SpecialAdminCommand implements CommandExecutor {

    public static String[] availableActions = {"reload","importjd","importls","syncnames","ptimport"};

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length > 0 && commandSender.hasPermission("unihome.admin")) {
            String xcommand = strings[0];

            if (xcommand.equals("reload")) {

                Data.plugin.reloadConfig();
                Data.configuration = Data.plugin.getConfig();
                commandSender.sendMessage("Reloaded Successfully.");

            } else if (xcommand.equals("importjd")) {

            } else if (xcommand.equals("importls")) {

            } else if (xcommand.equals("syncnames")) {
                commandSender.sendMessage("Synchronizing server cache and name list...");

                int recordsadded = 0;
                for (OfflinePlayer p: Bukkit.getOfflinePlayers()) {
                    //Are we already storing this in the list



                    if (!Data.playerInformation.containsKey(p.getUniqueId())) {
                        recordsadded++;
                        //Data.nameAliasTable.insert(Map.of("uuid",p.getUniqueId().toString(),"name",p.getName(),"nname",p.getName().toLowerCase()));
                        PlayerInfo pi = new PlayerInfo();
                        pi.uuid = p.getUniqueId().toString();
                        if (p.getName() == null) {
                            pi.name = "#unknown_"+ Utilities.getRandomInt(10000,99999);//No longer allowed to have duplicates, sorry
                            pi.comparableName = pi.name;
                        } else {
                            pi.name = p.getName();
                            pi.comparableName = p.getName().toLowerCase();
                        }
                        Data.playerInformation.put(p.getUniqueId(),pi);
                    } else {

                        if (Data.playerInformation.get(p.getUniqueId()).name.startsWith("#unknown")) {
                            //Data.nameAliasTable.update(new Comparison("uuid", p.getUniqueId().toString(), false), new Updater("name", p.getName()).add("nname", p.getName().toLowerCase()));
                            Data.playerInformation.get(p.getUniqueId()).name = p.getName();
                            Data.playerInformation.get(p.getUniqueId()).comparableName = p.getName().toLowerCase();
                            recordsadded++;
                        }

                    }
                }
                commandSender.sendMessage(String.format("Added %d new records",recordsadded));
            } else if (xcommand.equals("ptimport")) {
                int recordsadded = 0;

                for (OfflinePlayer p:Bukkit.getOfflinePlayers()) {
                    Data.playerInformation.get(p.getUniqueId()).playtimeInTicks = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
                    recordsadded++;
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
