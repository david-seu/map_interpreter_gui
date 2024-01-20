package src.domain.stmt;

import src.domain.exception.MyException;
import src.domain.exp.Exp;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.MyIList;
import src.domain.prgstate.PrgState;
import src.domain.value.Value;

public class PrintStmt implements IStmt {
    private final Exp exp;
    //private final IStmt stmt;0
    public PrintStmt(Exp exp) {
        this.exp = exp;
    }

    //public PrintStmt(IStmt stmt) {
    //    this.;
    //}

    @Override
    public String toString() {
        return "print(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIList<Value> out = state.getOut();
        out.add(exp.eval(state.getSymTable(), state.getHeap()));
        return null;
    }

    @Override
    public MyIDictionary<String, src.domain.type.Type> typeCheck(MyIDictionary<String, src.domain.type.Type> typeEnv) throws MyException {
        exp.typeCheck(typeEnv);
        return typeEnv;
    }
}
