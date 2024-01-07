package icsi311;

import java.util.ArrayList;
import java.util.Scanner;

public class realToInteger extends BuiltInFunctionNode
{
    boolean isVariadic = false;
    @Override
    public ArrayList<ParameterNode> execute(ArrayList<InterpreterDataType> dataList)
    {
        //check for valid number of args and valid type of arg
        if (dataList.size() != 2)
        {
            throw new RuntimeException("INVALID AMOUNT OF ARGUMENTS");
        }
        if((dataList.get(0) instanceof FloatDataType) == false)
        {
            throw new RuntimeException("INVALID TYPE");
        }
        int value = (int)((FloatDataType)dataList.get(0)).getValue();
        ArrayList<ParameterNode> toReturn = new ArrayList<>();
        toReturn.add(new ParameterNode(new VariableNode("someFloat",true,descriptor.REAL,new FloatNode((float) value))));
        toReturn.add(new ParameterNode(new VariableNode("argumentVar",false,descriptor.INTEGER,new IntegerNode(value))));
        return toReturn;
    }
    @Override
    public boolean getIsVariadic() {
        return isVariadic;
    }
}

