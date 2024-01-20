package src.domain.prgstate;

import javafx.util.Pair;
import src.domain.exception.EmptyStackException;
import src.domain.exception.MyException;
import src.domain.stmt.IStmt;
import src.domain.value.StringValue;
import src.domain.value.Value;

import java.io.BufferedReader;
import java.util.ArrayList;

public class PrgState {
    private final MyIStack<IStmt> stk;
    private final MyIStack<MyIDictionary<String, Value>> symTables;

    private final MyIList<Value> out;
    private final MyIDictionary<StringValue, BufferedReader> fileTable;
    private final MyIDictionary<Integer,Value> heap;

    private final MyIDictionary<String, Pair<ArrayList<Value>, IStmt>> procTable;

    private final Integer id;

    private static Integer nrPrgStates = 0;
    private IStmt originalProgram; //optional field, but good to have

    public PrgState(MyIStack<IStmt> stk, MyIStack<MyIDictionary<String, Value>> symTables, MyIList<Value> out, MyIDictionary<StringValue,BufferedReader> fileTable, MyIDictionary<Integer,Value> heap, MyIDictionary<String, Pair<ArrayList<Value>, IStmt>> procTable, IStmt prg) throws EmptyStackException {
        this.stk = stk;
        this.symTables = symTables;
        this.out = out;
        this.fileTable = fileTable;
        this.heap = heap;
        this.procTable = procTable;
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

    public MyIStack<MyIDictionary<String, Value>> getSymTables() {
        return symTables;
    }

    public MyIDictionary<String, Value> getSymTable() throws EmptyStackException {
        return symTables.top();
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

    public MyIDictionary<String, Pair<ArrayList<Value>, IStmt>> getProcTable() {
        return procTable;
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
        return "Id: " + id + "\nExeStack:\n" + stk.toString() + "\nSymTable:\n" + symTables.toString() + "\nOut:\n" + out.toString() + "\nFileTable:\n" + fileTable.toString() + "\nHeap:\n" + heap.toString() + "\n";
    }

    public Integer getId() {
        return id;
    }
}
