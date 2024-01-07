package icsi311;

public class IntDataType extends InterpreterDataType
{
    private int value;
    public IntDataType(int argValue)
    {
        value = argValue;
    }
    //gets a value from a string
    @Override
    public void FromString(String input)
    {
        value = Integer.parseInt(input);
    }

    public int getValue()
    {
        return value;
    }
    public String ToString()
    {
        return String.valueOf(value);
    }
}
