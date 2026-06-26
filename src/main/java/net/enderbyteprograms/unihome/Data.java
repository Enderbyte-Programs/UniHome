package net.enderbyteprograms.unihome;

import com.google.common.collect.BiMap;
import net.enderbyteprograms.unihome.structures.PlayerInfo;
import net.enderbyteprograms.unihome.structures.SizeTransition;
import net.enderbyteprograms.database.Database;
import net.enderbyteprograms.database.Table;
import net.enderbyteprograms.sjo.SerializedJavaObjectFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Data {
    public static FileConfiguration configuration;
    public static JavaPlugin plugin;

    //BEGIN DEPRECATED STUFF
    public static Database db;
    public static Table homeTable;
    public static Table pvpTable;
    public static Table nameAliasTable;
    public static Table joinTimeTable;
    //END DEPRECATED STUFF

    //Light data storage? I will be very upset if my 3rd data storage fix attempt goes wrong
    public static BiMap<UUID,String> uuidToNameMappings;
    public static BiMap<String,String> nameCapitalizationMappings;
    public static ConcurrentHashMap<UUID, PlayerInfo> playerInformation;

    public static SerializedJavaObjectFile<PlayerInfo> playerInfoFile;
    public static final Object playerInformationLock = new Object();

    public static String VersionString = "unihome Mark 5 Patch 0 (c) 2025-2026 Enderbyte Programs";
    public static List<SizeTransition> activeTransitions = new ArrayList<SizeTransition>();
    public static boolean isAprilFoolsRunning;
    public static int aprilFoolsTimer;

    public static UUID getUUIDFromName(String username) {

        try {
            String trueUsername = nameCapitalizationMappings.inverse().get(username.toLowerCase());
            return uuidToNameMappings.inverse().get(trueUsername);
        } catch (Exception e) {
            return null;
        }

    }

    public static String getNameFromUUID(UUID uuid) {
        try {
            return uuidToNameMappings.get(uuid);
        } catch (Exception e) {
            return null;
        }
    }

}
