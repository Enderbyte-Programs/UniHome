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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public class UniHomeMain extends JavaPlugin {
    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        PatchMaster.Patch(this);

        Data.Configuration = this.getConfig();
        Data.Plugin = this;
        Data.oldHomeTable = new EPDatabase(this).GetTable("homes");
        Data.oldHomeTable.AddColumn("uuid", net.enderbyteprograms.UniHome.epdb.DataTypes.String,"");
        Data.oldHomeTable.AddColumn("location",net.enderbyteprograms.UniHome.epdb.DataTypes.String,"");
        Data.oldPVPTable = new EPDatabase(this).GetTable("pvp");
        Data.oldPVPTable.AddColumn("uuid",net.enderbyteprograms.UniHome.epdb.DataTypes.String,"");
        Data.oldPVPTable.AddColumn("enabled",net.enderbyteprograms.UniHome.epdb.DataTypes.Boolean,true);

        //Set up the newer database
        Data.db = new Database(this.getDataFolder().getAbsolutePath() + "/datatables/",false);
        Data.db.assertTable("names",false);
        Data.db.assertTable("homes",false);
        Data.db.assertTable("pvp",false);

        Data.nameAliasTable = Data.db.getTable("names");
        Data.pvpTable = Data.db.getTable("pvp");
        Data.homeTable = Data.db.getTable("homes");

        Data.nameAliasTable.addColumn(DataTypes.MediumString,"name","*UNKNOWN");
        Data.nameAliasTable.addColumn(DataTypes.MediumString,"nname","*UNKNOWN");
        Data.nameAliasTable.addColumn(DataTypes.LargerString,"uuid","0");

        Data.pvpTable.addColumn(DataTypes.LargerString,"uuid","0");
        Data.pvpTable.addColumn(DataTypes.Boolean,"enabled",true);

        Data.homeTable.addColumn(DataTypes.LargerString,"uuid","0");
        Data.homeTable.addColumn(DataTypes.MediumString,"world","0");
        Data.homeTable.addColumn(DataTypes.Double,"x",0D);
        Data.homeTable.addColumn(DataTypes.Double,"y",0D);
        Data.homeTable.addColumn(DataTypes.Double,"z",0D);

        if (Data.oldPVPTable.GetAll().size() > 0) {
            this.getLogger().info("Migrating data");
            for (Map<String,Object> pvpObject: Data.oldPVPTable.GetAll()) {
                String uuid = pvpObject.get("uuid").toString();
                Data.nameAliasTable.insert(Map.of("name","*UNKNOWN","uuid",uuid,"nname","*UNKNOWN"));
                Data.pvpTable.insert(Map.of("uuid",uuid,"enabled",(boolean)pvpObject.get("enabled")));
            }

            for (Map<String,Object> homeObject: Data.oldHomeTable.GetAll()) {
                String[] oldFormatHome = homeObject.get("location").toString().split(":");
                String worldname = oldFormatHome[0];
                double x = Double.parseDouble(oldFormatHome[1]);
                double y = Double.parseDouble(oldFormatHome[2]);
                double z = Double.parseDouble(oldFormatHome[3]);

                Data.homeTable.insert(Map.of("uuid",homeObject.get("uuid").toString(),"world",worldname,"x",x,"y",y,"z",z));
            }

            Data.oldPVPTable.Clear();
            Data.oldHomeTable.Clear();
        }

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

        Data.isAprilFoolsRunning = Data.Configuration.getBoolean("run-april-fools-2026");

        if (Data.isAprilFoolsRunning) {
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
