package net.enderbyteprograms.UniHome.patch.patches;

import net.enderbyteprograms.UniHome.patch.PatchTemplate;
import org.bukkit.plugin.java.JavaPlugin;

public class Mark2Patch implements PatchTemplate {
    @Override
    public boolean NeedsPatch(JavaPlugin f) {
        return !f.getConfig().contains("pvpdefault");
    }

    @Override
    public void Patch(JavaPlugin f) {
        f.getConfig().set("pvpdefault",true);
        f.getConfig().set("pvprunsin",new String[] {"survival","survival_nether","survival_end"});
    }
}
