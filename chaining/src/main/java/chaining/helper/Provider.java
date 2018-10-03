package chaining.helper;

import java.util.Iterator;

public interface Provider<T> extends Iterator<T>, Resetable {
}
