package src.domain.stmt.semaphore;

import javafx.util.Pair;
import src.domain.exception.MyException;
import src.domain.exp.Exp;
import src.domain.exp.ValueExp;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.MySemaphore;
import src.domain.prgstate.PrgState;
import src.domain.stmt.IStmt;
import src.domain.type.Type;
import src.domain.value.IntValue;
import src.domain.value.Value;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateSemaphoreStmt implements IStmt {
    private final String var;
    private final Exp exp;
    private static final Lock lock = new ReentrantLock();


    public CreateSemaphoreStmt(String var, Exp exp) {
        this.var = var;
        this.exp = exp;
    }


    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        IntValue number = (IntValue) exp.eval(symTable, heap);

        MySemaphore semaphore = (MySemaphore) state.getSemaphoreTable();
        Integer location = semaphore.getFreeAddress();
        semaphore.add(location, new Pair<>((Integer) number.getVal(), new ArrayList<>()));
        if(symTable.isDefined(var))
            symTable.update(var, new IntValue(location));
        else
            throw new MyException("Variable " + var + " not defined");
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typVar = typeEnv.lookup(var);
        Type typExp = exp.typeCheck(typeEnv);
        if(typVar.equals(new src.domain.type.IntType()) && typExp.equals(new src.domain.type.IntType()))
            return typeEnv;
        else
            throw new MyException("Variable " + var + " not of type int");
    }


    @Override
    public String toString() {
        return "newSemaphore(" + var + ", " + exp.toString() + ")";
    }
}
