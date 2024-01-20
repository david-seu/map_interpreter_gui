package src.domain.prgstate;

import javafx.util.Pair;
import src.domain.exception.EmptyStackException;

import java.util.ArrayList;

public class MyCyclicBarrierTable extends MyDictionary<Integer, Pair<Integer, ArrayList<Integer>>>{
    private Integer address = 1;
    private static final MyIStack<Integer> freeAddress = new MyStack<>();

    public MyCyclicBarrierTable(){
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
