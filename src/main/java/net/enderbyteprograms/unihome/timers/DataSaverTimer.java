package net.enderbyteprograms.unihome.timers;

import net.enderbyteprograms.unihome.Data;
import org.bukkit.scheduler.BukkitRunnable;

public class DataSaverTimer extends BukkitRunnable {
    /**
     *
     */
    @Override
    public void run() {
        synchronized (Data.playerInformationLock) {
            Data.playerInfoFile.write(Data.playerInformation.values());

        }
    }
}
