package net.enderbyteprograms.unihome.commands;

import net.enderbyteprograms.Utilities;
import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.unihome.structures.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

                commandSender.sendMessage("Attempting to import from jdexport.txt");
                File importFile = new File(Data.plugin.getDataFolder(),"jdexport.txt");
                if (!importFile.exists()) {
                    commandSender.sendMessage("Ensure jdexport.txt is stored in the data folder");
                    return false;
                }
                try {
                    List<String> rawImport = Files.readAllLines(importFile.toPath());
                    int addedrecords = 0;
                    for (String line:rawImport) {
                        line = line.trim();
                        if (line.isEmpty()) {
                            continue;
                        }
                        UUID uuid = UUID.fromString(line.split(" ")[0]);
                        Instant joinDate = Instant.ofEpochSecond(Long.parseLong(line.split(" ")[1]));
                        if (Data.playerInformation.containsKey(uuid)) {
                            Data.playerInformation.get(uuid).joinDay = Math.toIntExact(joinDate.toEpochMilli() / (1000 * 60 * 60 * 24)) + 1;
                            addedrecords++;
                        }
                    }

                    commandSender.sendMessage(String.format("Added %d records",addedrecords));

                } catch (IOException e) {
                    commandSender.sendMessage("Failed to read import file");
                    return false;
                }

            } else if (xcommand.equals("importls")) {

                commandSender.sendMessage("Attempting to import from lsexport.txt");
                File importFile = new File(Data.plugin.getDataFolder(),"lsexport.txt");
                if (!importFile.exists()) {
                    commandSender.sendMessage("Ensure lsexport.txt is stored in the data folder");
                    return false;
                }
                try {
                    List<String> rawImport = Files.readAllLines(importFile.toPath());
                    int addedrecords = 0;
                    for (String line:rawImport) {
                        line = line.trim();
                        if (line.isEmpty()) {
                            continue;
                        }
                        UUID uuid = UUID.fromString(line.split(" ")[0]);
                        Instant joinDate = Instant.ofEpochSecond(Long.parseLong(line.split(" ")[1]));
                        if (Data.playerInformation.containsKey(uuid)) {
                            Data.playerInformation.get(uuid).lastSeenDay = Math.toIntExact(joinDate.toEpochMilli() / (1000 * 60 * 60 * 24)) + 1;
                            addedrecords++;
                        }
                    }
                    commandSender.sendMessage(String.format("Added %d records",addedrecords));

                } catch (IOException e) {
                    commandSender.sendMessage("Failed to read import file");
                    return false;
                }

            } else if (xcommand.equals("syncnames")) {
                commandSender.sendMessage("Synchronizing server cache and name list...");

                int recordsadded = 0;
                for (OfflinePlayer p: Bukkit.getOfflinePlayers()) {
                    //Are we already storing this in the list



                    if (!Data.playerInformation.containsKey(p.getUniqueId()) || Data.playerInformation.get(p.getUniqueId()).name == null) {
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

                        if (p.getName() == null) {
                            continue;
                        }

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
