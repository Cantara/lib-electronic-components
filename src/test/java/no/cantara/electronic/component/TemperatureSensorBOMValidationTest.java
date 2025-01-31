package no.cantara.electronic.component;

import no.cantara.electronic.component.lib.*;
import no.cantara.electronic.component.lib.componentsimilaritycalculators.*;
import no.cantara.electronic.component.lib.manufacturers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureSensorBOMValidationTest {
    private ComponentTypeDetector componentTypeDetector;
    private ComponentValueStandardizer valueStandardizer;
    private PackageStandardizer packageStandardizer;
    private MPNNormalizer mpnNormalizer;
    private ComponentAnalysis componentAnalysis;
    private Map<String, ManufacturerHandler> manufacturerHandlers;
    private Map<ComponentType, ComponentSimilarityCalculator> similarityCalculators;

    @BeforeEach
    void setUp() {
        componentTypeDetector = new ComponentTypeDetector();
        valueStandardizer = new ComponentValueStandardizer();
//        packageStandardizer = new PackageStandardizer();
        mpnNormalizer = new MPNNormalizer();
//        componentAnalysis = new ComponentAnalysis();

        // Initialize manufacturer handlers
        manufacturerHandlers = new HashMap<>();
        manufacturerHandlers.put("Microchip", new MicrochipHandler());
        manufacturerHandlers.put("Sensirion", new UnknownHandler()); // Custom handler could be created
        manufacturerHandlers.put("Samsung", new SamsungHandler());
        manufacturerHandlers.put("Yageo", new YageoHandler());
        manufacturerHandlers.put("Diodes Inc.", new DiodesIncHandler());

        // Initialize similarity calculators
        similarityCalculators = new HashMap<>();
        similarityCalculators.put(ComponentType.SENSOR, new SensorSimilarityCalculator());
        similarityCalculators.put(ComponentType.RESISTOR, new ResistorSimilarityCalculator());
        similarityCalculators.put(ComponentType.CAPACITOR, new CapacitorSimilarityCalculator());
        similarityCalculators.put(ComponentType.MICROCONTROLLER, new MicrocontrollerSimilarityCalculator());
    }

    void shouldValidateComponentRelationships() {
        BOM bom = createTemperatureSensorBOM();
        List<String> relationshipIssues = new ArrayList<>();

        // Find all ICs
        List<BOMEntry> ics = bom.getBomEntries().stream()
                .filter(entry -> ComponentTypeDetector.isIC(entry.getMpn()))
                .collect(Collectors.toList());

        // For each IC, validate associated components
        for (BOMEntry ic : ics) {
            // Check for bypass capacitors
            List<BOMEntry> bypassCaps = bom.getBomEntries().stream()
                    .filter(entry -> ComponentTypeDetector.isCapacitor(entry.getMpn()))
                    .filter(entry -> entry.getValue() != null && entry.getValue().contains("100n"))
                    .filter(entry -> areComponentsNearby(ic, entry))
                    .collect(Collectors.toList());

            if (bypassCaps.isEmpty()) {
                relationshipIssues.add("IC " + ic.getMpn() + " missing bypass capacitors");
            }

            // For I2C devices, check pull-up resistors
            if (ic.getSpecs().getOrDefault("Interface", "").toString().contains("I2C")) {
                List<BOMEntry> pullups = bom.getBomEntries().stream()
                        .filter(entry -> ComponentTypeDetector.isResistor(entry.getMpn()))
                        .filter(entry -> entry.getValue() != null && entry.getValue().contains("4.7k"))
                        .collect(Collectors.toList());

                if (pullups.size() < 2) {
                    relationshipIssues.add("I2C device " + ic.getMpn() + " missing pull-up resistors");
                }
            }
        }

        assertTrue(
                relationshipIssues.isEmpty(),
                "Component relationship issues found: " + String.join("\n", relationshipIssues)
        );
    }

    @Test
    void shouldValidateVoltageLevels() {
        BOM.PCBABOM bom = new BOM.PCBABOM();
        List<String> voltageIssues = new ArrayList<>();

        // Create and add entries to BOM first
        List<BOMEntry> entries = new ArrayList<>();





        bom.setBomEntries(entries);

        // Find voltage regulator using manufacturer handler
        BOMEntry regulatorComponent = bom.getBomEntries().stream()
                .filter(entry -> {
                    ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(entry.getMpn());
                    ManufacturerHandler handler = manufacturer.getHandler();
                    Set<ComponentType> supportedTypes = handler.getSupportedTypes();
                    return supportedTypes.stream().anyMatch(type ->
                            type == ComponentType.VOLTAGE_REGULATOR ||
                                    type == ComponentType.VOLTAGE_REGULATOR_DIODES ||
                                    type == ComponentType.VOLTAGE_REGULATOR_LINEAR_TI ||
                                    type.name().contains("VOLTAGE_REGULATOR")) ||
                            entry.getDescription().toLowerCase().contains("voltage regulator"); // Fallback to description
                })
                .findFirst()
                .orElse(null);

        if (regulatorComponent != null) {
            System.out.println("\nVoltage Regulator Analysis:");
            ComponentManufacturer regManufacturer = ComponentManufacturer.fromMPN(regulatorComponent.getMpn());
            System.out.println("Found voltage regulator: " + regulatorComponent.getMpn());
            System.out.println("Manufacturer: " + regManufacturer.getName());
            System.out.println("Specifications: " + regulatorComponent.getSpecs());

            Object outputVoltageObj = regulatorComponent.getSpecs().get("Output");
            if (outputVoltageObj == null) {
                voltageIssues.add("Voltage regulator missing output voltage specification");
            } else {
                String outputVoltage = outputVoltageObj.toString();

                // Check all active components
                for (BOMEntry entry : bom.getBomEntries()) {
                    if (entry == regulatorComponent) continue;

                    // Only check components that need power
                    ComponentType type = ComponentTypeDetector.determineComponentType(entry.getMpn());
                    if (type != null) {
                        System.out.println("Checking component: " + entry.getMpn() + " of type: " + type);
                        boolean isActiveComponent = type == ComponentType.MICROCONTROLLER ||
                                type == ComponentType.SENSOR ||
                                type == ComponentType.IC ||
                                entry.getDescription().toLowerCase().contains("sensor");

                        if (isActiveComponent) {
                            Object supplySpec = entry.getSpecs().get("Supply");
                            if (supplySpec == null) {
                                voltageIssues.add(String.format(
                                        "Missing supply voltage specification for active component %s (%s)",
                                        entry.getMpn(),
                                        entry.getManufacturer()
                                ));
                                continue;
                            }

                            String supplyVoltage = supplySpec.toString();
                            if (!isVoltageCompatible(outputVoltage, supplyVoltage)) {
                                voltageIssues.add(String.format(
                                        "Voltage mismatch for %s: requires %s, regulator provides %s",
                                        entry.getMpn(),
                                        supplyVoltage,
                                        outputVoltage
                                ));
                            }
                        }
                    }
                }
            }
        } else {
            voltageIssues.add("No voltage regulator found in BOM");
        }

        if (!voltageIssues.isEmpty()) {
            System.out.println("\nVoltage Issues Found:");
            voltageIssues.forEach(issue -> System.out.println("- " + issue));
        }


    }

    private boolean areComponentsNearby(BOMEntry comp1, BOMEntry comp2) {
        // Simple implementation - could be enhanced with actual layout information
        return true; // For now, assume all components are nearby
    }

    private boolean isVoltageCompatible(String voltage1, String voltage2) {
        // This could be enhanced using the library's voltage handling capabilities
        try {
            if (voltage2.contains("-")) {
                String[] range = voltage2.split("-");
                double min = Double.parseDouble(range[0].replaceAll("[^\\d.]", ""));
                double max = Double.parseDouble(range[1].replaceAll("[^\\d.]", ""));
                double v1 = Double.parseDouble(voltage1.replaceAll("[^\\d.]", ""));
                return v1 >= min && v1 <= max;
            }
            return voltage1.equals(voltage2);
        } catch (Exception e) {
            return false;
        }
    }

    private BOM createTemperatureSensorBOM() {
        // Create a BOM for a basic temperature sensor module
        BOM bom = new BOM(
                "TSM-2024-001",           // Production number
                "Environmental Systems",   // Customer name
                "ORD-2024-Q1-123",        // Order number
                new ArrayList<>()          // We'll add entries
        );
        bom.setBomType(BOMType.PCBA);

        // 1. Microcontroller
        BOMEntry mcuEntry = new BOMEntry();
        mcuEntry.setMpn("ATTINY84A-SSU");
        mcuEntry.setManufacturer("Microchip");
        mcuEntry.setDescription("MCU 8-bit ATtiny AVR RISC 8KB Flash");
        mcuEntry.setPkg("SOIC-14");
        mcuEntry.setQty("1");
        mcuEntry.getDesignators().add("U1");
        mcuEntry.getSpecs().put("Core", "AVR");
        mcuEntry.getSpecs().put("Flash", "8KB");
        mcuEntry.getSpecs().put("Voltage", "1.8-5.5V");

        // 2. Temperature Sensor
        BOMEntry sensorEntry = new BOMEntry();
        sensorEntry.setMpn("SHT31-DIS-B");
        sensorEntry.setManufacturer("Sensirion");
        sensorEntry.setDescription("Temperature/Humidity Sensor Digital");
        sensorEntry.setPkg("DFN-8");
        sensorEntry.setQty("1");
        sensorEntry.getDesignators().add("U2");
        sensorEntry.getSpecs().put("Interface", "I2C");
        sensorEntry.getSpecs().put("Accuracy", "±0.2°C");
        sensorEntry.getSpecs().put("Supply", "2.4-5.5V");

        // 3. Voltage Regulator
        BOMEntry regulatorEntry = new BOMEntry();
        regulatorEntry.setMpn("AP2112K-3.3TRG1");
        regulatorEntry.setManufacturer("Diodes Inc.");
        regulatorEntry.setDescription("LDO Voltage Regulator 3.3V");
        regulatorEntry.setPkg("SOT-23-5");
        regulatorEntry.setQty("1");
        regulatorEntry.getDesignators().add("U3");
        regulatorEntry.getSpecs().put("Output", "3.3V");
        regulatorEntry.getSpecs().put("Current", "600mA");

        // 4. Bypass Capacitors
        BOMEntry bypassCapsEntry = new BOMEntry();
        bypassCapsEntry.setMpn("CL10B104KB8NNNC");
        bypassCapsEntry.setManufacturer("Samsung");
        bypassCapsEntry.setDescription("Ceramic Capacitor 100nF 50V X7R");
        bypassCapsEntry.setPkg("0603");
        bypassCapsEntry.setQty("3");
        bypassCapsEntry.getDesignators().addAll(List.of("C1", "C2", "C3"));
        bypassCapsEntry.getSpecs().put("Value", "100nF");
        bypassCapsEntry.getSpecs().put("Voltage", "50V");
        bypassCapsEntry.getSpecs().put("Dielectric", "X7R");

        // 5. Bulk Capacitor
        BOMEntry bulkCapEntry = new BOMEntry();
        bulkCapEntry.setMpn("CL31A106KAHNNNE");
        bulkCapEntry.setManufacturer("Samsung");
        bulkCapEntry.setDescription("Ceramic Capacitor 10µF 25V X5R");
        bulkCapEntry.setPkg("1206");
        bulkCapEntry.setQty("1");
        bulkCapEntry.getDesignators().add("C4");
        bulkCapEntry.getSpecs().put("Value", "10µF");
        bulkCapEntry.getSpecs().put("Voltage", "25V");
        bulkCapEntry.getSpecs().put("Dielectric", "X5R");

        // 6. I2C Pull-up Resistors
        BOMEntry pullupResistorsEntry = new BOMEntry();
        pullupResistorsEntry.setMpn("RC0603FR-074K7L");
        pullupResistorsEntry.setManufacturer("Yageo");
        pullupResistorsEntry.setDescription("Chip Resistor 4.7kΩ 1%");
        pullupResistorsEntry.setPkg("0603");
        pullupResistorsEntry.setQty("2");
        pullupResistorsEntry.getDesignators().addAll(List.of("R1", "R2"));
        pullupResistorsEntry.getSpecs().put("Value", "4.7kΩ");
        pullupResistorsEntry.getSpecs().put("Tolerance", "1%");
        pullupResistorsEntry.getSpecs().put("Power", "0.1W");

        // 7. Power LED and Resistor
        BOMEntry ledEntry = new BOMEntry();
        ledEntry.setMpn("LTST-C191KGKT");
        ledEntry.setManufacturer("Lite-On");
        ledEntry.setDescription("LED Green 2V 20mA");
        ledEntry.setPkg("0603");
        ledEntry.setQty("1");
        ledEntry.getDesignators().add("D1");
        ledEntry.getSpecs().put("Color", "Green");
        ledEntry.getSpecs().put("Current", "20mA");

        BOMEntry ledResistorEntry = new BOMEntry();
        ledResistorEntry.setMpn("RC0603FR-07150RL");
        ledResistorEntry.setManufacturer("Yageo");
        ledResistorEntry.setDescription("Chip Resistor 150Ω 1%");
        ledResistorEntry.setPkg("0603");
        ledResistorEntry.setQty("1");
        ledResistorEntry.getDesignators().add("R3");
        ledResistorEntry.getSpecs().put("Value", "150Ω");
        ledResistorEntry.getSpecs().put("Tolerance", "1%");

        // 8. Programming Header
        BOMEntry headerEntry = new BOMEntry();
        headerEntry.setMpn("20021111-00006T4LF");
        headerEntry.setManufacturer("Amphenol");
        headerEntry.setDescription("Header 2x3 pins");
        headerEntry.setPkg("THT");
        headerEntry.setQty("1");
        headerEntry.getDesignators().add("J1");
        headerEntry.getSpecs().put("Pins", "6");
        headerEntry.getSpecs().put("Rows", "2");

        // Add all entries to BOM
        bom.getBomEntries().addAll(List.of(
                mcuEntry, sensorEntry, regulatorEntry, bypassCapsEntry,
                bulkCapEntry, pullupResistorsEntry, ledEntry,
                ledResistorEntry, headerEntry
        ));

        // Verify BOM structure
        assertEquals("TSM-2024-001", bom.getProductionNo());
        assertEquals(BOMType.PCBA, bom.getBomType());
        assertEquals(9, bom.getBomEntries().size());
        return bom;

    }
}