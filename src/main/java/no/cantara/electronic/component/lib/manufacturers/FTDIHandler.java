package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Handler for FTDI (Future Technology Devices International) components.
 * Implements specific pattern matching for USB interface ICs.
 *
 * Supported product families:
 * - FT232R/RL/RQ - USB to serial UART
 * - FT2232D/H - Dual USB to serial
 * - FT4232H - Quad USB to serial
 * - FT230X/FT231X/FT234X - USB to serial (X series)
 * - FT200XD/FT201X - USB to I2C
 * - FT220X/FT221X - USB to SPI/FT1248
 * - FT240X - USB to FIFO
 * - FT260 - USB to I2C/UART HID
 * - FT600/FT601 - USB 3.0 FIFO
 * - FT51A - USB Programmable MCU
 */
public class FTDIHandler implements ManufacturerHandler {

    // Package code mappings for FTDI devices
    private static final Map<String, String> PACKAGE_CODES = Map.ofEntries(
            Map.entry("R", "SSOP"),        // SSOP-28 (FT232R)
            Map.entry("RL", "SSOP"),       // SSOP-28 (FT232RL)
            Map.entry("RQ", "QFN"),        // QFN-32 (FT232RQ)
            Map.entry("S", "SSOP"),        // SSOP-28 (FT230X, etc.)
            Map.entry("Q", "QFN"),         // QFN package
            Map.entry("H", "LQFP"),        // LQFP package (FT2232H, FT4232H)
            Map.entry("D", "LQFP"),        // LQFP-48 (FT2232D)
            Map.entry("X", "TSSOP"),       // TSSOP package
            Map.entry("T", "TQFP"),        // TQFP package
            Map.entry("BL", "LQFP"),       // LQFP-76 (FT600/FT601)
            Map.entry("BQ", "QFN"),        // QFN-56 (FT600/FT601)
            Map.entry("XS", "TSSOP"),      // TSSOP package
            Map.entry("XQ", "QFN")         // QFN package
    );

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // FT232 series - USB to serial UART (single channel)
        registry.addPattern(ComponentType.IC, "^FT232[A-Z]{0,2}.*");

        // FT2232 series - Dual USB to serial
        registry.addPattern(ComponentType.IC, "^FT2232[A-Z]?.*");

        // FT4232 series - Quad USB to serial
        registry.addPattern(ComponentType.IC, "^FT4232[A-Z]?.*");

        // FT230X/FT231X/FT234X series - X series USB to serial
        registry.addPattern(ComponentType.IC, "^FT23[0-9]X[A-Z]?.*");

        // FT200XD/FT201X series - USB to I2C
        registry.addPattern(ComponentType.IC, "^FT20[0-1]X[A-Z]?.*");

        // FT220X/FT221X series - USB to SPI/FT1248
        registry.addPattern(ComponentType.IC, "^FT22[0-1]X[A-Z]?.*");

        // FT240X series - USB to FIFO (8-bit)
        registry.addPattern(ComponentType.IC, "^FT240X[A-Z]?.*");

        // FT260 series - USB to I2C/UART HID
        registry.addPattern(ComponentType.IC, "^FT260[A-Z]?.*");

        // FT600/FT601 series - USB 3.0 FIFO
        registry.addPattern(ComponentType.IC, "^FT60[0-1][A-Z]?.*");

        // FT51A series - USB Programmable MCU
        registry.addPattern(ComponentType.IC, "^FT51[A-Z].*");

        // FT311D/FT312D series - USB Android Open Accessory
        registry.addPattern(ComponentType.IC, "^FT31[1-2]D.*");

        // FT120 series - USB Host/Device
        registry.addPattern(ComponentType.IC, "^FT12[0-2].*");

