package icsi311;

public class BoolNode extends Node
{
    private boolean booleanValue;

    public BoolNode(boolean argumentBoolean)
    {
        booleanValue = argumentBoolean;
    }

    @Override
    public String toString() {
        return String.valueOf(booleanValue);
    }
    //getter method
    public boolean getBooleanStorage()
    {
        return booleanValue;
    }
}
