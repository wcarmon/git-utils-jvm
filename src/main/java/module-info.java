/** JVM module specification for Git Utils lib */
module io.github.wcarmon.config {
    requires org.eclipse.jgit;
    requires org.jetbrains.annotations;

    exports io.github.wcarmon.git;
}
