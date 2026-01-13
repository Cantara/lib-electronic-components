package no.cantara.electronic.component.lib.manufacturers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.ManufacturerHandler;
import no.cantara.electronic.component.lib.ManufacturerComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handler for CUI Devices components.
 *
 * CUI Devices manufactures:
 * - Audio Jacks: SJ series (3.5mm audio), PJ series (power jacks)
 * - Buzzers/Speakers: CMI, CMS, CPE, CPT series
 * - Encoders: ACZ, AMT series
 * - Switches and other components
 *
 * MPN Examples:
 * - SJ1-3523N (3.5mm audio jack, vertical, SPST)
 * - SJ1-3525N (3.5mm audio jack, vertical, with switch)
 * - PJ-002A (DC power jack)
 * - PJ-102A (DC power jack, 2.5mm)
 * - CMS-15118-78P (speaker, 15mm, 8 ohm)
 * - CMI-1295-85T (buzzer, 12mm, magnetic)
 * - AMT102-V (incremental encoder)
 * - ACZ11BR1E-20FA1-24C (rotary encoder)
 */
public class CUIHandler implements ManufacturerHandler {

    // Audio Jack patterns
    private static final Pattern SJ_AUDIO_PATTERN = Pattern.compile("^SJ([0-9]+)-([0-9]+)([A-Z]+)?$");
    private static final Pattern PJ_POWER_PATTERN = Pattern.compile("^PJ-([0-9]+)([A-Z]*)$");

    // Buzzer/Speaker patterns
    private static final Pattern CMI_BUZZER_PATTERN = Pattern.compile("^CMI-([0-9]+)-([0-9]+)([A-Z]?)$");
    private static final Pattern CMS_SPEAKER_PATTERN = Pattern.compile("^CMS-([0-9]+)-([0-9]+)([A-Z]?)$");
    private static final Pattern CPE_BUZZER_PATTERN = Pattern.compile("^CPE-([0-9]+)([A-Z]?)$");
    private static final Pattern CPT_BUZZER_PATTERN = Pattern.compile("^CPT-([0-9]+)-([0-9]+)([A-Z]?)$");

    // Encoder patterns
    private static final Pattern AMT_ENCODER_PATTERN = Pattern.compile("^AMT([0-9]+)(-[A-Z0-9]+)?$");
    private static final Pattern ACZ_ENCODER_PATTERN = Pattern.compile("^ACZ([0-9]+)([A-Z]+)?([0-9]+)?(-.*)?$");

    // Series mappings
    private static final Map<String, String> SERIES_FAMILIES = Map.ofEntries(
            new SimpleEntry<>("SJ1", "SJ1 Audio Jack Series"),
            new SimpleEntry<>("SJ2", "SJ2 Audio Jack Series"),
            new SimpleEntry<>("PJ", "PJ Power Jack Series"),
            new SimpleEntry<>("CMI", "CMI Magnetic Buzzer Series"),
            new SimpleEntry<>("CMS", "CMS Speaker Series"),
            new SimpleEntry<>("CPE", "CPE Piezo Buzzer Series"),
            new SimpleEntry<>("CPT", "CPT Transducer Series"),
            new SimpleEntry<>("AMT", "AMT Modular Encoder Series"),
            new SimpleEntry<>("ACZ", "ACZ Rotary Encoder Series")
    );

    // Package/mounting type mappings based on suffix letters
    private static final Map<String, String> PACKAGE_MAPPINGS = Map.ofEntries(
            new SimpleEntry<>("N", "Vertical/PCB Mount"),
            new SimpleEntry<>("NR", "Vertical with Nut"),
            new SimpleEntry<>("NC", "Vertical with Cap"),
            new SimpleEntry<>("A", "Surface Mount"),
            new SimpleEntry<>("AH", "Surface Mount Horizontal"),
            new SimpleEntry<>("B", "Through-Hole"),
            new SimpleEntry<>("BH", "Through-Hole Horizontal"),
            new SimpleEntry<>("P", "Panel Mount"),
            new SimpleEntry<>("T", "Terminal Type"),
            new SimpleEntry<>("V", "Vertical"),
            new SimpleEntry<>("H", "Horizontal")
    );

