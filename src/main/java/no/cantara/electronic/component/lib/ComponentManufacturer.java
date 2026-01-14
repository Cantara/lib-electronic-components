package no.cantara.electronic.component.lib;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import no.cantara.electronic.component.lib.manufacturers.*;

/**
 * Enum representing electronic component manufacturers with their specific patterns
 */
public enum ComponentManufacturer {
    // Microcontroller Manufacturers (Listed first for priority)
    MICROCHIP("(?:PIC|dsPIC|DSPIC|ATmega|ATtiny|AT[0-9]|AT[A-Z]{2})", "Microchip Technology", MicrochipHandler::new),
    ST("(?:STM32|STM8|ST[A-Z][0-9]|STD|STF|STP)", "STMicroelectronics", STHandler::new),
    ATMEL("(?:AT[0-9]|AT[A-Z]{2}|ATXMEGA)", "Atmel", AtmelHandler::new),
    TI("(?:TI|TPS|TMS|TLV|LM|SN|MSP430|CC[0-9]{4})", "Texas Instruments", TIHandler::new),
    RENESAS("(?:RX|RA|RE|RH|RZ|R[48]F|R5F|R7F)", "Renesas Electronics", RenesasHandler::new),
    NXP("(?:NXP|LPC|MK|IMX|S32K|KE[0-9]|PH|P[A-Z][A-Z]?[0-9])", "NXP Semiconductors", NXPHandler::new),
    CYPRESS("(?:CY|PSoC|FM[0-9]|CYW)", "Cypress Semiconductor", CypressHandler::new),
    SILICON_LABS("(?:SI|EFM|EFR|BGM|EM35|CP21)", "Silicon Labs", SiliconLabsHandler::new),
    ESPRESSIF("(?:ESP[0-9]|ESP-|ESP32)", "Espressif Systems", EspressifHandler::new),
    NUVOTON("(?:M[0-9]{3}|N76[E,S]|NUC[0-9]|NUA[0-9]|ML5[0-9])", "Nuvoton", NuvotonHandler::new),
    HOLTEK("(?:HT[0-9]{4}|HT66|HT68|BS8)", "Holtek", HoltekHandler::new),
    WCH("(?:CH[0-9]{3}|CH32[V,F])", "WCH", WCHHandler::new),
    ARTERY("(?:AT32F[0-9]|AT32WB)", "Artery Technology", ArteryHandler::new),

    // Semiconductor Manufacturers
    ALPHA_OMEGA("(?:AO[0-9]{4}|AOD|AON|AOI|AOT|AOB|AOC|AOP|AOTL|AOGT|AOGL|AONS|AONR|AONK)", "Alpha and Omega Semiconductor", AlphaOmegaHandler::new),
    INFINEON("(?:INF|IR|IFX|TLE|XMC|ICE|IPD|IRS|BSC|BSD|BSS|BTS|BTT)", "Infineon Technologies", InfineonHandler::new),
    VISHAY("(?:VS|SI|CRCW|IRF|SiR|1N[0-9]|BAV|BAS|BAT|TNR|VOW|VOX|2N)", "Vishay", VishayHandler::new),
    ON_SEMI("(?:ON|MC|NCP|FAN|CAT|NCV|MUR|1N|NTD|NTA|NTB)", "ON Semiconductor", OnSemiHandler::new),
    ANALOG_DEVICES("(?:AD|ADP|ADM|ADG|ADA|ADUM|LT|LTC|LTM)", "Analog Devices", AnalogDevicesHandler::new),
    MAXIM("(?:MAX|DS|ICL|DG|UP|LT|LTC|LTM)", "Maxim Integrated", MaximHandler::new),
    DIODES_INC("(?:DI|AP|AZ|DMG|DMN|DMP|ZXGD|1N|BAV|BAS|BAT)", "Diodes Incorporated", DiodesIncHandler::new),
    GOOD_ARK("(?:1N4|1N5|ES[0-9]|US[0-9]|SS[0-9]{2}|SK[0-9]{2}|MMBT|BAV|BAT)", "Good-Ark Semiconductor", GoodArkHandler::new),
    YANGJIE("(?:YJ|MBR[0-9]{4}|SS[0-9]{2}|SK[0-9]{2}|YJB|YJDB)", "Yangjie Technology", YangjieHandler::new),
    PANJIT("(?:1N4|1N5|ES[0-9]|RS[0-9]|SS[0-9]{2}|SK[0-9]{2}|MMBT|BAV|BAS|BAT|PJ[0-9]{4})", "Panjit International", PanjitHandler::new),
    ROHM("(?:BD|BA|BR|BSS|BU|2S[A-D]|RGT|PMR)", "ROHM Semiconductor", RohmHandler::new),
    TOSHIBA("(?:TC|TLP|TPH|TPC|2S[A-D]|TPN|TPR|TPS)", "Toshiba", ToshibaHandler::new),
    NEXPERIA("(?:NEX|PMBT|PBSS|BZX|BAT|BSS|2N|BAS|BAV|BZV|PHB)", "Nexperia", NexteriaHandler::new),
    BROADCOM("(?:BCM|AFBR|HCPL|ACPL|AVAGO)", "Broadcom", BroadcomHandler::new),
    AKM("(?:AK|LC|HM|HA)[0-9]", "Asahi Kasei Microdevices", AKMHandler::new),
    LG("(?:LG)[A-Z][0-9]", "LG Semiconductor", LGHandler::new),
    LOGIC_IC("(?:74|54)[LSAHCTTF]{0,4}[0-9]{2,4}|CD4[0-9]{3}", "Logic IC", LogicICHandler::new),

