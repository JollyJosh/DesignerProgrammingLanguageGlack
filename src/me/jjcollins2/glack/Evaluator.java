package me.jjcollins2.glack;

public class Evaluator {

    public static void main(String[] args)    {
        String file = args[0];
        Evaluator e = new Evaluator();
        Parser parser = new Parser(file);

        try {
            e.eval(parser.parse(), Environment.createEnvironment());
        }
        catch(Exception ex)   {
            System.err.println(ex.getMessage());
        }


    }

    public Lexeme eval(Lexeme tree,Lexeme env) {

        switch (tree.getType()) {

            case INTEGER:
            case STRING:
            case TRUE:
            case FALSE:
            case NULL:
                return tree;
            case ID:
                return Environment.lookupEnvironment(env, (String)tree.getValue());
            case FUNCTIONDEF:
                return evalFuncDef(tree, env);
            case FUNCTIONCALL:
                return evalFuncCall(tree, env, env);
            case STATEMENTLIST:
                return evalStateList(tree, env);
            case ARRAYACCESS:
                return evalArrayAccess(tree, env);
            case ASSIGN:
                return evalAssign(tree, env);
            case LT:
                return evalLT(tree, env);
            case GT:
                return evalGT(tree, env);
            case LTE:
                return evalLTE(tree, env);
            case GTE:
                return evalGTE(tree, env);
            case EQUALS:
                return evalEquals(tree, env);
            case NE:
                return evalNE(tree, env);
            case PLUS:
                return evalPlus(tree, env);
            case MINUS:
                return evalMinus(tree, env);
            case MULTIPLY:
                return evalMultiply(tree, env);
            case DIVIDE:
                return evalDivide(tree, env);
            case POWER:
                return evalPower(tree, env);
            case MOD:
                return evalMod(tree, env);
            case DOT:
                return evalDot(tree, env);
            case WHILE:
                return evalWhile(tree, env);
            case LAMBDA:
                return evalLambda(tree, env);
            case IF:
                return evalIfElse(tree, env);
            default:
                return null;

        }
    }

    private Lexeme evalIfElse(Lexeme tree, Lexeme env) {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme exCodeBlock;
        if(leftExpr.getType() == LexemeType.TRUE)   {
            exCodeBlock = eval(tree.getRight().getLeft(), env);
        }
        else {
            if(tree.getRight().getRight() == null)  {
                return leftExpr;
            }
            else {
                exCodeBlock = eval(tree.getRight().getRight(), env);
            }
        }
        return exCodeBlock;
    }

    private Lexeme evalWhile(Lexeme tree, Lexeme env)   {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightBlock = null;
        while(leftExpr.getType() == LexemeType.TRUE) {
            rightBlock = eval(tree.getRight(), env);
            leftExpr = eval(tree.getLeft(), env);
        }
        return rightBlock;
    }

    private Lexeme evalNE(Lexeme tree, Lexeme env)  {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightExpr = eval(tree.getRight(), env);
        if(leftExpr.compareTo(rightExpr) != 0)  {
            return new Lexeme(LexemeType.TRUE);
        }
        else    {
            return new Lexeme(LexemeType.FALSE);
        }
    }

    private Lexeme evalEquals(Lexeme tree, Lexeme env)  {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightExpr = eval(tree.getRight(), env);
        if(leftExpr.compareTo(rightExpr) == 0)  {
            return new Lexeme(LexemeType.TRUE);
        }
        else {
            return new Lexeme(LexemeType.FALSE);
        }
    }

    private Lexeme evalLT(Lexeme tree, Lexeme env)  {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightExpr = eval(tree.getRight(), env);
        if(leftExpr.getType() == LexemeType.STRING || leftExpr.getType() == LexemeType.TRUE || leftExpr.getType() == LexemeType.FALSE
                || rightExpr.getType() == LexemeType.STRING || rightExpr.getType() == LexemeType.TRUE | rightExpr.getType() == LexemeType.FALSE)
        {
            throw new RuntimeException("Object of type " + leftExpr.getType() + " and " + rightExpr.getType() + " are not comparable with this inequality.");
        }
        if(leftExpr.compareTo(rightExpr) == -1)  {
            return new Lexeme(LexemeType.TRUE);
        }
        else {
            return new Lexeme(LexemeType.FALSE);
        }
    }

