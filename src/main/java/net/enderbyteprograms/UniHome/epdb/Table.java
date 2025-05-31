package net.enderbyteprograms.UniHome.epdb;

import net.enderbyteprograms.UniHome.Static;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Table {
    private List<HashMap<String,Object>> Data = new ArrayList<>();
    private File filepath;
    private String tablename;
    private HashMap<String,DataTypes> Columns = new HashMap<>();
    public Table(File rootpath,String name){
        tablename = name;
        filepath = new File(rootpath,tablename+".ept");
        if (!filepath.exists()) {
            try {
                filepath.createNewFile();
                Files.writeString(filepath.toPath(),"||||");
            } catch (IOException e) {
                //Impossible situation
            }
        } else {
            //Attempt to load data from the table
            String rawdata = "";
            try {
                rawdata = Files.readString(filepath.toPath());
            } catch (IOException e) {

            }
            String head = rawdata.split("\\|\\|\\|\\|\\n")[0];
            for (String columnarentry:head.split(";")) {
                if (columnarentry.isBlank()) {
                    continue;
                }
                String k = columnarentry.split(" ")[0];
                int v = Integer.parseInt(columnarentry.split(" ")[1]);
                DataTypes dt = DataTypes.String;
                if (v == 0) {
                    dt = DataTypes.String;
                } else if (v == 1) {
                    dt = DataTypes.Number;
                } else if (v == 2) {
                    dt = DataTypes.Boolean;
                }
                Columns.put(k,dt);
            }
            String body = rawdata.split("\\|\\|\\|\\|\\n")[1];
            String[] lines = body.split("\\r?\\n");
            for (String line:lines) {
                if (line.isBlank()) {
                    continue;
                }
                HashMap<String,Object> currentline = new HashMap<>();
                for (String section:line.split("\\|")) {
                    if (section.isBlank()) {
                        continue;
                    }
                    String[] kv = section.split("\\$");
                    String k = kv[0];
                    if (!Columns.containsKey(k)) {
                        continue;
                    }
                    DataTypes vdt = Columns.get(k);
                    String v = new String(Base64.getDecoder().decode(kv[1]), Charset.defaultCharset());
                    Object ov = v;
                    if (vdt == DataTypes.Number) {
                        ov = Double.parseDouble(v);
                    } else if (vdt == DataTypes.Boolean) {
                        ov = Double.parseDouble(v) == 1D;//Either 0D or 1D
                    }
                    currentline.put(k,ov);
                }
                Data.add(currentline);
            }
        }
    }

    public void Save() throws IOException {
        //Static.Plugin.getLogger().info("Saving data");
        String finald = "";

        for (Map.Entry<String,DataTypes> ce:Columns.entrySet()) {
            String k = ce.getKey();
            String v = String.valueOf(ce.getValue().toNum(ce.getValue()));
            finald += k;
            finald += " ";
            finald += v;
            finald += ";";
        }

        finald += "||||\n";

        for (HashMap<String,Object> parsedline:Data) {
            for (Map.Entry<String,Object> entry: parsedline.entrySet()) {
                String k = entry.getKey();
                if (!Columns.containsKey(k)) {
                    continue;
                }
                DataTypes vdt = Columns.get(k);
                String v = String.valueOf(entry.getValue());
                finald += k;
                finald += "$";
                String vv = Base64.getEncoder().encodeToString(v.getBytes(StandardCharsets.UTF_8));
                if (vv.isBlank()) {
                    continue;
                }
                finald += vv;
                finald += "\n";
            }
        }
        //Static.Plugin.getLogger().info(finald);
        if (filepath.exists()) {
            Files.delete(filepath.toPath());
            filepath.createNewFile();
        }
        Files.writeString(filepath.toPath(),finald,Charset.defaultCharset(), StandardOpenOption.WRITE);
    }

    public List<HashMap<String,Object>> GetAll() {
        return Data;
    }
    public List<HashMap<String,Object>> GetAll(String sortkey) {
        DataTypes sdt = Columns.get(sortkey);
        List<HashMap<String,Object>> _d = new ArrayList<>();
        _d.sort((s1,s2) -> {
            if (sdt == DataTypes.String) {
                return ((String)(s1.get(sortkey))).compareTo((String)(s2.get(sortkey)));
            } else if (sdt == DataTypes.Number) {
                return ((Double)(s1.get(sortkey))).compareTo((Double)(s2.get(sortkey)));
            } else {
                return ((Boolean)(s1.get(sortkey))).compareTo((Boolean) (s2.get(sortkey)));
            }
        });
        return _d;
    }

    public void Clear() {
        Data = new ArrayList<>();
        try {
            Save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void DeleteWhere(String finderkey,Object deletewhere) {

        int doffset = 0;
        for (int i = 0; i<Data.toArray().length; i++) {
            HashMap<String,Object> sel = Data.get(i-doffset);
            if (sel.get(finderkey).equals(deletewhere)) {
                Data.remove(i-doffset);
                doffset += 1;
            }
        }

        try {
            Save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Insert(HashMap<String,Object> o) {
        Data.add(o);
        try {
            Save();
        } catch (IOException e) {

        }
    }

    public void AddColumn(String name,DataTypes dt,Object defaultvalue) {
        Columns.put(name,dt);
        for (HashMap<String,Object> d:Data) {
            if (!d.containsKey(name)) {
                if (dt == DataTypes.String) {
                    d.put(name,defaultvalue);
                }
                else if (dt == DataTypes.Number) {
                    d.put(name,defaultvalue);
                } else {
                    d.put(name,defaultvalue);
                }
            }
        }
    }
}
