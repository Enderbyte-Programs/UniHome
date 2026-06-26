package net.enderbyteprograms.unihome.patch;

import org.bukkit.plugin.java.JavaPlugin;

public interface PatchTemplate {
    public boolean NeedsPatch(JavaPlugin f);
    public void Patch(JavaPlugin f);
}
