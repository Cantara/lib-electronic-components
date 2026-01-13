package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for Allegro MicroSystems components.
 *
 * Allegro specializes in:
 * - Current sensors (ACS7xx series)
 * - Motor drivers (A39xx, A49xx series)
 * - Hall effect sensors (A13xx series, AH prefix)
 * - LED drivers (A6xxx, A8xxx series)
 *
 * MPN Structure:
 * [Family][Series][Package][Variant]-[Rating]-[Suffix]
 *
 * Examples:
 * - ACS712ELCTR-05B-T: Current sensor, SOIC-8, tape/reel, 5A bidirectional
 * - A4988SETTR-T: Motor driver, QFN package, tape/reel
 * - A1324LLHLT-T: Hall sensor, SOT-23 package, tape/reel
 */
public class AllegroHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Current Sensors (ACS7xx series)
        registry.addPattern(ComponentType.SENSOR_CURRENT, "^ACS7[0-9]{2}.*");
        registry.addPattern(ComponentType.SENSOR_CURRENT, "^ACS37[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^ACS7[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^ACS37[0-9]{3}.*");

        // Motor Drivers (A39xx, A49xx, A59xx series)
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^A3[0-9]{3}.*");
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^A4[0-9]{3}.*");
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^A5[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^A3[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^A4[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^A5[0-9]{3}.*");

        // Hall Effect Sensors (A1xxx series)
        registry.addPattern(ComponentType.HALL_SENSOR, "^A1[0-9]{3}.*");
        registry.addPattern(ComponentType.SENSOR, "^A1[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^A1[0-9]{3}.*");

        // Hall Effect Switches (AH series)
        registry.addPattern(ComponentType.HALL_SENSOR, "^AH[0-9]{3,4}.*");
        registry.addPattern(ComponentType.SENSOR, "^AH[0-9]{3,4}.*");

        // LED Drivers (A6xxx, A8xxx series)
        registry.addPattern(ComponentType.LED_DRIVER, "^A6[0-9]{3}.*");
        registry.addPattern(ComponentType.LED_DRIVER, "^A8[0-9]{3,4}.*");
        registry.addPattern(ComponentType.IC, "^A6[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^A8[0-9]{3,4}.*");

        // Power Management ICs
        registry.addPattern(ComponentType.IC, "^A2[0-9]{3}.*");

        // Angle Sensors
        registry.addPattern(ComponentType.SENSOR, "^AAS[0-9]{3,4}.*");
        registry.addPattern(ComponentType.IC, "^AAS[0-9]{3,4}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.SENSOR_CURRENT,
            ComponentType.MOTOR_DRIVER,
            ComponentType.HALL_SENSOR,
            ComponentType.LED_DRIVER,
            ComponentType.SENSOR,
            ComponentType.IC
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        // Current Sensors
        if (type == ComponentType.SENSOR_CURRENT || type == ComponentType.IC) {
            if (upperMpn.matches("^ACS7[0-9]{2}.*") || upperMpn.matches("^ACS37[0-9]{3}.*")) {
                return true;
            }
        }

        // Motor Drivers
        if (type == ComponentType.MOTOR_DRIVER || type == ComponentType.IC) {
            if (upperMpn.matches("^A[345][0-9]{3}.*")) {
                return true;
            }
        }

        // Hall Sensors
        if (type == ComponentType.HALL_SENSOR || type == ComponentType.SENSOR || type == ComponentType.IC) {
            if (upperMpn.matches("^A1[0-9]{3}.*") || upperMpn.matches("^AH[0-9]{3,4}.*")) {
                return true;
            }
        }

        // LED Drivers
        if (type == ComponentType.LED_DRIVER || type == ComponentType.IC) {
            if (upperMpn.matches("^A[68][0-9]{3,4}.*")) {
                return true;
            }
        }

        // Angle Sensors
        if (type == ComponentType.SENSOR || type == ComponentType.IC) {
            if (upperMpn.matches("^AAS[0-9]{3,4}.*")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Current sensors: ACS712ELCTR-05B-T -> ELC, ACS723LLCTR -> LLC
        if (upperMpn.startsWith("ACS")) {
            // Package code follows the part number before TR or -
            // Check longer suffixes first
            if (upperMpn.contains("ELCTR") || upperMpn.contains("ELC")) {
                return "SOIC-8";
            }
            if (upperMpn.contains("LLCTR") || upperMpn.contains("LLC")) {
                return "SOIC-8";  // LLC is leadless version of SOIC-8
            }
            if (upperMpn.contains("KLCTR") || upperMpn.contains("KLC")) {
                return "SOIC-8-EP";
            }
            if (upperMpn.contains("LLH")) {
                return "SOT-23";
            }
        }

        // Motor drivers: A4988SETTR -> SE (SOIC) or ET (QFN)
        if (upperMpn.matches("^A[345][0-9]{3}.*")) {
            if (upperMpn.contains("SET") || upperMpn.contains("SE-")) {
                return "QFN";
            }
            if (upperMpn.contains("SLB")) {
                return "SOIC-24";
            }
            if (upperMpn.contains("SL-") || upperMpn.contains("SLT")) {
                return "SOIC-20";
            }
            if (upperMpn.contains("ET")) {
                return "QFN";
            }
        }

        // Hall sensors: A1324LUA-T -> LUA (SIP), LH (SOT-23)
        if (upperMpn.matches("^A1[0-9]{3}.*") || upperMpn.matches("^AH[0-9]{3,4}.*")) {
            if (upperMpn.contains("LUA")) {
                return "SIP-3";
            }
            if (upperMpn.contains("LH") || upperMpn.contains("LLH")) {
                return "SOT-23";
            }
            if (upperMpn.contains("EUA")) {
                return "SIP-4";
            }
            if (upperMpn.contains("KUA")) {
                return "TO-92";
            }
        }

        // LED drivers: Various packages
        if (upperMpn.matches("^A[68][0-9]{3,4}.*")) {
            if (upperMpn.contains("KLJ") || upperMpn.contains("KL")) {
                return "TO-92";
            }
            if (upperMpn.contains("SE")) {
                return "SOIC";
            }
            if (upperMpn.contains("ET")) {
                return "QFN";
            }
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // Current sensors: ACS712, ACS723, ACS37612
        if (upperMpn.startsWith("ACS37")) {
            // Extract ACS37xxx
            int endIdx = 8; // ACS37612
            if (upperMpn.length() >= endIdx) {
                return upperMpn.substring(0, endIdx);
            }
        }
        if (upperMpn.startsWith("ACS7")) {
            // Extract ACS7xx
            int endIdx = 6; // ACS712
            if (upperMpn.length() >= endIdx) {
                return upperMpn.substring(0, endIdx);
            }
        }

        // Motor drivers: A4988, A3967
        if (upperMpn.matches("^A[345][0-9]{3}.*")) {
            // Extract first 5 chars (A4988)
            if (upperMpn.length() >= 5) {
                return upperMpn.substring(0, 5);
            }
        }

        // Hall sensors: A1324, A1301, AH3362
        if (upperMpn.startsWith("AH")) {
            // Extract AHxxxx
            int endIdx = Math.min(6, upperMpn.length());
            for (int i = 2; i < upperMpn.length(); i++) {
                if (!Character.isDigit(upperMpn.charAt(i))) {
                    endIdx = i;
                    break;
                }
            }
            return upperMpn.substring(0, endIdx);
        }
        if (upperMpn.matches("^A1[0-9]{3}.*")) {
            // Extract A1xxx
            if (upperMpn.length() >= 5) {
                return upperMpn.substring(0, 5);
            }
        }

        // LED drivers: A6261, A80601
        if (upperMpn.matches("^A[68][0-9]{3,4}.*")) {
            // Extract until non-digit after A6/A8
            int endIdx = 2;
            for (int i = 2; i < upperMpn.length() && i < 7; i++) {
                if (Character.isDigit(upperMpn.charAt(i))) {
                    endIdx = i + 1;
                } else {
                    break;
                }
            }
            return upperMpn.substring(0, endIdx);
        }

        // Angle sensors: AAS33001
        if (upperMpn.startsWith("AAS")) {
            int endIdx = 3;
            for (int i = 3; i < upperMpn.length() && i < 8; i++) {
                if (Character.isDigit(upperMpn.charAt(i))) {
                    endIdx = i + 1;
                } else {
                    break;
                }
            }
            return upperMpn.substring(0, endIdx);
        }

        return "";
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) {
            return false;
        }

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series for official replacement
        if (!series1.equals(series2)) {
            return false;
        }

        // Same package required
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        return pkg1.equals(pkg2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
