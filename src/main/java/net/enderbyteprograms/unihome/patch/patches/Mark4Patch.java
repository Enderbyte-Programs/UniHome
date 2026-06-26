package net.enderbyteprograms.unihome.patch.patches;

import net.enderbyteprograms.unihome.patch.PatchTemplate;
import org.bukkit.plugin.java.JavaPlugin;

public class Mark4Patch implements PatchTemplate {


    @Override
    public boolean NeedsPatch(JavaPlugin f) {
        return !f.getConfig().contains("run-april-fools-2026");
    }

    @Override
    public void Patch(JavaPlugin f) {
        f.getLogger().info("Updating config to mark 4");

        f.getConfig().set("run-april-fools-2026",false);

    }
}
