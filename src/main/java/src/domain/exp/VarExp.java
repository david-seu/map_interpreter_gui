package src.domain.exp;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.type.Type;
import src.domain.value.Value;

public class VarExp implements Exp{
    private final String id;

    public VarExp(String i) {id=i;}

    @Override
    public Value eval(MyIDictionary<String, Value> symTbl, MyIDictionary<Integer, Value> heap) throws MyException {

        Value val = symTbl.lookup(id);
        return val;
    }

    @Override
    public String toString(){
        return id;
    }

    @Override
    public Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException{
        return typeEnv.lookup(id);
    }
}