        // FT800/FT810/FT81x series - Embedded Video Engine
        registry.addPattern(ComponentType.IC, "^FT8[0-1][0-9].*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(ComponentType.IC);
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();

        // Only support IC type for FTDI parts
        if (type != ComponentType.IC) {
            return false;
        }

        // FT232 series - USB to serial UART
        if (upperMpn.matches("^FT232[A-Z]{0,2}.*")) {
            return true;
        }

        // FT2232 series - Dual USB to serial
        if (upperMpn.matches("^FT2232[A-Z]?.*")) {
            return true;
        }

        // FT4232 series - Quad USB to serial
        if (upperMpn.matches("^FT4232[A-Z]?.*")) {
            return true;
        }

        // FT230X/FT231X/FT234X series - X series USB to serial
        if (upperMpn.matches("^FT23[0-9]X[A-Z]?.*")) {
            return true;
        }

        // FT200XD/FT201X series - USB to I2C
        if (upperMpn.matches("^FT20[0-1]X[A-Z]?.*")) {
            return true;
        }

        // FT220X/FT221X series - USB to SPI/FT1248
        if (upperMpn.matches("^FT22[0-1]X[A-Z]?.*")) {
            return true;
        }

        // FT240X series - USB to FIFO
        if (upperMpn.matches("^FT240X[A-Z]?.*")) {
            return true;
        }

        // FT260 series - USB to I2C/UART HID
        if (upperMpn.matches("^FT260[A-Z]?.*")) {
            return true;
        }

        // FT600/FT601 series - USB 3.0 FIFO
        if (upperMpn.matches("^FT60[0-1][A-Z]?.*")) {
            return true;
        }

        // FT51A series - USB Programmable MCU
        if (upperMpn.matches("^FT51[A-Z].*")) {
            return true;
        }

        // FT311D/FT312D series - USB Android Open Accessory
        if (upperMpn.matches("^FT31[1-2]D.*")) {
            return true;
        }

        // FT120 series - USB Host/Device
        if (upperMpn.matches("^FT12[0-2].*")) {
            return true;
        }

        // FT800/FT810/FT81x series - Embedded Video Engine
        if (upperMpn.matches("^FT8[0-1][0-9].*")) {
            return true;
        }

        // Use handler-specific patterns for other matches
        return patterns.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Remove -REEL, -TUBE, -TRAY suffixes
        String cleanMpn = upperMpn.replaceAll("-?(REEL|TUBE|TRAY)$", "");

        // Handle FT232RL, FT232RQ style (letter after series number)
        if (cleanMpn.matches("^FT232[A-Z]{1,2}$")) {
            String suffix = cleanMpn.substring(5);
            return resolvePackageCode(suffix);
        }

        // Handle FT2232H, FT4232H style
        if (cleanMpn.matches("^FT[24]232[A-Z]$")) {
            String suffix = cleanMpn.substring(cleanMpn.length() - 1);
            return resolvePackageCode(suffix);
        }

        // Handle X-series (FT230XS, FT231XQ, etc.)
        if (cleanMpn.matches("^FT2[0-4][0-9]X[A-Z]?.*")) {
            int xPos = cleanMpn.indexOf('X');
            if (xPos >= 0 && xPos < cleanMpn.length() - 1) {
                String suffix = cleanMpn.substring(xPos + 1);
                // Handle -R suffix (indicating REEL was already stripped)
                suffix = suffix.replaceAll("-R$", "");
                if (!suffix.isEmpty()) {
                    return resolvePackageCode(suffix.substring(0, Math.min(suffix.length(), 2)));
                }
            }
        }

        // Handle FT260S, FT260Q style
        if (cleanMpn.matches("^FT260[A-Z].*")) {
            String suffix = cleanMpn.substring(5, Math.min(6, cleanMpn.length()));
            return resolvePackageCode(suffix);
        }

        // Handle FT600/FT601 (FT600BL, FT601BQ)
        if (cleanMpn.matches("^FT60[0-1][A-Z]{1,2}.*")) {
            String suffix = cleanMpn.substring(5, Math.min(7, cleanMpn.length()));
            return resolvePackageCode(suffix);
        }

        // Handle suffix after hyphen (e.g., FT4232H-REEL becomes FT4232H)
        int hyphen = cleanMpn.indexOf('-');
        if (hyphen > 0) {
            return extractPackageCode(cleanMpn.substring(0, hyphen));
        }

        return "";
    }

