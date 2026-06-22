package net.enderbyteprograms.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import net.enderbyteprograms.database.adapters.IOAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Program Title: Table Class
 * 
 * Program Summary: An object to store one table
 * 
 * Program Element List: 
 * Files
 * Exceptions
 * RandomAccessFile
 * ByteBuffers
 * Oaths
 * Charsets
 * HashMaps
 * Map entries
 * Arraylists
 * dates
 * hashmaps
 * maps and mre
 * 
 * @author Jordan Rahim
 * @version 1
 */

public class Table {//begin class

    public String tableName;
    public IOAdapter dataStream;//Scripting engine needs access to this
    public LinkedHashMap<String,DataTypes> columns;
    private LinkedHashMap<String,Integer> columnByteOffsets;
    private LinkedHashMap<String,Object> defaultValues;
    private AutoSaver autoSaver = null;
    private int dataRowLength = 0;
    private final int headerLength = 256;//Max header length is 256 bytes

    /**
     * Summary: Create a new blank table
     * @param parent the parent database
     * @param tableName the table name to create
     * @param dataAdapter the IO adapter to use (defining where the data is stored duriong the run of the program)
     * @param useFileSystem If true, save this table to the disk. If false, save the table "in memory", thus destroying it at the end of execution
     * @return an empty table object
     */
    public Table(Database parent,String tableName,IOAdapter dataAdapter,boolean useFileSystem) {//begin constructor

        File newFile;
        byte[] emptyHeader;

        columns = new LinkedHashMap<String,DataTypes>();
        columnByteOffsets = new LinkedHashMap<String,Integer>();
        defaultValues = new LinkedHashMap<String,Object>();
        this.tableName = tableName;
        newFile = Paths.get(parent.dbFolder.toPath().toString(),tableName + ".tbl").toFile();
        dataStream = dataAdapter;
        emptyHeader = new byte[headerLength];

        try {//begin try

            if (useFileSystem) {//begin if
                
                newFile.createNewFile();
                dataAdapter.useFile(newFile);

            } else {//end if begin else

                dataAdapter.useInMemory();

            }//end else

            //Set up filescanner with blank header
            dataStream.setLength(headerLength);//Allocate 256 bytes for header
            dataStream.setPointer(0);
            dataStream.writeArray(emptyHeader);

            writeHeader();//Write a proper header

        } catch (Exception e) {//end try begin catch

            //Let it die, let it die, let it shrivel up and die! if it crashes here
            throw new RuntimeException("Critical error setting up DB table");

        }//end catch



    }//end constructor





    /**
     * Summary: Create a Table from a data file, loading all columns
     * @param dataFile the data file
     * @param parent the parent database
     * @param dataAdapter The IO adapter (so either edited on FS or in memory) to use. Use the direct constructor either new FileSystemAdapter() for traditional behaviour or MemoryAdapter() for higher speed.
     * @return the shiny new table
     */
    public Table(Database parent,File dataFile,IOAdapter dataAdapter) throws IOException,FileNotFoundException {//begin constructor

        DataTypes[] columnDefinitions;
        String[] columnNames;
        DataTypes currentColumnDefDT;
        DataTypes defaultLoaderCurrentDT;
        String rawColumnName;
        Object currentDefaulObject;
        byte[] columnNameBytes;
        byte[] currentDefaultBytes;
        int columnCount;
        int currentColumnDef;
        byte curScanByte;

        columnByteOffsets = new LinkedHashMap<String,Integer>();
        defaultValues = new LinkedHashMap<String,Object>();
        columns = new LinkedHashMap<String,DataTypes>();
        tableName = dataFile.getName().split("\\.")[0];

        //Set up the data adapter
        dataStream = dataAdapter;
        dataStream.useFile(dataFile);//Set up the datastream to use this file.

        //Now, let's start reading the data
        columnCount = dataStream.readUnsignedByte();//populate the HIB with the data

        //Now set the length of the Cdef bytes
        columnDefinitions = new DataTypes[columnCount];
        columnNameBytes = new byte[headerLength - (columnCount + 1)];//can store the rest of it

        //ptr should be at position 1 now.
        dataStream.setPointer(1);
        
        for (int i = 0; i < columnCount; i++) {//begin for

            currentColumnDef = dataStream.readUnsignedByte();
            currentColumnDefDT = DataTypes.fromDefinition(currentColumnDef);
            columnDefinitions[i] = currentColumnDefDT;
            dataRowLength += currentColumnDefDT.neededBytes;

        }//end for

        //ptr should now be at the beginning of the column name definition
        for (int i = 0; i < (headerLength - (columnCount + 1));i++) {//begin while

            curScanByte = dataStream.readByte();
            if (curScanByte == 0) {//begin if

                break;//Reached the end of the column definition

            }//end if

            columnNameBytes[i] = curScanByte;

        }//end while

        rawColumnName = new String(columnNameBytes,StandardCharsets.UTF_8).replace("\0","");//Remove any nulls in case the slip through
        columnNames = rawColumnName.split(",");

        //Now place the column names into the column name dictionary
        for (int i = 0; i < columnCount;i++) {//begin for

            columns.put(columnNames[i],columnDefinitions[i]);

        }//end for

        recompileOffsetChart();

        //read defaults from first row
        if (dataRowLength > 0) {//begin if

            for (Entry<String,Integer> columnDefinition:columnByteOffsets.entrySet()) {//begin for

                defaultLoaderCurrentDT = columns.get(columnDefinition.getKey());
                currentDefaultBytes = new byte[defaultLoaderCurrentDT.neededBytes];

                dataStream.read(currentDefaultBytes);//Populate
                currentDefaulObject = loadFromBytes(currentDefaultBytes,defaultLoaderCurrentDT);
                defaultValues.put(columnDefinition.getKey(),currentDefaulObject);

            } //end for

        }//end if
        

    }//end constructor




