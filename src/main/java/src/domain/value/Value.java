package src.domain.value;

import src.domain.type.Type;

public interface Value {
    Type getType();
    Object getVal();
    Value deepCopy();
}
