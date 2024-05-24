package io.github.wcarmon.git;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SemVerTest {

    @Test
    void testParse() {

        SemVer got;

        got = SemVer.parse("0.0.0");
        assertFalse(got.includeVPrefix());
        assertEquals(0, got.major());
        assertEquals(0, got.minor());
        assertEquals(0, got.patch());
        assertEquals("", got.preReleaseLabel());
        assertEquals("", got.buildMetadata());

        got = SemVer.parse("v1.2.3");
        assertTrue(got.includeVPrefix());
        assertEquals(1, got.major());
        assertEquals(2, got.minor());
        assertEquals(3, got.patch());
        assertEquals("", got.preReleaseLabel());
        assertEquals("", got.buildMetadata());

        got = SemVer.parse("v3.4.5-beta.3");
        assertTrue(got.includeVPrefix());
        assertEquals(3, got.major());
        assertEquals(4, got.minor());
        assertEquals(5, got.patch());
        assertEquals("beta.3", got.preReleaseLabel());
        assertEquals("", got.buildMetadata());

        got = SemVer.parse("v4.5.6+sha809d8g42f87");
        assertTrue(got.includeVPrefix());
        assertEquals(4, got.major());
        assertEquals(5, got.minor());
        assertEquals(6, got.patch());
        assertEquals("", got.preReleaseLabel());
        assertEquals("sha809d8g42f87", got.buildMetadata());

        got = SemVer.parse("5.6.7-alpha.t.1+sha909d8g42f82");
        assertFalse(got.includeVPrefix());
        assertEquals(5, got.major());
        assertEquals(6, got.minor());
        assertEquals(7, got.patch());
        assertEquals("alpha.t.1", got.preReleaseLabel());
        assertEquals("sha909d8g42f82", got.buildMetadata());
    }
}
