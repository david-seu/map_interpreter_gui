package src.domain.stmt;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.type.Type;

public class ReturnStmt implements IStmt{

    public ReturnStmt(){
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        state.getSymTables().pop();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return null;
    }

    @Override
    public String toString(){
        return "return";
    }
}
