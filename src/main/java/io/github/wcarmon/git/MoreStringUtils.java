package io.github.wcarmon.git;

import org.jetbrains.annotations.Nullable;

public final class MoreStringUtils {

    static String normalize(@Nullable String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }

        return raw.strip();
    }
}
