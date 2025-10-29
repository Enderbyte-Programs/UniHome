package net.enderbyteprograms.UniHome.listeners;

import net.enderbyteprograms.UniHome.Static;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KillListener implements Listener {
    @EventHandler
    public void onDie(PlayerDeathEvent pde) {
        if (!Static.Configuration.getBoolean("censor-invisibility")) {
            return;
        }
        String deathMessage = pde.getDeathMessage();
        for (Player p: Bukkit.getOnlinePlayers()) {
            String pName = p.getDisplayName();
            if (deathMessage.contains(pName)) {
                if (!pde.getEntity().getDisplayName().equals(pName)) {
                    //Does the player have invisibility?

                    boolean hasInvisibility = false;
                    for (PotionEffect pe:p.getActivePotionEffects()) {
                        if (pe.getType().equals(PotionEffectType.INVISIBILITY)) {
                            Static.Plugin.getLogger().info("Real death message: "+deathMessage);
                            for (Player player:Bukkit.getOnlinePlayers()) {
                                if (player.hasPermission("unihome.admin")) {
                                    player.sendMessage("(uncensored) "+deathMessage);
                                }
                            }
                            deathMessage = deathMessage.replace(pName,"???");
                        }
                    }

                }
            }
        }

        pde.setDeathMessage(deathMessage);
    }
}
