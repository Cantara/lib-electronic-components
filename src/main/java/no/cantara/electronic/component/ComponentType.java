package no.cantara.electronic.component;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static no.cantara.electronic.component.ComponentTypeDetector.determineComponentType;


/**
 * Enum representing different types of electronic components
 */
public enum ComponentType {
    // Base types
    RESISTOR(true, false),
    CAPACITOR(true, false),
    INDUCTOR(true, false),
    DIODE(false, true),
    TRANSISTOR(false, true),
    MOSFET(false, true),
    LED(false, true),
    IC(false, true),
    MICROCONTROLLER(false, true),
    OPAMP(false, true),
    VOLTAGE_REGULATOR(false, true),
    CRYSTAL(true, false),
    OSCILLATOR(false, true),
    MEMORY(false, true),
    MEMORY_FLASH(false, true),
    MEMORY_EEPROM(false, true),
    CONNECTOR(false, false),
    ANALOG_IC( false, true),  // Add this if not present
    DIGITAL_IC( false, true),  // Add this for completeness

    // Specific connector manufacturers/types
    CONNECTOR_MOLEX(false, false),
    CONNECTOR_TE(false, false),
    CONNECTOR_JST(false, false),
    CONNECTOR_HIROSE(false, false),
    CONNECTOR_AMPHENOL(false, false),
    CONNECTOR_HARWIN(false, false),
    CONNECTOR_WURTH(false, false),  // Add this if not present

    // Microchip/Atmel
    MICROCONTROLLER_MICROCHIP(false, true),
    MCU_MICROCHIP(false, true),
    MEMORY_MICROCHIP(false, true),
    PIC_MCU(false, true),
    AVR_MCU(false, true),
    MICROCONTROLLER_ATMEL(false, true),
    MCU_ATMEL(false, true),
    MEMORY_ATMEL(false, true),
    TOUCH_ATMEL(false, true),
    CRYPTO_ATMEL(false, true),

    // STMicroelectronics
    MICROCONTROLLER_ST(false, true),
    MCU_ST(false, true),
    MEMORY_ST(false, true),
    OPAMP_ST(false, true),
    MOSFET_ST(false, true),
    VOLTAGE_REGULATOR_LINEAR_ST(false, true),
    VOLTAGE_REGULATOR_SWITCHING_ST(false, true),

    OPAMP_ON(false, true),
    DIODE_ON(false, true),

    KINETIS_MCU(false, true),
    LPC_MCU(false, true),
    IMX_PROCESSOR(false, true),



    // Cypress (now part of Infineon)
    MICROCONTROLLER_CYPRESS(false, true),
    MCU_CYPRESS(false, true),
    MEMORY_CYPRESS(false, true),
    PSOC_MCU(false, true),
    FM_SERIES_MCU(false, true),
    TRAVEO_MCU(false, true),

    // Silicon Labs
    MICROCONTROLLER_SILABS(false, true),
    MCU_SILABS(false, true),
    EFM8_MCU(false, true),
    EFM32_MCU(false, true),
    EFR32_MCU(false, true),

    // Texas Instruments
    MICROCONTROLLER_TI(false, true),
    MCU_TI(false, true),
    MSP430_MCU(false, true),
    C2000_MCU(false, true),
    OPAMP_TI(false, true),
    VOLTAGE_REGULATOR_LINEAR_TI(false, true),
    VOLTAGE_REGULATOR_SWITCHING_TI(false, true),

    LOGIC_IC(false, true),


    // Espressif
    MICROCONTROLLER_ESPRESSIF(false, true),
    MCU_ESPRESSIF(false, true),
    ESP8266_SOC(false, true),
    ESP32_SOC(false, true),
    ESP32_S2_SOC(false, true),
    ESP32_S3_SOC(false, true),
    ESP32_C3_SOC(false, true),
    ESP32_WROOM_MODULE(false, true),
    ESP32_WROVER_MODULE(false, true),



    // Samsung specific
    CAPACITOR_CERAMIC_SAMSUNG(true, false),
    LED_HIGHPOWER_SAMSUNG(false, true),



