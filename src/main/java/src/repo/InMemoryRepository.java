package src.repo;

import javafx.util.Pair;
import src.domain.exception.MyException;
import src.domain.prgstate.MyIDictionary;
import src.domain.prgstate.MyIList;
import src.domain.prgstate.PrgState;
import src.domain.stmt.CompStmt;
import src.domain.stmt.IStmt;
import src.domain.value.StringValue;
import src.domain.value.Value;
import src.utils.Utils;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

public class InMemoryRepository implements IRepository{
    private Vector<PrgState> prgList;
    private final String logFilePath;

    public InMemoryRepository(Vector<PrgState> prgList, String logFilePath) {
        this.prgList = prgList;
        this.logFilePath = logFilePath;
    }

    public InMemoryRepository(PrgState prgState, String logFilePath){
        this.prgList = new Vector<>();
        this.prgList.add(prgState);
        this.logFilePath = logFilePath;
    }


    @Override
    public Vector<PrgState> getPrgList(){
        return prgList;
    }

    @Override
    public Vector<PrgState> setPrgList(Vector<PrgState> prgList){
        return this.prgList = prgList;
    }

    public void logPrgStateExec(PrgState prg) throws MyException {
        try {
            PrintWriter logFile = new PrintWriter(new FileWriter(logFilePath, true));
            logFile.println("Id=" + prg.getId());
            logFile.println("SymTable_" + prg.getId() + ":");
            MyIDictionary<String, Value> symTbl = prg.getSymTable();
            for(String key : symTbl.getKeys()) {
                logFile.println(key + "-->" + symTbl.lookup(key));
            }
            logFile.println("------------------------------------------------------------");
            logFile.println("ExeStack_" + prg.getId() + ":");
            Stack<IStmt> stk = prg.getStack().getStack();
            while(!stk.isEmpty()) {
                IStmt s = stk.pop();
                if(s instanceof CompStmt)
                    logFile.println(Utils.infixForm((CompStmt) s));
                else
                    logFile.println(s.toString());
            }
            logFile.println("------------------------------------------------------------");
            logFile.flush();
            logFile.close();
        }
        catch (Exception e) {
            throw new MyException(e.getMessage());
        }
    }

    public void logAllPrgStateExec() throws MyException{
        try{
            PrintWriter logFile = new PrintWriter(new FileWriter(logFilePath, true));
            logFile.println("Heap");
            MyIDictionary<Integer,Value> heap = prgList.get(0).getHeap();
            for(Integer key : heap.getKeys()) {
                logFile.println(key + "-->" + heap.lookup(key));
            }
            logFile.println("------------------------------------------------------------");
            logFile.println("FileTable");
            MyIDictionary<StringValue, BufferedReader> fileTable = prgList.get(0).getFileTable();
            for(StringValue key : fileTable.getKeys()) {
                logFile.println(key);
            }
            logFile.println("------------------------------------------------------------");
            logFile.println("SemaphoreTable");
            MyIDictionary<Integer, Pair<Integer, Pair<ArrayList<Integer>, Integer>>> semaphoreTable = prgList.get(0).getSemaphoreTable();
            for(Integer key : semaphoreTable.getKeys()) {
                logFile.println(key + "-->" + semaphoreTable.lookup(key));
            }
            logFile.println("------------------------------------------------------------");
            logFile.println("Out");
            MyIList<Value> out = prgList.get(0).getOut();
            for(Value v : out.getValues()) {
                logFile.println(v);
            }
            logFile.println("------------------------------------------------------------");
            logFile.flush();
            logFile.close();
        }
        catch (Exception e) {
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public String getLogFilePath(){
        return this.logFilePath;
    }
}
