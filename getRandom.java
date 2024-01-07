package icsi311;

import java.util.ArrayList;

public class getRandom extends BuiltInFunctionNode
{
     boolean isVariadic = false;
    @Override
    public ArrayList<ParameterNode> execute(ArrayList<InterpreterDataType> dataList)
    {
        //check for valid number of args
        if (dataList.size() != 1)
        {
            throw new RuntimeException("INVALID NUMBER OF ARGS");
        }
        //check for valid type
        if ((dataList.get(0) instanceof FloatDataType) == true)
        {
            throw new RuntimeException("INVALID TYPE");
        }
        //get a random int
        int randomInt = (int)(Math.random() * 50000);
        ArrayList<ParameterNode> toReturn = new ArrayList<>();
        //return parameter
        toReturn.add(new ParameterNode(new VariableNode("someInt",false,descriptor.INTEGER,new IntegerNode(randomInt))));
        return toReturn;
    }
    @Override
    public boolean getIsVariadic() {
        return isVariadic;
    }
}
