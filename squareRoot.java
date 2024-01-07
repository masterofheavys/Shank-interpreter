package icsi311;

import java.util.ArrayList;

public class squareRoot extends BuiltInFunctionNode
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
        if((dataList.get(0) instanceof FloatDataType) == false &&(dataList.get(0) instanceof IntDataType) == false)
        {
            throw new RuntimeException("INVALID TYPE");
        }
        String stringStorage;
        if (dataList.get(0) instanceof FloatDataType)
        {
            stringStorage = ((FloatDataType)(dataList.get(0))).ToString();
        }
        else if (dataList.get(0) instanceof IntDataType)
        {
            stringStorage = ((IntDataType)(dataList.get(0))).ToString();
        }
        else
        {
            throw new RuntimeException("INVALID TYPE");
        }
        double squareRoot = Math.sqrt(Double.parseDouble(stringStorage));
        //return parameters
        ArrayList<ParameterNode> toReturn = new ArrayList<>();
        toReturn.add(new ParameterNode(new ParameterNode(new VariableNode("argInput",true,descriptor.REAL, new FloatNode(Float.parseFloat(stringStorage))))));
        toReturn.add(new ParameterNode(new VariableNode("someFloat",false,descriptor.REAL,new FloatNode((float) squareRoot))));
        return toReturn;

    }
    @Override
    public boolean getIsVariadic() {
        return isVariadic;
    }
}
