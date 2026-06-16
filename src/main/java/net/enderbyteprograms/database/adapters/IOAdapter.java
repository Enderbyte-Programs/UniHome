package net.enderbyteprograms.database.adapters;

import java.io.File;

/**
 * Program Title: Database IO Adapter
 * 
 * Program Summary: An interface to allow abstraction between the filesystem and the database. This can be used to make a memory adapter or a filesystem based system
 * 
 * Program Elements List:
 * Interface, longs, arrays, bytes
 */
public interface IOAdapter {//begin interface

    //Standard stream methods, only the ones used by Table
    /**
     * Summary: Move the pointer to a positioon
     * @param position The position to move the pointer to
     * @return nothing
     */
    public void setPointer(long position);//Set the position of the pointer

    /**
     * Summary: Get the position of the pointer
     * @param nothing
     * @return the position of the pointer
     */
    public long getPointer();//Get the position of the pointer

    /**
     * Summary: Set the length of the buffer
     * @param size the size you want the buffer to be in bytes
     * @return nothing
     */
    public void setLength(long size);//Set the length of the array

    /**
     * Summary: Get the length of the bugger
     * @param nothing
     * @return the length of the buffer
     */
    public long getLength();//Get the length of the array

    /**
     * Summary: Write a single unsigned byte to the position of the pointer. Increment the pointer.
     * @param value the value to write. It must be no greater than 255.
     * @return nothing
     */
    public void writeByte(int value);//Write a single byte, unsigned, to the pointer's position.

    /**
     * Summary: Write an array of bytes starting at the pointer's position. Increment the pointer accordingly.
     * @param array the array of bytes to write
     * @return nothing
     */
    public void writeArray(byte[] array);//Write a byte array to the position

    /**
     * Summary: Read the byte at the pointer as an unsigned byte (value 0 to 255). Increment the pointer.
     * @param nothing
     * @return The byte's value unsigned
     */
    public int readUnsignedByte();//Reads the byte unsigned at the pointer position

    /**
     * Summary: Read the byte at the pointer as a signed byte. Increment the pointer.
     * @param nothing
     * @return The byte's value as a byte
     */
    public byte readByte();//Read the byte signed at the pointer position
    
    /**
     * Summary: Read bytes from the buffer, starting at the position of the pointer until the provided array is full. Increment the pointer accordingly.
     * @param array the array to read into
     * @return nothing, but the original array will have the data in it.
     */
    public void read(byte[] array);//Read the next <count> bytes at the pointer position INTO the provided array.

    //Now these are special ones
    /**
     * Summary: Tell this IO adapter that it needs to set the data *source* as a file of this path
     * @param file the file that has the data in it
     * @return nothing
     */
    public void useFile(File file);//Load a file from the filesystem into this.

    /**
     * Summary: Tell this IO adapter that the data source is "in memory" AKA created empty
     * @param nothing
     * @return nothing
     */
    public void useInMemory();//Tell the adapter that it will be using an in-memory table. The data source and the data change environment are DIFFERENT!

    /**
     * Summary: Do relevant actions before the end of the program, such as freeing up memory, closing files, or saving to files.
     * @param nothing
     * @return nothing
     */
    public void saveAndClose();//Will not be used for memory sources, but for FS loaded IM modified this will need to save the data and/or close the file.

    /**
     * Summary: If applicable, save the data stored here to its source
     * @param nothing
     * @return nothing
     */
    public void save();

    /**
     * Summary: Get the source file for this adapter, if applicable. It will return null if it is in memory only
     * @param nothing
     * @return the file that is being used
     */
    public File getSourceFile();
    
}//end interface
/**
 * Notes:
 * This would let people use either in memory or filesystem based database
 * 
 * TEst COde:
 * Nothing it's an interface
 */