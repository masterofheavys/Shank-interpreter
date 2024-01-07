package icsi311;

public class MathOpNode extends Node
{
    private String typeIndicator;
    private Node leftNode;
    private Node rightNode;

    public MathOpNode(descriptor argumentIndicator,Node argumentLeftNode, Node argumentRightNode)
    {
        if(argumentIndicator == descriptor.PLUS)
        {
            typeIndicator = "+";
        }
        else if(argumentIndicator == descriptor.TIMES)
        {
            typeIndicator = "*";
        }
        else if (argumentIndicator == descriptor.MINUS)
        {
            typeIndicator = "-";
        }
        else if (argumentIndicator == descriptor.DIVIDE)
        {
            typeIndicator = "/";
        }
        else if(argumentIndicator == descriptor.MOD)
        {
            typeIndicator = "MOD";
        }
        leftNode = argumentLeftNode;
        rightNode = argumentRightNode;
    }
    //to IntegerNodes and FloatNodes are leaves,call their respective toString methods
    //a MathOpNode calls this function recursively, traverses tree until it hits all leaves
    @Override
    public String toString()
    {
        String toReturn = "";
        if(leftNode!= null)
        {
            toReturn+="MathOpNode(";
            toReturn += typeIndicator;
            toReturn += ",";
            toReturn += leftNode.toString();
            toReturn += ",";
        }
        if (rightNode != null)
        {
            toReturn += rightNode.toString();
            toReturn += ")";
        }

       return toReturn;
    }
    //getter methods
    public Node getLeftNode()
    {
        return leftNode;
    }
    public Node getRightNode()
    {
        return rightNode;
    }
    public String getTypeIndicator()
    {
        return typeIndicator;
    }
}
