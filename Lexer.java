package icsi311;

import java.util.ArrayList;
import java.util.HashMap;

public class Lexer
{

    String buffer = "";
    int state = 1;
    HashMap<String, descriptor> reserveWordMap = new HashMap<String, descriptor>();

    public ArrayList<Token> lex(String toParse) throws InvalidCharacterException
    {
        //add reserved words to hashMap
        reserveWordMap.put("define", descriptor.DEFINE);
        reserveWordMap.put("real", descriptor.REAL);
        reserveWordMap.put("begin", descriptor.BEGIN);
        reserveWordMap.put("end", descriptor.END);
        reserveWordMap.put("constants", descriptor.CONSTANTS);
        reserveWordMap.put("variables", descriptor.VARIABLES);
        reserveWordMap.put("integer", descriptor.INTEGER);
        reserveWordMap.put("if",descriptor.IF);
        reserveWordMap.put("then",descriptor.THEN);
        reserveWordMap.put("else",descriptor.ELSE);
        reserveWordMap.put("elsif",descriptor.ELSIF);
        reserveWordMap.put("for",descriptor.FOR);
        reserveWordMap.put("from",descriptor.FROM);
        reserveWordMap.put("to",descriptor.TO);
        reserveWordMap.put("while",descriptor.WHILE);
        reserveWordMap.put("repeat",descriptor.REPEAT);
        reserveWordMap.put("until",descriptor.UNTIL);
        reserveWordMap.put("mod",descriptor.MOD);
        reserveWordMap.put("var",descriptor.VAR);
        reserveWordMap.put("true",descriptor.TRUE);
        reserveWordMap.put("false",descriptor.FALSE);
        reserveWordMap.put("boolean",descriptor.BOOLEANTYPE);
        reserveWordMap.put("character",descriptor.CHARACTERTYPE);
        reserveWordMap.put("string",descriptor.STRINGTYPE);
        //add null character to end of string so states that loop into themselves can detect end of string
        toParse += '\0';
        ArrayList toReturn = new ArrayList();
        //parse all tokens
        for (int i = 0; i < toParse.length(); i++)
        {
            switch (state)
            {
                //checking if word or number
                case (1):
                {
                    if ((toParse.charAt(i) >= 'a' && toParse.charAt(i) <= 'z') || ((toParse.charAt(i) >= 'A' && toParse.charAt(i) <= 'Z')))
                    {
                        buffer += toParse.charAt(i);
                        state = 2;
                    }
                    else if (toParse.charAt(i) >= '0' && toParse.charAt(i) <= '9')
                    {
                        buffer += toParse.charAt(i);
                        state = 4;
                    }
                    else if (toParse.charAt(i) == ' ' || toParse.charAt(i) == '\t')
                    {
                        state = 1;
                    }
                    else if (toParse.charAt(i) == ';' || toParse.charAt(i) == '=' || toParse.charAt(i) == ',' || toParse.charAt(i) == ')')
                    {
                        if(toParse.charAt(i) == ';')
                        {
                            toReturn.add(new Token(descriptor.SEMICOLON,";"));
                        }
                        else if (toParse.charAt(i) == '=')
                        {
                            toReturn.add(new Token(descriptor.EQUAL,"="));
                        }
                        else if (toParse.charAt(i) == ',')
                        {
                            toReturn.add(new Token(descriptor.COMMA,","));
                        }
                        else if (toParse.charAt(i) == ')')
                        {
                            toReturn.add(new Token(descriptor.RIGHTPARENTHESIS,")"));
                        }
                        state = 1;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) == '(')
                    {
                        state = 6;
                    }
                    else if(toParse.charAt(i) == '+' || toParse.charAt(i) == '-' || toParse.charAt(i) == '*' || toParse.charAt(i) == '/')
                    {
                        state = 1;
                        buffer = "";
                        if(toParse.charAt(i) == '+')
                        {
                            toReturn.add(new Token(descriptor.PLUS,"+"));
                        }
                        else if(toParse.charAt(i) == '-')
                        {
                            toReturn.add(new Token(descriptor.MINUS,"-"));
                        }
                        else if (toParse.charAt(i) == '/')
                        {
                            toReturn.add(new Token(descriptor.DIVIDE,"/"));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.TIMES,"*"));
                        }
                    }
                    else if(toParse.charAt(i) == '\0')
                    {
                        continue;
                    }
                    else if(toParse.charAt(i) == ':')
                    {
                        state = 3;
                    }
                    else if(toParse.charAt(i) == '.')
                    {
                        buffer += '.';
                        state = 5;
                    }
                    else if(toParse.charAt(i) == '<')
                    {
                        state = 9;
                    }
                    else if(toParse.charAt(i) == '>')
                    {
                        state = 10;
                    }
                    else if(toParse.charAt(i) == '"')
                    {
                        state = 11;
                    }
                    else if(toParse.charAt(i) == '\'')
                    {
                        state = 12;
                    }
                    else
                    {
                        throw new InvalidCharacterException("Invalid Char In state 1");
                    }
                    break;
                }
                //processing word state
                case (2):
                {
                    //letters numbers and underscores loop back to the state
                    if ((toParse.charAt(i) >= 'a' && toParse.charAt(i) <= 'z') || ((toParse.charAt(i) >= 'A' && toParse.charAt(i) <= 'Z')) || ((toParse.charAt(i) >= '0' && toParse.charAt(i) <= '9')) || toParse.charAt(i) == '_') {
                        buffer += toParse.charAt(i);
                        state = 2;
                    }
                    else if(toParse.charAt(i) == '(')
                    {
                        descriptor hashReturnStorage = reserveWordMap.get(buffer);
                        if (hashReturnStorage != null)
                        {
                            toReturn.add(new Token(hashReturnStorage, buffer));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.IDENTIFIER, buffer));
                        }
                        buffer = "";
                        state = 6;
                    }
                    else if (toParse.charAt(i) == ' '|| toParse.charAt(i) == '\0' || toParse.charAt(i) == '\t')
                    {
                        descriptor hashReturnStorage = reserveWordMap.get(buffer);
                        if (hashReturnStorage != null)
                        {
                            toReturn.add(new Token(hashReturnStorage, buffer));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.IDENTIFIER, buffer));
                        }
                        buffer = "";
                        state = 1;
                    }
                    else if(toParse.charAt(i) == '<')
                    {
                        descriptor hashReturnStorage = reserveWordMap.get(buffer);
                        if (hashReturnStorage != null)
                        {
                            toReturn.add(new Token(hashReturnStorage, buffer));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.IDENTIFIER, buffer));
                        }
                        buffer = "";
                        state = 9;
                    }
                    else if(toParse.charAt(i) == '>')
                    {
                        descriptor hashReturnStorage = reserveWordMap.get(buffer);
                        if (hashReturnStorage != null)
                        {
                            toReturn.add(new Token(hashReturnStorage, buffer));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.IDENTIFIER, buffer));
                        }
                        buffer = "";
                        state = 10;
                    }
                    else if (toParse.charAt(i) == ';' || toParse.charAt(i) == '=' || toParse.charAt(i) == ',' || toParse.charAt(i) == ')') {
                        descriptor hashReturnStorage = reserveWordMap.get(buffer);
                        if (hashReturnStorage != null)
                        {
                            toReturn.add(new Token(hashReturnStorage, buffer));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.IDENTIFIER, buffer));
                        }
                        buffer = "";
                        state = 1;
                        if (toParse.charAt(i) == ';')
                        {
                            toReturn.add(new Token(descriptor.SEMICOLON, ";"));
                        }
                        else if (toParse.charAt(i) == '=')
                        {
                            toReturn.add(new Token(descriptor.EQUAL, "="));
                        }
                        else if (toParse.charAt(i) == ',')
                        {
                            toReturn.add(new Token(descriptor.COMMA, ","));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.RIGHTPARENTHESIS, ")"));
                        }
                    }
                    else if (toParse.charAt(i) == ':')
                    {
                        descriptor hashReturnStorage = reserveWordMap.get(buffer);
                        if (hashReturnStorage != null)
                        {
                            toReturn.add(new Token(hashReturnStorage, buffer));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.IDENTIFIER, buffer));
                        }
                        buffer = "";
                        state = 3;
                    }
                    else if(toParse.charAt(i) == '\0')
                    {
                        toReturn.add(new Token(descriptor.EndOfLine,"EndOfLine"));
                        state = 1;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) == '+')
                    {
                        descriptor hashReturnStorage = reserveWordMap.get(buffer);
                        if (hashReturnStorage != null)
                        {
                            toReturn.add(new Token(hashReturnStorage, buffer));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.IDENTIFIER, buffer));
                        }
                        toReturn.add(new Token(descriptor.PLUS,"+"));
                        buffer = "";
                        state = 1;
                    }
                    else if(toParse.charAt(i) == '-')
                    {
                        descriptor hashReturnStorage = reserveWordMap.get(buffer);
                        if (hashReturnStorage != null)
                        {
                            toReturn.add(new Token(hashReturnStorage, buffer));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.IDENTIFIER, buffer));
                        }
                        toReturn.add(new Token(descriptor.MINUS,"-"));
                        buffer = "";
                        state = 1;
                    }
                    else if(toParse.charAt(i) == '"')
                    {
                        descriptor hashReturnStorage = reserveWordMap.get(buffer);
                        if (hashReturnStorage != null)
                        {
                            toReturn.add(new Token(hashReturnStorage, buffer));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.IDENTIFIER, buffer));
                        }
                        buffer = "";
                        state = 11;
                    }
                    else if(toParse.charAt(i) == '\'')
                    {
                        descriptor hashReturnStorage = reserveWordMap.get(buffer);
                        if (hashReturnStorage != null)
                        {
                            toReturn.add(new Token(hashReturnStorage, buffer));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.IDENTIFIER, buffer));
                        }
                        buffer = "";
                        state = 12;
                    }
                    else
                    {
                        System.out.println();
                        System.out.println(toParse.charAt(i));
                        throw new InvalidCharacterException("Invalid character in state 2");
                    }
                    break;
                }
                //seeing if assignment token or colon token
                case (3):
                {
                    if (toParse.charAt(i) == '=')
                    {
                        toReturn.add(new Token(descriptor.ASSIGNMENT, ":="));
                        buffer = "";
                        state = 1;
                    }
                    else if(toParse.charAt(i) == '(')
                    {
                        toReturn.add(new Token(descriptor.COLON, ":"));
                        state = 6;
                    }
                    else
                    {
                        toReturn.add(new Token(descriptor.COLON, ":"));
                        if (toParse.charAt(i) == ';')
                        {
                            toReturn.add(new Token(descriptor.SEMICOLON, ";"));
                            buffer = "";
                            state = 1;
                        }
                        else if (toParse.charAt(i) == ';')
                        {
                            toReturn.add(new Token(descriptor.EQUAL, "="));
                            buffer = "";
                            state = 1;
                        }
                        else if (toParse.charAt(i) == ',')
                        {
                            toReturn.add(new Token(descriptor.COMMA, ","));
                            buffer = "";
                            state = 1;
                        }
                        else if (toParse.charAt(i) == '*' || toParse.charAt(i) == '/' || toParse.charAt(i) == '-' || toParse.charAt(i) == '+')
                        {
                            toReturn.add(new Token(descriptor.Number,buffer));
                            state = 1;
                            buffer = "";
                            if(toParse.charAt(i) == '+')
                            {
                                toReturn.add(new Token(descriptor.PLUS,"+"));
                            }
                            else if(toParse.charAt(i) == '-')
                            {
                                toReturn.add(new Token(descriptor.MINUS,"-"));
                            }
                            else if (toParse.charAt(i) == '/')
                            {
                                toReturn.add(new Token(descriptor.DIVIDE,"/"));
                            }
                            else
                            {
                                toReturn.add(new Token(descriptor.TIMES,"*"));
                            }
                        }
                        else if (toParse.charAt(i) == ' ' || toParse.charAt(i) == '\0' || toParse.charAt(i) == '\t')
                        {
                            buffer = "";
                            state = 1;
                        }
                        else if ((toParse.charAt(i) >= 'a' && toParse.charAt(i) <= 'z') || ((toParse.charAt(i) >= 'A' && toParse.charAt(i) <= 'Z')))
                        {
                            buffer = "";
                            buffer += toParse.charAt(i);
                            state = 2;
                        }
                        //need to add stuff for number handling
                        else if(toParse.charAt(i) >= '0' && toParse.charAt(i) <= '9')
                        {
                            buffer = "";
                            buffer += toParse.charAt(i);
                            state = 5;
                        }
                        else if(toParse.charAt(i) == ')')
                        {
                            toReturn.add(new Token(descriptor.RIGHTPARENTHESIS, ")"));
                        }
                        else if(toParse.charAt(i) == ':')
                        {
                            toReturn.add(new Token(descriptor.COLON,":"));
                        }
                        else if(toParse.charAt(i) == '<')
                        {
                            state = 9;
                        }
                        else if(toParse.charAt(i) == '>')
                        {
                            state = 10;
                        }
                        else if(toParse.charAt(i) == '"')
                        {
                            state = 11;
                        }
                        else if(toParse.charAt(i) == '\'')
                        {
                            state = 12;
                        }
                        else
                        {
                            throw new InvalidCharacterException("Invalid character in state 3");
                        }

                    }
                    break;
                }
                //number handling (no decimal)
                case (4):
                {
                    if (toParse.charAt(i) >= '0' && toParse.charAt(i) <= '9')
                    {
                        buffer += toParse.charAt(i);
                        state = 4;
                    }
                    else if(toParse.charAt(i) == ' ' || toParse.charAt(i) == '\0' || toParse.charAt(i) == '\t')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        buffer = "";
                        state = 1;
                    }
                    else if(toParse.charAt(i) == '(')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        buffer = "";
                        state = 6;
                    }
                    else if(toParse.charAt(i) == ')')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        toReturn.add(new Token(descriptor.RIGHTPARENTHESIS,")"));
                        buffer = "";
                        state = 1;
                    }
                    else if (toParse.charAt(i) == '*' || toParse.charAt(i) == '/' || toParse.charAt(i) == '-' || toParse.charAt(i) == '+')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        state = 1;
                        buffer = "";
                        if(toParse.charAt(i) == '+')
                        {
                            toReturn.add(new Token(descriptor.PLUS,"+"));
                        }
                        else if(toParse.charAt(i) == '-')
                        {
                            toReturn.add(new Token(descriptor.MINUS,"-"));
                        }
                        else if (toParse.charAt(i) == '/')
                        {
                            toReturn.add(new Token(descriptor.DIVIDE,"/"));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.TIMES,"*"));
                        }
                    }
                    else if(toParse.charAt(i) == '.')
                    {
                        buffer += '.';
                        state = 5;
                    }
                    else if(toParse.charAt(i) == '<')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        state = 9;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) == '>')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        state = 10;
                        buffer = "";
                    }
                    else if (toParse.charAt(i) >= 'a' && toParse.charAt(i) <= 'z' || toParse.charAt(i) >= 'A' && toParse.charAt(i) <= 'Z')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        buffer = "";
                        buffer += toParse.charAt(i);
                        state = 2;
                    }
                    else if(toParse.charAt(i) == ',')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        toReturn.add(new Token(descriptor.COMMA,","));
                        buffer = "";
                        state = 1;
                    }
                    else if(toParse.charAt(i) == '"')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        buffer = "";
                        state = 11;
                    }
                    else if(toParse.charAt(i) == '\'')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        buffer = "";
                        state = 12;
                    }
                    else
                    {
                        throw new InvalidCharacterException("Invalid character in state 4");
                    }
                break;
                }
                //number handling (decimal present)
                case(5):
                {
                    if(toParse.charAt(i) >= '0' && toParse.charAt(i) <= '9')
                    {
                        buffer += toParse.charAt(i);
                        state = 5;
                    }
                    else if (toParse.charAt(i) >= 'a' && toParse.charAt(i) <= 'z' || toParse.charAt(i) >= 'A' && toParse.charAt(i) <= 'Z')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        buffer = "";
                        buffer += toParse.charAt(i);
                        state = 2;
                    }
                    else if(toParse.charAt(i) == '(')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        buffer = "";
                        state = 6;
                    }
                    else if(toParse.charAt(i) == ' ' || toParse.charAt(i) == '\t')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        buffer = "";
                        state = 1;
                    }
                    else if (toParse.charAt(i) == ';' || toParse.charAt(i) == '=' || toParse.charAt(i) == ',' || toParse.charAt(i) == ')')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        if(toParse.charAt(i) == ';')
                        {
                            toReturn.add(new Token(descriptor.SEMICOLON,";"));
                        }
                        else if (toParse.charAt(i) == '=')
                        {
                            toReturn.add(new Token(descriptor.EQUAL,"="));
                        }
                        else if (toParse.charAt(i) == ',')
                        {
                            toReturn.add(new Token(descriptor.COMMA,","));
                        }
                        else if (toParse.charAt(i) == ')')
                        {
                            toReturn.add(new Token(descriptor.RIGHTPARENTHESIS,")"));
                        }
                        state = 1;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) == '<')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        state = 9;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) == '>')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        state = 10;
                        buffer = "";
                    }
                    else if (toParse.charAt(i) == '*' || toParse.charAt(i) == '/' || toParse.charAt(i) == '-' || toParse.charAt(i) == '+')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        buffer = "";
                        state = 1;
                        buffer = "";
                        if(toParse.charAt(i) == '+')
                        {
                            toReturn.add(new Token(descriptor.PLUS,"+"));
                        }
                        else if(toParse.charAt(i) == '-')
                        {
                            toReturn.add(new Token(descriptor.MINUS,"-"));
                        }
                        else if (toParse.charAt(i) == '/')
                        {
                            toReturn.add(new Token(descriptor.DIVIDE,"/"));
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.TIMES,"*"));
                        }
                    }
                    else if(toParse.charAt(i) == '"')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        buffer = "";
                        state = 11;
                    }
                    else if(toParse.charAt(i) == '\'')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        buffer = "";
                        state = 12;
                    }
                    else if(toParse.charAt(i) == '\0')
                    {
                        toReturn.add(new Token(descriptor.Number,buffer));
                        state = 1;
                        buffer = "";
                    }
                    else
                    {
                        throw new InvalidCharacterException("invalid character @ state 5");
                    }
                    break;
                }
                //see if comment indicator
                case(6):
                {
                    if(toParse.charAt(i) == '*')
                    {
                        buffer = "";
                        state  = 7;
                    }
                    else if (toParse.charAt(i) == '/' || toParse.charAt(i) == '-' || toParse.charAt(i) == '+')
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        state = 1;
                        buffer = "";
                        if(toParse.charAt(i) == '+')
                        {
                            toReturn.add(new Token(descriptor.PLUS,"+"));
                        }
                        else if(toParse.charAt(i) == '-')
                        {
                            toReturn.add(new Token(descriptor.MINUS,"-"));
                        }
                        else if (toParse.charAt(i) == '/')
                        {
                            toReturn.add(new Token(descriptor.DIVIDE,"/"));
                        }
                    }
                    else if(toParse.charAt(i) == '<')
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        state = 9;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) == '"')
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        state = 11;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) == '\'')
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        state = 12;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) == '>')
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        state = 10;
                        buffer = "";
                    }
                    else if (toParse.charAt(i) == ';' || toParse.charAt(i) == '=' || toParse.charAt(i) == ',' || toParse.charAt(i) == ')')
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        if(toParse.charAt(i) == ';')
                        {
                            toReturn.add(new Token(descriptor.SEMICOLON,";"));
                        }
                        else if (toParse.charAt(i) == '=')
                        {
                            toReturn.add(new Token(descriptor.EQUAL,"="));
                        }
                        else if (toParse.charAt(i) == ',')
                        {
                            toReturn.add(new Token(descriptor.COMMA,","));
                        }
                        else if (toParse.charAt(i) == ')')
                        {
                            toReturn.add(new Token(descriptor.RIGHTPARENTHESIS,")"));
                        }
                        state = 1;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) >= '0' && toParse.charAt(i) <= '9')
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        buffer = "";
                        buffer += toParse.charAt(i);
                        state = 4;
                    }
                    else if(toParse.charAt(i) == '.')
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        buffer = "";
                        buffer += toParse.charAt(i);
                        state = 5;
                    }
                    else if ((toParse.charAt(i) >= 'a' && toParse.charAt(i) <= 'z') || ((toParse.charAt(i) >= 'A' && toParse.charAt(i) <= 'Z')))
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        buffer += toParse.charAt(i);
                        state = 2;
                    }
                    else if(toParse.charAt(i) == '(')
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        buffer = "";
                        state = 1;
                    }
                    else if(toParse.charAt(i) == ')')
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        toReturn.add(new Token(descriptor.RIGHTPARENTHESIS,")"));
                        buffer = "";
                        state = 1;
                    }
                    else if(toParse.charAt(i) == ' ' || toParse.charAt(i) == '\t')
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        buffer = "";
                        state = 1;
                    }
                    else if(toParse.charAt(i) == '\0')
                    {
                        toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        state = 1;
                    }
                    else
                    {
                        throw new InvalidCharacterException("invalid character @ state 6");
                    }
                    break;
                }
                //looking for * (1st half of end of comment indicator)
                case(7):
                {
                    if (toParse.charAt(i) == '*')
                    {
                        state = 8;
                    }
                    else if (toParse.charAt(i) == '\0')
                    {
                        state = 8;
                        return toReturn;
                    }
                    else
                    {
                        state = 7;
                    }
                    break;
                }
                case(8):
                {
                    if (toParse.charAt(i) == ')')
                    {
                        state = 1;
                    }
                    else
                    {
                        state = 7;
                    }
                    break;
                }
                //'<' detected
                case(9):
                {
                    if(toParse.charAt(i) == '=')
                    {
                        toReturn.add(new Token(descriptor.LESSOREQUAL,"<="));
                        state  = 1;
                    }
                    else if(toParse.charAt(i) == '>')
                    {
                        toReturn.add(new Token(descriptor.NOTEQUAL,"<>"));
                        state = 1;
                    }
                    else if(toParse.charAt(i) == '\0' || toParse.charAt(i) == ' ' || toParse.charAt(i) == '\t')
                    {
                        toReturn.add(new Token(descriptor.LESS, "<"));
                        state = 1;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) >= '0' && toParse.charAt(i) <= '9')
                    {
                        toReturn.add(new Token(descriptor.LESS, "<"));
                        buffer = "";
                        buffer += toParse.charAt(i);
                        state = 4;
                    }
                    else if(toParse.charAt(i) == '.')
                    {
                        toReturn.add(new Token(descriptor.LESS, "<"));
                        buffer = "";
                        buffer += toParse.charAt(i);
                        state = 5;
                    }
                    else if ((toParse.charAt(i) >= 'a' && toParse.charAt(i) <= 'z') || ((toParse.charAt(i) >= 'A' && toParse.charAt(i) <= 'Z')))
                    {
                        toReturn.add(new Token(descriptor.LESS, "<"));
                        buffer += toParse.charAt(i);
                        state = 2;
                    }
                    else if (toParse.charAt(i) == ';' || toParse.charAt(i) == ':' || toParse.charAt(i) == ',' || toParse.charAt(i) == ')' || toParse.charAt(i) == '(' || toParse.charAt(i) == ',')
                    {
                        toReturn.add(new Token(descriptor.LESS, "<"));
                        if(toParse.charAt(i) == ';')
                        {
                            toReturn.add(new Token(descriptor.SEMICOLON,";"));
                        }
                        else if (toParse.charAt(i) == ':')
                        {
                            toReturn.add(new Token(descriptor.COLON,":"));
                        }
                        else if (toParse.charAt(i) == ',')
                        {
                            toReturn.add(new Token(descriptor.COMMA,","));
                        }
                        else if (toParse.charAt(i) == ')')
                        {
                            toReturn.add(new Token(descriptor.RIGHTPARENTHESIS,")"));
                        }
                        else if (toParse.charAt(i) == '(')
                        {
                            toReturn.add(new Token(descriptor.LEFTPARENTHESIS,"("));
                        }
                        else if(toParse.charAt(i) == ',')
                        {
                            toReturn.add(new Token(descriptor.COMMA));
                        }
                        state = 1;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) == '<')
                    {
                        toReturn.add(new Token(descriptor.LESS, "<"));
                        state = 9;
                    }
                    else if(toParse.charAt(i) == '"')
                    {
                        toReturn.add(new Token(descriptor.LESS, "<"));
                        buffer = "";
                        state = 11;
                    }
                    else if(toParse.charAt(i) == '\'')
                    {
                        toReturn.add(new Token(descriptor.LESS, "<"));
                        buffer = "";
                        state = 12;
                    }
                    else
                    {
                        throw new InvalidCharacterException("INVALID CHARACTER @ STATE 9");
                    }
                    break;
                }
                //> detected
                case(10):
                {
                    if(toParse.charAt(i) == '=')
                    {
                        toReturn.add(new Token(descriptor.GREATEROREQUAL,">="));
                        state = 1;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) == '<')
                    {
                        toReturn.add(new Token(descriptor.GREATER,">"));
                        state = 9;
                    }
                    else if(toParse.charAt(i) == ' ' || toParse.charAt(i) == '\0' || toParse.charAt(i) == '\t')
                    {
                        toReturn.add(new Token(descriptor.GREATER,">"));
                        state = 1;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) >= '0' && toParse.charAt(i) <= '9')
                    {
                        toReturn.add(new Token(descriptor.GREATER,">"));
                        buffer = "";
                        buffer += toParse.charAt(i);
                        state = 4;
                    }
                    else if(toParse.charAt(i) == '.')
                    {
                        toReturn.add(new Token(descriptor.GREATER,">"));
                        buffer = "";
                        buffer += toParse.charAt(i);
                        state = 5;
                    }
                    else if(toParse.charAt(i) >= 'a' && toParse.charAt(i) <= 'z' || toParse.charAt(i) >= 'A' && toParse.charAt(i) <= 'Z')
                    {
                        buffer = "";
                        buffer += toParse.charAt(i);
                        state = 2;
                    }
                    else if (toParse.charAt(i) == ';' || toParse.charAt(i) == ':' || toParse.charAt(i) == ',' || toParse.charAt(i) == ')' || toParse.charAt(i) == '(' || toParse.charAt(i) == ',')
                    {
                        toReturn.add(new Token(descriptor.GREATER, ">"));
                        if (toParse.charAt(i) == ';')
                        {
                            toReturn.add(new Token(descriptor.SEMICOLON, ";"));
                        }
                        else if (toParse.charAt(i) == ':')
                        {
                            toReturn.add(new Token(descriptor.COLON, ":"));
                        }
                        else if (toParse.charAt(i) == ',')
                        {
                            toReturn.add(new Token(descriptor.COMMA, ","));
                        }
                        else if (toParse.charAt(i) == ')')
                        {
                            toReturn.add(new Token(descriptor.RIGHTPARENTHESIS, ")"));
                        }
                        else if (toParse.charAt(i) == '(')
                        {
                            toReturn.add(new Token(descriptor.LEFTPARENTHESIS, "("));
                        }
                        else if (toParse.charAt(i) == ',')
                        {
                            toReturn.add(new Token(descriptor.COMMA));
                        }
                        state = 1;
                        buffer = "";
                    }
                    else if(toParse.charAt(i) == '>')
                    {
                        toReturn.add(new Token(descriptor.GREATER, ">"));
                        state = 10;
                    }
                    else if(toParse.charAt(i) == '"')
                    {
                        toReturn.add(new Token(descriptor.GREATER, ">"));
                        state = 11;
                    }
                    else if(toParse.charAt(i) == '\'')
                    {
                        toReturn.add(new Token(descriptor.GREATER, ">"));
                        state = 12;
                    }
                    else
                    {
                        throw new InvalidCharacterException("INVALID CHARACTER @ STATE 10");
                    }
                    break;
                }
                //dealing with string
                case(11):
                {
                    if (toParse.charAt(i) == '"')
                    {
                        toReturn.add(new Token(descriptor.STRING,buffer));
                        buffer = "";
                        state = 1;
                    }
                    else
                    {
                        buffer += toParse.charAt(i);
                    }
                    break;
                }
                //dealing with character
                case(12):
                {
                    if (toParse.charAt(i) == '\'')
                    {
                        if (buffer.length() != 1)
                        {
                            throw new InvalidCharacterException("TOO MANY CHARACTERS IN CHARACTER ASSIGNMENT");
                        }
                        else
                        {
                            toReturn.add(new Token(descriptor.CHARACTER,buffer));
                            buffer = "";
                            state = 1;
                        }
                    }
                    else
                    {
                        buffer += toParse.charAt(i);
                    }
                    break;
                }
            }
        }
        //tag on end of line token
        toReturn.add(new Token(descriptor.EndOfLine,"EndOfLine"));
        //flush buffer and reset state
        state = 1;
        buffer = "";
    return toReturn;
    }
}
