package src.domain.stmt.heap;
import src.domain.exception.MyException;
import src.domain.exp.Exp;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.stmt.IStmt;
import src.domain.type.RefType;
import src.domain.value.RefValue;
import src.domain.value.Value;

public class wHStmt implements IStmt{

    String varName;
    Exp exp;

    public wHStmt(String varName, Exp exp){
        this.varName = varName;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        if(!symTable.isDefined(varName))
            throw new MyException("Variable is not defined");
        Value val = symTable.lookup(varName);
        if(!(val.getType() instanceof RefType)){
            throw new MyException("Variable is not a reference");
        }
        if(!heap.isDefined((Integer)val.getVal())){
            throw new MyException("Address is not defined");
        }
        Value expVal = exp.eval(symTable, heap);
        if(!val.getType().equals((new RefValue(0, expVal.getType())).getType())){
            throw new MyException("Types do not match");
        }
        heap.update((Integer) val.getVal(), expVal);
        return null;
    }

    @Override
    public String toString(){
        return "wH(" + varName + ", " + exp.toString() + ")";
    }

    @Override
    public MyIDictionary<String, src.domain.type.Type> typeCheck(MyIDictionary<String, src.domain.type.Type> typeEnv) throws MyException {
        src.domain.type.Type typeVar = typeEnv.lookup(varName);
        src.domain.type.Type typeExp = exp.typeCheck(typeEnv);
        if(typeVar.equals(new RefType(typeExp))){
            return typeEnv;
        }
        else{
            throw new MyException("New statement: right hand side and left hand side have different types");
        }
    }

}
