package net.enderbyteprograms.UniHome.listeners;

import net.enderbyteprograms.UniHome.Static;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

            //Handle banhammer


            ItemStack itemUsedToHit = hitter.getInventory().getItemInMainHand();
            if (itemUsedToHit == null) {
                return;
            }
            ItemMeta iutim = itemUsedToHit.getItemMeta();
            if (!hitter.hasPermission("unihome.admin")) {
                return;
            }
            if (iutim.hasLore()) {
                if (iutim.getLore().get(0).contains("#banhammer")) {
                    boolean result = hitter.performCommand("tempban "+target.getDisplayName()+" 1m HIT BY BANHAMMER");
                    if (result) {
                        for (Player p: Bukkit.getOnlinePlayers()) {
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.MASTER,1f,1f);
                            p.sendTitle(ChatColor.DARK_RED+target.getDisplayName(), ChatColor.RED + "WAS SMASHED BY THE BANHAMMER",10,160,10);
                        }
                    }
                }
            }
        }


    }
}
