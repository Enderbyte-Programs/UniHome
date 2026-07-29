package net.enderbyteprograms.unihome.commands;

import net.enderbyteprograms.Utilities;
import net.enderbyteprograms.unihome.Data;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

public class EarmarkCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command");
            return false;
        }

        Player callingPlayer = (Player)sender;

        if (args.length == 0) {
            sender.sendMessage("You must specify one of the following: start, goto, tp");
            sender.sendMessage("start: Begin earmarking\ngoto: Go to earmarked entity\ntp: Finish earmarking");
        } else {
            switch (args[0]) {
                case "start":
                    RayTraceResult rt = callingPlayer.getWorld().rayTraceEntities(
                            callingPlayer.getEyeLocation(),
                            callingPlayer.getEyeLocation().getDirection(),
                            20,
                            entity -> entity != callingPlayer
                    );

                    if (rt == null) {
                        sender.sendMessage("Look at the target entity");
                        return false;
                    }

                    Entity e = rt.getHitEntity();

                    if (e == null) {
                        sender.sendMessage("Look at the target entity");
                        return false;
                    }

                    String newName = Utilities.rand62(16);
                    e.setCustomName(newName);
                    Data.earmarkedEntities.put(callingPlayer.getUniqueId(),e);
                    callingPlayer.sendMessage(String.format("Marked %s (UID: %s)",e.getType().toString(),newName));
                    return true;
                case "goto":
                    if (!Data.earmarkedEntities.containsKey(callingPlayer.getUniqueId())) {
                        callingPlayer.sendMessage("You have not marked an entity");
                        return false;
                    }
                    Entity selectedEntity = Data.earmarkedEntities.get(callingPlayer.getUniqueId());
                    callingPlayer.teleport(selectedEntity.getLocation());
                    callingPlayer.sendMessage("Teleporting you to marked entity's location");
                    return true;
                case "tp":
                    if (!Data.earmarkedEntities.containsKey(callingPlayer.getUniqueId())) {
                        callingPlayer.sendMessage("You have not marked an entity");
                        return false;
                    }
                    Entity selectedEntityTP = Data.earmarkedEntities.get(callingPlayer.getUniqueId());
                    selectedEntityTP.teleport(callingPlayer.getLocation());
                    selectedEntityTP.setCustomName(null);//Clear identifier name
                    callingPlayer.sendMessage("Marking finished");
                    return true;
                default:
                    sender.sendMessage("Invalid option. Valid options are start, goto, and tp.");
                    return false;
            }
        }
        return false;
    }
}
