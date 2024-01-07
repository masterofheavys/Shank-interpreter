package icsi311;

import java.util.ArrayList;

public class FunctionNode extends CallableNode
{
    private String name;
    private ArrayList<VariableNode> localParameterList = new ArrayList<>();
    private ArrayList<VariableNode> localVariableList = new ArrayList<>();
    private ArrayList <StatementNode> localStatementList = new ArrayList<>();

    void setName(String argName)
    {
        name = argName;
    }
    String getName()
    {
        return name;
    }
    ArrayList<VariableNode> getLocalParameterList()
    {
        return  localParameterList;
    }
    void setLocalParameterList(ArrayList argumentParameterList)
    {
        localParameterList = argumentParameterList;
    }
    ArrayList<VariableNode> getLocalVariableList()
    {
        return  localVariableList;
    }
    public ArrayList<StatementNode> getLocalStatementList()
    {
        return localStatementList;
    }
    void setLocalVariableList(ArrayList argumentParameterList)
    {
        localVariableList = argumentParameterList;
    }
    void setLocalStatementList(ArrayList argumentStatementList)
    {
        localStatementList = argumentStatementList;
    }
    public String toString()
    {
        //create a string with all properties and children of the function
        String toReturn = new String();
        toReturn+= "Function name: ";
        toReturn += name;
        toReturn += "\n";
        toReturn += "Parameters: ";
        if (localParameterList.size() == 0)
        {
            toReturn += "NO PARAMETERS";
        }
        else
        {
            for(int i = 0; i < localParameterList.size(); i++)
            {
                toReturn += localParameterList.get(i).toString();
            }
        }
        toReturn += "\n";
        if (localVariableList.size() == 0)
        {
            toReturn += "NO LOCAL VARIABLES ";
        }
        else
        {
            toReturn += "LOCAL VARIABLES: ";
            for(int i = 0; i < localVariableList.size(); i++)
            {
                toReturn += localVariableList.get(i).toString();
            }
        }
        toReturn += "\nbegin";
        //add statements to print
        if (localStatementList.size() == 0)
        {
            toReturn += "\nNO STATEMENTS";
        }
        else
        {
            System.out.println();
            for(int i = 0; i < localStatementList.size(); i++)
            {
                toReturn += '\n';
                toReturn += ((localStatementList.get(i)).toString());
            }
        }
        toReturn += "\nend";

        return toReturn;
    }
}