    /**
     * Summary: After the column list has been changed, recompile the offset charts
     * @param nothing
     * @return nothing
     */
    private void recompileOffsetChart() {//begin method

        //vars
        int offset = 0;

        //reset
        columnByteOffsets.clear();

        for (Entry<String,DataTypes> column:columns.entrySet()) {//begin for

            columnByteOffsets.put(column.getKey(),offset);

            offset += column.getValue().neededBytes;

        }//end for

        dataRowLength = offset;

    }//end method




    /**
     * Summary: Write the header to the randomaccessfile
     * @param nothing
     * @return nothing
     */
    private void writeHeader() {//begin method

        //variables
        String inboundString;
        byte[] byteRepr;
            
        zeroBytes(0, headerLength);
        dataStream.setPointer(0);
        dataStream.writeByte(columns.size());

        for (Entry<String,DataTypes> column:columns.entrySet()) {//begin for

            dataStream.writeByte(column.getValue().byteRepresentation);

        }//end for

        //Write column
        inboundString = String.join(",",columns.keySet());
        byteRepr = inboundString.getBytes(StandardCharsets.UTF_8);
        dataStream.writeArray(byteRepr);

        //Extend file if we need to
        if (dataStream.getLength() < (headerLength + dataRowLength)) {//begin if

            dataStream.setLength(headerLength + dataRowLength);

        }//end if

        zeroBytes(headerLength, dataRowLength);
        dataStream.setPointer(headerLength);//First byte after header
        //write default byte header
        writeCurrentRow(defaultValues);
        

    }//end method





    /**
     * Summary: parse some bytes to the associated object, either a Boolean, Integer, Double, DateTime, or String
     * @param rawData the raw byte array of the data
     * @param dataType the datatype of the byte
     * @return the fiished object
     */
    private Object loadFromBytes(byte[] rawData,DataTypes dataType) {//begin method

        ByteBuffer buffer;

        buffer = ByteBuffer.wrap(rawData);

        switch (dataType.id) {//begin switch

            case 0:
                return buffer.getInt();

            case 1:
                return buffer.get() == 1;//How booleans are stored

            case 2:
                return buffer.getDouble();

            case 3:
                return new String(rawData,StandardCharsets.UTF_8).replace("\0","");//no nulls 2day

            case 4:
                return new Date((long)(buffer.getInt())*1000L);

            default:
                return "";

        }//end switch

    }//end method




    
    /**
     * Summary: convert an object in one of the approved data types into a byte representation, designed to be written at the beginning of an allocated section
     * @param objToConvert the object to convert
     * @return the byte array containing bytes to be written to the file
     */
    private byte[] getBytes(Object objToConvert) {//begin method

        String strRepr;
        ByteBuffer buffer;
        Date dateRepr;
        byte[] stringBytes;
        byte[] result;

        if (objToConvert instanceof Integer) {//begin if

            buffer = ByteBuffer.allocate(4);
            buffer.putInt((Integer)(objToConvert));
            result = buffer.array();

        } else if (objToConvert instanceof Boolean) {//end if begin if

            buffer = ByteBuffer.allocate(1);
            if ((Boolean)objToConvert) {//begin if

                buffer.put((byte)1);

            } else {//end if begin else

                buffer.put((byte)0);

            }//end else
            result = buffer.array();

        } else if (objToConvert instanceof Double) {//end if begin if

            buffer = ByteBuffer.allocate(8);
            buffer.putDouble((Double)objToConvert);
            result = buffer.array();

        } else if (objToConvert instanceof String) {//end if begin if

            strRepr = (String)objToConvert;
            stringBytes = strRepr.getBytes(StandardCharsets.UTF_8);
            result = stringBytes;

        } else if (objToConvert instanceof Date) {//end if begin if

            dateRepr = (Date)objToConvert;
            buffer = ByteBuffer.allocate(4);
            buffer.putInt((int)(dateRepr.getTime() / 1000L));
            result = buffer.array();

        } else {//end if begin else

            buffer = ByteBuffer.allocate(0);
            result = buffer.array();

        }//end else

        return result;

    } //end method





