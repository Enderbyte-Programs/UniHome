package net.enderbyteprograms.database;

import java.util.Map;

/**
 * Program Title: Updater interface
 * 
 * Program Summary: Provide a way for the user to write custom updaters in addition to the normal one
 * 
 * Program Element List:
 * interface
 * map
 */
public interface CustomUpdater {//begin interface

    /**
     * Summary: Update the provided inbound row, returning the updated row
     * @param inboundRow the un-updated row which has been selected for updating
     * @return the updated row.
     */
    public Map<String,Object> write(Map<String,Object> inboundRow);
    
}//end interface

/*
Test codeL ntohig



Notes; Nothing
*/