    // Passive Component Manufacturers
    //YAGEO("(?:RC[0-9]|RT[0-9]|RL[0-9]|CC[0-9]|AC[0-9]|PE[0-9])", "Yageo", YageoHandler::new),
    YAGEO("(?:RC[0-9]|RT[0-9]|RL(?!20[1-7])[0-9]|CC[0-9]|AC[0-9]|PE[0-9])", "Yageo", YageoHandler::new),    PANASONIC("(?:ERJ|ERJP|ERJG|ECQ|EEF|ECA|EVJ|EVM)", "Panasonic", PanasonicHandler::new),
    BOURNS("(?:CR[^C]|CRM|CRH|3386|3296|91|TC|TH|3310)", "Bourns", BournsHandler::new),
    VIKING_TECH("(?:CR[0-9]{4}|AR[0-9]{4}|CS[0-9]{4}|PWR[0-9])", "Viking Tech", VikingTechHandler::new),
    KEMET("(?:C[0-9]{4}|T[0-9]{3}|A[0-9]{3}|ESD|PHE|F[0-9])", "KEMET Electronics", KemetHandler::new),
    //MURATA("(?:GRM|GRT|LQG|LQH|LQW|DLW|NFM|BLM|NCP)", "Murata Manufacturing", MurataHandler::new),
    //MURATA("(?:GRM|GCM|KCA|KC[ABMZ]|LLL|NFM|DLW|BLM|NCP)", "Murata Manufacturing", MurataHandler::new),
//    MURATA("(?:GRM|GCM|KCA|KC[ABMZ]|LLL|NFM|DLW|BLM|NCP)", "Murata Manufacturing", MurataHandler::new),
//    MURATA("(?:GRM|GCM|KCA|KC[ABMZ]|LLL|NFM|DLW|BLM|NCP)", "Murata Manufacturing", MurataHandler::new),
    MURATA("(?:GRM|GCM|KCA|KC[ABMZ]|LLL|NFM|DLW|BLM|NCP|LQM|LQW|LQG)", "Murata Manufacturing", MurataHandler::new),

    //TDK("(?:C[LH]|MPZ|SDR|ALT|NLV|B8[24]|MLG|MMZ)", "TDK Corporation", TDKHandler::new),
//    TDK("(?:CH[0-9]|MPZ|SDR|ALT|NLV|B8[24]|MLG|MMZ)", "TDK Corporation", TDKHandler::new),
    TDK("(?:CH[0-9]|MLF[0-9]|MPZ|SDR|ALT|NLV|B8[24]|MLG|MMZ)", "TDK Corporation", TDKHandler::new),

