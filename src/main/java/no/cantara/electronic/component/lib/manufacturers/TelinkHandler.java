package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for Telink Semiconductor components.
 * Telink specializes in BLE and Zigbee SoCs.
 *
 * Product families:
 * - TLSR82xx series (BLE 5.0): TLSR8251, TLSR8258
 * - TLSR92xx series (BLE 5.2): TLSR9218
 * - TLSR825x (Zigbee 3.0)
 * - B85/B87/B91 series (newer naming convention)
 *
 * MPN patterns:
 * - TLSR[0-9]{4}[A-Z]* (legacy naming)
 * - B[0-9]{2}[A-Z]* (newer naming)
 *
 * Package codes: QFN-32, QFN-48, QFN-24
 */
public class TelinkHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // TLSR82xx Series - BLE 5.0
        registry.addPattern(ComponentType.IC, "^TLSR82[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^TLSR8251.*");          // TLSR8251 specific
        registry.addPattern(ComponentType.IC, "^TLSR8258.*");          // TLSR8258 specific
        registry.addPattern(ComponentType.IC, "^TLSR8253.*");          // TLSR8253 specific
        registry.addPattern(ComponentType.IC, "^TLSR8261.*");          // TLSR8261 specific
        registry.addPattern(ComponentType.IC, "^TLSR8269.*");          // TLSR8269 specific

        // TLSR92xx Series - BLE 5.2
        registry.addPattern(ComponentType.IC, "^TLSR92[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^TLSR9218.*");          // TLSR9218 specific

        // TLSR825x Series - Zigbee 3.0
        registry.addPattern(ComponentType.IC, "^TLSR825[0-9].*");

        // B-series (newer naming) - BLE/Zigbee/Thread
        registry.addPattern(ComponentType.IC, "^B85[A-Z]*.*");         // B85 series
        registry.addPattern(ComponentType.IC, "^B87[A-Z]*.*");         // B87 series
        registry.addPattern(ComponentType.IC, "^B91[A-Z]*.*");         // B91 series (RISC-V)
        registry.addPattern(ComponentType.IC, "^B92[A-Z]*.*");         // B92 series

        // Generic TLSR pattern for any Telink SoC
        registry.addPattern(ComponentType.IC, "^TLSR[0-9]{4}.*");

        // Generic B-series pattern
        registry.addPattern(ComponentType.IC, "^B[0-9]{2}[A-Z]*.*");

        // Register same patterns for MICROCONTROLLER type
        registry.addPattern(ComponentType.MICROCONTROLLER, "^TLSR82[0-9]{2}.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^TLSR92[0-9]{2}.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^TLSR825[0-9].*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^TLSR[0-9]{4}.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^B85[A-Z]*.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^B87[A-Z]*.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^B91[A-Z]*.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^B92[A-Z]*.*");
        registry.addPattern(ComponentType.MICROCONTROLLER, "^B[0-9]{2}[A-Z]*.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.IC,
                ComponentType.MICROCONTROLLER
        );
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Check for explicit package suffix patterns
        // Format: TLSR8258F512ET32 where last digits indicate package
        if (upperMpn.contains("-")) {
            String[] parts = upperMpn.split("-");
            if (parts.length > 1) {
                String suffix = parts[parts.length - 1];
                return decodePackageSuffix(suffix);
            }
        }

        // Check for package code embedded in part number
        // Common patterns: ET32 (QFN-32), ET48 (QFN-48), ET24 (QFN-24)
        if (upperMpn.matches(".*ET32.*") || upperMpn.endsWith("32")) {
            return "QFN-32";
        }
        if (upperMpn.matches(".*ET48.*") || upperMpn.endsWith("48")) {
            return "QFN-48";
        }
        if (upperMpn.matches(".*ET24.*") || upperMpn.endsWith("24")) {
            return "QFN-24";
        }

        // Try to extract from suffix letters
        if (upperMpn.contains("Q")) {
            // Q typically indicates QFN package
            return "QFN";
        }

        return "";
    }

    private String decodePackageSuffix(String suffix) {
        return switch (suffix) {
            case "Q32", "QFN32" -> "QFN-32";
            case "Q48", "QFN48" -> "QFN-48";
            case "Q24", "QFN24" -> "QFN-24";
            default -> {
                if (suffix.endsWith("32")) yield "QFN-32";
                if (suffix.endsWith("48")) yield "QFN-48";
                if (suffix.endsWith("24")) yield "QFN-24";
                yield suffix;
            }
        };
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // TLSR series - extract first 8 characters or until non-alphanumeric
        if (upperMpn.startsWith("TLSR")) {
            StringBuilder series = new StringBuilder();
            for (int i = 0; i < Math.min(8, upperMpn.length()); i++) {
                char c = upperMpn.charAt(i);
                if (Character.isLetterOrDigit(c)) {
                    series.append(c);
                } else {
                    break;
                }
            }
            return series.toString();
        }

        // B-series - extract B + 2 digits
        if (upperMpn.matches("^B[0-9]{2}.*")) {
            return upperMpn.substring(0, 3); // e.g., "B91"
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Same series are potential replacements
        if (series1.equals(series2)) return true;

        // Check for compatible series upgrades
        return isCompatibleSeries(series1, series2);
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        // TLSR8258 can replace TLSR8251 (more features)
        if (series1.equals("TLSR8258") && series2.equals("TLSR8251")) return true;

        // TLSR8269 can replace TLSR8258 (more features)
        if (series1.equals("TLSR8269") && series2.equals("TLSR8258")) return true;

        // B91 (RISC-V) is compatible within its own series
        // B85/B87 are BLE 5.0 compatible
        if (series1.startsWith("B8") && series2.startsWith("B8")) {
            // B87 can replace B85
            if (series1.equals("B87") && series2.equals("B85")) return true;
        }

        // TLSR92xx can replace TLSR82xx (BLE 5.2 vs BLE 5.0)
        if (series1.startsWith("TLSR92") && series2.startsWith("TLSR82")) return true;

        return false;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Explicit matching for supported types
        if (type == ComponentType.IC || type == ComponentType.MICROCONTROLLER) {
            // TLSR series
            if (upperMpn.matches("^TLSR[0-9]{4}.*")) {
                return true;
            }
            // B-series
            if (upperMpn.matches("^B[0-9]{2}[A-Z]*.*")) {
                return true;
            }
        }

        // Fall back to registry matching for current handler
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }
}
