package me.jjcollins2.glack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.BiFunction;

public class Lexeme implements Comparable {

    //Building for trees
    private int lineNumber;

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    private LexemeType type;

    public void setType(LexemeType type) {
        this.type = type;
    }

    private Object value;
    private Lexeme[] arrayValues;

    public Lexeme[] getArrayValues() {
        return arrayValues;
    }

    public void setArrayValues(Lexeme[] arrayValues) {
        this.arrayValues = arrayValues;
    }

    private Lexeme left;
    private Lexeme right;

    //Building for environments
    private Lexeme parent;
    private ArrayList<String>  ids;
    private ArrayList<Lexeme> values;

    //For built ins
    private BiFunction<Lexeme, Lexeme, Lexeme> function;

    public BiFunction<Lexeme, Lexeme, Lexeme> getFunction() {
        return function;
    }

    public void setFunction(BiFunction<Lexeme, Lexeme, Lexeme> function) {
        this.function = function;
    }

    public Lexeme getParent() {
        return parent;
    }

    public void setParent(Lexeme parent) {
        this.parent = parent;
    }

    public ArrayList<String> getIds() {
        if(ids == null) {
            ids = new ArrayList<String>();
        }
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }

    public ArrayList<Lexeme> getValues() {
        if(values == null)  {
            values = new ArrayList<Lexeme>();
        }
        return values;
    }

    public void setValues(ArrayList<Lexeme> values) {
        this.values = values;
    }

    public Lexeme(LexemeType type, Lexeme left, Lexeme right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }

    public LexemeType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Lexeme getRight() {
        return right;
    }

    public void setRight(Lexeme right) {
        this.right = right;
    }

    public Lexeme getLeft() {

        return left;
    }

    public void setLeft(Lexeme left) {
        this.left = left;
    }

    public Lexeme(LexemeType type) {
        this.type = type;
    }

    public Lexeme(LexemeType type, Object value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public int compareTo(Object o) {
        Lexeme t = (Lexeme)o;

        //Figure out booleans
        if((this.getType() == LexemeType.TRUE && t.getType() == LexemeType.TRUE)
                || (this.getType() == LexemeType.FALSE && t.getType() == LexemeType.FALSE)) {
            //For the case of returning booleans, if they are both the same then this
            //function returns a one, else it returns a zero.
            return 0;
        }
        else if((this.getType() == LexemeType.TRUE && t.getType() == LexemeType.FALSE)
                || (this.getType() == LexemeType.FALSE && t.getType() == LexemeType.TRUE))  {
            return 1;
        }


        if(this.getType() == LexemeType.NULL && t.getType() == LexemeType.NULL) {
            return 0;
        }
        else if(this.getType() == LexemeType.NULL || t.getType() == LexemeType.NULL)    {
            return 1;
        }

        if(this.getType() != t.getType())   {
            throw new RuntimeException("Object of type " + this.getType() + " and " + t.getType() + " are not comparable.");
        }

        //Integer case
        if(this.getType() == LexemeType.INTEGER) {
            if (this.getValue() == t.getValue()) {
                return 0;
            } else if ((Integer) this.getValue() > (Integer) t.getValue()) {
                return 1;
            } else if ((Integer) this.getValue() < (Integer) t.getValue()) {
                return -1;
            }
        }

        //Strings case
        if(this.getType() == LexemeType.STRING) {
            if(Objects.equals(this.getValue().toString(), t.getValue().toString())) {
                return 0;
            }
            else    {
                return 1;
            }
        }

        return 0;
    }
}
