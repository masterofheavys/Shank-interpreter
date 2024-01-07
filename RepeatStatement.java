package icsi311;

import java.util.ArrayList;

public class RepeatStatement extends StatementNode
{
    private BooleanExpression statementCondition;
    private ArrayList<StatementNode> repeatCollection = new ArrayList<>();

    public RepeatStatement(BooleanExpression argStatementCondition, ArrayList<StatementNode> argStatementList)
    {
        statementCondition = argStatementCondition;
        repeatCollection = argStatementList;
    }

    public BooleanExpression getStatementCondition()
    {
        return statementCondition;
    }
    public ArrayList<StatementNode> getRepeatCollection()
    {
        return repeatCollection;
    }
    @Override
    public String toString()
    {
        String toReturn = "";
        toReturn += "Repeat\n";
        toReturn += "begin\n";
        //process all statements within loop
        for(int i = 0; i < repeatCollection.size(); i++)
        {
            toReturn += "\t";
            toReturn += repeatCollection.get(i).toString();
            toReturn += "\n";
        }
        toReturn += "end\n";
        toReturn += "until ";
        toReturn += statementCondition.toString();
        return toReturn;
    }
}
