package src.domain.stmt;

import src.domain.exception.MyException;
import src.domain.exp.Exp;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.type.BoolType;
import src.domain.value.BoolValue;
import src.domain.value.Value;

public class IfStmt implements IStmt {
    private final Exp exp;
    private final IStmt thenS;
    private final IStmt elseS;

    public IfStmt(Exp exp, IStmt thenS, IStmt elseS) {
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
    }

    @Override
    public String toString() {
        return "if(" + exp.toString() + ") then(" + thenS.toString() + ") else(" + elseS.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        Value val = exp.eval(symTable, heap);
        if (val.getType().equals(new BoolType())) {
            BoolValue boolVal = (BoolValue) val;
            if ((Boolean) boolVal.getVal()) {
                state.getStack().push(thenS);
            } else {
                state.getStack().push(elseS);
            }
        } else {
            throw new MyException("Conditional expression is not a boolean");
        }
        return null;
    }

    @Override
    public MyIDictionary<String, src.domain.type.Type> typeCheck(MyIDictionary<String, src.domain.type.Type> typeEnv) throws MyException {
        src.domain.type.Type typexp=exp.typeCheck(typeEnv);
        if (typexp.equals(new BoolType())) {
            thenS.typeCheck(typeEnv.duplicate());
            elseS.typeCheck(typeEnv.duplicate());
            return typeEnv;
        }
        else
            throw new MyException("The condition of IF has not the type bool");
    }


}
