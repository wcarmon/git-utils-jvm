package io.github.wcarmon.git;


import static java.util.Objects.requireNonNull;

import java.time.Instant;

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

        private Builder() {
        }

        public AnnotatedTag build() {
            return new AnnotatedTag(
                    ts,
                    fullMessage,
                    shortName,
                    shortMessage,
                    tagger,
                    taggerEmail
            );
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
