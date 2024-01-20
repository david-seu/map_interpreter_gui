package src.domain.stmt;

import javafx.util.Pair;
import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.type.Type;
import src.domain.value.Value;

import java.util.ArrayList;

public class AddProcStmt implements IStmt{

    private final String name;

    private final ArrayList<Value> params;

    private final IStmt stmt;

    public AddProcStmt(String name, ArrayList<Value> params, IStmt stmt) {
        this.name = name;
        this.params = params;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        state.getProcTable().add(name, new Pair<>(params, stmt));
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return null;
    }

    @Override
    public String toString(){
        StringBuilder paramsString = new StringBuilder();
        for(Value param : params){
            paramsString.append(param.toString()).append(", ");
        }
        return "proc " + name + "(" + paramsString + ") " + stmt.toString();
    }
}
