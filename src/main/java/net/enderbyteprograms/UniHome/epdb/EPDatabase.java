package net.enderbyteprograms.UniHome.epdb;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class EPDatabase {
    private File DataFolderPath;
    public EPDatabase(JavaPlugin j) {
        File dpp = j.getDataFolder();
        DataFolderPath = new File(dpp,"data");
        if (!DataFolderPath.exists()) {
            DataFolderPath.mkdirs();
        }
    }
    public Table GetTable(String name) {
        return new Table(DataFolderPath,name);
    }
}
