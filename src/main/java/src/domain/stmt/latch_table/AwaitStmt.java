package src.domain.stmt.latch_table;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.stmt.IStmt;
import src.domain.type.Type;
import src.domain.value.Value;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitStmt implements IStmt {

    private final String var;

    private final static Lock lock = new ReentrantLock();

    public AwaitStmt(String var) {
        this.var = var;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();

        Value foundIndex = state.getSymTable().lookup(var);

        if(foundIndex == null)
            throw new MyException("Variable is not defined");

        if(!foundIndex.getType().equals(new src.domain.value.IntValue(0).getType()))
            throw new MyException("Variable is not an integer");

        int index = (int) foundIndex.getVal();

        if(!state.getLatchTable().isDefined(index))
            throw new MyException("Index is not defined");

        if(state.getLatchTable().lookup(index) != 0)
        {
            state.getStack().push(this);
        }

        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type foundType = typeEnv.lookup(var);
        if(foundType.equals(new src.domain.type.IntType()))
            return typeEnv;
        else
            throw new MyException("Variable is not an integer");
    }

    @Override
    public String toString() {
        return "await(" + var + ")";
    }
}
