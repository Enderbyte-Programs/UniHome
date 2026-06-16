package net.enderbyteprograms.database.adapters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

/**
 * Program Title: File System IO Adapter
 * 
 * Program Summary: An adapter that edits using the filesystem, abstracting the RandomAccessFile in a way that can be used in a standardized manner by the table.
 * 
 * Program Elements List:
 * RandomAccessFile
 * Random generation
 * Buffer concepts - read write seek
 * REsizing of buffer
 * 
 * @author Jordan Rahim
 * @version 1.0
 * @date 2026-03-26
 */
public class FileSystemAdapter implements IOAdapter {//begin class

    private RandomAccessFile buffer;
    private File activeFile;
    private boolean sourceIsInMemory;//If true, this is a temporary file





    /**
     * Summary: Create a new file system adapter object. You are then required to call either useInMemory() or useFile(...)
     * @param nothing
     * @return nothing
     */
    public FileSystemAdapter() {//begin constructor

        //This does nnothing
        activeFile = null;

    }//end constructor



    
    
    
    /**
     * Summary: Set the pointer of the file to a specific position
     * @param position the position to jump to
     * @return nothing
     */
    @Override
    public void setPointer(long position) {//begin method
        
        try {//begin try

            buffer.seek(position);

        } catch (IOException e) {//end try begin catch

            throw new RuntimeException("Critical DB error!");

        }//end catch

    }//end method






    /**
     * Summary: Get the position of the pointer within the file
     * @param nothing
     * @return the byte offset of the position of the pointer
     */
    @Override
    public long getPointer() {//begin method

        try {//begin try

            return buffer.getFilePointer();

        } catch (IOException e) {//end try begin catch

            throw new RuntimeException("Critical DB error!");

        }//end catch

    }//end method






    /**
     * Summary: Set the length of the file. The file will be truncated or extended as necessary.
     * @param size the size in bytes that you want the file to be
     * @return nothing
     */
    @Override
    public void setLength(long size) {//begin method

        try {//begin try

            buffer.setLength(size);

        } catch (IOException e) {//end try begin catch

            throw new RuntimeException("Critical DB error!");

        }//end catch

    }//end method






    /**
     * Summary: Get the length of the file. 
     * @param nothing
     * @return the length of the file in bytes
     */
    @Override
    public long getLength() {//begin method

        try {//begin try

            return buffer.length();

        } catch (IOException e) {//end try begin catch

            throw new RuntimeException("Critical DB error!");

        }//end catch

    }//end method






    /**
     * Summary: Write a singular byte, unsigned, to the position at the pointer, incrementing the pointer.
     * @param value the value (should be 0-255) to write.
     * @return nothing
     */
    @Override
    public void writeByte(int value) {//begin method

        try {//begin try

            buffer.writeByte(value);

        } catch (IOException e) {//end try begin catch

            throw new RuntimeException("Critical DB error!");

        }//end catch

    }//end method






    /**
     * Summary: Write an array into the file, starting at the pointer position.
     * @param array the array to write
     * @return nothing
     */
    @Override
    public void writeArray(byte[] array) {//begin method

        try {//begin try

            buffer.write(array);

        } catch (IOException e) {//end try begin catch

            throw new RuntimeException("Critical DB error!");

        }//end catch

    }//end method






    /**
     * Summary: Read a single unsigned byte at the position of the pointer. INcrement the pointer.
     * @param nothing
     * @return the value, 0-255
     */
    @Override
    public int readUnsignedByte() {//begin method

        try {//begin try

            return buffer.readUnsignedByte();

        } catch (IOException e) {//end try begin catch

            throw new RuntimeException("Critical DB error!");

        }//end catch

    }//end method






    /**
     * Summary: Read a single signed byte at the position of the pointer, incremengint the pointer.
     * @param nothing
     * @return the byte value, signed
     */
    @Override
    public byte readByte() {//begin method

        try {//begin try

            return buffer.readByte();

        } catch (IOException e) {//end try begin catch

            throw new RuntimeException("Critical DB error!");

        }//end catch

    }//end method





