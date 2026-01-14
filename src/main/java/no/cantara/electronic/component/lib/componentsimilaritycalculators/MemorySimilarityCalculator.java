package no.cantara.electronic.component.lib.componentsimilaritycalculators;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadata;
import no.cantara.electronic.component.lib.similarity.config.ComponentTypeMetadataRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Similarity calculator for memory devices (EEPROM, Flash, SRAM).
 *
 * Uses equivalent groups (24LC256≈AT24C256, W25Q32 variants) and characteristics comparison.
 * Metadata infrastructure available for spec-based comparison when characteristics are known.
 */
public class MemorySimilarityCalculator implements ComponentSimilarityCalculator {
    private static final Logger logger = LoggerFactory.getLogger(MemorySimilarityCalculator.class);
    private final ComponentTypeMetadataRegistry metadataRegistry;

    private static final double HIGH_SIMILARITY = 0.9;
    private static final double MEDIUM_SIMILARITY = 0.7;
    private static final double LOW_SIMILARITY = 0.3;

    private static final Map<String, Set<String>> EQUIVALENT_GROUPS = new HashMap<>();
    static {
        // I2C EEPROM equivalents
        EQUIVALENT_GROUPS.put("24LC256", Set.of("24LC256", "24LC256A", "24LC256B", "24LC256C",
                "AT24C256", "AT24C256A", "AT24C256B", "AT24C256C",
                "M24C256", "CAT24C256"));
        EQUIVALENT_GROUPS.put("24LC512", Set.of("24LC512", "24LC512A",
                "AT24C512", "AT24C512A",
                "M24C512", "CAT24C512"));
        EQUIVALENT_GROUPS.put("24LC1024", Set.of("24LC1024", "24LC1024A",
                "AT24C1024", "AT24C1024A",
                "M24C1024", "CAT24C1024"));

        // SPI EEPROM equivalents
        EQUIVALENT_GROUPS.put("25LC256", Set.of("25LC256", "25AA256",
                "AT25256", "M95256",
                "SST25VF256"));
        EQUIVALENT_GROUPS.put("25LC512", Set.of("25LC512", "25AA512",
                "AT25512", "M95512",
                "SST25VF512"));

        // SPI Flash equivalents
        EQUIVALENT_GROUPS.put("W25Q32", Set.of("W25Q32JV", "W25Q32JW", "W25Q32FW",
                "MX25L3233F", "S25FL032P",
                "AT25SF321", "IS25LP032"));
        EQUIVALENT_GROUPS.put("W25Q64", Set.of("W25Q64JV", "W25Q64JW", "W25Q64FW",
                "MX25L6433F", "S25FL064P",
                "AT25SF641", "IS25LP064"));
        EQUIVALENT_GROUPS.put("W25Q128", Set.of("W25Q128JV", "W25Q128JW", "W25Q128FW",
                "MX25L12833F", "S25FL128",
                "AT25SF128A", "IS25LP128"));
    }

    private static final Map<String, String> PACKAGE_CODES = new HashMap<>();
    static {
        // Through-hole packages
        PACKAGE_CODES.put("P", "DIP");
        PACKAGE_CODES.put("N", "DIP");

        // Surface mount packages
        PACKAGE_CODES.put("SN", "SOIC");
        PACKAGE_CODES.put("SM", "SOIC");
        PACKAGE_CODES.put("ST", "TSSOP");
        PACKAGE_CODES.put("SE", "SOIC-Wide");
        PACKAGE_CODES.put("MF", "DFN");
        PACKAGE_CODES.put("MN", "WDFN");
        PACKAGE_CODES.put("MS", "MSOP");

        // Flash-specific packages
        PACKAGE_CODES.put("IQ", "SOIC");
        PACKAGE_CODES.put("IG", "SOIC");
        PACKAGE_CODES.put("TR", "TSSOP");
        PACKAGE_CODES.put("FN", "WSON");
        PACKAGE_CODES.put("UN", "WSON");
        PACKAGE_CODES.put("DF", "WSOP");
    }

