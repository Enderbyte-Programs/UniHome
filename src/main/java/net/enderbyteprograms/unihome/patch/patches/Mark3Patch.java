package net.enderbyteprograms.unihome.patch.patches;

import net.enderbyteprograms.unihome.patch.PatchTemplate;
import org.bukkit.plugin.java.JavaPlugin;

public class Mark3Patch implements PatchTemplate {
    @Override
    public boolean NeedsPatch(JavaPlugin f) {
        //f.getLogger().info(String.valueOf(!f.getConfig().contains("pvpdefault",true)));
        return !f.getConfig().contains("censor-invisibility",true);
    }

    @Override
    public void Patch(JavaPlugin f) {
        f.getLogger().info("Updating config to mark 3");

        f.getConfig().set("censor-invisibility",true);
    }
}
