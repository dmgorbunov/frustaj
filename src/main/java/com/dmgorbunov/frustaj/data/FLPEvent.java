package com.dmgorbunov.frustaj.data;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FLPEvent<T> {

    private final FLPEventType type;
    private final T content;

    private FLPEvent(FLPEventType type, T content) {
        this.type = type;
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public FLPEventType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("FLPEvent{%s=%s}", type, content);
    }

    public static <T> FLPEvent<?> build(FLPEventType type, byte[] bytes) {
        return switch (type) {
            case TEXT_TITLE, TEXT_COMMENT, TEXT_AUTHOR, TEXT_GENRE, TEXT_VERSION ->
                    new FLPEvent<>(type, new String(bytes, StandardCharsets.UTF_8));
            default -> new FLPEvent<>(type, Arrays.toString(Arrays.copyOfRange(bytes, 0, 8)));
        };
    }

}
