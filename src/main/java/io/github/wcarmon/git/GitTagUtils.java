package io.github.wcarmon.git;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Collection;

import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.jetbrains.annotations.Nullable;

/**
 * Convenient api over some Git tag interaction
 */
public final class GitTagUtils {

    /**
     * Semver version bump
     *
     * @param oldVersion "0.1.2" or "v0.1.2"
     * @param bumpType   major, minor, patch, ...
     * @return next major/minor/patch version (eg. "0.1.3" or "v0.1.3")
     */
    public static String bumpVersion(String oldVersion, VersionIncrementType bumpType) {
        requireNonNull(bumpType, "bumpType is required and null.");
        if (oldVersion == null || oldVersion.isBlank()) {
            throw new IllegalArgumentException("oldVersion is required");
        }

        return SemVer.parse(oldVersion)
                .withIncrement(bumpType, 1)
                .toString();
    }

    /**
     * Equivalent: git fetch --tags
     * Equivalent: git fetch --tags --prune --prune-tags
     * <p>
     * GOTCHA: requires "org.eclipse.jgit:org.eclipse.jgit.ssh.apache"
     *
     * @param git previously configured Git repo connection
     * @return {@link FetchResult}
     */
    public static FetchResult fetchTags(Git git) {
        requireNonNull(git, "git is required and null.");

        final FetchCommand fetch = git.fetch();
        fetch.setRefSpecs(new RefSpec("refs/tags/*:refs/tags/*"));

        try {
            return fetch.call();

        } catch (GitAPIException e) {
            throw new RuntimeException("Failed to git fetch", e);
        }
    }

    /**
     * Equivalent: git show v0.0.3
     * Equivalent: git show v0.0.4
     *
     * @param git     previously configured Git repo connection
     * @param tagName TODO
     * @return AnnotatedTag
     */
    @Nullable
    public static AnnotatedTag getTag(Git git, String tagName) {
        requireNonNull(git, "git is required and null.");
        if (tagName == null || tagName.isBlank()) {
            throw new IllegalArgumentException("tagName is required");
        }

        final Ref tagRef;

        try {
            tagRef = git.tagList()
                    .call()
                    .stream()
                    .filter(t -> tagNameMatches(t, tagName))
                    .findFirst()
                    .orElse(null);
        } catch (Exception ex) {
            throw new RuntimeException("failed to get tags list", ex);
        }

        if (tagRef == null) {
            return null;
        }

        try (final RevWalk walk = new RevWalk(git.getRepository())) {
            final ObjectId tagId = tagRef.getObjectId();
            final RevTag tag = walk.parseTag(tagId);

            return AnnotatedTag.builder()
                    .fullMessage(tag.getFullMessage().strip())
                    .shortMessage(tag.getShortMessage())
                    .shortName(tag.getTagName())
                    .tagger(tag.getTaggerIdent().getName())
                    .taggerEmail(tag.getTaggerIdent().getEmailAddress())
                    .ts(tag.getTaggerIdent().getWhen().toInstant())
                    .build();

        } catch (IOException ex) {
            throw new RuntimeException("failed to get tag annotations", ex);
        }
    }

    /**
     * Equivalent: git tag
     * Equivalent: git tag --list
     * Equivalent: git show-ref --tags
     * Equivalent: ls $REPO_ROOT/.git/refs/tags
     *
     * @param git previously configured Git repo connection
     * @return Zero or more tags
     */
    public static Collection<Ref> listTags(Git git) {
        requireNonNull(git, "git is required and null.");

        try {
            return git.tagList().call();
        } catch (GitAPIException e) {
            throw new RuntimeException("Failed to list tags", e);
        }
    }

    private static boolean tagNameMatches(Ref ref, String tagName) {
        requireNonNull(ref, "ref is required and null.");
        if (tagName == null || tagName.isBlank()) {
            throw new IllegalArgumentException("tagName is required");
        }

        final String n = ref.getName();
        return n.equals(tagName)
                || n.equals("refs/tags/" + tagName)
                || n.equals("refs/tags" + tagName);
    }
}
