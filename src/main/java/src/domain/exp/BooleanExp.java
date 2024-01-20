package src.domain.exp;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.type.IntType;
import src.domain.type.Type;
import src.domain.value.BoolValue;
import src.domain.value.Value;

public class BooleanExp implements Exp {
    private Exp exp_left;
    private Exp exp_right;
    private String comparisonOperator;

    public BooleanExp(Exp exp_left, Exp exp_right, String comparisonOperator){
        this.comparisonOperator = comparisonOperator;
        this.exp_left = exp_left;
        this.exp_right = exp_right;
    }

    public Exp getExp_left() {
        return exp_left;
    }

    public Exp getExp_right() {
        return exp_right;
    }

    public String getComparisonOperator() {
        return comparisonOperator;
    }

    public void setComparisonOperator(String comparisonOperator) {
        this.comparisonOperator = comparisonOperator;
    }

    public void setExp_left(Exp exp_left) {
        this.exp_left = exp_left;
    }

    public void setExp_right(Exp exp_right) {
        this.exp_right = exp_right;
    }

    @Override
    public String toString() {
        return "( " + exp_left.toString() + " " + comparisonOperator + " " + exp_right.toString() + " )";
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symTbl, MyIDictionary<Integer, Value> heap) throws MyException {
        Value expr1 = exp_left.eval(symTbl, heap);
        Value expr2 = exp_right.eval(symTbl, heap);
        boolean booleanEvaluationResult = switch (comparisonOperator) {
            case "<" -> (Integer) expr1.getVal() < (Integer) expr2.getVal();
            case "<=" -> (Integer) expr1.getVal() <= (Integer) expr2.getVal();
            case ">" -> (Integer) expr1.getVal() > (Integer) expr2.getVal();
            case ">=" -> (Integer) expr1.getVal() >= (Integer) expr2.getVal();
            case "==" -> expr1.equals(expr2);
            case "!=" -> !expr1.equals(expr2);
            default -> throw new MyException(comparisonOperator + " is not a valid comparison operator.");
        };

        return new BoolValue(booleanEvaluationResult);
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type type1, type2;
        type1 = exp_left.typeCheck(typeEnv);
        type2 = exp_right.typeCheck(typeEnv);
        if(type1.equals(new IntType()) && type2.equals(new IntType()))
            return new IntType();
        else throw new MyException("The two operands have different types.");
    }
}