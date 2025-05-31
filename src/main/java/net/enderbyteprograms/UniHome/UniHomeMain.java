package net.enderbyteprograms.UniHome;

import net.enderbyteprograms.UniHome.commands.DelHomeCommand;
import net.enderbyteprograms.UniHome.commands.HomeTabCompleter;
import net.enderbyteprograms.UniHome.commands.SetHomeCommand;
import net.enderbyteprograms.UniHome.epdb.DataTypes;
import net.enderbyteprograms.UniHome.epdb.EPDatabase;
import net.enderbyteprograms.UniHome.patch.PatchMaster;
import org.bukkit.plugin.java.JavaPlugin;

public class UniHomeMain extends JavaPlugin {
    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        PatchMaster.Patch(this);

        Static.Configuration = this.getConfig();
        Static.Plugin = this;
        Static.HomeTable = new EPDatabase(this).GetTable("homes");
        Static.HomeTable.AddColumn("uuid", DataTypes.String,"");
        Static.HomeTable.AddColumn("location",DataTypes.String,"");

        this.getCommand("sethome").setExecutor(new SetHomeCommand());
        this.getCommand("sethome").setTabCompleter(new HomeTabCompleter());
        this.getCommand("delhome").setExecutor(new DelHomeCommand());
        this.getCommand("delhome").setTabCompleter(new HomeTabCompleter());

        this.getLogger().info("UniHome (c) 2025 Enderbyte Programs, no rights reserved. Plugin initialized.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Goodbye!");
    }
}
