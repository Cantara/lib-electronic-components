package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class WurthHandler implements ManufacturerHandler {
    private static final Pattern HEADER_PATTERN = Pattern.compile("(61\\d{3})(\\d{2})(\\d{3})(\\d{1})");

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Headers and connectors
        registry.addPattern(ComponentType.CONNECTOR, "^61[0-9]{9}.*");        // Pin headers
        registry.addPattern(ComponentType.CONNECTOR, "^62[0-9]{9}.*");        // Socket headers
        registry.addPattern(ComponentType.CONNECTOR, "^618[0-9]{8}.*");
        registry.addPattern(ComponentType.CONNECTOR_WURTH, "^61[0-9]{8}.*");  // Würth-specific
// Other connectors

        // More specific patterns for different series
        registry.addPattern(ComponentType.CONNECTOR, "^613[0-9]{8}.*");       // WR-PHD series
        registry.addPattern(ComponentType.CONNECTOR, "^614[0-9]{8}.*");       // WR-BHD series
        registry.addPattern(ComponentType.CONNECTOR, "^615[0-9]{8}.*");       // WR-TBL series
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        Set<ComponentType> types = new HashSet<>();
        types.add(ComponentType.CONNECTOR);
        types.add(ComponentType.CONNECTOR_WURTH);
        types.add(ComponentType.LED);
        types.add(ComponentType.LED_STANDARD_WURTH);
        types.add(ComponentType.LED_RGB_WURTH);
        types.add(ComponentType.LED_SMD_WURTH);
        return types;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        Matcher m = HEADER_PATTERN.matcher(mpn);
        if (m.matches()) {
            return m.group(4);  // Last digit is the package/variant code
        }
        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        Matcher m = HEADER_PATTERN.matcher(mpn);
        if (m.matches()) {
            return m.group(1);  // First 5 digits represent the series
        }
        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) return false;

        Matcher m1 = HEADER_PATTERN.matcher(mpn1);
        Matcher m2 = HEADER_PATTERN.matcher(mpn2);

        if (m1.matches() && m2.matches()) {
            // Same series, pin count, and pitch but different variant
            return m1.group(1).equals(m2.group(1)) &&    // Same series
                    m1.group(2).equals(m2.group(2)) &&    // Same pin count
                    m1.group(3).equals(m2.group(3));      // Same pitch
        }

        return false;
    }

    private boolean isCompatibleVariant(String variant1, String variant2) {
        return (variant1.equals("21") && variant2.equals("11")) ||
                (variant1.equals("11") && variant2.equals("21")) ||
                (variant1.equals("20") && variant2.equals("10")) ||
                (variant1.equals("10") && variant2.equals("20"));
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}