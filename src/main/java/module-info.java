module lib.electronic.components {
    // Export your public packages
    exports no.cantara.electronic.component;

    // Require other modules (if any). For example, if you need SLF4J:
    requires org.slf4j;

    // If you have transitive dependencies, you can declare them as transitive.
    // For example, if clients of your module should also see Jackson:
    requires transitive com.fasterxml.jackson.core;
}