    /**
     * Summary: Insert a row at the end of the table
     * @param row A mapping of column name -> data - must obey type
     * @return nothing
     */
    public void insert(Map<String,Object> row) {//begin method

        //vars
        Map<String,byte[]> byteDictionary = new HashMap<String,byte[]>();
        Object currentValue;
        byte[] currentBytes;
        int startOfDataOffset;
        int currentWriteOffset;

        for (Entry<String,Object> expectedcolumn:row.entrySet()) {//begin for
            
            if (!columns.containsKey(expectedcolumn.getKey())) {//begin if

                continue;//Unexpected value: get rid of it

            }//end if

            currentValue = expectedcolumn.getValue();


            
            if (!(columns.get(expectedcolumn.getKey())).objectIsAcceptable(currentValue)) {//begin if

                System.out.println("I1c on"+currentValue+expectedcolumn.getKey());


                throw new RuntimeException("Encountered unacceptable data for column "+expectedcolumn.getKey());

            }//end if



            currentBytes = getBytes(currentValue);
            byteDictionary.put(expectedcolumn.getKey(),currentBytes);
            
        }//end for


            
        startOfDataOffset = (int)(dataStream.getLength());

        //Now get offsets and write
        //Expand DB file
        dataStream.setLength(startOfDataOffset + dataRowLength);
        zeroBytes(startOfDataOffset, dataRowLength);//Clean write area
        //Write defaults, then write new vals on top of it
        writeDefaultsAt(startOfDataOffset);
        
        for (Entry<String,byte[]> byteToAdd:byteDictionary.entrySet()) {//begin for


            currentWriteOffset = columnByteOffsets.get(byteToAdd.getKey());
            zeroBytes(startOfDataOffset + currentWriteOffset,columns.get(byteToAdd.getKey()).neededBytes);
            dataStream.setPointer(startOfDataOffset + currentWriteOffset);
            dataStream.writeArray(byteToAdd.getValue());

        }//end for
        
    }//end method





    /**
     * Summary: Load the entire data table as a list of rows
     * @param nothing
     * @return The entire data table as a list of rows
     */
    public ResultSet select() {//begin method
        
        //vars
        ResultSet result;
        ArrayList<ResultRow> tempResult;
        ResultRow currentRow;

        tempResult = new ArrayList<ResultRow>();
        result = new ResultSet();
        
        try {//begin try
        
            if (getTableLength() == 0) {//begin if

                return result;

            }//end if

            for (int targetOffset = (headerLength + dataRowLength);targetOffset < dataStream.getLength();targetOffset += dataRowLength) {//begin for

                dataStream.setPointer(targetOffset);
                currentRow = loadCurrentRow();//Pointer must be at the beginning of the row

                tempResult.add(currentRow);

            }//end for

        } catch (Exception e) {//end try begin catch

            throw new RuntimeException("Critical DB error",e);

        }//end catch

        return new ResultSet(tempResult);

    }//end method





