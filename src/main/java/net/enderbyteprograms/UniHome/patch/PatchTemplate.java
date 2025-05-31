package net.enderbyteprograms.UniHome.patch;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public interface PatchTemplate {
    public boolean NeedsPatch(JavaPlugin f);
    public void Patch(JavaPlugin f);
}
