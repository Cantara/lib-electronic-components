package no.cantara.electronic.component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import no.cantara.electronic.component.manufacturers.*;

/**
 * Enum representing electronic component manufacturers with their specific patterns
 */
public enum ComponentManufacturer {
    // Microcontroller Manufacturers (Listed first for priority)
    MICROCHIP("(?:PIC|dsPIC|DSPIC|ATmega|ATtiny|AT[0-9]|AT[A-Z]{2})", "Microchip Technology", new MicrochipHandler()),
    ST("(?:STM32|STM8|ST[A-Z][0-9]|STD|STF|STP)", "STMicroelectronics", new STHandler()),
    ATMEL("(?:AT[0-9]|AT[A-Z]{2}|ATXMEGA)", "Atmel", new AtmelHandler()),
    TI("(?:TI|TPS|TMS|TLV|LM|SN|MSP430|CC[0-9]{4})", "Texas Instruments", new TIHandler()),
    RENESAS("(?:RX|RA|RE|RH|RZ|R[48]F|R5F|R7F)", "Renesas Electronics", new RenesasHandler()),
    NXP("(?:NXP|LPC|MK|IMX|S32K|KE[0-9]|PH|P[A-Z][A-Z]?[0-9])", "NXP Semiconductors", new NXPHandler()),
    CYPRESS("(?:CY|PSoC|FM[0-9]|CYW)", "Cypress Semiconductor", new CypressHandler()),
    SILICON_LABS("(?:SI|EFM|EFR|BGM|EM35|CP21)", "Silicon Labs", new SiliconLabsHandler()),
    ESPRESSIF("(?:ESP[0-9]|ESP-|ESP32)", "Espressif Systems", new EspressifHandler()),

    // Semiconductor Manufacturers
    INFINEON("(?:INF|IR|IFX|TLE|XMC|ICE|IPD|IRS|BSC|BSD|BSS|BTS|BTT)", "Infineon Technologies", new InfineonHandler()),
    VISHAY("(?:VS|SI|CRCW|IRF|SiR|1N[0-9]|BAV|BAS|BAT|TNR|VOW|VOX|2N)", "Vishay", new VishayHandler()),
    ON_SEMI("(?:ON|MC|NCP|FAN|CAT|NCV|MUR|1N|LM|NTD|NTA|NTB)", "ON Semiconductor", new OnSemiHandler()),
    ANALOG_DEVICES("(?:AD|ADP|ADM|ADG|ADA|ADUM|LT|LTC|LTM)", "Analog Devices", new AnalogDevicesHandler()),
    MAXIM("(?:MAX|DS|ICL|DG|UP|LT|LTC|LTM)", "Maxim Integrated", new MaximHandler()),
    DIODES_INC("(?:DI|AP|AZ|DMG|DMN|DMP|ZXGD|1N|BAV|BAS|BAT)", "Diodes Incorporated", new DiodesIncHandler()),
    ROHM("(?:BD|BA|BR|BSS|BU|2S[A-D]|RGT|PMR)", "ROHM Semiconductor", new RohmHandler()),
    TOSHIBA("(?:TC|TLP|TPH|TPC|2S[A-D]|TPN|TPR|TPS)", "Toshiba", new ToshibaHandler()),
    NEXPERIA("(?:NEX|PMBT|PBSS|BZX|BAT|BSS|2N|BAS|BAV|BZV|PHB)", "Nexperia", new NexteriaHandler()),
    BROADCOM("(?:BCM|AFBR|HCPL|ACPL|AVAGO)", "Broadcom", new BroadcomHandler()),
    AKM("(?:AK|LC|HM|HA)[0-9]", "Asahi Kasei Microdevices", new AKMHandler()),
    LG("(?:LG)[A-Z][0-9]", "LG Semiconductor", new LGHandler()),
    LOGIC_IC("(?:74|54)[LSAHCTTF]{0,4}[0-9]{2,4}|CD4[0-9]{3}", "Logic IC", new LogicICHandler()),

