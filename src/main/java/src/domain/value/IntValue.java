package src.domain.value;

import src.domain.type.IntType;
import src.domain.type.Type;

public class IntValue implements Value{
    private final int val;
    public IntValue(int v) {val=v;}

    @Override
    public boolean equals(Object another) {
        if (another instanceof IntValue)
            return val == (Integer) ((IntValue) another).getVal();
        else
            return false;
    }

    public Object getVal(){
        return val;
    }

    @Override
    public String toString(){
        return Integer.toString(val);
    }

    @Override
    public Type getType(){
        return new IntType();
    }

    @Override
    public Value deepCopy() {
        return new IntValue(val);
    }


}
