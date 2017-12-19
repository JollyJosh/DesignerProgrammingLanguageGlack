package me.jjcollins2.glack;

public enum LexemeType {

    EOF("EOF"),
    INCLUDE("INCLUDE"),
    NULL("NULL"),
    OPAREN("("),
    CPAREN(")"),
    COMMA(","),
    SEMICOL(";"),
    OBRACKET("["),
    CBRACKET("]"),
    DEFINE("define"),
    LAMBDA("lambda"),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    OCBRACKET("{"),
    CCBRACKET("}"),
    INTEGER("int"),
    STRING("string"),
    ID("id"),
    TRUE("true"),
    FALSE("false"),
    PLUS("+"),
    MINUS("-"),
    POWER("^"),
    MOD("%"),
    MULTIPLY("*"),
    DIVIDE("/"),
    ASSIGN("="),
    EQUALS("=="),
    LT("<"),
    GT(">"),
    LTE("<="),
    GTE(">="),
    NE("!="),
    DOT("."),
    AND("&&"),
    OR("||"),
    STATEMENTLIST("statementList"),
    FUNCTIONCALL("functionCall"),
    FUNCTIONDEF("functionDef"),
    GLUE("glue"),
    PARAMLIST("paramList"),
    ARGSLIST("argsList"),
    CODEBLOCK("codeBlock"),
    ENVIRONMENT("env"),
    BUILTIN("builtIn"),
    ARRAYACCESS("arrayAccess"),
    ARRAY("array"),
    CLOSURE("closure");



    private String type;

    LexemeType(String type) {
        this.type = type;
    }

    public String toString()    {
        return type;
    }
}
