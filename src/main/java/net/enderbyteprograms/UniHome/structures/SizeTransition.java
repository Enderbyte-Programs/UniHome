package net.enderbyteprograms.UniHome.structures;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class SizeTransition {

    public double fromSize;
    public double toSize;
    public int progress;
    public double deltaPerTick;
    private final int transitionLength = 60;
    public Player associatedPlayer;

    public SizeTransition(Player p, double from, double to) {
        fromSize = from;
        toSize = to;
        deltaPerTick = (to - from) / transitionLength;
        progress = 0;
        associatedPlayer = p;
    }

    public void tick() {
        if (associatedPlayer.isOnline() && progress < transitionLength) {
            progress++;
            AttributeInstance currentScale = associatedPlayer.getAttribute(Attribute.SCALE);
            double currentValue = currentScale.getBaseValue();
            double newValue = currentValue + deltaPerTick;
            currentScale.setBaseValue(newValue);
        }
    }

    public boolean isFinished() {
        return progress == transitionLength;
    }

}
