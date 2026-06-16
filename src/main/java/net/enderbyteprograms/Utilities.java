package net.enderbyteprograms; 
//Import statements

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Program Name: Utility Function Program
 * 
 * Program Summary:
 * Assorted static utility methods that will be re-used across assignments. These include a method to get the user's input, a method to get the user's input as an integer, and a method to count the occurences of a substring in a string.
 * 
 * Program elements:
 * getInput, getInt, countOccurences.
 * Each of these are static methods (functions).
 *
 * @author Jordan Rahim
 * @version 1.0
 * @date 2025-11-05
 */
public class Utilities
{//begin class

    public static Scanner scanner;
    public static Random randomGenerator = new Random();
    
    
    
    /**
     * Summary: Constructor for Utilities, which is a static class anyway, so it does nothing
     * @param nothing
     * @return a userless Utilities object
     */
    public Utilities() {
        //begin constructor
        //no code
    }//end constructor




    /**
     * Summary: Generate a random integer from min to max
     * 
     * @param min the lowest number that may be generated
     * @param max the highest number that may be generated
     * @return the generated integer
     */
    public static int getRandomInt(int min,int max) {//begin method

        return randomGenerator.nextInt(max - min + 1) + min;

    }//end method




    /**
     * Summary: Get a string input in a non-managed context
     * 
     * @param prompt The prompt to give to the user
     * @return what the user enters
     */
    public static String getInput(String prompt) {
        //begin method
        //local variable declaration
        String res;
        
        scanner = new Scanner(System.in);
        System.out.print(prompt + " >>>");
        res = scanner.nextLine();
        scanner.close();

        return res;
    }//end method getInput





    /**
     * Summary: Force the user to enter a number
     * @param prompt the prompt to give the user
     * @return what the user entered
     */
    public static int getInt(String prompt) {
        //begin method
        //local variable declarations
        String temporaryResult;
        int result;


        while (true) {//begin while

            temporaryResult = getInput(prompt);

            try {//begin try

                result = Integer.parseInt(temporaryResult);
                //If the input can be parsed into text, then it is OK to break
                break;

            } catch (NumberFormatException e) {//end try begin catch

                System.out.println("Please specify an integer.");
                
            }//end catch

        }//end while


        return result;
        
    }//end method getInt





    /**
     * Summary: Get the length of the longest item in an array of strings
     * @param ar an array of strings
     * @return the length of the longest item in the array of strings
     */
    public static int getMaxLengthInStringArray(String[] ar) {//begin method

        //vars
        int result;
        
        result = 0;

        for (String s:ar) {//begin for

            if (s.length() > result) {//begin if

                result = s.length();

            }//end if

        }//end for

        return result;

    }//end method

    
    
    
    /**
    * Summary: Count the instances of ofChar in s
    * 
    * @param s The string from which we should count values
    * @param ofChar The character to count
    * @return the count
    */
    public static int countOccurences(String s,char ofChar) {
        //local variable declarations
        int count;
        
        count = 0;
        
        for (char c:s.toCharArray()) {//begin for
            
            if (c == ofChar) {//begin if
                
                count++;
                
            }//end if
            
        }//end for
        
        return count;
        
    }//end method countOccurences





    /**
     * Summary: Convert a number into a friendly data representation. Eg. 1648374 -> 1.65 MB
     * @param size the size to convert
     * @return the human readable string
     */
    public static String parseSize(long size) {//begin method

        if (size > 1000000000) {//begin if

            return String.format("%.2f GB",size / 1000000000D);//Hopefully this forces a Double value

        } else if (size > 1000000) {//end if begin if

            return String.format("%.2f MB",size / 1000000D);

        } else if (size > 1000) {//end if begin if

            return String.format("%.2f KB",size / 1000D);

        } else {//end if begin else

            return String.format("%d bytes",size);

        }//end else

    }//end method





    /**
     * Summary: Convert a number into a friendly data representation. Eg. 1648374 -> 1.65 MB
     * @param size the number to humanize
     * @param units the units to use
     * @return the human readable string
     */
    public static String humanizeNumber(long size,String units) {//begin method

        if (size > 1000000000) {//begin if

            return String.format("%.2fB %s",size / 1000000000D,units);//Hopefully this forces a Double value

        } else if (size > 1000000) {//end if begin if

            return String.format("%.2fM %s",size / 1000000D,units);

        } else if (size > 1000) {//end if begin if

            return String.format("%.2fK %s",size / 1000D,units);

        } else {//end if begin else

            return String.format("%d %s",size,units);

        }//end else

    }//end method





