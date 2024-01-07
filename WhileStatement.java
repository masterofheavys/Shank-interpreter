package icsi311;

import java.util.ArrayList;

public class WhileStatement extends StatementNode
{
    private BooleanExpression statementCondition;
    private ArrayList<StatementNode> whileCollection = new ArrayList<>();

    public WhileStatement(BooleanExpression argStatementCondition, ArrayList<StatementNode> argStatementList)
    {
        statementCondition = argStatementCondition;
        whileCollection = argStatementList;
    }

    public BooleanExpression getStatementCondition()
    {
        return statementCondition;
    }
    public ArrayList<StatementNode> getWhileCollection()
    {
        return whileCollection;
    }
    @Override
    public String toString()
    {
        String toReturn = "";
        toReturn += "While ";
        toReturn += statementCondition.toString();
        toReturn += "\n";
        toReturn += "begin\n";
        //process all statements within loop
        for(int i = 0; i < whileCollection.size(); i++)
        {
            toReturn += whileCollection.get(i).toString();
            toReturn += "\n";
        }
        toReturn += "end";
        return toReturn;
    }
}
