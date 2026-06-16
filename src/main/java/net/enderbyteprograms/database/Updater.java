package net.enderbyteprograms.database;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Program Title: Static Updater for Database Rows
 * 
 * Program Summary: Defines a simple direct replace operation for an arbitrary number of rows 
 * 
 * Program Elements List:
 * Map
 * Linkedhashmap
 * Implementing
 * 
 * @author Jordan Rahim
 * @version 2
 */
public class Updater implements CustomUpdater {//begin class

    public Map<String,Object> valuesToWrite;





    /**
     * Summary: Create a new updater with a single replace
     * @param columnName the column name to replace
     * @param value with what value
     * @return the object
     */
    public Updater(String columnName, Object value) {//begin constructor

        valuesToWrite = new LinkedHashMap<String,Object>();
        valuesToWrite.put(columnName,value);

    }//end constructor





    /**
     * Summary: Add another updater to directly set a column to a new value, chaining
     * @param columnName the column name to replace
     * @param value with what value
     * @return the same object, to allow chaining
     */
    public Updater add(String columnName,Object value) {//begin method

        valuesToWrite.put(columnName,value);

        return this;

    }//end method





    /**
     * Summary: Apply the updates stored in this class to a row of data
     * @param inboundRow the row of data
     * @return the updated row of data
     */
    @Override
    public Map<String,Object> write(Map<String,Object> inboundRow) {//begin method

        for (Entry<String,Object> requestedUpdate:valuesToWrite.entrySet()) {//begin for

            inboundRow.put(requestedUpdate.getKey(),requestedUpdate.getValue());

        }//end for

        return inboundRow;//Modified

    }//end method
    
}//end class
/*
NOTES:
Let's hope this works...




TEST CODE:
@Override
    public Map<String,Object> write(Map<String,Object> inboundRow) {//begin method

        for (Entry<String,Object> requestedUpdate:valuesToWrite.entrySet()) {//begin for

            inboundRow.put(requestedUpdate.getKey(),requestedUpdate.getValue());

        }//end for

        return inboundRow;//Modified

    }//end method
*/