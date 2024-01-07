package icsi311;

import java.util.ArrayList;

public class write extends BuiltInFunctionNode
{
    boolean isVariadic = true;
    @Override
    public ArrayList<ParameterNode> execute(ArrayList<InterpreterDataType> dataList)
    {
        //to String and print all parameters
        for(int i = 0; i < dataList.size(); i++)
        {
            System.out.print(dataList.get(i).ToString());
            System.out.print(" ");
        }
        System.out.println();
        ArrayList<ParameterNode> toReturn = new ArrayList<>();
        for(int i = 0; i <dataList.size(); i++)
        {
            if (dataList.get(i) instanceof FloatDataType)
            {
                float tempFloat = ((FloatDataType)dataList.get(i)).getValue();
                toReturn.add(new ParameterNode(new VariableNode("someFloat",false,descriptor.REAL,new FloatNode(tempFloat))));
            }
            else if(dataList.get(i) instanceof IntDataType)
            {
                int tempInt = ((IntDataType)dataList.get(i)).getValue();
                toReturn.add(new ParameterNode(new VariableNode("someInt",false,descriptor.INTEGER,new IntegerNode(tempInt))));
            }
            else if(dataList.get(i) instanceof StringDataType)
            {
                String tempString = ((StringDataType)dataList.get(i)).getValue();
                toReturn.add(new ParameterNode(new VariableNode("someInt",false,descriptor.STRING,new StringNode(tempString))));
            }
            else if(dataList.get(i) instanceof CharDataType)
            {
                char tempChar = ((CharDataType)dataList.get(i)).getValue();
                toReturn.add(new ParameterNode(new VariableNode("someInt",false,descriptor.CHARACTER,new CharNode(tempChar))));
            }
            else if(dataList.get(i) instanceof BoolDataType)
            {
                boolean tempBoolean = ((BoolDataType)dataList.get(i)).getValue();
                toReturn.add(new ParameterNode(new VariableNode("someInt",false,descriptor.BOOLEAN,new BoolNode(tempBoolean))));
            }

        }
        //return parameters
         return toReturn;
    }

    @Override
    public boolean getIsVariadic() {
        return isVariadic;
    }


}
