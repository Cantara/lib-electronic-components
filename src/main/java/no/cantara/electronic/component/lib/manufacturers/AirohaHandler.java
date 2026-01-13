package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for Airoha Technology (MediaTek subsidiary) Bluetooth audio SoCs.
 *
 * Airoha specializes in Bluetooth audio solutions for TWS earbuds, ANC headphones,
 * and premium audio applications.
 *
 * Product Series:
 * - AB155x: TWS earbuds (AB1552, AB1558)
 * - AB156x: ANC earbuds (AB1562, AB1568)
 * - AB157x: Premium audio
 * - AB158x: Ultra-low power
 *
 * MPN Pattern: AB15[5-8][0-9][A-Z]*
 * Package codes: QFN, BGA, CSP
 */
public class AirohaHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // AB155x Series - TWS earbuds
        registry.addPattern(ComponentType.IC, "^AB155[0-9].*");     // General AB155x pattern
        registry.addPattern(ComponentType.IC, "^AB1552.*");         // AB1552 specific
        registry.addPattern(ComponentType.IC, "^AB1558.*");         // AB1558 specific

        // AB156x Series - ANC earbuds
        registry.addPattern(ComponentType.IC, "^AB156[0-9].*");     // General AB156x pattern
        registry.addPattern(ComponentType.IC, "^AB1562.*");         // AB1562 specific
        registry.addPattern(ComponentType.IC, "^AB1563.*");         // AB1563 specific
        registry.addPattern(ComponentType.IC, "^AB1568.*");         // AB1568 specific

        // AB157x Series - Premium audio
        registry.addPattern(ComponentType.IC, "^AB157[0-9].*");     // General AB157x pattern
        registry.addPattern(ComponentType.IC, "^AB1570.*");         // AB1570 specific
        registry.addPattern(ComponentType.IC, "^AB1575.*");         // AB1575 specific

        // AB158x Series - Ultra-low power
        registry.addPattern(ComponentType.IC, "^AB158[0-9].*");     // General AB158x pattern
        registry.addPattern(ComponentType.IC, "^AB1580.*");         // AB1580 specific
        registry.addPattern(ComponentType.IC, "^AB1585.*");         // AB1585 specific

        // Generic Airoha pattern for all AB15xx Bluetooth audio SoCs
        registry.addPattern(ComponentType.IC, "^AB15[5-8][0-9].*"); // All AB155x-AB158x
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        // Use Set.of() per codebase conventions - immutable and no duplicates
        return Set.of(ComponentType.IC);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();
        String[] parts = upperMpn.split("-");

        if (parts.length > 1) {
            String suffix = parts[parts.length - 1];

            return switch (suffix) {
                case "QFN" -> "QFN";
                case "BGA" -> "BGA";
                case "CSP" -> "CSP";
                case "WLCSP" -> "Wafer Level CSP";
                case "FCBGA" -> "Flip-Chip BGA";
                default -> {
                    if (suffix.contains("QFN")) yield "QFN";
                    if (suffix.contains("BGA")) yield "BGA";
                    if (suffix.contains("CSP")) yield "CSP";
                    yield suffix;
                }
            };
        }

        // Check for inline package indicators without hyphen
        if (upperMpn.contains("QFN")) return "QFN";
        if (upperMpn.contains("BGA")) return "BGA";
        if (upperMpn.contains("CSP")) return "CSP";

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract base series (AB15xx)
        StringBuilder series = new StringBuilder();
        boolean inSeries = false;

        for (int i = 0; i < upperMpn.length(); i++) {
            char c = upperMpn.charAt(i);

            if (c == '-') break;

            if (Character.isLetter(c) || Character.isDigit(c)) {
                series.append(c);
                inSeries = true;

                // Stop after 6 characters (e.g., AB1562)
                if (series.length() >= 6 && Character.isDigit(c)) {
                    break;
                }
            } else if (inSeries) {
                break;
            }
        }

        String result = series.toString();

        // Validate it's an AB15xx pattern
        if (result.matches("^AB15[5-8][0-9].*")) {
            // Return first 6 characters (AB15xx) as the series
            return result.length() >= 6 ? result.substring(0, 6) : result;
        }

        return result;
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be from same product family
        if (!isCompatibleSeries(series1, series2)) {
            return false;
        }

        // Check Bluetooth version compatibility
        String btVersion1 = extractBluetoothVersion(mpn1);
        String btVersion2 = extractBluetoothVersion(mpn2);

        if (!btVersion1.isEmpty() && !btVersion2.isEmpty()) {
            if (!isCompatibleBluetoothVersion(btVersion1, btVersion2)) {
                return false;
            }
        }

        // Check feature compatibility
        return isCompatibleFeatures(mpn1, mpn2);
    }

    private boolean isCompatibleSeries(String series1, String series2) {
        if (series1.equals(series2)) return true;

        // AB155x family (TWS earbuds) compatibility
        if (series1.startsWith("AB155") && series2.startsWith("AB155")) {
            // AB1558 can typically replace AB1552 (higher model number = more features)
            return compareSeries(series1, series2) >= 0;
        }

        // AB156x family (ANC earbuds) compatibility
        if (series1.startsWith("AB156") && series2.startsWith("AB156")) {
            return compareSeries(series1, series2) >= 0;
        }

        // AB157x family (Premium audio) compatibility
        if (series1.startsWith("AB157") && series2.startsWith("AB157")) {
            return compareSeries(series1, series2) >= 0;
        }

        // AB158x family (Ultra-low power) compatibility
        if (series1.startsWith("AB158") && series2.startsWith("AB158")) {
            return compareSeries(series1, series2) >= 0;
        }

        // Cross-series compatibility: Higher series can typically replace lower
        // AB158x > AB157x > AB156x > AB155x (in terms of capabilities)
        int seriesNum1 = extractSeriesNumber(series1);
        int seriesNum2 = extractSeriesNumber(series2);

        // Only allow replacement within related product lines
        // e.g., AB1562 (ANC) shouldn't replace AB1552 (basic TWS) as they're different products
        return false;
    }

    private int compareSeries(String series1, String series2) {
        try {
            int num1 = extractSeriesNumber(series1);
            int num2 = extractSeriesNumber(series2);
            return Integer.compare(num1, num2);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int extractSeriesNumber(String series) {
        if (series == null || series.length() < 6) return 0;
        try {
            // Extract numeric part from AB15xx (positions 2-5)
            return Integer.parseInt(series.substring(2, 6));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String extractBluetoothVersion(String mpn) {
        if (mpn == null) return "";
        String upper = mpn.toUpperCase();

        // Common Bluetooth version indicators
        if (upper.contains("BT5.3")) return "5.3";
        if (upper.contains("BT5.2")) return "5.2";
        if (upper.contains("BT5.1")) return "5.1";
        if (upper.contains("BT5.0") || upper.contains("BT5")) return "5.0";
        if (upper.contains("BT4.2")) return "4.2";
        if (upper.contains("BT4.1")) return "4.1";
        if (upper.contains("BT4.0") || upper.contains("BT4")) return "4.0";

        // Airoha AB15xx series typically supports BT5.x
        // Series-based inference
        if (upper.startsWith("AB158")) return "5.3"; // Ultra-low power, latest
        if (upper.startsWith("AB157")) return "5.2"; // Premium audio
        if (upper.startsWith("AB156")) return "5.2"; // ANC
        if (upper.startsWith("AB155")) return "5.0"; // TWS basic

        return "";
    }

    private boolean isCompatibleBluetoothVersion(String version1, String version2) {
        try {
            double v1 = Double.parseDouble(version1);
            double v2 = Double.parseDouble(version2);
            // Higher Bluetooth version can typically replace lower version
            return v1 >= v2;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isCompatibleFeatures(String mpn1, String mpn2) {
        String upper1 = mpn1.toUpperCase();
        String upper2 = mpn2.toUpperCase();

        // ANC (Active Noise Cancellation) feature check
        boolean hasAnc1 = upper1.contains("ANC") || upper1.startsWith("AB156");
        boolean hasAnc2 = upper2.contains("ANC") || upper2.startsWith("AB156");

        // If target requires ANC, replacement must have ANC
        if (hasAnc2 && !hasAnc1) return false;

        // Premium audio features
        boolean hasPremium1 = upper1.startsWith("AB157") || upper1.contains("HIFI");
        boolean hasPremium2 = upper2.startsWith("AB157") || upper2.contains("HIFI");

        if (hasPremium2 && !hasPremium1) return false;

        return true;
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
