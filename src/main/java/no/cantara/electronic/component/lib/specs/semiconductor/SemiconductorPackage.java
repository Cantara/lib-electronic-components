package no.cantara.electronic.component.lib.specs.semiconductor;

/**
 * Package types for semiconductor devices.
 */
public enum SemiconductorPackage {
    // Through-hole packages
    TO220("TO-220", "Through-hole"),
    TO247("TO-247", "Through-hole"),
    TO92("TO-92", "Through-hole"),

    // Surface mount packages
    SOT23("SOT-23", "SMD"),
    SOT223("SOT-223", "SMD"),
    SOIC8("SOIC-8", "SMD"),
    SOIC16("SOIC-16", "SMD"),
    TSSOP("TSSOP", "SMD"),
    TQFP("TQFP", "SMD"),
    QFN("QFN", "SMD"),
    BGA("BGA", "SMD"),
    DFN("DFN", "SMD");

    private final String name;
    private final String mountType;

    SemiconductorPackage(String name, String mountType) {
        this.name = name;
        this.mountType = mountType;
    }

    public String getName() {
        return name;
    }

    public String getMountType() {
        return mountType;
    }
}
