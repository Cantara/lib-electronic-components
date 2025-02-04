module lib.electronic.components {
    // Export your packages
    exports no.cantara.electronic.component;
    exports no.cantara.electronic.component.advanced;
    exports no.cantara.electronic.component.lib;
    exports no.cantara.electronic.component.lib.componentsimilaritycalculators;
    exports no.cantara.electronic.component.lib.connectors;
    exports no.cantara.electronic.component.lib.manufacturers;

    // Add opens directives for Jackson
    opens no.cantara.electronic.component to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.advanced to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.lib to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.lib.componentsimilaritycalculators to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.lib.connectors to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.lib.manufacturers to com.fasterxml.jackson.databind;

    // Required dependencies
    requires org.slf4j;

    // Transitive dependencies that should be visible to consumers of this module
    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.fasterxml.jackson.databind;
}