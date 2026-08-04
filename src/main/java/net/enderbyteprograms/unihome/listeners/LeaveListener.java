package net.enderbyteprograms.unihome.listeners;

import net.enderbyteprograms.unihome.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Instant;

public class LeaveListener implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent playerQuitEvent) {
        Player p = playerQuitEvent.getPlayer();
        Data.playerInformation.get(p.getUniqueId()).lastSeenDay = Math.toIntExact(Instant.now().getEpochSecond() / (60 * 60 * 24)) + 1;
    }

}
