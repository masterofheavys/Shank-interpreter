package icsi311;

import java.util.ArrayList;
import java.util.Scanner;

public class read extends BuiltInFunctionNode
{
    boolean isVariadic = true;
    @Override
    public ArrayList<ParameterNode> execute(ArrayList<InterpreterDataType> dataList)
    {
        if (dataList.size() == 0)
        {
            throw new RuntimeException("INVALID NUMBER OF ARGS");
        }
        ArrayList<ParameterNode> toReturn = new ArrayList<>();
        Scanner readScanner = new Scanner(System.in);
        ArrayList<Object> values = new ArrayList<>();

        for(int i = 0; i < dataList.size(); i++)
        {
            values.add(readScanner.next());
        }
        //read through all parameters and process appropriately
        for(int i = 0; i < dataList.size(); i++)
        {
            if(dataList.get(i) instanceof IntDataType)
            {
                int scannedInt = Integer.valueOf((String) values.get(i));
                toReturn.add(new ParameterNode(new VariableNode("int",false,descriptor.INTEGER,new IntegerNode(scannedInt))));
            }
            else if(dataList.get(i) instanceof CharDataType)
            {
                char scannedChar = ((String)values.get(i)).charAt(0);
                toReturn.add(new ParameterNode(new VariableNode("char",false,descriptor.CHARACTER,new CharNode(scannedChar))));
            }
            else if(dataList.get(i) instanceof StringDataType)
            {
                String scannedString = (String)values.get(i);
                toReturn.add(new ParameterNode(new VariableNode("char",false,descriptor.STRING,new StringNode(scannedString))));
            }
            else if(dataList.get(i) instanceof BoolDataType)
            {
                boolean scannedBoolean = Boolean.parseBoolean((String)values.get(i));
                toReturn.add(new ParameterNode(new VariableNode("char",false,descriptor.BOOLEAN,new BoolNode(scannedBoolean))));
            }
            else
            {
                float scannedFloat = Float.valueOf((String) values.get(i));
                toReturn.add(new ParameterNode(new VariableNode("float",false,descriptor.REAL,new FloatNode((scannedFloat)))));
            }
        }
        //return list of parameters
        return toReturn;
    }
    @Override
    public boolean getIsVariadic() {
        return isVariadic;
    }
}
