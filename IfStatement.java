package icsi311;

import java.util.ArrayList;
import java.util.LinkedList;

public class IfStatement extends StatementNode
{
    private ArrayList<StatementNode> statementList;
    private BooleanExpression statementCondition;
    private LinkedList<IfStatement> elseIfList;
    public IfStatement(BooleanExpression argCondition, LinkedList<IfStatement> argLinkedList, ArrayList<StatementNode> argStatementList)
    {
        statementCondition = argCondition;
        elseIfList = argLinkedList;
        statementList = argStatementList;
    }
    public IfStatement(BooleanExpression argCondition, ArrayList<StatementNode> argStatementList)
    {
        statementCondition = argCondition;
        statementList = argStatementList;
    }
    public IfStatement( ArrayList<StatementNode> argStatementList)
    {
        statementList = argStatementList;
    }
    public String toString()
    {
        String toReturn = "";
        toReturn += "if ";
        toReturn += statementCondition.toString();
        toReturn += " then\n";
        toReturn += "begin\n";
        for (int i = 0; i < statementList.size(); i++)
        {
            toReturn += statementList.get(i).toString();
            toReturn += "\n";
        }
        toReturn += "end\n";
        for(int i = 0; i < elseIfList.size()-1; i++)
        {
            toReturn += "elif ";
            toReturn += ((elseIfList.get(i)).getStatementCondition()).toString();
            toReturn += " then\n";
            for (int j = 0; j < elseIfList.get(i).getStatementList().size(); j++)
            {
                toReturn += elseIfList.get(i).getStatementList().get(j).toString();
                toReturn += "\n";
            }
            toReturn += "end\n";
        }
        if(elseIfList.size() > 0)
        {
            if(elseIfList.getLast().getStatementCondition() == null)
            {
                toReturn+= "else\n";
                toReturn+= "begin\n";
                for (int j = 0; j < elseIfList.getLast().getStatementList().size(); j++)
                {
                    toReturn += elseIfList.getLast().getStatementList().get(j).toString();
                    toReturn += "\n";
                }
                toReturn += "end\n";
            }
            else
            {
                toReturn += "elif ";
                toReturn += ((elseIfList.getLast()).getStatementCondition()).toString();
                toReturn += " then\n";
                //added at line 67 for to print if need to grade dispute (assignment 5)
                toReturn += "begin\n";
                for (int j = 0; j < elseIfList.getLast().getStatementList().size(); j++)
                {
                    toReturn += elseIfList.getLast().getStatementList().get(j).toString();
                    toReturn += "\n";
                }
                toReturn += "end\n";
            }
        }
        return toReturn;
    }

    public BooleanExpression getStatementCondition()
    {
        return statementCondition;
    }

    public ArrayList<StatementNode> getStatementList()
    {
        return statementList;
    }
    public LinkedList<IfStatement> getElseIfList()
    {
        return elseIfList;
    }
}
