package src.utils;

import src.domain.exception.MyException;
import src.domain.exp.*;
import src.domain.prgstate.*;
import src.domain.stmt.*;
import src.domain.stmt.filestmt.CloseRFileStmt;
import src.domain.stmt.filestmt.OpenRFileStmt;
import src.domain.stmt.filestmt.ReadFileStmt;
import src.domain.stmt.heap.NewStmt;
import src.domain.stmt.heap.wHStmt;
import src.domain.stmt.lockTable.LockStmt;
import src.domain.stmt.lockTable.NewLockStmt;
import src.domain.stmt.lockTable.UnlockStmt;
import src.domain.type.*;
import src.domain.value.IntValue;
import src.domain.value.StringValue;
import src.domain.value.Value;

import java.io.BufferedReader;
import java.util.Map;
import java.util.Vector;

public class Utils {
    public static String infixForm(CompStmt stmt){
        if (stmt.getSecond() instanceof CompStmt){
            return stmt.getFirst().toString() + "\n" + infixForm((CompStmt) stmt.getSecond());
        }
        return stmt.getFirst().toString() + "\n" + stmt.getSecond().toString();
    }

    public static PrgState createPrgState(IStmt stmt) {
        MyIStack<IStmt> stk1 = new MyStack<>();
        MyIDictionary<String, Value> symTable1 = new MyDictionary<>();
        MyIDictionary<Integer, Value> heap = new MyHeap();
        MyIList<Value> out1 = new MyList<>();
        MyIDictionary<StringValue, BufferedReader> fileTable1 = new MyDictionary<>();
        MyIDictionary<Integer, Integer> lockTable = new MyLockTable();
        return new PrgState(stk1, symTable1, out1, fileTable1, heap, lockTable, stmt);
    }

    public static MyException typeChecker(IStmt stmt, Integer id) {
        MyIDictionary<String, Type> typeEnv = new MyDictionary<>();
        try {
            stmt.typeCheck(typeEnv);
        } catch (MyException e) {
            return e;
        }
        return null;
    }

    public static MyIDictionary<String, Value> cloneSymTable(MyIDictionary<String, Value> symTable) throws MyException {
        Map<String, Value> collect = symTable.getContent();
        MyIDictionary<String, Value> cloneSymTable = new MyDictionary<>();
        for(String key: collect.keySet()){
            cloneSymTable.add(key, collect.get(key).deepCopy());
        }
        return cloneSymTable;
    }

