package rub.labs.chaining.utils;

@FunctionalInterface
public interface Converter<S, D> {
    public D convert(S source);
}
