package net.enderbyteprograms.database.scripting;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import net.enderbyteprograms.Utilities;
import net.enderbyteprograms.database.Database;
import net.enderbyteprograms.database.Table;
import net.enderbyteprograms.database.DataTypes;

/**
 * Program Title: Enderbyte Programs Database Debug Scripting System
 * 
 * Program Summary: A program to let users read and write to the database on the users' terms
 * 
 * Program Elements List: 
 * 
 * @author Jordan Rahim
 * @version 1
 * @date 2026-03-29
 */
public class ScriptUI {//begin class

    //variables
    public Database connectedDB;
    public Table selectedTable;
    private final String helpMenu = """
    ** Command List **
    Commands marked with a # are interactive
    help: Print this help menu
    exit: Exit the Debug Scripting Engine
    tables: Print a list of tables in this database
    table #: Choose a table in the database
    count: Produce a row count and total size in bytes of the selected table.
    add #: Add a row to the selected table.
    cols: Produce a list of columns on the currently selected table
    """;



    /**
     * Summary: Create a new Script UI object, but don't actually show the UI to the user yet.
     * @param db the database to attach this scripter to
     * @return the script ui object
     */
    public ScriptUI(Database db) {//begin constructor

        connectedDB = db;//That is all...

    }//end constructor





    /**
     * Summary: Show the scripting UI, acting upon the connected database
     * @param nothing
     * @return nothing
     */
    public void showScripter() {//begin method

        //variables
        Scanner inputScanner;
        String preCommandString;
        String lastCommand;
        boolean running;

        selectedTable = null;
        running = true;

        //Send the clear terminal character
        System.out.print("\u000C");
        System.out.println("Enderbyte Programs Database System Debug Scripting Engine (c) 2026 Jordan Rahim");

        while (running) {//begin while

            if (selectedTable == null) {//begin if

                preCommandString = "Database >>>";

            } else {//end if begin else

                preCommandString = String.format("Database [%s] >>>",selectedTable.tableName);

            }//end else

            System.out.println("Write the command you wish to execute, then press enter. For a list of commands, run 'help'");
            System.out.print(preCommandString);
            inputScanner = new Scanner(System.in);//Create new each time
            lastCommand = inputScanner.nextLine();
            inputScanner.close();//This breaks everything but BlueJ, but that is what Mr. Vatougios wants

            lastCommand = lastCommand.strip().toLowerCase();//Make everything case insensitive

            switch (lastCommand) {//begin swticg

                case "exit":
                    running = false;
                    break;

                case "help":
                    System.out.println(helpMenu);
                    break;

                case "tables":
                    doTablesCommand();
                    break;

                case "table":
                    doTableCommand();
                    break;

                case "count":
                    doCountCommand();
                    break;

                case "cols":
                    doColsCommand();
                    break;
                
                case "add":
                    doAddCommand();
                    break;

                default:
                    System.out.println("That is not a recognized command.");
                    break;

            }//end switch

        }//end while

        System.out.print("\u000C");
        //Clear the screen one more time on exit.

    }//end method




    /**
     * Summary: do the tables command
     * @param nothing
     * @return nothing
     */
    public void doTablesCommand() {//begin method

        //variables
        File possibleSourceFile;

        System.out.println("** TABLE LIST **");

        for (Table table:connectedDB.tables.values()) {//begin for

            System.out.print(table.tableName);//Begin with the table name
            System.out.print(" ");

            possibleSourceFile = table.dataStream.getSourceFile();

            if (possibleSourceFile == null) {//begin if

                //This is a temporary table
                System.out.print("[TEMP]");

            } else {//end if begin else

                System.out.print(String.format("on source %s",possibleSourceFile.toPath().toString()));

            }//end else

            System.out.println();//New line

        }//end for

    }//end method





