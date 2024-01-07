package icsi311;

import java.util.ArrayList;

public class left extends BuiltInFunctionNode
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
        if (dataList.size() != 3)
        {
            throw new RuntimeException("INVALID NUMBER OF ARGS");
        }
        String argString = ((StringDataType)dataList.get(0)).getValue();
        //get length of return string
        int length = ((IntDataType)dataList.get(1)).getValue();
        String subString = "";
        for(int i = 0; i < length; i++)
        {
            subString += argString.charAt(i);
        }
        ArrayList<ParameterNode> toReturn = new ArrayList<>();
        toReturn.add(new ParameterNode(new ParameterNode(new VariableNode("argInput",true,descriptor.STRING, new StringNode(argString)))));
        toReturn.add(new ParameterNode(new VariableNode("argLength",true,descriptor.INTEGER,new IntegerNode(length))));
        toReturn.add(new ParameterNode(new VariableNode("return",false,descriptor.STRING,new StringNode(subString))));
        return toReturn;
    }
}