    // NXP specific
    MICROCONTROLLER_NXP(false, true),
    MCU_NXP(false, true),
    MEMORY_NXP(false, true),
    MOSFET_NXP(false, true),
    TRANSISTOR_NXP(false, true),
    OPAMP_NXP(false, true),


    // Silicon Labs specific
    MICROCONTROLLER_SILICON_LABS(false, true),
    MCU_SILICON_LABS(false, true),


    // Renesas specific
    MICROCONTROLLER_RENESAS(false, true),
    MCU_RENESAS(false, true),
    MEMORY_RENESAS(false, true),
    // Murata
    CAPACITOR_CERAMIC_MURATA(true, false),
    INDUCTOR_CHIP_MURATA(true, false),
    INDUCTOR_POWER_MURATA(true, false),
    EMI_FILTER_MURATA(true, false),
    RESONATOR_MURATA(true, false),
    COMMON_MODE_CHOKE_MURATA(true, false),

    // TDK
    CAPACITOR_CERAMIC_TDK(true, false),
    INDUCTOR_CHIP_TDK(true, false),
    INDUCTOR_POWER_TDK(true, false),
    EMI_FILTER_TDK(true, false),
    FERRITE_BEAD_TDK(true, false),
    COMMON_MODE_CHOKE_TDK(true, false),

    // Yageo
    RESISTOR_CHIP_YAGEO(true, false),
    RESISTOR_THT_YAGEO(true, false),
    CAPACITOR_CERAMIC_YAGEO(true, false),
    INDUCTOR_CHIP_YAGEO(true, false),
    FERRITE_BEAD_YAGEO(true, false),

    // Vishay
    RESISTOR_CHIP_VISHAY(true, false),
    RESISTOR_THT_VISHAY(true, false),
    MOSFET_VISHAY(false, true),
    DIODE_VISHAY(false, true),
    OPTOCOUPLER_VISHAY(false, true),
    TRANSISTOR_VISHAY(false, true),
    // KEMET
    CAPACITOR_CERAMIC_KEMET(true, false),
    CAPACITOR_TANTALUM_KEMET(true, false),
    CAPACITOR_ALUMINUM_KEMET(true, false),
    CAPACITOR_FILM_KEMET(true, false),
    EMI_FILTER_KEMET(true, false),

    // AVX
    CAPACITOR_TANTALUM_AVX(true, false),
    CAPACITOR_CERAMIC_AVX(true, false),
    CAPACITOR_FILM_AVX(true, false),
    CAPACITOR_POLYMER_AVX(true, false),
    SUPERCAP_AVX(true, false),
    FILTER_AVX(false, true),

    // Panasonic
    RESISTOR_CHIP_PANASONIC(true, false),
    CAPACITOR_CERAMIC_PANASONIC(true, false),
    CAPACITOR_ELECTROLYTIC_PANASONIC(true, false),
    CAPACITOR_FILM_PANASONIC(true, false),
    INDUCTOR_PANASONIC(true, false),

    // Nichicon
    CAPACITOR_ELECTROLYTIC_NICHICON(true, false),
    CAPACITOR_FILM_NICHICON(true, false),
    SUPERCAP_NICHICON(true, false),

    // Bourns
    RESISTOR_CHIP_BOURNS(true, false),
    INDUCTOR_CHIP_BOURNS(true, false),
    INDUCTOR_THT_BOURNS(true, false),
    POTENTIOMETER_BOURNS(true, false),
    TRANSFORMER_BOURNS(true, false),
    CIRCUIT_PROTECTION_BOURNS(false, true),



    // Infineon
    MOSFET_INFINEON(false, true),
    IGBT_INFINEON(false, true),
    VOLTAGE_REGULATOR_LINEAR_INFINEON(false, true),
    VOLTAGE_REGULATOR_SWITCHING_INFINEON(false, true),
    LED_DRIVER_INFINEON(false, true),
    GATE_DRIVER_INFINEON(false, true),
    OPAMP_INFINEON(false, true),
    MICROCONTROLLER_INFINEON(false, true),
    MCU_INFINEON(false, true),
    MEMORY_INFINEON(false, true),


