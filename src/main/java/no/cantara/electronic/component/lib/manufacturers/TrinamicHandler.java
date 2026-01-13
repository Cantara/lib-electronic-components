package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for Trinamic Motion Control components (now part of Analog Devices).
 *
 * Trinamic specializes in:
 * - Stepper motor drivers (TMC21xx, TMC22xx, TMC26xx series)
 * - Motion controllers (TMC5xxx series)
 * - Gate drivers (TMC4xxx series)
 * - 3-phase drivers (TMC6xxx series)
 *
 * MPN Structure:
 * TMC[Family][Series][Package]-[Suffix]
 *
 * Package codes from suffix:
 * - LA = QFN (Leadless Array)
 * - TA = TQFP (Thin Quad Flat Package)
 * - WA = WQFN (Very thin QFN)
 * - BOB = Breakout Board
 *
 * Examples:
 * - TMC2209-LA: Stepper driver, QFN package
 * - TMC5160-TA: Motion controller, TQFP package
 * - TMC2130-LA-T: Stepper driver, QFN, tape and reel
 * - TMC6100-LA: 3-phase driver, QFN package
 */
public class TrinamicHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // TMC21xx Stepper Drivers (TMC2100, TMC2130, TMC2160)
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^TMC21[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^TMC21[0-9]{2}.*");

        // TMC22xx Advanced Stepper Drivers (TMC2208, TMC2209, TMC2225, TMC2226)
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^TMC22[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^TMC22[0-9]{2}.*");

        // TMC23xx Stepper Drivers
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^TMC23[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^TMC23[0-9]{2}.*");

        // TMC24xx Stepper Drivers
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^TMC24[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^TMC24[0-9]{2}.*");

        // TMC26xx High Power Drivers (TMC2660, TMC2690)
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^TMC26[0-9]{2}.*");
        registry.addPattern(ComponentType.IC, "^TMC26[0-9]{2}.*");

        // TMC4xxx Gate Drivers / Motion Controllers (TMC4361, TMC4671)
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^TMC4[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^TMC4[0-9]{3}.*");

        // TMC5xxx Motion Controllers (TMC5041, TMC5072, TMC5130, TMC5160)
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^TMC5[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^TMC5[0-9]{3}.*");

        // TMC6xxx 3-Phase Drivers (TMC6100, TMC6140, TMC6200)
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^TMC6[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^TMC6[0-9]{3}.*");

        // TMC7xxx series
        registry.addPattern(ComponentType.MOTOR_DRIVER, "^TMC7[0-9]{3}.*");
        registry.addPattern(ComponentType.IC, "^TMC7[0-9]{3}.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.MOTOR_DRIVER,
            ComponentType.IC
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        // All Trinamic parts start with TMC
        if (!upperMpn.startsWith("TMC")) {
            return false;
        }

        // Check for motor driver types
        if (type == ComponentType.MOTOR_DRIVER || type == ComponentType.IC) {
            // TMC21xx - Basic stepper drivers
            if (upperMpn.matches("^TMC21[0-9]{2}.*")) {
                return true;
            }
            // TMC22xx - Advanced stepper drivers
            if (upperMpn.matches("^TMC22[0-9]{2}.*")) {
                return true;
            }
            // TMC23xx - Stepper drivers
            if (upperMpn.matches("^TMC23[0-9]{2}.*")) {
                return true;
            }
            // TMC24xx - Stepper drivers
            if (upperMpn.matches("^TMC24[0-9]{2}.*")) {
                return true;
            }
            // TMC26xx - High power drivers
            if (upperMpn.matches("^TMC26[0-9]{2}.*")) {
                return true;
            }
            // TMC4xxx - Gate drivers / Motion controllers
            if (upperMpn.matches("^TMC4[0-9]{3}.*")) {
                return true;
            }
            // TMC5xxx - Motion controllers
            if (upperMpn.matches("^TMC5[0-9]{3}.*")) {
                return true;
            }
            // TMC6xxx - 3-phase drivers
            if (upperMpn.matches("^TMC6[0-9]{3}.*")) {
                return true;
            }
            // TMC7xxx
            if (upperMpn.matches("^TMC7[0-9]{3}.*")) {
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

        // Check for breakout board first (longer suffix)
        if (upperMpn.contains("-BOB") || upperMpn.endsWith("BOB")) {
            return "Breakout Board";
        }

        // Package code patterns: -LA, -TA, -WA (with optional -T for tape/reel)
        if (upperMpn.contains("-LA") || upperMpn.matches(".*LA(-T)?$")) {
            return "QFN";
        }
        if (upperMpn.contains("-TA") || upperMpn.matches(".*TA(-T)?$")) {
            return "TQFP";
        }
        if (upperMpn.contains("-WA") || upperMpn.matches(".*WA(-T)?$")) {
            return "WQFN";
        }

        // Try to extract from end of MPN if no hyphen separator
        // Some parts use format like TMC2209LA
        if (!upperMpn.contains("-")) {
            if (upperMpn.endsWith("LA") || upperMpn.endsWith("LAT")) {
                return "QFN";
            }
            if (upperMpn.endsWith("TA") || upperMpn.endsWith("TAT")) {
                return "TQFP";
            }
            if (upperMpn.endsWith("WA") || upperMpn.endsWith("WAT")) {
                return "WQFN";
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

        // All Trinamic parts start with TMC
        if (!upperMpn.startsWith("TMC")) {
            return "";
        }

        // Series is TMCxxxx (first 7 characters for 4-digit series)
        // But some series are TMCxx (like TMC21xx uses TMC2100, TMC2130, etc.)

        // Find where the series number ends
        int seriesEnd = 3; // Start after "TMC"
        for (int i = 3; i < upperMpn.length() && i < 7; i++) {
            if (Character.isDigit(upperMpn.charAt(i))) {
                seriesEnd = i + 1;
            } else {
                break;
            }
        }

        // Ensure we have at least some digits
        if (seriesEnd <= 3) {
            return "";
        }

        return upperMpn.substring(0, seriesEnd);
    }

    @Override
    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        if (mpn1 == null || mpn2 == null) {
            return false;
        }

        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be same series for official replacement
        if (series1.isEmpty() || !series1.equals(series2)) {
            return false;
        }

        // Same package required for drop-in replacement
        String pkg1 = extractPackageCode(mpn1);
        String pkg2 = extractPackageCode(mpn2);

        // Empty package or same package both count as compatible
        if (pkg1.isEmpty() || pkg2.isEmpty()) {
            return true;
        }

        return pkg1.equals(pkg2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
