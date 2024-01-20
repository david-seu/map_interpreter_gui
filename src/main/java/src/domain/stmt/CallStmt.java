package src.domain.stmt;

import javafx.util.Pair;
import src.domain.exception.MyException;
import src.domain.exp.Exp;
import src.domain.prgstate.MyDictionary;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.MyIList;
import src.domain.prgstate.PrgState;
import src.domain.type.Type;
import src.domain.value.StringValue;
import src.domain.value.Value;

import java.util.ArrayList;

public class CallStmt implements IStmt{

    private final String name;

    private final ArrayList<Exp> arguments;

    public CallStmt(String name, ArrayList<Exp> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Pair<ArrayList<Value>, IStmt>> procTable = state.getProcTable();
        if(!procTable.isDefined(name))
            throw new MyException("Procedure is not defined");
        ArrayList<Value> values = procTable.lookup(name).getKey();
        IStmt stmt = procTable.lookup(name).getValue();
        MyIDictionary<String, Value> symTable = new MyDictionary<>();
        for(int i = 0; i < values.size(); i++){
            symTable.add(values.get(i).getVal().toString(), arguments.get(i).eval(state.getSymTable(), state.getHeap()));
        }
        state.getSymTables().push(symTable);
        state.getStack().push(new ReturnStmt());
        state.getStack().push(stmt);
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        return null;
    }

    @Override
    public String toString(){
        return "call(" + name + ", " + arguments.toString() + ")";
    }
}
