/** JVM module specification for Git Utils lib */
module io.github.wcarmon.config {
    requires org.jetbrains.annotations;
    requires semver4j;
    requires org.eclipse.jgit;

    exports io.github.wcarmon.git;
}
