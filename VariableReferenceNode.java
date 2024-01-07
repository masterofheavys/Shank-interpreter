package icsi311;

public class VariableReferenceNode  extends Node
{
    private String target;

    public VariableReferenceNode(String argString)
    {
        target = argString;
    }
    public String toString()
    {
        return target;
    }

    public String getTarget()
    {
        return target;
    }
}
