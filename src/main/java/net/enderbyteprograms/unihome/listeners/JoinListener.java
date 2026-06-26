package net.enderbyteprograms.unihome.listeners;

import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.database.Comparison;
import net.enderbyteprograms.database.Updater;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Map;

public class JoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (Data.nameAliasTable.select(new Comparison("uuid",p.getUniqueId().toString(),false)).isEmpty()) {

            //Initialize username
            Data.nameAliasTable.insert(Map.of("uuid",p.getUniqueId().toString(),"name",p.getName(),"nname",p.getName().toLowerCase()));

        } else if (Data.nameAliasTable.select(new Comparison(new Comparison("uuid",p.getUniqueId().toString(),false)).and(new Comparison("name",p.getName(),false))).isEmpty()) {
            //User has changed name
            Data.nameAliasTable.update(new Comparison("uuid",p.getUniqueId().toString(),false),new Updater("name",p.getName()).add("nname",p.getName().toLowerCase()));
        }

        if (Data.pvpTable.select(new Comparison("uuid",p.getUniqueId().toString(),false)).isEmpty()) {
            Data.pvpTable.insert(Map.of("uuid",p.getUniqueId().toString(),"enabled",Data.Configuration.getBoolean("pvpdefault")));
        }

        if (!Data.isAprilFoolsRunning) {
            p.getAttribute(Attribute.SCALE).setBaseValue(1);//Reset their size
        }
    }
}
