package net.enderbyteprograms.unihome;

import com.google.common.collect.HashBiMap;
import net.enderbyteprograms.unihome.commands.*;
import net.enderbyteprograms.unihome.listeners.HitListener;
import net.enderbyteprograms.unihome.listeners.JoinListener;
import net.enderbyteprograms.unihome.listeners.KillListener;
import net.enderbyteprograms.unihome.timers.DataSaverTimer;
import net.enderbyteprograms.unihome.timers.SecondlyTimer;
import net.enderbyteprograms.unihome.patch.PatchMaster;
import net.enderbyteprograms.unihome.structures.PlayerInfo;
import net.enderbyteprograms.database.DataTypes;
import net.enderbyteprograms.database.Database;
import net.enderbyteprograms.sjo.SerializedJavaObjectFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

public class UniHomeMain extends JavaPlugin {
    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        PatchMaster.Patch(this);

        Data.Configuration = this.getConfig();
        Data.Plugin = this;

        //Set up the newer database
        Data.db = new Database(this.getDataFolder().getAbsolutePath() + "/datatables/",true);
        Data.db.assertTable("names",false);
        Data.db.assertTable("homes",false);
        Data.db.assertTable("pvp",false);
        Data.db.assertTable("joindate",false);

        Data.nameAliasTable = Data.db.getTable("names");
        Data.pvpTable = Data.db.getTable("pvp");
        Data.homeTable = Data.db.getTable("homes");
        Data.joinTimeTable = Data.db.getTable("joindate");

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

        Data.joinTimeTable.addColumn(DataTypes.LargerString,"uuid","0");
        Data.joinTimeTable.addColumn(DataTypes.Integer,"epochdays",0);
        Data.joinTimeTable.addColumn(DataTypes.Integer,"lastseen",0);

        //Data storage V3 setup
        Data.nameCapitalizationMappings = HashBiMap.create();
        Data.uuidToNameMappings = HashBiMap.create();
        Data.playerInformation = new HashMap<>();

        Data.playerInfoFile = new SerializedJavaObjectFile<>(new File(getDataFolder().getAbsoluteFile().toString() + "/playerinfo.sjo"));

        int inc = 0;
        for (PlayerInfo pi:Data.playerInfoFile.read()) {
            Data.playerInformation.put(UUID.fromString(pi.uuid),pi);
            Data.nameCapitalizationMappings.put(pi.name,pi.comparableName);
            Data.uuidToNameMappings.forcePut(UUID.fromString(pi.uuid),pi.name);
            inc++;
        }
        getLogger().info(String.format("Loaded %d objects from storage",inc));

        if (Data.nameAliasTable.getTableLength() > 0) {
            getLogger().info("Migrating from EPDB2 to SJO");

            //Make empty profiles for each user

        }

        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new HitListener(), this);
        getServer().getPluginManager().registerEvents(new KillListener(),this);

        this.getCommand("sethome").setExecutor(new SetHomeCommand());
        this.getCommand("sethome").setTabCompleter(new GenericOnlinePlayersTabCompleter());
        this.getCommand("delhome").setExecutor(new DelHomeCommand());
        this.getCommand("delhome").setTabCompleter(new GenericOnlinePlayersTabCompleter());
        this.getCommand("home").setExecutor(new HomeCommand());
        this.getCommand("home").setTabCompleter(new GenericOnlinePlayersTabCompleter());
        this.getCommand("pvpon").setExecutor(new PvpOnCommand());
        this.getCommand("pvpon").setTabCompleter(new GenericOnlinePlayersTabCompleter());
        this.getCommand("pvpoff").setExecutor(new PvpOffCommand());
        this.getCommand("pvpoff").setTabCompleter(new GenericOnlinePlayersTabCompleter());
        this.getCommand("uhact").setExecutor(new SpecialAdminCommand());
        this.getCommand("uhact").setTabCompleter(new SpecialAdminCommandTabCompleter());
        this.getCommand("getnearby").setExecutor(new GetNearbyCommand());
        this.getCommand("playtime").setExecutor(new PlaytimeCommand());
        this.getCommand("playtime").setTabCompleter(new GenericOnlinePlayersTabCompleter());
        this.getCommand("topplaytime").setExecutor(new PlaytimeLeaderboardCommand());

        Data.isAprilFoolsRunning = Data.Configuration.getBoolean("run-april-fools-2026");


        SecondlyTimer secondlyTimer = new SecondlyTimer();
        secondlyTimer.runTaskTimer(this,1L,20L);
        DataSaverTimer saveTimer = new DataSaverTimer();
        saveTimer.runTaskTimerAsynchronously(this,1L,200L);//Autosave every 10 seconds. Subject to change if it is struggling


        this.getLogger().info("unihome (c) 2025-2026 Enderbyte Programs, no rights reserved. Plugin initialized.");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Goodbye!");
        Data.pvpTable.finish();
        Data.nameAliasTable.finish();
        Data.homeTable.finish();
        Data.joinTimeTable.finish();
    }
}
