module lib.electronic.components {
    // Export your public packages
    exports no.cantara.electronic.component;
    exports no.cantara.electronic.component.advanced;
    exports no.cantara.electronic.component.lib;
    exports no.cantara.electronic.component.lib.manufacturers;
    exports no.cantara.electronic.component.lib.connectors;
    exports no.cantara.electronic.component.lib.componentsimilaritycalculators;
    // Require other modules (if any). For example, if you need SLF4J:
    requires org.slf4j;

    // Add opens directives for Jackson
    opens no.cantara.electronic.component to com.fasterxml.jackson.databind;

    // If you have subpackages that also need to be serialized, add them too
    opens no.cantara.electronic.component.advanced to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.lib to com.fasterxml.jackson.databind;


    // If you have transitive dependencies, you can declare them as transitive.
    // For example, if clients of your module should also see Jackson:
    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.fasterxml.jackson.databind;

}

