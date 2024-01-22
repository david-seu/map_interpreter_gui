package src.domain.stmt.barrier;

import javafx.util.Pair;
import src.domain.exception.MyException;
import src.domain.exp.Exp;
import src.domain.prgstate.MyCyclicBarrierTable;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.stmt.IStmt;
import src.domain.type.IntType;
import src.domain.type.Type;
import src.domain.value.IntValue;
import src.domain.value.Value;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NewBarrierStmt implements IStmt {

    private final String var;

    private final Exp exp;

    private final static Lock lock = new ReentrantLock();

    public NewBarrierStmt(String var, Exp exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();

        Value number = exp.eval(state.getSymTable(), state.getHeap());

        if(number.getType().equals(new IntType())) {
            int numberInt = (int) number.getVal();
            MyIDictionary<Integer, Pair<Integer, ArrayList<Integer>>> cyclicBarrierTable = state.getCyclicBarrierTable();
            int newFreeLocation = ((MyCyclicBarrierTable) cyclicBarrierTable).getFreeAddress();
            cyclicBarrierTable.add(newFreeLocation, new Pair<>(numberInt, new ArrayList<>()));
            if(state.getSymTable().isDefined(var) && state.getSymTable().lookup(var).getType().equals(new IntType())) {
                state.getSymTable().update(var, new IntValue(newFreeLocation));
            }
            else {
                throw new MyException("Variable " + var + " is not defined");
            }
        }
        else {
            throw new MyException("Expression " + exp + " is not of type int");
        }


        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeVar = typeEnv.lookup(var);
        Type typeExp = exp.typeCheck(typeEnv);
        if(typeVar.equals(new IntType())) {
            if(typeExp.equals(new IntType())) {
                return typeEnv;
            }
            else {
                throw new MyException("Expression " + exp + " is not of type int");
            }
        }
        else {
            throw new MyException("Variable " + var + " is not of type int");
        }
    }

    @Override
    public String toString() {
        return "newBarrier(" + var + ", " + exp.toString() + ")";
    }
}