    //SAMSUNG("(?:CL[0-9]|CM[0-9]|SPH|RC_|CL21|CL31)", "Samsung Electro-Mechanics", SamsungHandler::new),
    //SAMSUNG("(?:CL[0-9]+B|CM[0-9]|SPH|RC_|CL21|CL31)", "Samsung Electro-Mechanics", SamsungHandler::new),NICHICON("(?:UUD|UPW|UVR|UCZ|UMA|UVZ|UHW|UHE)", "Nichicon", NichiconHandler::new),
    //SAMSUNG("(?:CL[0-9]+B|CM[0-9]|SPH|RC_|CL21|CL31)", "Samsung Electro-Mechanics", SamsungHandler::new),
    SAMSUNG("(?:CL(?:10|21|31)B|CM[0-9]|SPH|RC_)", "Samsung Electro-Mechanics", SamsungHandler::new),
    AVX("(?:TAJ|TPS|TCJ|F[0-9]|CR[0-9]|AR|LD|SD)", "AVX Corporation", AVXHandler::new),
    LELON("(?:RGA|RGC|RWE|REA|RZC|VYS)[0-9]", "Lelon Electronics", LelonHandler::new),
    RUBYCON("(?:YXF|YXG|ZLH|ZLJ|50YXF|63YXF)[0-9]", "Rubycon", RubyconHandler::new),
    ELNA("(?:RJJ|RJG|RJK|RSH|RAA|RA2|RFS|CE-BP)[0-9]", "Elna", ElnaHandler::new),
    NIPPON_CHEMICON("(?:KMG|KMH|KMQ|KY|KZH|KZN|KXJ|KZE)[0-9]", "Nippon Chemi-Con", NipponChemiConHandler::new),
    WIMA("(?:MKS|MKP|FKS|FKP|FKC)[0-9]", "WIMA", WIMAHandler::new),
    FAIRCHILD("(?:FQ[PNS]|FDS|FDC|FDD)[0-9]", "Fairchild/ON Semi", FairchildHandler::new),
    // Connector Manufacturers
    //WURTH("(?:61|62|63|64|65)[0-9]{8}", "Wurth Electronics", WurthHandler::new),
    WURTH("(?:61|62|63|64|65)[0-9]{8}", "Wurth Electronics", WurthHandler::new),

    MOLEX("(?:43|53|55|67|87|88)[0-9]{4}", "Molex", MolexHandler::new),
    //TE("(?:1-|2-)[0-9]{6}|(?:282837|5-826)", "TE Connectivity", TEHandler::new),
    //TE("(?:1-|2-)[0-9]{6}|(?:282[0-9]{3})-[0-9]|(?:5-826)", "TE Connectivity", TEHandler::new),
    //TE("(?:1-|2-)[0-9]{6}|(?:282836|282837|282838|282842)-[0-9]+", "TE Connectivity", TEHandler::new),
    TE("(?:(?:1-|2-)[0-9]{6}|(?:282[0-9]{3}))-[0-9]+", "TE Connectivity", TEHandler::new) ,

    JST("(?:B|S|P|X)[H,M][0-9]|(?:PA|PH|SM|EH)", "JST", JSTHandler::new),
    HIROSE("(?:DF|FH|BM|ZX|GT)[0-9]", "Hirose Electric", HiroseHandler::new),
    AMPHENOL("(?:10|20|54)[0-9]{4}|(?:G5|UE)", "Amphenol", AmphenolHandler::new),
    CUI("(?:SJ[0-9]|PJ-|CMI|CMS|CPE|CPT|ACZ|AMT)", "CUI Devices", CUIHandler::new),
    JAE("(?:FI-|DX[0-9]|MX[0-9]|IL-)", "JAE Electronics", JAEHandler::new),
    JINLING("(?:1[2367]|2[267]|3[25])[0-9]{4,}", "Shenzhen Jinling Electronics", JinlingHandler::new),

    // LED Manufacturers
    CREE("(?:XL|XH|XP|XQ|XB|CL)[A-Z][0-9]", "Cree", CreeHandler::new),
    OSRAM("(?:LS|LA|LW|LY|LO|LB)[A-Z][0-9]", "OSRAM", OSRAMHandler::new),
    LUMILEDS("(?:L|P)[X,T][H,L][0-9]|LUXEON", "Lumileds", LumiledsHandler::new),
    KINGBRIGHT("(?:WP|KP|AP|AA)[0-9]", "Kingbright", KingbrightHandler::new),

    // Sensor Manufacturers
    ALLEGRO("(?:ACS[0-9]|A[0-9]{4}|AH[0-9]{3}|AAS[0-9])", "Allegro MicroSystems", AllegroHandler::new),
    BOSCH("(?:BM[A-Z][0-9]|BSH|BMP|BME|BNO)", "Bosch Sensortec", BoschHandler::new),
    MELEXIS("(?:MLX|TMF)[0-9]", "Melexis", MelexisHandler::new),
    INVSENSE("(?:MPU|ICM|IAM|IIM)[0-9]", "InvenSense", InvSenseHandler::new),

    // Relay/Switch/Sensor Manufacturers
    OMRON("(?:G[0-9]|G3[A-Z]|B3[A-Z]|D2F|EE-S|D6F|E2E)", "Omron", OmronHandler::new),

