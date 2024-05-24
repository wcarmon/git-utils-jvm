package io.github.wcarmon.git;

/**
 * See https://semver.org/
 */
public enum VersionIncrementType {
    /** incompatible API change */
    MAJOR,

    /** add features, backward compatible */
    MINOR,

    /** backward compatible bug fix */
    PATCH
}
