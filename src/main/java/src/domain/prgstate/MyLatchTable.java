package src.domain.prgstate;

import src.domain.exception.EmptyStackException;

public class MyLatchTable extends MyDictionary<Integer, Integer>{
    private Integer address = 1;
    private static final MyIStack<Integer> freeAddress = new MyStack<>();

    public MyLatchTable(){
        super();
    }

    public Integer getFreeAddress() throws EmptyStackException {
        if(freeAddress.isEmpty())
            return address++;
        Integer free = freeAddress.top();
        freeAddress.pop();
        return free;

    }
}
