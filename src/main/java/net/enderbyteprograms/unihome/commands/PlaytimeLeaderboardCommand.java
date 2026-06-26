package net.enderbyteprograms.unihome.commands;

import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.unihome.Playtime;
import net.enderbyteprograms.unihome.structures.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.profile.PlayerProfile;

import java.time.Duration;
import java.util.*;

public class PlaytimeLeaderboardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        int limit;

        try {
            limit = Integer.parseInt(args[0]);

            if (limit < 0) {
                throw new Exception();
            }
        } catch (Exception e) {
            limit = 10;
        }

        sender.sendMessage(ChatColor.AQUA+""+ChatColor.BOLD+"===== Top Playtime =====");
        int finalLimit = limit;
        Bukkit.getScheduler().runTaskAsynchronously(Data.plugin,() -> {
           //Mr Vatougious would have a fit because I have no clue what I'm doing
            List<PlayerInfo> sortedByPlaytime = new ArrayList<PlayerInfo>();

            for (PlayerInfo pi:Data.playerInformation.values()) {
                if (pi.playtimeFilledIn()) {
                    sortedByPlaytime.add(pi);
                }
            }

            Collections.sort(sortedByPlaytime,new Comparator<PlayerInfo>() {

                @Override
                public int compare(PlayerInfo playerInfo, PlayerInfo t1) {
                    return playerInfo.playtimeInTicks.compareTo(t1.playtimeInTicks);
                }


            });
            Collections.reverse(sortedByPlaytime);

            for (int i = 0; i < finalLimit && !(sortedByPlaytime.isEmpty()); i++) {

                ChatColor selectedColour;
                PlayerInfo selectedProfile = sortedByPlaytime.remove(0);
                Duration ppl = Playtime.getPlaytime(UUID.fromString(selectedProfile.uuid));

                switch (i + 1) {
                    case 1:
                        selectedColour = ChatColor.GOLD;
                        break;

                    case 2:
                        selectedColour = ChatColor.GRAY;
                        break;

                    case 3:
                        selectedColour = ChatColor.RED;
                        break;

                    default:
                        selectedColour = ChatColor.GREEN;
                        break;
                }

                sender.sendMessage(String.format(selectedColour+"%d: %s - %d days, %02d:%02d:%02d (%.1f kmin)",i+1,selectedProfile.name,ppl.toDaysPart(),ppl.toHoursPart(),ppl.toMinutesPart(),ppl.toSecondsPart(),(double)ppl.toMinutes() / 1000D));

            }
        });

        return true;

    }
}
