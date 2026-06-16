package net.enderbyteprograms.database;

/**
 * Program Title: Comparison Modes
 * 
 * Program Summary: Modes that a Comparison object can take on: pure value or an expression
 * 
 * Program Elements List:
 * enum
 * 
 * @author Jordan Rahim
 * @version 1
 */

public enum ComparisonModes {//begin enum

    EXPRESSION,//bare value
    EQUALS,// x == y
    NEQUALS,// x != y
    COLEQUALS,//x == y where they are both column names (and values are loaded from column)
    COLNEQUALS,//x != y where they are both column names (and values are loaded from column)
    AND,// (expression) and (expression)
    OR// (expression) or (expression)
    
}//end enum
/**
 * Notes: No notes because this is super simple
 * 
 * TEst Code:
 * public enum ComparisonModes {
 * 
 * NOTHIG
 * That was one that used to be there but I removed it
 * 
 * }
 */