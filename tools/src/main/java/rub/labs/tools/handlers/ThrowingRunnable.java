package rub.labs.tools.handlers;

public interface ThrowingRunnable<E extends Exception> {
    void run() throws E;
}
