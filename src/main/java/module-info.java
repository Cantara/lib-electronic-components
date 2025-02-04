module lib.electronic.components {
    // Export existing packages
    exports no.cantara.electronic.component;
    exports no.cantara.electronic.component.advanced;
    exports no.cantara.electronic.component.lib;
    exports no.cantara.electronic.component.lib.componentsimilaritycalculators;
    exports no.cantara.electronic.component.lib.connectors;
    exports no.cantara.electronic.component.lib.manufacturers;

    // Export new specification packages
    exports no.cantara.electronic.component.lib.specs.base;
    exports no.cantara.electronic.component.lib.specs.passive;
    exports no.cantara.electronic.component.lib.specs.semiconductor;
    exports no.cantara.electronic.component.lib.specs.power;
    //exports no.cantara.electronic.component.lib.specs.mechanical;
    //exports no.cantara.electronic.component.lib.specs.validation;
    //exports no.cantara.electronic.component.lib.specs.comparison;
    //exports no.cantara.electronic.component.lib.specs.util;

    // Add opens directives for Jackson serialization
    opens no.cantara.electronic.component to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.advanced to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.lib to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.lib.componentsimilaritycalculators to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.lib.connectors to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.lib.manufacturers to com.fasterxml.jackson.databind;

    // Open new specification packages to Jackson
    opens no.cantara.electronic.component.lib.specs.base to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.lib.specs.passive to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.lib.specs.semiconductor to com.fasterxml.jackson.databind;
    opens no.cantara.electronic.component.lib.specs.power to com.fasterxml.jackson.databind;
    //opens no.cantara.electronic.component.lib.specs.mechanical to com.fasterxml.jackson.databind;
    //opens no.cantara.electronic.component.lib.specs.validation to com.fasterxml.jackson.databind;
    //opens no.cantara.electronic.component.lib.specs.comparison to com.fasterxml.jackson.databind;
    //opens no.cantara.electronic.component.lib.specs.util to com.fasterxml.jackson.databind;

    // Required dependencies
    requires org.slf4j;

    // Transitive dependencies that should be visible to consumers of this module
    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.fasterxml.jackson.databind;
    requires transitive com.fasterxml.jackson.datatype.jsr310;
    requires transitive com.fasterxml.jackson.datatype.jdk8;
}