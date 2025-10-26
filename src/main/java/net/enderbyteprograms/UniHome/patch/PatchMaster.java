package net.enderbyteprograms.UniHome.patch;

import net.enderbyteprograms.UniHome.patch.patches.Mark1Patch;
import net.enderbyteprograms.UniHome.patch.patches.Mark2Patch;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PatchMaster {
    public static void Patch(JavaPlugin f) {
        //Updates the configuration through the versions
        PatchTemplate[] Patches = {//Must be in order, from oldest to newest
                 new Mark1Patch(),new Mark2Patch()
        };
        f.getLogger().info("Checking for patches");
        for (PatchTemplate t : Patches) {
            if (t.NeedsPatch(f)) {
                t.Patch(f);
            }
        }
        f.saveConfig();
    }
}
