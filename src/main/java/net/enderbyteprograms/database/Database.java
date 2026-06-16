package net.enderbyteprograms.database;

import java.io.File;
import java.util.HashMap;

import net.enderbyteprograms.database.adapters.FileSystemAdapter;
import net.enderbyteprograms.database.adapters.InMemoryAdapter;

/**
 * Program Title: Database
 * 
 * Program Summary: Master class for database
 * 
 * Program Elements List:
 * - Files
 * - Hashmaps
 * IO Adapters
 * Memory adapter
 * FS adapter
 * File processing
 * User customization
 * 
 * @author jordan rahim
 * @verion 2
 */

public class Database {//begin class

    public File dbFolder;
    public HashMap<String,Table> tables;





    /**
     * Summary: Create a new database with the default folder for tables ("data"), loading tables if they exist
     * @param inMemoryEditing should loaded tables be edited in memory or in place on the disk?
     * @return the database object.
     */
    public Database(boolean inMemoryEditing) {//begin constructor

        this("data",inMemoryEditing);

    }//end constructor

    
    
    
    
    /**
     * Summary: Create a new database, loading tables if they exist
     * @param tableFolder the relative folder to use for tables.
     * @return the db object
     */
    public Database(String tableFolder,boolean inMemoryEditing) {//begin constructor

        String tableFileName;
        Table currentTable;

        dbFolder = new File(tableFolder);
        tables = new HashMap<String,Table>();
        
        if (!dbFolder.exists()) {//begin if

            dbFolder.mkdirs();

        }//end if

        
        for (File tableFile:dbFolder.listFiles()) {//begin for

            if (tableFile.getName().endsWith("tbl")) {//begin if

                tableFileName = tableFile.getName().split("\\.")[0];//Wowowwow that'szalot of methods

                if (inMemoryEditing) {//begin if

                    //Faster, but more unstable I would think (FS-IM)

                    try {//begin try

                        currentTable = new Table(this,tableFile,new InMemoryAdapter());

                    } catch (Exception e) {//end try begin catch

                        currentTable = new Table(this,tableFileName,new InMemoryAdapter(),true);//Usefilesystem is true because the user would want to save this

                    }//end catch

                } else {//end if begin else

                    //Traditional on filesystem behaviour (FS-FS)

                    try {//begin try

                        currentTable = new Table(this,tableFile,new FileSystemAdapter());

                    } catch (Exception e) {//end try begin catch

                        currentTable = new Table(this,tableFileName,new FileSystemAdapter(),true);

                    }//end catch

                }//end else

                tables.put(tableFileName,currentTable);

            }//end if

        }//end for

    }//end constructor




    /**
     * Summary: Create an empty table if tablename does not exist. Does nothing if the table already exists. Returns whether or not the table was created new. THis method should be called on each table you want to use. getTable will return an error if the table does not exist.
     * @param tableName the name of the table to check
     * @return TRUE if the table already existed, FALSE if the table is now created as empty.
     */
    public boolean assertTable(String tableName,boolean useInMemory) {//begin method

        boolean tableExists;

        tableExists = tables.containsKey(tableName);

        if (!tableExists) {//begin if

            if (useInMemory) {//begin if

                tables.put(tableName,new Table(this,tableName,new InMemoryAdapter(),true));//Add a new empty table with editing in memory

            } else {//end if begin else

                tables.put(tableName,new Table(this,tableName,new FileSystemAdapter(),true));//Add a new empty table

            }//end else

            return false;

        }//end if

        return true;

    }//end method




    /**
     * Summary: Create an in memory table that will survive only as long as the program runs
     * @param tableName The name of this table. It MUST NOT conflict with any loaded table.
     * @param editInMemory Should this table also be edited in memory? You probably want to set this as true except in very specific circumstances
     */
    public Table createInMemoryTable(String tableName,boolean editInMemory) {//begin method

        if (editInMemory) {//begin if

            tables.put(tableName,new Table(this,tableName,new InMemoryAdapter(),false));

        } else {//end if begin else

            tables.put(tableName,new Table(this,tableName,new FileSystemAdapter(),false));

        }//end else

        return getTable(tableName);

    }//end methood





    /**
     * Summary: Prepare the database for shutdown by saving and closing all tables
     * @param nothing
     * @return nothing
     */
    public void shutdownDatabase() {//begin method

        for (Table table:tables.values()) {//begin for

            table.finish();

        }//end for

    }//end method





    /**
     * Summary: Close and delete an in-memory table
     * @param tableName the name of the table to delete
     * @return nothing
     */
    public void deleteInMemoryTable(String tableName) {//begin method

        getTable(tableName).finish();
        tables.remove(tableName);

    }//end method





    /**
     * Summary: Get a table by name
     * @param name the name of the table
     * @return the table
     */
    public Table getTable(String name) {//begin method
    
        return tables.get(name);

    }//end method
    
}//end class

/**
 * Notes:
 * 
 * Test Code:
 * 
 * public boolean assertTable(String tableName) {//begin method

        boolean tableExists;

        tableExists = tables.containsKey(tableName);

        if (!tableExists) {//begin if

            tables.put(tableName,new Table(this,tableName));//Add a new empty table
            return false;

        }//end if

        return true;

    }//end method

    This design is better than add table because I don't need to check
 * 
 */