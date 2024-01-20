package src.domain.stmt;

import src.domain.exception.MyException;
import src.domain.prgstate.*;
import src.domain.value.StringValue;
import src.domain.value.Value;
import src.utils.Utils;

import java.io.BufferedReader;

public class ForkStmt implements IStmt{
    IStmt stmt;

    public ForkStmt(IStmt stmt){
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState prg) throws MyException {
        MyIStack<IStmt> stk = new MyStack<>();
        MyIDictionary<String, Value> symTable = Utils.cloneSymTable(prg.getSymTable());
        MyIDictionary<Integer, Value> heap = prg.getHeap();
        MyIDictionary<StringValue, BufferedReader> fileTable = prg.getFileTable();
        MyIList<Value> out = prg.getOut();
        return new PrgState(stk, symTable, out, fileTable, heap, stmt);
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