    private static final Map<String, MemoryCharacteristics> CHARACTERISTICS = new HashMap<>();
    static {
        // I2C EEPROMs
        MemoryCharacteristics i2c256k = new MemoryCharacteristics(256, "EEPROM", "I2C", 1.7, 5.5);
        CHARACTERISTICS.put("24LC256", i2c256k);
        CHARACTERISTICS.put("24LC256A", i2c256k);
        CHARACTERISTICS.put("24LC256B", i2c256k);
        CHARACTERISTICS.put("24LC256C", i2c256k);
        CHARACTERISTICS.put("AT24C256", i2c256k);
        CHARACTERISTICS.put("AT24C256A", i2c256k);
        CHARACTERISTICS.put("AT24C256B", i2c256k);
        CHARACTERISTICS.put("AT24C256C", i2c256k);
        CHARACTERISTICS.put("M24C256", i2c256k);
        CHARACTERISTICS.put("CAT24C256", i2c256k);

        // SPI Flash devices
        MemoryCharacteristics spiFlash32m = new MemoryCharacteristics(32768, "Flash", "SPI", 2.7, 3.6);
        CHARACTERISTICS.put("W25Q32JV", spiFlash32m);
        CHARACTERISTICS.put("W25Q32FW", spiFlash32m);
        CHARACTERISTICS.put("MX25L3233F", spiFlash32m);

        MemoryCharacteristics spiFlash64m = new MemoryCharacteristics(65536, "Flash", "SPI", 2.7, 3.6);
        CHARACTERISTICS.put("W25Q64JV", spiFlash64m);
        CHARACTERISTICS.put("W25Q64FW", spiFlash64m);
        CHARACTERISTICS.put("MX25L6433F", spiFlash64m);

        MemoryCharacteristics spiFlash128m = new MemoryCharacteristics(131072, "Flash", "SPI", 2.7, 3.6);
        CHARACTERISTICS.put("W25Q128JV", spiFlash128m);
        CHARACTERISTICS.put("W25Q128FW", spiFlash128m);
        CHARACTERISTICS.put("MX25L12833F", spiFlash128m);
    }

    public MemorySimilarityCalculator() {
        this.metadataRegistry = ComponentTypeMetadataRegistry.getInstance();
    }

    @Override
    public boolean isApplicable(ComponentType type) {
        if (type == null) return true;  // Handle unrecognized memory devices
        return type == ComponentType.MEMORY ||
                type.name().startsWith("MEMORY_") ||
                type.getBaseType() == ComponentType.MEMORY;
    }

    @Override
    public double calculateSimilarity(String mpn1, String mpn2, PatternRegistry patternRegistry) {
        if (mpn1 == null || mpn2 == null) return 0.0;

        logger.debug("Comparing memory devices: {} vs {}", mpn1, mpn2);

        // First check if these are memory devices
        if (!isMemoryDevice(mpn1) || !isMemoryDevice(mpn2)) {
            logger.debug("One or both parts are not memory devices");
            return 0.0;
        }

        // Check if metadata is available (for future spec-based enhancement)
        Optional<ComponentTypeMetadata> metadataOpt = metadataRegistry.getMetadata(ComponentType.MEMORY);
        if (metadataOpt.isEmpty()) {
            logger.trace("No metadata found for MEMORY, using equivalent group approach");
        }

        // Memory comparison primarily uses equivalent groups and characteristics
        return calculateEquivalentGroupBasedSimilarity(mpn1, mpn2);
    }

    /**
     * Calculate similarity based on equivalent groups and known characteristics.
     * This is the primary memory comparison method.
     */
    private double calculateEquivalentGroupBasedSimilarity(String mpn1, String mpn2) {

        // Extract base part numbers
        String base1 = extractBasePart(mpn1);
        String base2 = extractBasePart(mpn2);

        logger.trace("Base parts: {} and {}", base1, base2);

        // Same exact base part
        if (base1.equals(base2)) {
            // Check package compatibility
            String pkg1 = extractPackage(mpn1);
            String pkg2 = extractPackage(mpn2);

            if (pkg1.equals(pkg2)) {
                return 1.0;  // Exact same part and package
            }

            if (areCompatiblePackages(pkg1, pkg2)) {
                return 0.9;  // Same part, compatible packages
            }

            return 0.8;  // Same part, different packages
        }



        // Check for equivalent parts
        if (areEquivalentParts(base1, base2)) {
            return 0.8;  // Known equivalent parts
        }

        // Get characteristics
        MemoryCharacteristics char1 = getCharacteristics(base1);
        MemoryCharacteristics char2 = getCharacteristics(base2);

        if (char1 != null && char2 != null) {
            return calculateCharacteristicsSimilarity(char1, char2);
        }
// For different memory sizes, return LOW_SIMILARITY instead of 0.0
        if (base1.equals(base2)) {
            return HIGH_SIMILARITY;
        } else {
            // Different parts - return LOW_SIMILARITY instead of 0.0
            return LOW_SIMILARITY;
        }
        // return 0.0;
    }

    private boolean isMemoryDevice(String mpn) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();