    // ON Semiconductor
    MOSFET_ONSEMI(false, true),
    IGBT_ONSEMI(false, true),
    VOLTAGE_REGULATOR_LINEAR_ON(false, true),
    VOLTAGE_REGULATOR_SWITCHING_ON(false, true),
    LED_DRIVER_ONSEMI(false, true),
    MOTOR_DRIVER_ONSEMI(false, true),

    // Nexperia
    MOSFET_NEXPERIA(false, true),
    LOGIC_IC_NEXPERIA(false, true),
    ESD_PROTECTION_NEXPERIA(false, true),
    BIPOLAR_TRANSISTOR_NEXPERIA(false, true),

    // ROHM Semiconductor
    MOSFET_ROHM(false, true),
    DIODE_ROHM(false, true),
    LED_ROHM(false, true),
    LED_DRIVER_ROHM(false, true),
    OPAMP_ROHM(false, true),
    VOLTAGE_REGULATOR_ROHM(false, true),
    MOTOR_DRIVER_ROHM(false, true),

    // Analog Devices
    OPAMP_AD(false, true),
    ADC_AD(false, true),
    DAC_AD(false, true),
    AMPLIFIER_AD(false, true),
    VOLTAGE_REFERENCE_AD(false, true),
    TEMPERATURE_SENSOR_AD(false, true),
    ACCELEROMETER_AD(false, true),
    GYROSCOPE_AD(false, true),

    // Maxim Integrated (now part of Analog Devices)
    INTERFACE_IC_MAXIM(false, true),
    VOLTAGE_REGULATOR_MAXIM(false, true),
    RTC_MAXIM(false, true),
    TEMPERATURE_SENSOR_MAXIM(false, true),
    BATTERY_MANAGEMENT_MAXIM(false, true),

    // Diodes Incorporated
    MOSFET_DIODES(false, true),
    VOLTAGE_REGULATOR_DIODES(false, true),
    LED_DRIVER_DIODES(false, true),
    LOGIC_IC_DIODES(false, true),
    HALL_SENSOR_DIODES(false, true),

    // Toshiba
    MOSFET_TOSHIBA(false, true),
    IGBT_TOSHIBA(false, true),
    MOTOR_DRIVER_TOSHIBA(false, true),
    GATE_DRIVER_TOSHIBA(false, true),
    OPTOCOUPLER_TOSHIBA(false, true),
    VOLTAGE_REGULATOR_TOSHIBA(false, true),
    MICROCONTROLLER_TOSHIBA(false, true),

    // Epson
    CRYSTAL_EPSON(true, false),
    OSCILLATOR_EPSON(false, true),
    OSCILLATOR_TCXO_EPSON(false, true),
    OSCILLATOR_VCXO_EPSON(false, true),
    OSCILLATOR_OCXO_EPSON(false, true),
    RTC_EPSON(false, true),
    TIMER_EPSON(false, true),
    GYRO_SENSOR_EPSON(false, true),

    // NDK
    CRYSTAL_NDK(true, false),
    OSCILLATOR_NDK(false, true),
    OSCILLATOR_TCXO_NDK(false, true),
    OSCILLATOR_VCXO_NDK(false, true),
    OSCILLATOR_OCXO_NDK(false, true),
    SAW_FILTER_NDK(false, true),
    SAW_RESONATOR_NDK(false, true),

    // Abracon
    CRYSTAL_ABRACON(true, false),
    OSCILLATOR_ABRACON(false, true),
    OSCILLATOR_TCXO_ABRACON(false, true),
    OSCILLATOR_VCXO_ABRACON(false, true),
    OSCILLATOR_OCXO_ABRACON(false, true),
    CLOCK_ABRACON(false, true),
    ANTENNA_ABRACON(true, false),
    RF_FILTER_ABRACON(false, true),



    // IQD
    CRYSTAL_IQD(true, false),
    OSCILLATOR_IQD(false, true),
    OSCILLATOR_TCXO_IQD(false, true),
    OSCILLATOR_VCXO_IQD(false, true),
    OSCILLATOR_OCXO_IQD(false, true),
    CRYSTAL_FILTER_IQD(true, false),
    RTC_MODULE_IQD(false, true),

    // Coilcraft types
    INDUCTOR_CHIP_COILCRAFT(true, false),

