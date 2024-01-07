package icsi311;

public class CharNode extends Node
{
    private char charValue;

    public CharNode(char argumentChar)
    {
        charValue = argumentChar;
    }

    @Override
    public String toString() {
        return String.valueOf(charValue);
    }
    //getter method
    public char getCharValue()
    {
        return charValue;
    }
}
