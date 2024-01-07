package icsi311;

import java.util.ArrayList;

public class subString extends BuiltInFunctionNode
{
    boolean isVariadic = false;
    public boolean getIsVariadic()
    {
        return isVariadic;
    }
    @Override
    public ArrayList<ParameterNode> execute(ArrayList<InterpreterDataType> dataList)
    {
        //check for valid number of args and valid type of arg
        if (dataList.size() != 4)
        {
            throw new RuntimeException("INVALID NUMBER OF ARGS");
        }
        String argString = ((StringDataType)dataList.get(0)).getValue();
        //get start and end values
        int index = ((IntDataType)dataList.get(1)).getValue();
        int length = ((IntDataType)dataList.get(2)).getValue();
        String subString = "";
        for(int i = index; i < length; i++)
        {
            subString += argString.charAt(i);
        }
        //return parameters
        ArrayList<ParameterNode> toReturn = new ArrayList<>();
        toReturn.add(new ParameterNode(new ParameterNode(new VariableNode("argInput",true,descriptor.STRING, new StringNode(argString)))));
        toReturn.add(new ParameterNode(new VariableNode("argIndex",true,descriptor.INTEGER,new IntegerNode(index))));
        toReturn.add(new ParameterNode(new VariableNode("argLength",true,descriptor.INTEGER,new IntegerNode(length))));
        toReturn.add(new ParameterNode(new VariableNode("return",false,descriptor.STRING,new StringNode(subString))));
        return toReturn;
    }
}
