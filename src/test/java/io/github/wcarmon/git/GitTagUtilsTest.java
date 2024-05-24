package io.github.wcarmon.git;

import static io.github.wcarmon.git.VersionIncrementType.MAJOR;
import static io.github.wcarmon.git.VersionIncrementType.MINOR;
import static io.github.wcarmon.git.VersionIncrementType.PATCH;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

class GitTagUtilsTest {

    @Test
    void testBumpVersion() {

        final var major = new HashMap<String, String>(64);
        major.put("0.0.0", "1.0.0");
        major.put("1.2.3-beta.4+sha899d8g79f87", "2.2.3");
        major.put("v0.0.0", "v1.0.0");
        major.put("v1.2.3-beta.4+sha899d8g79f87", "v2.2.3");
        // TODO: more here

        final var minor = new HashMap<String, String>(64);
        minor.put("0.0.0", "0.1.0");
        minor.put("1.2.3-beta.4+sha899d8g79f87", "1.3.3");
        minor.put("v0.0.0", "v0.1.0");
        // TODO: more here

        final var patch = new HashMap<String, String>(64);
        patch.put("1.2.3-beta.4+sha899d8g79f87", "1.2.4-beta.4");
        patch.put("0.0.0", "0.0.1");
        patch.put("v0.0.0", "v0.0.1");
        // TODO: more here

        for (final var entry : major.entrySet()) {
            assertEquals(
                    entry.getValue(),
                    GitTagUtils.bumpVersion(entry.getKey(), MAJOR),
                    "Failed on major version bump: input=" + entry.getKey()
            );
        }

        for (final var entry : minor.entrySet()) {
            assertEquals(
                    entry.getValue(),
                    GitTagUtils.bumpVersion(entry.getKey(), MINOR),
                    "Failed on minor version bump: input=" + entry.getKey()
            );
        }

        for (final var entry : patch.entrySet()) {
            assertEquals(
                    entry.getValue(),
                    GitTagUtils.bumpVersion(entry.getKey(), PATCH),
                    "Failed on patch version bump: input=" + entry.getKey()
            );
        }
    }
}
