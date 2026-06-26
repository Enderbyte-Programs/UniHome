package net.enderbyteprograms.unihome;

import com.google.common.collect.HashBiMap;
import net.enderbyteprograms.Utilities;
import net.enderbyteprograms.database.ResultRow;
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
import java.util.concurrent.ConcurrentHashMap;

public class UniHomeMain extends JavaPlugin {
    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        PatchMaster.Patch(this);

        Data.configuration = this.getConfig();
        Data.plugin = this;

        //Set up the newer database
        Data.db = new Database(this.getDataFolder().getAbsolutePath() + "/datatables/",false);
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
        Data.playerInformation = new ConcurrentHashMap<>();

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
            for (ResultRow nameRow:Data.nameAliasTable.select()) {
                PlayerInfo newProfile = new PlayerInfo();
                newProfile.uuid = nameRow.getString("uuid");
                newProfile.name = nameRow.getString("name");
                newProfile.comparableName = nameRow.getString("nname");

                if (newProfile.name.startsWith("*UNKNOWN")) {
                    newProfile.name = "#unknown_"+ Utilities.getRandomInt(10000,99999);//No longer allowed to have duplicates, sorry
                    newProfile.comparableName = newProfile.name;
                }

                try {

                    Data.playerInformation.put(UUID.fromString(newProfile.uuid),newProfile);
                    Data.nameCapitalizationMappings.put(newProfile.name,newProfile.comparableName);
                    Data.uuidToNameMappings.forcePut(UUID.fromString(newProfile.uuid),newProfile.name);

                } catch (Exception e) {

                }
            }

            Data.nameAliasTable.delete();
            Data.nameAliasTable.saveData();

            for (ResultRow homeRow:Data.homeTable.select()) {
                UUID uuid = UUID.fromString(homeRow.getString("uuid"));

                PlayerInfo currentProfile = Data.playerInformation.get(uuid);
                currentProfile.homeWorld = homeRow.getString("world");
                currentProfile.homeX = homeRow.getDouble("x");
                currentProfile.homeY = homeRow.getDouble("y");
                currentProfile.homeZ = homeRow.getDouble("z");

            }
            Data.homeTable.delete();
            Data.homeTable.saveData();

            for (ResultRow pvpRow:Data.pvpTable.select()) {
                UUID uuid = UUID.fromString(pvpRow.getString("uuid"));

                PlayerInfo currentProfile = Data.playerInformation.get(uuid);
                currentProfile.pvpEnabled = pvpRow.getBool("enabled");
            }

            Data.pvpTable.delete();
            Data.pvpTable.delete();
            Data.joinTimeTable.delete();
            Data.joinTimeTable.delete();

            Data.playerInfoFile.write(Data.playerInformation.values());

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

        Data.isAprilFoolsRunning = Data.configuration.getBoolean("run-april-fools-2026");


        SecondlyTimer secondlyTimer = new SecondlyTimer();
        secondlyTimer.runTaskTimer(this,1L,20L);
        DataSaverTimer saveTimer = new DataSaverTimer();
        saveTimer.runTaskTimerAsynchronously(this,1L,200L);//Autosave every 10 seconds. Subject to change if it is struggling


        this.getLogger().info("UniHome (c) 2025-2026 Enderbyte Programs, no rights reserved. Plugin initialized.");
    }

    @Override
    public void onDisable() {
        synchronized (Data.playerInformationLock) {
            Data.playerInfoFile.write(Data.playerInformation.values());
        }
        this.getLogger().info("Goodbye!");
    }
}
