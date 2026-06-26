package net.enderbyteprograms.unihome.timers;

import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.unihome.structures.SizeTransition;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class SecondlyTimer extends BukkitRunnable {
    @Override
    public void run() {

        for (Player p:Bukkit.getOnlinePlayers()) {
            Data.playerInformation.get(p.getUniqueId()).playtimeInTicks = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
        }

    }
}
