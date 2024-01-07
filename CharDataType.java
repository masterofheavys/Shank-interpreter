package icsi311;

public class CharDataType extends InterpreterDataType
{
    private char value;
    public CharDataType(char argValue)
    {
        value = argValue;
    }
    //gets a value from a string
    @Override
    public void FromString(String input)
    {
        value = input.charAt(0);
    }

    public char getValue()
    {
        return value;
    }
    public String ToString()
    {
        return String.valueOf(value);
    }
}