    @Override
    public Set<ComponentType> getSupportedTypes() {
        return Set.of(
                ComponentType.CONNECTOR,
                ComponentType.AUDIO_JACK,
                ComponentType.SPEAKER,
                ComponentType.BUZZER,
                ComponentType.ENCODER
        );
    }

    @Override
    public void initializePatterns(PatternRegistry registry) {
        // Audio Jacks (SJ series - 3.5mm audio)
        registry.addPattern(ComponentType.CONNECTOR, "^SJ[0-9]+-[0-9]+[A-Z]*$");
        registry.addPattern(ComponentType.AUDIO_JACK, "^SJ[0-9]+-[0-9]+[A-Z]*$");

        // Power Jacks (PJ series)
        registry.addPattern(ComponentType.CONNECTOR, "^PJ-[0-9]+[A-Z]*$");

        // Buzzers (CMI, CPE series - magnetic and piezo buzzers)
        registry.addPattern(ComponentType.BUZZER, "^CMI-[0-9]+-[0-9]+[A-Z]?$");
        registry.addPattern(ComponentType.BUZZER, "^CPE-[0-9]+[A-Z]?$");
        registry.addPattern(ComponentType.BUZZER, "^CPT-[0-9]+-[0-9]+[A-Z]?$");

        // Speakers (CMS series)
        registry.addPattern(ComponentType.SPEAKER, "^CMS-[0-9]+-[0-9]+[A-Z]?$");

        // Encoders (AMT, ACZ series)
        registry.addPattern(ComponentType.ENCODER, "^AMT[0-9]+.*$");
        registry.addPattern(ComponentType.ENCODER, "^ACZ[0-9]+.*$");
    }

    @Override
    public boolean matches(String mpn, ComponentType type, PatternRegistry patterns) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        // Direct pattern checks for performance
        switch (type) {
            case CONNECTOR:
            case AUDIO_JACK:
                if (upperMpn.matches("^SJ[0-9]+-[0-9]+[A-Z]*$") ||
                    upperMpn.matches("^PJ-[0-9]+[A-Z]*$")) {
                    return true;
                }
                break;
            case BUZZER:
                if (upperMpn.matches("^CMI-[0-9]+-[0-9]+[A-Z]?$") ||
                    upperMpn.matches("^CPE-[0-9]+[A-Z]?$") ||
                    upperMpn.matches("^CPT-[0-9]+-[0-9]+[A-Z]?$")) {
                    return true;
                }
                break;
            case SPEAKER:
                if (upperMpn.matches("^CMS-[0-9]+-[0-9]+[A-Z]?$")) {
                    return true;
                }
                break;
            case ENCODER:
                if (upperMpn.matches("^AMT[0-9]+.*$") ||
                    upperMpn.matches("^ACZ[0-9]+.*$")) {
                    return true;
                }
                break;
            default:
                break;
        }

