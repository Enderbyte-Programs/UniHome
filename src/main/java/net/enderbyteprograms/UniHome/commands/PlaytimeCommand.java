package net.enderbyteprograms.UniHome.commands;

import net.enderbyteprograms.UniHome.Data;
import net.enderbyteprograms.UniHome.Playtime;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.UUID;

public class PlaytimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        UUID targetUUID;

        if (args.length > 0){
            String tpn = args[0];
            targetUUID = Data.getUUIDFromName(tpn);

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
        String friendlyResult = String.format("%d days, %02d:%02d:%02d",result.toDaysPart(),result.toHoursPart(),result.toMinutesPart(),result.toSecondsPart());
        double resultInKilominutes = result.toMinutes() / 1000D;
        sender.sendMessage( ChatColor.LIGHT_PURPLE+""+ChatColor.BOLD+""+"===== Information for "+Data.getNameFromUUID(targetUUID)+" =====");
        sender.sendMessage(ChatColor.AQUA+"Playtime: "+ChatColor.RESET+friendlyResult);
        sender.sendMessage(String.format("%sPlaytime (kmin):%s %.03f",ChatColor.AQUA,ChatColor.RESET,resultInKilominutes));
        sender.sendMessage(String.format("%sJoin date:%s %s",ChatColor.AQUA,ChatColor.RESET,""));
        sender.sendMessage(String.format("%sLast Seen on:%s %s",ChatColor.AQUA,ChatColor.RESET,""));
        return true;
    }
}
