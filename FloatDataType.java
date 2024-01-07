package icsi311;

public class FloatDataType extends InterpreterDataType
{
    private float value;
    //get value from string
    public FloatDataType(float argValue)
    {
        value = argValue;
    }
    @Override
    public void FromString(String input)
    {
        value = Float.parseFloat(input);
    }

    //to string
    public String ToString()
    {
        return String.valueOf(value);
    }

    //getter method
    public float getValue() {
        return value;
    }
}

