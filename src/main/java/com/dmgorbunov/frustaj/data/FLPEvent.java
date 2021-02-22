package com.dmgorbunov.frustaj.data;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.function.Function;

public class FLPEvent<T> {

    private final long id;
    private final FLPEventType type;
    private final T content;

    public FLPEvent(long id, FLPEventType type, T content) {
        this.id = id;
        this.type = type;
        this.content = content;
    }

    public FLPEvent(long id, T content) {
        this(id, FLPEventType.find(id), content);
    }

    public T getContent() {
        return content;
    }

    public FLPEventType getType() {
        return type;
    }

    public static <T> FLPEvent<T> build(long id, byte[] bytes, Function<byte[], T> mappingFunction) {
        return new FLPEvent<>(id, FLPEventType.find(id), mappingFunction.apply(bytes));
    }

    public static FLPEvent<String> build(long id, byte[] bytes) {
        return build(id, bytes, Arrays::toString);
    }

    public static FLPEvent<String> build(long id, byte[] bytes, Charset charset) {
        return build(id, bytes, b -> new String(b, charset));
    }

    @Override
    public String toString() {
        return String.format("FLPEvent{%d (%s): %s}", id, type, content);
    }

    public long getId() {
        return id;
    }
}
