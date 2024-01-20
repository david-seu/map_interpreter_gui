package src.domain.prgstate;


import java.util.ArrayList;

public class MyList<T> implements MyIList<T>{
    private final ArrayList<T> list;

    public MyList() {
        this.list = new ArrayList<>();
    }

    @Override
    public void add(T v) {
        list.add(v);
    }

    public ArrayList<T> getValues() {
        return list;
    }

    @Override
    public String toString() {
        if(list.isEmpty())
            return "Empty list";
        return list.toString();
    }
}