        // Fallback to registry
        return patterns.matches(upperMpn, type);
    }

    @Override
    public String extractPackageCode(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // SJ series: suffix after dash and number (e.g., SJ1-3523N -> N)
        Matcher sjMatcher = SJ_AUDIO_PATTERN.matcher(upperMpn);
        if (sjMatcher.matches() && sjMatcher.group(3) != null) {
            return sjMatcher.group(3);
        }

        // PJ series: suffix after number (e.g., PJ-002A -> A)
        Matcher pjMatcher = PJ_POWER_PATTERN.matcher(upperMpn);
        if (pjMatcher.matches() && pjMatcher.group(2) != null && !pjMatcher.group(2).isEmpty()) {
            return pjMatcher.group(2);
        }

        // CMI series: last letter (e.g., CMI-1295-85T -> T)
        Matcher cmiMatcher = CMI_BUZZER_PATTERN.matcher(upperMpn);
        if (cmiMatcher.matches() && cmiMatcher.group(3) != null && !cmiMatcher.group(3).isEmpty()) {
            return cmiMatcher.group(3);
        }

        // CMS series: last letter (e.g., CMS-15118-78P -> P)
        Matcher cmsMatcher = CMS_SPEAKER_PATTERN.matcher(upperMpn);
        if (cmsMatcher.matches() && cmsMatcher.group(3) != null && !cmsMatcher.group(3).isEmpty()) {
            return cmsMatcher.group(3);
        }

        // AMT series: suffix after dash (e.g., AMT102-V -> V)
        Matcher amtMatcher = AMT_ENCODER_PATTERN.matcher(upperMpn);
        if (amtMatcher.matches() && amtMatcher.group(2) != null) {
            return amtMatcher.group(2).replaceFirst("^-", "");
        }

        return "";
    }

    @Override
    public String extractSeries(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return "";
        }

        String upperMpn = mpn.toUpperCase();

        // SJ series (SJ1, SJ2, etc.)
        if (upperMpn.matches("^SJ[0-9].*")) {
            int dashIndex = upperMpn.indexOf('-');
            if (dashIndex > 0) {
                return upperMpn.substring(0, dashIndex);
            }
            // Return just prefix if no dash
            for (int i = 2; i < upperMpn.length(); i++) {
                if (!Character.isDigit(upperMpn.charAt(i))) {
                    return upperMpn.substring(0, i);
                }
            }
            return upperMpn;
        }

        // PJ series
        if (upperMpn.startsWith("PJ-") || upperMpn.startsWith("PJ")) {
            return "PJ";
        }

        // CMI series
        if (upperMpn.startsWith("CMI-") || upperMpn.startsWith("CMI")) {
            return "CMI";
        }

        // CMS series
        if (upperMpn.startsWith("CMS-") || upperMpn.startsWith("CMS")) {
            return "CMS";
        }

        // CPE series
        if (upperMpn.startsWith("CPE-") || upperMpn.startsWith("CPE")) {
            return "CPE";
        }

        // CPT series
        if (upperMpn.startsWith("CPT-") || upperMpn.startsWith("CPT")) {
            return "CPT";
        }

        // AMT series (extract AMTxxx)
        if (upperMpn.startsWith("AMT")) {
            Matcher amtMatcher = AMT_ENCODER_PATTERN.matcher(upperMpn);
            if (amtMatcher.matches()) {
                return "AMT" + amtMatcher.group(1);
            }
            return "AMT";
        }

        // ACZ series (extract ACZxx)
        if (upperMpn.startsWith("ACZ")) {
            // Extract series number
            StringBuilder series = new StringBuilder("ACZ");
            for (int i = 3; i < upperMpn.length(); i++) {
                char c = upperMpn.charAt(i);
                if (Character.isDigit(c)) {
                    series.append(c);
                } else {
                    break;
                }
            }
            return series.toString();
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

        // Must be from same series
        if (!series1.equals(series2) || series1.isEmpty()) {
            return false;
        }

        // For audio jacks, check if same connector type
        if (series1.startsWith("SJ")) {
            return areCompatibleAudioJacks(mpn1, mpn2);
        }

        // For encoders, check if same resolution/type
        if (series1.startsWith("AMT") || series1.startsWith("ACZ")) {
            return areCompatibleEncoders(mpn1, mpn2);
        }

        // For buzzers/speakers, check if same size and impedance
        return areCompatibleTransducers(mpn1, mpn2);
    }

    @Override
    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return Collections.emptySet();
    }

    // Helper methods

    private boolean areCompatibleAudioJacks(String mpn1, String mpn2) {
        // Extract the part number (middle section)
        Matcher m1 = SJ_AUDIO_PATTERN.matcher(mpn1.toUpperCase());
        Matcher m2 = SJ_AUDIO_PATTERN.matcher(mpn2.toUpperCase());

        if (m1.matches() && m2.matches()) {
            // Same connector number means same physical type
            String num1 = m1.group(2);
            String num2 = m2.group(2);
            return num1.equals(num2);
        }
        return false;
    }

    private boolean areCompatibleEncoders(String mpn1, String mpn2) {
        String series1 = extractSeries(mpn1);
        String series2 = extractSeries(mpn2);

        // Must be exactly same series (includes resolution number)
        return series1.equals(series2);
    }

    private boolean areCompatibleTransducers(String mpn1, String mpn2) {
        // For buzzers/speakers, extract size and impedance codes
        String upper1 = mpn1.toUpperCase();
        String upper2 = mpn2.toUpperCase();

        // CMI and CMS format: PREFIX-SIZE-IMPEDANCE
        if ((upper1.startsWith("CMI-") || upper1.startsWith("CMS-")) &&
            (upper2.startsWith("CMI-") || upper2.startsWith("CMS-"))) {

            // Extract size (first number group) and impedance (second number group)
            String[] parts1 = upper1.split("-");
            String[] parts2 = upper2.split("-");

            if (parts1.length >= 3 && parts2.length >= 3) {
                // Same size and impedance means compatible
                return parts1[1].equals(parts2[1]) &&
                       extractNumericPrefix(parts1[2]).equals(extractNumericPrefix(parts2[2]));
            }
        }
        return false;
    }

    private String extractNumericPrefix(String s) {
        StringBuilder result = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                result.append(c);
            } else {
                break;
            }
        }
        return result.toString();
    }

    /**
     * Returns the product family for the given MPN.
     */
    public String getProductFamily(String mpn) {
        String series = extractSeries(mpn);
        return SERIES_FAMILIES.getOrDefault(series.length() > 2 ? series.substring(0, 3) : series, "Unknown");
    }

    /**
     * Returns the mounting type description.
     */
    public String getMountingType(String mpn) {
        String packageCode = extractPackageCode(mpn);
        return PACKAGE_MAPPINGS.getOrDefault(packageCode, "Unknown");
    }

    /**
     * Returns true if this is an audio connector (SJ series).
     */
    public boolean isAudioConnector(String mpn) {
        return mpn != null && mpn.toUpperCase().matches("^SJ[0-9].*");
    }

    /**
     * Returns true if this is a power connector (PJ series).
     */
    public boolean isPowerConnector(String mpn) {
        return mpn != null && mpn.toUpperCase().matches("^PJ-.*");
    }

    /**
     * Returns true if this is a buzzer (CMI, CPE, CPT series).
     */
    public boolean isBuzzer(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^CMI-.*") || upper.matches("^CPE-.*") || upper.matches("^CPT-.*");
    }

    /**
     * Returns true if this is a speaker (CMS series).
     */
    public boolean isSpeaker(String mpn) {
        return mpn != null && mpn.toUpperCase().matches("^CMS-.*");
    }

    /**
     * Returns true if this is an encoder (AMT, ACZ series).
     */
    public boolean isEncoder(String mpn) {
        if (mpn == null) return false;
        String upper = mpn.toUpperCase();
        return upper.matches("^AMT[0-9].*") || upper.matches("^ACZ[0-9].*");
    }

    /**
     * Extracts the diameter/size in mm for transducers (CMI, CMS series).
     * Returns -1 if not determinable.
     */
    public int getTransducerSizeMm(String mpn) {
        if (mpn == null) return -1;
        String upper = mpn.toUpperCase();

        // CMI-SIZE-IMPEDANCE format (e.g., CMI-1295-85T = 12mm)
        Matcher cmiMatcher = CMI_BUZZER_PATTERN.matcher(upper);
        if (cmiMatcher.matches()) {
            String sizeCode = cmiMatcher.group(1);
            if (sizeCode.length() >= 2) {
                try {
                    return Integer.parseInt(sizeCode.substring(0, 2));
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }

        // CMS-SIZE-IMPEDANCE format (e.g., CMS-15118-78P = 15mm)
        Matcher cmsMatcher = CMS_SPEAKER_PATTERN.matcher(upper);
        if (cmsMatcher.matches()) {
            String sizeCode = cmsMatcher.group(1);
            if (sizeCode.length() >= 2) {
                try {
                    return Integer.parseInt(sizeCode.substring(0, 2));
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }

        return -1;
    }

    /**
     * Extracts the impedance in ohms for speakers/buzzers.
     * Returns -1 if not determinable.
     */
    public int getImpedanceOhms(String mpn) {
        if (mpn == null) return -1;
        String upper = mpn.toUpperCase();

        // CMI-SIZE-IMPEDANCE format
        Matcher cmiMatcher = CMI_BUZZER_PATTERN.matcher(upper);
        if (cmiMatcher.matches()) {
            String impedanceCode = cmiMatcher.group(2);
            try {
                return Integer.parseInt(impedanceCode);
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        // CMS-SIZE-IMPEDANCE format
        Matcher cmsMatcher = CMS_SPEAKER_PATTERN.matcher(upper);
        if (cmsMatcher.matches()) {
            String impedanceCode = cmsMatcher.group(2);
            try {
                return Integer.parseInt(impedanceCode);
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        return -1;
    }
}
