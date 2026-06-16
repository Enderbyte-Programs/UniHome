package net.enderbyteprograms.database;

import java.util.Map;


/**
 * Program Title: Comparison interface
 * 
 * Program Summary: an interface to permit the writing of custom comparison operatives in addition to the default and or not equals one
 * 
 * Program Elements List:
 * interface
 * maps
 * 
 * @author Jordan Rahim
 * @version 1
 */

public interface CustomComparison {//begin interface

    /**
     * Summary: Does the row match the filter you are trying to implement?
     * @param row the row that is being evaluated
     * @return true if the row matches, false if the row does not match
     */
    public boolean shouldAccept(Map<String,Object> row);
    
}//end interface

/*
Notes: There aren't any









Test code:
Nothing as well... It's an interface. They are so simple
private interface? Not going to work
*/