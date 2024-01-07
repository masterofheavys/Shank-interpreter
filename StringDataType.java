package icsi311;

public class StringDataType extends InterpreterDataType
{
    private String value;
    public StringDataType(String argValue)
    {
        value = argValue;
    }
    //gets a value from a string
    @Override
    public void FromString(String input)
    {
        value = input;
    }

    public String getValue()
    {
        return value;
    }
    public String ToString()
    {
        return value;
    }
}
