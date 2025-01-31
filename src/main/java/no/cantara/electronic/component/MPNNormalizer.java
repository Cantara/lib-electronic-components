package no.cantara.electronic.component;

import java.util.regex.Pattern;

/**
 * Utility class for normalizing MPNs
 */
public class MPNNormalizer {
    private static final Pattern SPECIAL_CHARS = Pattern.compile("[^a-zA-Z0-9]");

    public String normalize(String mpn) {
        if (mpn == null || mpn.trim().isEmpty()) {
            return "";
        }
        return SPECIAL_CHARS.matcher(mpn.trim().toUpperCase()).replaceAll("");
    }
}