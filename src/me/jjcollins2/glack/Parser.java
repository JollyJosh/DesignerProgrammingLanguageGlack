package me.jjcollins2.glack;

public class Parser {

    public static void main(String[] params)    {
        //Test Parser class

        Parser parser = new Parser(params[0]);


    }

    private Lexer lexer;

    public Parser(String fileName) {

        lexer = new Lexer(fileName);

    }

    private Lexeme pending;

    public Lexeme parse()   {

        pending = lexer.lex();

        return program();

    }

    public Lexeme match(LexemeType type) {

        if(check(type)) {
            Lexeme temp = pending;
            pending = lexer.lex();
            return temp;
        }
        else    {
            throw new RuntimeException("Line Number: " + lexer.getLineNumber() + " - Match failed. Expected type " + type.toString() + " got type " + pending.getType().toString());
        }

    }

    public boolean check(LexemeType type)   {

        return (type == pending.getType());
    }

    private Lexeme program()    {

        Lexeme program = optStatementList();
        match(LexemeType.EOF);
        return program;
    }

    private Lexeme optStatementList()   {

        if(statementPending())  {
            Lexeme tree = new Lexeme(LexemeType.STATEMENTLIST, statement(), optStatementList());
            return tree;
        }
        else    {
            return null;
        }
    }

    private Lexeme statement()  {

        if(whilePending())  {
            return whileDef();
        }
        if(funcDefPending())    {
            return funcDef();
        }
        else if(expr0Pending()){
            Lexeme result = expr0();
            match(LexemeType.SEMICOL);
            return result;
        }
        else if(ifElsePending())    {
            return ifElse();
        }
        else    {
            return null;
        }
        //Repeat for each condition
    }

    private Lexeme ifElse() {
        Lexeme head = new Lexeme(LexemeType.IF);
        Lexeme glue = new Lexeme(LexemeType.GLUE);
        match(LexemeType.IF);
        match(LexemeType.OPAREN);
        Lexeme leftExpr = expr0();
        match(LexemeType.CPAREN);
        Lexeme codeBlockIf = codeBlock();
        match(LexemeType.ELSE);
        Lexeme codeBlockElse = codeBlock();

        glue.setLeft(codeBlockIf);
        glue.setRight(codeBlockElse);
        head.setRight(glue);
        head.setLeft(leftExpr);

        return head;
    }

    private Lexeme whileDef()   {
       Lexeme whileHead = match(LexemeType.WHILE);
       match(LexemeType.OPAREN);
       Lexeme expr = expr0();
       match(LexemeType.CPAREN);
       Lexeme block = codeBlock();
       whileHead.setLeft(expr);
       whileHead.setRight(block);
       return whileHead;
    }

    private Lexeme funcDef()    {
        //No need to check because I already known I checked define

        //Consume the define
        match(LexemeType.DEFINE);
        //Store the ID
        Lexeme id = match(LexemeType.ID);
        //Consume the oparens
        match(LexemeType.OPAREN);
        Lexeme optParams = optParamList();
        //Consume the cparens
        match(LexemeType.CPAREN);
        Lexeme codeBlock = codeBlock();

        Lexeme tree = new Lexeme(LexemeType.FUNCTIONDEF, id,
                new Lexeme(LexemeType.GLUE, optParams, codeBlock));

        return tree;
    }

    private Lexeme lambda() {

        match(LexemeType.LAMBDA);
        match(LexemeType.OPAREN);
        Lexeme optParams = optParamList();
        match(LexemeType.CPAREN);
        Lexeme codeBlock = codeBlock();

        Lexeme tree = new Lexeme(LexemeType.LAMBDA, null,
                new Lexeme(LexemeType.GLUE, optParams, codeBlock));

        return tree;
    }

