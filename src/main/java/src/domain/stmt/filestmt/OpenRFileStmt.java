package src.domain.stmt.filestmt;

import src.domain.exception.MyException;
import src.domain.exp.Exp;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.stmt.IStmt;
import src.domain.type.StringType;
import src.domain.value.StringValue;
import src.domain.value.Value;

import java.io.BufferedReader;
import java.io.FileReader;

public class OpenRFileStmt implements IStmt {

    private final Exp exp;

    public OpenRFileStmt(Exp exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "openRFile(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, Value> symTable = state.getSymTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        MyIDictionary<StringValue, BufferedReader> fileTable = state.getFileTable();
        Value expVal = exp.eval(symTable, heap);
        StringValue val = new StringValue((String)expVal.getVal());
        if(!expVal.getType().equals(new StringType()))
            throw new MyException("Expression is not a string");
        if(fileTable.isDefined((StringValue) expVal))
            throw new MyException("File already opened");
        try{
            BufferedReader br = new BufferedReader(new FileReader((String)expVal.getVal()));
            fileTable.add(val, br);
        }
        catch (Exception e){
            throw new MyException(e.toString());
        }
        return null;
    }

    @Override
    public MyIDictionary<String, src.domain.type.Type> typeCheck(MyIDictionary<String, src.domain.type.Type> typeEnv) throws MyException {
        src.domain.type.Type typeExp = exp.typeCheck(typeEnv);
        if(typeExp.equals(new StringType())){
            return typeEnv;
        }
        else{
            throw new MyException("OpenRFile statement: expression is not a string");
        }
    }
}