    /**
     * Summary: Create a 16 byte long hash of the provided string
     * @param inbound the string you want to hash
     * @return hexadecimal string representation of hash
     */
    public static String getSha128Hash(String inbound) {//begin method

        MessageDigest hasher;
        byte[] rawDigest;
        String result;

        try {//begin try

            hasher = MessageDigest.getInstance("SHA-256");
            rawDigest = hasher.digest(inbound.getBytes(StandardCharsets.UTF_8));
            result = String.format("%064x", new BigInteger(1, rawDigest));
            result = result.substring(0,16);

        } catch (NoSuchAlgorithmException e) {//end try begin catch

            return "theresnoalgorithm";

        }//end catch

        return result;

    }//end method




    /**
     * Summary: Trim a string to s specified length
     * @param s the string to trim
     * @param toLength The amount of characters to limit 2
     * @return the trimmed string
     */
    public static String trimString(String s,int toLength) {//begin method

        //variables (none)
        
        if (s.length() > toLength) {//begin if

            return s.substring(0,toLength);

        }//end if

        return s;

    }//end method






    /**
     * Summary: Get a floored log base 2 for any integer. For instance 1 returns 0, 2 returns 1, 4 returns 2, etc...
     * @param ingress the number you want to do the log2 on 
     * @return the log2 of ingress, floored
     */
    public static int log2(int ingress) {//begin method

        //vars
        int count;
        
        count = 0;

        while (ingress > 1) {//begin while

            count++;
            ingress /= 2;//Should do integer division, eg 128 /= 2 -> 64

        }//end while

        return count;

    }//end method
    
    
    
    
    /**
     * Summary: Sleep for the specified number of milliseconds
     * @param m the number of milliseconds to sleep for
     * @return nothing
     */
    public static void sleep(int m) {//begin method
        
        try {//begin try
            
            TimeUnit.MILLISECONDS.sleep(m);
            
        } catch (InterruptedException e) {//end try begin catch
            
            return;
            
        }//end catch
    
    }//end method



    /**
     * Summary: Unsign a byte value
     * @param b the byte
     * @return the unsigned version as an int
     */
    public static int unsignByte(byte b) {//begin method

        return b & 0xFF;

    }//end method





    /**
     * Summary: Get a double from an object, because Java type casting likes to complain
     * @param o the object to convert into a double
     * @return the double version of that object
     */
    public static double getDouble(Object o) {//begin methodf

        if (o instanceof Double) {//begin if

            return ((Double)o).doubleValue();

        } else if (o instanceof Integer) {//end if begin if

            return ((Integer)o).doubleValue();

        }//end if

        return 0D;

    }//end method





    /**
     * Summary: Shift all values in an array of doubles to the left, and append a value in the last position. This modifies the array IN PLACE.
     * @param array The array of doubles to shift
     * @param valueToAppend the new value at the end of the list
     */
    public static void shiftAndAppend(double[] array,double valueToAppend) {//begin method

        for (int i = 0; i < (array.length - 1); i++) {//begin for

            array[i] = array[i + 1];//Shift to the left

        }//end for

        //Set the last item
        array[array.length - 1] = valueToAppend;

    }//end method





    /**
     * Summary: Round a double to a specific number of places
     * @param value the double to round
     * @param places the number of places to round to
     * @return the value
     */
    public static double round(double value,int places) {//begin method

        //results
        double raised;
        long rounded;
        double result;

        raised = value * Math.pow(10,places);
        rounded = Math.round(raised);
        result = rounded / Math.pow(10,places);

        return result;

    }//end method





    /**
     * Summary: Check if a string is an integer without using exceptions
     * @param potentialValue the value to check for safety
     * @return true if it is safe false if it isnt'
     */
    public static boolean isIntegerSafe(String potentialValue) {//begin method

        try {//begin try

            Integer.parseInt(potentialValue);
            return true;
            
        } catch (Exception e) {//end try begin catch

            return false;
            
        }

    }//end method





    /**
     * Summary: Get the mean of a list
     * @param array the list or collection object to get the mean of
     * @return the mean value of this list
     */
    public static double getMean(Collection<Double> array) {//begin method

        //vars
        double sum = 0;

        for (Double d:array) {//begin for

            sum += d;

        }//end for

        return (sum / (double)(array.size()));

    }//end method





    /**
     * Summary: Get the median of a list
     * @param array the list object to get the median value of
     * @return the median value of this list
     */
    public static double getMedian(List<Double> array) {//begin method

        Collections.sort(array);

        return array.get(array.size() / 2);

    }//end method
    
}//end class
//end program

/*
 * NOTES
 * 
 * This class has now been reused 3 times
 * 
 * Test code:
 * 
 * public static String getSha128Hash(String inbound) {//begin method

        MessageDigest hasher;
        byte[] rawDigest;
        String result;

        try {//begin try

            hasher = MessageDigest.getInstance("SHA-256");
            rawDigest = hasher.digest(inbound.getBytes(StandardCharsets.UTF_8));
            result = String.format("%064x", new BigInteger(1, rawDigest));
            result = result.substring(0,16);

        } catch (NoSuchAlgorithmException e) {//end try begin catch

            return "theresnoalgorithm";

        }//end catch

        return result;

    }//end method
 */