package me.jjcollins2.glack;

import java.io.FileReader;
import java.io.PushbackReader;
import java.util.Arrays;
import java.util.List;

public class Lexer {
    private List<Lexeme> program;
    private int lineNumber = 1;

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public static void main(String[] params)    {
        // Use this to print every time a lexeme is created
        // to test the value and retrievals

        Lexer file = new Lexer(params[0]);

    }


    public Lexeme lex() {
        //Look at Lusth's notes, and use Java Pushback reader class
        //https://docs.oracle.com/javase/7/docs/api/java/io/PushbackReader.html

//        if(counter > program.size() - 1)
//            return new Lexeme(LexemeType.EOF);
//        Lexeme temp = program.get(counter);
//        counter++;
//        return temp;
        char ch, red;
        int check;

        try {
            skipWhiteSpace();
            check = reader.read();
            if(check == -1)    {
                //Done reading file.
               return new Lexeme(LexemeType.EOF);
            }
            ch = (char)check;
            switch (ch) {

                case '(': return new Lexeme(LexemeType.OPAREN);
                case ')': return new Lexeme(LexemeType.CPAREN);
                case ',': return new Lexeme(LexemeType.COMMA);
                case ';': return new Lexeme(LexemeType.SEMICOL);
                case '[': return new Lexeme(LexemeType.OBRACKET);
                case ']': return new Lexeme(LexemeType.CBRACKET);
                case '{': return new Lexeme(LexemeType.OCBRACKET);
                case '}': return new Lexeme(LexemeType.CCBRACKET);
                case '+': return new Lexeme(LexemeType.PLUS);
                case '-':
                    red = (char)reader.read();
                    if(isDigit(red))    {
                        reader.unread(red);
                        return lexNumber(true);
                    }
                    else    {
                        reader.unread(red);
                        return new Lexeme(LexemeType.MINUS);
                    }
                case '^': return new Lexeme(LexemeType.POWER);
                case '%': return new Lexeme(LexemeType.MOD);
                case '*': return new Lexeme(LexemeType.MULTIPLY);
                case '/': return new Lexeme(LexemeType.DIVIDE);
                case '=':
                    //Possibly "==" or "="
                    red = (char)reader.read();
                    if(red == '=')    {
                        //Must be "=="
                        return new Lexeme(LexemeType.EQUALS);
                    }
                    else {
                        reader.unread(red);
                        return new Lexeme(LexemeType.ASSIGN);
                    }
                case '<':
                    //Follow implementation for all of the following w 2 chars
                    //and 1 chars do as before
                    red = (char)reader.read();
                    if(red == '=')    {
                        return new Lexeme(LexemeType.LTE);
                    }
                    else    {
                        reader.unread(red);
                        return new Lexeme(LexemeType.LT);
                    }
                case '>':
                    //Possibly '>' or ">="
                    red = (char)reader.read();
                    if(red == '=')    {
                        //>=
                        return new Lexeme(LexemeType.GTE);
                    }
                    else    {
                        reader.unread(red);
                        //>
                        return new Lexeme(LexemeType.GT);
                    }
                case '!':
                    if(reader.read() == '=')    {
                        return new Lexeme(LexemeType.NE);
                    }
                case '.':
                    return new Lexeme(LexemeType.DOT);
                default:

                    if(isDigit(ch)) {
                        //REMEMBER NO NEGATIVES... SERIOUSLY EVER
                        reader.unread(ch);
                        return lexNumber(false);
                    }
                    else if(isLetter(ch))   {
                        reader.unread(ch);
                        return lexVarOrKeyword();
                    }
                    else if(ch == '\"') {
                        return lexString();
                    }
                    else    {
                        // throw new Exception("Unknown lex type! :(");
                    }

            }

        } catch (Exception ex)  {
            System.out.println("Skip White Space failed! :(");
        }

         return new Lexeme(LexemeType.EOF);
    }

    private PushbackReader reader;