    // Passive Component Manufacturers
    //YAGEO("(?:RC[0-9]|RT[0-9]|RL[0-9]|CC[0-9]|AC[0-9]|PE[0-9])", "Yageo", new YageoHandler()),
    YAGEO("(?:RC[0-9]|RT[0-9]|RL(?!20[1-7])[0-9]|CC[0-9]|AC[0-9]|PE[0-9])", "Yageo", new YageoHandler()),    PANASONIC("(?:ERJ|ERJP|ERJG|ECQ|EEF|ECA|EVJ|EVM)", "Panasonic", new PanasonicHandler()),
    BOURNS("(?:CR[^C]|CRM|CRH|3386|3296|91|TC|TH|3310)", "Bourns", new BournsHandler()),
    KEMET("(?:C[0-9]{4}|T[0-9]{3}|A[0-9]{3}|ESD|PHE|F[0-9])", "KEMET Electronics", new KemetHandler()),
    //MURATA("(?:GRM|GRT|LQG|LQH|LQW|DLW|NFM|BLM|NCP)", "Murata Manufacturing", new MurataHandler()),
    //MURATA("(?:GRM|GCM|KCA|KC[ABMZ]|LLL|NFM|DLW|BLM|NCP)", "Murata Manufacturing", new MurataHandler()),
//    MURATA("(?:GRM|GCM|KCA|KC[ABMZ]|LLL|NFM|DLW|BLM|NCP)", "Murata Manufacturing", new MurataHandler()),
//    MURATA("(?:GRM|GCM|KCA|KC[ABMZ]|LLL|NFM|DLW|BLM|NCP)", "Murata Manufacturing", new MurataHandler()),
    MURATA("(?:GRM|GCM|KCA|KC[ABMZ]|LLL|NFM|DLW|BLM|NCP|LQM|LQW|LQG)", "Murata Manufacturing", new MurataHandler()),

    //TDK("(?:C[LH]|MPZ|SDR|ALT|NLV|B8[24]|MLG|MMZ)", "TDK Corporation", new TDKHandler()),
//    TDK("(?:CH[0-9]|MPZ|SDR|ALT|NLV|B8[24]|MLG|MMZ)", "TDK Corporation", new TDKHandler()),
    TDK("(?:CH[0-9]|MLF[0-9]|MPZ|SDR|ALT|NLV|B8[24]|MLG|MMZ)", "TDK Corporation", new TDKHandler()),

    //SAMSUNG("(?:CL[0-9]|CM[0-9]|SPH|RC_|CL21|CL31)", "Samsung Electro-Mechanics", new SamsungHandler()),
    //SAMSUNG("(?:CL[0-9]+B|CM[0-9]|SPH|RC_|CL21|CL31)", "Samsung Electro-Mechanics", new SamsungHandler()),NICHICON("(?:UUD|UPW|UVR|UCZ|UMA|UVZ|UHW|UHE)", "Nichicon", new NichiconHandler()),
    //SAMSUNG("(?:CL[0-9]+B|CM[0-9]|SPH|RC_|CL21|CL31)", "Samsung Electro-Mechanics", new SamsungHandler()),
    SAMSUNG("(?:CL(?:10|21|31)B|CM[0-9]|SPH|RC_)", "Samsung Electro-Mechanics", new SamsungHandler()),
    AVX("(?:TAJ|TPS|TCJ|F[0-9]|CR[0-9]|AR|LD|SD)", "AVX Corporation", new AVXHandler()),
    FAIRCHILD("(?:FQ[PNS]|FDS|FDC|FDD)[0-9]", "Fairchild/ON Semi", new FairchildHandler()),
    // Connector Manufacturers
    //WURTH("(?:61|62|63|64|65)[0-9]{8}", "Wurth Electronics", new WurthHandler()),
    WURTH("(?:61|62|63|64|65)[0-9]{8}", "Wurth Electronics", new WurthHandler()),

    MOLEX("(?:43|53|55|67|87|88)[0-9]{4}", "Molex", new MolexHandler()),
    //TE("(?:1-|2-)[0-9]{6}|(?:282837|5-826)", "TE Connectivity", new TEHandler()),
    //TE("(?:1-|2-)[0-9]{6}|(?:282[0-9]{3})-[0-9]|(?:5-826)", "TE Connectivity", new TEHandler()),
    //TE("(?:1-|2-)[0-9]{6}|(?:282836|282837|282838|282842)-[0-9]+", "TE Connectivity", new TEHandler()),
    TE("(?:(?:1-|2-)[0-9]{6}|(?:282[0-9]{3}))-[0-9]+", "TE Connectivity", new TEHandler()) ,

