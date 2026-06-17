package net.enderbyteprograms.UniHome;

import net.enderbyteprograms.UniHome.structures.SizeTransition;
import net.enderbyteprograms.database.Comparison;
import net.enderbyteprograms.database.Database;
import net.enderbyteprograms.database.ResultSet;
import net.enderbyteprograms.database.Table;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Data {
    public static FileConfiguration Configuration;
    public static JavaPlugin Plugin;

    //BEGIN DEPRECATED STUFF
    public static net.enderbyteprograms.UniHome.epdb.Table oldHomeTable;
    public static net.enderbyteprograms.UniHome.epdb.Table oldPVPTable;
    //END DEPRECATED STUFF

    public static Database db;
    public static Table homeTable;
    public static Table pvpTable;
    public static Table nameAliasTable;
    public static String VersionString = "UniHome Mark 4 Patch 0 (c) 2025-2026 Enderbyte Programs";
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

}
