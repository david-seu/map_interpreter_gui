package src.domain.stmt.lockTable;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.stmt.IStmt;
import src.domain.type.Type;
import src.domain.value.IntValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockStmt implements IStmt {

    private final String var;

    private final static Lock lock = new ReentrantLock();

    public LockStmt(String var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        IntValue foundIndex = (IntValue) state.getSymTable().lookup(var);
        if(foundIndex == null)
            throw new MyException("Variable " + var + " is not defined");

        Integer index = (Integer) foundIndex.getVal();
        Integer lockValue = state.getLockTable().lookup(index);
        if(lockValue == null)
            throw new MyException("Variable " + var + " is not defined");

        if(lockValue == -1)
            state.getLockTable().update(index, state.getId());
        else
            state.getStack().push(this);
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeVar = typeEnv.lookup(var);
        if(typeVar.equals(new IntValue(0).getType()))
            return typeEnv;
        else
            throw new MyException(var + " is not of type int");
    }

    @Override
    public String toString() {
        return "lock(" + var + ")";
    }
}