    /**
     * Summary: do the table command
     * @param nothing
     * @return nothing
     */
    public void doTableCommand() {//begin method

        String targetTable;

        targetTable = Utilities.getInput("What table do you want to select?");

        if (!connectedDB.tables.containsKey(targetTable)) {//begin if

            System.out.println("The requested table does not exist.");
            return;

        }//end if

        selectedTable = connectedDB.getTable(targetTable);
        System.out.println(String.format("Selected table %s",targetTable));

    }//end method




    /**
     * Summary: do the count command
     * @param nothing
     * @return nothing
     */
    public void doCountCommand() {//begin method

        int rowCount;
        String occupiedSize;

        if (selectedTable == null) {//begin if

            System.out.println("Please select a table before performing this command");
            return;

        }//end if

        rowCount = selectedTable.getTableLength();
        occupiedSize = Utilities.parseSize(selectedTable.dataStream.getLength());

        System.out.println(String.format("Table %s has %d rows storing %s",selectedTable.tableName,rowCount,occupiedSize));

    }//end method




    /**
     * Summary: do the add command
     * @param nothing
     * @return nothing
     */
    public void doAddCommand() {//begin method

        //variables
        HashMap<String,Object> inboundData;
        SimpleDateFormat dateFormat;
        String rawValue;
        Object processedValue;

        inboundData = new HashMap<String,Object>();
        processedValue = 0;//Boring default value to prevent whinging
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Entry<String,DataTypes> column:selectedTable.columns.entrySet()) {//begin for

            while (true) {//begin while

                rawValue = Utilities.getInput(String.format("Value for column %s (Type: %s)",column.getKey(),column.getValue().toString()));

                try {//begin try

                    switch (column.getValue()) {//begin switch

                        case ShortString:
                        case MediumString:
                        case LargerString:
                        case String:
                            processedValue = rawValue;
                            //No special processing needed on string
                            break;

                        case Integer:
                            processedValue = Integer.parseInt(rawValue);
                            break;

                        case Boolean:

                            if (rawValue.toLowerCase().strip().equals("true")) {//begin if

                                processedValue = true;

                            } else if (rawValue.toLowerCase().strip().equals("false")) {//end if begin if

                                processedValue = false;

                            }//end if

                            break;

                        case Double:
                            processedValue = Double.parseDouble(rawValue);
                            break;

                        case DateTime:
                            processedValue = dateFormat.parse(rawValue);
                            break;

                        default:
                            break;

                    }//end switch

                    if (!column.getValue().objectIsAcceptable(processedValue)) {//begin if

                        throw new Exception("Object unacceptable");

                    }//end if

                } catch (Exception e) {//end try begin catch

                    System.out.println("This value is not appropriate for this column. Please try again.");
                    continue;

                }//end catch

                //If it got here the value is acceptable
                inboundData.put(column.getKey(),processedValue);
                break;
                    
            }//end while

        }//end for

        selectedTable.insert(inboundData);
        System.out.println("Successfully added row to table");

    }//end method





    /**
     * Summary: do the columns command
     * @param nothing
     * @return nothing
     */
    public void doColsCommand() {//begin method

        System.out.println("** Columns on selected table **");

        for (Entry<String,DataTypes> entry:selectedTable.columns.entrySet()) {//begin for

            System.out.println(String.format("Name: %s, Type: %s, Length: %d bytes",entry.getKey(),entry.getValue().toString(),entry.getValue().neededBytes));

        }//end for

    }//end method
    
}//end class
/**
 * Notes:
 * I think I need to stop being a tentpole Object Oriented Programmer and actually write the commands in here
 * I was considering making a Command interface and then implementing this for each command, but that would just be wasting more of my own time
 * Work smarter, not harder and all that
 * 
 * Test Code:
 *  System.out.println("Write the command you wish to execute, then press enter. For a list of commands, run 'help'");
            System.out.print(preCommandString);
            inputScanner = new Scanner(System.in);//Create new each time
            lastCommand = inputScanner.nextLine();
            inputScanner.close();//This breaks everything but BlueJ, but that is what Mr. Vatougios wants

            lastCommand = lastCommand.strip().toLowerCase();//Make everything case insensitive
 */