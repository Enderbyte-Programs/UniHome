package net.enderbyteprograms.unihome.listeners;

import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.unihome.structures.PlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        Data.uuidToNameMappings.forcePut(p.getUniqueId(),p.getName());
        Data.nameCapitalizationMappings.forcePut(p.getName(),p.getName().toLowerCase());

        if (!Data.playerInformation.containsKey(p.getUniqueId())) {
            PlayerInfo profile = new PlayerInfo();
            profile.uuid = p.getUniqueId().toString();
            profile.name = p.getName();
            profile.comparableName = p.getName().toLowerCase();
            profile.pvpEnabled = Data.plugin.getConfig().getBoolean("pvpdefault");
            profile.playtimeInTicks = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
            Data.playerInformation.put(p.getUniqueId(),profile);
            //The above line was missing for TOO LONG...
        }

        if (!Data.playerInformation.get(p.getUniqueId()).joinDataFilledIn()) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(p.getUniqueId());
            long baseval = op.getFirstPlayed();
            if (baseval >= 0) {
                Data.playerInformation.get(p.getUniqueId()).joinDay = Math.toIntExact(baseval / (1000 * 60 * 60 * 24)) + 1;
            }
        }

        if (!Data.playerInformation.get(p.getUniqueId()).lastSeenDataFilledIn()) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(p.getUniqueId());
            long baseval = op.getLastPlayed();
            if (baseval >= 0) {
                Data.playerInformation.get(p.getUniqueId()).lastSeenDay = Math.toIntExact(baseval / (1000 * 60 * 60 * 24)) + 1;
            }
        }

        if (!Data.isAprilFoolsRunning) {
            p.getAttribute(Attribute.SCALE).setBaseValue(1);//Reset their size
        }
    }
}
