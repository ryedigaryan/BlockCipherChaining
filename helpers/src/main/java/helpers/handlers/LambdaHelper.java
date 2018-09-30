package helpers.handlers;

import java.util.function.Consumer;

public class LambdaHelper {
    public static <T, E extends Exception> Consumer<T> rethrowAsError(ThrowingConsumer<T, E> consumer) {
        return t -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                System.err.println("Error while accepting: " + t.toString() + " Consumer: " + consumer);
                throw new Error(e);
            }
        };
    }

    public static <E extends Exception> Runnable rethrowAsError(ThrowingRunnable<E> runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                System.err.println("Error while running runnable: " + runnable);
                throw new Error(e);
            }
        };
    }
}
