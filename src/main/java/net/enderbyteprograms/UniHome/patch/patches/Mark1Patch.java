package net.enderbyteprograms.UniHome.patch.patches;

import net.enderbyteprograms.UniHome.patch.PatchTemplate;
import org.bukkit.plugin.java.JavaPlugin;

public class Mark1Patch implements PatchTemplate {
    public boolean NeedsPatch(JavaPlugin f) {
        return !f.getConfig().contains("allowinworlds",true);
    }

    public void Patch(JavaPlugin f) {
        f.getLogger().info("Updating config to mark 1");
        f.getConfig().set("allowinworlds",new String[] {"ExampleWorld"});
    }
}
