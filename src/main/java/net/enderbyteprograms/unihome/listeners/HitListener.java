package net.enderbyteprograms.unihome.listeners;

import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.database.Comparison;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HitListener implements Listener {
    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {

        Player target;
        Player hitter;

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            target = (Player)e.getEntity();
            hitter = (Player)e.getDamager();

        } else if (e.getEntity() instanceof Player) {
            target = (Player)e.getEntity();

            if (e.getDamager() instanceof Projectile) {
                Projectile striker = (Projectile)e.getDamager();
                if (striker.getShooter() instanceof Player) {
                    hitter = (Player)striker.getShooter();
                } else {
                    return;
                }
            } else {
                return;
            }
        } else {
            return;
        }

        if (!Data.configuration.getStringList("pvprunsin").contains(target.getWorld().getName())) {
            return;
        }
        String tguuid = target.getUniqueId().toString();
        String isuuid = hitter.getUniqueId().toString();
        //boolean isenabled = (boolean)(Data.oldPVPTable.GetWhere("uuid",tguuid).get(0).get("enabled"));
        //boolean isenabled = Data.pvpTable.select(new Comparison("uuid",tguuid,false)).get(0).getBool("enabled");
        boolean isenabled = Data.playerInformation.get(target.getUniqueId()).pvpEnabled;
        //boolean isinstigatorenabled = (boolean)(Data.oldPVPTable.GetWhere("uuid",isuuid).get(0).get("enabled"));
        //boolean isinstigatorenabled = Data.pvpTable.select(new Comparison("uuid",isuuid,false)).get(0).getBool("enabled");
        boolean isinstigatorenabled = Data.playerInformation.get(hitter.getUniqueId()).pvpEnabled;
        if (!isenabled) {
            hitter.sendMessage("This player has disabled PVP.");
            e.setCancelled(true);
        }
        if (!isinstigatorenabled) {
            hitter.sendMessage("You may not PVP whilst PVP is off for you.");
            e.setCancelled(true);
        }

        //Handle banhammer


        ItemStack itemUsedToHit = hitter.getInventory().getItemInMainHand();
        ItemMeta iutim = itemUsedToHit.getItemMeta();
        if (!hitter.hasPermission("unihome.admin") || iutim == null) {//Exit out if not hit my item
            return;
        }
        if (iutim.hasLore()) {
            if (iutim.getLore().get(0).contains("#banhammer")) {
                boolean result = hitter.performCommand("tempban "+target.getName()+" 1m HIT BY BANHAMMER");
                if (result) {
                    for (Player p: Bukkit.getOnlinePlayers()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER,1f,1f);
                        p.sendTitle(ChatColor.DARK_RED+target.getName(), ChatColor.RED + "WAS SMASHED BY THE BANHAMMER",10,160,10);
                    }
                }
            }
        }


    }
}
