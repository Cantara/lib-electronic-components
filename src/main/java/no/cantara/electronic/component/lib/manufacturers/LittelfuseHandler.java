package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Set;

/**
 * Handler for Littelfuse components.
 *
 * Littelfuse specializes in circuit protection:
 * - TVS Diodes: SMAJ, SMBJ, SMCJ, P4KE, P6KE, 1.5KE series
 * - Fuses: 0451, 0452, 0453, 0454, 0448, 154, 215 series
 * - Varistors: V series MOVs, MLV multilayer
 *
 * TVS Diode MPN Structure (VR-based naming):
 * [Series][Voltage]A/CA
 * - Series: SMAJ (400W), SMBJ (600W), SMCJ (1500W), SMDJ (3000W)
 * - Voltage: Reverse standoff voltage (e.g., 5.0, 15, 33)
 * - A = Unidirectional, CA = Bidirectional
 *
 * TVS Diode MPN Structure (VBR-based naming):
 * [Power][Package][Voltage]A/CA
 * - Power: P4 (400W), P6 (600W), 1.5 (1500W)
 * - Package: KE (axial), SMA/SMB/SMC (surface mount)
 * - Voltage: Breakdown voltage
 * - A = Unidirectional, CA = Bidirectional
 *
 * Fuse MPN Structure:
 * [Series][Current].[Suffix]
 * - Series: 0451 (Very Fast), 0452 (Slow Blow), 0453 (Very Fast), 0448 (Nano2)
 * - Current: Rating in amps (e.g., 001 = 1A, 0.25 = 250mA)
 * - Suffix: MRL (Mini Reel/1000pcs), NRL (Full Reel/5000pcs)
 *
 * Varistor MPN Structure:
 * V[Voltage][Series][Size][Options]
 * - Voltage: Operating voltage
 * - Series: E (Epoxy radial), P (Phenolic), MLE (Multilayer SMD)
 * - Size: Disc diameter for radial, package size for SMD
 *
 * Examples:
 * - SMAJ5.0A: 400W TVS, 5V standoff, unidirectional, SMA package
 * - SMAJ33CA: 400W TVS, 33V standoff, bidirectional, SMA package
 * - SMBJ15A: 600W TVS, 15V standoff, unidirectional, SMB package
 * - P6KE6.8A: 600W TVS, 6.8V breakdown, unidirectional, axial
 * - P6KE15CA: 600W TVS, 15V breakdown, bidirectional, axial
 * - 1.5KE15CA: 1500W TVS, 15V breakdown, bidirectional, axial
 * - 0452005.MRL: Slow blow fuse, 5A, mini reel
 * - 0451002.MRL: Very fast fuse, 2A, mini reel
 * - V07E130P: 130V MOV, 7mm disc, epoxy coating
 * - V18MLE0603N: 18V multilayer varistor, 0603 package
 */
public class LittelfuseHandler implements ManufacturerHandler {

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // TVS Diodes - SM series (surface mount, VR-based naming)
        // SMAJ series - 400W, SMA package
        registry.addPattern(ComponentType.DIODE, "^SMAJ[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^SMAJ[0-9]+\\.?[0-9]*C?A.*");

        // SMBJ series - 600W, SMB package
        registry.addPattern(ComponentType.DIODE, "^SMBJ[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^SMBJ[0-9]+\\.?[0-9]*C?A.*");

        // SMCJ series - 1500W, SMC package
        registry.addPattern(ComponentType.DIODE, "^SMCJ[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^SMCJ[0-9]+\\.?[0-9]*C?A.*");

        // SMDJ series - 3000W, SMD package
        registry.addPattern(ComponentType.DIODE, "^SMDJ[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^SMDJ[0-9]+\\.?[0-9]*C?A.*");

        // 5.0SMDJ series - 5000W, SMD package
        registry.addPattern(ComponentType.DIODE, "^5\\.0SMDJ[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^5\\.0SMDJ[0-9]+\\.?[0-9]*C?A.*");

        // TVS Diodes - P series (axial and SMD, VBR-based naming)
        // P4KE series - 400W, axial (DO-41)
        registry.addPattern(ComponentType.DIODE, "^P4KE[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^P4KE[0-9]+\\.?[0-9]*C?A.*");

        // P6KE series - 600W, axial (DO-15)
        registry.addPattern(ComponentType.DIODE, "^P6KE[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^P6KE[0-9]+\\.?[0-9]*C?A.*");