    /**
     * Summary: Select data where the comparator's requirements are met
     * @param comparator The discriminator as to whether or no a row should be added on to the result
     * @return the results of this select query.
     */
    public ResultSet select(CustomComparison comparator) {//begin method

        //vars
        ArrayList<ResultRow> tempResult;
        ResultSet result;
        ResultRow currentRow;

        tempResult = new ArrayList<ResultRow>();
        result = new ResultSet();
        
        try {//begin try
        
            if (getTableLength() == 0) {//begin if

                return result;

            }//end if

            for (int targetOffset = (headerLength + dataRowLength);targetOffset < dataStream.getLength();targetOffset += dataRowLength) {//begin for

                dataStream.setPointer(targetOffset);
                currentRow = loadCurrentRow();//Pointer must be at the beginning of the row

                if (comparator.shouldAccept(currentRow)) {//begin if

                    tempResult.add(currentRow);

                }//end if

            }//end for

        } catch (Exception e) {//end try begin catch

            throw new RuntimeException("Critical DB error",e);

        }//end catch

        result = new ResultSet(tempResult);

        return result;

    }//end method





    /**
     * Summary: Check if a column exists
     * @param columnName the column name to check
     * @return True if the column exists, false if the column doesn't.
     */
    public boolean columnExists(String columnName) {//begin method

        return columns.containsKey(columnName);

    }//end method


    /**
     * Summary: Update data in the table, using a form of CustomComparison - static (Comparison) or some custom one you write.
     * @param comparator the custom comparator, which can accept or reject rows dynamically. Can also be a normal Comparison
     * @param updater the custom updater, which can set values cynamically. Can also be an Updater
     * @return nothing
     */
    public void update(CustomComparison comparator,CustomUpdater updater) {//begin method

        //vars
        Map<String,Object> currentRow;
        
        try {//begin try
        
            if (getTableLength() == 0) {//begin if

                return;

            }//end if

            for (int targetOffset = (headerLength + dataRowLength);targetOffset < dataStream.getLength();targetOffset += dataRowLength) {//begin for

                dataStream.setPointer(targetOffset);
                currentRow = loadCurrentRow();//Pointer must be at the beginning of the row

                if (comparator.shouldAccept(currentRow)) {//begin if

                    currentRow = updater.write(currentRow);
                    dataStream.setPointer(targetOffset);
                    writeCurrentRow(currentRow);

                }//end if

            }//end for

        } catch (Exception e) {//end try begin catch

            throw new RuntimeException("Critical DB error",e);

        }//end catch

    }//end methood







    /**
     * Summary: Update every row on the table with the dynamic updater or static updater, whatever you want
     * @param updater the dyanmic updater to use
     * @return nothing
     */
    public void update(CustomUpdater updater) {//begin method

        //vars
        Map<String,Object> currentRow;
        
        try {//begin try
        
            if (getTableLength() == 0) {//begin if

                return;

            }//end if

            for (int targetOffset = (headerLength + dataRowLength);targetOffset < dataStream.getLength();targetOffset += dataRowLength) {//begin for

                dataStream.setPointer(targetOffset);
                currentRow = loadCurrentRow();//Pointer must be at the beginning of the row
                currentRow = updater.write(currentRow);
                dataStream.setPointer(targetOffset);
                writeCurrentRow(currentRow);


            }//end for

        } catch (Exception e) {//end try begin catch

            throw new RuntimeException("Critical DB error",e);

        }//end catch

    }//end method




    /**
     * Summary: Delete all rows in the table
     * @param nothing
     * @return nothing
     */
    public void delete() {//begin method


        dataStream.setLength(dataRowLength + headerLength);


    }//end method





    /**
     * Summary: Delete rows in the table where the custom comparator or normal comparator says yes
     * @param comparator the comparator to use
     * @return nothing
     */
    public void delete(CustomComparison comparator) {//begin method

        //Now this is going to be much more difficult. Begin the vars...
        Map<String,Object> currentRow;
        int rowCounter;


        try {

            //For simplicity's sake, I will start from 1 so we can treat the defaults row as a row
            rowCounter = 1;

            while (rowCounter < (getTableLength() + 1)) {//begin while - cancel -1 on gettablelength

                //recalculate table length each time
                dataStream.setPointer((rowCounter * dataRowLength) + headerLength);
                currentRow = loadCurrentRow();
                
                if (comparator.shouldAccept(currentRow)) {//begin if

                    //This row gets to die now. Let's check if there is an available row to clear it
                    if (rowCounter < (getTableLength() + 1)) {//begin if

                        //Move the last row to fill the space, No guarantee of order LOL!!!! HO HO HO
                        dataStream.setPointer((getTableLength() * dataRowLength) + headerLength);//No -1 because +1 from getTableLength cancels it out!
                        //If I am debugging this REMEMBER TO READ THIS!!!!!!
                        //overwrite current row... no point in saving it
                        currentRow = loadCurrentRow();
                        dataStream.setPointer((rowCounter * dataRowLength) + headerLength);
                        writeCurrentRow(currentRow);//Write that last row into the space that just got delete

                        //Now let's truncate the file
                        dataStream.setLength(dataStream.getLength() - dataRowLength);
                        rowCounter--;//Decrement it so we check the row we just pasted in JUST IN CASE it also needs to be deleted.

                    }//end if

                }//end if

                rowCounter++;

            }//end while

        } catch (Exception e) {//end try begin catch

            throw new RuntimeException("Critical DB error!",e);

        }//end catch


    }//end method