    public static Vector<IStmt> exampleList() {
        Vector<IStmt> prgList = new Vector<>();
        IStmt ex1= new CompStmt(new VarDeclStmt("v",new IntType()),
                new CompStmt(new AssignStmt("v",
                        new ValueExp(new IntValue(2))),
                        new PrintStmt(new VarExp("v"))));
        prgList.add(ex1);
        IStmt ex2 = new CompStmt( new VarDeclStmt("a",new IntType()),
                new CompStmt(new VarDeclStmt("b",new IntType()),
                        new CompStmt(new AssignStmt("a", new ArithExp('+',new ValueExp(new IntValue(2)),
                                new ArithExp('*',new ValueExp(new IntValue(3)), new ValueExp(new IntValue(5))))),
                                new CompStmt(new AssignStmt("b",new ArithExp('+',new VarExp("a"),
                                        new ValueExp(new IntValue(1)))), new PrintStmt(new VarExp("b"))))));
        prgList.add(ex2);
        IStmt ex3 = new CompStmt(new VarDeclStmt("a",new BoolType()),
                new CompStmt(new VarDeclStmt("v", new IntType()),
                        new CompStmt(new AssignStmt("a", new ValueExp(new IntValue(10))),
                                new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ValueExp(new  IntValue(2))),
                                        new AssignStmt("v", new ValueExp(new IntValue(3)))), new PrintStmt(new  VarExp("v"))))));
        prgList.add(ex3);
        IStmt ex4 = new CompStmt(new VarDeclStmt("varf",new StringType()),
                new CompStmt(new AssignStmt("varf",
                        new ValueExp(new StringValue("C:\\Users\\seu21\\Documente\\GitHub\\map_interpreter_gui\\test.in"))),
                        new CompStmt(new OpenRFileStmt(new VarExp("varf")),new CompStmt(new VarDeclStmt("varc",new IntType()),
                                new CompStmt(new ReadFileStmt(new VarExp("varf"),"varc"),
                                        new CompStmt(new PrintStmt(new VarExp("varc")),
                                                new CompStmt(new ReadFileStmt(new VarExp("varf"),"varc"),
                                                        new CompStmt(new PrintStmt(new VarExp("varc")),
                                                                new CloseRFileStmt(new VarExp("varf"))))))))));
        prgList.add(ex4);
        IStmt ex5 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new VarExp("v")),
                                                new PrintStmt(new VarExp("a")))))));
        prgList.add(ex5);
        IStmt ex6 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v",new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new PrintStmt(new rHExp(new VarExp("v"))),
                                                new PrintStmt(new ArithExp('+', new rHExp(new rHExp(new VarExp("a"))), new ValueExp(new IntValue(5)))))))));
        prgList.add(ex6);
        IStmt ex7 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v",new ValueExp(new IntValue(20))),
                        new CompStmt(new PrintStmt(new rHExp(new VarExp("v"))),
                                new CompStmt(new wHStmt("v",new ValueExp(new IntValue(30))),
                                        new PrintStmt(new ArithExp('+', new rHExp(new VarExp("v")),
                                                new ValueExp(new IntValue(5))))))));
        prgList.add(ex7);
        IStmt ex8 = new CompStmt(new VarDeclStmt("v", new RefType(new IntType())),
                new CompStmt(new NewStmt("v", new ValueExp(new IntValue(20))),
                        new CompStmt(new VarDeclStmt("a", new RefType(new RefType(new IntType()))),
                                new CompStmt(new NewStmt("a", new VarExp("v")),
                                        new CompStmt(new NewStmt("v", new ValueExp(new IntValue(30))),
                                                new PrintStmt(new rHExp(new rHExp(new VarExp("a")))))))));
        prgList.add(ex8);
        IStmt ex9 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(4))),
                        new CompStmt(new WhileStmt(new BooleanExp(new VarExp("v"), new ValueExp(new IntValue(0)), ">"),
                                new CompStmt(new PrintStmt(new VarExp("v")),
                                        new AssignStmt("v", new ArithExp('-',new VarExp("v"), new ValueExp(new IntValue(1)))))),
                                new PrintStmt(new VarExp("v")))));
        prgList.add(ex9);
        IStmt ex10 = new CompStmt(new VarDeclStmt("v", new IntType()),
                new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                        new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(10))),
                                new CompStmt(new NewStmt("a", new ValueExp(new IntValue(22))),
                                        new CompStmt(new ForkStmt(
                                                new CompStmt(new wHStmt("a", new ValueExp(new IntValue(30))),
                                                        new CompStmt(new AssignStmt("v", new ValueExp(new IntValue(32))),
                                                                new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new rHExp(new VarExp("a"))))))),
                                                new CompStmt(new PrintStmt(new VarExp("v")), new PrintStmt(new rHExp(new VarExp("a")))))))));
        prgList.add(ex10);

        //Ref int a; new(a,20); (for(v=0;v<3;v=v+1) fork(print(v);v=v*rh(a))); print(rh(a))
        //Create this statement

        IStmt ex11 = new CompStmt(new VarDeclStmt("a", new RefType(new IntType())),
                new CompStmt(new NewStmt("a", new ValueExp(new IntValue(20))),
                        new CompStmt(new ForStmt(new ValueExp(new IntValue(0)), new ValueExp(new IntValue(3)), new ArithExp('+', new VarExp("v"), new ValueExp(new IntValue(1))),
                                new ForkStmt(new CompStmt(new PrintStmt(new VarExp("v")), new AssignStmt("v", new ArithExp('*', new VarExp("v"), new rHExp(new VarExp("a"))))))),
                                new PrintStmt(new rHExp(new VarExp("a"))))));
        prgList.add(ex11);

        //Ref int v1; Ref int v2; int x;  int q;      new(v1,20);new(v2,30);newLock(x); fork(   fork(     lock(x);wh(v1,rh(v1)-1);unlock(x)   );    lock(x);wh(v1,rh(v1)*10);unlock(x) );newLock(q); fork(   fork(lock(q);wh(v2,rh(v2)+5);unlock(q));  lock(q);wh(v2,rh(v2)*10);unlock(q) ); nop;nop;nop;nop; lock(x); print(rh(v1)); unlock(x); lock(q); print(rh(v2)); unlock(q);
        //Create this statement

        IStmt ex12 = new CompStmt(new VarDeclStmt("v1", new RefType(new IntType())),
                new CompStmt(new VarDeclStmt("v2", new RefType(new IntType())),
                        new CompStmt(new VarDeclStmt("x", new IntType()),
                                new CompStmt(new VarDeclStmt("q", new IntType()),
                                        new CompStmt(new NewStmt("v1", new ValueExp(new IntValue(20))),
                                                new CompStmt(new NewStmt("v2", new ValueExp(new IntValue(30))),
                                                        new CompStmt(new NewLockStmt("x"),
                                                                new CompStmt(new ForkStmt(new CompStmt(new ForkStmt(new CompStmt(new LockStmt("x"),
                                                                        new CompStmt(new wHStmt("v1", new ArithExp('-', new rHExp(new VarExp("v1")), new ValueExp(new IntValue(1)))),
                                                                                new UnlockStmt("x")))),
                                                                        new CompStmt(new LockStmt("x"),
                                                                                new CompStmt(new wHStmt("v1", new ArithExp('*', new rHExp(new VarExp("v1")), new ValueExp(new IntValue(10)))),
                                                                                        new UnlockStmt("x"))))),
                                                                        new CompStmt(new NewLockStmt("q"),
                                                                                new CompStmt(new ForkStmt(new CompStmt(new ForkStmt(new CompStmt(new LockStmt("q"),
                                                                                        new CompStmt(new wHStmt("v2", new ArithExp('+', new rHExp(new VarExp("v2")), new ValueExp(new IntValue(5)))),
                                                                                                new UnlockStmt("q")))),
                                                                                        new CompStmt(new LockStmt("q"),
                                                                                                new CompStmt(new wHStmt("v2", new ArithExp('*', new rHExp(new VarExp("v2")), new ValueExp(new IntValue(10)))),
                                                                                                        new UnlockStmt("q"))))),
                                                                                        new CompStmt(new NoPStmt(),
                                                                                                new CompStmt(new NoPStmt(),
                                                                                                        new CompStmt(new NoPStmt(),
                                                                                                                new CompStmt(new NoPStmt(),
                                                                                                                        new CompStmt(new LockStmt("x"),
                                                                                                                                new CompStmt(new PrintStmt(new rHExp(new VarExp("v1"))),
                                                                                                                                        new CompStmt(new UnlockStmt("x"),
                                                                                                                                                new CompStmt(new LockStmt("q"),
                                                                                                                                                        new CompStmt(new PrintStmt(new rHExp(new VarExp("v2"))),
                                                                                                                                                                new UnlockStmt("q"))))))))))))))))))));
        prgList.add(ex12);
        return prgList;
    }
}
