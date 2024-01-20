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

public class NewSemaphoreStmt implements IStmt {
    private final String var;
    private final Exp exp1;

    private final Exp exp2;

    private static final Lock lock = new ReentrantLock();


    public NewSemaphoreStmt(String var, Exp exp1, ValueExp exp2) {
        this.var = var;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }


    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        IntValue number1 = (IntValue) exp1.eval(symTable, heap);
        IntValue number2 = (IntValue) exp2.eval(symTable, heap);

        MySemaphore semaphore = (MySemaphore) state.getSemaphoreTable();
        Integer location = semaphore.getFreeAddress();
        semaphore.add(location, new Pair<>((Integer) number1.getVal(), new Pair<>(new ArrayList<>(), (Integer) number2.getVal())));;
        if(symTable.isDefined(var))
            symTable.update(var, new IntValue(location));
        else
            throw new MyException("Variable " + var + " not defined");
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return null;
    }


    @Override
    public String toString() {
        return "newSemaphore(" + var + ", " + exp1.toString() + ", " + exp2.toString() + ")";
    }
}
