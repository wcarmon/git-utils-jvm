package io.github.wcarmon.git;

import static java.util.Objects.requireNonNull;

import java.time.Instant;

/**
 * @param ts           timestamp (date)
 * @param fullMessage  See https://git-scm.com/book/en/v2/Git-Basics-Tagging
 * @param shortName    See https://git-scm.com/book/en/v2/Git-Basics-Tagging
 * @param shortMessage See https://git-scm.com/book/en/v2/Git-Basics-Tagging
 * @param tagger       See https://git-scm.com/book/en/v2/Git-Basics-Tagging
 * @param taggerEmail  See https://git-scm.com/book/en/v2/Git-Basics-Tagging
 */
public record AnnotatedTag(
        Instant ts,
        String fullMessage,
        String shortName,
        String shortMessage,
        String tagger,
        String taggerEmail) {

    public AnnotatedTag {
        requireNonNull(ts, "ts is required and null.");
        if (shortName == null || shortName.isBlank()) {
            throw new IllegalArgumentException("shortName is required");
        }

        fullMessage = MoreStringUtils.normalize(fullMessage);
        shortMessage = MoreStringUtils.normalize(shortMessage);
        tagger = MoreStringUtils.normalize(tagger);
        taggerEmail = MoreStringUtils.normalize(taggerEmail);
    }

    private AnnotatedTag(Builder builder) {
        this(
                builder.ts,
                builder.fullMessage,
                builder.shortName,
                builder.shortMessage,
                builder.tagger,
                builder.taggerEmail);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String fullMessage;
        private String shortMessage;
        private String shortName;
        private String tagger;
        private String taggerEmail;
        private Instant ts;

        private Builder() {}

        public AnnotatedTag build() {
            return new AnnotatedTag(this);
        }

        public Builder fullMessage(String val) {
            fullMessage = val;
            return this;
        }

        public Builder shortMessage(String val) {
            shortMessage = val;
            return this;
        }

        public Builder shortName(String val) {
            shortName = val;
            return this;
        }

        public Builder tagger(String val) {
            tagger = val;
            return this;
        }

        public Builder taggerEmail(String val) {
            taggerEmail = val;
            return this;
        }

        public Builder ts(Instant val) {
            ts = val;
            return this;
        }
    }
}
