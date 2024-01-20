package src.domain.stmt;

import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.type.Type;
import src.domain.value.Value;

public class VarDeclStmt implements IStmt {
    private final String name;
    private final Type type;

    public VarDeclStmt(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return type.toString() + " " + name;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        if(symTable.lookup(name) != null) {
            throw new MyException("Variable " + name + " already declared");
        }
        else
        {
                symTable.add_default(name, type);
        }
        return null;
    }

    @Override
    public MyIDictionary<String, Type> typeCheck(MyIDictionary<String, Type> typeEnv) throws MyException {
        typeEnv.add( name, type);
        return typeEnv;
    }

}