    // LED manufacturer types
    LED_STANDARD_KINGBRIGHT(false, true),
    LED_RGB_KINGBRIGHT(false, true),
    LED_SMD_KINGBRIGHT(false, true),
    LED_STANDARD_LITEON(false, true),
    LED_RGB_LITEON(false, true),
    LED_SMD_LITEON(false, true),
    LED_STANDARD_VISHAY(false, true),
    LED_RGB_VISHAY(false, true),
    LED_SMD_VISHAY(false, true),
    LED_STANDARD_WURTH(false, true),
    LED_RGB_WURTH(false, true),
    LED_SMD_WURTH(false, true),
    LED_HIGHPOWER_CREE(false, true),
    LED_HIGHPOWER_LUMILEDS(false, true),
    LED_HIGHPOWER_OSRAM(false, true),
    LED_TI(false, true),

    MEMORY_FLASH_WINBOND(false, true),
    MEMORY_EEPROM_WINBOND(false, true),



    // Circuit Protection
    FUSE_LITTELFUSE(true, false),
    VARISTOR_LITTELFUSE(false, true),
    FUSE_BUSSMANN(true, false),
    FUSE_SCHURTER(true, false),
    TVS_DIODE_BOURNS(false, true),
    PPTC_FUSE_BOURNS(true, false),

    // RF/Wireless
    RF_IC_SKYWORKS(false, true),
    RF_IC_QORVO(false, true),
    BLUETOOTH_IC_NORDIC(false, true),
    WIFI_IC_QUALCOMM(false, true),

    // Sensors
    SENSOR(false, true),
    TEMPERATURE_SENSOR(false, true),
    HUMIDITY_SENSOR(false, true),
    PRESSURE_SENSOR(false, true),
    ACCELEROMETER(false, true),
    GYROSCOPE(false, true),
    MAGNETOMETER(false, true),

    // Add this with the other sensor types
    ACCELEROMETER_BOSCH(false, true),  // Note: corrected from BONE to BOSCH
    // Memory types by manufacturer
    MEMORY_MAXIM(false, true),     // Add this line

    MEMORY_MICRON(false, true),

    // Add this with the other sensor types
    // Add other Bosch-specific sensor types with the other sensor types
    GYROSCOPE_BOSCH(false, true),
    IMU_BOSCH(false, true),          // For BMI series
    MAGNETOMETER_BOSCH(false, true),
    PRESSURE_SENSOR_BOSCH(false, true),
    HUMIDITY_SENSOR_BOSCH(false, true),
    TEMPERATURE_SENSOR_BOSCH(false, true),
    GAS_SENSOR_BOSCH(false, true),   // For BME68x series
    // Memory types by manufacturer
    MEMORY_ISSI(false, true),      // Add this new line
    // Bosch-specific sensor types
    // Temperature sensors by manufacturer
    TEMPERATURE_SENSOR_TI(false, true),
    TEMPERATURE_SENSOR_ST(false, true),
    TEMPERATURE_SENSOR_NXP(false, true),

    // LED manufacturer types
    LED_STANDARD_OSRAM(false, true),
    LED_RGB_OSRAM(false, true),
    LED_SMD_OSRAM(false, true),
    // Generic catch-all type
    GENERIC(false, false);





    private final boolean passive;
    private final boolean semiconductor;

    ComponentType(boolean passive, boolean semiconductor) {
        this.passive = passive;
        this.semiconductor = semiconductor;
    }

    public boolean isPassive() {
        return passive;
    }

    public boolean isSemiconductor() {
        return semiconductor;
    }

