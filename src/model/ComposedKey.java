package model;

import java.util.Objects;

public class ComposedKey<T1,T2> {
    private final T1 key1;
    private final T2 key2;

    public ComposedKey(T1 key1, T2 key2) {
        this.key1 = key1;
        this.key2 = key2;
    }

    public T1 getKey1() {
        return key1;
    }

    public T2 getKey2() {
        return key2;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ComposedKey)
            return key1.equals(((ComposedKey) o).getKey1()) && key2.equals(((ComposedKey) o).getKey2());

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key1, key2);
    }
}
