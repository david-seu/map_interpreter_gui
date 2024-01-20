package src.domain.exp;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.type.Type;
import src.domain.value.Value;

public class ValueExp implements Exp{
    private final Value e;

    public ValueExp(Value v) {e=v;}

    @Override
    public String toString(){
        return e.toString();
    }

    @Override
    public Value eval(MyIDictionary<String, Value> symTbl, MyIDictionary<Integer, Value> heap) throws MyException {
        return e;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return e.getType();
    }
}
