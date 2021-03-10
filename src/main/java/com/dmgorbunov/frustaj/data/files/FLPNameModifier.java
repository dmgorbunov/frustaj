package com.dmgorbunov.frustaj.data.files;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public enum FLPNameModifier {
    TRIM_LEADING_NUMBERS(v -> v.replaceAll("^[0-9]+([ -]+)?", "")),
    TRIM_TRAILING_NUMBERS(v -> v.replaceAll("_[0-9]+", "")),
    REMOVE_TEXT_WITH_PARENTHESES(v -> v.replaceAll("\\([A-Za-z0-9- ]+\\)$", "")),
    REMOVE_LEADING_AND_TRAILING_UNDERSCORES(v -> v.replaceAll("^[_]+|[_]+$", "").trim());

    private final Function<String,String> function;

    FLPNameModifier(Function<String,String> function) {
        this.function = function;
    }

    public static List<FLPNameModifier> DEFAULT_MODIFIERS = Arrays.asList(values());

    public Function<String, String> getFunction() {
        return function;
    }
}
