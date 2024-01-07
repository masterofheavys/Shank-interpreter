package icsi311;

public class BoolDataType extends InterpreterDataType
{
    private boolean value;
    public BoolDataType(boolean argValue)
    {
        value = argValue;
    }
    //gets a value from a string
    @Override
    public void FromString(String input)
    {
        value = Boolean.parseBoolean(input);
    }

    public boolean getValue()
    {
        return value;
    }
    public String ToString()
    {
        return String.valueOf(value);
    }
}
