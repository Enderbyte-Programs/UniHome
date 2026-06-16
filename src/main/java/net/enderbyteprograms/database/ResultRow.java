package net.enderbyteprograms.database;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Program Title: REsult Row
 * 
 * Program Summary: An extended map class that assists with DB results
 * 
 * Program Elements List:
 * Extends
 * super
 * Sets, maps
 * Dates
 * schema tracking
 * typed getters
 * 
 * @author Jordan Rahim
 * @version 1
 */
public class ResultRow extends HashMap<String,Object> {//begin class

    //vars
    public Map<String,DataTypes> schema;





    /**
     * Summary: Create a new result row from a map
     * @param inmap the map going in
     * @param cols the coolumn schema of this map
     * @return the object
     */
    public ResultRow(Map<String,Object> inmap,Map<String,DataTypes> cols,String tableName) {//begin constructor

        super(inmap);//Sorry Mr. Vatougios. Java crashes if this isn't the first line. Variables will have to be delayed to later.
        
        //vars
        Set<Entry<String,DataTypes>> originalCols;
        Map<String,DataTypes> copiedCols;//I had to do this because otherwise it mods the original copy.

        originalCols = new HashSet<Entry<String,DataTypes>>(cols.entrySet());//Prevent concurrentmodificationexception
        copiedCols = new HashMap<String,DataTypes>(cols);

        for (Entry<String,DataTypes> schemaEntry:originalCols) {//begin for

            copiedCols.put(tableName+"."+schemaEntry.getKey(),schemaEntry.getValue());//Add namespace for columns too

        }//end for

        schema = copiedCols;

        //Add associated namespaced keys
        for (Entry<String,Object> inboundEntry:inmap.entrySet()) {//begin for

            this.put(tableName+"."+inboundEntry.getKey(),inboundEntry.getValue());

        }//end for

    }//end constructor






    /**
     * Summary: Create a new result row WITHOUT namespacing. This is useful for cloning an existing resultrow.
     * @param inmap the map of data, untyped
     * @param columns the map of columns to show what type they should be
     * @return resultrow object
     */
    public ResultRow(Map<String,Object> inmap, Map<String,DataTypes> columns) {//begin constructor

        super(inmap);//The actual cloning
        schema = columns;

    }//end constructor





    /**
     * SUmmary: Create a new empty result row from a schema
     * @param cols the schema
     * @return the object
     */
    public ResultRow(Map<String,DataTypes> cols) {//begin constructor

        super();
        schema = cols;

    }//end constructor




    
    /**
     * Summary: Add an entry to this row with an unnamespaced and namespaced entry. This may overwrite other data, particularly in the unnamespaced entry
     * @param columnName The un-namespaced local column name
     * @param value the value to insert
     * @param namespace the namespace (usually the table name)
     * @return nothing
     */
    public void putNamespaced(String columnName, Object value, String namespace) {//begin method

        this.put(columnName,value);
        this.put(namespace+"."+columnName,value);

    }//end method





    /**
     * Summary: Get an integer based on the column name
     * @param name the column name
     * @return the value
     */
    public int getInt(String name) {//begin method

        return (Integer)this.get(name);

    }//end methodf





    /**
     * Summary: Get a boolean based on the column name
     * @param name the column name
     * @return the value
     */
    public boolean getBool(String name) {//begin method

        return (Boolean)this.get(name);

    }//end methodf





    /**
     * Summary: Get a double based on the column name
     * @param name the column name
     * @return the value
     */
    public double getDouble(String name) {//begin method

        return (Double)this.get(name);

    }//end methodf





    /**
     * Summary: Get a string based on the column name
     * @param name the column name
     * @return the value
     */
    public String getString(String name) {//begin method

        return (String)this.get(name);

    }//end methodf






    /**
     * Summary: Get a date based on the column name
     * @param name the column name
     * @return the value
     */
    public Date getDateTime(String name) {//begin method

        return (Date)this.get(name);

    }//end methodf


    
}//end class
/**
 * Notes: This is going to have some stuff like getInt, getBool, etc, so that the types are all good.
 * 
 * 
 * Test Code: public Date getDateTime(String name) {//begin method

        return new Date(this.get(name));
        This is a broken version that was fixed

    }//end methodf
 * 
 */
