package io.github.wcarmon.git;

import static java.util.Objects.requireNonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** See https://semver.org */
public record SemVer(
        int major,
        int minor,
        int patch,
        String preReleaseLabel,
        String buildMetadata,
        boolean includeVPrefix) {

    private static final Pattern LABEL_PATTERN = Pattern.compile("^[0-9A-Za-z.]+$");
    private static final int MAX_BUILD_META_LEN = 48;
    private static final int MAX_LEN = 128;
    private static final int MAX_PRE_RELEASE_LABEL_LEN = 48;
    private static final Pattern SEMVER =
            Pattern.compile("^v?(\\d+)\\.(\\d+)\\.(\\d+)(-([0-9A-Za-z.]+))?(\\+([0-9A-Za-z.]+))?$");

    public SemVer {
        if (major < 0) {
            throw new IllegalArgumentException("major must be >= 0");
        }
        if (minor < 0) {
            throw new IllegalArgumentException("minor must be >= 0");
        }
        if (patch < 0) {
            throw new IllegalArgumentException("patch must be >= 0");
        }

        preReleaseLabel = MoreStringUtils.normalize(preReleaseLabel);
        buildMetadata = MoreStringUtils.normalize(buildMetadata);

        if (preReleaseLabel.length() > MAX_PRE_RELEASE_LABEL_LEN) {
            throw new IllegalArgumentException("preReleaseLabel is too long");
        }

        if (buildMetadata.length() > MAX_BUILD_META_LEN) {
            throw new IllegalArgumentException("buildMetadata is too long");
        }

        if (!preReleaseLabel.isBlank() && !LABEL_PATTERN.matcher(preReleaseLabel).matches()) {
            throw new IllegalArgumentException(
                    "preReleaseLabel must contain only letters, numbers, and periods: "
                            + preReleaseLabel);
        }

        if (!buildMetadata.isBlank() && !LABEL_PATTERN.matcher(buildMetadata).matches()) {
            throw new IllegalArgumentException(
                    "buildMetadata must contain only letters, numbers, and periods: "
                            + buildMetadata);
        }
    }

    private SemVer(Builder builder) {
        this(
                builder.major,
                builder.minor,
                builder.patch,
                builder.preReleaseLabel,
                builder.buildMetadata,
                builder.includeVPrefix);
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Simplified factory for the common case
     *
     * @param major see semver
     * @param minor see semver
     * @param patch see semver
     * @return new instance
     */
    public static SemVer from(int major, int minor, int patch) {
        return new SemVer(major, minor, patch, "", "", false);
    }

    /**
     * @param raw semver string
     * @return parsed instance
     */
    public static SemVer parse(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("semver value is required");
        }

        if (raw.length() > MAX_LEN) {
            throw new IllegalArgumentException(
                    "semver value is too long: " + "length=" + raw.length() + " max=" + MAX_LEN);
        }

        final Matcher m = SEMVER.matcher(raw);
        if (!m.find()) {
            throw new IllegalArgumentException("invalid semver value");
        }

        final boolean includeVPrefix = raw.startsWith("v");
        final int major = Integer.parseInt(m.group(1));
        final int minor = Integer.parseInt(m.group(2));
        final int patch = Integer.parseInt(m.group(3));
        final String preReleaseLabel = m.group(5);
        final String buildMetadata = m.group(7);

        return new SemVer(major, minor, patch, preReleaseLabel, buildMetadata, includeVPrefix);
    }

    @Override
    public String toString() {
        final StringBuilder out = new StringBuilder(MAX_LEN);
        if (includeVPrefix) {
            out.append("v");
        }

        out.append(major).append(".").append(minor).append(".").append(patch);

        if (!preReleaseLabel.isBlank()) {
            out.append("-").append(preReleaseLabel);
        }

        if (!buildMetadata.isBlank()) {
            out.append("+").append(buildMetadata);
        }
        return out.toString();
    }

    /**
     * Bump appropriate version
     *
     * @param type   major, minor, patch, ...
     * @param amount eg. 1
     * @return new instance
     */
    public SemVer withIncrement(VersionIncrementType type, int amount) {
        requireNonNull(type, "type is required and null.");

        return switch (type) {
            case MAJOR -> withMajorInc(amount);
            case MINOR -> withMinorInc(amount);
            case PATCH -> withPatchInc(amount);
        };
    }

    /**
     * Bump the major version, drop preRelease and build metadata, retain v prefix when present.
     *
     * @param amount eg. 1
     * @return new instance
     */
    public SemVer withMajorInc(int amount) {
        return new SemVer(major + amount, minor, patch, "", "", includeVPrefix);
    }

    /**
     * Bump the minor version, drop preRelease and build metadata, retain v prefix when present.
     *
     * @param amount eg. 1
     * @return new instance
     */
    public SemVer withMinorInc(int amount) {
        return new SemVer(major, minor + amount, patch, "", "", includeVPrefix);
    }

    /**
     * Bump the patch version, drop preRelease and build metadata, retain v prefix when present.
     *
     * @param amount eg. 1
     * @return new instance
     */
    public SemVer withPatchInc(int amount) {
        return new SemVer(major, minor, patch + amount, "", "", includeVPrefix);
    }

    public static final class Builder {

        private String buildMetadata;
        private boolean includeVPrefix;
        private int major;
        private int minor;
        private int patch;
        private String preReleaseLabel;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public SemVer build() {
            return new SemVer(this);
        }

        public Builder buildMetadata(String val) {
            buildMetadata = val;
            return this;
        }

        public Builder includeVPrefix(boolean val) {
            includeVPrefix = val;
            return this;
        }

        public Builder major(int val) {
            major = val;
            return this;
        }

        public Builder minor(int val) {
            minor = val;
            return this;
        }

        public Builder patch(int val) {
            patch = val;
            return this;
        }

        public Builder preReleaseLabel(String val) {
            preReleaseLabel = val;
            return this;
        }
    }
}
