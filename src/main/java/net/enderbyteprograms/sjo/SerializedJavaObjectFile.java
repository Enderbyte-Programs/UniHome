package net.enderbyteprograms.sjo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

//Import statements


/**
 * Program Title: SJO file reader and writer
 * 
 * Program Summary: Read and write Serialized Java Object Files (or SJO) which is another experimental storage format I am using
 * 
 * Program Elements List:
 * TODO
 * 
 * @author Jordan Rahim
 * @version 1
 */
public class SerializedJavaObjectFile<T extends SafetySerializable> {//begin class

    //vars
    public File path;



    /**
     * Summary: Create a new SJO file
     * 
     * @param filePath the path to the file (whether it exists or not)
     * @return SJOF object
     */
    public SerializedJavaObjectFile(File filePath) {//begin constructor

        path = filePath;

        try {
            if (!filePath.exists()) {
                Files.createFile(filePath.toPath());
            }
        } catch (Exception e) {

        }

    }//end constructor




    /**
     * Summary: Read from an SJO file
     * @param nothing
     * @return a list of objects read from this file
     */
    @SuppressWarnings("unchecked")//Stop Java complaining when I cast into the type T. I couldn't find a better way to get around this
    public List<T> read() {//begin method

        ArrayList<T> result;
        List<String> lines;
        Decoder base64Decoder;
        ObjectInputStream objectInputStream;
        ByteArrayInputStream byteInputStream;
        Object rawReadObject;
        T parsedObject;
        byte[] decodedBase64;

        try {//begin try

            result = new ArrayList<T>();
            lines = Files.readAllLines(path.toPath());
            base64Decoder = Base64.getDecoder();

            for (String b64Line:lines) {//begin for

                decodedBase64 = base64Decoder.decode(b64Line);
                byteInputStream = new ByteArrayInputStream(decodedBase64);
                objectInputStream = new ObjectInputStream(byteInputStream);
                rawReadObject = objectInputStream.readObject();
                
                parsedObject = (T)rawReadObject;
                parsedObject.safetyCheck();
                result.add(parsedObject);

            }//end for

        } catch (IOException ioe) {//end try begin catch

            throw new RuntimeException("Critical IOERROR",ioe);

        } catch (ClassNotFoundException cnfe) {//end catch begin catch

            throw new RuntimeException("Unsupported object type",cnfe);
            
        }//end catch

        return result;

    }//end method




    /**
     * Summary: Write a collection of objects into the file, completely replacing the original contents
     * @param objects the objects to write, of type T
     * @return nothing
     */
    public void write(Collection<T> objects) {//begin method

        Encoder base64Encoder;
        ByteArrayOutputStream outputStream;
        ObjectOutputStream objectOutputStream;
        StringBuilder fileBuilder;

        try {//begin try

            base64Encoder = Base64.getEncoder();
            fileBuilder = new StringBuilder();

            for (Object object:objects) {//begin for

                outputStream = new ByteArrayOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(object);
                fileBuilder.append(base64Encoder.encodeToString(outputStream.toByteArray()));
                fileBuilder.append("\n");//Newline char

            }//end for

            Files.writeString(path.toPath(),fileBuilder.toString());

        } catch (IOException ioe) {//end try begin catch

            throw new RuntimeException("Critical IO error",ioe);

        }//end catch
            
    }//end method

}//end class
/**
 * Notes:
 * TODO
 * 
 * Test Code:
 * TODO
 */