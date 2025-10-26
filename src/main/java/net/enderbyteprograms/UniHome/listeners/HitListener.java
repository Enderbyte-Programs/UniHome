package net.enderbyteprograms.UniHome.listeners;

import net.enderbyteprograms.UniHome.Static;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitListener implements Listener {
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player target = (Player)e.getEntity();
            Player hitter = (Player)e.getDamager();
            if (!Static.Configuration.getStringList("pvprunsin").contains(target.getWorld().getName())) {
                return;
            }
            String tguuid = target.getUniqueId().toString();
            String isuuid = hitter.getUniqueId().toString();
            boolean isenabled = (boolean)(Static.PvPTable.GetWhere("uuid",tguuid).get(0).get("enabled"));
            boolean isinstigatorenabled = (boolean)(Static.PvPTable.GetWhere("uuid",isuuid).get(0).get("enabled"));
            if (!isenabled) {
                e.getDamager().sendMessage("This player has disabled PVP.");
                e.setCancelled(true);
            }
            if (!isinstigatorenabled) {
                hitter.sendMessage("You may not PVP whilst PVP is off for you.");
                e.setCancelled(true);
            }
        }
    }
}