    JST("(?:B|S|P|X)[H,M][0-9]|(?:PA|PH|SM|EH)", "JST", new JSTHandler()),
    HIROSE("(?:DF|FH|BM|ZX|GT)[0-9]", "Hirose Electric", new HiroseHandler()),
    AMPHENOL("(?:10|20|54)[0-9]{4}|(?:G5|UE)", "Amphenol", new AmphenolHandler()),

    // LED Manufacturers
    CREE("(?:XL|XH|XP|XQ|XB|CL)[A-Z][0-9]", "Cree", new CreeHandler()),
    OSRAM("(?:LS|LA|LW|LY|LO|LB)[A-Z][0-9]", "OSRAM", new OSRAMHandler()),
    LUMILEDS("(?:L|P)[X,T][H,L][0-9]|LUXEON", "Lumileds", new LumiledsHandler()),

    // Sensor Manufacturers
    BOSCH("(?:BM[A-Z][0-9]|BSH|BMP|BME|BNO)", "Bosch Sensortec", new BoschHandler()),
    MELEXIS("(?:MLX|TMF)[0-9]", "Melexis", new MelexisHandler()),
    INVSENSE("(?:MPU|ICM|IAM|IIM)[0-9]", "InvenSense", new InvSenseHandler()),

    // Memory Manufacturers
    MICRON("(?:MT)[0-9]|(?:N25Q|M25P)", "Micron Technology", new MicronHandler()),
    WINBOND("(?:W)[0-9]{2}[A-Z]|(?:W25Q|W25X)", "Winbond", new WinbondHandler()),
    ISSI("(?:IS)[0-9]|(?:IS25LP|IS25WP)", "ISSI", new ISSIHandler()),

    // RF/Wireless Manufacturers
    NORDIC("(?:nRF)[0-9]|(?:NRF|nRF52)", "Nordic Semiconductor", new NordicHandler()),
    QUALCOMM("(?:QCA|IPQ|MDM|WCN)[0-9]", "Qualcomm", new QualcommHandler()),
    SKYWORKS("(?:SKY|SE|SI)[0-9]", "Skyworks Solutions", new SkyworksHandler()),
    QORVO("(?:RF|RFX|TQP)[0-9]", "Qorvo", new QorvoHandler()),

    // Crystal/Oscillator Manufacturers
    EPSON("(?:SG|FA|TSX|TC|MA)[0-9]", "Epson", new EpsonHandler()),
    NDK("(?:NX|NT|NZ|NH)[0-9]", "NDK", new NDKHandler()),
    ABRACON("(?:ABM|ABLS|ABLM|AB26|ASDM)", "Abracon", new AbraconHandler()),
    IQD("(?:LFXTAL|CFPS|LFTCXO|LFOCA)", "IQD Frequency Products", new IQDHandler()),

    // Unknown (must be last)
    UNKNOWN("", "Unknown Manufacturer", new UnknownHandler());

    private final String code;
    private final String name;
    private final ManufacturerHandler handler;
    private final PatternRegistry patterns;
    private final Set<ManufacturerComponentType> manufacturerTypes;

    ComponentManufacturer(String code, String name, ManufacturerHandler handler) {
        this.code = code;
        this.name = name;
        this.handler = handler;
        this.patterns = new PatternRegistry();
        this.manufacturerTypes = handler.getManufacturerTypes();
        handler.initializePatterns(this.patterns);
    }

    public ManufacturerHandler getHandler() {
        return handler;
    }

    public PatternRegistry getPatterns() {
        return patterns;
    }


    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Set<ComponentType> getSupportedTypes() {
        //return patterns.getSupportedTypes();
        return getHandler().getSupportedTypes();
    }

    public boolean supportsType(ComponentType type) {
        if (type == null) return false;

        // Check direct support
        if (patterns.hasPattern(type)) return true;

        // Check if manufacturer supports any specific type that has this base type
        ComponentType baseType = type.getBaseType();
        return getSupportedTypes().stream()
                .anyMatch(t -> t.getBaseType() == baseType);
    }

    public boolean matchesPattern(String mpn, ComponentType type) {
        if (mpn == null || mpn.isEmpty() || type == null) {
            return false;
        }

        // Delegate to handler's matches method
        return handler.matches(mpn, type, patterns);
    }

    public String extractPackageCode(String mpn) {
        return handler.extractPackageCode(mpn);
    }

