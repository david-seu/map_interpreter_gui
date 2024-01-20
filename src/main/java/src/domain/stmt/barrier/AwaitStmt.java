package src.domain.stmt.barrier;

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

        Value index = state.getSymTable().lookup(var);

        if(index == null) {
            throw new MyException("Variable " + var + " not defined");
        }

        if(!index.getType().equals(new src.domain.value.IntValue(0).getType())) {
            throw new MyException("Variable " + var + " not of type int");
        }

        Integer indexInt = (Integer) index.getVal();
        if(!state.getCyclicBarrierTable().isDefined(indexInt)) {
            throw new MyException("Variable " + var + " not in cyclic barrier table");
        }

        Integer totalNumberOfThreads = state.getCyclicBarrierTable().lookup(indexInt).getKey();
        Integer numberOfThreadsAwaiting = state.getCyclicBarrierTable().lookup(indexInt).getValue().size();

        if(totalNumberOfThreads > numberOfThreadsAwaiting){
            if(state.getCyclicBarrierTable().lookup(indexInt).getValue().contains(state.getId())) {
                state.getStack().push(this);
            }
            else {
                state.getCyclicBarrierTable().lookup(indexInt).getValue().add(state.getId());
            }
        }

        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return null;
    }

    @Override
    public String toString() {
        return "await(" + var + ")";
    }
}
