package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for Mean Well power supply components.
 * Mean Well is a leading manufacturer of standard switching power supplies.
 *
 * Product lines covered:
 * - AC-DC Enclosed: RS, LRS, SE, NES series
 * - AC-DC Open Frame: PS, PT series
 * - DC-DC Converters: SD, DDR series
 * - LED Drivers: LPV, HLG, ELG, PLN, PWM series
 * - DIN Rail: HDR, EDR, MDR, NDR series
 */
public class MeanWellHandler extends AbstractManufacturerHandler {

    private static final Map<String, ComponentSeriesInfo> COMPONENT_SERIES = new LinkedHashMap<>();

    static {
        // === AC-DC Enclosed Power Supplies ===

        // RS Series - Economy enclosed switching power supply
        COMPONENT_SERIES.put("RS", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                "^RS-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "Economy Enclosed Switching Power Supply",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_AC_DC),
                "^RS-[0-9]+"
        ));

        // LRS Series - Slim enclosed switching power supply
        COMPONENT_SERIES.put("LRS", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                "^LRS-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "Slim Enclosed Switching Power Supply",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_AC_DC),
                "^LRS-[0-9]+"
        ));

        // SE Series - High power enclosed switching power supply
        COMPONENT_SERIES.put("SE", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                "^SE-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "High Power Enclosed Switching Power Supply",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_AC_DC),
                "^SE-[0-9]+"
        ));

        // NES Series - Single output enclosed switching power supply
        COMPONENT_SERIES.put("NES", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                "^NES-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "Single Output Enclosed Switching Power Supply",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_AC_DC),
                "^NES-[0-9]+"
        ));

        // SP Series - Single output with PFC
        COMPONENT_SERIES.put("SP", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                "^SP-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "Single Output with PFC",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_AC_DC),
                "^SP-[0-9]+"
        ));

        // === AC-DC Open Frame Power Supplies ===

        // PS Series - Open frame single output
        COMPONENT_SERIES.put("PS", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                "^PS-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "Open Frame Single Output Power Supply",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_AC_DC),
                "^PS-[0-9]+"
        ));

        // PT Series - Open frame triple output
        COMPONENT_SERIES.put("PT", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                "^PT-[0-9]+(?:[A-Z])?$",
                "Open Frame Triple Output Power Supply",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_AC_DC),
                "^PT-[0-9]+"
        ));

        // === DC-DC Converters ===

        // SD Series - DC-DC enclosed converter
        COMPONENT_SERIES.put("SD", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_DC_DC_MEANWELL,
                "^SD-[0-9]+[A-Z]?-[0-9]+(?:[A-Z])?$",
                "DC-DC Enclosed Converter",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_DC_DC),
                "^SD-[0-9]+"
        ));

        // DDR Series - DIN rail DC-DC converter
        COMPONENT_SERIES.put("DDR", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_DC_DC_MEANWELL,
                "^DDR-[0-9]+[A-Z]?-[0-9]+(?:[A-Z])?$",
                "DIN Rail DC-DC Converter",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_DC_DC),
                "^DDR-[0-9]+"
        ));

        // === LED Drivers ===

        // LPV Series - LED constant voltage driver
        COMPONENT_SERIES.put("LPV", new ComponentSeriesInfo(
                ComponentType.LED_DRIVER_MEANWELL,
                "^LPV-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "LED Constant Voltage Driver",
                Set.of(),
                Set.of(ComponentType.LED_DRIVER, ComponentType.POWER_SUPPLY_MEANWELL),
                "^LPV-[0-9]+"
        ));

        // HLG Series - LED constant current/voltage driver with dimming
        COMPONENT_SERIES.put("HLG", new ComponentSeriesInfo(
                ComponentType.LED_DRIVER_MEANWELL,
                "^HLG-[0-9]+H?-[0-9]+[A-Z]?(?:-[A-Z]+)?$",
                "LED Driver with Dimming",
                Set.of(),
                Set.of(ComponentType.LED_DRIVER, ComponentType.POWER_SUPPLY_MEANWELL),
                "^HLG-[0-9]+"
        ));

        // ELG Series - LED constant current/voltage driver
        COMPONENT_SERIES.put("ELG", new ComponentSeriesInfo(
                ComponentType.LED_DRIVER_MEANWELL,
                "^ELG-[0-9]+(?:-[A-Z0-9]+)?$",
                "LED Constant Current/Voltage Driver",
                Set.of(),
                Set.of(ComponentType.LED_DRIVER, ComponentType.POWER_SUPPLY_MEANWELL),
                "^ELG-[0-9]+"
        ));

        // PLN Series - LED constant current driver
        COMPONENT_SERIES.put("PLN", new ComponentSeriesInfo(
                ComponentType.LED_DRIVER_MEANWELL,
                "^PLN-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "LED Constant Current Driver",
                Set.of(),
                Set.of(ComponentType.LED_DRIVER, ComponentType.POWER_SUPPLY_MEANWELL),
                "^PLN-[0-9]+"
        ));

        // PWM Series - PWM output LED driver
        COMPONENT_SERIES.put("PWM", new ComponentSeriesInfo(
                ComponentType.LED_DRIVER_MEANWELL,
                "^PWM-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "PWM Output LED Driver",
                Set.of(),
                Set.of(ComponentType.LED_DRIVER, ComponentType.POWER_SUPPLY_MEANWELL),
                "^PWM-[0-9]+"
        ));

        // LCM Series - LED modular driver
        COMPONENT_SERIES.put("LCM", new ComponentSeriesInfo(
                ComponentType.LED_DRIVER_MEANWELL,
                "^LCM-[0-9]+(?:[A-Z])?(?:-[A-Z]+)?$",
                "LED Modular Driver",
                Set.of(),
                Set.of(ComponentType.LED_DRIVER, ComponentType.POWER_SUPPLY_MEANWELL),
                "^LCM-[0-9]+"
        ));

        // === DIN Rail Power Supplies ===

        // HDR Series - Ultra slim DIN rail power supply
        COMPONENT_SERIES.put("HDR", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                "^HDR-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "Ultra Slim DIN Rail Power Supply",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_AC_DC),
                "^HDR-[0-9]+"
        ));

        // EDR Series - Economy DIN rail power supply
        COMPONENT_SERIES.put("EDR", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                "^EDR-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "Economy DIN Rail Power Supply",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_AC_DC),
                "^EDR-[0-9]+"
        ));

        // MDR Series - Miniature DIN rail power supply
        COMPONENT_SERIES.put("MDR", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                "^MDR-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "Miniature DIN Rail Power Supply",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_AC_DC),
                "^MDR-[0-9]+"
        ));

        // NDR Series - Slim DIN rail power supply
        COMPONENT_SERIES.put("NDR", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                "^NDR-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "Slim DIN Rail Power Supply",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_AC_DC),
                "^NDR-[0-9]+"
        ));

        // DR Series - DIN rail power supply
        COMPONENT_SERIES.put("DR", new ComponentSeriesInfo(
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                "^DR-[0-9]+(?:-[0-9]+)?(?:[A-Z])?$",
                "DIN Rail Power Supply",
                Set.of(),
                Set.of(ComponentType.POWER_SUPPLY, ComponentType.POWER_SUPPLY_MEANWELL, ComponentType.POWER_SUPPLY_AC_DC),
                "^DR-[0-9]+"
        ));
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.POWER_SUPPLY,
                ComponentType.POWER_SUPPLY_AC_DC,
                ComponentType.POWER_SUPPLY_DC_DC,
                ComponentType.POWER_SUPPLY_MEANWELL,
                ComponentType.POWER_SUPPLY_AC_DC_MEANWELL,
                ComponentType.POWER_SUPPLY_DC_DC_MEANWELL,
                ComponentType.LED_DRIVER,
                ComponentType.LED_DRIVER_MEANWELL
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        for (ComponentSeriesInfo info : COMPONENT_SERIES.values()) {
            registry.addPattern(info.primaryType, info.pattern);
            registry.addPattern(info.primaryType.getBaseType(), info.pattern);
            for (ComponentType additionalType : info.additionalTypes) {
                registry.addPattern(additionalType, info.pattern);
            }
        }
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry registry) {
        if (mpn == null || type == null) return false;

        String upperMpn = mpn.toUpperCase();
        for (ComponentSeriesInfo info : COMPONENT_SERIES.values()) {
            if (upperMpn.matches(info.pattern)) {
                return type == info.primaryType ||
                        type == info.primaryType.getBaseType() ||
                        info.additionalTypes.contains(type);
            }
        }

        // Use handler-specific patterns to avoid cross-handler false matches
        return registry.matchesForCurrentHandler(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract wattage from MPN (e.g., RS-25-5 -> 25W, LRS-350-24 -> 350W)
        Pattern wattagePattern = Pattern.compile("^[A-Z]+-([0-9]+)");
        Matcher matcher = wattagePattern.matcher(upperMpn);
        if (matcher.find()) {
            return matcher.group(1) + "W";
        }

        return "";
    }

    /**
     * Extract the output voltage from the MPN.
     *
     * @param mpn the manufacturer part number
     * @return the output voltage (e.g., "5V", "12V", "24V")
     */
    public String extractOutputVoltage(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Pattern for voltage after wattage: SERIES-WATTAGE-VOLTAGE
        // e.g., RS-25-5 -> 5V, LRS-350-24 -> 24V
        Pattern voltagePattern = Pattern.compile("^[A-Z]+-[0-9]+(?:[A-Z])?-([0-9]+)");
        Matcher matcher = voltagePattern.matcher(upperMpn);
        if (matcher.find()) {
            return matcher.group(1) + "V";
        }

        // HLG series: HLG-150H-24A -> 24V
        Pattern hlgPattern = Pattern.compile("^HLG-[0-9]+H?-([0-9]+)");
        matcher = hlgPattern.matcher(upperMpn);
        if (matcher.find()) {
            return matcher.group(1) + "V";
        }

        return "";
    }

    /**
     * Extract the power rating (wattage) from the MPN.
     *
     * @param mpn the manufacturer part number
     * @return the power rating (e.g., "25W", "150W", "350W")
     */
    public String extractPowerRating(String mpn) {
        return extractPackageCode(mpn); // Same as package code for Mean Well
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        // Extract series prefix (e.g., RS, LRS, SE, HLG, etc.)
        for (String seriesKey : COMPONENT_SERIES.keySet()) {
            if (upperMpn.startsWith(seriesKey + "-")) {
                return seriesKey;
            }
        }

        // Try to extract from pattern match
        Pattern seriesPattern = Pattern.compile("^([A-Z]+)-");
        Matcher matcher = seriesPattern.matcher(upperMpn);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
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

        // Check voltage compatibility
        String voltage1 = extractOutputVoltage(mpn1);
        String voltage2 = extractOutputVoltage(mpn2);

        // Same series and same voltage means compatible
        if (!voltage1.isEmpty() && voltage1.equals(voltage2)) {
            // Check power rating - higher or equal power rating is acceptable
            String power1 = extractPowerRating(mpn1);
            String power2 = extractPowerRating(mpn2);

            // Parse power values
            int watts1 = parseWattage(power1);
            int watts2 = parseWattage(power2);

            // Part 2 must have >= wattage of part 1 to be a replacement
            return watts2 >= watts1;
        }

        return false;
    }

    private int parseWattage(String wattage) {
        if (wattage == null || wattage.isEmpty()) return 0;
        try {
            return Integer.parseInt(wattage.replace("W", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    /**
     * Get the product type description for a Mean Well part number.
     *
     * @param mpn the manufacturer part number
     * @return the product type description
     */
    public String getProductType(String mpn) {
        if (mpn == null || mpn.isEmpty()) return "";

        String upperMpn = mpn.toUpperCase();

        for (ComponentSeriesInfo info : COMPONENT_SERIES.values()) {
            if (upperMpn.matches(info.pattern)) {
                return info.description;
            }
        }

        return "";
    }

    /**
     * Determine if the part is an LED driver.
     *
     * @param mpn the manufacturer part number
     * @return true if the part is an LED driver
     */
    public boolean isLEDDriver(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;

        String series = extractSeries(mpn);
        return Set.of("LPV", "HLG", "ELG", "PLN", "PWM", "LCM").contains(series);
    }

    /**
     * Determine if the part is a DIN rail power supply.
     *
     * @param mpn the manufacturer part number
     * @return true if the part is a DIN rail power supply
     */
    public boolean isDINRail(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;

        String series = extractSeries(mpn);
        return Set.of("HDR", "EDR", "MDR", "NDR", "DR", "DDR").contains(series);
    }

    /**
     * Determine if the part is a DC-DC converter.
     *
     * @param mpn the manufacturer part number
     * @return true if the part is a DC-DC converter
     */
    public boolean isDCDC(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;

        String series = extractSeries(mpn);
        return Set.of("SD", "DDR").contains(series);
    }

    /**
     * Class to hold information about a specific component series.
     */
    private static class ComponentSeriesInfo {
        final ComponentType primaryType;
        final String pattern;
        final String description;
        final Set<String> equivalentSeries;
        final Set<ComponentType> additionalTypes;
        final String baseSeriesPattern;

        ComponentSeriesInfo(ComponentType primaryType, String pattern, String description,
                           Set<String> equivalentSeries) {
            this(primaryType, pattern, description, equivalentSeries, Set.of(), null);
        }

        ComponentSeriesInfo(ComponentType primaryType, String pattern, String description,
                           Set<String> equivalentSeries, Set<ComponentType> additionalTypes,
                           String baseSeriesPattern) {
            this.primaryType = primaryType;
            this.pattern = pattern;
            this.description = description;
            this.equivalentSeries = equivalentSeries;
            this.additionalTypes = additionalTypes != null ? additionalTypes : Set.of();
            this.baseSeriesPattern = baseSeriesPattern != null ? baseSeriesPattern : pattern;
        }
    }
}
