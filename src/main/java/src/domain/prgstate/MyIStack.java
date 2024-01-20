package src.domain.prgstate;

import src.domain.exception.EmptyStackException;

import java.util.Stack;

public interface MyIStack<T> {
    T pop() throws EmptyStackException;
    void push(T v);

    T top() throws EmptyStackException;

    boolean isEmpty();

    Stack<T> getStack();
}
