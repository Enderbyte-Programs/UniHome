package net.enderbyteprograms.UniHome;

import net.enderbyteprograms.UniHome.commands.*;
import net.enderbyteprograms.UniHome.epdb.DataTypes;
import net.enderbyteprograms.UniHome.epdb.EPDatabase;
import net.enderbyteprograms.UniHome.listeners.HitListener;
import net.enderbyteprograms.UniHome.listeners.JoinListener;
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
        Static.PvPTable = new EPDatabase(this).GetTable("pvp");
        Static.PvPTable.AddColumn("uuid",DataTypes.String,"");
        Static.PvPTable.AddColumn("enabled",DataTypes.Boolean,true);

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new HitListener(), this);

        this.getCommand("sethome").setExecutor(new SetHomeCommand());
        this.getCommand("sethome").setTabCompleter(new HomeTabCompleter());
        this.getCommand("delhome").setExecutor(new DelHomeCommand());
        this.getCommand("delhome").setTabCompleter(new HomeTabCompleter());
        this.getCommand("home").setExecutor(new HomeCommand());
        this.getCommand("home").setTabCompleter(new HomeTabCompleter());
        this.getCommand("pvpon").setExecutor(new PvpOnCommand());
        this.getCommand("pvpon").setTabCompleter(new HomeTabCompleter());
        this.getCommand("pvpoff").setExecutor(new PvpOffCommand());
        this.getCommand("pvpoff").setTabCompleter(new HomeTabCompleter());

        this.getLogger().info("UniHome (c) 2025 Enderbyte Programs, no rights reserved. Plugin initialized.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Goodbye!");
    }
}