        // P4SMA series - 400W, SMA package (SMD version of P4KE)
        registry.addPattern(ComponentType.DIODE, "^P4SMA[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^P4SMA[0-9]+\\.?[0-9]*C?A.*");

        // P6SMB series - 600W, SMB package
        registry.addPattern(ComponentType.DIODE, "^P6SMB[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^P6SMB[0-9]+\\.?[0-9]*C?A.*");

        // 1.5KE series - 1500W, axial
        registry.addPattern(ComponentType.DIODE, "^1\\.5KE[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^1\\.5KE[0-9]+\\.?[0-9]*C?A.*");

        // 1.5SMC series - 1500W, SMC package
        registry.addPattern(ComponentType.DIODE, "^1\\.5SMC[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^1\\.5SMC[0-9]+\\.?[0-9]*C?A.*");

        // 1KSMB series - 1000W, SMB package
        registry.addPattern(ComponentType.DIODE, "^1KSMB[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^1KSMB[0-9]+\\.?[0-9]*C?A.*");

        // 3KP series - 3000W, axial
        registry.addPattern(ComponentType.DIODE, "^3KP[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^3KP[0-9]+\\.?[0-9]*C?A.*");

        // SA/SAC series - 500W, axial (SA = standard, SAC = low capacitance)
        registry.addPattern(ComponentType.DIODE, "^SA[C]?[0-9]+\\.?[0-9]*C?A.*");
        registry.addPattern(ComponentType.TVS_DIODE_LITTELFUSE, "^SA[C]?[0-9]+\\.?[0-9]*C?A.*");

        // Fuses - 045x series (NANO2)
        // 0451 - Very Fast Acting
        registry.addPattern(ComponentType.FUSE_LITTELFUSE, "^0451[0-9\\.]+.*");

        // 0452 - Slow Blow / Slo-Blo
        registry.addPattern(ComponentType.FUSE_LITTELFUSE, "^0452[0-9\\.]+.*");

        // 0453 - Very Fast Acting (larger)
        registry.addPattern(ComponentType.FUSE_LITTELFUSE, "^0453[0-9\\.]+.*");

        // 0454 - Slow Blow (larger)
        registry.addPattern(ComponentType.FUSE_LITTELFUSE, "^0454[0-9\\.]+.*");

        // 0448 - Nano2 SMF Fuse
        registry.addPattern(ComponentType.FUSE_LITTELFUSE, "^0448[0-9\\.]+.*");

        // 154/155 series - 5x20mm glass tube fuses
        registry.addPattern(ComponentType.FUSE_LITTELFUSE, "^154[0-9\\.]+.*");
        registry.addPattern(ComponentType.FUSE_LITTELFUSE, "^155[0-9\\.]+.*");

        // 215/216/217/218 series - 5x20mm ceramic fuses
        registry.addPattern(ComponentType.FUSE_LITTELFUSE, "^21[5-8][0-9\\.]+.*");

        // 3AG/AGC series - Glass cartridge fuses
        registry.addPattern(ComponentType.FUSE_LITTELFUSE, "^AGC[0-9\\.]+.*");
        registry.addPattern(ComponentType.FUSE_LITTELFUSE, "^3AG[0-9\\.]+.*");

        // Varistors - V series MOVs
        // Radial lead MOVs: V[voltage][E/P][size]
        registry.addPattern(ComponentType.VARISTOR_LITTELFUSE, "^V[0-9]{2,3}[EPD][0-9]+.*");

        // Multilayer varistors: V[voltage]MLE[package]
        registry.addPattern(ComponentType.VARISTOR_LITTELFUSE, "^V[0-9]{2}MLE[0-9]+.*");

        // MHS series - Multilayer High Surge
        registry.addPattern(ComponentType.VARISTOR_LITTELFUSE, "^V[0-9]{4}MHS[0-9]+.*");

        // UltraMOV series
        registry.addPattern(ComponentType.VARISTOR_LITTELFUSE, "^V[0-9]+[BCEMP]A[0-9]+.*");

        // TMOV series - Thermally protected MOVs
        registry.addPattern(ComponentType.VARISTOR_LITTELFUSE, "^TMOV[0-9]+.*");

        // AUMOV series - Automotive MOVs
        registry.addPattern(ComponentType.VARISTOR_LITTELFUSE, "^AUMOV[0-9]+.*");

