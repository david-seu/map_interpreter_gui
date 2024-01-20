package src.domain.exception;

public class EmptyStackException extends MyException{
    public EmptyStackException() {
        super("Empty stack!");
    }
}
