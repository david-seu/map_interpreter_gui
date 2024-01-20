package src.domain.stmt;

import src.domain.exception.MyException;
import src.domain.exception.VariableNotDefinedException;
import src.domain.exp.Exp;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.type.Type;
import src.domain.value.Value;

public class AssignStmt implements IStmt {
    private final String id;

    private final Exp exp;

    public AssignStmt(String id, Exp exp){
        this.id = id;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return id + " = " + exp.toString();
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        if(symTbl.isDefined(id)){
            Value val = exp.eval(symTbl,heap);
            Type typId = (symTbl.lookup(id)).getType();
            if(val.getType().equals(typId)){
                symTbl.update(id,val);
            }
            else{
                throw new MyException("declared type of variable" + id + " and type of the assigned expression do not match");
            }
        }
        else{
            throw new VariableNotDefinedException(id);
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        Type typeVar = typeEnv.lookup(id);
        Type typeExp = exp.typeCheck(typeEnv);
        if(typeVar.equals(typeExp)){
            return typeEnv;
        }
        else{
            throw new MyException("Assignment: right hand side and left hand side have different types");
        }
    }

    public String getId() {
        return id;
    }

    public Exp getExp() {
        return exp;
    }


}
