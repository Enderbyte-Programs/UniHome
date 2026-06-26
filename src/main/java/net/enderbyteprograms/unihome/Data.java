package net.enderbyteprograms.unihome;

import com.google.common.collect.BiMap;
import net.enderbyteprograms.unihome.structures.PlayerInfo;
import net.enderbyteprograms.unihome.structures.SizeTransition;
import net.enderbyteprograms.database.Comparison;
import net.enderbyteprograms.database.Database;
import net.enderbyteprograms.database.ResultSet;
import net.enderbyteprograms.database.Table;
import net.enderbyteprograms.sjo.SerializedJavaObjectFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Data {
    public static FileConfiguration Configuration;
    public static JavaPlugin Plugin;

    //BEGIN DEPRECATED STUFF
    public static Database db;
    public static Table homeTable;
    public static Table pvpTable;
    public static Table nameAliasTable;
    public static Table joinTimeTable;
    //END DEPRECATED STUFF

    //Light data storage?
    public static BiMap<UUID,String> uuidToNameMappings;
    public static BiMap<String,String> nameCapitalizationMappings;
    public static HashMap<UUID, PlayerInfo> playerInformation;

    public static SerializedJavaObjectFile<PlayerInfo> playerInfoFile;
    public static final Object playerInformationLock = new Object();

    public static String VersionString = "unihome Mark 5 Patch 0 (c) 2025-2026 Enderbyte Programs";
    public static List<SizeTransition> activeTransitions = new ArrayList<SizeTransition>();
    public static boolean isAprilFoolsRunning;
    public static int aprilFoolsTimer;

    public static UUID getUUIDFromName(String username) {

        ResultSet tgt = nameAliasTable.select(new Comparison("nname",username.toLowerCase(),false));

        if (tgt.size() == 0) {
            return null;
        } else {
            return UUID.fromString(tgt.get(0).getString("uuid"));
        }

    }

    public static String getNameFromUUID(UUID uuid) {
        ResultSet tgt = nameAliasTable.select(new Comparison("uuid",uuid.toString(),false));

        if (tgt.isEmpty()) {
            return null;
        } else {
            return tgt.get(0).getString("name");
        }
    }

}
