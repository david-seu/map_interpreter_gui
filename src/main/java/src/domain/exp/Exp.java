package src.domain.exp;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.type.Type;
import src.domain.value.Value;

public interface Exp {
    Value eval(MyIDictionary<String, Value> symTbl, MyIDictionary<Integer, Value> heap) throws MyException;

    Type typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}
