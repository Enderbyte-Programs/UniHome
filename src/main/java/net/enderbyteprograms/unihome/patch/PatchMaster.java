package net.enderbyteprograms.unihome.patch;

import net.enderbyteprograms.unihome.patch.patches.Mark1Patch;
import net.enderbyteprograms.unihome.patch.patches.Mark2Patch;
import net.enderbyteprograms.unihome.patch.patches.Mark3Patch;
import net.enderbyteprograms.unihome.patch.patches.Mark4Patch;
import org.bukkit.plugin.java.JavaPlugin;

public class PatchMaster {
    public static void Patch(JavaPlugin f) {
        //Updates the configuration through the versions
        PatchTemplate[] Patches = {//Must be in order, from oldest to newest
                 new Mark1Patch(),new Mark2Patch(),new Mark3Patch(),new Mark4Patch()
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
