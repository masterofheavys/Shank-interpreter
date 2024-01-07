package icsi311;

import java.util.ArrayList;

public class ForStatement extends StatementNode
{
    private VariableReferenceNode counter;
    private ArrayList<StatementNode> forStatementList = new ArrayList<>();
    private Node Start;
    private Node End;

    public ForStatement(VariableReferenceNode argCounter,Node argStart, Node argEnd, ArrayList<StatementNode> argStatementList)
    {
        counter = argCounter;
        Start = argStart;
        End = argEnd;
        forStatementList = argStatementList;
    }

    public Node getEnd()
    {
        return End;
    }

    public Node getStart()
    {
        return Start;
    }

    public VariableReferenceNode getCounter()
    {
        return counter;
    }

    public ArrayList<StatementNode> getForStatementList()
    {
        return forStatementList;
    }

    public void setCounter(VariableReferenceNode counter)
    {
        this.counter = counter;
    }

    public void setForStatementList(ArrayList<StatementNode> forStatementList)
    {
        this.forStatementList = forStatementList;
    }

    public void setEnd(Node end)
    {
        End = end;
    }

    public void setStart(Node start)
    {
        Start = start;
    }

    @Override
    public String toString()
    {
        Interpreter interpreter = new Interpreter();
        String toReturn= "";
        toReturn += "for ";
        toReturn += counter.toString();
        toReturn += " from ";
        //check if math needs to be performed
        if(Start instanceof MathOpNode)
        {
            toReturn += interpreter.resolve(Start);
        }
        else
        {
            toReturn += Start.toString();
        }
        toReturn += " to ";
        //check if math needs to be performed
        if(End instanceof MathOpNode)
        {
            toReturn += interpreter.resolve(End);
        }
        else
        {
            toReturn += End.toString();
        }
        toReturn += "\nbegin\n";
        //process statements
        for(int i = 0; i < forStatementList.size(); i++)
        {
            toReturn+= "\t";
            toReturn += (forStatementList.get(i)).toString();
            toReturn += "\n";
        }
        toReturn += "end";
        return toReturn;
    }
}
