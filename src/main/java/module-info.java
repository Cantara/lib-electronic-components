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

    // Add opens directives for Jackson 3.x serialization
    opens no.cantara.electronic.component to tools.jackson.databind;
    opens no.cantara.electronic.component.advanced to tools.jackson.databind;
    opens no.cantara.electronic.component.lib to tools.jackson.databind;
    opens no.cantara.electronic.component.lib.componentsimilaritycalculators to tools.jackson.databind;
    opens no.cantara.electronic.component.lib.connectors to tools.jackson.databind;
    opens no.cantara.electronic.component.lib.manufacturers to tools.jackson.databind;

    // Open new specification packages to Jackson
    opens no.cantara.electronic.component.lib.specs.base to tools.jackson.databind;
    opens no.cantara.electronic.component.lib.specs.passive to tools.jackson.databind;
    opens no.cantara.electronic.component.lib.specs.semiconductor to tools.jackson.databind;
    opens no.cantara.electronic.component.lib.specs.power to tools.jackson.databind;
    //opens no.cantara.electronic.component.lib.specs.mechanical to tools.jackson.databind;
    //opens no.cantara.electronic.component.lib.specs.validation to tools.jackson.databind;
    //opens no.cantara.electronic.component.lib.specs.comparison to tools.jackson.databind;
    //opens no.cantara.electronic.component.lib.specs.util to tools.jackson.databind;

    // Required dependencies
    requires org.slf4j;

    // Jackson 3.x dependencies
    requires transitive tools.jackson.core;
    requires transitive tools.jackson.databind;
    // Annotations remain on 2.x
    requires transitive com.fasterxml.jackson.annotation;
    // Note: Java 8 date/time support is built-in to Jackson 3.x
}
