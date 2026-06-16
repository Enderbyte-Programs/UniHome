package net.enderbyteprograms.UniHome.listeners;

import net.enderbyteprograms.UniHome.Static;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;

public class JoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (!Static.oldPVPTable.ExistsWhere("uuid",p.getUniqueId().toString())) {
            HashMap<String,Object> row = new HashMap<>();
            row.put("uuid",p.getUniqueId().toString());
            row.put("enabled",Static.Configuration.getBoolean("pvpdefault"));
            Static.oldPVPTable.Insert(row);
        }

        if (!Static.isAprilFoolsRunning) {
            p.getAttribute(Attribute.SCALE).setBaseValue(1);//Reset their size
        }
    }
}
