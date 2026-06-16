package net.enderbyteprograms.UniHome;

import net.enderbyteprograms.UniHome.commands.*;
import net.enderbyteprograms.UniHome.epdb.EPDatabase;
import net.enderbyteprograms.UniHome.listeners.HitListener;
import net.enderbyteprograms.UniHome.listeners.JoinListener;
import net.enderbyteprograms.UniHome.listeners.KillListener;
import net.enderbyteprograms.UniHome.listeners.SizeChangeTimer;
import net.enderbyteprograms.UniHome.patch.PatchMaster;
import net.enderbyteprograms.database.DataTypes;
import net.enderbyteprograms.database.Database;
import net.enderbyteprograms.database.Table;
import org.bukkit.plugin.java.JavaPlugin;

public class UniHomeMain extends JavaPlugin {
    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        PatchMaster.Patch(this);

        Static.Configuration = this.getConfig();
        Static.Plugin = this;
        Static.oldHomeTable = new EPDatabase(this).GetTable("homes");
        Static.oldHomeTable.AddColumn("uuid", net.enderbyteprograms.UniHome.epdb.DataTypes.String,"");
        Static.oldHomeTable.AddColumn("location",net.enderbyteprograms.UniHome.epdb.DataTypes.String,"");
        Static.oldPVPTable = new EPDatabase(this).GetTable("pvp");
        Static.oldPVPTable.AddColumn("uuid",net.enderbyteprograms.UniHome.epdb.DataTypes.String,"");
        Static.oldPVPTable.AddColumn("enabled",net.enderbyteprograms.UniHome.epdb.DataTypes.Boolean,true);

        //Set up the newer database
        Static.db = new Database(this.getDataFolder().getAbsolutePath() + "/datatables/",false);
        Static.db.assertTable("names",false);
        Static.db.assertTable("homes",false);
        Static.db.assertTable("pvp",false);

        Static.nameAliasTable = Static.db.getTable("names");
        Static.pvpTable = Static.db.getTable("pvp");
        Static.homeTable = Static.db.getTable("homes");

        Static.nameAliasTable.addColumn(DataTypes.MediumString,"name","Jimmy Johnson");
        Static.nameAliasTable.addColumn(DataTypes.LargerString,"uuid","0");

        Static.pvpTable.addColumn(DataTypes.LargerString,"uuid","0");
        Static.pvpTable.addColumn(DataTypes.Boolean,"enabled",true);

        Static.homeTable.addColumn(DataTypes.LargerString,"uuid","0");
        Static.homeTable.addColumn(DataTypes.MediumString,"worldname","0");
        Static.homeTable.addColumn(DataTypes.Double,"x",0D);
        Static.homeTable.addColumn(DataTypes.Double,"y",0D);
        Static.homeTable.addColumn(DataTypes.Double,"z",0D);



        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new HitListener(), this);
        getServer().getPluginManager().registerEvents(new KillListener(),this);

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
        this.getCommand("uhreload").setExecutor(new UHReloadCommand());
        this.getCommand("getnearby").setExecutor(new GetNearbyCommand());

        Static.isAprilFoolsRunning = Static.Configuration.getBoolean("run-april-fools-2026");

        if (Static.isAprilFoolsRunning) {
            SizeChangeTimer aprilFoolsTimer = new SizeChangeTimer();
            aprilFoolsTimer.runTaskTimer(this,1L,20L);
        }

        this.getLogger().info("UniHome (c) 2025-2026 Enderbyte Programs, no rights reserved. Plugin initialized.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Goodbye!");
    }
}
