package src.domain.stmt;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.type.Type;
import src.domain.value.IntValue;

public class SleepStmt implements IStmt{

    private final IntValue number;

    public SleepStmt(IntValue number){
        this.number = number;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        if( (Integer) number.getVal() > 0){
            state.getStack().push(new SleepStmt(new IntValue((Integer) number.getVal() - 1)));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return null;
    }

    @Override
    public String toString(){
        return "sleep(" + number + ")";
    }
}
