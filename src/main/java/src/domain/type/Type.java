package src.domain.type;

import src.domain.value.Value;

public interface Type {
    boolean equals(Object another);

    Value defaultValue();
}
