package icsi311;
import jdk.jshell.spi.ExecutionControl;

import java.beans.Expression;
import java.beans.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.Float.parseFloat;

public class Parser
{
    private ArrayList<Node> returnNodeStorage = new ArrayList<Node>();
    private ArrayList<Token> parserList;
    public Parser(ArrayList<Token> argumentList)
    {
        parserList = argumentList;
    }

    public ArrayList<Node> Parse() throws ParserException
    {
        //expression will always return one root node
        //returnNodeStorage.add(Expression());
        //as long as define keyword is detected, attempt function construction

        while (checkNext(descriptor.DEFINE) == true)
        {

            returnNodeStorage.add(functionDefinition());
            //clear end of lines before attempting to parse next function
            while (checkNext(descriptor.EndOfLine)== true)
            {
                matchAndRemove(descriptor.EndOfLine);
            }
        }
        //if there are tokens left and an error was not detected elsewhere, there is a missing define token
        matchAndRemove(descriptor.EndOfLine);
        if (parserList.size() != 0 && checkNext(descriptor.DEFINE) == false)
        {
            throw new ParserException("NO DEFINE FOUND");
        }
        return  returnNodeStorage;
    }

    public Node functionDefinition() throws ParserException {
        FunctionNode rootNode = new FunctionNode();
        //Check for define keyword
        Token tokenStorage = matchAndRemove(descriptor.DEFINE);
        if (tokenStorage == null)
        {
            throw new ParserException("NO DEFINE FOUND");
        }
        //check for identifier
        tokenStorage = matchAndRemove(descriptor.IDENTIFIER);
        if(tokenStorage == null)
        {
            throw new ParserException("NO IDENTIFER");
        }
        //Set the name of the function to the name of the identifier
        rootNode.setName(tokenStorage.getValueString());
        tokenStorage = matchAndRemove(descriptor.LEFTPARENTHESIS);
        //check for a left parenthesis
        if(tokenStorage == null)
        {
            throw new ParserException("NO LEFT PARENTHESIS");
        }
        //check for an identifier
        if(checkNext(descriptor.IDENTIFIER) || checkNext(descriptor.VAR))
        {
            //process arguments
            (rootNode.getLocalParameterList()).addAll(processArguments());
        }
        else
        {
            //check for right parenthesis
            if(matchAndRemove(descriptor.RIGHTPARENTHESIS) == null)
            {
                //report error
                throw new ParserException("NO RIGHT PARENTHESIS");
            }
        }
        //check for end of line
        tokenStorage = matchAndRemove(descriptor.EndOfLine);
        if(tokenStorage == null)
        {
            //report error
            throw new ParserException("MSSING END OF LINE");
        }
        //check for constants keyword
        tokenStorage = matchAndRemove(descriptor.CONSTANTS);
        if(tokenStorage != null)
        {
            //check for end of line
            if(matchAndRemove(descriptor.EndOfLine) == null)
            {
                //report error
                throw new ParserException("MSSING END OF LINE");
            }
            else
            {
                //process constants
                rootNode.getLocalVariableList().addAll(processConstants());
            }
        }
        //check for variables or begin
        else if(checkNext(descriptor.VARIABLES) == false && checkNext(descriptor.BEGIN) == false)
        {
            throw new ParserException("INVALID FUNCTION DEFINITION");
        }
        //check for variables keyword
        tokenStorage = matchAndRemove(descriptor.VARIABLES);
        if(tokenStorage != null)
        {
            //check for end of line
            if(matchAndRemove(descriptor.EndOfLine) == null)
            {
                //report error
                throw new ParserException("MSSING END OF LINE");
            }
            else
            {
                //process variables
                rootNode.getLocalVariableList().addAll(processVariables());
            }
        }
        //need begin keyword
        while(matchAndRemove(descriptor.EndOfLine) != null)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        functionBody(rootNode);
        return rootNode;
    }

