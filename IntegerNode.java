package icsi311;

public class IntegerNode extends Node
{
    private int integerStorage;

    public IntegerNode(int argumentInteger)
    {
        integerStorage = argumentInteger;
    }

    @Override
    public String toString() {
        return String.valueOf(integerStorage);
    }
    //getter method
    public int getIntegerStorage()
    {
        return integerStorage;
    }
}
