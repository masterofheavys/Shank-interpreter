package icsi311;

import static java.lang.Float.parseFloat;

public class VariableNode extends Node
{
    private String name;
    private boolean isConstant;
    private descriptor dataType;
    private Node initialValue;

    public descriptor getDataType()
    {
        return dataType;
    }
    //Node initialValue;

    public String getName()
    {
        return name;
    }
    public boolean getIsConstant()
    {
        return isConstant;
    }
    public Node getInitialValue()
    {
        return initialValue;
    }
    VariableNode(String argName, boolean argConst)
    {
        name = argName;
        isConstant = argConst;
    }
    VariableNode(String argName, boolean argConst, descriptor argDescriptor)
    {
        name = argName;
        isConstant = argConst;
        dataType = argDescriptor;
    }
    VariableNode(String argName, boolean argConst, descriptor argDescriptor, Node argNode)
    {
        name = argName;
        isConstant = argConst;
        dataType = argDescriptor;
        initialValue = argNode;
    }
    @Override
    public String toString()
    {
        //create string with all properties of the variable
        String toReturn = (name + " ");
        if (isConstant == true)
        {
            toReturn += "CONSTANT ";
        }
        else
        {
            toReturn += "NOT CONSTANT ";
        }
        if(initialValue == null)
        {
            toReturn += "NO VALUE ASSIGNED ";
            toReturn += dataType;
            toReturn += ": ";
        }
        else
        {
            toReturn += initialValue.toString();
            toReturn += " ";
            if(initialValue instanceof FloatNode)
            {
                toReturn += "REAL:";
            }
            else
            {
                toReturn += "INTEGER:";
            }
            toReturn += " ";
        }
        return toReturn;
    }
}