    private String resolvePackageCode(String suffix) {
        if (suffix == null || suffix.isEmpty()) return "";

        // Try exact match first
        if (PACKAGE_CODES.containsKey(suffix)) {
            return PACKAGE_CODES.get(suffix);
        }

        // Try first two characters
        if (suffix.length() >= 2) {
            String twoChar = suffix.substring(0, 2);
            if (PACKAGE_CODES.containsKey(twoChar)) {
                return PACKAGE_CODES.get(twoChar);
            }
        }

        // Try first character
        if (PACKAGE_CODES.containsKey(suffix.substring(0, 1))) {
            return PACKAGE_CODES.get(suffix.substring(0, 1));
        }

        return suffix;
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // FT232 series
        if (upperMpn.matches("^FT232[A-Z]{0,2}.*")) {
            return "FT232";
        }

        // FT2232 series
        if (upperMpn.matches("^FT2232[A-Z]?.*")) {
            return "FT2232";
        }

        // FT4232 series
        if (upperMpn.matches("^FT4232[A-Z]?.*")) {
            return "FT4232";
        }

        // X-series: FT230X, FT231X, FT234X
        if (upperMpn.matches("^FT230X.*")) {
            return "FT230X";
        }
        if (upperMpn.matches("^FT231X.*")) {
            return "FT231X";
        }
        if (upperMpn.matches("^FT234X.*")) {
            return "FT234X";
        }

        // USB to I2C: FT200XD, FT201X
        if (upperMpn.matches("^FT200X.*")) {
            return "FT200X";
        }
        if (upperMpn.matches("^FT201X.*")) {
            return "FT201X";
        }

        // USB to SPI: FT220X, FT221X
        if (upperMpn.matches("^FT220X.*")) {
            return "FT220X";
        }
        if (upperMpn.matches("^FT221X.*")) {
            return "FT221X";
        }

        // FT240X series
        if (upperMpn.matches("^FT240X.*")) {
            return "FT240X";
        }

        // FT260 series
        if (upperMpn.matches("^FT260.*")) {
            return "FT260";
        }

        // FT600/FT601 series
        if (upperMpn.matches("^FT600.*")) {
            return "FT600";
        }
        if (upperMpn.matches("^FT601.*")) {
            return "FT601";
        }

        // FT51A series
        if (upperMpn.matches("^FT51A.*")) {
            return "FT51A";
        }

        // FT311D/FT312D series
        if (upperMpn.matches("^FT311D.*")) {
            return "FT311D";
        }
        if (upperMpn.matches("^FT312D.*")) {
            return "FT312D";
        }

        // FT800/FT81x series (Embedded Video Engine)
        if (upperMpn.matches("^FT800.*")) {
            return "FT800";
        }
        if (upperMpn.matches("^FT81[0-9].*")) {
            // Extract FT810, FT811, FT812, etc.
            return upperMpn.substring(0, 5);
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        // Same series with different package is typically a valid replacement
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        if (series1.isEmpty() || series2.isEmpty()) {
            return false;
        }

        // Same series is always compatible (just different package variants)
        if (series1.equals(series2)) {
            return true;
        }

        // FT232R and FT232RL are part of the same family
        if (isFT232Family(series1) && isFT232Family(series2)) {
            return true;
        }

        // X-series UART variants can replace each other (similar functionality)
        // FT230X (basic) / FT231X (with RTS/CTS) / FT234X (with RTS/CTS/DTR/DSR)
        if (isXSeriesUart(series1) && isXSeriesUart(series2)) {
            // FT234X can replace FT231X or FT230X (more pins = superset)
            // FT231X can replace FT230X
            // But not the reverse
            return canReplaceXSeriesUart(series1, series2);
        }

        // FT2232 and FT4232 are not interchangeable (different channel count)
        // FT600 and FT601 are not interchangeable (different bus width)

        return false;
    }

    private boolean isFT232Family(String series) {
        return "FT232".equals(series);
    }

    private boolean isXSeriesUart(String series) {
        return "FT230X".equals(series) || "FT231X".equals(series) || "FT234X".equals(series);
    }

    private boolean canReplaceXSeriesUart(String replacement, String original) {
        // FT234X (4 control lines) can replace FT231X (2) or FT230X (0)
        // FT231X (2 control lines) can replace FT230X (0)
        // But downgrade is not a valid replacement
        int replacementLevel = getXSeriesUartLevel(replacement);
        int originalLevel = getXSeriesUartLevel(original);
        return replacementLevel >= originalLevel;
    }

    private int getXSeriesUartLevel(String series) {
        return switch (series) {
            case "FT230X" -> 0;  // Basic UART
            case "FT231X" -> 1;  // UART with RTS/CTS
            case "FT234X" -> 2;  // UART with RTS/CTS/DTR/DSR
            default -> -1;
        };
    }

    /**
     * Get the USB interface type for the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the USB interface type (e.g., "UART", "I2C", "SPI", "FIFO")
     */
    public String getInterfaceType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        return switch (series) {
            case "FT232", "FT2232", "FT4232", "FT230X", "FT231X", "FT234X" -> "UART";
            case "FT200X", "FT201X" -> "I2C";
            case "FT220X", "FT221X" -> "SPI";
            case "FT240X", "FT600", "FT601" -> "FIFO";
            case "FT260" -> "I2C/UART";
            case "FT51A" -> "USB MCU";
            case "FT311D", "FT312D" -> "Android AOA";
            default -> {
                if (series.startsWith("FT8")) yield "EVE";  // Embedded Video Engine
                yield "";
            }
        };
    }

    /**
     * Get the number of channels for the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the number of channels (1, 2, 4, or 0 if unknown)
     */
    public int getChannelCount(String mpn) {
        if (mpn == null || mpn.isEmpty()) return 0;

        String series = extractSeries(mpn);
        return switch (series) {
            case "FT232", "FT230X", "FT231X", "FT234X", "FT200X", "FT201X",
                 "FT220X", "FT221X", "FT240X", "FT260" -> 1;
            case "FT2232", "FT600" -> 2;
            case "FT4232" -> 4;
            case "FT601" -> 1;  // Single 32-bit channel
            default -> 0;
        };
    }

    /**
     * Get the USB version supported by the given MPN.
     *
     * @param mpn the manufacturer part number
     * @return the USB version (e.g., "USB 2.0 Full-Speed", "USB 2.0 High-Speed", "USB 3.0")
     */
    public String getUsbVersion(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String series = extractSeries(mpn);
        return switch (series) {
            case "FT232", "FT230X", "FT231X", "FT234X", "FT200X", "FT201X",
                 "FT220X", "FT221X", "FT240X", "FT260" -> "USB 2.0 Full-Speed";
            case "FT2232", "FT4232" -> "USB 2.0 High-Speed";
            case "FT600", "FT601" -> "USB 3.0 Super-Speed";
            case "FT51A" -> "USB 2.0 Full-Speed";
            default -> "";
        };
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
