package net.enderbyteprograms.UniHome;

import net.enderbyteprograms.UniHome.epdb.Table;
import net.enderbyteprograms.UniHome.structures.SizeTransition;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Static {
    public static FileConfiguration Configuration;
    public static JavaPlugin Plugin;
    public static Table HomeTable;
    public static Table PvPTable;
    public static String VersionString = "UniHome Mark 3 Patch 0 (c) 2025 Enderbyte Programs";
    public static List<SizeTransition> activeTransitions = new ArrayList<SizeTransition>();
    public static boolean isAprilFoolsRunning;
    public static int aprilFoolsTimer;

}