    /**
     * Summary: Add a new column, doing nothing if the coolumn exists
     * @param dataType the type of data that this column will store
     * @param columnName the name of this column
     * @return nothing
     */
    public void addColumn(DataTypes dataType,String columnName,Object defaultValue) {//begin method

        byte[] currentlyMovedRow;
        int numberOfRows;
        int recommendedDataLength;
        int oldDataLength;
        int sourceStartOfDataPosition;
        int targetStartOfDataPosition;

        oldDataLength = dataRowLength;
        currentlyMovedRow = new byte[oldDataLength];

        if (columnExists(columnName)) {//begin if

            return;

        }//end if

        defaultValues.put(columnName,defaultValue);
        columns.put(columnName,dataType);

        recompileOffsetChart();

        //Now I need to create space for the new column
        try {//begin try

            numberOfRows = getTableLength() + 1;//This time we want to include the defaults row
            recommendedDataLength = dataRowLength * numberOfRows;
            dataStream.setLength(recommendedDataLength);//Expand table file
            
            for (int i = (numberOfRows - 1); i >= 0; i--) {//begin for

                //For every row, move it to its target location - Do it backwards so we don't write over any data
                sourceStartOfDataPosition = (i * oldDataLength) + headerLength;
                targetStartOfDataPosition = (i * dataRowLength) + headerLength;

                dataStream.setPointer(sourceStartOfDataPosition);
                dataStream.read(currentlyMovedRow);
                zeroBytes(targetStartOfDataPosition, dataRowLength);//Zero target, as Java says that it could contain undefined junk
                dataStream.setPointer(targetStartOfDataPosition);
                dataStream.writeArray(currentlyMovedRow);//Write the data
                dataStream.writeArray(getBytes(defaultValue));//Write the default value for the new column

            }//end for

            writeHeader();//Writeheader also writes the defaults row so we need to have the stuff moved away from this point

        } catch (Exception e) {//end try begin catch

            throw new RuntimeException("IO Error when expanding table",e);

        }//end catch


    }//end method





    /**
     * Summary: Delete a column, then rewrite the table so that everything is in alignment
     * @param columnName the column name to delete
     * @return nothing
     */
    public void dropColumn(String columnName) {//begin method

        //vars
        Map<String,Object> tempRow;
        int oldDataLength;
        int newDataStartPosition;
        int counter;

        try {//begin try

            //Drop the column
            oldDataLength = dataRowLength;
            counter = 0;//We want to clean up the defaults row this time
            columns.remove(columnName);
            recompileOffsetChart();

            //Start reading lines and writing them to their new positions, beginning to end
            for (int startOfOldLine = headerLength; startOfOldLine < dataStream.getLength(); startOfOldLine += oldDataLength) {//begin for

                dataStream.setPointer(startOfOldLine);
                tempRow = loadCurrentRow();
                //When we write it back, it should actually ignore the deleted column

                newDataStartPosition = (counter * dataRowLength) + headerLength;
                dataStream.setPointer(newDataStartPosition);

                writeCurrentRow(tempRow);

                counter++;

            }//end for

            //Truncate the file
            dataStream.setLength(headerLength + ((getTableLength() + 1) * dataRowLength));

        } catch (Exception e) {//end try begin catch

            throw new RuntimeException("Critical IO error",e);

        }//end catch

    }//end method





    /**
     * Summary: Get the number of rows in the table. Returns -1 on error
     * @param nothing
     * @return the number of rows
     */
    public int getTableLength() {//begin method

        if (dataRowLength == 0) {//begin if

            return 0;

        }

        try {//begin try

            return ((((int)(dataStream.getLength()) - headerLength) / dataRowLength) - 1);

        } catch (Throwable e) {//end try begin catch

            return -1;

        }//end catch

    }//end method




