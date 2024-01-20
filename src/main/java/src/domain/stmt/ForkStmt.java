package src.domain.stmt;

import javafx.util.Pair;
import src.domain.exception.MyException;
import src.domain.prgstate.*;
import src.domain.value.StringValue;
import src.domain.value.Value;
import src.utils.Utils;

import java.io.BufferedReader;
import java.util.ArrayList;

public class ForkStmt implements IStmt{
    IStmt stmt;

    public ForkStmt(IStmt stmt){
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState prg) throws MyException {
        MyIStack<IStmt> stk = new MyStack<>();
        MyIStack<MyIDictionary<String, Value>> symTables = new MyStack<>();
        for(MyIDictionary<String, Value> symTable : prg.getSymTables().getStack()){
            symTables.push(Utils.cloneSymTable(symTable));
        }
        MyIDictionary<Integer, Value> heap = prg.getHeap();
        MyIDictionary<StringValue, BufferedReader> fileTable = prg.getFileTable();
        MyIList<Value> out = prg.getOut();
        MyIDictionary<String, Pair<ArrayList<Value>, IStmt>> procTable = prg.getProcTable();
        return new PrgState(stk, symTables, out, fileTable, heap, procTable, stmt);
    }

    @Override
    public String toString(){
        return "fork(" + stmt.toString() + ")";
    }

    @Override
    public MyIDictionary<String, src.domain.type.Type> typeCheck(MyIDictionary<String, src.domain.type.Type> typeEnv) throws MyException {
        stmt.typeCheck(typeEnv.duplicate());
        return typeEnv;
    }
}
