package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Set;
import java.util.Collections;

/**
 * Handler for Sensirion environmental sensors.
 *
 * Sensirion Product Families:
 * - SHTxx - Humidity & Temperature sensors (SHT30, SHT31, SHT35, SHT40, SHT41, SHT45, SHT85)
 * - SGPxx - Gas sensors (SGP30, SGP40, SGP41)
 * - SCDxx - CO2 sensors (SCD30, SCD40, SCD41)
 * - SFAxx - Formaldehyde sensors (SFA30)
 * - SPSxx - Particulate matter sensors (SPS30)
 * - STSxx - Temperature only sensors (STS30, STS31, STS40)
 * - SLFxxxx - Liquid flow sensors (SLF3S-1300F)
 * - SDPxxx - Differential pressure sensors (SDP31, SDP810)
 *
 * MPN Format Examples:
 * - SHT31-DIS-B (Humidity/Temp, DIS package, B accuracy)
 * - SHT40-AD1B-R2 (Humidity/Temp, AD1B variant, R2 reel)
 * - SGP40-D-R4 (Gas sensor, D variant, R4 reel)
 * - SCD40-D-R2 (CO2 sensor, D variant, R2 reel)
 *
 * Package Codes:
 * - DIS: Digital sensor, tape & reel
 * - AD1B: Analog variant B
 * - D: Standard digital
 * - P: Prototype/Pin header
 */
