package no.cantara.electronic.component.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class ComponentTypeDetector {
    private static final Logger logger = LoggerFactory.getLogger(ComponentTypeDetector.class);

    /**
        * Determines the specific ComponentType for an MPN.
            */
    public static ComponentType determineComponentType(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return null;
        }

        logger.debug("Determining type for MPN: {}", mpn);

        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(mpn);
        logger.debug("Found manufacturer: {}", manufacturer.getName());

        // Special case for 78xx and 79xx series - always voltage regulator
        if (mpn.matches("^(?:LM|MC)?(?:78|79)\\d{2}.*")) {
            return ComponentType.VOLTAGE_REGULATOR_LINEAR_TI;
        }

        // Special case for LM317 - always voltage regulator
        if (mpn.matches("^LM317.*")) {
            return ComponentType.VOLTAGE_REGULATOR_LINEAR_TI;
        }

        // Special case for 74-series logic ICs - always identify as generic IC
        if (mpn.matches("^74.*")) {
            return ComponentType.IC;
        }

        // Use the manufacturer's handler to determine type
        ManufacturerHandler handler = manufacturer.getHandler();
        Set<ComponentType> supportedTypes = handler.getSupportedTypes();

        // Special case for LM358 - always identify as op-amp
        if (mpn.startsWith("LM358")) {
            for (ComponentType type : supportedTypes) {
                if (isOpAmp(type)) {
                    return type;
                }
            }
        }

        // Special case for LM358 - always identify as op-amp
        if (mpn.startsWith("LM358")) {
            for (ComponentType type : supportedTypes) {
                if (isOpAmp(type)) {
                    return type;
                }
            }
        }

        // Start with most specific types first
        ComponentType matchedType = null;
        for (ComponentType type : supportedTypes) {
            if (handler.matches(mpn, type, manufacturer.getPatterns())) {
                logger.trace("Matched type: {}", type);
                if (matchedType == null ||
                        isMoreSpecific(type, matchedType)) {
                    matchedType = type;
                }
            }
        }
        // If we matched LOGIC_IC for a 74-series, return IC instead
        if (matchedType == ComponentType.LOGIC_IC && mpn.matches("^74.*")) {
            return ComponentType.IC;
        }

        logger.debug("Final determined type: {}", matchedType);
        if (matchedType != null) {
            logger.trace("Base type: {}", matchedType.getBaseType());
        }

        return matchedType;
    }

    /**
     * Helper method to determine if one type is more specific than another.
     */
    private static boolean isMoreSpecific(ComponentType type1, ComponentType type2) {
        // Special cases for specific component families
        if (isVoltageRegulator(type1) && isOpAmp(type2)) {
            return true;  // Prefer voltage regulator for LM317
        }
        if (isOpAmp(type1) && isVoltageRegulator(type2)) {
            return false; // Prefer voltage regulator for LM317
        }
        if (isTemperatureSensor(type1) && isOpAmp(type2)) {
            return false;  // Prefer op-amp type for LM358
        }
        if (isOpAmp(type1) && isTemperatureSensor(type2)) {
            return true;   // Prefer op-amp type for LM358
        }

        // If type1 extends type2, then type1 is more specific
        ComponentType current = type1;
        while (current != null && current != current.getBaseType()) {
            current = current.getBaseType();
            if (current == type2) {
                return true;
            }
        }

        // If neither extends the other, check specificity levels
        int specificity1 = getSpecificityLevel(type1);
        int specificity2 = getSpecificityLevel(type2);

        return specificity1 > specificity2;
    }

    private static boolean isVoltageRegulator(ComponentType type) {
        return type == ComponentType.VOLTAGE_REGULATOR ||
                type == ComponentType.VOLTAGE_REGULATOR_LINEAR_TI ||
                type == ComponentType.VOLTAGE_REGULATOR_LINEAR_ON ||
                type == ComponentType.VOLTAGE_REGULATOR_SWITCHING_TI ||
                type == ComponentType.VOLTAGE_REGULATOR_SWITCHING_ON ||
                (type != null && type.name().startsWith("VOLTAGE_REGULATOR_"));
    }
    /**
     * Helper method to get specificity level of a component type.
     */
    private static int getSpecificityLevel(ComponentType type) {
        String name = type.name();

        // Special case for 74-series logic ICs - prefer generic IC type
        if (name.equals("LOGIC_IC")) {
            return 1;  // Same level as IC for 74-series
        }

        if (name.contains("_")) {
            return 4;  // Manufacturer-specific types (most specific)
        }
        if (name.equals("IC")) {
            return 1;  // Most generic
        }
        if (name.equals("ANALOG_IC") || name.equals("DIGITAL_IC")) {
            return 2;  // Category types
        }
        return 3;  // Component-specific types (OPAMP, TRANSISTOR, etc.)
    }

    /**
     * Helper method to check if a type is an op-amp type
     */
    private static boolean isOpAmp(ComponentType type) {
        return type == ComponentType.OPAMP ||
                type == ComponentType.OPAMP_TI ||
                type == ComponentType.OPAMP_ON ||
                (type != null && type.name().startsWith("OPAMP_"));
    }

    /**
     * Helper method to check if a type is a temperature sensor type
     */
    private static boolean isTemperatureSensor(ComponentType type) {
        return type == ComponentType.TEMPERATURE_SENSOR ||
                type == ComponentType.TEMPERATURE_SENSOR_TI ||
                (type != null && type.name().startsWith("TEMPERATURE_SENSOR_"));
    }

    /**
     * Returns the most specific type for an MPN.
     */
    public static ComponentType determineSpecificType(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return null;
        }

        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(mpn);
        ManufacturerHandler handler = manufacturer.getHandler();
        Set<ComponentType> supportedTypes = handler.getSupportedTypes();

        // Return the most specific matching type
        for (ComponentType type : supportedTypes) {
            if (handler.matches(mpn, type, manufacturer.getPatterns())) {
                return type;
            }
        }

        return null;
    }


    /**
     * Checks if an MPN matches a specific component type.
     */
    /**
     * Checks if an MPN matches a specific component type.
     */
