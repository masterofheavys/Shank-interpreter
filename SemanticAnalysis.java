package icsi311;

import java.util.ArrayList;
import java.util.HashMap;

public class SemanticAnalysis
{
    public static void CheckAssignments(ArrayList<FunctionNode> functionList)
    {
        for (int i = 0; i < functionList.size(); i++)
        {
            //get all variables in function and corresponding types
            HashMap<String,descriptor> variableTypes = new HashMap<>();
            for (int j = 0; j < functionList.get(i).getLocalParameterList().size(); j++)
            {
                variableTypes.put(functionList.get(i).getLocalParameterList().get(j).getName(),functionList.get(i).getLocalParameterList().get(j).getDataType());
            }
            for (int j = 0; j < functionList.get(i).getLocalVariableList().size(); j++)
            {
                variableTypes.put(functionList.get(i).getLocalVariableList().get(j).getName(),functionList.get(i).getLocalVariableList().get(j).getDataType());
            }
            for (int j = 0; j < functionList.get(i).getLocalStatementList().size(); j++)
            {
                if (functionList.get(i).getLocalStatementList().get(j) instanceof AssignmentNode)
                {
                    //get target datatype and all assignment values data types
                    AssignmentNode tempNode = (AssignmentNode)functionList.get(i).getLocalStatementList().get(j);
                    descriptor assignmentDataType = variableTypes.get(tempNode.getLocalVariableReference().getTarget());
                    ArrayList<descriptor> expressionType = traverseExpression(tempNode.getExpression(),variableTypes);
                    //make sure target and expression types match
                    if (assignmentDataType == descriptor.CHARACTER || assignmentDataType == descriptor.STRING)
                    {
                        for (int k = 0; k < expressionType.size(); k++)
                        {
                            if (expressionType.get(k) != descriptor.CHARACTER && expressionType.get(k) != descriptor.STRING)
                            {
                                throw new RuntimeException("INVALID TYPE IN ASSIGNMENT " + assignmentDataType + " CANNOT BE ASSIGNED TO " + expressionType.get(k));
                            }
                        }
                    }
                    else
                    {
                        for (int k = 0; k < expressionType.size(); k++)
                        {
                            if (assignmentDataType != expressionType.get(k))
                            {
                                throw new RuntimeException("INVALID TYPE IN ASSIGNMENT " + tempNode.getLocalVariableReference().getTarget() + " " + assignmentDataType + " CANNOT BE ASSIGNED TO " + expressionType.get(k));
                            }
                        }
                    }

                }
                //for any block statements that may have nested statements, check all assignments within the block
                else if (functionList.get(i).getLocalStatementList().get(j) instanceof ForStatement)
                {
                    ForStatement tempFor = (ForStatement) functionList.get(i).getLocalStatementList().get(j);
                    checkBlockAssignments(tempFor.getForStatementList(),variableTypes);
                }
                else if (functionList.get(i).getLocalStatementList().get(j) instanceof RepeatStatement)
                {
                    RepeatStatement tempFor = (RepeatStatement) functionList.get(i).getLocalStatementList().get(j);
                    checkBlockAssignments(tempFor.getRepeatCollection(),variableTypes);
                }
                else if (functionList.get(i).getLocalStatementList().get(j) instanceof WhileStatement)
                {
                    WhileStatement tempWhile = (WhileStatement) functionList.get(i).getLocalStatementList().get(j);
                    checkBlockAssignments(tempWhile.getWhileCollection(),variableTypes);
                }
                else if (functionList.get(i).getLocalStatementList().get(j) instanceof IfStatement)
                {
                    IfStatement tempIf = (IfStatement) functionList.get(i).getLocalStatementList().get(j);
                    checkBlockAssignments(tempIf.getStatementList(),variableTypes);
                }
            }
        }
    }

    public static void checkBlockAssignments(ArrayList<StatementNode> statementList,HashMap<String,descriptor> typeMap)
    {
        for (int i = 0; i < statementList.size(); i++)
        {
            //check assignment node types
            if (statementList.get(i) instanceof AssignmentNode)
            {
                AssignmentNode tempNode = (AssignmentNode)statementList.get(i);
                descriptor assignmentDataType = typeMap.get(tempNode.getLocalVariableReference().getTarget());
                ArrayList<descriptor> expressionType = traverseExpression(tempNode.getExpression(),typeMap);
                if (assignmentDataType == descriptor.CHARACTER || assignmentDataType == descriptor.STRING)
                {
                    for (int k = 0; k < expressionType.size(); k++)
                    {
                        if (expressionType.get(k) != descriptor.CHARACTER && expressionType.get(k) != descriptor.STRING)
                        {
                            throw new RuntimeException("INVALID TYPE IN ASSIGNMENT " + assignmentDataType + " CANNOT BE ASSIGNED TO " + expressionType.get(k));
                        }
                    }
                }
                else
                {
                    for (int k = 0; k < expressionType.size(); k++)
                    {
                        if (assignmentDataType != expressionType.get(k))
                        {
                            throw new RuntimeException("INVALID TYPE IN ASSIGNMENT: TYPE " + assignmentDataType + " CANNOT BE ASSIGNED TO VARIABLE [" +  tempNode.getLocalVariableReference().getTarget() + "] WHOSE TYPE IS " + expressionType.get(k));
                        }
                    }
                }
            }
            //recursively call with any statement that may have nested assignments
            else if (statementList.get(i) instanceof ForStatement)
            {
                ForStatement tempFor = (ForStatement) statementList.get(i);
                checkBlockAssignments(tempFor.getForStatementList(),typeMap);
            }
            else if (statementList.get(i) instanceof WhileStatement)
            {
                WhileStatement tempWhile = (WhileStatement) statementList.get(i);
                checkBlockAssignments(tempWhile.getWhileCollection(),typeMap);
            }
            else if (statementList.get(i) instanceof RepeatStatement)
            {
                RepeatStatement tempRepeat = (RepeatStatement) statementList.get(i);
                checkBlockAssignments(tempRepeat.getRepeatCollection(),typeMap);
            }
            else if (statementList.get(i) instanceof IfStatement)
            {
                IfStatement tempIf = (IfStatement) statementList.get(i);
                checkBlockAssignments(tempIf.getStatementList(),typeMap);
            }
        }
    }
    public static ArrayList<descriptor> traverseExpression(Node expression,HashMap<String,descriptor> typeMap)
    {
        ArrayList<descriptor> toReturn = new ArrayList<>();
        //recursively call
        if (expression instanceof MathOpNode)
        {
            toReturn.addAll(traverseExpression(((MathOpNode) expression).getLeftNode(),typeMap));
            toReturn.addAll(traverseExpression(((MathOpNode) expression).getRightNode(),typeMap));
        }
        //base case, add type of base element to return list
        else
        {
            if(expression instanceof VariableNode)
            {
                toReturn.add(((VariableNode) expression).getDataType());
            }
            else if(expression instanceof VariableReferenceNode)
            {
                toReturn.add(typeMap.get(((VariableReferenceNode) expression).getTarget()));
            }
            else if(expression instanceof BoolNode)
            {
                toReturn.add(descriptor.BOOLEAN);
            }
            else if(expression instanceof IntegerNode)
            {
                toReturn.add(descriptor.INTEGER);
            }
            else if(expression instanceof FloatNode)
            {
                toReturn.add(descriptor.REAL);
            }
            else if(expression instanceof CharNode)
            {
                toReturn.add(descriptor.CHARACTER);
            }
            else if(expression instanceof StringNode)
            {
                toReturn.add(descriptor.STRING);
            }

        }
        //return all types
        return toReturn;
    }
}
