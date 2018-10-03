package chaining.helper;

public class ArrayProvider<T> implements Provider<T> {
    T[] elements;
    int current = 0;

    public ArrayProvider(T... elements) {
        this.elements = elements;
    }

    @Override
    public void reset() {
        current = 0;
    }

    @Override
    public boolean hasNext() {
        return current < elements.length;
    }

    @Override
    public T next() {
        return elements[++current];
    }
}
