package com.dmgorbunov.frustaj.data.files;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class FLPFileUtils {

    public static final String FLP_EXTENSION = ".flp";

    public static final BiPredicate<Path, BasicFileAttributes> FILE_MATCHER = (path, attr) ->
            path.toString().endsWith(FLPFileUtils.FLP_EXTENSION);

    public static final Function<String,String> TRIM_LEADING_NUMBERS = v -> v.replaceAll("^[0-9]+([ -]+)?", "");
    public static final Function<String,String> TRIM_TRAILING_NUMBERS = v -> v.replaceAll("_[0-9]+", "");
    public static final Function<String,String> REMOVE_TEXT_WITH_PARENTHESES = v -> v.replaceAll("\\([A-Za-z0-9- ]+\\)$", "");
    public static final Function<String,String> REMOVE_LEADING_AND_TRAILING_UNDERSCORES = v -> v.replaceAll("^[_]+|[_]+$", "").trim();

    public static List<Function<String,String>> getDefaultModifiers() {
        return Arrays.asList(TRIM_LEADING_NUMBERS, TRIM_TRAILING_NUMBERS, REMOVE_TEXT_WITH_PARENTHESES, REMOVE_LEADING_AND_TRAILING_UNDERSCORES);
    }

    public static String processName(String input, List<String> excludedKeywords) {
        String v = input.replace(FLP_EXTENSION, "").trim();

        for (Function<String,String> mod : getDefaultModifiers()) {
            v = mod.apply(v);
        }

        for (String excludedKeyword : excludedKeywords) {
            v = v.replaceAll("([ -]+)?" + excludedKeyword + "([ -]+)?", "");
        }

        return REMOVE_LEADING_AND_TRAILING_UNDERSCORES.apply(v);
    }
}