//    public static boolean isType(String mpn, ComponentType type) {
//        if (mpn == null || mpn.isEmpty() || type == null) {
//            return false;
//        }
//
//        System.out.println("\nChecking MPN: " + mpn + " against type: " + type);
//
//        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(mpn);
//        System.out.println("Found manufacturer: " + manufacturer.getName());
//
//        // First get the actual type of the component
//        ComponentType detectedType = determineComponentType(mpn);
//        if (detectedType == null) {
//            return false;
//        }
//
//        // Check if the detected type matches the requested type or is a subtype of it
//        ComponentType currentType = detectedType;
//        while (currentType != null) {
//            if (currentType == type) {
//                return true;
//            }
//            // Move up the type hierarchy
//            currentType = (currentType == currentType.getBaseType()) ? null : currentType.getBaseType();
//        }
//
//        return false;
//    }
//    public static boolean isType(String mpn, ComponentType type) {
//        if (mpn == null || mpn.isEmpty() || type == null) {
//            return false;
//        }
//
//        System.out.println("\nChecking MPN: " + mpn + " against type: " + type);
//
//        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(mpn);
//        System.out.println("Found manufacturer: " + manufacturer.getName());
//
//        ManufacturerHandler handler = manufacturer.getHandler();
//        PatternRegistry patterns = manufacturer.getPatterns();
//
//        // Direct check for the requested type
//        if (handler.matches(mpn, type, patterns)) {
//            return true;
//        }
//
//        // If checking for a base type (like IC), also check if any subtype matches
//        if (type.getBaseType() == type) {  // Is this a base type?
//            Set<ComponentType> supportedTypes = handler.getSupportedTypes();
//            for (ComponentType supportedType : supportedTypes) {
//                if (supportedType.getBaseType() == type && handler.matches(mpn, supportedType, patterns)) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }
    public static boolean isType(String mpn, ComponentType type) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        logger.trace("Checking MPN: {} against type: {}", mpn, type);

        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(mpn);
        logger.trace("Found manufacturer: {}", manufacturer.getName());

        ManufacturerHandler handler = manufacturer.getHandler();
        PatternRegistry patterns = manufacturer.getPatterns();

        // First try direct match
        if (handler.matches(mpn, type, patterns)) {
            return true;
        }

        // Then check all supported types to see if any matches and is in the hierarchy
        Set<ComponentType> supportedTypes = handler.getSupportedTypes();
        for (ComponentType supportedType : supportedTypes) {
            if (handler.matches(mpn, supportedType, patterns)) {
                // Check if the matching type is in the hierarchy chain of the requested type
                ComponentType currentType = supportedType;
                while (currentType != null) {
                    if (currentType == type) {
                        return true;
                    }
                    // Move up the hierarchy
                    currentType = (currentType == currentType.getBaseType()) ? null : currentType.getBaseType();
                }
                // Also check if the requested type is in the base type chain of the matching type
                currentType = type;
                while (currentType != null) {
                    if (currentType == supportedType) {
                        return true;
                    }
                    // Move up the hierarchy
                    currentType = (currentType == currentType.getBaseType()) ? null : currentType.getBaseType();
                }
            }
        }

        return false;
    }

    /**
     * Check if MPN is a resistor.
     */
    public static boolean isResistor(String mpn) {
        return isType(mpn, ComponentType.RESISTOR);
    }

    /**
     * Check if MPN is a capacitor.
     */
    public static boolean isCapacitor(String mpn) {
        return isType(mpn, ComponentType.CAPACITOR);
    }

    /**
     * Check if MPN is a transistor.
     */
    public static boolean isTransistor(String mpn) {
        return isType(mpn, ComponentType.TRANSISTOR);
    }

    /**
     * Check if MPN is a diode.
     */
    public static boolean isDiode(String mpn) {
        return isType(mpn, ComponentType.DIODE);
    }

    /**
     * Check if MPN is an IC.
     */
    public static boolean isIC(String mpn) {
        return isType(mpn, ComponentType.IC);
    }

    /**
     * Check if MPN is an inductor.
     */
    public static boolean isInductor(String mpn) {
        return isType(mpn, ComponentType.INDUCTOR);
    }

    /**
     * Check if MPN is a crystal.
     */
    public static boolean isCrystal(String mpn) {
        return isType(mpn, ComponentType.CRYSTAL);
    }

    /**
     * Check if MPN is a MOSFET.
     */
    public static boolean isMosfet(String mpn) {
        ComponentType type = determineComponentType(mpn);
        return type != null && (type == ComponentType.MOSFET || type.getBaseType() == ComponentType.MOSFET);
    }

    /**
     * Check if MPN is an analog IC.
     */
    public static boolean isAnalogIC(String mpn) {
        if (mpn == null) {
            logger.trace("MPN is null");
            return false;
        }

        logger.debug("Analog IC check for MPN: {}", mpn);

        ComponentType type = determineComponentType(mpn);
        logger.debug("Determined type: {}", type);

        if (type == null) {
            logger.trace("Type is null");
            return false;
        }

        // Check for known analog IC types
        boolean isKnownAnalogType = (type == ComponentType.TEMPERATURE_SENSOR ||
                type == ComponentType.OPAMP ||
                type == ComponentType.OPAMP_TI ||
                type == ComponentType.OPAMP_ON ||
                type.name().startsWith("OPAMP_"));

        if (isKnownAnalogType) {
            logger.trace("Matched known analog type");
            return true;
        }

        // Check base types
        boolean isAnalogBaseType = (type == ComponentType.ANALOG_IC ||
                type == ComponentType.OPAMP ||
                type == ComponentType.VOLTAGE_REGULATOR ||
                (type.getBaseType() != null &&
                        (type.getBaseType() == ComponentType.OPAMP ||
                                type.getBaseType() == ComponentType.VOLTAGE_REGULATOR ||
                                type.getBaseType() == ComponentType.ANALOG_IC)));

        logger.trace("Base type check result: {}", isAnalogBaseType);

        return isAnalogBaseType;
    }

    /**
     * Check if MPN is a digital IC.
     */
    public static boolean isDigitalIC(String mpn) {
        if (mpn == null) return false;

        // First check if it's an analog IC - if it is, it can't be digital
        if (isAnalogIC(mpn)) {
            return false;
        }

        ComponentType type = determineComponentType(mpn);
        if (type == null) return false;

        // Check for common digital IC patterns
        if (mpn.startsWith("74") || mpn.startsWith("CD4")) {
            return true;
        }

        // For other ICs, they must be explicitly marked as digital
        // or not be in the analog category
        return type == ComponentType.DIGITAL_IC ||
                (type == ComponentType.IC && !isAnalogIC(mpn));
    }
}