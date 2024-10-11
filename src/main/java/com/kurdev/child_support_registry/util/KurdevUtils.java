package com.kurdev.child_support_registry.util;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KurdevUtils {

    public static <T, R> List<R> map(Collection<T> source, Function<T, R> mapper) {
        if (source == null) {
            return null;
        }
        return source.stream().map(mapper).collect(Collectors.toList());
    }
}