    private Lexeme unary()  {

        if(idPending() || funcCallPending())    {
            Lexeme id = match(LexemeType.ID);
            if(check(LexemeType.OBRACKET))  {
                //Must be an id obracket expr cbracket
                match(LexemeType.OBRACKET);
                Lexeme expr = expr0();
                match(LexemeType.CBRACKET);
                Lexeme arrayAccess = new Lexeme(LexemeType.ARRAYACCESS, id, expr);
                return arrayAccess;
            }
            else if(check(LexemeType.OPAREN))    {
                //Must be a function call
                //Can tell from id then open parans
                match(LexemeType.OPAREN);
                Lexeme args = optArgList();
                match(LexemeType.CPAREN);
                Lexeme funcCall = new Lexeme(LexemeType.FUNCTIONCALL, id, args);
                return funcCall;
            }

            else {
                return id;
            }
        }

        if(stringPending()) {
            return match(LexemeType.STRING);
        }

        else if(check(LexemeType.NULL))  {
            return match(LexemeType.NULL);
        }

        else if(check(LexemeType.INTEGER))   {
            return match(LexemeType.INTEGER);
        }

        else if(check(LexemeType.TRUE)) {
            return match(LexemeType.TRUE);
        }

        else if(check(LexemeType.FALSE))    {
            return match(LexemeType.FALSE);
        }

        else if(lambdaPending())    {
            return lambda();
        }

        return null;
    }

    private Lexeme expr5()  {

        Lexeme u = unary();
        if(op5Pending())    {
            Lexeme op = match(LexemeType.DOT);
            Lexeme ex = expr5();
            op.setLeft(u);
            op.setRight(ex);
            return op;
        }
        return u;
    }

    private Lexeme expr4()  {
        Lexeme ex5 = expr5();
        if(op4Pending())    {
            Lexeme op = null;
            if(check(LexemeType.POWER)) {
                op = match(LexemeType.POWER);
            }
            else if (check(LexemeType.MOD)) {
                op = match(LexemeType.MOD);
            }
            Lexeme ex4 = expr4();
            op.setLeft(ex5);
            op.setRight(ex4);
            return op;
        }
        return ex5;
    }

    private Lexeme expr3()  {
        Lexeme ex4 = expr4();
        if(op3Pending())    {
            Lexeme op = null;
            if(check(LexemeType.MULTIPLY))  {
                op = match(LexemeType.MULTIPLY);
            }
            else if(check(LexemeType.DIVIDE))   {
                op = match(LexemeType.DIVIDE);
            }
            Lexeme ex3 = expr3();
            op.setLeft(ex4);
            op.setRight(ex3);
            return op;
        }
        return ex4;
    }

    private Lexeme expr2()  {
        Lexeme ex3 = expr3();
        if(op2Pending())    {
            Lexeme op = null;
            if(check(LexemeType.PLUS))  {
                op = match(LexemeType.PLUS);
            }
            else if(check(LexemeType.MINUS))   {
                op = match(LexemeType.MINUS);
            }
            Lexeme ex2 = expr2();
            op.setLeft(ex3);
            op.setRight(ex2);
            return op;
        }
        return ex3;
    }

    private Lexeme expr1()  {
        Lexeme ex2 = expr2();
        if(op1Pending())    {
            Lexeme op = null;
            if(check(LexemeType.LT))    {
                op = match(LexemeType.LT);
            }
            else if(check(LexemeType.GT))   {
                op = match(LexemeType.GT);
            }
            else if(check(LexemeType.LTE))  {
                op = match(LexemeType.LTE);
            }
            else if(check(LexemeType.GTE))  {
                op = match(LexemeType.GTE);
            }
            else if(check(LexemeType.EQUALS))   {
                op = match(LexemeType.EQUALS);
            }
            else if(check(LexemeType.NE))   {
                op = match(LexemeType.NE);
            }
            Lexeme ex1 = expr1();

            op.setLeft(ex2);
            op.setRight(ex1);
            return op;
        }
        return ex2;
    }