    // Optocoupler Manufacturers
    ISOCOM("(?:ISP|ISQ|ISD)[0-9]{3}|(?:4N|6N)[0-9]{2}|(?:MOC|TLP)[0-9]{3}", "Isocom Components", IsocomHandler::new),
    COSMO("(?:KP[CSH]?[0-9]{3,4}|KPTR[0-9]{4})", "Cosmo Electronics", CosmoHandler::new),

    // Memory Manufacturers
    MICRON("(?:MT)[0-9]|(?:N25Q|M25P)", "Micron Technology", MicronHandler::new),
    WINBOND("(?:W)[0-9]{2}[A-Z]|(?:W25Q|W25X)", "Winbond", WinbondHandler::new),
    ISSI("(?:IS)[0-9]|(?:IS25LP|IS25WP)", "ISSI", ISSIHandler::new),

    // RF/Wireless Manufacturers
    NORDIC("(?:nRF)[0-9]|(?:NRF|nRF52)", "Nordic Semiconductor", NordicHandler::new),
    QUALCOMM("(?:QCA|IPQ|MDM|WCN)[0-9]", "Qualcomm", QualcommHandler::new),
    SKYWORKS("(?:SKY|SE|SI)[0-9]", "Skyworks Solutions", SkyworksHandler::new),
    QORVO("(?:RF|RFX|TQP)[0-9]", "Qorvo", QorvoHandler::new),
    AIROHA("(?:AB[0-9]{4}|AU[0-9]{4}|AG[0-9]{4})", "Airoha Technology", AirohaHandler::new),
    BEKEN("(?:BK[0-9]{4}|BL[0-9]{4})", "Beken", BekenHandler::new),
    TELINK("(?:TLSR[0-9]{4}|TC[0-9]{2})", "Telink Semiconductor", TelinkHandler::new),
    SEMTECH("(?:SX[0-9]{4}|LR[0-9]{4}|SY[0-9]{4})", "Semtech", SemtechHandler::new),

    // Crystal/Oscillator Manufacturers
    EPSON("(?:SG|FA|TSX|TC|MA)[0-9]", "Epson", EpsonHandler::new),
    NDK("(?:NX|NT|NZ|NH)[0-9]", "NDK", NDKHandler::new),
    ABRACON("(?:ABM|ABLS|ABLM|AB26|ASDM)", "Abracon", AbraconHandler::new),
    IQD("(?:LFXTAL|CFPS|LFTCXO|LFOCA)", "IQD Frequency Products", IQDHandler::new),
    TXC("(?:7[MVX]|8Y|9C|AX)-", "TXC Corporation", TXCHandler::new),
    KYOCERA("(?:CX|KC|CT|CXO|PBRC)[0-9]|(?:5600|5800)-", "Kyocera", KyoceraHandler::new),
    KDS("(?:DSX|DST|DSO|DSB|1N-)[0-9]", "KDS Daishinku", KDSHandler::new),

    // Motor Driver Manufacturers
    TRINAMIC("(?:TMC[0-9]{4})", "Trinamic Motion Control", TrinamicHandler::new),

    // Circuit Protection Manufacturers
    LITTELFUSE("(?:SMAJ|SMBJ|SMCJ|SMDJ|P[46]KE|P4SMA|P6SMB|1\\.?5KE|045[1-4]|0448|V[0-9]{2}[EPDM])", "Littelfuse", LittelfuseHandler::new),
    PROTEK_DEVICES("(?:TVS[0-9]{5}|GBLC|PSM[0-9]{3}|ULC[0-9]{4}|SMD[0-9]{4})", "ProTek Devices", ProTekDevicesHandler::new),

    // Power Supply Manufacturers
    MEAN_WELL("(?:RS|LRS|SE|NES|SP|PS|PT|SD|DDR|LPV|HLG|ELG|PLN|PWM|LCM|HDR|EDR|MDR|NDR|DR)-[0-9]", "Mean Well", MeanWellHandler::new),

    // Interface IC Manufacturers
    FTDI("(?:FT)[0-9]{3}", "FTDI", FTDIHandler::new),
    GENESYS_LOGIC("(?:GL[0-9]{3}|GL3[0-9]{3})", "Genesys Logic", GenesysLogicHandler::new),
    ASMEDIA("(?:ASM[0-9]{4}|ASM1[0-9]{3})", "ASMedia Technology", ASMediaHandler::new),
    PROLIFIC("(?:PL[0-9]{4}|PL23[0-9]{2})", "Prolific Technology", ProlificHandler::new),
    JMICRON("(?:JM[0-9]{3}|JMS[0-9]{3})", "JMicron Technology", JMicronHandler::new),
    VIALABS("(?:VL[0-9]{3}|VL8[0-9]{2})", "VIA Labs", ViaLabsHandler::new),

