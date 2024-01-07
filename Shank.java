/*
Max Solomon
ICSI311 Assignment 01
Submission date 9/4/2022
The purpose of this program is to read a text document passed in via arguments and turn the characters into appropriate tokens
valid input is characters in the form of a mathematical operation with the operators +,-,/,* allowed
termination states of the program are as follows:
Invalid number of arguments
Invalid file
Invalid character
All characters successfully processed and turned into tokens
 */

package icsi311;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.*;

public class Shank
{
    public static void main(String[] args) throws IOException, InvalidCharacterException, ParserException {
        //handle arguments
        if (args.length != 1) {
            System.out.println("Invalid number of args");
            System.exit(0);
        }


        ArrayList<ArrayList> tokenArrayListStorage = new ArrayList<>();
        List<String> fileLineStorage = Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8);
        Lexer shankLexer2 = new Lexer();


        //process all lines
        for (int i = 0; i < fileLineStorage.size(); i++) {
            tokenArrayListStorage.add(shankLexer2.lex(fileLineStorage.get(i)));
        }

        //combine all token lists into a single array list
        ArrayList<Token> combinedTokenList = new ArrayList<>();
        for (int i = 0; i < tokenArrayListStorage.size(); i++) {
            combinedTokenList.addAll(tokenArrayListStorage.get(i));
        }
       for (int i = 0 ; i < combinedTokenList.size(); i++)
        {
            //System.out.print(combinedTokenList.get(i).getValueString() + " ");
        }

        Parser shankParser = new Parser(combinedTokenList);
        ArrayList<Node> nodeStore = shankParser.Parse();
        //print nodes
        //for(int i = 0; i < nodeStore.size(); i++)
        //{
            //System.out.println(nodeStore.get(i).toString());
        //}
        Interpreter shankInterpreter = new Interpreter();
        //generate function map with built-in functions included
        HashMap<String,CallableNode> functionMap = new HashMap<>();
        write writeFunction = new write();
        read readFunction = new read();
        getRandom getRandomFunction = new getRandom();
        realToInteger realToIntegerFunction = new realToInteger();
        integerToReal integerToRealFunction = new integerToReal();
        left leftFunction = new left();
        right rightFunction = new right();
        subString subStringFunction = new subString();
        squareRoot squareRootFunction = new squareRoot();
        functionMap.put("write",writeFunction);
        functionMap.put("read",readFunction);
        functionMap.put("getRandom",getRandomFunction);
        functionMap.put("realToInteger",realToIntegerFunction);
        functionMap.put("integerToReal", integerToRealFunction);
        functionMap.put("squareRoot",squareRootFunction);
        functionMap.put("left",leftFunction);
        functionMap.put("right",rightFunction);
        functionMap.put("subString", subStringFunction);
        ArrayList<FunctionNode> functionNodeArrayList = new ArrayList<>();
        for(int i = 0; i < nodeStore.size(); i++)
        {
            if (nodeStore.get(i) instanceof FunctionNode)
            {
                String functionName = ((FunctionNode) nodeStore.get(i)).getName();
                functionMap.put(functionName,(FunctionNode)nodeStore.get(i));
                functionNodeArrayList.add((FunctionNode) nodeStore.get(i));
            }
        }
        SemanticAnalysis.CheckAssignments(functionNodeArrayList);
        //set the interpreter function map
        Interpreter.functionMap = functionMap;
        //find start call
        for(int i = 0; i < nodeStore.size(); i++)
        {
            if (nodeStore.get(i) instanceof FunctionNode)
            {
                if(((String)((FunctionNode)nodeStore.get(i)).getName()).equals("start"))
                {
                    //interpret start function
                    Interpreter.InterpretFunction((FunctionNode) nodeStore.get(i),new ArrayList<InterpreterDataType>());
                }
            }
        }

    }

}
