package icsi311;

import java.util.ArrayList;

public abstract class CallableNode extends Node
{
    private String name;
    private ArrayList<VariableNode> localParameterList = new ArrayList<>();
}