    // Audio IC Manufacturers
    CIRRUS_LOGIC("(?:CS4|CS5|CS8|WM8)[0-9]", "Cirrus Logic", CirrusLogicHandler::new),
    REALTEK("(?:ALC|RTL|RTD)[0-9]", "Realtek", RealtekHandler::new),
    ESS("(?:ES[0-9]{4}|SABRE)", "ESS Technology", ESSHandler::new),
    CMEDIA("(?:CM[0-9]{3}|CMI[0-9]{4})", "C-Media", CMediaHandler::new),

    // Sensor Manufacturers (additional)
    SENSIRION("(?:SHT|SGP|SCD|SFA|SPS|STS|SLF|SDP)[0-9]", "Sensirion", SensirionHandler::new),
    HONEYWELL("(?:HIH|HSC|SSC|ABP|MPR|SS4|SS5|HMC|HOA|HLC)[0-9]", "Honeywell Sensing", HoneywellHandler::new),
    AMS("(?:AS[3-7]|TSL|TMD|TCS|APDS|ENS)[0-9]", "ams-OSRAM", AMSHandler::new),

    // Inductor/Magnetics Manufacturers
    COILCRAFT("(?:XAL|XEL|XFL|SER|LPS|MSS|DO[0-9]|MSD|SLC|SLR|0[46]0[23]HP)", "Coilcraft", CoilcraftHandler::new),
    SUMIDA("(?:CDRH|CDR|CDEP|CDEF|CR[0-9]|RCH|CEP|CDC|CLF)[0-9]", "Sumida", SumidaHandler::new),
    PULSE_ELECTRONICS("(?:H[0-9]{4}|T[0-9]{4}|P[0-9]{4}|PE-|PA-|JD|JK|JXD)", "Pulse Electronics", PulseElectronicsHandler::new),
    SUNLORD("(?:SDCL|SWPA|SDFL|MWSA|SMDRR)", "Sunlord Electronics", SunlordHandler::new),
    CHILISIN("(?:SQC[0-9]{4}|SCDS[0-9]|SCD[0-9]{4})", "Chilisin Electronics", ChilisinHandler::new),
    CYNTEC("(?:PMC|PBSS|PCMN)[0-9]", "Cyntec", CyntecHandler::new),

    // Power Management Manufacturers
    VICOR("(?:DCM|BCM|PRM|VTM|NBM|PI3[35])[0-9]", "Vicor", VicorHandler::new),
    POWER_INTEGRATIONS("(?:TOP|TNY|LNK|INN|PFS|LCS|CAP|SEN)[0-9]", "Power Integrations", PowerIntegrationsHandler::new),
    MPS("(?:MP[0-9]|MPQ|MPM)[0-9]", "Monolithic Power Systems", MPSHandler::new),
    RICHTEK("(?:RT[0-9]{4}|RTQ[0-9])", "Richtek", RichtekHandler::new),
    SILERGY("(?:SY[0-9]{4}|SY8[0-9])", "Silergy", SilergyHandler::new),
    TOREX("(?:XC[0-9]{4}|XCL[0-9])", "Torex Semiconductor", TorexHandler::new),
    ABLIC("(?:S-[0-9]{5}|S8[0-9]{3})", "ABLIC", ABLICHandler::new),
    SGMICRO("(?:SGM[0-9]{4}|SG[0-9]{3})", "SG Micro", SGMicroHandler::new),
    THREE_PEAK("(?:TP[0-9]{4}|TPF[0-9])", "3PEAK", ThreePeakHandler::new),

    // Memory Manufacturers (additional)
    MACRONIX("(?:MX25|MX29|MX30|MX66)[A-Z]", "Macronix", MacronixHandler::new),
    GIGADEVICE("(?:GD25|GD32|GD5F)[A-Z0-9]", "GigaDevice", GigaDeviceHandler::new),
    ALLIANCE_MEMORY("(?:AS6C|AS7C|AS4C|AS29)[0-9]", "Alliance Memory", AllianceMemoryHandler::new),
    PUYA("(?:P25[A-Z]|PY25[A-Z])", "Puya Semiconductor", PuyaHandler::new),
    XMC("(?:XM25Q|XM25E)", "XMC", XMCHandler::new),
    ESMT("(?:M[0-9]{2}S|F[0-9]{2}L)", "Elite Semiconductor Memory Technology", ESMTHandler::new),

