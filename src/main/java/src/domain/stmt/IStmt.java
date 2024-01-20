package src.domain.stmt;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.type.Type;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException;

    MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException;
}