    public String extractSeries(String mpn) {
        return handler.extractSeries(mpn);
    }

    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        return handler.isOfficialReplacement(mpn1, mpn2);
    }

    public Pattern getPattern(ComponentType type) {
        return patterns.getPattern(type);
    }

    private boolean matchesAnyPattern(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return false;
        }

        return getSupportedTypes().stream()
                .anyMatch(type -> matchesPattern(mpn, type));
    }

    public static ComponentManufacturer[] getManufacturersForType(ComponentType type) {
        if (type == null) return new ComponentManufacturer[0];

        return Arrays.stream(values())
                .filter(m -> m != UNKNOWN && m.supportsType(type))
                .toArray(ComponentManufacturer[]::new);
    }

    public static ComponentManufacturer fromMPN(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return UNKNOWN;
        }

        String upperMpn = mpn.toUpperCase();

        // Microcontroller special cases - highest priority
        if (upperMpn.startsWith("DSPIC") || upperMpn.startsWith("dsPIC")) {
            return MICROCHIP;
        }
        if (upperMpn.startsWith("PIC")) {
            return MICROCHIP;
        }
        if (upperMpn.startsWith("STM32") || upperMpn.startsWith("STM8")) {
            return ST;
        }
        if (upperMpn.startsWith("ATMEGA") || upperMpn.startsWith("ATTINY")) {
            return ATMEL;
        }
        if (upperMpn.startsWith("MSP430")) {
            return TI;
        }
        if (upperMpn.startsWith("ESP32") || upperMpn.startsWith("ESP8266")) {
            return ESPRESSIF;
        }

        // Diode special cases
        if (upperMpn.matches("^1N[0-9].*")) {
            if (upperMpn.matches("^1N400[1-7].*")) return VISHAY;  // 1N400x rectifier series
            if (upperMpn.matches("^1N414[8].*")) return VISHAY;    // 1N4148 switching diode
            if (upperMpn.matches("^1N47[0-9]{2}.*")) return ON_SEMI; // 1N47xx Zener series
            return VISHAY; // Default for other 1N series
        }

        // Other diode patterns
        if (upperMpn.matches("^(BAT|BAS|BAV)[0-9].*")) {
            if (upperMpn.contains("VISHAY")) return VISHAY;
            if (upperMpn.contains("ON")) return ON_SEMI;
            if (upperMpn.contains("NXP")) return NXP;
            return VISHAY;  // Default to Vishay if no specific manufacturer
        }

        // Passive component special cases
        if (upperMpn.startsWith("CRCW")) {
            return VISHAY;
        }
        if ((upperMpn.startsWith("RC") && upperMpn.matches("^RC[0-9]{4}.*")) ||
                (upperMpn.startsWith("RT") && upperMpn.matches("^RT[0-9]{4}.*")) ||
                (upperMpn.startsWith("RL") && upperMpn.matches("^RL[0-9]{4}.*"))) {
            return YAGEO;
        }
        if (upperMpn.startsWith("ERJ") || upperMpn.startsWith("ERJP") || upperMpn.startsWith("ERJG")) {
            return PANASONIC;
        }
        if (upperMpn.startsWith("GRM") || upperMpn.startsWith("LQG") || upperMpn.startsWith("LQW")) {
            return MURATA;
        }

        // Connector special cases
        if (upperMpn.matches("^61[0-9]{8}") || upperMpn.matches("^62[0-9]{8}")) {
            return WURTH;
        }
        if (upperMpn.matches("^(43|53|55)[0-9]{4}")) {
            return MOLEX;
        }
        if (upperMpn.matches("^(1-|2-)[0-9]{6}")) {
            return TE;
        }
        if (upperMpn.matches("^(PH|EH|XH|ZH)[0-9].*")) {
            return JST;
        }

        // Semiconductor special cases
        if (upperMpn.startsWith("LM") || upperMpn.startsWith("TL") || upperMpn.startsWith("TPS")) {
            if (upperMpn.contains("ST")) return ST;
            return TI;  // Default to TI for these series
        }
        if (upperMpn.startsWith("IRF") || upperMpn.startsWith("IRL")) {
            return INFINEON;
        }

        // Try manufacturer-specific prefix patterns
        for (ComponentManufacturer mfr : values()) {
            if (!mfr.code.isEmpty() && upperMpn.matches("^(?i)" + mfr.code + ".*")) {
                return mfr;
            }
        }

        // Advanced pattern matching for components without clear prefixes
        if (upperMpn.matches(".*V(CC|DD|EE|SS|IO).*")) {
            if (upperMpn.contains("TI")) return TI;
            if (upperMpn.contains("ST")) return ST;
            if (upperMpn.contains("ON")) return ON_SEMI;
        }

        // Try pattern matching for component types
        for (ComponentManufacturer mfr : values()) {
            if (mfr != UNKNOWN && mfr.matchesAnyPattern(upperMpn)) {
                return mfr;
            }
        }

        // Additional generic patterns with manufacturer indicators
        if (upperMpn.contains("-ST")) return ST;
        if (upperMpn.contains("-TI")) return TI;
        if (upperMpn.contains("-NXP")) return NXP;
        if (upperMpn.contains("-INF")) return INFINEON;
        if (upperMpn.contains("-ON")) return ON_SEMI;
        if (upperMpn.contains("-VISHAY")) return VISHAY;

        return UNKNOWN;
    }

    public static ComponentManufacturer[] getPossibleManufacturers(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return new ComponentManufacturer[]{UNKNOWN};
        }

        String upperMpn = mpn.toUpperCase();
        Set<ComponentManufacturer> matches = new HashSet<>();
        Set<ComponentManufacturer> highConfidence = new HashSet<>();
        Set<ComponentManufacturer> mediumConfidence = new HashSet<>();
        Set<ComponentManufacturer> lowConfidence = new HashSet<>();

        // Microcontroller special cases - Highest priority
        if (upperMpn.startsWith("DSPIC") || upperMpn.startsWith("dsPIC") || upperMpn.startsWith("PIC")) {
            return new ComponentManufacturer[]{MICROCHIP};
        }
        if (upperMpn.startsWith("STM32") || upperMpn.startsWith("STM8")) {
            return new ComponentManufacturer[]{ST};
        }
        if (upperMpn.startsWith("ATMEGA") || upperMpn.startsWith("ATTINY")) {
            return new ComponentManufacturer[]{ATMEL};
        }
        if (upperMpn.startsWith("MSP430")) {
            return new ComponentManufacturer[]{TI};
        }
        if (upperMpn.startsWith("ESP32") || upperMpn.startsWith("ESP8266")) {
            return new ComponentManufacturer[]{ESPRESSIF};
        }

        // Diode special cases - High confidence
        if (upperMpn.matches("^1N[0-9].*")) {
            if (upperMpn.matches("^1N400[1-7].*") || upperMpn.matches("^1N414[8].*")) {
                highConfidence.add(VISHAY);
                mediumConfidence.add(ON_SEMI);
                mediumConfidence.add(DIODES_INC);
            } else if (upperMpn.matches("^1N47[0-9]{2}.*")) {
                highConfidence.add(ON_SEMI);
                mediumConfidence.add(VISHAY);
                mediumConfidence.add(DIODES_INC);
            }
        }

        // BA-series diodes
        if (upperMpn.matches("^(BAT|BAS|BAV)[0-9].*")) {
            if (upperMpn.contains("VISHAY")) {
                highConfidence.add(VISHAY);
            } else if (upperMpn.contains("ON")) {
                highConfidence.add(ON_SEMI);
            } else if (upperMpn.contains("NXP")) {
                highConfidence.add(NXP);
            } else {
                mediumConfidence.add(VISHAY);
                mediumConfidence.add(ON_SEMI);
                mediumConfidence.add(NXP);
            }
        }

        // Passive components - High confidence
        if (upperMpn.startsWith("CRCW")) {
            highConfidence.add(VISHAY);
        }
        if ((upperMpn.startsWith("RC") && upperMpn.matches("^RC[0-9]{4}.*")) ||
                (upperMpn.startsWith("RT") && upperMpn.matches("^RT[0-9]{4}.*")) ||
                (upperMpn.startsWith("RL") && upperMpn.matches("^RL[0-9]{4}.*"))) {
            highConfidence.add(YAGEO);
        }
        if (upperMpn.startsWith("ERJ") || upperMpn.startsWith("ERJP") || upperMpn.startsWith("ERJG")) {
            highConfidence.add(PANASONIC);
        }
        if (upperMpn.startsWith("GRM") || upperMpn.startsWith("LQG") || upperMpn.startsWith("LQW")) {
            highConfidence.add(MURATA);
        }

        // Connector patterns - High confidence
        if (upperMpn.matches("^61[0-9]{8}") || upperMpn.matches("^62[0-9]{8}")) {
            highConfidence.add(WURTH);
        }
        if (upperMpn.matches("^(43|53|55)[0-9]{4}")) {
            highConfidence.add(MOLEX);
        }
        if (upperMpn.matches("^(1-|2-)[0-9]{6}")) {
            highConfidence.add(TE);
        }
        if (upperMpn.matches("^(PH|EH|XH|ZH)[0-9].*")) {
            highConfidence.add(JST);
        }

        // Semiconductor patterns - High/Medium confidence
        if (upperMpn.startsWith("LM") || upperMpn.startsWith("TL") || upperMpn.startsWith("TPS")) {
            if (upperMpn.contains("ST")) {
                highConfidence.add(ST);
                mediumConfidence.add(TI);
            } else {
                highConfidence.add(TI);
                mediumConfidence.add(ST);
            }
            mediumConfidence.add(ON_SEMI);
        }

        // MOSFET patterns
        if (upperMpn.startsWith("IRF") || upperMpn.startsWith("IRL")) {
            highConfidence.add(INFINEON);
            mediumConfidence.add(VISHAY);
            mediumConfidence.add(ST);
        }

        // LED patterns
        if (upperMpn.startsWith("OSRAM") || upperMpn.matches("^(LS|LA|LW|LY)[A-Z][0-9].*")) {
            highConfidence.add(OSRAM);
        }
        if (upperMpn.startsWith("CREE") || upperMpn.matches("^(XP|XR|XT)[A-Z][0-9].*")) {
            highConfidence.add(CREE);
        }

        // Memory patterns
        if (upperMpn.startsWith("MT") || upperMpn.startsWith("N25Q")) {
            highConfidence.add(MICRON);
        }
        if (upperMpn.startsWith("W25") || upperMpn.startsWith("W29")) {
            highConfidence.add(WINBOND);
        }

        // Add manufacturers with matching prefix patterns - Medium confidence
        for (ComponentManufacturer mfr : values()) {
            if (!mfr.code.isEmpty() && upperMpn.matches("^(?i)" + mfr.code + ".*")) {
                if (!highConfidence.contains(mfr)) {
                    mediumConfidence.add(mfr);
                }
            }
        }

        // Add manufacturers with matching component patterns - Low confidence
        for (ComponentManufacturer mfr : values()) {
            if (mfr != UNKNOWN && mfr.matchesAnyPattern(upperMpn)) {
                if (!highConfidence.contains(mfr) && !mediumConfidence.contains(mfr)) {
                    lowConfidence.add(mfr);
                }
            }
        }

        // Check for manufacturer indicators in the part number - Medium confidence
        if (upperMpn.contains("-ST")) mediumConfidence.add(ST);
        if (upperMpn.contains("-TI")) mediumConfidence.add(TI);
        if (upperMpn.contains("-NXP")) mediumConfidence.add(NXP);
        if (upperMpn.contains("-INF")) mediumConfidence.add(INFINEON);
        if (upperMpn.contains("-ON")) mediumConfidence.add(ON_SEMI);
        if (upperMpn.contains("-VISHAY")) mediumConfidence.add(VISHAY);

        // Combine results in order of confidence
        matches.addAll(highConfidence);
        matches.addAll(mediumConfidence);
        matches.addAll(lowConfidence);

        if (matches.isEmpty()) {
            return new ComponentManufacturer[]{UNKNOWN};
        }

        // Convert to array and sort by confidence level
        ComponentManufacturer[] result = new ComponentManufacturer[matches.size()];
        int index = 0;

        // Add high confidence matches first
        for (ComponentManufacturer mfr : highConfidence) {
            result[index++] = mfr;
        }

        // Add medium confidence matches next
        for (ComponentManufacturer mfr : mediumConfidence) {
            if (!highConfidence.contains(mfr)) {
                result[index++] = mfr;
            }
        }

        // Add low confidence matches last
        for (ComponentManufacturer mfr : lowConfidence) {
            if (!highConfidence.contains(mfr) && !mediumConfidence.contains(mfr)) {
                result[index++] = mfr;
            }
        }

        return result;
    }
}