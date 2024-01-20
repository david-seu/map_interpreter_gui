package src.domain.prgstate;

import src.domain.exception.MyException;
import src.domain.type.Type;

import java.util.ArrayList;
import java.util.Map;

public interface MyIDictionary<K,V> {
    V lookup(K id);

    Boolean isDefined(K id);
    void update(K id, V val) throws MyException;
    void add_default(K id, Type type) throws MyException;

    void add(K id, V val) throws MyException;

    void setContent(Map<K, V> newContent);

    ArrayList<K> getKeys();

    void remove(K key);

    Map<K, V> getContent();

    MyIDictionary<K, V> duplicate() throws MyException;

}
