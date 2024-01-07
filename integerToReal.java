package icsi311;

import java.util.ArrayList;

public class integerToReal extends BuiltInFunctionNode
{
    boolean isVariadic = false;
    @Override
    public ArrayList<ParameterNode> execute(ArrayList<InterpreterDataType> dataList)
    {
        //check for valid number of args
        if (dataList.size() != 2)
        {
            throw new RuntimeException("INVALID AMOUNT OF ARGUMENTS");
        }
        //check for valid type
        if((dataList.get(0) instanceof IntDataType) == false)
        {
            throw new RuntimeException("INVALID TYPE");
        }
        float value = ((IntDataType)dataList.get(0)).getValue();
        ArrayList<ParameterNode> toReturn = new ArrayList<>();
        //return parameters
        toReturn.add(new ParameterNode(new VariableNode("someInt",true,descriptor.INTEGER,new IntegerNode((int)value))));
        toReturn.add(new ParameterNode(new VariableNode("argumentVar",false,descriptor.REAL,new FloatNode(value))));
        return toReturn;
    }
    @Override
    public boolean getIsVariadic() {
        return isVariadic;
    }
}
