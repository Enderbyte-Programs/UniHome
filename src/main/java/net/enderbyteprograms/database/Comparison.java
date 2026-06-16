package net.enderbyteprograms.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Program Title: Static comparison class
 * 
 * Program Summary: Construct complex logical requirements using chains and chains of comparisons
 * 
 * Program Elements List:
 * Enums
 * Classes
 * erm
 * recursion I guess
 * ands
 * ors
 * 
 * @author Jordan Rahim
 * @version 1
 * @date 2026-02-29 for good measure eh
 */
public class Comparison implements CustomComparison {//begin class

    public ComparisonModes mode;
    public Comparison child;
    public Object value;
    protected List<Comparison> andOrStatements;
    protected String equalsComparisonColumn;
    protected String equalsComparisonColumn2;//Only used if we are doing a column compare






    /**
     * Summary: Create a bare and empty object for private use
     * @param nothing
     * @return nothing
     */
    private Comparison() {//begin constructor

        //Make a bare object

    }//end constructor





    /**
     * Summary: Create a comparison with a child comparison
     * @param child the child comparison
     * @return the object
     */
    public Comparison(Comparison child) {//begin constructor

        mode = ComparisonModes.EXPRESSION;//General expression - type unknown. If and or or is applied, will switch appropriately
        andOrStatements = new ArrayList<Comparison>();
        andOrStatements.add(child);

    }//end constructoir





    /**
     * Summary: Create a new equals comparison expression, that columnName in the row must be of expectedValue
     * @param columnName the column name to check
     * @param expectedValue the expected value
     * @param negate FALSE for a normal equals, TRUE for a not equals
     * @return the object
     */
    public Comparison(String columnName,Object expectedValue,boolean negate) {//begin method

        if (negate) {//begin else

            this.mode = ComparisonModes.NEQUALS;

        } else {//end if begin else

            this.mode = ComparisonModes.EQUALS;

        }//end else

        equalsComparisonColumn = columnName;
        value = expectedValue;

    }//end method





    /**
     * Summary: Create a new equals or !equals comparison expression, except for this one we are saying that the values in two column names must be the same.
     * @param firstColumnName the column name
     * @param secondColumnName the column name, whose value must be the same as the value in the first column.
     * @param negate If true, this goes from an equals statement to a not equals
     */
    public static Comparison ofColumnComparison(String firstColumnName,String secondColumnName,boolean negate) {//begin method

        //vars
        Comparison result;

        result = new Comparison();
        
        if (negate) {//begin if

            result.mode = ComparisonModes.COLNEQUALS;

        } else {//end if begin else

            result.mode = ComparisonModes.COLEQUALS;

        }//end else

        
        result.equalsComparisonColumn = firstColumnName;
        result.equalsComparisonColumn2 = secondColumnName;

        return result;

    }//end method





    /**
     * Summary: Get the boolean result of whether it should be accepted from down the line
     * @param row the current data row
     * @return whether or not the row should be accepted
     */
    @Override
    public boolean shouldAccept(Map<String,Object> row) {//begin method

        boolean tempVal;


        if (this.mode == ComparisonModes.EXPRESSION) {//begin if

            throw new RuntimeException("Comparsion must be initialized with equals, nequals, and, or or");

        }//end if

        switch (this.mode) {//begin switch

            case EQUALS:
                return row.get(equalsComparisonColumn).equals(value);

            case NEQUALS:
                return !(row.get(equalsComparisonColumn).equals(value));

            case COLEQUALS:
                return row.get(equalsComparisonColumn).equals(row.get(equalsComparisonColumn2));

            case COLNEQUALS:
                return !(row.get(equalsComparisonColumn).equals(row.get(equalsComparisonColumn2)));

            case AND:

                tempVal = true;

                for (Comparison c:this.andOrStatements) {//begin for

                    if (!c.shouldAccept(row)) {//begin if

                        tempVal = false;//Lose one lose all

                    }

                }//end for

                return tempVal;

            case OR:
                
                tempVal = false;
                for (Comparison c:this.andOrStatements) {//begin for

                    if (c.shouldAccept(row)) {//begin if

                        tempVal = true;//Win one win all

                    }//end if

                }//end for

                return tempVal;

            default:
                throw new RuntimeException("It is unacceptable to evaluate the condition in this state.");

        }//end switch

    }//end method





    /**
     * Summary: Add an AND statement, that is, add a condition that must be met with an AND
     * @param andStatement the child comparison that must be met
     * @return the object for easy chaining
     */
    public Comparison and(Comparison andStatement) {//begin method

        if (!checkRequestedOperation(ComparisonModes.AND)) {//begin if

            throw new RuntimeException("An AND operation may not be chained to this condition.");

        }//end if

        andOrStatements.add(andStatement);
        mode = ComparisonModes.AND;

        return this;//For easy chaining

    }//end method






    /**
     * Summary: Add an OR statement, that is, add a condition that must be met with an OR
     * @param orStatement the child comparison that must be met
     * @return the object for easy chaining
     */
    public Comparison or(Comparison orStatement) {//begin method

        if (!checkRequestedOperation(ComparisonModes.OR)) {//begin if

            throw new RuntimeException("An OR operation may not be chained to this condition.");

        }//end if

        andOrStatements.add(orStatement);
        mode = ComparisonModes.OR;

        return this;//For easy chaining

    }//end method




    /**
     * Summary: Check if the requested operation (either an AND or an OR) is acceptable, as you cannot mix ands and ors on the same object. 
     * @param requestedOperation either ComparisonModes.AND or ComparisonModes.OR to represent the requested operation
     * @return true if acceptable false if unacceptable
     */
    private boolean checkRequestedOperation(ComparisonModes requestedOperation) {//begin method
    
        if (this.mode == ComparisonModes.EXPRESSION) {//begin if

            return true;//Is not initialized so anything is OK

        }//end if

        if ((this.mode == ComparisonModes.EQUALS) || (this.mode == ComparisonModes.NEQUALS)) {//begin if

            return false;//Equals and Nequals are "end of the line" - no more chaining allowed

        }//end if

        //Ooh logic...
        return (requestedOperation == this.mode);//Now that exceptions are out of the way I can compare direct

    }//end method

    
}//end class

/**
 * Notes: Intended syntax is
 * new Comparison(new Comparison(new Comparison(new Comparison(a).equals(w)).and(new Comparison(b).equals(x))).or(new Comparison(new Comparison(c).equals(y)).and(new Comparison(d).equals(z))))
 * 
 * Test Code:
 * private boolean checkRequestedOperation(ComparisonModes requestedOperation) {//begin method
    
        if (this.mode == ComparisonModes.EXPRESSION) {//begin if

            return true;//Is not initialized so anything is OK

        }//end if

        if ((this.mode == ComparisonModes.EQUALS) || (this.mode == ComparisonModes.NEQUALS)) {//begin if

            return false;//Equals and Nequals are "end of the line" - no more chaining allowed

        }//end if

        //Ooh logic...
        return (requestedOperation == this.mode);//Now that exceptions are out of the way I can compare direct

    }//end method
 */