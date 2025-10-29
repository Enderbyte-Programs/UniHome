package net.enderbyteprograms.UniHome.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GetNearbyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        double radius = 20;
        if (strings.length > 0){
            String rawRadius = strings[0];
            try {
                radius = Double.parseDouble(rawRadius);
            } catch (NumberFormatException e) {
                commandSender.sendMessage(ChatColor.DARK_RED + "Invalid radius format. Defaulting to 20");
            }
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.DARK_RED + "May only be executed by players");
            return false;
        }

        Player executor = (Player)commandSender;

        commandSender.sendMessage(ChatColor.BOLD+"NEARBY PLAYERS");

        for (Player p: Bukkit.getOnlinePlayers()) {
            if (p.getDisplayName().equals(executor.getDisplayName())) {
                continue;
            }

            Location l = p.getLocation();
            double distance = executor.getLocation().distance(l);
            if (distance <= radius) {

                boolean isInvisible = false;
                for (PotionEffect potionEffect:p.getActivePotionEffects()) {
                    if (potionEffect.getType().equals(PotionEffectType.INVISIBILITY)) {
                        isInvisible = true;
                    }
                }

                String constructedString = "";
                constructedString += p.getDisplayName();
                constructedString += " ";
                constructedString += p.getLocation().getBlockX() + "," + p.getLocation().getBlockY() + "," + p.getLocation().getBlockZ();
                constructedString += " ";
                if (isInvisible) {
                    constructedString += ChatColor.RED + "INVISIBLE";
                } else {
                    constructedString += ChatColor.GREEN + "VISIBLE";
                }
                commandSender.sendMessage(constructedString);

            }
        }

        return true;
    }
}