    /**
     * Returns the base type for specialized components
     */
    public ComponentType getBaseType() {
        return switch (this) {
            // Microcontroller types
            case MICROCONTROLLER_MICROCHIP, MICROCONTROLLER_ST,
                 MICROCONTROLLER_ATMEL, MICROCONTROLLER_TI,
                 MICROCONTROLLER_INFINEON, MICROCONTROLLER_NXP,
                 MICROCONTROLLER_RENESAS, MICROCONTROLLER_CYPRESS,  // Added for Cypress
                 MCU_MICROCHIP, MCU_ST, MCU_ATMEL, MCU_TI,
                 MCU_INFINEON, MCU_NXP, MCU_RENESAS, MCU_CYPRESS  // Added for Cypress
                    -> MICROCONTROLLER;

            // MOSFET types
            case MOSFET_INFINEON, MOSFET_ST, MOSFET_VISHAY,
                 MOSFET_ONSEMI, MOSFET_NEXPERIA, MOSFET_ROHM -> MOSFET;

            // Memory types
            case MEMORY_MICROCHIP, MEMORY_ATMEL, MEMORY_ST,
                 MEMORY_INFINEON, MEMORY_NXP -> MEMORY;

            // OpAmp types
            case OPAMP_TI, OPAMP_ST, OPAMP_AD, OPAMP_INFINEON -> OPAMP;

            // Resistor types
            case RESISTOR_CHIP_VISHAY, RESISTOR_CHIP_YAGEO,
                 RESISTOR_CHIP_PANASONIC, RESISTOR_CHIP_BOURNS,
                 RESISTOR_THT_VISHAY, RESISTOR_THT_YAGEO -> RESISTOR;

            // Capacitor types
            case CAPACITOR_CERAMIC_KEMET, CAPACITOR_CERAMIC_MURATA,
                 CAPACITOR_CERAMIC_TDK, CAPACITOR_CERAMIC_SAMSUNG,
                 CAPACITOR_ELECTROLYTIC_PANASONIC, CAPACITOR_ELECTROLYTIC_NICHICON,
                 CAPACITOR_TANTALUM_KEMET, CAPACITOR_TANTALUM_AVX -> CAPACITOR;

            // Inductor types
            case INDUCTOR_CHIP_TDK, INDUCTOR_CHIP_MURATA,
                 INDUCTOR_CHIP_BOURNS, INDUCTOR_CHIP_COILCRAFT,
                 INDUCTOR_THT_BOURNS -> INDUCTOR;

            // LED types
            case LED_STANDARD_KINGBRIGHT, LED_STANDARD_LITEON,
                 LED_STANDARD_VISHAY, LED_STANDARD_WURTH,
                 LED_RGB_KINGBRIGHT, LED_RGB_LITEON,
                 LED_RGB_VISHAY, LED_RGB_WURTH,
                 LED_SMD_KINGBRIGHT, LED_SMD_LITEON,
                 LED_SMD_VISHAY, LED_SMD_WURTH,
                 LED_HIGHPOWER_CREE, LED_HIGHPOWER_LUMILEDS,
                 LED_HIGHPOWER_OSRAM, LED_HIGHPOWER_SAMSUNG -> LED;

            // Voltage regulator types
            case VOLTAGE_REGULATOR_LINEAR_TI, VOLTAGE_REGULATOR_LINEAR_ST,
                 VOLTAGE_REGULATOR_LINEAR_INFINEON, VOLTAGE_REGULATOR_LINEAR_ON,
                 VOLTAGE_REGULATOR_SWITCHING_TI, VOLTAGE_REGULATOR_SWITCHING_ST,
                 VOLTAGE_REGULATOR_SWITCHING_INFINEON, VOLTAGE_REGULATOR_SWITCHING_ON -> VOLTAGE_REGULATOR;

            // Crystal/Oscillator types
            case CRYSTAL_EPSON, CRYSTAL_NDK,
                 CRYSTAL_ABRACON, CRYSTAL_IQD -> CRYSTAL;
            case OSCILLATOR_EPSON, OSCILLATOR_NDK,
                 OSCILLATOR_ABRACON, OSCILLATOR_IQD -> OSCILLATOR;

            // Default case for base types
            default -> this;
        };

    }

    /**
     * Check if two component types are from the same family
     */
    public boolean isSameFamily(ComponentType other) {
        if (other == null) return false;
        return this.getBaseType() == other.getBaseType();
    }

    /**
     * Get the manufacturer associated with this component type
     */
    public ComponentManufacturer getManufacturer() {
        String[] parts = name().split("_");
        if (parts.length > 1) {
            String mfrName = parts[parts.length - 1];
            return Arrays.stream(ComponentManufacturer.values())
                    .filter(m -> m.getName().toUpperCase().contains(mfrName))
                    .findFirst()
                    .orElse(ComponentManufacturer.UNKNOWN);
        }
        return ComponentManufacturer.UNKNOWN;
    }


