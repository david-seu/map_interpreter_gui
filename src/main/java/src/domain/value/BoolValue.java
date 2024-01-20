package src.domain.value;

import src.domain.type.BoolType;
import src.domain.type.Type;

public class BoolValue implements Value{
    private final Boolean val;

    public BoolValue(Boolean v) {val=v;}

    @Override
    public boolean equals(Object another) {
        return another instanceof BoolValue;
    }

    @Override
    public String toString(){
        return Boolean.toString(val);
    }

    @Override
    public Object getVal(){
        return val;
    }

    @Override
    public Type getType(){
        return new BoolType();
    }

    @Override
    public Value deepCopy() {
        return new BoolValue(val);
    }

}
