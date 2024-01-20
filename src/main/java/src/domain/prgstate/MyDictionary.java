package src.domain.prgstate;

import src.domain.exception.MyException;
import src.domain.type.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyDictionary<K,V> implements MyIDictionary<K,V> {

    private final HashMap<K,V> dict = new HashMap<>();

    public MyDictionary() {

    }

    @Override
    public V lookup(K id) {
        for(K key:dict.keySet()){
            if(key.equals(id))
                return dict.get(key);
        }
        return null;
    }

    @Override
    public Boolean isDefined(K id) {
        for(K key:dict.keySet()){
            if(key.equals(id))
                return true;
        }
        return false;
    }

    @Override
    public void update(K id, V val) {
        dict.put(id,val);
    }

    @Override
    public void add_default(K id, Type type) throws MyException {
        if(dict.get(id) == null)
            if(type != null)
            {
                //noinspection unchecked
                dict.put(id, (V)type.defaultValue());
            }
        else throw new MyException("Variable " + id + " already declared");
    }

    @Override
    public void add(K id, V val) throws MyException {
        for(K key:dict.keySet()){
            if(key.equals(id))
                throw new MyException("Variable " + id + " already declared");
        }
        dict.put(id,val);
    }

    @Override
    public void remove(K id) {
        for(K key:dict.keySet()){
            if(key.equals(id))
                dict.remove(key);
        }
    }

    public ArrayList<K> getKeys() {
        return new ArrayList<>(dict.keySet());
    }

    public void setContent(Map<K,V> newDict){
        dict.clear();
        dict.putAll(newDict);
    }

    public Map<K, V> getContent() {
        return (Map<K, V>) dict.clone();
    }

    @Override
    public String toString() {
        if(dict.isEmpty())
            return "Empty dictionary";
        return dict.toString();
    }

    public MyIDictionary<K, V> duplicate() throws MyException{
        MyIDictionary<K, V> clone = new MyDictionary<>();
        for (K key : dict.keySet()) {
            clone.add(key, dict.get(key));
        }
        return clone;
    }
}
