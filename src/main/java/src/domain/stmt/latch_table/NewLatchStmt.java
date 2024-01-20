package src.domain.stmt.latch_table;

import src.domain.exception.MyException;
import src.domain.exp.Exp;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.MyLatchTable;
import src.domain.prgstate.PrgState;
import src.domain.stmt.IStmt;
import src.domain.type.Type;
import src.domain.value.IntValue;
import src.domain.value.Value;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewLatchStmt implements IStmt {
    private final String var;
    private final Exp exp;

    private final static Lock lock = new ReentrantLock();

    public NewLatchStmt(String var, Exp exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        Value number = exp.eval(state.getSymTable(), state.getHeap());

        if(!number.getType().equals(new IntValue(0).getType()))
            throw new MyException("Expression is not an integer");

        int numberInt = (int) number.getVal();

        int newFreeLocation = ((MyLatchTable) state.getLatchTable()).getFreeAddress();

        state.getLatchTable().add(newFreeLocation, numberInt);

        if(state.getSymTable().isDefined(var))
            state.getSymTable().update(var, new IntValue(newFreeLocation));
        else
            throw new MyException("Variable is not defined");
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type foundType = typeEnv.lookup(var);
        Type foundTypeExp = exp.typeCheck(typeEnv);
        if(foundType.equals(new IntValue(0).getType()) && foundTypeExp.equals(new IntValue(0).getType()))
            return typeEnv;
        else
            throw new MyException("Variable is not an integer");
    }

    @Override
    public String toString() {
        return "newLatch(" + var + ", " + exp.toString() + ")";
    }
}
