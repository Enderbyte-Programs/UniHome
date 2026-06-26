package net.enderbyteprograms.unihome.listeners;

import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.unihome.structures.PlayerInfo;
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

        if (!Data.playerInformation.containsKey(p.getUniqueId())) {
            PlayerInfo profile = new PlayerInfo();
            profile.uuid = p.getUniqueId().toString();
            profile.name = p.getName();
            profile.comparableName = p.getName().toLowerCase();
            profile.pvpEnabled = Data.plugin.getConfig().getBoolean("pvpdefault");
            profile.playtimeInTicks = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
        }

        if (!Data.isAprilFoolsRunning) {
            p.getAttribute(Attribute.SCALE).setBaseValue(1);//Reset their size
        }
    }
}
