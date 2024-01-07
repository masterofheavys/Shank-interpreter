package icsi311;

import java.util.ArrayList;
import java.util.HashMap;

public class Interpreter
{
    static public HashMap<String,InterpreterDataType> variableHash = new HashMap<>();
    static public HashMap<String,CallableNode> functionMap = new HashMap<>();

    //Helper method to get the data type of members of a math op node based on the first term
    public static descriptor traverseMathOpNode(Node traversalNode)
    {
        //recursive call
        if (traversalNode instanceof MathOpNode)
        {
            return traverseMathOpNode(((MathOpNode) traversalNode).getLeftNode());
        }
        //base case, return corresponding data type
        else if (traversalNode instanceof CharNode)
        {
            return descriptor.CHARACTER;
        }
        else if (traversalNode instanceof StringNode)
        {
            return descriptor.STRING;
        }
        else if (traversalNode instanceof IntegerNode)
        {
            return descriptor.INTEGER;
        }
        else if (traversalNode instanceof FloatNode)
        {
            return descriptor.REAL;
        }
        else if (traversalNode instanceof VariableReferenceNode)
        {
            InterpreterDataType targetType =variableHash.get(((VariableReferenceNode)traversalNode).getTarget());
            if (targetType instanceof IntDataType)
            {
                return descriptor.INTEGER;
            }
            else if (targetType instanceof FloatDataType)
            {
                return descriptor.REAL;
            }
            else if (targetType instanceof CharDataType)
            {
                return descriptor.CHARACTER;
            }
            else if (targetType instanceof StringDataType)
            {
                return descriptor.STRING;
            }
            else if (targetType instanceof BoolDataType)
            {
                return descriptor.BOOLEAN;
            }
        }
        throw new RuntimeException("UNEXPECTED TRAVERSAL");
    }
    public static boolean EvaluateBooleanExpression(BooleanExpression argBooleanExpression, HashMap<String,InterpreterDataType> variableMap)
    {
        //get comparison operator
        descriptor operator = argBooleanExpression.getOperator();
        descriptor comparisonType = null;
        //get data type of left side of expression
        if (argBooleanExpression.getLeftCondition() instanceof IntegerNode)
        {
            comparisonType = descriptor.INTEGER;
        }
        else if (argBooleanExpression.getLeftCondition() instanceof FloatNode)
        {
            comparisonType = descriptor.REAL;
        }
        else if(argBooleanExpression.getLeftCondition() instanceof CharNode)
        {
            comparisonType = descriptor.CHARACTER;
        }
        else if(argBooleanExpression.getLeftCondition() instanceof StringNode)
        {
            comparisonType = descriptor.STRING;
        }
        else if (argBooleanExpression.getLeftCondition() instanceof BoolNode)
        {
            comparisonType = descriptor.BOOLEAN;
        }
        else if (argBooleanExpression.getLeftCondition() instanceof VariableReferenceNode)
        {
            InterpreterDataType targetType =variableMap.get(((VariableReferenceNode)argBooleanExpression.getLeftCondition()).getTarget());
            if (targetType instanceof IntDataType)
            {
                comparisonType = descriptor.INTEGER;
            }
            else if (targetType instanceof FloatDataType)
            {
                comparisonType = descriptor.REAL;
            }
            else if (targetType instanceof CharDataType)
            {
                comparisonType = descriptor.CHARACTER;
            }
            else if (targetType instanceof StringDataType)
            {
                comparisonType = descriptor.STRING;
            }
            else if (targetType instanceof BoolDataType)
            {
                comparisonType = descriptor.BOOLEAN;
            }
        }
        else if (argBooleanExpression.getLeftCondition() instanceof MathOpNode)
        {
            comparisonType = traverseMathOpNode(argBooleanExpression.getLeftCondition());
        }
        //calculate Numerical expression
        if (comparisonType == descriptor.INTEGER || comparisonType == descriptor.REAL)
        {
            //compute sides
            float leftSide = Float.valueOf(resolve(argBooleanExpression.getLeftCondition()));
            float rightSide = Float.valueOf(resolve(argBooleanExpression.getRightCondition()));
            //evaluate condition
            if (operator == descriptor.LESS)
            {
                if (leftSide < rightSide)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if (operator == descriptor.LESSOREQUAL)
            {
                if (leftSide <= rightSide)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if (operator == descriptor.GREATEROREQUAL)
            {
                if (leftSide >= rightSide)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if (operator == descriptor.GREATER)
            {
                if (leftSide > rightSide)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if (operator == descriptor.EQUAL)
            {
                if (leftSide == rightSide)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if (operator == descriptor.NOTEQUAL)
            {
                if (leftSide != rightSide)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                throw new RuntimeException("UNEXPECTED OPERATOR");
            }
        }
        //If doing boolean comparison
        else if(comparisonType == descriptor.BOOLEAN)
        {
            //get both sides of expression
            if (argBooleanExpression.getLeftCondition() instanceof VariableReferenceNode)
            {
                if (argBooleanExpression.getRightCondition() instanceof VariableReferenceNode)
                {
                    boolean leftSide = ((BoolDataType)(variableHash.get(((VariableReferenceNode) argBooleanExpression.getLeftCondition()).getTarget()))).getValue();
                    boolean rightSide = ((BoolDataType)(variableHash.get(((VariableReferenceNode) argBooleanExpression.getRightCondition()).getTarget()))).getValue();
                    //evaluate condition
                    if (operator == descriptor.EQUAL)
                    {
                        if(leftSide == rightSide)
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    //evaluate condition
                    else if (operator == descriptor.NOTEQUAL)
                    {
                        if(leftSide != rightSide)
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        throw new RuntimeException("UNEXPECTED OPERATOR");
                    }
                }
                else
                {
                    boolean leftSide = ((BoolDataType)(variableHash.get(((VariableReferenceNode) argBooleanExpression.getLeftCondition()).getTarget()))).getValue();
                    boolean rightSide = ((BoolNode)argBooleanExpression.getRightCondition()).getBooleanStorage();
                    //evaluate condition
                    if (operator == descriptor.EQUAL)
                    {

                        if(leftSide == rightSide)
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else if (operator == descriptor.NOTEQUAL)
                    {
                        if(leftSide != rightSide)
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        throw new RuntimeException("UNEXPECTED OPERATOR");
                    }
                }
            }
            else
            {
                if (argBooleanExpression.getRightCondition() instanceof VariableReferenceNode)
                {
                    boolean leftSide = ((BoolNode)argBooleanExpression.getLeftCondition()).getBooleanStorage();
                    boolean rightSide = ((BoolDataType)(variableHash.get(((VariableReferenceNode) argBooleanExpression.getRightCondition()).getTarget()))).getValue();
                    //evaluate condition
                    if (operator == descriptor.EQUAL)
                    {
                        if(leftSide == rightSide)
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else if (operator == descriptor.NOTEQUAL)
                    {
                        if(leftSide != rightSide)
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        throw new RuntimeException("UNEXPECTED OPERATOR");
                    }
                }
                else
                {
                    boolean leftSide = ((BoolNode)argBooleanExpression.getLeftCondition()).getBooleanStorage();
                    if ((argBooleanExpression).getOperator() == null)
                    {
                        return leftSide;
                    }
                    Boolean rightSide = null;
                    if (argBooleanExpression.getRightCondition() instanceof BooleanExpression)
                    {
                        rightSide = ((BoolNode)((BooleanExpression)argBooleanExpression.getRightCondition()).getLeftCondition()).getBooleanStorage();
                    }
                    else if (argBooleanExpression.getRightCondition() instanceof BoolNode)
                    {
                        rightSide = ((BoolNode)argBooleanExpression.getRightCondition()).getBooleanStorage();
                    }
                    if (operator == descriptor.EQUAL)
                    {
                        if(leftSide == rightSide)
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else if (operator == descriptor.NOTEQUAL)
                    {
                        if(leftSide != rightSide)
                        {
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        throw new RuntimeException("UNEXPECTED OPERATOR");
                    }
                }
            }
        }
        //handle character equality
        else if (comparisonType == descriptor.CHARACTER)
        {
            String leftSide = resolve(argBooleanExpression.getLeftCondition());
            String rightSide = resolve(argBooleanExpression.getRightCondition());
            //evaluate condition

            if (operator == descriptor.EQUAL)
            {
                if (leftSide.charAt(0) == rightSide.charAt(0))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if (operator == descriptor.NOTEQUAL)
            {
                if (leftSide.charAt(0) != rightSide.charAt(0))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                throw new RuntimeException("UNEXPECTED OPERATOR");
            }
        }
        else if (comparisonType == descriptor.STRING)
        {
            String leftSide = resolve(argBooleanExpression.getLeftCondition());
            String rightSide = resolve(argBooleanExpression.getRightCondition());
            //evaluate condition

            if (operator == descriptor.EQUAL)
            {
                if (leftSide.equals(rightSide))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else if (operator == descriptor.NOTEQUAL)
            {
                if (leftSide.equals(rightSide) == false)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                throw new RuntimeException("UNEXPECTED OPERATOR");
            }
        }
        throw new RuntimeException("UNEXPCECTED TYPE");
    }
    public static String resolve(Node argumentNode)
    {
        float interpreterStorageReturn = 0;
        //check if not a MathOpNode
        if(argumentNode instanceof FloatNode)
        {
            return String.valueOf(((FloatNode) argumentNode).getFloatStorage());
        }
        else if (argumentNode instanceof IntegerNode)
        {
            return  String.valueOf((((IntegerNode) argumentNode).getIntegerStorage()));
        }
        else if(argumentNode instanceof VariableReferenceNode)
        {
            InterpreterDataType typeStorage = variableHash.get(((VariableReferenceNode) argumentNode).getTarget());
            if (typeStorage instanceof FloatDataType)
            {
                interpreterStorageReturn = ((FloatDataType)(typeStorage)).getValue();
            }
            else if (typeStorage instanceof IntDataType)
            {
                interpreterStorageReturn = ((IntDataType)(typeStorage)).getValue();
            }
            else if (typeStorage instanceof BoolDataType)
            {
                boolean toReturn = ((BoolDataType)(typeStorage)).getValue();
                return String.valueOf(toReturn);
            }
            else if(typeStorage instanceof CharDataType)
            {
                char toReturn = ((CharDataType)(typeStorage)).getValue();
                return String.valueOf(toReturn);
            }
            else if (typeStorage instanceof StringDataType)
            {
                String toReturn = ((StringDataType)(typeStorage)).getValue();
                return toReturn;
            }
            return String.valueOf(interpreterStorageReturn);
        }
        else if(argumentNode instanceof BooleanExpression)
        {
            return String.valueOf(EvaluateBooleanExpression((BooleanExpression)argumentNode,variableHash));
        }
        else if(argumentNode instanceof BoolNode)
        {
            return String.valueOf(((BoolNode) argumentNode).getBooleanStorage());
        }
        else if(argumentNode instanceof StringNode)
        {
            return String.valueOf(((StringNode)argumentNode).getStringStorage());
        }
        else if (argumentNode instanceof CharNode)
        {
            return String.valueOf(((CharNode)argumentNode).getCharValue());
        }
        else
        {
            String interpreterStorageStringOne = null;
            String interpreterStorageStringTwo = null;
            float interpreterStorageOne;
            float interpreterStorageTwo;
            //get both sides of math op node
            Node nodeStorageOne = ((MathOpNode)argumentNode).getLeftNode();
            Node nodeStorageTwo = ((MathOpNode)argumentNode).getRightNode();
            //handle variables/variable reference nodes
            if(nodeStorageOne instanceof VariableReferenceNode || nodeStorageTwo instanceof VariableReferenceNode)
            {
                if (nodeStorageOne instanceof VariableReferenceNode)
                {
                    //get valid type
                    InterpreterDataType typeStorage = variableHash.get(((VariableReferenceNode) nodeStorageOne).getTarget());
                    if (typeStorage instanceof FloatDataType)
                    {
                        interpreterStorageOne = ((FloatDataType)(typeStorage)).getValue();
                    }
                    else if(typeStorage instanceof CharDataType)
                    {
                        interpreterStorageStringOne = String.valueOf(((CharDataType)typeStorage).getValue());
                    }
                    else if (typeStorage instanceof StringDataType)
                    {
                        interpreterStorageStringOne =((StringDataType)typeStorage).getValue();
                    }
                    else
                    {
                        interpreterStorageOne = ((IntDataType)(typeStorage)).getValue();
                    }
                }
                if (nodeStorageTwo instanceof VariableReferenceNode)
                {
                    //get valid type
                    InterpreterDataType typeStorage = variableHash.get(((VariableReferenceNode) nodeStorageTwo).getTarget());
                    if (typeStorage instanceof FloatDataType)
                    {
                        interpreterStorageTwo = ((FloatDataType)(typeStorage)).getValue();
                    }
                    else if(typeStorage instanceof CharDataType)
                    {
                        interpreterStorageStringTwo = String.valueOf(((CharDataType)typeStorage).getValue());
                    }
                    else if (typeStorage instanceof StringDataType)
                    {
                        interpreterStorageStringTwo =((StringDataType)typeStorage).getValue();
                    }
                    else
                    {
                        interpreterStorageTwo = ((IntDataType)(typeStorage)).getValue();
                    }
                }
            }
            else if (nodeStorageOne instanceof StringNode)
            {
                interpreterStorageStringOne = ((StringNode)nodeStorageOne).getStringStorage();
            }
            else if (nodeStorageOne instanceof CharNode)
            {
                interpreterStorageStringOne = "";
                interpreterStorageStringOne += ((CharNode)nodeStorageOne).getCharValue();
            }
            if (nodeStorageTwo instanceof StringNode)
            {
                interpreterStorageStringTwo = ((StringNode)nodeStorageTwo).getStringStorage();
            }
            else if (nodeStorageTwo instanceof CharNode)
            {
                interpreterStorageStringTwo = "";
                interpreterStorageStringTwo += ((CharNode)nodeStorageTwo).getCharValue();
            }
            String operatorString = ((MathOpNode)argumentNode).getTypeIndicator();
            if (interpreterStorageStringOne != null || interpreterStorageStringTwo != null)
            {
                interpreterStorageStringOne = resolve(nodeStorageOne);
                interpreterStorageStringTwo = resolve(nodeStorageTwo);
                String toReturn = interpreterStorageStringOne;
                switch (operatorString)
                {
                    case ("+"):
                        toReturn += interpreterStorageStringTwo;
                        return toReturn;
                    default:
                        throw new RuntimeException("INVALID CHAR/STRING OPERATOR");
                }

            }
            //call resolve on left and right side, may be recursive
            interpreterStorageOne = Float.parseFloat(resolve(nodeStorageOne));
            interpreterStorageTwo = Float.parseFloat(resolve(nodeStorageTwo));
            //evaluate the token
            switch (operatorString)
            {
                case("+"):
                    interpreterStorageReturn = interpreterStorageOne + interpreterStorageTwo;
                    break;
                case("-"):
                    interpreterStorageReturn = interpreterStorageOne - interpreterStorageTwo;
                    break;
                case("*"):
                    interpreterStorageReturn = interpreterStorageOne * interpreterStorageTwo;
                    break;
                case("/"):
                    interpreterStorageReturn = interpreterStorageOne / interpreterStorageTwo;
                    break;
                case("MOD"):
                    interpreterStorageReturn = interpreterStorageOne % interpreterStorageTwo;
                    break;

            }

            return String.valueOf(interpreterStorageReturn);
        }

    }
    public static void InterpretFunction(FunctionNode function, ArrayList<InterpreterDataType> idtList)
    {

        ArrayList<VariableNode> varNodeList;
        varNodeList = function.getLocalParameterList();
        //add all parameters to hashmap
        for(int i = 0; i < idtList.size(); i++)
        {
            variableHash.put(varNodeList.get(i).getName(),idtList.get(i));
        }
        //add all local variables to hashmap
        for(int j = 0; j < function.getLocalVariableList().size(); j++)
        {
            //check for constant
            if(function.getLocalVariableList().get(j).getIsConstant())
            {
                //add all initial values to constants
                Node tempNodeStore = function.getLocalVariableList().get(j).getInitialValue();
                if(tempNodeStore instanceof FloatNode)
                {
                    float tempFloatStore = ((FloatNode) tempNodeStore).getFloatStorage();
                    variableHash.put(function.getLocalVariableList().get(j).getName(),new FloatDataType(tempFloatStore));
                }
                else if(tempNodeStore instanceof IntegerNode)
                {
                    int tempIntStore = ((IntegerNode) tempNodeStore).getIntegerStorage();
                    variableHash.put(function.getLocalVariableList().get(j).getName(),new IntDataType(tempIntStore));
                }
                else if(tempNodeStore instanceof BoolNode)
                {
                    boolean tempBooleanStore = ((BoolNode) tempNodeStore).getBooleanStorage();
                    variableHash.put(function.getLocalVariableList().get(j).getName(),new BoolDataType(tempBooleanStore));
                }
                else if (tempNodeStore instanceof CharNode)
                {
                    char tempCharStore = ((CharNode) tempNodeStore).getCharValue();
                    variableHash.put(function.getLocalVariableList().get(j).getName(),new CharDataType(tempCharStore));
                }
                else if (tempNodeStore instanceof StringNode)
                {
                    String tempStringStore = ((StringNode) tempNodeStore).getStringStorage();
                    variableHash.put(function.getLocalVariableList().get(j).getName(),new StringDataType(tempStringStore));
                }
            }
            else
            {
                //default value is zero
                if (function.getLocalVariableList().get(j).getDataType() == descriptor.REAL)
                {
                    variableHash.put(function.getLocalVariableList().get(j).getName(),new FloatDataType(0));
                }
                else if(function.getLocalVariableList().get(j).getDataType() == descriptor.INTEGER)
                {
                    variableHash.put(function.getLocalVariableList().get(j).getName(),new IntDataType(0));
                }
                else if(function.getLocalVariableList().get(j).getDataType() == descriptor.BOOLEAN)
                {
                    variableHash.put(function.getLocalVariableList().get(j).getName(),new BoolDataType(false));
                }
                else if(function.getLocalVariableList().get(j).getDataType() == descriptor.STRING)
                {
                    variableHash.put(function.getLocalVariableList().get(j).getName(),new StringDataType(""));
                }
                else if(function.getLocalVariableList().get(j).getDataType() == descriptor.CHARACTER)
                {
                    variableHash.put(function.getLocalVariableList().get(j).getName(),new CharDataType('\0'));
                }
            }
        }
        //interpret block
        InterpretBlock(function.getLocalStatementList(),variableHash);
    }
    public static void InterpretBlock(ArrayList<StatementNode> statementList, HashMap<String,InterpreterDataType> variableMap)
    {

        //processing of function call
        for(int i = 0; i < statementList.size(); i++)
        {
            if (statementList.get(i) instanceof FunctionCall)
            {
                CallableNode correspondingNode = functionMap.get(((FunctionCall) statementList.get(i)).getFunctionName());
                //make sure function is defined
                if (correspondingNode == null)
                {
                    throw new RuntimeException("NOT DEFINED FUNCTION");
                }
                //make sure parameter count matches
                if (correspondingNode instanceof BuiltInFunctionNode)
                {
                    if((((BuiltInFunctionNode)correspondingNode).getIsVariadic()) == false)
                    {
                        if(((FunctionCall) statementList.get(i)).getParameterList().size() != (((FunctionCall) statementList.get(i)).getParameterList().size()))
                        {
                            throw new RuntimeException("MISMATCH IN PARAMETERS");
                        }
                    }
                }
                else if(((FunctionNode)correspondingNode).getLocalParameterList().size() != (((FunctionCall) statementList.get(i)).getParameterList().size()))
                {
                    throw new RuntimeException("MISMATCH IN PARAMETERS");
                }
                //create InterpreterDataTypes
                ArrayList<InterpreterDataType> dataTypeCreation = new ArrayList<>();
                for(int j = 0; j < (((FunctionCall) statementList.get(i)).getParameterList().size()); j++)
                {
                    //get parameter node, handle correctly depending on type
                    Node paramValue = (((FunctionCall) statementList.get(i)).getParameterList().get(j)).getValue();
                    if (paramValue instanceof FloatNode)
                    {
                        dataTypeCreation.add(new FloatDataType(((FloatNode) paramValue).getFloatStorage()));
                    }
                    else if(paramValue instanceof IntegerNode)
                    {
                        dataTypeCreation.add(new IntDataType(((IntegerNode) paramValue).getIntegerStorage()));
                    }
                    else if(paramValue instanceof StringNode)
                    {
                        dataTypeCreation.add(new StringDataType(((StringNode) paramValue).getStringStorage()));
                    }
                    else if(paramValue instanceof CharNode)
                    {
                        dataTypeCreation.add(new CharDataType(((CharNode) paramValue).getCharValue()));
                    }
                    else if(paramValue instanceof BoolNode)
                    {
                        dataTypeCreation.add(new BoolDataType(((BoolNode) paramValue).getBooleanStorage()));
                    }
                    else if(paramValue instanceof VariableNode)
                    {
                        String name = ((VariableNode)paramValue).getName();
                        dataTypeCreation.add(variableMap.get(name));
                    }
                    else if(paramValue instanceof VariableReferenceNode)
                    {
                        String name = ((VariableReferenceNode)paramValue).getTarget();
                        dataTypeCreation.add(variableMap.get(name));
                    }
                }
                //get modified values from function
                ArrayList<ParameterNode> functionReturnList = new ArrayList<>();
                if(functionMap.get(((FunctionCall)statementList.get(i)).getFunctionName()) instanceof BuiltInFunctionNode)
                {
                    functionReturnList = ((BuiltInFunctionNode) functionMap.get(((FunctionCall)statementList.get(i)).getFunctionName())).execute(dataTypeCreation);
                }
                else
                {
                    InterpretFunction((FunctionNode) functionMap.get(((FunctionCall) statementList.get(i)).getFunctionName()),dataTypeCreation);
                }
                //get the parameter list of the function
                ArrayList<ParameterNode> parameterList = (((FunctionCall)statementList.get(i)).getParameterList());
                //handle updating values depending on variadic or not
                if(correspondingNode instanceof BuiltInFunctionNode)
                {
                    if((((BuiltInFunctionNode)correspondingNode).getIsVariadic()))
                    {
                        for(int j = 0; j < (((FunctionCall) statementList.get(i)).getParameterList().size()); j++)
                        {
                            //update real variables
                            if (((VariableNode)functionReturnList.get(j).getValue()).getDataType() == descriptor.REAL)
                            {
                                //in variadic functions, update both variables and variable refrences (variadic updates no matter what)
                                String varName = new String();
                                VariableNode tempVarNode = ((VariableNode)functionReturnList.get(j).getValue());
                                if(parameterList.get(j).getValue() instanceof VariableReferenceNode)
                                {
                                   varName  = ((VariableReferenceNode)parameterList.get(j).getValue()).getTarget();
                                }
                                else if(parameterList.get(j).getValue() instanceof VariableNode)
                                {
                                    varName = ((VariableNode)parameterList.get(j).getValue()).getName();
                                }
                                if (parameterList.get(j).getValue() instanceof VariableReferenceNode || parameterList.get(j).getValue() instanceof VariableNode)
                                {
                                    float floatVal = ((FloatNode)tempVarNode.getInitialValue()).getFloatStorage();
                                    variableMap.put(varName,new FloatDataType(floatVal));
                                }
                            }
                            //update integer variables
                            else if (((VariableNode)functionReturnList.get(j).getValue()).getDataType() == descriptor.INTEGER)
                            {
                                String varName = new String();
                                VariableNode tempVarNode = ((VariableNode)functionReturnList.get(j).getValue());
                                if(parameterList.get(j).getValue() instanceof VariableReferenceNode)
                                {
                                    varName  = ((VariableReferenceNode)parameterList.get(j).getValue()).getTarget();
                                }
                                else if(parameterList.get(j).getValue() instanceof VariableNode)
                                {
                                    varName = ((VariableNode)parameterList.get(j).getValue()).getName();
                                }
                                if (parameterList.get(j).getValue() instanceof VariableReferenceNode || parameterList.get(j).getValue() instanceof VariableNode)
                                {
                                    int intVal = ((IntegerNode)tempVarNode.getInitialValue()).getIntegerStorage();
                                    variableMap.put(varName,new IntDataType(intVal));
                                }
                            }
                            else if (((VariableNode)functionReturnList.get(j).getValue()).getDataType() == descriptor.CHARACTER)
                            {
                                String varName = new String();
                                VariableNode tempVarNode = ((VariableNode)functionReturnList.get(j).getValue());
                                if(parameterList.get(j).getValue() instanceof VariableReferenceNode)
                                {
                                    varName  = ((VariableReferenceNode)parameterList.get(j).getValue()).getTarget();
                                }
                                else if(parameterList.get(j).getValue() instanceof VariableNode)
                                {
                                    varName = ((VariableNode)parameterList.get(j).getValue()).getName();
                                }
                                if (parameterList.get(j).getValue() instanceof VariableReferenceNode || parameterList.get(j).getValue() instanceof VariableNode)
                                {
                                    char charVal = ((CharNode)tempVarNode.getInitialValue()).getCharValue();
                                    variableMap.put(varName,new CharDataType(charVal));
                                }
                            }
                            else if (((VariableNode)functionReturnList.get(j).getValue()).getDataType() == descriptor.STRING)
                            {
                                String varName = new String();
                                VariableNode tempVarNode = ((VariableNode)functionReturnList.get(j).getValue());
                                if(parameterList.get(j).getValue() instanceof VariableReferenceNode)
                                {
                                    varName  = ((VariableReferenceNode)parameterList.get(j).getValue()).getTarget();
                                }
                                else if(parameterList.get(j).getValue() instanceof VariableNode)
                                {
                                    varName = ((VariableNode)parameterList.get(j).getValue()).getName();
                                }
                                if (parameterList.get(j).getValue() instanceof VariableReferenceNode || parameterList.get(j).getValue() instanceof VariableNode)
                                {
                                    String stringVal = ((StringNode)tempVarNode.getInitialValue()).getStringStorage();
                                    variableMap.put(varName,new StringDataType(stringVal));
                                }
                            }
                            else if (((VariableNode)functionReturnList.get(j).getValue()).getDataType() == descriptor.BOOLEAN)
                            {
                                String varName = new String();
                                VariableNode tempVarNode = ((VariableNode)functionReturnList.get(j).getValue());
                                if(parameterList.get(j).getValue() instanceof VariableReferenceNode)
                                {
                                    varName  = ((VariableReferenceNode)parameterList.get(j).getValue()).getTarget();
                                }
                                else if(parameterList.get(j).getValue() instanceof VariableNode)
                                {
                                    varName = ((VariableNode)parameterList.get(j).getValue()).getName();
                                }
                                if (parameterList.get(j).getValue() instanceof VariableReferenceNode || parameterList.get(j).getValue() instanceof VariableNode)
                                {
                                    boolean booleanVal = ((BoolNode)tempVarNode.getInitialValue()).getBooleanStorage();
                                    variableMap.put(varName,new BoolDataType(booleanVal));
                                }
                            }
                        }
                    }
                    else
                    {
                        for(int k = 0; k < parameterList.size(); k++)
                        {
                            //update only if invocation and definition are both var
                            if (parameterList.get(k).getValue() instanceof VariableReferenceNode)
                            {
                                if ((functionReturnList.get(k).getValue()) instanceof VariableNode)
                                {
                                    if (((VariableNode)functionReturnList.get(k).getValue()).getDataType() == descriptor.REAL)
                                    {
                                        VariableNode tempVarNode = ((VariableNode)functionReturnList.get(k).getValue());
                                        String varName = ((VariableReferenceNode)parameterList.get(k).getValue()).getTarget();
                                        float floatVal = ((FloatNode)tempVarNode.getInitialValue()).getFloatStorage();
                                        variableMap.put(varName,new FloatDataType(floatVal));
                                    }
                                    else if (((VariableNode)functionReturnList.get(k).getValue()).getDataType() == descriptor.INTEGER)
                                    {
                                        VariableNode tempVarNode = ((VariableNode)functionReturnList.get(k).getValue());
                                        String varName = ((VariableReferenceNode)parameterList.get(k).getValue()).getTarget();
                                        int intVal = ((IntegerNode)tempVarNode.getInitialValue()).getIntegerStorage();
                                        variableMap.put(varName,new IntDataType(intVal));
                                    }
                                    else if (((VariableNode)functionReturnList.get(k).getValue()).getDataType() == descriptor.STRING)
                                    {
                                        VariableNode tempVarNode = ((VariableNode)functionReturnList.get(k).getValue());
                                        String varName = ((VariableReferenceNode)parameterList.get(k).getValue()).getTarget();
                                        String stringVal = ((StringNode)tempVarNode.getInitialValue()).getStringStorage();
                                        variableMap.put(varName,new StringDataType(stringVal));
                                    }
                                    else if (((VariableNode)functionReturnList.get(k).getValue()).getDataType() == descriptor.CHARACTER)
                                    {
                                        VariableNode tempVarNode = ((VariableNode)functionReturnList.get(k).getValue());
                                        String varName = ((VariableReferenceNode)parameterList.get(k).getValue()).getTarget();
                                        char charVal = ((CharNode)tempVarNode.getInitialValue()).getCharValue();
                                        variableMap.put(varName,new CharDataType(charVal));
                                    }
                                    else if (((VariableNode)functionReturnList.get(k).getValue()).getDataType() == descriptor.BOOLEAN)
                                    {
                                        VariableNode tempVarNode = ((VariableNode)functionReturnList.get(k).getValue());
                                        String varName = ((VariableReferenceNode)parameterList.get(k).getValue()).getTarget();
                                        boolean booleanVal = ((BoolNode)tempVarNode.getInitialValue()).getBooleanStorage();
                                        variableMap.put(varName,new BoolDataType(booleanVal));
                                    }
                                }
                            }
                        }
                    }

                }
                else
                {
                    ArrayList<VariableNode> functionDefinitionParameters = ((FunctionNode) correspondingNode).getLocalParameterList();
                    for (int k = 0; k < parameterList.size(); k++)
                    {
                        //update only if invocation and definition are both var
                        if ((functionDefinitionParameters.get(k).getIsConstant()) == false)
                        {
                            if ((((FunctionCall) statementList.get(i)).getParameterList().get(k).getValue() instanceof VariableReferenceNode))
                            {
                                VariableReferenceNode tempVarNode = (VariableReferenceNode) (((FunctionCall) statementList.get(i)).getParameterList().get(k).getValue());
                                String name = tempVarNode.getTarget();
                                variableMap.put(name, variableMap.get(functionDefinitionParameters.get(k).getName()));
                            }
                        }
                    }
                }
            }
            //handle assignment nodes
            else if(statementList.get(i) instanceof AssignmentNode)
            {
                //get assignment
                AssignmentNode assignment = (AssignmentNode) statementList.get(i);
                //get data type
                if (variableMap.get(assignment.getLocalVariableReference().getTarget()) instanceof FloatDataType)
                {
                    //get right hand side
                    variableMap.put(assignment.getLocalVariableReference().getTarget(),new FloatDataType(Float.valueOf(resolve(assignment.getExpression()))));
                }
                else if (variableMap.get(assignment.getLocalVariableReference().getTarget()) instanceof IntDataType)
                {
                    //get right hand side
                    float toPass = Float.valueOf(resolve(assignment.getExpression()));
                    variableMap.put(assignment.getLocalVariableReference().getTarget(),new IntDataType((int)toPass));
                }
                else if (variableMap.get(assignment.getLocalVariableReference().getTarget()) instanceof BoolDataType)
                {
                    boolean toPass = Boolean.valueOf(resolve(assignment.getExpression()));
                    variableMap.put(assignment.getLocalVariableReference().getTarget(),new BoolDataType(toPass));
                }
                else if (variableMap.get(assignment.getLocalVariableReference().getTarget()) instanceof CharDataType)
                {
                    char toPass = Character.valueOf(resolve(assignment.getExpression()).charAt(0));
                    variableMap.put(assignment.getLocalVariableReference().getTarget(),new CharDataType(toPass));
                }
                else if (variableMap.get(assignment.getLocalVariableReference().getTarget()) instanceof StringDataType)
                {
                    String toPass = String.valueOf(resolve(assignment.getExpression()));
                    variableMap.put(assignment.getLocalVariableReference().getTarget(),new StringDataType(toPass));
                }
            }
            else if (statementList.get(i) instanceof WhileStatement)
            {
                //get the while statement
                WhileStatement whileStatementProcessing = (WhileStatement)statementList.get(i);
                //while the condition is true, process the block
                while(EvaluateBooleanExpression(whileStatementProcessing.getStatementCondition(),variableMap))
                {
                    InterpretBlock(whileStatementProcessing.getWhileCollection(),variableMap);
                }
            }
            else if (statementList.get(i) instanceof RepeatStatement)
            {
                //get the repeat statement
                RepeatStatement repeatStatementProcessing = (RepeatStatement) statementList.get(i);
                //process block at least once until the statement is true
                do {
                    InterpretBlock(repeatStatementProcessing.getRepeatCollection(),variableMap);
                }
                while (EvaluateBooleanExpression(repeatStatementProcessing.getStatementCondition(),variableMap) == false);
            }
            else if (statementList.get(i) instanceof ForStatement)
            {
                int StartValue;
                int EndValue;
                //get the for statement
                ForStatement processForStatement = (ForStatement) statementList.get(i);
                //get the variable name of the counter
                VariableReferenceNode counter = processForStatement.getCounter();
                Node start = processForStatement.getStart();
                Node end =processForStatement.getEnd();
                //get start value
                if (start instanceof VariableReferenceNode)
                {
                    InterpreterDataType varType = variableMap.get(((VariableReferenceNode) start).getTarget());
                    if (varType instanceof FloatDataType)
                    {
                        throw new RuntimeException("FLOAT USED IN FOR");
                    }
                    else
                    {
                        StartValue = ((IntDataType)variableMap.get(((VariableReferenceNode) start).getTarget())).getValue();
                    }
                }
                else if(start instanceof FloatNode)
                {
                    throw new RuntimeException("FLOAT USED IN FOR");
                }
                else
                {
                    StartValue = ((IntegerNode)start).getIntegerStorage();
                }
                //get end value
                if (end instanceof VariableReferenceNode)
                {
                    InterpreterDataType varType = variableMap.get(((VariableReferenceNode) end).getTarget());
                    if (varType instanceof FloatDataType)
                    {
                        throw new RuntimeException("FLOAT USED IN FOR");
                    }
                    else
                    {
                        EndValue = ((IntDataType)variableMap.get(((VariableReferenceNode) end).getTarget())).getValue();
                    }
                }
                else if(end instanceof FloatNode)
                {
                    throw new RuntimeException("FLOAT USED IN FOR");
                }
                else
                {
                    EndValue = ((IntegerNode)end).getIntegerStorage();
                }
                //update variable with start value
                variableMap.put(counter.getTarget(), new IntDataType(StartValue));
                //process for loop
                while (((IntDataType)variableMap.get(counter.getTarget())).getValue() <= EndValue)
                {
                    //interpret block
                    InterpretBlock(processForStatement.getForStatementList(),variableMap);
                    //update start value
                    StartValue++;
                    //update variable value
                    variableMap.put(counter.getTarget(), new IntDataType(StartValue));
                }
            }
            else if(statementList.get(i) instanceof IfStatement)
            {
                boolean executedBlock = false;
                //get if statement
                IfStatement ifStatementProcessing = (IfStatement) statementList.get(i);
                //get boolean expression
                BooleanExpression expression = ifStatementProcessing.getStatementCondition();
                //check initial statement
                if(EvaluateBooleanExpression(expression,variableMap))
                {
                    InterpretBlock(ifStatementProcessing.getStatementList(),variableMap);
                    executedBlock = true;
                }
                int currentElifChain = 0;
                //if there are elif/elses, loop through all but last one. If a block is executed, exit loop
                if(executedBlock == false && ifStatementProcessing.getElseIfList().size() != 0)
                {
                    IfStatement elseNode = ifStatementProcessing.getElseIfList().getLast();
                    while (executedBlock == false && ifStatementProcessing.getElseIfList().get(currentElifChain) != elseNode)
                    {
                        expression = ifStatementProcessing.getElseIfList().get(currentElifChain).getStatementCondition();
                        if (EvaluateBooleanExpression(expression,variableMap))
                        {
                            InterpretBlock(ifStatementProcessing.getElseIfList().get(currentElifChain).getStatementList(),variableMap);
                            executedBlock = true;
                        }
                        else
                        {
                            currentElifChain++;
                        }
                    }
                }
                //if nothing has been executed and there is an existing elif/else
                if(executedBlock == false && ifStatementProcessing.getElseIfList().size() != 0)
                {
                    //check if else statement
                    if(ifStatementProcessing.getElseIfList().get(currentElifChain).getStatementCondition() == null)
                    {
                        InterpretBlock(ifStatementProcessing.getElseIfList().get(currentElifChain).getStatementList(),variableMap);
                    }
                    else
                    {
                        if(EvaluateBooleanExpression(ifStatementProcessing.getElseIfList().get(currentElifChain).getStatementCondition(),variableMap))
                        {
                            InterpretBlock(ifStatementProcessing.getElseIfList().get(currentElifChain).getStatementList(),variableMap);
                        }
                    }
                }
            }
        }
    }
}
