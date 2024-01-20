package src.domain.prgstate;

import src.domain.exception.MyException;
import src.domain.stmt.IStmt;
import src.domain.value.StringValue;
import src.domain.value.Value;

import java.io.BufferedReader;

public class PrgState {
    private final MyIStack<IStmt> stk;
    private final MyIDictionary<String, Value> symTable;
    private final MyIList<Value> out;
    private final MyIDictionary<StringValue, BufferedReader> fileTable;
    private final MyIDictionary<Integer,Value> heap;

    private final Integer id;

    private static Integer nrPrgStates = 0;
    private IStmt originalProgram; //optional field, but good to have

    public PrgState(MyIStack<IStmt> stk, MyIDictionary<String, Value> symTable, MyIList<Value> out, MyIDictionary<StringValue,BufferedReader> fileTable, MyIDictionary<Integer,Value> heap, IStmt prg){
        this.stk = stk;
        this.symTable = symTable;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.originalProgram = prg;
        this.id = getNewId();
        stk.push(prg);
    }

    private synchronized Integer getNewId(){
        nrPrgStates++;
        return nrPrgStates;
    }

    public MyIStack<IStmt> getStack() {
        return stk;
    }

    public MyIDictionary<String, Value> getSymTable() {
        return symTable;
    }

    public MyIList<Value> getOut() {
        return out;
    }

    public MyIDictionary<StringValue, BufferedReader> getFileTable() {
        return fileTable;
    }

    public MyIDictionary<Integer,Value> getHeap(){
        return this.heap;
    }

    public IStmt getOriginalProgram() {
        return originalProgram;
    }

    public void setOriginalProgram(IStmt originalProgram) {
        this.originalProgram = originalProgram;
    }


    public boolean isNotCompleted(){
        return !stk.isEmpty();
    }

    public PrgState oneStep() throws MyException {
        if(stk.isEmpty()){
            throw new MyException("Program state stack is empty");
        }
        IStmt crtStmt = stk.pop();
        return crtStmt.execute(this);
    }
    @Override
    public String toString() {
        return "Id: " + id + "\nExeStack:\n" + stk.toString() + "\nSymTable:\n" + symTable.toString() + "\nOut:\n" + out.toString() + "\nFileTable:\n" + fileTable.toString() + "\nHeap:\n" + heap.toString() + "\n";
    }

    public Integer getId() {
        return id;
    }
}