    /**
     * Summary: If applicable, save the data in this table. If this is using a filesystem, this is done automatically. If this is edited in memory but saved on the disk, one should call this regularly.
     * @param nothing
     * @return nothing
     */
    public void saveData() {//begin method

        dataStream.save();

    }//end method





    /**
     * Summary: User an autosaver, saving every n seconds
     * @param savePeriod after how many seconds to save
     * @return nothing
     */
    public void useAutosave(int savePeriod) {//begin method

        autoSaver = new AutoSaver(this,savePeriod);
        autoSaver.start();

    }//end method




    /**
     * Summary: Save and close up the table. No more methods should be called on this table after this is called.
     * @param nothing
     * @return nothing
     */
    public void finish() {//begin method

        autoSaver.stop();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {

        }
        dataStream.saveAndClose();

    }//end method





    /**
     * Summary: Zero the bytes in the file scanner from offset for length bytes
     * @param offset The byte to start zeroing at
     * @param length the amount of zeros to write
     * @return nothing
     */
    private void zeroBytes(int offset, int length) {//begin method

        try {//begin try

            dataStream.setPointer(offset);

            for (int i = 0; i < length; i++) {//begin for

                dataStream.writeByte(0);

            }//end for

        } catch (Exception e) {//end try begin catch

            throw new RuntimeException("Critical IO error",e);

        }//end catch

    }//end method





    /**
     * Summary: Read the row, beginning from the file scanner's pointer and returned a parsed object
     * @param nothing
     * @return the map representation of the row starting at the pointer
     */
    private ResultRow loadCurrentRow() {//begin method

        //variables
        ResultRow result;
        Map<String,Object> tempResult;
        DataTypes currentDataType;
        Object currenObject;
        byte[] currentColumn;
        long startOfRowPos;

        tempResult = new LinkedHashMap<String,Object>();

        try {//begin try

            startOfRowPos = dataStream.getPointer();      
                        
            for (Entry<String,Integer> offset:columnByteOffsets.entrySet()) {//begin for

                currentDataType = columns.get(offset.getKey());
                currentColumn = new byte[currentDataType.neededBytes];
                dataStream.setPointer(startOfRowPos + offset.getValue());//Move the pointer to the beginning of the column
                dataStream.read(currentColumn);
                currenObject = loadFromBytes(currentColumn, currentDataType);
                tempResult.put(offset.getKey(),currenObject);
            }//end for

        } catch (Exception e) {//end try begin catch

            throw new RuntimeException("Critical DB error!",e);

        }//end catch

        result = new ResultRow(tempResult, columns,this.tableName);
        return result;

    }//end method




    
    
    /**
     * Summary: Write the row at the current position
     * @param row the row
     * @return nothinng
     */
    private void writeCurrentRow(Map<String,Object> row) {//begin method

        //variables
        ByteBuffer rollingBuffer;

        rollingBuffer = ByteBuffer.allocate(dataRowLength);

        for (Entry<String,Integer> offset:columnByteOffsets.entrySet()) {//begin for

            rollingBuffer.position(offset.getValue());
            rollingBuffer.put(getBytes(row.get(offset.getKey())));

        }//end for

        try {//begin try

            dataStream.writeArray(rollingBuffer.array());

        } catch (Exception e) {//end try begin catch

            throw new RuntimeException("Critical DB error",e);

        }//end catch

    }//end method





    /**
     * Summary: Write a defaults row at the given position
     * @param position the position to start writing the defaults row from
     * @return nothing
     */
    private void writeDefaultsAt(int position) {//begin method

        //variables
        ResultRow defaultsRow;

        try {//begin try

            dataStream.setPointer(headerLength);//Beginning of defaults
            defaultsRow = loadCurrentRow();
            dataStream.setPointer(position);
            writeCurrentRow(defaultsRow);
             
        } catch (Exception e) {//end try begin catch

            throw new RuntimeException(e);

        }//end catch

    }//end method
    
}//end class
/**
 * Notes: I had better be able to reuse this
 * 
 * Test Code: 
 * private void writeDefaultsAt(int position) {//begin method

        //variables
        ResultRow defaultsRow;

        try {//begin try

            fileScanner.setPointer(headerLength);//Beginning of defaults
            defaultsRow = loadCurrentRow();
            fileScanner.setPointer(position);
            writeCurrentRow(defaultsRow);
             
        } catch (Exception e) {//end try begin catch

            throw new RuntimeException("DB ERROR!");

        }//end catch

    }//end method
 */