    /**
     * Summary: Read into an array
     * @param array the array to read into
     * @return nothing
     */
    @Override
    public void read(byte[] array) {//begin method
  
        try {//begin try

            buffer.read(array);

        } catch (IOException e) {//end try begin catch

            throw new RuntimeException("Critical DB error!");

        }//end catch

    }//end method





    /**
     * Summary: Set the source of this adapter to be a file, which you specify. As this is an FSA, the file will be edited in place.
     * @param file the file that will be loaded
     * @return nothing
     */
    @Override
    public void useFile(File file) {//begin methood

        sourceIsInMemory = false;
        activeFile = file;

        try {//begin try

            buffer = new RandomAccessFile(file,"rw");

        } catch (FileNotFoundException e) {//end try begin catch

            throw new RuntimeException("Please create the file before assigning a database to it");

        }//end catch

    }//end method





    /**
     * Summary: SEt this up as an in-mempory table being edited on the file system (thus a temporary directory)
     * @param nothing
     * @return nothing
     */
    @Override
    public void useInMemory() {//begin method

        Random randomFileGenerator;
        StringBuilder temporaryDirectory;
        File temporaryFile;
        
        sourceIsInMemory = true;

        //Because this is a file system adapter, we need to make a temporary file.
        randomFileGenerator = new Random();
        temporaryDirectory = new StringBuilder(System.getProperty("java.io.tmpdir"));//Start with the temporary dir
        temporaryDirectory.append(File.separator);
        temporaryDirectory.append(randomFileGenerator.nextInt(1111,9999));
        temporaryDirectory.append(".tbl");

        temporaryFile = new File(temporaryDirectory.toString());
        activeFile = temporaryFile;
        
        try {//bewgin try

            temporaryFile.createNewFile();
            buffer = new RandomAccessFile(temporaryFile,"rw");

        } catch (IOException e) {//end try begin catch

            throw new RuntimeException("File IO error");

        }//end catch


    }//end method





    /**
     * Summary: Close the file if on-file, and delete the file if it is temporary.
     * @param nothing
     * @return nothing
     */
    @Override
    public void saveAndClose() {//begin method

        try {//begin try

            buffer.close();

            if (sourceIsInMemory) {//begin if

                activeFile.delete();

            }//end if

        }  catch (IOException e) {//end try begin catch

            throw new RuntimeException("Fatal error");

        }//end catch

    }//end method




    /**
     * Summary: Do nothing, as this adapter is auto-saving in nature
     * @param nothing
     * @return nothing
     */    
    @Override
    public void save() {//begin methood

        //This method does nothing because RandomAccessFile is auto-saving

    }//end method





    /**
     * Summary: Get the source file, if applicable, or null if there is no source file
     * @param nothing
     * @return the source file, or null if there is no source file
     */
    @Override
    public File getSourceFile() {//begin methood

        return activeFile;

    }//end method
    
}//end class
/**
 * Notes:
 * 
 * I intend to have this get passed to the table itself, such as new Table(new FileSystemAdapter...) and we will load from file if needed.
 * The database will need to have another method which is "assertInMemoryTable" which will use the memory adapter. This will use randomaccess file, so if the user attempts to use this without doing a loadFile, it will throw null pointer.
 * 
 * Table of usage
 *              FS SOURCE         CREATED IN MEMORY
 * MOD ON FS     Traditional          Rare, no use known
 * MOD ON MEMORY  Fast/modern          Common for throwaway tables
 * 
 * Important note for the InMemoryAdapter (not this class) is that we will need to save to disk.
 * Important note for THIS class is that for FS source we will need to close it when the user is done or it might leak resources and file locks.
 * 
 * Test Code:
 *  @Override
    public void saveAndClose() {//begin method

        try {//begin try

            buffer.close();

            if (sourceIsInMemory) {//begin if

                activeFile.delete();

            }//end if

        }  catch (IOException e) {//end try begin catch

            throw new RuntimeException("Fatal error");

        }//end catch

    }//end method
 * 
 */