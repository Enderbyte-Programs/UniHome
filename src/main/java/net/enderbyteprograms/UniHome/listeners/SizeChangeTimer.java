package net.enderbyteprograms.UniHome.listeners;

import net.enderbyteprograms.UniHome.Data;
import net.enderbyteprograms.UniHome.structures.SizeTransition;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class SizeChangeTimer extends BukkitRunnable {
    @Override
    public void run() {

        Data.aprilFoolsTimer++;

        for (int i = 0; i < Data.activeTransitions.size(); i++) {
            if (Data.activeTransitions.get(i).isFinished()) {
                Data.activeTransitions.remove(i);
                i--;//Back it up
            }
        }//This block clears transitions that are finished

        for (SizeTransition transition: Data.activeTransitions) {
            transition.tick();
        }

        if (Data.aprilFoolsTimer % 600 == 0) {
            for (Player p: Bukkit.getOnlinePlayers()) {
                double currentSize = p.getAttribute(Attribute.SCALE).getBaseValue();
                double newsize = getNewSize();
                Data.activeTransitions.add(new SizeTransition(p,currentSize,newsize));
                p.sendMessage("Your new size is: "+newsize);
            }
        }

    }

    private double getNewSize() {
        switch (new Random().nextInt(0,3)) {
            case 0://Size 0.1 - 1
                return new Random().nextDouble(0.1,1);

            case 1://Size 1-2
                return new Random().nextDouble(1,2);

            case 2://Size 2-5
                return new Random().nextDouble(2,5);
            default:
                return 1;
        }
    }
}