    // Timing/Oscillator Manufacturers (additional)
    SITIME("(?:SiT)[0-9]", "SiTime", SiTimeHandler::new),

    // LED Manufacturers (additional)
    SEOUL_SEMI("(?:Z5|STW|STN|SFH|CUD|MJT|WICOP|SunLike|Acrich)", "Seoul Semiconductor", SeoulSemiHandler::new),
    EVERLIGHT("(?:17-|19-|26-|333|EL8|EL3|IR[0-9]|PT[0-9]|ALS)", "Everlight Electronics", EverlightHandler::new),

    // LED Driver / Display Driver Manufacturers
    MACROBLOCK("(?:MBI[0-9]{4}|MBI5[0-9])", "Macroblock", MacroblockHandler::new),
    CHIPONE("(?:ICN[0-9]{4}|ICND[0-9])", "Chipone Technology", ChiponeHandler::new),
    SITRONIX("(?:ST[0-9]{4}|SSD[0-9]{4})", "Sitronix Technology", SitronixHandler::new),
    RAYDIUM("(?:RM[0-9]{5}|RM6[0-9]{4})", "Raydium Semiconductor", RaydiumHandler::new),
    NOVATEK("(?:NT[0-9]{5}|NVT[0-9])", "Novatek Microelectronics", NovatekHandler::new),

    // Connector Manufacturers (additional)
    PHOENIX_CONTACT("(?:MC |MCV |MSTB|PT |UK |UT |PTSM|SPT |FK-)", "Phoenix Contact", PhoenixContactHandler::new),
    HARTING("(?:09 |21 0|02 0|14 |15 )", "Harting", HartingHandler::new),
    SAMTEC("(?:ESQ|SSQ|TSM|TSW|TLE|SMH|CLT|HSEC|QSH|QTH)", "Samtec", SamtecHandler::new),
    MILL_MAX("(?:[0-9]{3}-[0-9]{2}-[0-9]{3}|[0-9]{4}-[0-9]-[0-9]{2})", "Mill-Max", MillMaxHandler::new),
    SULLINS("(?:PRPC|PPTC|NRPN|SWH|GRPB|NPPC|LPPB)", "Sullins Connector Solutions", SullinsHandler::new),

    // Unknown (must be last)
    UNKNOWN("", "Unknown Manufacturer", UnknownHandler::new);

    private final String code;
    private final String name;
    private final Supplier<ManufacturerHandler> handlerFactory;
    private volatile ManufacturerHandler handler;
    private volatile PatternRegistry patterns;
    private volatile Set<ManufacturerComponentType> manufacturerTypes;

    /**
     * Constructor accepting a Supplier for lazy handler initialization.
     * This breaks the circular dependency between ComponentManufacturer and ComponentType.
     *
     * @param code the manufacturer code pattern
     * @param name the manufacturer display name
     * @param handlerFactory supplier that creates the handler on first access
     */
    ComponentManufacturer(String code, String name, Supplier<ManufacturerHandler> handlerFactory) {
        this.code = code;
        this.name = name;
        this.handlerFactory = handlerFactory;
    }

    /**
     * Get the handler for this manufacturer, lazily initializing it on first access.
     * Uses double-checked locking for thread safety.
     *
     * @return the manufacturer handler
     */
    public ManufacturerHandler getHandler() {
        if (handler == null) {
            synchronized (this) {
                if (handler == null) {
                    handler = handlerFactory.get();
                    patterns = new PatternRegistry();
                    manufacturerTypes = handler.getManufacturerTypes();
                    handler.initializePatterns(patterns);
                }
            }
        }
        return handler;
    }

    public PatternRegistry getPatterns() {
        // Ensure handler (and thus patterns) is initialized
        if (patterns == null) {
            getHandler(); // Triggers lazy initialization
        }
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
        if (getPatterns().hasPattern(type)) return true;

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
        return getHandler().matches(mpn, type, getPatterns());
    }

    public String extractPackageCode(String mpn) {
        return getHandler().extractPackageCode(mpn);
    }

    public String extractSeries(String mpn) {
        return getHandler().extractSeries(mpn);
    }

    public boolean isOfficialReplacement(String mpn1, String mpn2) {
        return getHandler().isOfficialReplacement(mpn1, mpn2);
    }

    public Pattern getPattern(ComponentType type) {
        return getPatterns().getPattern(type);
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