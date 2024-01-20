package src.domain.stmt;

import src.domain.exception.EmptyStackException;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;

public class NoPStmt implements IStmt {

    public NoPStmt() {
    }

    @Override
    public PrgState execute(PrgState state) throws EmptyStackException {
        return null;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public MyIDictionary<String, src.domain.type.Type> typeCheck(MyIDictionary<String, src.domain.type.Type> typeEnv) {
        return typeEnv;
    }
}