    public AssignmentNode assignments() throws ParserException
    {
        //look for pattern: IDENTIFIER ASSIGNMENT EXPRESSION NEWLINE
        AssignmentNode toReturn;
        Token localTokenStorage;
        localTokenStorage = matchAndRemove(descriptor.IDENTIFIER);
        if(matchAndRemove(descriptor.ASSIGNMENT) == null)
        {
            throw new ParserException("MISSING ASSIGNMENT");
        }
        if(checkNext(descriptor.Number) == false && checkNext(descriptor.MINUS) == false && checkNext(descriptor.PLUS)== false && checkNext(descriptor.LEFTPARENTHESIS) == false && checkNext(descriptor.IDENTIFIER) == false && checkNext(descriptor.FALSE) && checkNext(descriptor.TRUE))
        {
            throw new ParserException("MISSING EXPRESSION");
        }
        toReturn = (new AssignmentNode(new VariableReferenceNode(localTokenStorage.getValueString()),Expression()));
        //need at least one end of line
        if(matchAndRemove(descriptor.EndOfLine) == null)
        {
            System.out.println(parserList.get(0).getValueString());
            throw new ParserException("MISSING END OF LINE");
        }
        //can have infinite following
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        return toReturn;
    }
    public ForStatement processForStatement() throws ParserException
    {
        Node leftSide;
        Node rightSide;
        ForStatement toReturn;
        Token localTokenStorage;
        //clear detected for keyword
        localTokenStorage = matchAndRemove(descriptor.FOR);
        //check for counter variable name
        if(checkNext(descriptor.IDENTIFIER) == false)
        {
            throw new ParserException("MISSING VARIABLE IN FOR LOOP");
        }
        localTokenStorage = matchAndRemove(descriptor.IDENTIFIER);
        VariableReferenceNode referenceNodeStorage = new VariableReferenceNode(localTokenStorage.getValueString());
        //check for from keyword
        localTokenStorage = matchAndRemove(descriptor.FROM);
        if(localTokenStorage == null)
        {
            throw new ParserException("MISSING FROM IN FOR LOOP");
        }
        //check for valid number
        if(checkNext(descriptor.MINUS) || checkNext(descriptor.PLUS) || checkNext(descriptor.Number))
        {
            leftSide = Expression();
        }
        else if (checkNext(descriptor.IDENTIFIER))
        {
            localTokenStorage = matchAndRemove(descriptor.IDENTIFIER);
            leftSide = new VariableReferenceNode(localTokenStorage.getValueString());
        }
        else
        {
            throw new ParserException("MISSING LEFT SIDE OF BOOLEAN IN FOR LOOP");
        }
        //check for to keyword
        localTokenStorage = matchAndRemove(descriptor.TO);
        if (localTokenStorage == null)
        {
            throw new ParserException("MISSING TO IN FOR LOOP");
        }
        //check for valid number
        if(checkNext(descriptor.MINUS) || checkNext(descriptor.PLUS) || checkNext(descriptor.Number))
        {
            rightSide = Expression();
        }
        else if (checkNext(descriptor.IDENTIFIER))
        {
            localTokenStorage = matchAndRemove(descriptor.IDENTIFIER);
            rightSide = new VariableReferenceNode(localTokenStorage.getValueString());
        }
        else
        {
            throw new ParserException("MISSING RIGHT SIDE OF BOOLEAN IN FOR LOOP");
        }
        //deal with end of line
        if (matchAndRemove(descriptor.EndOfLine) == null)
        {
            throw new ParserException("MISSING END OF LINE");
        }
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //check for begin keyword
        if(matchAndRemove(descriptor.BEGIN) == null)
        {
            throw new ParserException("MISSING BEGIN IN FOR LOOP");
        }
        //deal with end of line
        if (matchAndRemove(descriptor.EndOfLine) == null)
        {
            throw new ParserException("MISSING END OF LINE");
        }
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //process all statements in loop
        ArrayList<StatementNode> forStatementList = statements();
        //deal with end of line
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //check for end keyword
        localTokenStorage = matchAndRemove(descriptor.END);
        if (localTokenStorage == null)
        {
            throw new ParserException("MISSING END");
        }
        toReturn = new ForStatement(referenceNodeStorage,leftSide,rightSide,forStatementList);
        return toReturn;
    }
    public WhileStatement processWhileStatement() throws ParserException
    {
        WhileStatement toReturn;
        Token localTokenStorage;
        //deal with already detected while keyowrd
        localTokenStorage = matchAndRemove(descriptor.WHILE);
        //get boolean expression
        BooleanExpression whileExpression = (BooleanExpression)Expression();
        //deal with end of line
        if (matchAndRemove(descriptor.EndOfLine) == null)
        {
            throw new ParserException("MISSING END OF LINE");
        }
        //look for begin keyword
        localTokenStorage = matchAndRemove(descriptor.BEGIN);
        if (localTokenStorage == null)
        {
            throw new ParserException("MISSING BEGIN");
        }
        //deal with end of line
        if (matchAndRemove(descriptor.EndOfLine) == null)
        {
            throw new ParserException("MISSING END OF LINE");
        }
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //process all statements within loop
        ArrayList<StatementNode> whileStatementList = statements();
        //deal with end of line
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //check for end keyword
        localTokenStorage = matchAndRemove(descriptor.END);
        if (localTokenStorage == null)
        {
            throw new ParserException("MISSING END");
        }
        toReturn = new WhileStatement(whileExpression,whileStatementList);
        return toReturn;
    }
    public RepeatStatement processRepeatStatement() throws ParserException
    {
        //deal with repeat keyword that was detected
        matchAndRemove(descriptor.REPEAT);
        //deal with end of line
        if (matchAndRemove(descriptor.EndOfLine) == null)
        {
            throw new ParserException("MISSING END OF LINE");
        }
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //look for being
        Token localTokenStorage = matchAndRemove(descriptor.BEGIN);
        if(localTokenStorage == null)
        {
            throw new ParserException("BEGIN");
        }
        //deal with end of line
        if (matchAndRemove(descriptor.EndOfLine) == null)
        {
            throw new ParserException("MISSING END OF LINE");
        }
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //process statements in loop
        ArrayList<StatementNode> statementList = statements();
        //deal with end of line
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //look for end keyword
        localTokenStorage = matchAndRemove(descriptor.END);
        if (localTokenStorage == null)
        {
            throw new ParserException("MISSING END");
        }
        //deal with end of line
        if (matchAndRemove(descriptor.EndOfLine) == null)
        {
            throw new ParserException("MISSING END OF LINE");
        }
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //look for until keyword
        localTokenStorage = matchAndRemove(descriptor.UNTIL);
        if(localTokenStorage == null)
        {
            throw new ParserException("MISSING UNTL");
        }
        BooleanExpression repeatExpression = (BooleanExpression)Expression();
        RepeatStatement toReturn = new RepeatStatement(repeatExpression,statementList);
        return toReturn;

    }
    public IfStatement processIfStatement() throws ParserException
    {
        //deal with if keyowrd already found
        matchAndRemove(descriptor.IF);
        //process boolean
        BooleanExpression ifExpression = (BooleanExpression)Expression();
        //look for then keyword
        if (matchAndRemove(descriptor.THEN) == null)
        {
            throw new ParserException("MISSING THEN");
        }
        //deal with end of line
        if (matchAndRemove(descriptor.EndOfLine) == null)
        {
            throw new ParserException("MISSING END OF LINE");
        }
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //look for begin keyword
        Token localTokenStorage = matchAndRemove(descriptor.BEGIN);
        if(localTokenStorage == null)
        {
            throw new ParserException("BEGIN");
        }
        //deal with end of line
        if (matchAndRemove(descriptor.EndOfLine) == null)
        {
            throw new ParserException("MISSING END OF LINE");
        }
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //get statement
        ArrayList<StatementNode> IfStatmentList = statements();
        LinkedList<IfStatement> ElseIfList = new LinkedList<>();
        //check for end
        localTokenStorage = matchAndRemove(descriptor.END);
        if (localTokenStorage == null)
        {
            throw new ParserException("MISSING END");
        }
        //deal with end of line
        if (matchAndRemove(descriptor.EndOfLine) == null)
        {
            throw new ParserException("MISSING END OF LINE");
        }
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //get elsif
        while(checkNext(descriptor.ELSIF))
        {
            //check for keyword
            matchAndRemove(descriptor.ELSIF);
            //get boolean expression
            BooleanExpression elifExpression = (BooleanExpression)Expression();
            //check for then keyword
            if (matchAndRemove(descriptor.THEN) == null)
            {
                throw new ParserException("MISSING THEN");
            }
            //deal with end of line
            if (matchAndRemove(descriptor.EndOfLine) == null)
            {
                throw new ParserException("MISSING END OF LINE");
            }
            while(checkNext(descriptor.EndOfLine) == true)
            {
                matchAndRemove(descriptor.EndOfLine);
            }
            //check for begin
            localTokenStorage = matchAndRemove(descriptor.BEGIN);
            if(localTokenStorage == null)
            {
                throw new ParserException("BEGIN");
            }
            //deal with end of line
            if (matchAndRemove(descriptor.EndOfLine) == null)
            {
                throw new ParserException("MISSING END OF LINE");
            }
            while(checkNext(descriptor.EndOfLine) == true)
            {
                matchAndRemove(descriptor.EndOfLine);
            }
            //get statements
            ArrayList<StatementNode> elIfStatmentList = statements();
            //check for end
            localTokenStorage = matchAndRemove(descriptor.END);
            if (localTokenStorage == null)
            {
                throw new ParserException("MISSING END");
            }
            //deal with end of line
            if (matchAndRemove(descriptor.EndOfLine) == null)
            {
                throw new ParserException("MISSING END OF LINE");
            }
            while(checkNext(descriptor.EndOfLine) == true)
            {
                matchAndRemove(descriptor.EndOfLine);
            }
            ElseIfList.add(new IfStatement(elifExpression,elIfStatmentList));
        }
        if (matchAndRemove(descriptor.ELSE) != null)
        {
            //deal with end of line
            if (matchAndRemove(descriptor.EndOfLine) == null)
            {
                throw new ParserException("MISSING END OF LINE");
            }
            while(checkNext(descriptor.EndOfLine) == true)
            {
                matchAndRemove(descriptor.EndOfLine);
            }
            //check for begin
            localTokenStorage = matchAndRemove(descriptor.BEGIN);
            if(localTokenStorage == null)
            {
                throw new ParserException("BEGIN");
            }
            //deal with end of line
            if (matchAndRemove(descriptor.EndOfLine) == null)
            {
                throw new ParserException("MISSING END OF LINE");
            }
            while(checkNext(descriptor.EndOfLine) == true)
            {
                matchAndRemove(descriptor.EndOfLine);
            }
            //gets statements
            ArrayList<StatementNode> elseStatmentList = statements();
            //check for end
            localTokenStorage = matchAndRemove(descriptor.END);
            if (localTokenStorage == null)
            {
                throw new ParserException("MISSING END");
            }
            //deal with end of line
            if (matchAndRemove(descriptor.EndOfLine) == null)
            {
                throw new ParserException("MISSING END OF LINE");
            }
            while(checkNext(descriptor.EndOfLine) == true)
            {
                matchAndRemove(descriptor.EndOfLine);
            }
            ElseIfList.add(new IfStatement(elseStatmentList));
        }
        return new IfStatement(ifExpression,ElseIfList,IfStatmentList);
    }
    public FunctionCall processFunctionCall() throws ParserException
    {
        //get name of function
        Token localTokenStorage = matchAndRemove(descriptor.IDENTIFIER);
        String functionCallName = localTokenStorage.getValueString();
        ArrayList<ParameterNode> parameterList = new ArrayList<>();
        //process parameters
        while(checkNext(descriptor.Number) || checkNext(descriptor.VAR) || checkNext(descriptor.IDENTIFIER) || checkNext(descriptor.MINUS) || checkNext(descriptor.PLUS) || checkNext(descriptor.STRING) || checkNext(descriptor.CHARACTER) || checkNext(descriptor.BOOLEAN))
        {
            //process number
            if(checkNext(descriptor.Number))
            {
                localTokenStorage = matchAndRemove(descriptor.Number);
                if((localTokenStorage.getValueString()).contains(".") == false)
                {
                    parameterList.add(new ParameterNode(new IntegerNode((int)parseFloat(localTokenStorage.getValueString()))));
                }
                else
                {
                    parameterList.add(new ParameterNode(new FloatNode(parseFloat(localTokenStorage.getValueString()))));
                }
            }
            else if (checkNext(descriptor.MINUS))
            {
                if(checkOneAhead(descriptor.Number) == false)
                {
                    throw new RuntimeException("INVALID NUMBER");
                }
                matchAndRemove(descriptor.MINUS);
                localTokenStorage = matchAndRemove(descriptor.Number);
                String appendMinus = "-";
                appendMinus += localTokenStorage.getValueString();
                localTokenStorage.setValueString(appendMinus);
                if((localTokenStorage.getValueString()).contains(".") == false)
                {
                    parameterList.add(new ParameterNode(new IntegerNode((int)parseFloat(localTokenStorage.getValueString()))));
                }
                else
                {
                    parameterList.add(new ParameterNode(new FloatNode(parseFloat(localTokenStorage.getValueString()))));
                }
            }
            else if(checkNext(descriptor.PLUS))
            {
                matchAndRemove(descriptor.PLUS);
                if(checkNext(descriptor.Number) == false)
                {
                    throw new RuntimeException("INVALID NUMBER");
                }
                localTokenStorage = matchAndRemove(descriptor.Number);
                if((localTokenStorage.getValueString()).contains(".") == false)
                {
                    parameterList.add(new ParameterNode(new IntegerNode((int)parseFloat(localTokenStorage.getValueString()))));
                }
                else
                {
                    parameterList.add(new ParameterNode(new FloatNode(parseFloat(localTokenStorage.getValueString()))));
                }
            }
            //process variable reference node
            else if(checkNext(descriptor.VAR))
            {
                localTokenStorage = matchAndRemove(descriptor.VAR);
                localTokenStorage = matchAndRemove(descriptor.IDENTIFIER);
                if (localTokenStorage == null)
                {
                    throw new ParserException("MISSING IDENTIFER AFTER VAR");
                }
                parameterList.add(new ParameterNode(new VariableReferenceNode(localTokenStorage.getValueString())));
            }
            else if (checkNext(descriptor.STRING))
            {
                localTokenStorage = matchAndRemove(descriptor.STRING);
                parameterList.add(new ParameterNode(new StringNode(localTokenStorage.getValueString())));
            }
            else if (checkNext(descriptor.CHARACTER))
            {
                localTokenStorage = matchAndRemove(descriptor.CHARACTER);
                parameterList.add(new ParameterNode(new CharNode(localTokenStorage.getValueString().charAt(0))));
            }
            else if (checkNext(descriptor.BOOLEAN))
            {
                localTokenStorage = matchAndRemove(descriptor.BOOLEAN);
                parameterList.add(new ParameterNode(new BoolNode(Boolean.parseBoolean(localTokenStorage.getValueString()))));
            }
            //process variable by value
            else
            {
                localTokenStorage = matchAndRemove(descriptor.IDENTIFIER);
                parameterList.add(new ParameterNode(new VariableNode(localTokenStorage.getValueString(),true)));
            }
            //check for comma
            localTokenStorage = matchAndRemove(descriptor.COMMA);
            //if no comma, must be no other arguments
            if (localTokenStorage == null)
            {
                if (checkNext(descriptor.EndOfLine) == false)
                {
                    throw new ParserException("COMMA ERROR");
                }
            }
        }
        //return statement
        return new FunctionCall(functionCallName,parameterList);
    }
    public StatementNode statement() throws ParserException
    {
        //deal with end of lines
        while (matchAndRemove(descriptor.EndOfLine) != null)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //determine which function to call
        if(checkOneAhead(descriptor.ASSIGNMENT))
        {
            return assignments();
        }
        else if(checkNext(descriptor.WHILE) == true)
        {
            return processWhileStatement();
        }
        else if(checkNext(descriptor.FOR) == true)
        {
            return processForStatement();
        }
        else if(checkNext(descriptor.REPEAT) == true)
        {
            return processRepeatStatement();
        }
        else if(checkNext(descriptor.IF) == true)
        {
            return processIfStatement();
        }
        else if(checkNext(descriptor.IDENTIFIER))
        {
            return processFunctionCall();
        }
        else
        {
            return null;
        }
    }
    //Statement just casts assignment as statement and returns the list
    public ArrayList<StatementNode> statements() throws ParserException
    {
        ArrayList<StatementNode> toReturn = new ArrayList<>();
        StatementNode statmentStorage = statement();
        while (statmentStorage != null)
        {
            toReturn.add(statmentStorage);
            statmentStorage = statement();
        }
        return toReturn;
    }
    public boolean functionBody(FunctionNode rootNode) throws ParserException {
        //check for sequence: BEGIN ENDOFLINE END
        while(matchAndRemove(descriptor.EndOfLine) != null)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        if(matchAndRemove(descriptor.BEGIN) == null)
        {
            throw new ParserException("MISSING BEGIN");
        }
        //need at least one end of line, can have infinite
        if (matchAndRemove(descriptor.EndOfLine) == null)
        {
            throw new ParserException("MISSING END OF LINE");
        }
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        rootNode.setLocalStatementList(statements());
        while(checkNext(descriptor.EndOfLine) == true)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        if(matchAndRemove(descriptor.END) == null)
        {
            System.out.println(parserList.get(0).getValueString());
            throw new ParserException("MISSING END");
        }
        return true;
    }
    public ArrayList<VariableNode> processVariables() throws ParserException {
        Token tokenStorage;
        ArrayList<String> variableNameStorage = new ArrayList<>();
        ArrayList<VariableNode> listOfVariables = new ArrayList<>();
        while (matchAndRemove(descriptor.EndOfLine) != null)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //for every identifer in the sequence
        while (checkNext(descriptor.IDENTIFIER) == true)
        {
            tokenStorage = matchAndRemove(descriptor.IDENTIFIER);
            //store the name of the indentifier
            variableNameStorage.add(tokenStorage.getValueString());
            //check for colon
            if(checkNext(descriptor.COLON))
            {
                matchAndRemove(descriptor.COLON);
                //check for variable type
                if(matchAndRemove(descriptor.REAL) != null)
                {
                    for (int i = 0; i < variableNameStorage.size(); i++)
                    {
                        listOfVariables.add(new VariableNode(variableNameStorage.get(i),false,descriptor.REAL));
                    }
                    //need at least one end of line, can have infinite
                    if (matchAndRemove(descriptor.EndOfLine) == null)
                    {
                        throw new ParserException("MISSING END OF LINE");
                    }
                    while(checkNext(descriptor.EndOfLine) == true)
                    {
                        matchAndRemove(descriptor.EndOfLine);
                    }
                    variableNameStorage.clear();
                }
                else if (matchAndRemove(descriptor.INTEGER) != null)
                {
                    for (int i = 0; i < variableNameStorage.size(); i++)
                    {
                        listOfVariables.add(new VariableNode(variableNameStorage.get(i),false,descriptor.INTEGER));
                    }
                    matchAndRemove(descriptor.EndOfLine);
                    while(checkNext(descriptor.EndOfLine) == true)
                    {
                        matchAndRemove(descriptor.EndOfLine);
                    }
                    variableNameStorage.clear();
                }
                else if (matchAndRemove(descriptor.BOOLEANTYPE) != null)
                {
                    for (int i = 0; i < variableNameStorage.size(); i++)
                    {
                        listOfVariables.add(new VariableNode(variableNameStorage.get(i),false,descriptor.BOOLEAN));
                    }
                    matchAndRemove(descriptor.EndOfLine);
                    while(checkNext(descriptor.EndOfLine) == true)
                    {
                        matchAndRemove(descriptor.EndOfLine);
                    }
                    variableNameStorage.clear();
                }
                else if (matchAndRemove(descriptor.BOOLEANTYPE) != null)
                {
                    for (int i = 0; i < variableNameStorage.size(); i++)
                    {
                        listOfVariables.add(new VariableNode(variableNameStorage.get(i),false,descriptor.BOOLEAN));
                    }
                    matchAndRemove(descriptor.EndOfLine);
                    while(checkNext(descriptor.EndOfLine) == true)
                    {
                        matchAndRemove(descriptor.EndOfLine);
                    }
                    variableNameStorage.clear();
                }
                else if (matchAndRemove(descriptor.STRINGTYPE) != null)
                {
                    for (int i = 0; i < variableNameStorage.size(); i++)
                    {
                        listOfVariables.add(new VariableNode(variableNameStorage.get(i),false,descriptor.STRING));
                    }
                    matchAndRemove(descriptor.EndOfLine);
                    while(checkNext(descriptor.EndOfLine) == true)
                    {
                        matchAndRemove(descriptor.EndOfLine);
                    }
                    variableNameStorage.clear();
                }
                else if (matchAndRemove(descriptor.CHARACTERTYPE) != null)
                {
                    for (int i = 0; i < variableNameStorage.size(); i++)
                    {
                        listOfVariables.add(new VariableNode(variableNameStorage.get(i),false,descriptor.CHARACTER));
                    }
                    matchAndRemove(descriptor.EndOfLine);
                    while(checkNext(descriptor.EndOfLine) == true)
                    {
                        matchAndRemove(descriptor.EndOfLine);
                    }
                    variableNameStorage.clear();
                }
                else
                {
                    throw new ParserException("MISSING INTEGER/REAL");
                }
            }
            //check for comma
            else if(checkNext(descriptor.COMMA))
            {
                matchAndRemove(descriptor.COMMA);
            }
            else
            {
                throw new ParserException("ERROR IN DECLARING VARIABLES");
            }
        }
        return listOfVariables;
    }
    public ArrayList<VariableNode> processConstants() throws ParserException {
        Token tokenStorage;
        String tokenStringStorage;
        ArrayList<VariableNode> listOfConstants = new ArrayList<>();
        while (matchAndRemove(descriptor.EndOfLine) != null)
        {
            matchAndRemove(descriptor.EndOfLine);
        }
        //for every identifier in sequence
        while(checkNext(descriptor.IDENTIFIER) == true)
        {
            tokenStorage = matchAndRemove(descriptor.IDENTIFIER);
            tokenStringStorage = tokenStorage.getValueString();
            tokenStorage = matchAndRemove(descriptor.EQUAL);
            //check for equals sign
            if (tokenStorage == null)
            {
                throw new ParserException("MISSING EQUALS SIGN");
            }
            if (checkNext(descriptor.TRUE) || checkNext(descriptor.FALSE))
            {
                if ((tokenStorage = matchAndRemove(descriptor.TRUE)) != null)
                {
                    listOfConstants.add(new VariableNode(tokenStringStorage,true,descriptor.BOOLEAN,new BoolNode(true)));
                }
                else
                {
                    matchAndRemove(descriptor.FALSE);
                    listOfConstants.add(new VariableNode(tokenStringStorage,true,descriptor.BOOLEAN,new BoolNode(false)));
                }
                if (matchAndRemove(descriptor.EndOfLine) == null)
                {
                    throw new ParserException("MISSING END OF LINE");
                }
                while(checkNext(descriptor.EndOfLine) == true)
                {
                    matchAndRemove(descriptor.EndOfLine);
                }
            }
            else if (checkNext(descriptor.STRING))
            {
                tokenStorage = matchAndRemove(descriptor.STRING);
                listOfConstants.add(new VariableNode(tokenStringStorage,true, descriptor.STRING, new StringNode((tokenStorage.getValueString()))));
                if (matchAndRemove(descriptor.EndOfLine) == null)
                {
                    throw new ParserException("MISSING END OF LINE");
                }
                while(checkNext(descriptor.EndOfLine) == true)
                {
                    matchAndRemove(descriptor.EndOfLine);
                }
            }
            else if (checkNext(descriptor.CHARACTER))
            {
                tokenStorage = matchAndRemove(descriptor.CHARACTER);
                listOfConstants.add(new VariableNode(tokenStringStorage,true, descriptor.CHARACTER, new CharNode((tokenStorage.getValueString().charAt(0)))));
                if (matchAndRemove(descriptor.EndOfLine) == null)
                {
                    throw new ParserException("MISSING END OF LINE");
                }
                while(checkNext(descriptor.EndOfLine) == true)
                {
                    matchAndRemove(descriptor.EndOfLine);
                }
            }
            else
            {
                //check for number
                if(checkNext(descriptor.MINUS) == false && checkNext(descriptor.PLUS) == false && checkNext(descriptor.Number) == false)
                {
                    throw new ParserException("MISSING NUMBER");
                }
                if (matchAndRemove(descriptor.MINUS) != null)
                {
                    tokenStorage = matchAndRemove(descriptor.Number);
                    if (tokenStorage != null)
                    {
                        tokenStorage.setValueString("-" + tokenStorage.getValueString());
                    }
                    else
                    {
                        throw new ParserException("MISSING NUMBER");
                    }
                    if((tokenStorage.getValueString().contains(".")) == false)
                    {
                        listOfConstants.add(new VariableNode(tokenStringStorage,true,descriptor.Number, new IntegerNode((int) parseFloat(tokenStorage.getValueString()))));
                    }
                    else
                    {
                        listOfConstants.add(new VariableNode(tokenStringStorage,true,descriptor.Number, new FloatNode(parseFloat(tokenStorage.getValueString()))));
                        listOfConstants.add(new VariableNode(tokenStringStorage,true,descriptor.Number, new FloatNode(parseFloat(tokenStorage.getValueString()))));
                    }
                }
                else if(matchAndRemove(descriptor.PLUS) != null)
                {
                    tokenStorage = matchAndRemove(descriptor.Number);
                    if (tokenStorage != null)
                    {
                        tokenStorage.setValueString("+" + tokenStorage.getValueString());
                    }
                    else
                    {
                        throw new ParserException("MISSING NUMBER");
                    }
                    if((tokenStorage.getValueString()).contains(".") == false)
                    {
                        listOfConstants.add(new VariableNode(tokenStringStorage,true,descriptor.Number, new IntegerNode((int) parseFloat(tokenStorage.getValueString()))));
                    }
                    else
                    {
                        listOfConstants.add(new VariableNode(tokenStringStorage,true,descriptor.Number, new FloatNode(parseFloat(tokenStorage.getValueString()))));
                    }
                }
                else
                {
                    tokenStorage = matchAndRemove(descriptor.Number);
                    if (tokenStorage != null)
                    {
                        tokenStorage.setValueString("+" + tokenStorage.getValueString());
                    }
                    else
                    {
                        throw new ParserException("MISSING NUMBER");
                    }
                    if((tokenStorage.getValueString()).contains(".") == false)
                    {
                        listOfConstants.add(new VariableNode(tokenStringStorage,true,descriptor.Number, new IntegerNode((int) parseFloat(tokenStorage.getValueString()))));
                    }
                    else
                    {
                        listOfConstants.add(new VariableNode(tokenStringStorage,true,descriptor.Number, new FloatNode(parseFloat(tokenStorage.getValueString()))));
                    }
                }
                //need at least one end of line, can have infinite
                if (matchAndRemove(descriptor.EndOfLine) == null)
                {
                    throw new ParserException("MISSING END OF LINE");
                }
                while(checkNext(descriptor.EndOfLine) == true)
                {
                    matchAndRemove(descriptor.EndOfLine);
                }
            }
        }
        return listOfConstants;
    }
    public ArrayList<VariableNode> processArguments() throws ParserException {
        Token tokenStorage;
        String tokenStringStorage;
        ArrayList<VariableNode> listOfArguments = new ArrayList<>();
        //check for identifier
        while (checkNext(descriptor.IDENTIFIER) == true || checkNext(descriptor.VAR) == true)
        {
            ArrayList<Boolean> isVar = new ArrayList<Boolean>();
            ArrayList<String> varNames = new ArrayList<>();
            do
            {
                tokenStorage = matchAndRemove(descriptor.VAR);
                if (tokenStorage != null)
                {
                    isVar.add(new Boolean(false));
                }
                else
                {
                    isVar.add(new Boolean(true));
                }
                tokenStorage = matchAndRemove(descriptor.IDENTIFIER);
                if(tokenStorage == null)
                {
                    throw new ParserException("MISSING IDENTIFIER");
                }
                tokenStringStorage = tokenStorage.getValueString();
                varNames.add(tokenStringStorage);

            } while (matchAndRemove(descriptor.COMMA) != null);
            tokenStorage = matchAndRemove(descriptor.COLON);
            //check for colon
            if(tokenStorage == null)
            {
                throw new ParserException("MISSING COLON");
            }
            //determine if int or real
            if ((tokenStorage = matchAndRemove(descriptor.REAL)) != null)
            {
                for(int i = 0; i < varNames.size(); i++)
                {
                    listOfArguments.add(new VariableNode(varNames.get(i), isVar.get(i),descriptor.REAL));

                }
                isVar = new ArrayList<Boolean>();
                varNames = new ArrayList<>();
            }
            else if ((tokenStorage = matchAndRemove(descriptor.INTEGER)) != null)
            {
                for(int i = 0; i < varNames.size(); i++)
                {
                    listOfArguments.add(new VariableNode(varNames.get(i), isVar.get(i),descriptor.INTEGER));
                }
                isVar = new ArrayList<Boolean>();
                varNames = new ArrayList<>();
            }
            else if ((tokenStorage = matchAndRemove(descriptor.BOOLEANTYPE)) != null)
            {
                for(int i = 0; i < varNames.size(); i++)
                {
                    listOfArguments.add(new VariableNode(varNames.get(i), isVar.get(i),descriptor.BOOLEAN));
                }
                isVar = new ArrayList<Boolean>();
                varNames = new ArrayList<>();
            }
            else if ((tokenStorage = matchAndRemove(descriptor.CHARACTERTYPE)) != null)
            {
                for(int i = 0; i < varNames.size(); i++)
                {
                    listOfArguments.add(new VariableNode(varNames.get(i), isVar.get(i),descriptor.CHARACTER));
                }
                isVar = new ArrayList<Boolean>();
                varNames = new ArrayList<>();
            }
            else if ((tokenStorage = matchAndRemove(descriptor.STRINGTYPE)) != null)
            {
                for(int i = 0; i < varNames.size(); i++)
                {
                    listOfArguments.add(new VariableNode(varNames.get(i), isVar.get(i),descriptor.STRING));
                }
                isVar = new ArrayList<Boolean>();
                varNames = new ArrayList<>();
            }
            else
            {
                throw new ParserException("MISSING VARIABLE TYPE");
            }
            //sequence must end with either semi colon or right parenthesis in the case of the last
            if(matchAndRemove(descriptor.SEMICOLON) == null && matchAndRemove(descriptor.RIGHTPARENTHESIS) == null)
            {
                throw new ParserException("MISSING SEMI COLON/RIGHT PARENTHESIS IN PARAMETER LIST");
            }
        }
        return listOfArguments;
    }
    //Expression deals with + or - operands as well as boolean expressions
    public Node Expression() throws ParserException {
        //get first term (preserves order of operations as term has higher precedence)
        Node termStorageOne = Term();
        descriptor comparator = null;
        //determine if boolean expression based on operator
        if(checkNext(descriptor.EQUAL) || checkNext(descriptor.NOTEQUAL) || checkNext(descriptor.GREATER) || checkNext(descriptor.GREATEROREQUAL) || checkNext(descriptor.LESSOREQUAL) || checkNext(descriptor.LESS))
        {
            if(matchAndRemove(descriptor.EQUAL) != null)
            {
                comparator = descriptor.EQUAL;
            }
            else if(matchAndRemove(descriptor.NOTEQUAL) != null)
            {
                comparator = descriptor.NOTEQUAL;
            }
            else if(matchAndRemove(descriptor.GREATER) != null)
            {
                comparator = descriptor.GREATER;
            }
            else if(matchAndRemove(descriptor.GREATEROREQUAL) != null)
            {
                comparator = descriptor.GREATEROREQUAL;
            }
            else if(matchAndRemove(descriptor.LESS) != null)
            {
                comparator = descriptor.LESS;
            }
            else if(matchAndRemove(descriptor.LESSOREQUAL) != null)
            {
                comparator = descriptor.LESSOREQUAL;
            }
        }
        if(comparator != null)
        {
            Node termStorageTwo = Expression();
            if(termStorageTwo instanceof BooleanExpression)
            {
                //prevent chaining of operators
                if((((BooleanExpression) termStorageTwo).getOperator()) != null)
                {
                    throw new ParserException("CANNOT CHAIN OPERATORS");
                }
                else
                {
                    return new BooleanExpression(termStorageOne,comparator,termStorageTwo);
                }
            }
            else
            {
                return new BooleanExpression(termStorageOne,comparator,termStorageTwo);
            }
        }
        else if(termStorageOne instanceof BoolNode)
        {
            return new BooleanExpression(termStorageOne,null,null);
        }
        //deal with numerical expressions
        else
        {
            Token matchStorage = matchAndRemove(descriptor.PLUS);
            //If plus operand found
            if(matchStorage != null)
            {
                //get the 2nd term (preserves order of operations as term has higher precedence)
                Node termStorageTwo = Term();
                //if there is a plus operator after the 2nd term
                if(checkNext(descriptor.PLUS) == true)
                {
                    //remove plus token (will be accounted for in constructor)
                    matchAndRemove(descriptor.PLUS);
                    //Get the right term of the outer MathOpNode
                    Node recursiveExpression = Expression();
                    //create the MathOpNode with the first two terms and operands
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.PLUS,termStorageOne,termStorageTwo);
                    //Construct full MathOpNode
                    return new MathOpNode(descriptor.PLUS,nestedMathOpNode,recursiveExpression);
                }
                //if there is a minus operator after 2nd term
                else if(checkNext(descriptor.MINUS) == true)
                {
                    //remove minus token (will be accounted for in constructor)
                    matchAndRemove(descriptor.MINUS);
                    //Get the right term of the outer MathOpNode
                    Node recursiveExpression = Expression();
                    //create the MathOpNode with the first two terms and operands
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.PLUS,termStorageOne,termStorageTwo);
                    //Construct full MathOpNode
                    return new MathOpNode(descriptor.MINUS,nestedMathOpNode,recursiveExpression);
                }
                else if(checkNext(descriptor.LESS) == true)
                {
                    matchAndRemove(descriptor.LESS);
                    Node recursiveExpression = Expression();
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.PLUS,termStorageOne,termStorageTwo);
                    return new BooleanExpression(nestedMathOpNode,descriptor.LESS,recursiveExpression);

                }
                else if(checkNext(descriptor.GREATER) == true)
                {
                    matchAndRemove(descriptor.GREATER);
                    Node recursiveExpression = Expression();
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.PLUS,termStorageOne,termStorageTwo);
                    return new BooleanExpression(nestedMathOpNode,descriptor.GREATER,recursiveExpression);

                }
                else if(checkNext(descriptor.LESSOREQUAL) == true)
                {
                    matchAndRemove(descriptor.LESSOREQUAL);
                    Node recursiveExpression = Expression();
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.PLUS,termStorageOne,termStorageTwo);
                    return new BooleanExpression(nestedMathOpNode,descriptor.LESSOREQUAL,recursiveExpression);

                }
                else if(checkNext(descriptor.GREATEROREQUAL) == true)
                {
                    matchAndRemove(descriptor.GREATEROREQUAL);
                    Node recursiveExpression = Expression();
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.PLUS,termStorageOne,termStorageTwo);
                    return new BooleanExpression(nestedMathOpNode,descriptor.GREATEROREQUAL,recursiveExpression);

                }
                else if(checkNext(descriptor.EQUAL) == true)
                {
                    matchAndRemove(descriptor.EQUAL);
                    Node recursiveExpression = Expression();
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.PLUS,termStorageOne,termStorageTwo);
                    return new BooleanExpression(nestedMathOpNode,descriptor.EQUAL,recursiveExpression);

                }
                else if(checkNext(descriptor.NOTEQUAL) == true)
                {
                    matchAndRemove(descriptor.NOTEQUAL);
                    Node recursiveExpression = Expression();
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.PLUS,termStorageOne,termStorageTwo);
                    return new BooleanExpression(nestedMathOpNode,descriptor.NOTEQUAL,recursiveExpression);

                }
                //if no plus or minus operator after initial expression
                else
                {
                    return new MathOpNode(descriptor.PLUS,termStorageOne,termStorageTwo);
                }
            }
            //if minus operator found
            matchStorage = matchAndRemove(descriptor.MINUS);
            if(matchStorage!= null)
            {
                //get the 2nd term (preserves order of operations as term has higher precedence)
                Node termStorageTwo = Term();
                //if there is a plus operator after the 2nd term
                if(checkNext(descriptor.PLUS) == true)
                {
                    //remove plus token (will be accounted for in constructor)
                    matchAndRemove(descriptor.PLUS);
                    //Get the right term of the outer MathOpNode
                    Node recursiveExpression = Expression();
                    //create the MathOpNode with the first two terms and operands
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.MINUS,termStorageOne,termStorageTwo);
                    //Construct full MathOpNode
                    return new MathOpNode(descriptor.PLUS,nestedMathOpNode,recursiveExpression);
                }
                else if(checkNext(descriptor.MINUS) == true)
                {
                    //remove minus token (will be accounted for in constructor)
                    matchAndRemove(descriptor.MINUS);
                    //Get the right term of the outer MathOpNode
                    Node recursiveExpression = Expression();
                    //create the MathOpNode with the first two terms and operands
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.MINUS,termStorageOne,termStorageTwo);
                    //Construct full MathOpNode
                    return new MathOpNode(descriptor.MINUS,nestedMathOpNode,recursiveExpression);
                }
                else if(checkNext(descriptor.LESS) == true)
                {
                    matchAndRemove(descriptor.LESS);
                    Node recursiveExpression = Expression();
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.MINUS,termStorageOne,termStorageTwo);
                    return new BooleanExpression(nestedMathOpNode,descriptor.LESS,recursiveExpression);

                }
                else if(checkNext(descriptor.GREATER) == true)
                {
                    matchAndRemove(descriptor.GREATER);
                    Node recursiveExpression = Expression();
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.MINUS,termStorageOne,termStorageTwo);
                    return new BooleanExpression(nestedMathOpNode,descriptor.GREATER,recursiveExpression);

                }
                else if(checkNext(descriptor.LESSOREQUAL) == true)
                {
                    matchAndRemove(descriptor.LESSOREQUAL);
                    Node recursiveExpression = Expression();
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.MINUS,termStorageOne,termStorageTwo);
                    return new BooleanExpression(nestedMathOpNode,descriptor.LESSOREQUAL,recursiveExpression);

                }
                else if(checkNext(descriptor.GREATEROREQUAL) == true)
                {
                    matchAndRemove(descriptor.GREATEROREQUAL);
                    Node recursiveExpression = Expression();
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.MINUS,termStorageOne,termStorageTwo);
                    return new BooleanExpression(nestedMathOpNode,descriptor.GREATEROREQUAL,recursiveExpression);

                }
                else if(checkNext(descriptor.EQUAL) == true)
                {
                    matchAndRemove(descriptor.EQUAL);
                    Node recursiveExpression = Expression();
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.MINUS,termStorageOne,termStorageTwo);
                    return new BooleanExpression(nestedMathOpNode,descriptor.EQUAL,recursiveExpression);

                }
                else if(checkNext(descriptor.NOTEQUAL) == true)
                {
                    matchAndRemove(descriptor.NOTEQUAL);
                    Node recursiveExpression = Expression();
                    MathOpNode nestedMathOpNode = new MathOpNode(descriptor.MINUS,termStorageOne,termStorageTwo);
                    return new BooleanExpression(nestedMathOpNode,descriptor.NOTEQUAL,recursiveExpression);

                }
                //if no plus or minus operator after initial expression
                else
                {
                    return new MathOpNode(descriptor.MINUS,termStorageOne,termStorageTwo);
                }
            }
            //if there's no operators, just return first term
            else
            {
                return termStorageOne;
            }
        }
    }
    public Node Term() throws ParserException {
        //get first factor
        Node factorStorageOne = Factor();
        Token matchStorage = matchAndRemove(descriptor.TIMES);
        //if times is found
        if(matchStorage != null)
        {
            //get 2nd factor
            Node factorStorageTwo = Factor();
            //check if next token is a multiplication token
            if(checkNext(descriptor.TIMES) == true)
            {
                //remove times (will be dealt with in constructor)
                matchAndRemove(descriptor.TIMES);
                //Get right term of outer MathOpNode
                Node recursiveTerm = Term();
                //create the MathOpNode with the first two terms and operands
                MathOpNode nestedMathOpNode = new MathOpNode(descriptor.TIMES,factorStorageOne,factorStorageTwo);
                //Construct full MathOpNode
                return new MathOpNode(descriptor.TIMES,nestedMathOpNode,recursiveTerm);
            }
            //check if next token is a division token
            else if(checkNext(descriptor.DIVIDE) == true)
            {
                //remove divide (will be dealt with in constructor)
                matchAndRemove(descriptor.DIVIDE);
                //Get right term of outer MathOpNode
                Node recursiveTerm = Term();
                //create the MathOpNode with the first two terms and operands
                MathOpNode nestedMathOpNode = new MathOpNode(descriptor.TIMES,factorStorageOne,factorStorageTwo);
                //Construct full MathOpNode
                return new MathOpNode(descriptor.DIVIDE,nestedMathOpNode,recursiveTerm);
            }
            //check if next token is a mod token
            else if(checkNext(descriptor.MOD) == true)
            {
                //remove mod (will be dealt with in constructor)
                matchAndRemove(descriptor.MOD);
                Node recursiveTerm = Term();
                //create the MathOpNode with the first two terms and operands
                MathOpNode nestedMathOpNode = new MathOpNode(descriptor.TIMES,factorStorageOne,factorStorageTwo);
                //Construct full MathOpNode
                return new MathOpNode(descriptor.MOD,nestedMathOpNode,recursiveTerm);
            }
            //if no multiply or divide operator after initial expression
            else
            {
                return new MathOpNode(descriptor.TIMES,factorStorageOne,factorStorageTwo);
            }
        }
        matchStorage = matchAndRemove(descriptor.DIVIDE);
        //if divide is found
        if(matchStorage != null)
        {
            //get 2nd factor
            Node factorStorageTwo = Factor();
            //check if next token is a multiplication token
            if(checkNext(descriptor.TIMES) == true)
            {
                //remove multiply (will be dealt with in constructor)
                matchAndRemove(descriptor.TIMES);
                //Get right term of outer MathOpNode
                Node recursiveTerm = Term();
                //create the MathOpNode with the first two terms and operands
                MathOpNode nestedMathOpNode = new MathOpNode(descriptor.DIVIDE,factorStorageOne,factorStorageTwo);
                //Construct full MathOpNode
                return new MathOpNode(descriptor.TIMES,nestedMathOpNode,recursiveTerm);
            }
            //check if next token is a division token
            else if(checkNext(descriptor.DIVIDE) == true)
            {
                //remove divide (will be dealt with in constructor)
                matchAndRemove(descriptor.DIVIDE);
                //Get right term of outer MathOpNode
                Node recursiveTerm = Term();
                //create the MathOpNode with the first two terms and operands
                MathOpNode nestedMathOpNode = new MathOpNode(descriptor.DIVIDE,factorStorageOne,factorStorageTwo);
                //Construct full MathOpNode
                return new MathOpNode(descriptor.DIVIDE,nestedMathOpNode,recursiveTerm);
            }
            //check if next token is a mod token
            else if(checkNext(descriptor.MOD) == true)
            {
                //remove mod (will be dealt with in constructor)
                matchAndRemove(descriptor.MOD);
                Node recursiveTerm = Term();
                //create the MathOpNode with the first two terms and operands
                MathOpNode nestedMathOpNode = new MathOpNode(descriptor.DIVIDE,factorStorageOne,factorStorageTwo);
                //Construct full MathOpNode
                return new MathOpNode(descriptor.MOD,nestedMathOpNode,recursiveTerm);
            }
            //if no multiply or divide operator after initial expression
            else
            {
                return new MathOpNode(descriptor.DIVIDE,factorStorageOne,factorStorageTwo);
            }
        }
        matchStorage = matchAndRemove(descriptor.MOD);
        //if MOD is found
        if(matchStorage != null)
        {
            //get 2nd factor
            Node factorStorageTwo = Factor();
            //check if next token is a multiplication token
            if(checkNext(descriptor.TIMES) == true)
            {
                //remove multiply (will be dealt with in constructor)
                matchAndRemove(descriptor.TIMES);
                //Get right term of outer MathOpNode
                Node recursiveTerm = Term();
                //create the MathOpNode with the first two terms and operands
                MathOpNode nestedMathOpNode = new MathOpNode(descriptor.MOD,factorStorageOne,factorStorageTwo);
                //Construct full MathOpNode
                return new MathOpNode(descriptor.TIMES,nestedMathOpNode,recursiveTerm);
            }
            //check if next token is a division token
            else if(checkNext(descriptor.DIVIDE) == true)
            {
                //remove divide (will be dealt with in constructor)
                matchAndRemove(descriptor.DIVIDE);
                //Get right term of outer MathOpNode
                Node recursiveTerm = Term();
                //create the MathOpNode with the first two terms and operands
                MathOpNode nestedMathOpNode = new MathOpNode(descriptor.MOD,factorStorageOne,factorStorageTwo);
                //Construct full MathOpNode
                return new MathOpNode(descriptor.DIVIDE,nestedMathOpNode,recursiveTerm);
            }
            //check if next token is a mod token
            else if(checkNext(descriptor.MOD) == true)
            {
                //remove mod (will be dealt with in constructor)
                matchAndRemove(descriptor.MOD);
                Node recursiveTerm = Term();
                //create the MathOpNode with the first two terms and operands
                MathOpNode nestedMathOpNode = new MathOpNode(descriptor.MOD,factorStorageOne,factorStorageTwo);
                //Construct full MathOpNode
                return new MathOpNode(descriptor.MOD,nestedMathOpNode,recursiveTerm);
            }
            //if no multiply or divide operator after initial expression
            else
            {
                return new MathOpNode(descriptor.MOD,factorStorageOne,factorStorageTwo);
            }
        }
        //if there's no operators, just return first factor
        else
        {
            return  factorStorageOne;
        }
    }
    public Node Factor() throws ParserException {
        Token localTokenStorage;
        localTokenStorage = matchAndRemove(descriptor.Number);
        //if token is a number
        if(localTokenStorage != null)
        {
            //get value of the value string
            float stringToFloat = parseFloat(localTokenStorage.getValueString());
            //handle if float or int
            if((localTokenStorage.getValueString().contains(".")))
            {
                return new FloatNode(stringToFloat);
            }
            else
            {
                return new IntegerNode((int)stringToFloat);
            }
        }
        else if (checkNext(descriptor.TRUE))
        {
            localTokenStorage = matchAndRemove(descriptor.TRUE);
            return new BoolNode(true);
        }
        else if ((checkNext(descriptor.FALSE)))
        {
            localTokenStorage = matchAndRemove(descriptor.FALSE);
            return new BoolNode(false);
        }
        else if((checkNext(descriptor.CHARACTER)))
        {
            localTokenStorage = matchAndRemove(descriptor.CHARACTER);
            return new CharNode((localTokenStorage.getValueString().charAt(0)));
        }
        else if (checkNext(descriptor.STRING))
        {
            localTokenStorage = matchAndRemove(descriptor.STRING);
            return new StringNode(localTokenStorage.getValueString());
        }
        //deal with unary minus
        else if ((localTokenStorage = matchAndRemove(descriptor.MINUS)) != null)
        {
            //check for number
            localTokenStorage = matchAndRemove(descriptor.Number);
            if(localTokenStorage == null)
            {
                throw new ParserException("MINUS ERROR");
            }
            //append minus sign to start
            localTokenStorage.setValueString("-" + localTokenStorage.getValueString());
            //get value of the value string
            float stringToFloat = parseFloat(localTokenStorage.getValueString());
            //handle if float or int
            if(localTokenStorage.getValueString().contains("."))
            {
                return new FloatNode(stringToFloat);
            }
            else
            {
                return new IntegerNode((int)stringToFloat);
            }
        }
        //deal with unary plus
        else if ((localTokenStorage = matchAndRemove(descriptor.PLUS)) != null)
        {
            //looks for number
            localTokenStorage = matchAndRemove(descriptor.Number);
            if(localTokenStorage == null)
            {
                throw new ParserException("PLUS ERROR");
            }
            //append unary plus
            localTokenStorage.setValueString("+" + localTokenStorage.getValueString());
            //get value of the value string
            float stringToFloat = parseFloat(localTokenStorage.getValueString());
            //handle if float or int
            if(localTokenStorage.getValueString().contains("."))
            {
                return new FloatNode(stringToFloat);
            }
            else
            {
                return new IntegerNode((int)stringToFloat);
            }
        }
        //deal with identifier
        else if((localTokenStorage = matchAndRemove(descriptor.IDENTIFIER)) != null)
        {
            return new VariableReferenceNode(localTokenStorage.getValueString());
        }
        else
        {
            //check if there's parenthesis
            localTokenStorage = matchAndRemove(descriptor.LEFTPARENTHESIS);
            if (localTokenStorage != null)
            {
                Node expressionToReturn = Expression();
                //check for close parenthesis
                localTokenStorage = matchAndRemove(descriptor.RIGHTPARENTHESIS);
                if(localTokenStorage == null)
                {
                    //no close parenthesis
                    throw new ParserException("INVALID MATH");
                }
                else
                {
                    return expressionToReturn;
                }
            }
            //if not a number or paren, throw an error
            throw new ParserException((parserList.get(0).getValueString() + parserList.get(1).getValueString()));
        }

    }
    public Token matchAndRemove(descriptor searchTerm)
    {
        Token localMatchStorage;
        //if enums are equal
        if(parserList.get(0).getLocalEnum() == searchTerm)
        {
            //remove the token from the list and return it
            localMatchStorage = parserList.get(0);
            parserList.remove(0);
            return localMatchStorage;
        }
        else
        {
            //return nothing
            return null;
        }
    }
    //Helper function to check the next value Without removing a token
    public boolean checkNext(descriptor searchTerm)
    {
        if (parserList.size() < 2)
        {
            return false;
        }
        else
        {
            if(parserList.get(0).getLocalEnum() == searchTerm)
            {
                return true;
            }
            else
            {
                return false;
            }
        }

    }
    public boolean checkOneAhead(descriptor searchTerm)
    {
        if(parserList.get(1).getLocalEnum() == searchTerm)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}