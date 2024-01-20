package src.domain.exception;

public class VariableNotDefinedException extends MyException{
    public VariableNotDefinedException(String id) {
        super("Variable " + id + " is not defined!");
    }
}
