package no.cantara.electronic.component.lib;

import no.cantara.electronic.component.lib.manufacturers.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a manufacturer of electronic components, including:
 * - Semiconductors (MCUs, ICs, discrete components)
 * - Passive components (resistors, capacitors, inductors)
 * - Modules and specialized components
 */
public final class ElectronicComponentManufacturer {
    private final String code;
    private final String name;
    private final ManufacturerHandler handler;
    private final PatternRegistry patterns;
    private final Set<ManufacturerComponentType> manufacturerTypes;

    private ElectronicComponentManufacturer(String code, String name, ManufacturerHandler handler) {
        this.code = Objects.requireNonNull(code, "code must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.handler = Objects.requireNonNull(handler, "handler must not be null");
        this.patterns = new PatternRegistry();
        this.patterns.setCurrentHandlerClass(handler.getClass());
        this.manufacturerTypes = handler.getManufacturerTypes();
        handler.initializePatterns(this.patterns);
    }

    public Set<ComponentType> getSupportedTypes() {
        return patterns.getSupportedTypes();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Set<ManufacturerComponentType> getManufacturerTypes() {
        return manufacturerTypes;
    }

    public PatternRegistry getPatternRegistry() {
        return patterns;
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
        return patterns.matches(mpn.toUpperCase(), type);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectronicComponentManufacturer that = (ElectronicComponentManufacturer) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, code);
    }

    /**
     * Registry for electronic component manufacturers
     */
    public static class Registry {
        private static final Registry INSTANCE = new Registry();
        private final Map<String, ElectronicComponentManufacturer> manufacturersByCode = new HashMap<>();
        private final ElectronicComponentManufacturer unknown;

        private Registry() {
            unknown = register("", "Unknown Manufacturer", new UnknownHandler());

            // Microcontroller Manufacturers
            register("MCP", "Microchip Technology", new MicrochipHandler());
            register("PIC", "Microchip Technology", new MicrochipHandler());  // Add PIC prefix
            register("ST", "STMicroelectronics", new STHandler());
            register("AT", "Atmel", new AtmelHandler());
            register("TI", "Texas Instruments", new TIHandler());
            register("R", "Renesas Electronics", new RenesasHandler());
            register("NXP", "NXP Semiconductors", new NXPHandler());
            register("CY", "Cypress Semiconductor", new CypressHandler());
            register("SI", "Silicon Labs", new SiliconLabsHandler());
            register("ESP", "Espressif Systems", new EspressifHandler());

            // Semiconductor Manufacturers
            register("INF", "Infineon Technologies", new InfineonHandler());
            register("VSH", "Vishay", new VishayHandler());
            register("ON", "ON Semiconductor", new OnSemiHandler());
            register("AD", "Analog Devices", new AnalogDevicesHandler());
            register("MAX", "Maxim Integrated", new MaximHandler());
            register("DI", "Diodes Incorporated", new DiodesIncHandler());
            register("ROHM", "ROHM Semiconductor", new RohmHandler());
            register("TOSH", "Toshiba", new ToshibaHandler());
            register("NEX", "Nexperia", new NexteriaHandler());

            // Passive Component Manufacturers
            register("RC", "Yageo", new YageoHandler());
            register("PAN", "Panasonic", new PanasonicHandler());
            register("BOU", "Bourns", new BournsHandler());
            register("KEM", "KEMET Electronics", new KemetHandler());
            register("MUR", "Murata Manufacturing", new MurataHandler());
            register("TDK", "TDK Corporation", new TDKHandler());
            register("SAM", "Samsung Electro-Mechanics", new SamsungHandler());
            register("NIC", "Nichicon", new NichiconHandler());
            register("AVX", "AVX Corporation", new AVXHandler());

            // Crystal/Oscillator Manufacturers
            register("EPS", "Epson", new EpsonHandler());
            register("NDK", "NDK", new NDKHandler());
            register("ABR", "Abracon", new AbraconHandler());
            register("IQD", "IQD Frequency Products", new IQDHandler());

            // Connector Manufacturers
            register("WE", "Wurth Electronics", new WurthHandler());
            register("TYC", "TE Connectivity", new TEHandler());
            register("MOL", "Molex", new MolexHandler());
            register("JST", "JST", new JSTHandler());
            register("HIR", "Hirose", new HiroseHandler());
            register("AMP", "Amphenol", new AmphenolHandler());

            // LED Manufacturers
            register("CREE", "Cree, Inc.", new CreeHandler());
            register("LG", "LG Electronics", new LGHandler());
            register("OSR", "OSRAM Opto", new OSRAMHandler());
            register("LUMI", "Lumileds", new LumiledsHandler());

            // Memory Manufacturers
            register("WB", "Winbond Electronics", new WinbondHandler());
            register("ISSI", "ISSI", new ISSIHandler());
            register("SPD", "Spansion", new SpansionHandler());
            register("MIC", "Micron Technology", new MicronHandler());

            // Sensor Manufacturers
            register("BONE", "Bosch Sensortec", new BoschHandler());
            register("STM", "STMicroelectronics", new STHandler());
            register("AKM", "Asahi Kasei Microdevices", new AKMHandler());
            register("INV", "InvenSense", new InvSenseHandler());
            register("MEL", "Melexis", new MelexisHandler());

            // RF/Wireless Manufacturers
            register("SKY", "Skyworks", new SkyworksHandler());
            register("QRV", "Qorvo", new QorvoHandler());
            register("NRD", "Nordic Semiconductor", new NordicHandler());
            register("QCL", "Qualcomm", new QualcommHandler());
            register("BCM", "Broadcom", new BroadcomHandler());
        }

        public static Registry getInstance() {
            return INSTANCE;
        }

        private ElectronicComponentManufacturer register(String code, String name, ManufacturerHandler handler) {
            ElectronicComponentManufacturer manufacturer = new ElectronicComponentManufacturer(code, name, handler);
            manufacturersByCode.put(code, manufacturer);
            return manufacturer;
        }

        public ElectronicComponentManufacturer fromCode(String code) {
            return manufacturersByCode.getOrDefault(code, unknown);
        }

        public ElectronicComponentManufacturer fromMPN(String mpn) {
            if (mpn == null || mpn.isEmpty()) {
                return unknown;
            }

            String upperMpn = mpn.toUpperCase();
            Map<ElectronicComponentManufacturer, Integer> matchScores = new HashMap<>();

            // First identify component family
            ComponentFamily family = ComponentFamily.fromMPN(upperMpn);
            Set<String> primaryManufacturers = family != null ?
                    family.getPrimaryManufacturers() : Collections.emptySet();

            // Special case for Wurth connectors
            if (upperMpn.matches("^61[0-9]{9}.*") ||
                    upperMpn.matches("^62[0-9]{9}.*")) {
                return manufacturersByCode.get("WE");
            }

            // Special cases for known patterns
            if (upperMpn.matches("^SI[0-9].*")) {  // SI-prefix MOSFETs are Vishay
                return manufacturersByCode.get("VSH");
            }
            if (upperMpn.matches("^IRF[0-9].*")) {  // IRF-prefix MOSFETs are Infineon
                return manufacturersByCode.get("INF");
            }

            // Maxim parts
            if (upperMpn.startsWith("DS") || upperMpn.startsWith("MAX")) {
                return manufacturersByCode.get("MAX");
            }

            // Special case for Winbond memories
            if (upperMpn.startsWith("W25") || upperMpn.startsWith("W29")) {
                return manufacturersByCode.get("WB");
            }

            // Special case for Cree LEDs
            if (upperMpn.startsWith("CLV")) {
                return manufacturersByCode.get("CREE");
            }            // Special case for LG LEDs
            if (upperMpn.startsWith("LG ")) {
                return manufacturersByCode.get("LG");
            }

            for (ElectronicComponentManufacturer manufacturer : manufacturersByCode.values()) {
                if (manufacturer == unknown) continue;

                int score = 0;

                // Primary manufacturer bonus
                if (primaryManufacturers.contains(manufacturer.getCode())) {
                    score += 500;
                }

                // Exact prefix match with length bonus
                if (!manufacturer.getCode().isEmpty() && upperMpn.startsWith(manufacturer.getCode())) {
                    score += 100 * manufacturer.getCode().length();
                }

                // Pattern matches with type specificity bonus
                Set<ComponentType> matchingTypes = manufacturer.getSupportedTypes().stream()
                        .filter(type -> manufacturer.matchesPattern(upperMpn, type))
                        .collect(Collectors.toSet());

                for (ComponentType type : matchingTypes) {
                    if (type.name().contains("_" + manufacturer.getCode())) {
                        score += 300;  // Manufacturer-specific type
                    } else if (type.name().contains("_")) {
                        score += 200;  // Any manufacturer-specific type
                    } else {
                        score += 100;  // Generic type
                    }
                }

                // Series and package recognition bonus
                if (manufacturer.extractSeries(upperMpn) != null) {
                    score += 150;
                }
                if (manufacturer.extractPackageCode(upperMpn) != null) {
                    score += 100;
                }

                if (score > 0) {
                    matchScores.put(manufacturer, score);
                }
            }

            return matchScores.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse(unknown);
        }

        public Collection<ElectronicComponentManufacturer> getAllManufacturers() {
            return Collections.unmodifiableCollection(manufacturersByCode.values());
        }

        public Collection<ElectronicComponentManufacturer> getManufacturersForType(ComponentType type) {
            if (type == null) return Collections.emptyList();

            return manufacturersByCode.values().stream()
                    .filter(m -> m != unknown && m.getSupportedTypes().contains(type))
                    .collect(Collectors.toList());
        }
    }
}