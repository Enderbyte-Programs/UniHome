package net.enderbyteprograms.unihome.patch.patches;

import net.enderbyteprograms.unihome.patch.PatchTemplate;
import org.bukkit.plugin.java.JavaPlugin;

public class Mark5Patch implements PatchTemplate {
    @Override
    public boolean NeedsPatch(JavaPlugin f) {
        return !(f.getConfig().contains("autogrant-cake",true));
    }

    @Override
    public void Patch(JavaPlugin f) {
        f.getConfig().set("autogrant-cake",true);
        f.saveConfig();
    }
}
