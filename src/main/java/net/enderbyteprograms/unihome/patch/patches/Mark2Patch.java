package net.enderbyteprograms.unihome.patch.patches;

import net.enderbyteprograms.unihome.patch.PatchTemplate;
import org.bukkit.plugin.java.JavaPlugin;

public class Mark2Patch implements PatchTemplate {
    @Override
    public boolean NeedsPatch(JavaPlugin f) {
        //f.getLogger().info(String.valueOf(!f.getConfig().contains("pvpdefault",true)));
        return !f.getConfig().contains("pvpdefault",true);
    }

    @Override
    public void Patch(JavaPlugin f) {
        f.getLogger().info("Updating config to mark 2");

        f.getConfig().set("pvpdefault",true);
        f.getConfig().set("pvprunsin",new String[] {"survival","survival_nether","survival_end"});
    }
}
