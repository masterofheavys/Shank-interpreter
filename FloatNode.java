package icsi311;

public class FloatNode extends Node
{
    private float floatStorage;

    public FloatNode(float argumentFloat)
    {
        floatStorage = argumentFloat;
    }

    @Override
    public String toString() {
        return String.valueOf(floatStorage);
    }
    //getter method
    public float getFloatStorage()
    {
        return floatStorage;
    }
}
