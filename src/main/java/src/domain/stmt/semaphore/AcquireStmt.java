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

public class AcquireStmt implements IStmt {

    private final String var;

    private static final Lock lock = new ReentrantLock();

    public AcquireStmt(String var) {
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

        Pair<Integer, ArrayList<Integer>> tuple = state.getSemaphoreTable().lookup(location);
        int length = tuple.getValue().size();
        if(tuple.getKey() > length) {
            if(!tuple.getValue().contains(state.getId()))
                tuple.getValue().add(state.getId());
        }
        else {
            state.getStack().push(this);
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typVar = typeEnv.lookup(var);
        if(typVar.equals(new src.domain.type.IntType()))
            return typeEnv;
        else
            throw new MyException("Variable " + var + " not of type int");
    }

    @Override
    public String toString() {
        return "acquire(" + var + ")";
    }
}
