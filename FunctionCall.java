package icsi311;

import java.util.ArrayList;

public class FunctionCall extends StatementNode
{
    private ArrayList<ParameterNode> parameterList = new ArrayList<>();
    private String functionName;

    public FunctionCall(String argName,ArrayList<ParameterNode> argList)
    {
        functionName = argName;
        parameterList = argList;
    }


    public String toString()
    {
        //add function name to return string
        String toReturn = "";
        toReturn += functionName;
        toReturn += "(";
        //process all parameters
        for(int i = 0; i < parameterList.size(); i++)
        {
            if (parameterList.get(i).getValue() instanceof IntegerNode || parameterList.get(i).getValue() instanceof FloatNode)
            {
                toReturn += parameterList.get(i).toString();
            }
            else if (parameterList.get(i).getValue() instanceof VariableNode)
            {
                toReturn += ((VariableNode)((parameterList.get(i).getValue()))).getName();
            }
            else
            {
                toReturn += "var ";
                toReturn += parameterList.get(i).toString();
            }
            toReturn += " ";
        }
        //get rid of trailing space
        toReturn = toReturn.substring(0,toReturn.length()-1);
        toReturn += ")";
        return toReturn;
    }

    public ArrayList<ParameterNode> getParameterList() {
        return parameterList;
    }

    public String getFunctionName() {
        return functionName;
    }
}
