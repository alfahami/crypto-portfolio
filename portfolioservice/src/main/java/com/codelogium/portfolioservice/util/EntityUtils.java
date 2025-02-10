package com.codelogium.portfolioservice.util;

import java.util.Optional;
import java.util.function.Consumer;

public class EntityUtils {
    private EntityUtils() {}; // Prevent instantiation

    // Updates a field only if the provided value is not null
    public static <T> void updateIfNotNull(Consumer<T> setter, T value) {
        Optional.ofNullable(value).ifPresent(setter);
    }
}
