package net.enderbyteprograms.database;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import net.enderbyteprograms.Utilities;

/**
 * Program Title: Datatypes Enum
 * 
 * Program Summary: Storage and handling for datatypes, which are used in the database.
 * 
 * Program Elements List:
 * enum
 * log2
 * Low level byte stuff
 * 
 * @author Jordan Rahim
 * @version 2026.01.28
 * @date January 28 2026
 */

public enum DataTypes {
    String(3,256),//General long string
    LargerString(3,64),//64 characters
    MediumString(3,32),//Longer string for UUID and username
    DateTime(4,4),//Pretty much just a fancy integer
    ShortString(3,16),//16 bytes means 16 characters. 
    Double(2,8),//Double needs 2x long so 16. 2^4
    Integer(0,4),//2^32 fits into 4 bytes
    Boolean(1,1);//Just needs one byte 0000 0000 or 1111 1111
    //Storage in bits - |||| < number representation of id 0-15, |||| < number representation of power of 2. 2^0 = 1 byte, up to 2^15






    public final int id;
    public final int neededBytes;
    public final byte byteRepresentation;






    /**
     * Summary: Private constructor for a data type
     * @param id the internal id of the data type
     * @param neededBytes the amount of bytes the data type needs
     * @return the finished object
     */
    private DataTypes(int id,int neededBytes) {//begin constructor

        //Variables
        int idRepr;
        int neededBytesRepr;
        int finalIntegerRepr;

        this.id = id;
        this.neededBytes = neededBytes;

        idRepr = (id * 16);//Bitshift so that the representation is ____0000. The last four bits are for needed bytes
        neededBytesRepr = Utilities.log2(neededBytes);
        finalIntegerRepr = (idRepr + neededBytesRepr);

        byteRepresentation = (byte)finalIntegerRepr;

    }//end constructor




    /**
     * Summary: Check if an object is acceptable based on this data type.
     * @param o the object to check if it is compatible witth 
     * @return whether or not the objec tis acceptable
     */
    public boolean objectIsAcceptable(Object o) {//begin method

        byte[] byteStrRepr;

        if ((o instanceof Integer) && (this.id == 0)) {//begin if
            
            return true;

        } else if ((o instanceof Boolean) && (this.id == 1)) {//end if begin if

            return true;

        } else if ((o instanceof Double) && (this.id == 2)) {//end if begin if

            return true;

        } else if ((o instanceof Date) && (this.id == 4)) {//end if begin if

            return true;

        } else if ((o instanceof String) && (this.id == 3)) {//end if begin if

            byteStrRepr = ((String)(o)).getBytes(StandardCharsets.UTF_8);
            
            if (byteStrRepr.length <= this.neededBytes) {//begin if

                return true;

            }//end if
            
        }//end if
        
        return false;

    }//end method





    /**
     * Load the associated data type from the single-byte definition. This relies only on ID
     * @param definition the definition byte from the file, unsigned
     * @return the appropriate datatype
     */
    public static DataTypes fromDefinition(int definition) {

        switch (definition) {//begin switch

            //This is probably really bad design... I'm using a fancy dynamic system to create this definition number, but then this static list toreturn it. 

            case 16:
                return Boolean;
            
            case 2:
                return Integer;

            case 35:
                return Double;
            
            case 52:
                return ShortString;

            case 53:
                return MediumString;

            case 54:
                return LargerString;

            case 66:
                return DateTime;

            case 56:
                return String;

            default:
                throw new RuntimeException("Unknown or malformed datatype definition");

        }//end switch

    }

}
/**
 * Notes: It is a bad design I have used in fromdefinition but I'm too lazy to fix it right now
 * 
 * Test Code: //Variables
        int idRepr;
        int neededBytesRepr;
        int finalIntegerRepr;

        this.id = id;
        this.neededBytes = neededBytes;

        idRepr = (id * 16);//Bitshift so that the representation is ____0000. The last four bits are for needed bytes
        neededBytesRepr = Utilities.log2(neededBytes);
        finalIntegerRepr = (idRepr + neededBytesRepr);

        byteRepresentation = (byte)finalIntegerRepr;
 */