package src.domain.stmt.semaphore;

import javafx.util.Pair;
import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.stmt.IStmt;
import src.domain.type.Type;
import src.domain.value.Value;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReleaseStmt implements IStmt {

    private final String var;

    private static final Lock lock = new ReentrantLock();

    public ReleaseStmt(String var) {
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        MyIDictionary<String, Value> symTable = state.getSymTable();

        if(!symTable.isDefined(var))
            throw new MyException("Variable " + var + " not defined");

        Integer location = (Integer) symTable.lookup(var).getVal();

        if(!state.getSemaphoreTable().isDefined(location))
            throw new MyException("Semaphore " + location + " not defined");

        Pair<Integer, Pair<ArrayList<Integer>, Integer>> tuple = state.getSemaphoreTable().lookup(location);
        tuple.getValue().getKey().remove(state.getId());
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return null;
    }

    @Override
    public String toString() {
        return "release(" + var + ")";
    }
}
