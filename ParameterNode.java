package icsi311;

public class ParameterNode extends Node
{
    private Node value;
    public ParameterNode(Node argValue)
    {
        value = argValue;
    }

    @Override
    public String toString()
    {
        return value.toString();
    }

    public Node getValue() {
        return value;
    }
}
