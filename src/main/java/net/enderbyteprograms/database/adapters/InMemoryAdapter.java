package net.enderbyteprograms.database.adapters;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import net.enderbyteprograms.Utilities;

/**
 * Program Title: In Memory IO Adapter for Database
 * 
 * Program Summary: An implementation of the IOAdapter that works purely in memory. This is faster, but requires manual saving. If you have a large dataset, it may run you out of memory.
 * 
 * Program Elements List:
 * - ByteBuffer
 * Stream and buffer concepts
 * Resizing static array
 * Traditional file opening
 * buffer capacity
 * buffer IO
 * 
 * @author Jordan Rahim
 * @version 1.0
 * @date 2026-03-26
 */
public class InMemoryAdapter implements IOAdapter {//begin class

    //Instance variables
    private ByteBuffer buffer;
    private File activeFile;
    public boolean sourceIsInMemory;




    /**
     * Summary: Create a new memory adapter object. You are then required to call either useFile(...) or useInMemory() before this can be used.,
     * @param nothing
     * @return nothing
     */
    public InMemoryAdapter() {//begin constructor

        //This does nothing
        activeFile = null;

    }//end constructor

    
    

    /**
     * Summary: Set the position of the pointer in the buffer
     * @param position the position to jump to
     * @return nothing
     */
    @Override
    public void setPointer(long position) {//begin method
        
        buffer.position((int)position);

    }//end method






    /**
     * Summary: Get the position of the pointer in the buffer
     * @param nothing
     * @return the offset of the pointer
     */
    @Override
    public long getPointer() {//begin method

        return buffer.position();

    }//end method





    /**
     * Summary: Set the length of the buffer. Data will be truncated if necessary.
     * @param size the size to allocate
     * @return nothing
     */
    @Override
    public void setLength(long size) {//begin method

        ByteBuffer newBuffer;
        int oldPosition;

        oldPosition = buffer.position();

        newBuffer = ByteBuffer.allocate((int)size);
        newBuffer.position(0);//Now we copy old data into new data

        if (size > buffer.capacity()) {//begin if

            //The buffer is expanding

            newBuffer.put(buffer.array(),0,buffer.capacity());

        } else {//end if begin else

            //The buffer is shrinking

            newBuffer.put(buffer.array(),0,(int)size);

        }//end else

        if (oldPosition < size) {//begin if

            newBuffer.position(oldPosition);

        } else {//end if begin else

            //Just go to 0
            newBuffer.position(0);

        }//end else

        buffer = newBuffer;

    }//end method





    /**
     * Summary: Get the length of the buffer
     * @param nothing
     * @return the length of the buffer in bytes
     */
    @Override
    public long getLength() {//begin method

        return buffer.capacity();

    }//end method





    /**
     * Summary: Write a single byte at the cursor position
     * @param value the value to write
     * @return nothing
     */
    @Override
    public void writeByte(int value) {//begin method

        buffer.put((byte)value);//Apparently this will properly carry it over in terms of signing

    }//end method






    /**
     * Summary: Write the array at the cursor position
     * @param array the array to write
     * @return nothing
     */
    @Override
    public void writeArray(byte[] array) {//begin method

        buffer.put(array);

    }//end method





    /**
     * Summary: Read a single unsigned byte
     * @param nothing
     * @return the byte, unsigned
     */
    @Override
    public int readUnsignedByte() {//begin method

        return Utilities.unsignByte(buffer.get());//Let us hope that this works...

    }//end method





    /**
     * Summary: Read a single signed byte
     * @param nothing
     * @return the byte, signed
     */
    @Override
    public byte readByte() {//begin method

        return buffer.get();

    }//end method





    /**
     * Summary: Read into an array
     * @param array the array to read into
     * @return nothing
     */
    @Override
    public void read(byte[] array) {//begin method
  
        buffer.get(array);

    }//end method






    /**
     * Summary: Set this up to use a file as the source item. Data will be synced from the file to the database on request (one way)
     * @param file the file to read from. It must exist when you call this.
     * @return nothing
     */
    @Override
    public void useFile(File file) {//begin methood

        //vars
        
        sourceIsInMemory = false;//Signal that this needs to be saved.
        activeFile = file;
        
        try {//begin try

            buffer = ByteBuffer.wrap(Files.readAllBytes(file.toPath()));

        } catch (IOException e) {//end try begin catch

            throw new RuntimeException("Create a file before loading it...");

        }//end catch

    }//end method





    /**
     * Summary: Set up this as a purely in memory table that will be destroyed at the end of this program's operation. 
     * @param nothing
     * @return nothing
     */
    @Override
    public void useInMemory() {//begin method

        buffer = ByteBuffer.allocate(0);//Just create an empty one.
        sourceIsInMemory = true;

    }//end method





    /**
     * Summary: Read "save". Due to the nature of this, only save is needed on close.
     * @param nothing
     * @return nothing
     */
    @Override
    public void saveAndClose() {//begin method

        save();//No closing is required as we do not use files

    }//end method




    /**
     * Summary: If applicable, save the data to its source file
     * @param nothing
     * @return nothing
     */
    @Override
    public void save() {//begin methood

        if (!sourceIsInMemory) {//begin if

            try {//begin try

                Files.write(activeFile.toPath(),buffer.array(),StandardOpenOption.WRITE);

            } catch (IOException e) {//end try begin catch

                throw new RuntimeException("Failed to save table!");

            }//end catch

        }//end if

    }//end method




    /**
     * Summary: Get the source file for this adapter, or null if ther eisn't one
     * @param nothing
     * @return the file or null
     */
    @Override
    public File getSourceFile() {//begin method

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
 * 
 * @Override
    public File getSourceFile() {//begin method

        return activeFile;

    }//end method
 */