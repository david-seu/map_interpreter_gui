package src.domain.exp;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.stmt.IStmt;
import src.domain.type.Type;
import src.domain.value.Value;

public class MulExp implements Exp{

    private final Exp exp1;
    private final Exp exp2;

    public MulExp(Exp exp1, Exp exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symTbl, MyIDictionary<Integer, Value> heap) throws MyException {
        Exp e = new ArithExp('-',new ArithExp('*', exp1, exp2), new ArithExp('+', exp1, exp2));
        return e.eval(symTbl, heap);
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return null;
    }

    @Override
    public String toString(){
        return "MUL("+exp1.toString() + ", " + exp2.toString() + ")";
    }
}
