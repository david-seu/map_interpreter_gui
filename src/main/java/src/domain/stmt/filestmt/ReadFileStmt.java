package src.domain.stmt.filestmt;

import src.domain.exception.MyException;
import src.domain.exp.Exp;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.PrgState;
import src.domain.stmt.IStmt;
import src.domain.type.IntType;
import src.domain.type.StringType;
import src.domain.value.IntValue;
import src.domain.value.StringValue;
import src.domain.value.Value;

import java.io.BufferedReader;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadFileStmt implements IStmt {

    private final Exp exp;
    private final String var_name;

    private static final Lock lock = new ReentrantLock();

    public ReadFileStmt(Exp exp, String var_name) {
        this.exp = exp;
        this.var_name = var_name;
    }

    @Override
    public String toString(){
        return "readFile(" + exp.toString() + ", " + var_name + ")";
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        MyIDictionary<String, Value> symTbl = state.getSymTable();
        MyIDictionary<Integer, Value> heap = state.getHeap();
        MyIDictionary<StringValue,BufferedReader> fileTbl = state.getFileTable();
        if(!(symTbl.isDefined(var_name) && symTbl.lookup(var_name).getType().equals(new IntType())))
            throw new MyException("Variable is not defined or types do not match");
        Value expVal = exp.eval(symTbl, heap);
        StringValue val = new StringValue((String)expVal.getVal());
        if(!expVal.getType().equals(new StringType()))
            throw new MyException("Expression is not a string");
        if(!fileTbl.isDefined(val))
            throw new MyException("File is not opened");
        try{
            BufferedReader br = fileTbl.lookup(val);
            String line = br.readLine();
            if(Objects.equals(line, "<EOF>"))
                throw new MyException("End of file");
            if(Objects.equals(line,null))
                symTbl.update(var_name, new IntType().defaultValue());
            else
                symTbl.update(var_name, new IntValue(Integer.parseInt(line)));
        }
        catch (Exception e){
            throw new MyException(e.toString());
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, src.domain.type.Type> typeCheck(MyIDictionary<String, src.domain.type.Type> typeEnv) throws MyException {
        src.domain.type.Type typeExp = exp.typeCheck(typeEnv);
        src.domain.type.Type typeVar = typeEnv.lookup(var_name);
        if(typeExp.equals(new StringType()) && typeVar.equals(new IntType())){
            return typeEnv;
        }
        else{
            throw new MyException("ReadFile statement: expression is not a string or variable is not an integer");
        }
    }
}
