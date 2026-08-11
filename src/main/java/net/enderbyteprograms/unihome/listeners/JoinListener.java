package net.enderbyteprograms.unihome.listeners;

import net.enderbyteprograms.unihome.Data;
import net.enderbyteprograms.unihome.structures.PlayerInfo;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.Instant;
import java.util.List;

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

        PlayerInfo pi = Data.playerInformation.get(p.getUniqueId());

        if (!pi.joinDataFilledIn()) {
            OfflinePlayer op = Bukkit.getOfflinePlayer(p.getUniqueId());
            long baseval = op.getFirstPlayed();
            if (baseval >= 0) {
                pi.joinDay = Math.toIntExact(baseval / (1000 * 60 * 60 * 24)) + 1;
            }
        }

        pi.lastSeenDay = Math.toIntExact(Instant.now().getEpochSecond() / (60 * 60 * 24)) + 1;

        if (!Data.isAprilFoolsRunning) {
            p.getAttribute(Attribute.SCALE).setBaseValue(1);//Reset their size
        }

        if (!pi.anniversaryFilledIn() && pi.joinDataFilledIn()) {
            if (pi.joinDay < 5) {
                Data.plugin.getLogger().warning(String.format("Player %s needs their join date filled in!",p.getName()));
            } else {
                pi.nextAnniversary = pi.joinDay + 365;
            }
        }

        if (Data.plugin.getConfig().getBoolean("autogrant-cake")) {
            if (pi.anniversaryFilledIn()) {
                int currentDayID = Math.toIntExact(Instant.now().getEpochSecond() / (60 * 60 * 24)) + 1;
                if (currentDayID >= pi.nextAnniversary) {

                    ItemStack award = new ItemStack(Material.CAKE, 1);
                    ItemMeta im = award.getItemMeta();
                    if (im == null) {
                        im = Bukkit.getItemFactory().getItemMeta(Material.CAKE);
                    }
                    int yearcount = Math.floorDiv(((Math.toIntExact(Instant.now().getEpochSecond() / (60 * 60 * 24)) + 1) - pi.joinDay), 365);
                    im.setDisplayName("Anniversary Award");
                    im.setLore(List.of(String.format("Awarded to %s for playing on the server for %d year(s)", p.getName(), yearcount)));
                    im.addEnchant(Enchantment.MENDING,1,true);
                    award.setItemMeta(im);

                    p.getInventory().addItem(award);
                    p.sendMessage(ChatColor.GREEN + "You have been given an anniversary award! Thank you for your loyalty to this server.");
                    pi.nextAnniversary += 365;
                    Data.plugin.getLogger().info("Issued award to most recently joined player");
                }
            }
        }
    }
}