public class SensirionHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Humidity & Temperature Sensors (SHTxx)
        registry.addPattern(ComponentType.HUMIDITY_SENSOR, "^SHT[0-9]{2}.*");
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^SHT[0-9]{2}.*");
        registry.addPattern(ComponentType.SENSOR, "^SHT[0-9]{2}.*");

        // Temperature Only Sensors (STSxx)
        registry.addPattern(ComponentType.TEMPERATURE_SENSOR, "^STS[0-9]{2}.*");
        registry.addPattern(ComponentType.SENSOR, "^STS[0-9]{2}.*");

        // Gas Sensors (SGPxx) - VOC/Air Quality
        registry.addPattern(ComponentType.SENSOR, "^SGP[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SGP[0-9]{2}.*");

        // CO2 Sensors (SCDxx)
        registry.addPattern(ComponentType.SENSOR, "^SCD[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SCD[0-9]{2}.*");

        // Formaldehyde Sensors (SFAxx)
        registry.addPattern(ComponentType.SENSOR, "^SFA[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SFA[0-9]{2}.*");

        // Particulate Matter Sensors (SPSxx)
        registry.addPattern(ComponentType.SENSOR, "^SPS[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^SPS[0-9]{2}.*");

        // Liquid Flow Sensors (SLFxxxx)
        registry.addPattern(ComponentType.SENSOR, "^SLF[0-9A-Z]+.*");
        registry.addPattern(ComponentType.SENSOR_FLOW, "^SLF[0-9A-Z]+.*");

        // Differential Pressure Sensors (SDPxxx)
        registry.addPattern(ComponentType.PRESSURE_SENSOR, "^SDP[0-9]{2,3}.*");
        registry.addPattern(ComponentType.SENSOR, "^SDP[0-9]{2,3}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.SENSOR,
            ComponentType.HUMIDITY_SENSOR,
            ComponentType.TEMPERATURE_SENSOR,
            ComponentType.PRESSURE_SENSOR,
            ComponentType.SENSOR_FLOW,
            ComponentType.IC
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Handle hyphenated MPNs: SHT31-DIS-B, SHT40-AD1B-R2, SGP40-D-R4
        int firstDash = upperMpn.indexOf('-');
        if (firstDash > 0) {
            String afterBase = upperMpn.substring(firstDash + 1);
            int secondDash = afterBase.indexOf('-');

            // Extract first part after dash (package/variant code)
            String packagePart = secondDash > 0 ? afterBase.substring(0, secondDash) : afterBase;

            // Map known package codes
            return switch (packagePart) {
                case "DIS" -> "DFN";           // Digital sensor, DFN package
                case "DIS-B" -> "DFN";
                case "ARP" -> "DFN";           // ARP variant
                case "AD1B" -> "DFN";          // Analog variant
                case "AD1F" -> "DFN";
                case "BD1B" -> "DFN";
                case "D" -> "DFN";             // Standard digital
                case "P" -> "PIN";             // Pin header (development)
                case "B" -> "DFN";             // B accuracy grade
                case "F" -> "DFN";             // F grade
                default -> {
                    // For flow sensors (SLF3S-1300F), return the variant
                    if (packagePart.matches("[0-9]+[A-Z]?")) {
                        yield packagePart;
                    }
                    yield packagePart;
                }
            };
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract base series before any dash
        int dashIndex = upperMpn.indexOf('-');
        String basePart = dashIndex > 0 ? upperMpn.substring(0, dashIndex) : upperMpn;

        // Group by series family
        if (basePart.startsWith("SHT3")) return "SHT3x";
        if (basePart.startsWith("SHT4")) return "SHT4x";
        if (basePart.startsWith("SHT8")) return "SHT8x";
        if (basePart.startsWith("STS3")) return "STS3x";
        if (basePart.startsWith("STS4")) return "STS4x";
        if (basePart.startsWith("SGP3")) return "SGP3x";
        if (basePart.startsWith("SGP4")) return "SGP4x";
        if (basePart.startsWith("SCD3")) return "SCD3x";
        if (basePart.startsWith("SCD4")) return "SCD4x";
        if (basePart.startsWith("SFA3")) return "SFA3x";
        if (basePart.startsWith("SPS3")) return "SPS3x";
        if (basePart.startsWith("SLF")) return "SLFxx";
        if (basePart.startsWith("SDP3")) return "SDP3x";
        if (basePart.startsWith("SDP8")) return "SDP8x";

        // Return the base part number
        return basePart;
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Same series are typically compatible
        if (series1.equals(series2)) {
            // Check accuracy grade compatibility
            String grade1 = extractAccuracyGrade(mpn1);
            String grade2 = extractAccuracyGrade(mpn2);

            return isCompatibleAccuracyGrade(grade1, grade2);
        }

        // Check for known compatible series pairs
        return isCompatibleSeries(series1, series2);
    }

    /**
     * Extract accuracy grade from MPN (A, B, F, etc.)
     */
    private String extractAccuracyGrade(String mpn) {
        String upperMpn = mpn.toUpperCase();

        // Look for grade suffix after package code
        // SHT31-DIS-B -> B
        // SHT40-AD1B-R2 -> B (within AD1B)
        if (upperMpn.contains("-B") || upperMpn.contains("1B")) return "B";
        if (upperMpn.contains("-F") || upperMpn.contains("1F")) return "F";
        if (upperMpn.contains("-A") || upperMpn.contains("1A")) return "A";

        return "";
    }

    /**
     * Check if accuracy grades are compatible.
     * Higher accuracy can replace lower accuracy.
     * Grade ordering: A (highest) > B > F (lowest)
     */
    private boolean isCompatibleAccuracyGrade(String grade1, String grade2) {
        if (grade1.equals(grade2)) return true;
        if (grade1.isEmpty() || grade2.isEmpty()) return true;

        // A can replace B and F
        if (grade1.equals("A")) return true;
        // B can replace F
        if (grade1.equals("B") && grade2.equals("F")) return true;

        return false;
    }

    /**
     * Check for compatible series pairs within Sensirion product families.
     */
    private boolean isCompatibleSeries(String series1, String series2) {
        // SHT3x family compatibility (SHT30, SHT31, SHT35)
        // SHT35 is higher accuracy than SHT31, SHT30
        if (series1.equals("SHT3x") && series2.equals("SHT3x")) return true;

        // SHT4x family compatibility (SHT40, SHT41, SHT45)
        // SHT45 is higher accuracy than SHT41, SHT40
        if (series1.equals("SHT4x") && series2.equals("SHT4x")) return true;

        // SHT4x can replace SHT3x (next generation)
        if (series1.equals("SHT4x") && series2.equals("SHT3x")) return true;

        // STS3x and STS4x temperature sensors
        if (series1.equals("STS4x") && series2.equals("STS3x")) return true;

        // SGP4x can replace SGP3x (improved gas sensor)
        if (series1.equals("SGP4x") && series2.equals("SGP3x")) return true;

        // SCD4x can replace SCD3x (miniaturized CO2 sensor)
        if (series1.equals("SCD4x") && series2.equals("SCD3x")) return true;

        // SDP3x and SDP8x are different pressure ranges, generally not interchangeable
        // unless explicitly checking pressure range compatibility

        return false;
    }

    /**
     * Extract interface type from MPN.
     */
    private String extractInterface(String mpn) {
        String upperMpn = mpn.toUpperCase();

        // Most Sensirion sensors use I2C
        // Some variants support different addresses
        if (upperMpn.contains("-AD")) return "I2C-ALT"; // Alternate address
        return "I2C"; // Default
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