    private Lexeme evalLTE(Lexeme tree, Lexeme env)  {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightExpr = eval(tree.getRight(), env);
        if(leftExpr.getType() == LexemeType.STRING || leftExpr.getType() == LexemeType.TRUE || leftExpr.getType() == LexemeType.FALSE
                || rightExpr.getType() == LexemeType.STRING || rightExpr.getType() == LexemeType.TRUE | rightExpr.getType() == LexemeType.FALSE)
        {
            throw new RuntimeException("Object of type " + leftExpr.getType() + " and " + rightExpr.getType() + " are not comparable with this inequality.");        }
        if(leftExpr.compareTo(rightExpr) == -1 || leftExpr.compareTo(rightExpr) == 0)  {
            return new Lexeme(LexemeType.TRUE);
        }
        else {
            return new Lexeme(LexemeType.FALSE);
        }
    }

    private Lexeme evalGTE(Lexeme tree, Lexeme env)  {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightExpr = eval(tree.getRight(), env);
        if(leftExpr.getType() == LexemeType.STRING || leftExpr.getType() == LexemeType.TRUE || leftExpr.getType() == LexemeType.FALSE
                || rightExpr.getType() == LexemeType.STRING || rightExpr.getType() == LexemeType.TRUE | rightExpr.getType() == LexemeType.FALSE)
        {
            throw new RuntimeException("Object of type " + leftExpr.getType() + " and " + rightExpr.getType() + " are not comparable with this inequality.");        }
        if(leftExpr.compareTo(rightExpr) == 1 || leftExpr.compareTo(rightExpr) == 0)  {
            return new Lexeme(LexemeType.TRUE);
        }
        else {
            return new Lexeme(LexemeType.FALSE);
        }
    }

    private Lexeme evalGT(Lexeme tree, Lexeme env)  {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightExpr = eval(tree.getRight(), env);
        if(leftExpr.getType() == LexemeType.STRING || leftExpr.getType() == LexemeType.TRUE || leftExpr.getType() == LexemeType.FALSE
                || rightExpr.getType() == LexemeType.STRING || rightExpr.getType() == LexemeType.TRUE | rightExpr.getType() == LexemeType.FALSE)
        {
            throw new RuntimeException("Object of type " + leftExpr.getType() + " and " + rightExpr.getType() + " are not comparable with this inequality.");        }
        if(leftExpr.compareTo(rightExpr) == 1)  {
            return new Lexeme(LexemeType.TRUE);
        }
        else {
            return new Lexeme(LexemeType.FALSE);
        }
    }

    private Lexeme evalPlus(Lexeme tree, Lexeme env)    {
        //TODO implement string concatenation
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightExpr = eval(tree.getRight(), env);
        return new Lexeme(LexemeType.INTEGER, (Integer)leftExpr.getValue() + (Integer)rightExpr.getValue());
    }

    private Lexeme evalMinus(Lexeme tree, Lexeme env)   {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightExpr = eval(tree.getRight(), env);
        return new Lexeme(LexemeType.INTEGER, (Integer)leftExpr.getValue() - (Integer)rightExpr.getValue());
    }

    private Lexeme evalMultiply(Lexeme tree, Lexeme env)   {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightExpr = eval(tree.getRight(), env);
        return new Lexeme(LexemeType.INTEGER, (Integer)leftExpr.getValue() * (Integer)rightExpr.getValue());
    }

    private Lexeme evalDivide(Lexeme tree, Lexeme env)   {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightExpr = eval(tree.getRight(), env);
        return new Lexeme(LexemeType.INTEGER, (Integer)leftExpr.getValue() / (Integer)rightExpr.getValue());
    }

    private Lexeme evalPower(Lexeme tree, Lexeme env)   {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightExpr = eval(tree.getRight(), env);
        return new Lexeme(LexemeType.INTEGER, (int)Math.pow((Integer)leftExpr.getValue(), (Integer)rightExpr.getValue()));
    }

