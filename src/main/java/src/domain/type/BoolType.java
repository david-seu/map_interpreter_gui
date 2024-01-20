package src.domain.type;

import src.domain.value.BoolValue;
import src.domain.value.Value;

public class BoolType implements Type{
    @Override
    public boolean equals(Object another) {
        return another instanceof BoolType;
    }

    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public Value defaultValue() {
        return new BoolValue(false);
    }
}
