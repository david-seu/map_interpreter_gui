package src.domain.type;

import src.domain.value.StringValue;
import src.domain.value.Value;

public class StringType implements Type{


    @Override
    public boolean equals(Object another) {
        return another instanceof StringType;
    }

    @Override
    public String toString() {
        return "string";
    }

    @Override
    public Value defaultValue() {
        return new StringValue("");
    }
}
