package icsi311;

import java.util.ArrayList;

public abstract class BuiltInFunctionNode extends CallableNode
{
    private boolean isVariadic;
    public abstract ArrayList<ParameterNode> execute(ArrayList<InterpreterDataType> dataList);
    public abstract boolean getIsVariadic();

}
