package src.domain.stmt.lockTable;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.MyLockTable;
import src.domain.prgstate.PrgState;
import src.domain.stmt.IStmt;
import src.domain.type.Type;
import src.domain.value.IntValue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewLockStmt implements IStmt {

    private final String var;

    private final static Lock lock = new ReentrantLock();

    public NewLockStmt(String var) {
        this.var = var;
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        MyIDictionary<Integer, Integer> lockTable = state.getLockTable();
        IntValue freeAddress = new IntValue(((MyLockTable) lockTable).getFreeAddress());
        lockTable.add((Integer) freeAddress.getVal(), -1);
        if(state.getSymTable().isDefined(var))
            state.getSymTable().update(var, freeAddress);
        else
            throw new MyException("Variable " + var + " is not defined");
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
        return "newLock(" + var + ")";
    }
}
