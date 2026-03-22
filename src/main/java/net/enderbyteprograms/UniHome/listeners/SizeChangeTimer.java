package net.enderbyteprograms.UniHome.listeners;

import net.enderbyteprograms.UniHome.Static;
import net.enderbyteprograms.UniHome.structures.SizeTransition;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class SizeChangeTimer extends BukkitRunnable {
    @Override
    public void run() {

        Static.aprilFoolsTimer++;

        for (int i = 0; i < Static.activeTransitions.size(); i++) {
            if (Static.activeTransitions.get(i).isFinished()) {
                Static.activeTransitions.remove(i);
                i--;//Back it up
            }
        }//This block clears transitions that are finished

        for (SizeTransition transition:Static.activeTransitions) {
            transition.tick();
        }

        if (Static.aprilFoolsTimer % 600 == 0) {
            for (Player p: Bukkit.getOnlinePlayers()) {
                double currentSize = p.getAttribute(Attribute.SCALE).getBaseValue();
                double newsize = getNewSize();
                Static.activeTransitions.add(new SizeTransition(p,currentSize,newsize));
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