    private Lexeme evalMod(Lexeme tree, Lexeme env) {
        Lexeme leftExpr = eval(tree.getLeft(), env);
        Lexeme rightExpr = eval(tree.getRight(), env);
        return new Lexeme(LexemeType.INTEGER, (Integer)leftExpr.getValue() % (Integer)rightExpr.getValue());
    }

    private Lexeme evalAssign(Lexeme tree, Lexeme env)   {
        Lexeme expr = eval(tree.getRight(), env);
        if(tree.getLeft().getType() == LexemeType.ARRAYACCESS)  {
            Lexeme array = eval(tree.getLeft().getLeft(), env);
            Lexeme index = eval(tree.getLeft().getRight(), env);
            array.getArrayValues()[(Integer)index.getValue()] = expr;
        }
        else if(tree.getLeft().getType() == LexemeType.DOT) {
            Lexeme trackEnv = env;
            Lexeme curr = tree.getLeft();
            while(curr.getType() == LexemeType.DOT) {
                trackEnv = eval(curr.getLeft(), trackEnv);
                curr = curr.getRight();
            }
            Environment.insertEnvironment(trackEnv, (String)curr.getValue(),expr);
        }
        else if(tree.getLeft().getType() == LexemeType.ID)  {
            Lexeme id = tree.getLeft();
            Environment.insertEnvironment(env, (String)id.getValue(), expr);
        }
        return expr;
    }

    private Lexeme evalDot(Lexeme tree, Lexeme env) {
        Lexeme u = eval(tree.getLeft(), env);
        if(tree.getRight().getType() == LexemeType.FUNCTIONCALL)    {
            return evalFuncCall(tree, u, env);
        }
        else    {
            return eval(tree.getRight(), u);
        }

    }

    private Lexeme evalFuncDef(Lexeme tree, Lexeme env) {
        Lexeme closure = new Lexeme(LexemeType.CLOSURE, env, tree);
        Environment.insertEnvironment(env, (String)tree.getLeft().getValue(), closure);
        return closure;
    }

    private Lexeme evalLambda(Lexeme tree, Lexeme env)  {
        Lexeme closure = new Lexeme(LexemeType.CLOSURE, env, tree);
        return closure;
    }

    private Lexeme evalArgList(Lexeme tree,Lexeme env)  {

        Lexeme head = null, leftExpr, rightExpr;
        if(tree == null)    {
            return null;
        }
        else    {
            head = new Lexeme(LexemeType.ARGSLIST);
            leftExpr = eval(tree.getLeft(), env);
            head.setLeft(leftExpr);
            head.setRight(evalArgList(tree.getRight(),env));
        }

        return head;
    }

    private Lexeme evalFuncCall(Lexeme tree,Lexeme env, Lexeme argEnv)  {
        Lexeme closure = Environment.lookupEnvironment(env, (String)tree.getLeft().getValue());
        Lexeme newArgs = evalArgList(tree.getRight(), argEnv);
        if(closure.getType() == LexemeType.BUILTIN)     {
            return closure.getFunction().apply(newArgs, env);
        }
        else    {
            Lexeme newEnv = Environment.extendEnvironment(closure.getRight().getRight().getLeft(), newArgs, closure.getLeft());
            return eval(closure.getRight().getRight().getRight(), newEnv);
        }
    }

    private Lexeme evalStateList(Lexeme tree, Lexeme env)   {
        Lexeme temp = null;
        while(tree != null) {
            temp = eval(tree.getLeft(), env);
            tree = tree.getRight();
        }

        return temp;
    }

    private Lexeme evalArrayAccess(Lexeme tree, Lexeme env) {
        Lexeme array = eval(tree.getLeft(), env);
        if(array.getType() != LexemeType.ARRAY)  {
            //Run time error 1
            throw new RuntimeException("Look up of " + tree.getLeft().getValue() + " is not array.");
        }

        Lexeme index = eval(tree.getRight(), env);
        return array.getArrayValues()[(Integer)index.getValue()];
    }
}
