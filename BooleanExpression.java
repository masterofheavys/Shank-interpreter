package icsi311;

public class BooleanExpression extends Node
{
    private descriptor operator;
    private Node leftCondition;
    private Node rightCondition;
    public BooleanExpression(Node argLeftCondition, descriptor argOperator,Node argRightCondition)
    {
        leftCondition = argLeftCondition;
        operator = argOperator;
        rightCondition = argRightCondition;

    }
    public Node getLeftCondition()
    {
        return leftCondition;
    }
    public Node getRightCondition()
    {
        return rightCondition;
    }
    public descriptor getOperator()
    {
        return operator;
    }

    @Override
    public String toString()
    {
        Interpreter localInterpreter = new Interpreter();
        String toReturn = "";
        //check if math needs to be performed
        if(leftCondition instanceof MathOpNode)
        {
            toReturn += localInterpreter.resolve(leftCondition);
        }
        else
        {
            toReturn += leftCondition.toString();
        }
        toReturn += " ";
        //get operator
        if (operator == descriptor.LESS)
        {
            toReturn += "<";
        }
        else if (operator == descriptor.GREATER)
        {
            toReturn += ">";
        }
        else if (operator == descriptor.EQUAL)
        {
            toReturn += "=";
        }
        else if (operator == descriptor.NOTEQUAL)
        {
            toReturn += "<>";
        }
        else if (operator == descriptor.LESSOREQUAL)
        {
            toReturn += "<=";
        }
        else if (operator == descriptor.GREATEROREQUAL)
        {
            toReturn += ">=";
        }
        toReturn += " ";
        //check if math needs to be performed
        if(rightCondition instanceof MathOpNode)
        {
            toReturn += rightCondition.toString();
        }
        else
        {
            if (rightCondition != null)
            {
                toReturn += rightCondition.toString();
            }
        }
        return toReturn;
    }
}
