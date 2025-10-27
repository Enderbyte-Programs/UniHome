package net.enderbyteprograms.UniHome.patch.patches;

import net.enderbyteprograms.UniHome.patch.PatchTemplate;
import org.bukkit.plugin.java.JavaPlugin;

public class Mark3Patch implements PatchTemplate {
    @Override
    public boolean NeedsPatch(JavaPlugin f) {
        //f.getLogger().info(String.valueOf(!f.getConfig().contains("pvpdefault",true)));
        f.getLogger().info("Updating config to mark 3");
        return !f.getConfig().contains("censor-invisibility",true);
    }

    @Override
    public void Patch(JavaPlugin f) {
        f.getConfig().set("censor-invisibility",true);
    }
}
