package icsi311;

enum descriptor
{
    Number,
    MINUS,
    PLUS,
    EndOfLine,
    TIMES,
    DIVIDE,
    LEFTPARENTHESIS,
    RIGHTPARENTHESIS,
    WORD,
    DEFINE,
    REAL,
    BEGIN,
    END,
    INTEGER,
    CONSTANTS,
    VARIABLES,
    IDENTIFIER,
    COLON,
    SEMICOLON,
    EQUAL,
    COMMA,
    ASSIGNMENT,
    IF,
    THEN,
    ELSE,
    ELSIF,
    FOR,
    FROM,
    TO,
    WHILE,
    REPEAT,
    UNTIL,
    MOD,
    LESS,
    GREATER,
    GREATEROREQUAL,
    LESSOREQUAL,
    NOTEQUAL,
    VAR,
    TRUE,
    FALSE,
    STRING,
    CHARACTER,
    BOOLEAN,
    BOOLEANTYPE,
    STRINGTYPE,
    CHARACTERTYPE

}

public class Token
{
    private  descriptor localEnum;
    private  String valueString;

    public Token(descriptor argDescriptor)
    {
        localEnum = argDescriptor;
    }
    public Token(descriptor argDescriptor, String argValue)
    {
        localEnum = argDescriptor;
        valueString = argValue;
    }

    public String toString(Token arg)
    {
        String returnString = "";
        //if the token has the number or word enum, need to print both enum and the string storage
        if(arg.getLocalEnum().equals(descriptor.Number) || arg.getLocalEnum().equals(descriptor.WORD))
        {
            returnString += (arg.getLocalEnum() + "(" + arg.getValueString() + ") ");
        }
        //otherwise it's a symbol, only need to print enum
        else
        {
            returnString += (arg.getLocalEnum() + " ");
        }
        return returnString;
    }
    //getter functions needed to access enum and value string for the toString function
    public String getValueString()
    {
        return valueString;
    }
    public descriptor getLocalEnum()
    {
        return localEnum;
    }
    public void setValueString(String argString)
    {
        valueString = argString;
    }
}

