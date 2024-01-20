package src.domain.prgstate;

import src.domain.exception.EmptyStackException;

import java.util.Stack;

public class MyStack<T> implements MyIStack<T> {
    private final Stack<T> stack;

    public MyStack() {
        stack = new Stack<>();
    }

    @Override
    public T pop() throws EmptyStackException {
        try{
            return stack.pop();
        }
        catch (Exception ignored){
            throw new EmptyStackException();
        }
    }

    @Override
    public void push(T v) {
        stack.push(v);
    }

    @Override
    public T top() throws EmptyStackException {
        try{
            return stack.peek();
        }
        catch (Exception ignored){
            throw  new EmptyStackException();
        }
    }

    @Override
    public Stack<T> getStack() {
        return (Stack<T>) stack.clone();
    }
    @Override
    public String toString() {
        if(stack.isEmpty())
            return "Empty stack";
        return stack.toString();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

}
