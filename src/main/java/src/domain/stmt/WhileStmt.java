package src.domain.stmt;

import src.domain.exception.MyException;
import src.domain.exp.Exp;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.MyIStack;
import src.domain.prgstate.PrgState;
import src.domain.type.BoolType;
import src.domain.value.Value;

public class WhileStmt implements IStmt{

    Exp exp;
    IStmt stmt;

    public WhileStmt(Exp exp, IStmt stmt){
        this.exp = exp;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stk = state.getStack();
        Value val = exp.eval(state.getSymTable(), state.getHeap());
        if(!val.getType().equals(new BoolType())){
            throw new MyException("Conditional expression is not a boolean");
        }
        if(val.getVal().equals(true)){
            stk.push(this);
            stk.push(stmt);
        }
        return null;
    }

    @Override
    public String toString(){
        return "(while(" + exp.toString() + ") " + stmt.toString() + ")";
    }

    @Override
    public MyIDictionary<String, src.domain.type.Type> typeCheck(MyIDictionary<String, src.domain.type.Type> typeEnv) throws MyException {
        src.domain.type.Type typexp=exp.typeCheck(typeEnv);
        stmt.typeCheck(typeEnv);
        return typeEnv;
    }
}