        // ZA series - High energy zinc oxide varistors
        registry.addPattern(ComponentType.VARISTOR_LITTELFUSE, "^ZA[0-9]+.*");

        // MLV series - Multilayer varistors (alternate naming)
        registry.addPattern(ComponentType.VARISTOR_LITTELFUSE, "^MLV[0-9]+.*");
    }

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
            ComponentType.DIODE,
            ComponentType.TVS_DIODE_LITTELFUSE,
            ComponentType.FUSE_LITTELFUSE,
            ComponentType.VARISTOR_LITTELFUSE
        );
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        // TVS Diodes - SM series (VR-based naming)
        if (type == ComponentType.DIODE || type == ComponentType.TVS_DIODE_LITTELFUSE) {
            // SMAJ/SMBJ/SMCJ/SMDJ series
            if (upperMpn.matches("^SM[ABCD]J[0-9]+\\.?[0-9]*C?A.*")) {
                return true;
            }
            // 5.0SMDJ series
            if (upperMpn.matches("^5\\.0SMDJ[0-9]+\\.?[0-9]*C?A.*")) {
                return true;
            }
            // P4KE/P6KE series (axial)
            if (upperMpn.matches("^P[46]KE[0-9]+\\.?[0-9]*C?A.*")) {
                return true;
            }
            // P4SMA/P6SMB series (SMD)
            if (upperMpn.matches("^P4SMA[0-9]+\\.?[0-9]*C?A.*") ||
                upperMpn.matches("^P6SMB[0-9]+\\.?[0-9]*C?A.*")) {
                return true;
            }
            // 1.5KE/1.5SMC series
            if (upperMpn.matches("^1\\.?5KE[0-9]+\\.?[0-9]*C?A.*") ||
                upperMpn.matches("^1\\.?5SMC[0-9]+\\.?[0-9]*C?A.*")) {
                return true;
            }
            // 1KSMB series
            if (upperMpn.matches("^1KSMB[0-9]+\\.?[0-9]*C?A.*")) {
                return true;
            }
            // 3KP series
            if (upperMpn.matches("^3KP[0-9]+\\.?[0-9]*C?A.*")) {
                return true;
            }
            // SA/SAC series
            if (upperMpn.matches("^SAC?[0-9]+\\.?[0-9]*C?A.*")) {
                return true;
            }
        }

        // Fuses
        if (type == ComponentType.FUSE_LITTELFUSE) {
            // 045x series
            if (upperMpn.matches("^045[1-4][0-9\\.]+.*")) {
                return true;
            }
            // 0448 series
            if (upperMpn.matches("^0448[0-9\\.]+.*")) {
                return true;
            }
            // 154/155 series
            if (upperMpn.matches("^15[45][0-9\\.]+.*")) {
                return true;
            }
            // 21x series
            if (upperMpn.matches("^21[5-8][0-9\\.]+.*")) {
                return true;
            }
            // AGC/3AG series
            if (upperMpn.matches("^AGC[0-9\\.]+.*") || upperMpn.matches("^3AG[0-9\\.]+.*")) {
                return true;
            }
        }

        // Varistors
        if (type == ComponentType.VARISTOR_LITTELFUSE) {
            // V series MOVs
            if (upperMpn.matches("^V[0-9]{2,3}[EPDM].*")) {
                return true;
            }
            // TMOV/AUMOV series
            if (upperMpn.matches("^[TA]UMOV[0-9]+.*") || upperMpn.matches("^TMOV[0-9]+.*")) {
                return true;
            }
            // ZA series
            if (upperMpn.matches("^ZA[0-9]+.*")) {
                return true;
            }
            // MLV series
            if (upperMpn.matches("^MLV[0-9]+.*")) {
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

        // TVS Diodes - SM series packages
        if (upperMpn.startsWith("SMAJ")) {
            return "SMA";  // DO-214AC
        }
        if (upperMpn.startsWith("SMBJ")) {
            return "SMB";  // DO-214AA
        }
        if (upperMpn.startsWith("SMCJ") || upperMpn.matches("^1\\.?5SMC.*")) {
            return "SMC";  // DO-214AB
        }
        if (upperMpn.matches("^5?\\.?0?SMDJ.*")) {
            return "SMD";  // Large SMD package
        }
        if (upperMpn.startsWith("P4SMA")) {
            return "SMA";  // DO-214AC
        }
        if (upperMpn.startsWith("P6SMB") || upperMpn.startsWith("1KSMB")) {
            return "SMB";  // DO-214AA
        }
        if (upperMpn.startsWith("P4KE")) {
            return "DO-41";  // Axial
        }
        if (upperMpn.startsWith("P6KE") || upperMpn.matches("^1\\.?5KE.*")) {
            return "DO-15";  // Axial
        }
        if (upperMpn.startsWith("3KP")) {
            return "P600";  // Axial power
        }
        if (upperMpn.matches("^SAC?[0-9].*")) {
            return "DO-15";  // Axial
        }

        // Fuses - extract package info from series
        if (upperMpn.matches("^045[1-4].*")) {
            return "NANO2";  // 6.1mm x 2.69mm
        }
        if (upperMpn.matches("^0448.*")) {
            return "NANO2-SMF";  // Square SMD
        }
        if (upperMpn.matches("^15[45].*") || upperMpn.matches("^21[5-8].*")) {
            return "5x20mm";  // Standard cartridge
        }
        if (upperMpn.matches("^AGC.*") || upperMpn.matches("^3AG.*")) {
            return "6.3x32mm";  // AGC size
        }

        // Varistors - extract size from MPN
        if (upperMpn.matches("^V[0-9]{2}MLE0402.*")) {
            return "0402";
        }
        if (upperMpn.matches("^V[0-9]{2}MLE0603.*")) {
            return "0603";
        }
        if (upperMpn.matches("^V[0-9]{2}MLE0805.*")) {
            return "0805";
        }
        if (upperMpn.matches("^V[0-9]{2}MLE1206.*")) {
            return "1206";
        }
        if (upperMpn.matches("^V([0-9]{2})[EP].*")) {
            // Extract disc diameter from radial MOV
            // Format: V[size][E/P][voltage]... e.g., V07E130P -> 07mm disc
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("^V([0-9]{2})[EP]");
            java.util.regex.Matcher m = p.matcher(upperMpn);
            if (m.find()) {
                return m.group(1) + "mm";  // Disc diameter
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

        // TVS Diodes - SM series
        if (upperMpn.startsWith("SMAJ")) return "SMAJ";
        if (upperMpn.startsWith("SMBJ")) return "SMBJ";
        if (upperMpn.startsWith("SMCJ")) return "SMCJ";
        if (upperMpn.matches("^5\\.0SMDJ.*")) return "5.0SMDJ";
        if (upperMpn.startsWith("SMDJ")) return "SMDJ";

        // TVS Diodes - P series
        if (upperMpn.startsWith("P4KE")) return "P4KE";
        if (upperMpn.startsWith("P6KE")) return "P6KE";
        if (upperMpn.startsWith("P4SMA")) return "P4SMA";
        if (upperMpn.startsWith("P6SMB")) return "P6SMB";
        if (upperMpn.matches("^1\\.?5KE.*")) return "1.5KE";
        if (upperMpn.matches("^1\\.?5SMC.*")) return "1.5SMC";
        if (upperMpn.startsWith("1KSMB")) return "1KSMB";
        if (upperMpn.startsWith("3KP")) return "3KP";

        // TVS Diodes - SA series
        if (upperMpn.startsWith("SAC")) return "SAC";
        if (upperMpn.matches("^SA[0-9].*")) return "SA";

        // Fuses
        if (upperMpn.startsWith("0451")) return "0451";
        if (upperMpn.startsWith("0452")) return "0452";
        if (upperMpn.startsWith("0453")) return "0453";
        if (upperMpn.startsWith("0454")) return "0454";
        if (upperMpn.startsWith("0448")) return "0448";
        if (upperMpn.startsWith("154")) return "154";
        if (upperMpn.startsWith("155")) return "155";
        if (upperMpn.startsWith("215")) return "215";
        if (upperMpn.startsWith("216")) return "216";
        if (upperMpn.startsWith("217")) return "217";
        if (upperMpn.startsWith("218")) return "218";
        if (upperMpn.startsWith("AGC")) return "AGC";
        if (upperMpn.startsWith("3AG")) return "3AG";

        // Varistors
        if (upperMpn.matches("^V[0-9]{2}MLE.*")) return "MLE";
        if (upperMpn.matches("^V[0-9]{4}MHS.*")) return "MHS";
        if (upperMpn.startsWith("TMOV")) return "TMOV";
        if (upperMpn.startsWith("AUMOV")) return "AUMOV";
        if (upperMpn.startsWith("ZA")) return "ZA";
        if (upperMpn.startsWith("MLV")) return "MLV";
        if (upperMpn.matches("^V[0-9]+.*")) return "V";

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
        if (!series1.equals(series2) || series1.isEmpty()) {
            return false;
        }

        // Extract voltage ratings for TVS diodes
        String voltage1 = extractVoltage(mpn1);
        String voltage2 = extractVoltage(mpn2);

        // Same voltage required
        if (!voltage1.equals(voltage2) || voltage1.isEmpty()) {
            return false;
        }

        // Check directionality (A = unidirectional, CA = bidirectional)
        boolean bidir1 = mpn1.toUpperCase().contains("CA");
        boolean bidir2 = mpn2.toUpperCase().contains("CA");

        // Same directionality required
        return bidir1 == bidir2;
    }

    /**
     * Extract voltage rating from TVS diode or varistor MPN.
     *
     * @param mpn the manufacturer part number
     * @return voltage string or empty if not found
     */
    public String extractVoltage(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // TVS Diodes - extract voltage from SM series
        // Format: SMAJ[voltage]A or SMAJ[voltage]CA
        java.util.regex.Pattern smPattern = java.util.regex.Pattern.compile(
            "^SM[ABCD]J([0-9]+\\.?[0-9]*)C?A");
        java.util.regex.Matcher smMatcher = smPattern.matcher(upperMpn);
        if (smMatcher.find()) {
            return smMatcher.group(1);
        }

        // TVS Diodes - P series
        // Format: P[4/6]KE[voltage]A/CA or P[4/6]SM[A/B][voltage]A/CA
        java.util.regex.Pattern pPattern = java.util.regex.Pattern.compile(
            "^(?:P[46]KE|P4SMA|P6SMB|1\\.?5KE|1\\.?5SMC|1KSMB|3KP)([0-9]+\\.?[0-9]*)C?A");
        java.util.regex.Matcher pMatcher = pPattern.matcher(upperMpn);
        if (pMatcher.find()) {
            return pMatcher.group(1);
        }

        // SA/SAC series
        java.util.regex.Pattern saPattern = java.util.regex.Pattern.compile(
            "^SAC?([0-9]+\\.?[0-9]*)C?A");
        java.util.regex.Matcher saMatcher = saPattern.matcher(upperMpn);
        if (saMatcher.find()) {
            return saMatcher.group(1);
        }

        // Varistors - V series
        java.util.regex.Pattern vPattern = java.util.regex.Pattern.compile(
            "^V([0-9]{2,3})[EPDM]");
        java.util.regex.Matcher vMatcher = vPattern.matcher(upperMpn);
        if (vMatcher.find()) {
            return vMatcher.group(1);
        }

        return "";
    }

    /**
     * Extract current rating from fuse MPN.
     *
     * @param mpn the manufacturer part number
     * @return current rating string or empty if not found
     */
    public String extractCurrentRating(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // 045x series: 0452005.MRL -> 005 -> 5A
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
            "^045[1-4]([0-9\\.]+)\\.");
        java.util.regex.Matcher matcher = pattern.matcher(upperMpn);
        if (matcher.find()) {
            return matcher.group(1).replaceFirst("^0+", "");  // Remove leading zeros
        }

        // 0448 series
        pattern = java.util.regex.Pattern.compile("^0448\\.?([0-9]+)");
        matcher = pattern.matcher(upperMpn);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    /**
     * Check if TVS diode is bidirectional.
     *
     * @param mpn the manufacturer part number
     * @return true if bidirectional (CA suffix), false if unidirectional (A suffix)
     */
    public boolean isBidirectional(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return false;
        }
        return mpn.toUpperCase().contains("CA");
    }

    /**
     * Get TVS diode power rating based on series.
     *
     * @param mpn the manufacturer part number
     * @return power rating in watts, or 0 if not a TVS diode
     */
    public int getPowerRating(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return 0;
        }

        String series = extractSeries(mpn);
        return switch (series) {
            case "SMAJ", "P4KE", "P4SMA" -> 400;
            case "SA", "SAC" -> 500;
            case "SMBJ", "P6KE", "P6SMB" -> 600;
            case "1KSMB" -> 1000;
            case "SMCJ", "1.5KE", "1.5SMC" -> 1500;
            case "SMDJ", "3KP" -> 3000;
            case "5.0SMDJ" -> 5000;
            default -> 0;
        };
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }
}