    private Lexeme lexNumber(boolean isNeg) throws Exception  {
        char ch;
        StringBuilder builder = new StringBuilder();

        ch = (char)reader.read();
        while(isDigit(ch))  {
            builder.append(ch);
            ch = (char)reader.read();
        }

        reader.unread(ch);
        int var = new Integer(builder.toString());
        if(isNeg)   {
            var *= -1;
        }
        Lexeme num = new Lexeme(LexemeType.INTEGER, var);
        return num;
    }

    private Lexeme lexVarOrKeyword() throws Exception   {
        char ch;
        StringBuilder builder = new StringBuilder();

        ch = (char)reader.read();
        while(isDigit(ch) || isLetter(ch))  {
            builder.append(ch);
            ch = (char)reader.read();
        }

        reader.unread(ch);

        String key = builder.toString();



        //ADDING KEYWORDS
        if(key.equals(LexemeType.DEFINE.toString()))
            return new Lexeme(LexemeType.DEFINE);
        else if (key.equals(LexemeType.IF.toString()))
            return new Lexeme(LexemeType.IF);
        else if (key.equals(LexemeType.ELSE.toString()))
            return new Lexeme(LexemeType.ELSE);
        else if (key.equals(LexemeType.TRUE.toString()))
            return new Lexeme((LexemeType.TRUE));
        else if (key.equals(LexemeType.FALSE.toString()))
            return new Lexeme(LexemeType.FALSE);
        else if (key.equals(LexemeType.WHILE.toString()))
            return new Lexeme(LexemeType.WHILE);
        else if (key.equals(LexemeType.LAMBDA.toString()))
            return new Lexeme(LexemeType.LAMBDA);
        else if (key.equals(LexemeType.NULL.toString()))
            return new Lexeme(LexemeType.NULL);
        else {
            Lexeme l = new Lexeme(LexemeType.ID, key);
            l.setLineNumber(lineNumber);
            return l;
        }

    }

    private Lexeme lexString() throws Exception {
        char ch;
        StringBuilder builder = new StringBuilder();

        ch = (char)reader.read();
        while(ch != '\"')   {
            builder.append(ch);
            ch = (char)reader.read();
        }

        return new Lexeme(LexemeType.STRING, builder.toString());
    }

    public Lexer(String fileName) {

//        program = Arrays.asList(new Lexeme(LexemeType.DEFINE), new Lexeme(LexemeType.ID, "Hello"), new Lexeme(LexemeType.OPAREN),
//                new Lexeme(LexemeType.CPAREN), new Lexeme(LexemeType.OCBRACKET), new Lexeme(LexemeType.ID, "print"),
//                new Lexeme(LexemeType.OPAREN), new Lexeme(LexemeType.STRING, "Hello World"), new Lexeme(LexemeType.CPAREN),
//                new Lexeme(LexemeType.SEMICOL), new Lexeme(LexemeType.CCBRACKET),
//                new Lexeme(LexemeType.ID, "Hello"), new Lexeme(LexemeType.OPAREN), new Lexeme(LexemeType.CPAREN),
//                new Lexeme(LexemeType.SEMICOL), new Lexeme(LexemeType.EOF) );

            try{
                reader = new PushbackReader(new FileReader( fileName ));

            }catch (Exception ex)   {
                System.out.println("Unable to open file. :(");
            }

    }

    public void skipWhiteSpace()  throws Exception  {

            char ch;
            boolean isComment = false;

            ch = (char)reader.read();

            while(isWhiteSpace(ch) || isComment || ch == -1)   {
                if(ch == '\n')  {
                    isComment = false;
                    lineNumber++;
                }
                if(ch == '$')   {
                    isComment = true;
                }
                ch = (char)reader.read();
            }

            reader.unread(ch);

    }

    public boolean isWhiteSpace(char ch)   {
        //Comments are done line by line and started with dollar signs
        //BECAUSE I'M ALL ABOUT THAT MONEY!!!!!

        if(ch == ' ' || ch == '\t' || ch == '\n' || ch == '$')  {
            return true;
        }

        return false;
    }

    public boolean isDigit(char ch)  {
        if(ch >= '0' && ch <= '9') {
            return true;
        } else  {
            return false;
        }
    }

    public boolean isLetter(char ch)    {
        if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
            || ch == '_')   {
            return true;
        } else  {
            return false;
        }
    }

}

