package icsi311;

public class StringNode extends Node
{
    private String stringValue;

    public StringNode(String argumentString)
    {
        stringValue = argumentString;
    }

    @Override
    public String toString() {
        return stringValue;
    }
    //getter method
    public String getStringStorage()
    {
        return stringValue;
    }
}
