package net.enderbyteprograms.database;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
 * Program Title: Result Set
 * 
 * Program Summary: A class to return a list, but permit sorting as well! This is my first use of class extensions.
 * 
 * Program Elements List:
 * Extension,
 * Sorting
 * Database joins
 * 2-staged loops (i, j) - what Mr. Vatougios has o the board
 * Date
 * Comparators and comparisons
 * 
 * @author Jordan Rahim
 * @version 1
 */
public class ResultSet extends ArrayList<ResultRow> {//begin class

    //No custom instance vars
    
    
    
    
    /**
     * Summary: Load in a result set from an unconverted list.
     * @param inboung the list
     * @return the list as a resultset
     */
    public ResultSet(List<ResultRow> inboung) {//begin constructor

        super();//Use the super-constructor to make a new empty list
        
        for (ResultRow entry:inboung) {//begin for

            this.add(entry);

        }//end for

    }//end constructor




    /**
     * Summary: Make a new empty list
     * @param nothing
     * @return this
     */
    public ResultSet() {//begin constructor

        super();

    }//end constructor





    /**
     * Summary: Sort the results by a column with a direction,  returning this list again so it can be chained
     * @param column the column name to sort by
     * @param direction EIther SortDirection.ASCENDING or SortDirection.DESCENDING to sort in a certain direction
     * @return this list again
     */
    public ResultSet sortBy(String column,SortDirection direction) {//begin method

        //Inline classes go go go
        this.sort(new Comparator<ResultRow>() {//begin inline class

            /**
             * Summary: Compare one result row to another, taking into account the requested column and sort direction
             * @param row1 the first row
             * @param row2 the next row
             * @return -1 if row1's val > row2's val; 1 if row1's val > row2's val; 0 if they are the same
             */
            @Override
            public int compare(ResultRow row1,ResultRow row2) {//begin inline method

                //variables. As you can see, there are a lot of possible options. Many of them will ot be used.
                DataTypes requestedDT;
                Date possibleRow1Date;
                Date possibleRow2Date;
                String possibleRow1String;
                String possibleRow2String;
                Double possibleRow1Double;
                Double possibleRow2Double;
                Integer possibleRow1Int;
                Integer possibleRow2Int;
                int result;
                boolean possibleRow1Boolean;
                boolean possibleRow2Boolean;
                
                requestedDT = row1.schema.get(column);

                switch (requestedDT) {//begin switch

                    case Boolean:
                        possibleRow1Boolean = row1.getBool(column);
                        possibleRow2Boolean = row2.getBool(column);

                        if (possibleRow1Boolean && (!possibleRow2Boolean)) {//begin if

                            result = 1;

                        } else {//end if begin else

                            if (possibleRow1Boolean == possibleRow2Boolean) {//begin if

                                result = 0;

                            } else {//end if begin else

                                result = -1;

                            }//end else

                        }//end else

                        break;

                    case DateTime:
                        possibleRow1Date = row1.getDateTime(column);
                        possibleRow2Date = row2.getDateTime(column);
                        result = possibleRow1Date.compareTo(possibleRow2Date);
                        break;
                        
                    case Double:
                        possibleRow1Double = row1.getDouble(column);
                        possibleRow2Double = row2.getDouble(column);
                        result = possibleRow1Double.compareTo(possibleRow2Double);
                        break;

                    case Integer:
                        possibleRow1Int = row1.getInt(column);
                        possibleRow2Int = row2.getInt(column);
                        result = possibleRow1Int.compareTo(possibleRow2Int);
                        break;

                    case ShortString:
                    case MediumString:
                    case LargerString:
                    case String:
                        possibleRow1String = row1.getString(column);
                        possibleRow2String = row2.getString(column);
                        result = possibleRow1String.compareTo(possibleRow2String);
                        break;

                    default:
                        result = 0;
                        break;  

                }//end switch


                if (direction == SortDirection.DESCENDING) {//begin if

                    result = -result;//Invert the result if we want a reversed sort

                }//end if

                return result;

            }//end inline method

        });//end inline class

        return this;

    }//end method





    /**
     * Summary: Join two result sets to each other using inner join, where the right side is matched onto a row on the left.
     * @param otherSet The other dataset to join
     * @param shouldMergeDiscriminator The comparison (as a result of Comparison.ofColumnComparison)
     * @return the result set containing the joined data
     */
    public ResultSet join(ResultSet otherSet,CustomComparison shouldMergeDiscriminator) {//begin method

        //vars
        ResultSet result;
        ResultRow tempRow;
        
        result = new ResultSet();//Make a new empty result set

        //This is an inner join system. For every row on the left side, the first matching row on the right side is connected. Only rows that have a match are returned.
        for (ResultRow leftSideRow:this) {//begin for

            for (ResultRow possibleRightSideRow:otherSet) {//begin for

                tempRow = new ResultRow(leftSideRow,leftSideRow.schema);

                //We will manually append the items to the left side row to create a temporary full row.
                //First, add the schema
                tempRow.schema.putAll(possibleRightSideRow.schema);
                //Then, add the data (possibly overwritig things, but thatt is what namespaces are for)
                tempRow.putAll(possibleRightSideRow);

                if (shouldMergeDiscriminator.shouldAccept(tempRow)) {//begin if

                    result.add(tempRow);

                }//end if

            }//end for

        }//end for

        return result;

    }//end method





    /**
     * Summary: Limit this resultset to the size of N rows, being the first ones on this list
     * @param n the number of rows to keep
     * @return this result set, limited
     */
    public ResultSet limitToFirst(int n) {//begin methood

        while (size() > n) {//begin while

            remove(n);

        }//end while

        return this;

    }//end method





    /**
     * Summary: Limit this resultset to the size of N rows, being the last ones on this list
     * @param n the number of rows to keep
     * @return this result set, limited
     */
    public ResultSet limitToLast(int n) {//begin methood

        while (size() > n) {//begin while

            remove(0);//Remove ths first items

        }//end while

        return this;

    }//end method





    /**
     * Summary: Lock out this Result Set into a static array of result rows. This was made to comply with the No Dynamic Array rule in project 5. I sure hope that dictionaries are also not forbidden.
     * @param nothing
     * @return a static array version of this result set
     */
    public ResultRow[] toStaticArray() {//begin method

        //vars
        ResultRow[] result;

        result = new ResultRow[this.size()];

        for (int i = 0; i < this.size(); i++) {//begin for

            result[i] = this.get(i);//Copy items of myself to the static array

        }//end for

        return result;

    }//end method





    /**
     * Summary: Convert this ResultSet to its base type form, that is a list of maps
     * @param nothing
     * @return the converted and genericzied object because polymorphism is somehow not working...
     */
    public List<Map<String,Object>> toDynamicGenericList() {//begin methood

        //vars
        List<Map<String,Object>> result;

        result = new ArrayList<Map<String,Object>>();
        result.addAll(this);
        return result;
        
    }//end method
    
}//end class
/**
 * NOTES: Hmm how will I do extension?
 * 
 * Test Code:
 * comparator
 *     public ResultSet() {//begin constructor

        super();

    }//end constructor
 */