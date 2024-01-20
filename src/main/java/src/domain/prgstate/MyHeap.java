package src.domain.prgstate;

import src.domain.exception.EmptyStackException;
import src.domain.value.Value;

public class MyHeap extends MyDictionary<Integer, Value>{

    private Integer address = 1;
    private static final MyIStack<Integer> freeAddress = new MyStack<>();

    public MyHeap(){
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
