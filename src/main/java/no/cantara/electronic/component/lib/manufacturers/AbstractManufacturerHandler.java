package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.*;

import java.util.Collections;
import java.util.Set;

/**
 * Abstract base class for manufacturer handlers providing common functionality.
 * <p>
 * Handlers should extend this class to benefit from:
 * <ul>
 *   <li>Centralized package code resolution via {@link PackageCodeRegistry}</li>
 *   <li>Common helper methods for MPN parsing</li>
 *   <li>Default implementations for optional methods</li>
 * </ul>
 */
public abstract class AbstractManufacturerHandler implements ManufacturerHandler {

    // ========== Abstract Methods (must be implemented) ==========

    /**
     * Initialize component type patterns for this manufacturer.
     * Subclasses must register their patterns with the registry.
     */
    @Override
    public abstract void initializePatterns(PatternRegistry registry);

    /**
     * Get the set of component types this handler supports.
     */
    @Override
    public abstract Set<ComponentType> getSupportedTypes();

    // ========== Default Implementations ==========

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Check package compatibility
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // If packages are specified, check compatibility
        if (!pkg1.isEmpty() && !pkg2.isEmpty()) {
            return PackageCodeRegistry.areCompatible(pkg1, pkg2);
        }

        return true;
    }

    // ========== Package Code Helpers ==========

    /**
     * Extract package code from MPN using standard suffix patterns.
     * Override this method for manufacturer-specific package code formats.
     *
     * @param mpn the manufacturer part number
     * @return the package code or empty string if not found
     */
    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Try hyphen-separated suffix first (e.g., "ATMEGA328P-PU" -> "PU")
        String suffix = extractSuffixAfterHyphen(upperMpn);
        if (!suffix.isEmpty() && PackageCodeRegistry.isKnownCode(suffix)) {
            return PackageCodeRegistry.resolve(suffix);
        }

        // Try common suffix patterns at end of MPN
        suffix = extractTrailingSuffix(upperMpn);
        if (!suffix.isEmpty() && PackageCodeRegistry.isKnownCode(suffix)) {
            return PackageCodeRegistry.resolve(suffix);
        }

        return "";
    }

    /**
     * Extract the suffix after the last hyphen in an MPN.
     *
     * @param mpn the manufacturer part number (uppercase)
     * @return the suffix or empty string if no hyphen found
     */
    protected String extractSuffixAfterHyphen(String mpn) {
        int lastHyphen = mpn.lastIndexOf('-');
        if (lastHyphen > 0 && lastHyphen < mpn.length() - 1) {
            return mpn.substring(lastHyphen + 1);
        }
        return "";
    }

    /**
     * Extract trailing letter suffix from MPN (e.g., "LM7805CT" -> "CT", "LM358N" -> "N").
     *
     * @param mpn the manufacturer part number (uppercase)
     * @return the trailing suffix or empty string
     */
    protected String extractTrailingSuffix(String mpn) {
        // Find where the trailing letters start (after last digit)
        int lastDigitIndex = findLastDigitIndex(mpn);
        if (lastDigitIndex >= 0 && lastDigitIndex < mpn.length() - 1) {
            return mpn.substring(lastDigitIndex + 1);
        }
        return "";
    }

    // ========== Series Extraction Helpers ==========

    /**
     * Extract the series identifier from an MPN.
     * Default implementation returns the prefix up to and including numbers.
     * Override for manufacturer-specific series formats.
     *
     * @param mpn the manufacturer part number
     * @return the series identifier or empty string
     */
    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Default: extract prefix + first numeric sequence
        int firstDigit = findFirstDigitIndex(upperMpn, 0);
        if (firstDigit < 0) {
            return upperMpn; // No digits, return whole thing
        }

        int endOfDigits = firstDigit;
        while (endOfDigits < upperMpn.length() && Character.isDigit(upperMpn.charAt(endOfDigits))) {
            endOfDigits++;
        }

        return upperMpn.substring(0, endOfDigits);
    }

    /**
     * Extract series with a known prefix.
     * Useful for manufacturers with consistent prefix patterns.
     *
     * @param mpn the MPN (uppercase)
     * @param prefix the expected prefix (e.g., "LM", "STM32")
     * @return the series including prefix and following digits
     */
    protected String extractSeriesWithPrefix(String mpn, String prefix) {
        if (!mpn.startsWith(prefix)) {
            return "";
        }

        int end = prefix.length();
        while (end < mpn.length() && Character.isDigit(mpn.charAt(end))) {
            end++;
        }
        return mpn.substring(0, end);
    }

    // ========== String Parsing Helpers ==========

    /**
     * Find the index of the first digit in a string, starting from a given position.
     *
     * @param str the string to search
     * @param startFrom starting index
     * @return index of first digit, or -1 if not found
     */
    protected int findFirstDigitIndex(String str, int startFrom) {
        for (int i = startFrom; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Find the index of the last digit in a string.
     *
     * @param str the string to search
     * @return index of last digit, or -1 if no digits found
     */
    protected int findLastDigitIndex(String str) {
        for (int i = str.length() - 1; i >= 0; i--) {
            if (Character.isDigit(str.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Extract a numeric value from within an MPN.
     * Useful for extracting voltage ratings, pin counts, etc.
     *
     * @param mpn the manufacturer part number
     * @param afterPrefix extract numbers after this prefix
     * @return the numeric string or empty string if not found
     */
    protected String extractNumericAfterPrefix(String mpn, String afterPrefix) {
        int prefixIndex = mpn.toUpperCase().indexOf(afterPrefix.toUpperCase());
        if (prefixIndex < 0) {
            return "";
        }

        int start = prefixIndex + afterPrefix.length();
        int end = start;
        while (end < mpn.length() && Character.isDigit(mpn.charAt(end))) {
            end++;
        }

        if (end > start) {
            return mpn.substring(start, end);
        }
        return "";
    }

    /**
     * Check if MPN starts with any of the given prefixes.
     *
     * @param mpn the MPN to check (will be uppercased)
     * @param prefixes the prefixes to check
     * @return true if MPN starts with any prefix
     */
    protected boolean startsWithAny(String mpn, String... prefixes) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        for (String prefix : prefixes) {
            if (upperMpn.startsWith(prefix.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if MPN matches any of the given regex patterns.
     *
     * @param mpn the MPN to check (will be uppercased)
     * @param patterns the regex patterns to match
     * @return true if MPN matches any pattern
     */
    protected boolean matchesAny(String mpn, String... patterns) {
        if (mpn == null) return false;
        String upperMpn = mpn.toUpperCase();
        for (String pattern : patterns) {
            if (upperMpn.matches(pattern)) {
                return true;
            }
        }
        return false;
    }
}
