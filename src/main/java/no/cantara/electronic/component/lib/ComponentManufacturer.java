package no.cantara.electronic.component.lib;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import no.cantara.electronic.component.lib.manufacturers.*;

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
    NUVOTON("(?:M[0-9]{3}|N76[E,S]|NUC[0-9]|NUA[0-9]|ML5[0-9])", "Nuvoton", new NuvotonHandler()),
    HOLTEK("(?:HT[0-9]{4}|HT66|HT68|BS8)", "Holtek", new HoltekHandler()),
    WCH("(?:CH[0-9]{3}|CH32[V,F])", "WCH", new WCHHandler()),
    ARTERY("(?:AT32F[0-9]|AT32WB)", "Artery Technology", new ArteryHandler()),

    // Semiconductor Manufacturers
    ALPHA_OMEGA("(?:AO[0-9]{4}|AOD|AON|AOI|AOT|AOB|AOC|AOP|AOTL|AOGT|AOGL|AONS|AONR|AONK)", "Alpha and Omega Semiconductor", new AlphaOmegaHandler()),
    INFINEON("(?:INF|IR|IFX|TLE|XMC|ICE|IPD|IRS|BSC|BSD|BSS|BTS|BTT)", "Infineon Technologies", new InfineonHandler()),
    VISHAY("(?:VS|SI|CRCW|IRF|SiR|1N[0-9]|BAV|BAS|BAT|TNR|VOW|VOX|2N)", "Vishay", new VishayHandler()),
    ON_SEMI("(?:ON|MC|NCP|FAN|CAT|NCV|MUR|1N|LM|NTD|NTA|NTB)", "ON Semiconductor", new OnSemiHandler()),
    ANALOG_DEVICES("(?:AD|ADP|ADM|ADG|ADA|ADUM|LT|LTC|LTM)", "Analog Devices", new AnalogDevicesHandler()),
    MAXIM("(?:MAX|DS|ICL|DG|UP|LT|LTC|LTM)", "Maxim Integrated", new MaximHandler()),
    DIODES_INC("(?:DI|AP|AZ|DMG|DMN|DMP|ZXGD|1N|BAV|BAS|BAT)", "Diodes Incorporated", new DiodesIncHandler()),
    GOOD_ARK("(?:1N4|1N5|ES[0-9]|US[0-9]|SS[0-9]{2}|SK[0-9]{2}|MMBT|BAV|BAT)", "Good-Ark Semiconductor", new GoodArkHandler()),
    YANGJIE("(?:YJ|MBR[0-9]{4}|SMBJ|SMAJ|SS[0-9]{2}|SK[0-9]{2}|YJB|YJDB)", "Yangjie Technology", new YangjieHandler()),
    PANJIT("(?:1N4|1N5|ES[0-9]|RS[0-9]|SS[0-9]{2}|SK[0-9]{2}|MMBT|BAV|BAS|BAT|PJ[0-9]{4})", "Panjit International", new PanjitHandler()),
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
    VIKING_TECH("(?:CR[0-9]{4}|AR[0-9]{4}|CS[0-9]{4}|PWR[0-9])", "Viking Tech", new VikingTechHandler()),
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
    LELON("(?:RGA|RGC|RWE|REA|RZC|VYS)[0-9]", "Lelon Electronics", new LelonHandler()),
    RUBYCON("(?:YXF|YXG|ZLH|ZLJ|50YXF|63YXF)[0-9]", "Rubycon", new RubyconHandler()),
    ELNA("(?:RJJ|RJG|RJK|RSH|RAA|RA2|RFS|CE-BP)[0-9]", "Elna", new ElnaHandler()),
    NIPPON_CHEMICON("(?:KMG|KMH|KMQ|KY|KZH|KZN|KXJ|KZE)[0-9]", "Nippon Chemi-Con", new NipponChemiConHandler()),
    WIMA("(?:MKS|MKP|FKS|FKP|FKC)[0-9]", "WIMA", new WIMAHandler()),
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
    CUI("(?:SJ[0-9]|PJ-|CMI|CMS|CPE|CPT|ACZ|AMT)", "CUI Devices", new CUIHandler()),
    JAE("(?:FI-|DX[0-9]|MX[0-9]|IL-)", "JAE Electronics", new JAEHandler()),

    // LED Manufacturers
    CREE("(?:XL|XH|XP|XQ|XB|CL)[A-Z][0-9]", "Cree", new CreeHandler()),
    OSRAM("(?:LS|LA|LW|LY|LO|LB)[A-Z][0-9]", "OSRAM", new OSRAMHandler()),
    LUMILEDS("(?:L|P)[X,T][H,L][0-9]|LUXEON", "Lumileds", new LumiledsHandler()),
    KINGBRIGHT("(?:WP|KP|AP|AA)[0-9]", "Kingbright", new KingbrightHandler()),

    // Sensor Manufacturers
    ALLEGRO("(?:ACS[0-9]|A[0-9]{4}|AH[0-9]{3}|AAS[0-9])", "Allegro MicroSystems", new AllegroHandler()),
    BOSCH("(?:BM[A-Z][0-9]|BSH|BMP|BME|BNO)", "Bosch Sensortec", new BoschHandler()),
    MELEXIS("(?:MLX|TMF)[0-9]", "Melexis", new MelexisHandler()),
    INVSENSE("(?:MPU|ICM|IAM|IIM)[0-9]", "InvenSense", new InvSenseHandler()),

    // Relay/Switch/Sensor Manufacturers
    OMRON("(?:G[0-9]|G3[A-Z]|B3[A-Z]|D2F|EE-S|D6F|E2E)", "Omron", new OmronHandler()),

    // Optocoupler Manufacturers
    ISOCOM("(?:ISP|ISQ|ISD)[0-9]{3}|(?:4N|6N)[0-9]{2}|(?:MOC|TLP)[0-9]{3}", "Isocom Components", new IsocomHandler()),
    COSMO("(?:KP[CSH]?[0-9]{3,4}|KPTR[0-9]{4})", "Cosmo Electronics", new CosmoHandler()),

    // Memory Manufacturers
    MICRON("(?:MT)[0-9]|(?:N25Q|M25P)", "Micron Technology", new MicronHandler()),
    WINBOND("(?:W)[0-9]{2}[A-Z]|(?:W25Q|W25X)", "Winbond", new WinbondHandler()),
    ISSI("(?:IS)[0-9]|(?:IS25LP|IS25WP)", "ISSI", new ISSIHandler()),

    // RF/Wireless Manufacturers
    NORDIC("(?:nRF)[0-9]|(?:NRF|nRF52)", "Nordic Semiconductor", new NordicHandler()),
    QUALCOMM("(?:QCA|IPQ|MDM|WCN)[0-9]", "Qualcomm", new QualcommHandler()),
    SKYWORKS("(?:SKY|SE|SI)[0-9]", "Skyworks Solutions", new SkyworksHandler()),
    QORVO("(?:RF|RFX|TQP)[0-9]", "Qorvo", new QorvoHandler()),
    AIROHA("(?:AB[0-9]{4}|AU[0-9]{4}|AG[0-9]{4})", "Airoha Technology", new AirohaHandler()),
    BEKEN("(?:BK[0-9]{4}|BL[0-9]{4})", "Beken", new BekenHandler()),
    TELINK("(?:TLSR[0-9]{4}|TC[0-9]{2})", "Telink Semiconductor", new TelinkHandler()),
    SEMTECH("(?:SX[0-9]{4}|LR[0-9]{4}|SY[0-9]{4})", "Semtech", new SemtechHandler()),

    // Crystal/Oscillator Manufacturers
    EPSON("(?:SG|FA|TSX|TC|MA)[0-9]", "Epson", new EpsonHandler()),
    NDK("(?:NX|NT|NZ|NH)[0-9]", "NDK", new NDKHandler()),
    ABRACON("(?:ABM|ABLS|ABLM|AB26|ASDM)", "Abracon", new AbraconHandler()),
    IQD("(?:LFXTAL|CFPS|LFTCXO|LFOCA)", "IQD Frequency Products", new IQDHandler()),
    TXC("(?:7[MVX]|8Y|9C|AX)-", "TXC Corporation", new TXCHandler()),
    KYOCERA("(?:CX|KC|CT|CXO|PBRC)[0-9]|(?:5600|5800)-", "Kyocera", new KyoceraHandler()),
    KDS("(?:DSX|DST|DSO|DSB|1N-)[0-9]", "KDS Daishinku", new KDSHandler()),

    // Motor Driver Manufacturers
    TRINAMIC("(?:TMC[0-9]{4})", "Trinamic Motion Control", new TrinamicHandler()),

    // Circuit Protection Manufacturers
    LITTELFUSE("(?:SMAJ|SMBJ|SMCJ|SMDJ|P[46]KE|P4SMA|P6SMB|1\\.?5KE|045[1-4]|0448|V[0-9]{2}[EPDM])", "Littelfuse", new LittelfuseHandler()),
    PROTEK_DEVICES("(?:TVS[0-9]{5}|GBLC|PSM[0-9]{3}|ULC[0-9]{4}|SMD[0-9]{4})", "ProTek Devices", new ProTekDevicesHandler()),

    // Power Supply Manufacturers
    MEAN_WELL("(?:RS|LRS|SE|NES|SP|PS|PT|SD|DDR|LPV|HLG|ELG|PLN|PWM|LCM|HDR|EDR|MDR|NDR|DR)-[0-9]", "Mean Well", new MeanWellHandler()),

    // Interface IC Manufacturers
    FTDI("(?:FT)[0-9]{3}", "FTDI", new FTDIHandler()),
    GENESYS_LOGIC("(?:GL[0-9]{3}|GL3[0-9]{3})", "Genesys Logic", new GenesysLogicHandler()),
    ASMEDIA("(?:ASM[0-9]{4}|ASM1[0-9]{3})", "ASMedia Technology", new ASMediaHandler()),
    PROLIFIC("(?:PL[0-9]{4}|PL23[0-9]{2})", "Prolific Technology", new ProlificHandler()),
    JMICRON("(?:JM[0-9]{3}|JMS[0-9]{3})", "JMicron Technology", new JMicronHandler()),
    VIALABS("(?:VL[0-9]{3}|VL8[0-9]{2})", "VIA Labs", new ViaLabsHandler()),

    // Audio IC Manufacturers
    CIRRUS_LOGIC("(?:CS4|CS5|CS8|WM8)[0-9]", "Cirrus Logic", new CirrusLogicHandler()),
    REALTEK("(?:ALC|RTL|RTD)[0-9]", "Realtek", new RealtekHandler()),
    ESS("(?:ES[0-9]{4}|SABRE)", "ESS Technology", new ESSHandler()),
    CMEDIA("(?:CM[0-9]{3}|CMI[0-9]{4})", "C-Media", new CMediaHandler()),

    // Sensor Manufacturers (additional)
    SENSIRION("(?:SHT|SGP|SCD|SFA|SPS|STS|SLF|SDP)[0-9]", "Sensirion", new SensirionHandler()),
    HONEYWELL("(?:HIH|HSC|SSC|ABP|MPR|SS4|SS5|HMC|HOA|HLC)[0-9]", "Honeywell Sensing", new HoneywellHandler()),
    AMS("(?:AS[3-7]|TSL|TMD|TCS|APDS|ENS)[0-9]", "ams-OSRAM", new AMSHandler()),

    // Inductor/Magnetics Manufacturers
    COILCRAFT("(?:XAL|XEL|XFL|SER|LPS|MSS|DO[0-9]|MSD|SLC|SLR|0[46]0[23]HP)", "Coilcraft", new CoilcraftHandler()),
    SUMIDA("(?:CDRH|CDR|CDEP|CDEF|CR[0-9]|RCH|CEP|CDC|CLF)[0-9]", "Sumida", new SumidaHandler()),
    PULSE_ELECTRONICS("(?:H[0-9]{4}|T[0-9]{4}|P[0-9]{4}|PE-|PA-|JD|JK|JXD)", "Pulse Electronics", new PulseElectronicsHandler()),
    SUNLORD("(?:SDCL|SWPA|SDFL|MWSA|SMDRR)", "Sunlord Electronics", new SunlordHandler()),
    CHILISIN("(?:SQC[0-9]{4}|SCDS[0-9]|SCD[0-9]{4})", "Chilisin Electronics", new ChilisinHandler()),
    CYNTEC("(?:PMC|PBSS|PCMN)[0-9]", "Cyntec", new CyntecHandler()),

    // Power Management Manufacturers
    VICOR("(?:DCM|BCM|PRM|VTM|NBM|PI3[35])[0-9]", "Vicor", new VicorHandler()),
    POWER_INTEGRATIONS("(?:TOP|TNY|LNK|INN|PFS|LCS|CAP|SEN)[0-9]", "Power Integrations", new PowerIntegrationsHandler()),
    MPS("(?:MP[0-9]|MPQ|MPM)[0-9]", "Monolithic Power Systems", new MPSHandler()),
    RICHTEK("(?:RT[0-9]{4}|RTQ[0-9])", "Richtek", new RichtekHandler()),
    SILERGY("(?:SY[0-9]{4}|SY8[0-9])", "Silergy", new SilergyHandler()),
    TOREX("(?:XC[0-9]{4}|XCL[0-9])", "Torex Semiconductor", new TorexHandler()),
    ABLIC("(?:S-[0-9]{5}|S8[0-9]{3})", "ABLIC", new ABLICHandler()),
    SGMICRO("(?:SGM[0-9]{4}|SG[0-9]{3})", "SG Micro", new SGMicroHandler()),
    THREE_PEAK("(?:TP[0-9]{4}|TPF[0-9])", "3PEAK", new ThreePeakHandler()),

    // Memory Manufacturers (additional)
    MACRONIX("(?:MX25|MX29|MX30|MX66)[A-Z]", "Macronix", new MacronixHandler()),
    GIGADEVICE("(?:GD25|GD32|GD5F)[A-Z0-9]", "GigaDevice", new GigaDeviceHandler()),
    ALLIANCE_MEMORY("(?:AS6C|AS7C|AS4C|AS29)[0-9]", "Alliance Memory", new AllianceMemoryHandler()),
    PUYA("(?:P25[A-Z]|PY25[A-Z])", "Puya Semiconductor", new PuyaHandler()),
    XMC("(?:XM25Q|XM25E)", "XMC", new XMCHandler()),
    ESMT("(?:M[0-9]{2}S|F[0-9]{2}L)", "Elite Semiconductor Memory Technology", new ESMTHandler()),

    // Timing/Oscillator Manufacturers (additional)
    SITIME("(?:SiT)[0-9]", "SiTime", new SiTimeHandler()),

    // LED Manufacturers (additional)
    SEOUL_SEMI("(?:Z5|STW|STN|SFH|CUD|MJT|WICOP|SunLike|Acrich)", "Seoul Semiconductor", new SeoulSemiHandler()),
    EVERLIGHT("(?:17-|19-|26-|333|EL8|EL3|IR[0-9]|PT[0-9]|ALS)", "Everlight Electronics", new EverlightHandler()),

    // LED Driver / Display Driver Manufacturers
    MACROBLOCK("(?:MBI[0-9]{4}|MBI5[0-9])", "Macroblock", new MacroblockHandler()),
    CHIPONE("(?:ICN[0-9]{4}|ICND[0-9])", "Chipone Technology", new ChiponeHandler()),
    SITRONIX("(?:ST[0-9]{4}|SSD[0-9]{4})", "Sitronix Technology", new SitronixHandler()),
    RAYDIUM("(?:RM[0-9]{5}|RM6[0-9]{4})", "Raydium Semiconductor", new RaydiumHandler()),
    NOVATEK("(?:NT[0-9]{5}|NVT[0-9])", "Novatek Microelectronics", new NovatekHandler()),

    // Connector Manufacturers (additional)
    PHOENIX_CONTACT("(?:MC |MCV |MSTB|PT |UK |UT |PTSM|SPT |FK-)", "Phoenix Contact", new PhoenixContactHandler()),
    HARTING("(?:09 |21 0|02 0|14 |15 )", "Harting", new HartingHandler()),
    SAMTEC("(?:ESQ|SSQ|TSM|TSW|TLE|SMH|CLT|HSEC|QSH|QTH)", "Samtec", new SamtecHandler()),
    MILL_MAX("(?:[0-9]{3}-[0-9]{2}-[0-9]{3}|[0-9]{4}-[0-9]-[0-9]{2})", "Mill-Max", new MillMaxHandler()),
    SULLINS("(?:PRPC|PPTC|NRPN|SWH|GRPB|NPPC|LPPB)", "Sullins Connector Solutions", new SullinsHandler()),

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