        // Match common memory device patterns
        return upperMpn.matches("^(24|25|93)[A-Z]?C[0-9]+.*") ||      // Common EEPROM
                upperMpn.matches("^AT24.*|^AT25.*") ||                  // Atmel
                upperMpn.matches("^M24.*|^M95.*") ||                    // ST
                upperMpn.matches("^CAT24.*") ||                         // ON Semi
                upperMpn.matches("^SST25.*") ||                         // Microchip
                upperMpn.matches("^W25[QX][0-9]+.*") ||                // Winbond Flash
                upperMpn.matches("^MX25[LU][0-9]+.*") ||               // Macronix Flash
                upperMpn.matches("^S25FL[0-9]+.*") ||                  // Spansion/Cypress Flash
                upperMpn.matches("^IS25LP[0-9]+.*");                   // ISSI Flash
    }

    private String extractBasePart(String mpn) {
        if (mpn == null) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract core part number using regex
        Pattern pattern = Pattern.compile(
                "((?:24LC|AT24C|M24C|CAT24C|25LC|AT25|M95|W25Q|W25X|MX25L|S25FL|IS25LP)[0-9]+)(?:JV|FW|JW)?");
        Matcher matcher = pattern.matcher(upperMpn);

        if (matcher.find()) {
            return matcher.group(1);
        }

        // Remove suffixes and temperature/package codes
        return upperMpn.replaceAll("([A-Z0-9]+(?:JV|FW|JW)?)[A-Z]*(-.*|/.*)?", "$1");
    }

    private String extractPackage(String mpn) {
        if (mpn == null) return "";

        // Handle standard package codes
        if (mpn.endsWith("IQ") || mpn.endsWith("IG")) return "SOIC";
        if (mpn.endsWith("TR") || mpn.endsWith("T")) return "TSSOP";
        if (mpn.endsWith("FN") || mpn.endsWith("UN")) return "WSON";
        if (mpn.endsWith("DF")) return "WSOP";

        // Try to find package code after the last / or -
        String[] parts = mpn.split("[/-]");
        if (parts.length > 0) {
            String lastPart = parts[parts.length - 1];
            return PACKAGE_CODES.getOrDefault(lastPart, lastPart);
        }

        return "";
    }

    private boolean areCompatiblePackages(String pkg1, String pkg2) {
        if (pkg1.equals(pkg2)) return true;

        // SOIC and WSOP are often pin-compatible for flash devices
        if ((pkg1.equals("SOIC") || pkg1.equals("WSOP")) &&
                (pkg2.equals("SOIC") || pkg2.equals("WSOP"))) {
            return true;
        }

        // SOIC and WSON might be compatible in some cases
        if ((pkg1.equals("SOIC") || pkg1.equals("WSON")) &&
                (pkg2.equals("SOIC") || pkg2.equals("WSON"))) {
            return true;
        }

        return false;
    }

    private boolean areEquivalentParts(String base1, String base2) {
        // Check each equivalent group
        for (Set<String> group : EQUIVALENT_GROUPS.values()) {
            // Try exact match
            if (group.contains(base1) && group.contains(base2)) {
                return true;
            }

            // Try matching base parts (without JV/FW suffixes)
            String stripped1 = stripRevision(base1);
            String stripped2 = stripRevision(base2);

            if (group.stream().map(this::stripRevision)
                    .anyMatch(p -> p.equals(stripped1)) &&
                    group.stream().map(this::stripRevision)
                            .anyMatch(p -> p.equals(stripped2))) {
                return true;
            }
        }
        return false;
    }

    private String stripRevision(String mpn) {
        return mpn.replaceAll("([A-Z0-9]+)(?:JV|FW|JW)[A-Z]*$", "$1");
    }

    private MemoryCharacteristics getCharacteristics(String mpn) {
        // Try exact match
        MemoryCharacteristics exact = CHARACTERISTICS.get(mpn);
        if (exact != null) return exact;

        // Try without revision letter
        String stripped = stripRevision(mpn);
        return CHARACTERISTICS.get(stripped);
    }

    private double calculateCharacteristicsSimilarity(MemoryCharacteristics char1,
                                                      MemoryCharacteristics char2) {
        double similarity = 0.0;

        // Same memory type (EEPROM, Flash, etc.)
        if (char1.memoryType.equals(char2.memoryType)) {
            similarity += 0.3;
        }

        // Same interface (I2C, SPI, etc.)
        if (char1.interface_.equals(char2.interface_)) {
            similarity += 0.2;
        }

        // Similar capacity (within same order of magnitude)
        if (Math.abs(Math.log10(char1.capacity) - Math.log10(char2.capacity)) <= 1) {
            similarity += 0.2;
        }

        // Compatible voltage range
        if (areVoltageRangesCompatible(char1, char2)) {
            similarity += 0.2;
        }

        return similarity;
    }

    private boolean areVoltageRangesCompatible(MemoryCharacteristics char1,
                                               MemoryCharacteristics char2) {
        // Check if voltage ranges overlap
        return !(char1.maxVoltage < char2.minVoltage || char2.maxVoltage < char1.minVoltage);
    }

    private static class MemoryCharacteristics {
        final int capacity;          // Size in Kbit
        final String memoryType;     // EEPROM, Flash, etc.
        final String interface_;     // I2C, SPI, etc.
        final double minVoltage;     // Minimum operating voltage
        final double maxVoltage;     // Maximum operating voltage

        MemoryCharacteristics(int capacity, String memoryType, String interface_,
                              double minVoltage, double maxVoltage) {
            this.capacity = capacity;
            this.memoryType = memoryType;
            this.interface_ = interface_;
            this.minVoltage = minVoltage;
            this.maxVoltage = maxVoltage;
        }
    }
}