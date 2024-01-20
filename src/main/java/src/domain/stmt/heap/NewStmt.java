package src.domain.stmt.heap;

import src.domain.exception.MyException;
import src.domain.exp.Exp;
import src.domain.prgstate.MyHeap;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.stmt.IStmt;
import src.domain.type.RefType;
import src.domain.value.RefValue;
import src.domain.value.Value;

public class NewStmt implements IStmt {

    private final String var_name;
    private final Exp exp;

    public NewStmt(String var_name, Exp exp){
        this.var_name = var_name;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyHeap heap = (MyHeap) state.getHeap();
        if(!symTbl.isDefined(var_name))
            throw new MyException("Variable is not defined");
        Value val = symTbl.lookup(var_name);
        if(!(val.getType() instanceof RefType))
            throw new MyException("Variable is not a reference");
        Value expVal = exp.eval(symTbl, heap);
        if(!val.getType().equals((new RefValue(0, expVal.getType())).getType()))
            throw new MyException("Types do not match");
        Integer address = heap.getFreeAddress();
        RefValue refVal = new RefValue(address, expVal.getType());
        heap.add(address, expVal);
        symTbl.update(var_name, refVal);
        return null;
    }

    @Override
    public String toString(){
        return "new(" + var_name + ", " + exp.toString() + ")";
    }

    @Override
    public MyIDictionary<String, src.domain.type.Type> typeCheck(MyIDictionary<String, src.domain.type.Type> typeEnv) throws MyException {
        src.domain.type.Type typeVar = typeEnv.lookup(var_name);
        src.domain.type.Type typeExp = exp.typeCheck(typeEnv);
        if(typeVar.equals(new RefType(typeExp))){
            return typeEnv;
        }
        else{
            throw new MyException("New statement: right hand side and left hand side have different types");
        }
    }
}
