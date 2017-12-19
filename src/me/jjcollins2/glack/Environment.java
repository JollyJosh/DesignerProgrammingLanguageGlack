package me.jjcollins2.glack;

public class Environment {

    public static Lexeme extendEnvironment(Lexeme ids,Lexeme args,Lexeme parentEnv)    {
        Lexeme env = new Lexeme(LexemeType.ENVIRONMENT);
        while(ids != null)  {
            env.getIds().add((String)ids.getLeft().getValue());
            ids = ids.getRight();
        }
        while(args != null) {
            env.getValues().add(args.getLeft());
            args = args.getRight();
        }
        env.setParent(parentEnv);
        insertEnvironment(env, "this", env);
        return env;
    }

    public static Lexeme createEnvironment() {
        Lexeme env = extendEnvironment(null, null, null);
        Lexeme id = new Lexeme(LexemeType.ID, "print");
        Lexeme print = new Lexeme(LexemeType.BUILTIN);
        print.setFunction((args, environment) -> {
            if(args.getLeft() == null)   {
                System.out.println("null");
            }
            else {
                System.out.println(args.getLeft().getValue());
            }
            return args.getLeft();
        });
        insertEnvironment(env, id.getValue().toString(), print);
        id = new Lexeme(LexemeType.ID, "array");
        Lexeme array = new Lexeme(LexemeType.BUILTIN);
        array.setFunction((args, environment) -> {
            Lexeme retVal = new Lexeme(LexemeType.ARRAY);
            retVal.setArrayValues(new Lexeme[(Integer)args.getLeft().getValue()]);
            return retVal;
        });
        insertEnvironment(env, id.getValue().toString(), array);
        id = new Lexeme (LexemeType.INCLUDE, "include");
        Lexeme include = new Lexeme(LexemeType.BUILTIN);
        include.setFunction((args, environment) ->  {
            Evaluator e = new Evaluator();
            String file = (String)args.getLeft().getValue();
            Parser parser = new Parser(file);
            e.eval(parser.parse(), env);
            return env;
        });
        insertEnvironment(env, id.getValue().toString(), include);
        return env;
    }

    public static Lexeme lookupEnvironment(Lexeme env,String value)    {
        for(int x = 0; x < env.getIds().size(); x++)    {
            if(env.getIds().get(x).equals(value)) {
                return env.getValues().get(x);
            }
        }

        if(env.getParent() != null) {
            return lookupEnvironment(env.getParent(), value);
        }

        //Run time exception 2
        throw new RuntimeException("Undefined Variable " + value);
    }

    public static Lexeme insertEnvironment(Lexeme env,String id,Lexeme value)   {
        env.getIds().add(0, id);
        env.getValues().add(0, value);
        return value;
    }
}
