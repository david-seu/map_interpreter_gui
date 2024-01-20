package src.domain.prgstate;

import java.util.ArrayList;

public interface MyIList<T>{
    void add(T v);

    ArrayList<T> getValues();
}
