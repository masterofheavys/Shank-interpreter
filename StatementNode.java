package icsi311;

public class StatementNode  extends Node
{
    public String toString(Node argumentNode)
    {
        String toReturn = "";
        if(argumentNode instanceof AssignmentNode)
        {
            toReturn += argumentNode.toString();
        }
        return toReturn;
    }
}