    private Lexeme expr0()  {
        Lexeme ex1 = expr1();
        if(op0Pending())    {
            Lexeme op = match(LexemeType.ASSIGN);
            Lexeme ex0 = expr0();
            op.setLeft(ex1);
            op.setRight(ex0);
            return op;
        }
        return ex1;
    }

    private Lexeme codeBlock()  {
        match(LexemeType.OCBRACKET);
        Lexeme tree = optStatementList();
        match(LexemeType.CCBRACKET);

        return tree;
    }

    private Lexeme optArgList() {

        if(argListPending())    {
            return argList();
        }
        else {
            return null;
        }
    }

    private boolean argListPending()    {
        return expr0Pending();
    }

    private Lexeme argList()    {

        Lexeme right = null;

        Lexeme id = expr0();
        if(check(LexemeType.COMMA)) {
            match(LexemeType.COMMA);
            right = argList();
        }
        Lexeme tree = new Lexeme(LexemeType.ARGSLIST, id, right);
        return tree;
    }

    private Lexeme optParamList()  {

        if(paramListPending())  {
            return paramList();
        }
        else  {
            return null;
        }
    }

    private Lexeme paramList()  {

        Lexeme right = null;

        Lexeme id = match(LexemeType.ID);
        if(check(LexemeType.COMMA)) {
            match(LexemeType.COMMA);
            right = paramList();

        }
        Lexeme tree = new Lexeme(LexemeType.PARAMLIST, id, right);
        return tree;
    }

    private boolean unaryPending()  { return check(LexemeType.INTEGER) || check(LexemeType.STRING)
                                             || check(LexemeType.ID) || check(LexemeType.TRUE) || check(LexemeType.FALSE)
                                             || funcCallPending() || lambdaPending() || check(LexemeType.NULL); }

    private boolean paramListPending()  {
        return (idPending());
    }

    private boolean statementPending()   {

        return (funcDefPending() || codeBlockPending() || ifElsePending() || whilePending() || expr0Pending());
    }

    private boolean funcDefPending()    {
        return check(LexemeType.DEFINE);
    }

    private boolean codeBlockPending()  {
        return check(LexemeType.OCBRACKET);
    }

    private boolean ifElsePending() {
        return check(LexemeType.IF);
    }

    private boolean idPending() {
        return check(LexemeType.ID);
    }

    private boolean stringPending() {
        return check(LexemeType.STRING);
    }


    private boolean intPending()    {
        return check(LexemeType.INTEGER);
    }

    private boolean truePending()   {
        return check(LexemeType.TRUE);
    }

    private boolean falsePending()  {
        return check(LexemeType.FALSE);
    }

    private boolean funcCallPending()   {
        return check(LexemeType.ID);
    }

    private boolean op0Pending()    { return check(LexemeType.ASSIGN); }

    private boolean op1Pending() {
        return check(LexemeType.LT) || check(LexemeType.GT)
                || check(LexemeType.LTE) || check(LexemeType.GTE) ||
                check(LexemeType.EQUALS) || check(LexemeType.NE);
    }

    private boolean op2Pending()    {
        return check(LexemeType.PLUS) || check(LexemeType.MINUS);
    }

    private boolean op3Pending()    {
        return check(LexemeType.MULTIPLY) || check(LexemeType.DIVIDE);
    }

    private boolean op4Pending()    {
        return check(LexemeType.POWER) || check(LexemeType.MOD);
    }

    private boolean op5Pending()    {
        return check(LexemeType.DOT);
    }

    private boolean expr5Pending()  {
        return unaryPending();
    }

    private boolean expr4Pending()  {
        return expr5Pending();
    }

    private boolean expr3Pending()  {
        return expr4Pending();
    }

    private boolean expr2Pending()  {
        return expr3Pending();
    }

    private boolean expr1Pending()  {
        return expr2Pending();
    }

    private boolean expr0Pending()  {
        return expr1Pending();
    }

    private boolean whilePending()  {
        return check(LexemeType.WHILE);
    }

    private boolean lambdaPending() {
        return check(LexemeType.LAMBDA);
    }

}

