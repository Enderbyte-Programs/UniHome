package net.enderbyteprograms.unihome.commands;

import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.unihome.Playtime;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.*;
import java.util.UUID;

public class PlaytimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        UUID targetUUID;

        if (args.length > 0){
            String tpn = args[0];

            if (tpn.startsWith("uuid:")) {
                try {
                    targetUUID = UUID.fromString(tpn.split(":")[1]);
                } catch (Exception e) {
                    sender.sendMessage("Invalid UUID");
                    return false;
                }
            } else {

                targetUUID = Data.getUUIDFromName(tpn);
            }

        } else {
            if (sender instanceof Player) {
                targetUUID = ((Player) sender).getUniqueId();
            } else {
                sender.sendMessage("Console must specify name. /playtime <name>");
                return false;
            }
        }

        if (targetUUID == null) {
            sender.sendMessage("Invalid name. This name does not exist or has not played on this server.");
            return false;
        }

        Duration result = Playtime.getPlaytime(targetUUID);
        if (result.isZero()) {
            sender.sendMessage("Unable to get playtime data for this player");
            return false;
        }
        String friendlyResult = String.format("%d days, %02d:%02d:%02d",result.toDaysPart(),result.toHoursPart(),result.toMinutesPart(),result.toSecondsPart());
        double resultInKilominutes = result.toMinutes() / 1000D;

        Instant whenJoined = Playtime.getJoinTime(targetUUID);
        String wjres;

        if (whenJoined == null) {
            wjres = "Not recorded";
        } else {
            LocalDate joinDate = whenJoined.atZone(ZoneId.systemDefault()).toLocalDate();
            wjres = String.format("%04d-%02d-%02d",joinDate.getYear(),joinDate.getMonthValue(),joinDate.getDayOfMonth());
        }

        Instant whenSeen = Playtime.getLastSeen(targetUUID);
        String lsres;

        if (whenSeen == null) {
            lsres = "Not recorded";
        } else {
            LocalDate joinDate = whenSeen.atZone(ZoneId.systemDefault()).toLocalDate();
            lsres = String.format("%04d-%02d-%02d",joinDate.getYear(),joinDate.getMonthValue(),joinDate.getDayOfMonth());
        }

        sender.sendMessage( ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+"===== Information for "+Data.getNameFromUUID(targetUUID)+" =====");
        sender.sendMessage(ChatColor.AQUA+"Playtime: "+ChatColor.RESET+friendlyResult);
        sender.sendMessage(String.format("%sPlaytime (kmin):%s %.03f",ChatColor.AQUA,ChatColor.RESET,resultInKilominutes));
        sender.sendMessage(String.format("%sJoin date:%s %s",ChatColor.AQUA,ChatColor.RESET,wjres));
        sender.sendMessage(String.format("%sLast Seen on:%s %s",ChatColor.AQUA,ChatColor.RESET,lsres));
        return true;
    }
}