    /**
     * Determine component type from MPN
     */
    public static ComponentType fromMPN(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return GENERIC;
        }

        // First try manufacturer-specific detection
        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(mpn);
        if (manufacturer != ComponentManufacturer.UNKNOWN) {
            for (ComponentType type : values()) {
                if (manufacturer.matchesPattern(mpn, type)) {
                    return type;
                }
            }
        }

        // Add specific detection for common op-amps
        if (mpn.toUpperCase().matches("^(LM358|LM324|MC1458|TL072|TL082|NE5532).*")) {
            return OPAMP;
        }

        // If no manufacturer match, try generic detection
        return detectGenericType(mpn);
    }

    private static ComponentType detectGenericType(String mpn) {
        String upperMpn = mpn.toUpperCase();

        // Prioritize op-amp detection for LM series
        if (upperMpn.startsWith("LM") &&
                (upperMpn.startsWith("LM358") || upperMpn.startsWith("LM324"))) {
            return OPAMP;
        }

        // Capacitor patterns
        if (upperMpn.matches("^(C[0-9]|CC[0-9]|GRM|GCM|KCA|KC[ABMZ]|LLL|NFM).*")) {
            return CAPACITOR;
        }

        // Add Murata-specific capacitor detection
        if (upperMpn.startsWith("GRM") || // Ceramic chip capacitors
                upperMpn.startsWith("GCM") || // Ceramic chip capacitors (automotive)
                upperMpn.startsWith("KCA") || // Film capacitors
                upperMpn.startsWith("LLL") || // Polymer aluminum electrolytic
                upperMpn.startsWith("NFM"))   // EMI suppression filters/capacitors
        {
            return CAPACITOR;
        }

        // Add Würth connector detection
        if (upperMpn.matches("^61[0-9]{8}")) {
            return CONNECTOR_WURTH;
        }

        if (upperMpn.matches("^R[0-9].*|^RC[0-9].*")) return RESISTOR;
        if (upperMpn.matches("^C[0-9].*|^CC[0-9].*")) return CAPACITOR;
        if (upperMpn.matches("^L[0-9].*")) return INDUCTOR;
        if (upperMpn.matches("^(1N|BAV|BAS|BAT)[0-9].*")) return DIODE;
        if (upperMpn.matches("^(BC|2N|PN|2SC|2SA)[0-9].*")) return TRANSISTOR;
        if (upperMpn.matches("^(IRF|IRL|FQP|FDS)[0-9].*")) return MOSFET;
        if (upperMpn.matches("^(PIC|STM32|ATMEGA)[0-9].*")) return MICROCONTROLLER;
        if (upperMpn.matches("^(LM|TL|NE|UA|AD)[0-9].*")) return OPAMP;
        if (upperMpn.matches("^LED.*|^(WP|L-|LTL)[0-9].*")) return LED;
        if (upperMpn.matches("^Y[0-9].*|^(HC|Q)[0-9].*")) return CRYSTAL;

        return GENERIC;
    }

    /**
     * Get all possible component types that match the given MPN
     */
    public static ComponentType[] getPossibleTypes(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return new ComponentType[]{GENERIC};
        }

        Set<ComponentType> matchingTypes = new HashSet<>();
        String upperMpn = mpn.toUpperCase();

        // First try manufacturer-specific detection
        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(upperMpn);
        if (manufacturer != ComponentManufacturer.UNKNOWN) {
            // Add manufacturer-specific types
            for (ComponentType type : values()) {
                if (manufacturer.matchesPattern(upperMpn, type)) {
                    matchingTypes.add(type);
                    // Also add the base type
                    matchingTypes.add(type.getBaseType());
                }
            }
        }

        // Add Würth connector detection
        if (upperMpn.matches("^61[0-9]{8}")) {
            matchingTypes.add(CONNECTOR);
            matchingTypes.add(CONNECTOR_WURTH);
        }

        // Add generic type detection
        ComponentType genericType = detectGenericType(upperMpn);
        if (genericType != GENERIC) {
            matchingTypes.add(genericType);

            // Add related manufacturer-specific types
            for (ComponentType type : values()) {
                if (type.getBaseType() == genericType &&
                        (manufacturer == ComponentManufacturer.UNKNOWN ||
                                type.getManufacturer() == manufacturer)) {
                    matchingTypes.add(type);
                }
            }
        }

        // If no matches found, return GENERIC
        if (matchingTypes.isEmpty()) {
            return new ComponentType[]{GENERIC};
        }

        // Convert set to array and sort by specificity
        return matchingTypes.stream()
                .sorted((a, b) -> {
                    // Put manufacturer-specific types before generic types
                    boolean aIsSpecific = a.name().contains("_");
                    boolean bIsSpecific = b.name().contains("_");
                    if (aIsSpecific && !bIsSpecific) return -1;
                    if (!aIsSpecific && bIsSpecific) return 1;
                    // Sort alphabetically within same specificity level
                    return a.name().compareTo(b.name());
                })
                .toArray(ComponentType[]::new);
    }

    /**
     * Get all types that could be derived from this type
     */
    public ComponentType[] getDerivedTypes() {
        return Arrays.stream(values())
                .filter(type -> type.getBaseType() == this && type != this)
                .toArray(ComponentType[]::new);
    }


    /**
     * Check if this type matches the given MPN
     */
    public boolean matches(String mpn) {
        if (mpn == null || mpn.isEmpty()) {
            return false;
        }

        String upperMpn = mpn.toUpperCase();

        // Check manufacturer-specific patterns first
        ComponentManufacturer manufacturer = ComponentManufacturer.fromMPN(upperMpn);
        if (manufacturer != ComponentManufacturer.UNKNOWN) {
            if (manufacturer.matchesPattern(upperMpn, this)) {
                return true;
            }
        }

        // Then check if it matches as a base type
        return this == fromMPN(upperMpn).getBaseType();
    }

    /**
     * ComponentAnalysis class for detailed component analysis
     */
    public static class ComponentAnalysis {
        private final String category;
        private final String subCategory;
        private final String manufacturer;
        private final boolean passive;
        private final boolean semiconductor;

        public ComponentAnalysis(String category, String subCategory, String manufacturer,
                                 boolean passive, boolean semiconductor) {
            this.category = category;
            this.subCategory = subCategory;
            this.manufacturer = manufacturer;
            this.passive = passive;
            this.semiconductor = semiconductor;
        }

        public String getCategory() { return category; }
        public String getSubCategory() { return subCategory; }
        public String getManufacturer() { return manufacturer; }
        public boolean isPassive() { return passive; }
        public boolean isSemiconductor() { return semiconductor; }
    }

    /**
     * Checks if an MPN represents a MOSFET.
     */
    public static boolean isMosfet(String mpn) {
        ComponentType type = determineComponentType(mpn);
        return type != null && (type == ComponentType.MOSFET || type.getBaseType() == ComponentType.MOSFET);
    }

    /**
     * Checks if a BOM object represents a MOSFET.
     */
    public static boolean isMosfet(ElectronicPart electronicPart) {
        if (electronicPart == null || electronicPart.getMpn()==null) {
            return false;
        }
        return isMosfet(electronicPart.getMpn());
    }

    /**
     * Checks if an MPN represents an analog IC.
     */
    public static boolean isAnalogIC(String mpn) {
        ComponentType type = determineComponentType(mpn);
        if (type == null) return false;

        // Check for analog-specific types
        return type == ComponentType.OPAMP ||
                type == ComponentType.VOLTAGE_REGULATOR ||
                type.getBaseType() == ComponentType.OPAMP ||
                type.getBaseType() == ComponentType.VOLTAGE_REGULATOR;
    }

    /**
     * Checks if an MPN represents a digital IC.
     */
    public static boolean isDigitalIC(String mpn) {
        if (mpn == null || mpn.isEmpty()) return false;

        // Check for common digital IC patterns
        if (mpn.startsWith("74")) return true;
        if (mpn.startsWith("CD4")) return true;

        ComponentType type = determineComponentType(mpn);
        return type != null && type == ComponentType.IC && !isAnalogIC(mpn);
    }



}