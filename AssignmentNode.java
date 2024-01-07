package icsi311;

import java.util.concurrent.atomic.AtomicReference;

public class AssignmentNode extends StatementNode
{
    //to evaluate expressions in math op node
    Interpreter localInterpreter = new Interpreter();
    private VariableReferenceNode localVariableReference;
    private Node expression;
    public AssignmentNode(VariableReferenceNode argVariableReferenceNode, Node argExpression)
    {
        expression = argExpression;
        localVariableReference = argVariableReferenceNode;
    }
    public VariableReferenceNode getLocalVariableReference()
    {
        return localVariableReference;
    }
    public Node getExpression()
    {
        return expression;
    }
    public String toString()
    {
        String toReturn = "";
        toReturn += (localVariableReference).toString();
        toReturn += " := ";
        //check if math needs to be performed
        if(expression instanceof MathOpNode)
        {
            toReturn += localInterpreter.resolve(expression);
        }
        else
        {
            toReturn += expression.toString();
        }
        return toReturn;
    }
}
