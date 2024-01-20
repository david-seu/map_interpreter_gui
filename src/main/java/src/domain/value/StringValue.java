package src.domain.value;

import src.domain.type.StringType;
import src.domain.type.Type;

public class StringValue implements Value{

    private final String val;

    public StringValue(String val){
        this.val = val;
    }

    @Override
    public boolean equals(Object another) {
        return another instanceof StringValue;
    }

    @Override
    public Type getType() {
        return new StringType();
    }

    @Override
    public Object getVal() {
        return val;
    }

    @Override
    public String toString() {
        return val;
    }

    @Override
    public Value deepCopy() {
        return new StringValue(val);
    }
}
