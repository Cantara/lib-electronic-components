package no.cantara.electronic.component.lib;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ComponentSpecificationDefinitions to demonstrate usage and validate specifications.
 */
public class ComponentSpecificationDefinitionsTest {

    @Test
    public void shouldProvideKeySpecsForMOSFET() {
        List<ComponentSpecificationDefinitions.SpecDefinition> mosfetSpecs =
                ComponentSpecificationDefinitions.getKeySpecs(ComponentType.MOSFET);

        // Verify essential MOSFET specifications are present
        assertSpecExists(mosfetSpecs, "vdsMax", SpecUnit.VOLTS);
        assertSpecExists(mosfetSpecs, "vgsMax", SpecUnit.VOLTS);
        assertSpecExists(mosfetSpecs, "idMax", SpecUnit.AMPS);
        assertSpecExists(mosfetSpecs, "rdson", SpecUnit.OHMS);

        // Print specifications for documentation
        System.out.println("MOSFET Specifications:");
        printSpecs(mosfetSpecs);
    }

    @Test
    public void shouldProvideKeySpecsForVoltageRegulator() {
        List<ComponentSpecificationDefinitions.SpecDefinition> regulatorSpecs =
                ComponentSpecificationDefinitions.getKeySpecs(ComponentType.VOLTAGE_REGULATOR);

        // Verify essential voltage regulator specifications
        assertSpecExists(regulatorSpecs, "inputVoltageMin", SpecUnit.VOLTS);
        assertSpecExists(regulatorSpecs, "inputVoltageMax", SpecUnit.VOLTS);
        assertSpecExists(regulatorSpecs, "outputVoltage", SpecUnit.VOLTS);
        assertSpecExists(regulatorSpecs, "outputCurrent", SpecUnit.AMPS);
        assertSpecExists(regulatorSpecs, "efficiency", SpecUnit.PERCENTAGE);

        System.out.println("\nVoltage Regulator Specifications:");
        printSpecs(regulatorSpecs);
    }

    @Test
    public void shouldProvideMarineSpecificationsForConnector() {
        List<ComponentSpecificationDefinitions.SpecDefinition> marineSpecs =
                ComponentSpecificationDefinitions.getMarineSpecs(ComponentType.CONNECTOR);

        // Verify marine-specific requirements
        assertSpecExists(marineSpecs, "ipRating", SpecUnit.NONE);
        assertSpecExists(marineSpecs, "pressureRating", SpecUnit.BARS);
        assertSpecExists(marineSpecs, "saltSprayHours", SpecUnit.HOURS);
        assertSpecExists(marineSpecs, "underwaterMating", SpecUnit.NONE);

        System.out.println("\nMarine Connector Specifications:");
        printSpecs(marineSpecs);
    }

    @Test
    public void shouldGroupSpecificationsByUnit() {
        List<ComponentSpecificationDefinitions.SpecDefinition> mcuSpecs =
                ComponentSpecificationDefinitions.getKeySpecs(ComponentType.MICROCONTROLLER);

        // Group specifications by unit type
        Map<SpecUnit, List<ComponentSpecificationDefinitions.SpecDefinition>> specsByUnit =
                mcuSpecs.stream().collect(Collectors.groupingBy(ComponentSpecificationDefinitions.SpecDefinition::unit));

        System.out.println("\nMCU Specifications by Unit:");
        specsByUnit.forEach((unit, specs) -> {
            System.out.printf("\n%s:%n", unit.getDescription());
            specs.forEach(spec -> System.out.printf("  - %s: %s%n", spec.name(), spec.description()));
        });

        // Verify we have specifications in expected units
        assertTrue(specsByUnit.containsKey(SpecUnit.VOLTS), "Should have voltage specifications");
        assertTrue(specsByUnit.containsKey(SpecUnit.MEGAHERTZ), "Should have frequency specifications");
        assertTrue(specsByUnit.containsKey(SpecUnit.BYTES), "Should have memory specifications");
    }

    @Test
    public void shouldValidateUnitsForCompatibility() {
        // Get specs for multiple component types
        List<ComponentSpecificationDefinitions.SpecDefinition> ledSpecs =
                ComponentSpecificationDefinitions.getKeySpecs(ComponentType.LED);
        List<ComponentSpecificationDefinitions.SpecDefinition> resistorSpecs =
                ComponentSpecificationDefinitions.getKeySpecs(ComponentType.RESISTOR);

        // Verify no conflicting unit usage
        assertNoConflictingUnits(ledSpecs, "LED");
        assertNoConflictingUnits(resistorSpecs, "Resistor");

        System.out.println("\nLED Specifications:");
        printSpecs(ledSpecs);

        System.out.println("\nResistor Specifications:");
        printSpecs(resistorSpecs);
    }

    private void assertSpecExists(List<ComponentSpecificationDefinitions.SpecDefinition> specs,
                                  String name, SpecUnit unit) {
        assertTrue(
                specs.stream().anyMatch(spec ->
                        spec.name().equals(name) && spec.unit() == unit),
                String.format("Should have %s specification with unit %s", name, unit.getSymbol())
        );
    }

    private void assertNoConflictingUnits(List<ComponentSpecificationDefinitions.SpecDefinition> specs,
                                          String componentType) {
        // Check that the same measurement type doesn't use different unit scales
        Map<String, SpecUnit> measurementUnits = specs.stream()
                .collect(Collectors.toMap(
                        ComponentSpecificationDefinitions.SpecDefinition::name,
                        ComponentSpecificationDefinitions.SpecDefinition::unit
                ));

        // Example validation: voltage measurements should be consistent
        List<SpecUnit> voltageUnits = measurementUnits.entrySet().stream()
                .filter(e -> e.getKey().toLowerCase().contains("voltage"))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        if (!voltageUnits.isEmpty()) {
            SpecUnit baseUnit = voltageUnits.get(0).getBaseUnit();
            voltageUnits.forEach(unit ->
                    assertEquals(baseUnit, unit.getBaseUnit(),
                            String.format("Inconsistent voltage units in %s specifications", componentType))
            );
        }
    }

    private void printSpecs(List<ComponentSpecificationDefinitions.SpecDefinition> specs) {
        specs.forEach(spec ->
                System.out.printf("- %s (%s): %s%n",
                        spec.name(),
                        spec.unit().getSymbol(),
                        spec.description())
        );
    }
}