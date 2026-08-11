package net.enderbyteprograms.unihome.commands;

import net.enderbyteprograms.Utilities;
import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.unihome.Playtime;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class IsEligibleForTrustedCommand implements CommandExecutor {
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
                sender.sendMessage("Console must specify name. /iseligiblefortrusted <name>");
                return false;
            }
        }

        if (targetUUID == null) {
            sender.sendMessage("Invalid name. This name does not exist or has not played on this server.");
            return false;
        }

        Duration playtime = Playtime.getPlaytime(targetUUID);
        Instant joinDate = Playtime.getJoinTime(targetUUID);

        if (playtime == null) {
            sender.sendMessage(ChatColor.YELLOW+"Playtime could not be determined for this player.");
        } else {
            sender.sendMessage("Satisfies playtime requirements? "+ Utilities.friendlyBool(playtime.getSeconds() >= (60 * 60 * 20)));
            if (!(playtime.getSeconds() >= (60 * 60 * 20))){
                Duration remaining = Duration.ofSeconds((60 * 60 * 20) - playtime.getSeconds());
                sender.sendMessage(String.format("Remaining playtime: %02d:%02d:%02d", remaining.toHoursPart(), remaining.toMinutesPart(), remaining.toSecondsPart()));
            }
        }

        if (joinDate == null) {
            sender.sendMessage(ChatColor.YELLOW+"Playtime could not be determined for this player.");
        } else {
            int currentDayID = Math.toIntExact(Instant.now().getEpochSecond() / (60 * 60 * 24)) + 1;
            int userDayID = Math.toIntExact(joinDate.getEpochSecond() / (60 * 60 * 24));
            int delta = currentDayID - userDayID;

            sender.sendMessage("Satisfies join requirements? "+Utilities.friendlyBool(delta >= 14));

            if (delta < 14) {
                int days_remaining = 14 - delta;
                sender.sendMessage("The user will not be eligible for at least "+days_remaining+" more days");
            }

        }

        sender.sendMessage(ChatColor.YELLOW+"Remember to check the user's history to ensure they have zero active points!");
        sender.sendMessage(ChatColor.AQUA+"Due to current limitations, we cannot check right now whether or not the user is trusted either. Do not trust the output of this command if the user is already trusted.");
        return true;
    }
}
