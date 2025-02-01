package no.cantara.electronic.component.advanced;


import no.cantara.electronic.component.BOM;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Packaging Bill of Materials, describing product packaging,
 * shipping materials, and additional packaging components.
 */
public class PackagingBOM extends BOM {
    private String id;
    private String revision;
    private String description;
    private PackagingType type;
    private Map<String, String> specifications = new HashMap<>();

    public enum PackagingType {
        PRODUCT_BOX,        // Primary product packaging
        PROTECTIVE_INSERT,  // Foam, plastic inserts
        SHIPPING_BOX,      // Outer shipping container
        PALLET_PACKAGING,  // Pallet-level packaging
        DESICCANT,        // Moisture protection
        DOCUMENTATION,     // Manuals, certificates
        LABELS            // Warning labels, shipping labels
    }

    // Builder-style setters for fluent API
    @Override
    public PackagingBOM setId(String id) {
        this.id = id;
        return this;
    }

    public PackagingBOM setRevision(String revision) {
        this.revision = revision;
        return this;
    }

    public PackagingBOM setDescription(String description) {
        this.description = description;
        return this;
    }

    public PackagingBOM setType(PackagingType type) {
        this.type = type;
        return this;
    }

    public PackagingBOM addSpecification(String key, String value) {
        specifications.put(key, value);
        return this;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getRevision() {
        return revision;
    }

    public String getDescription() {
        return description;
    }

    public PackagingType getType() {
        return type;
    }

    public Map<String, String> getSpecifications() {
        return Map.copyOf(specifications);  // Return immutable copy
    }

    // Factory methods for common packaging types
    public static PackagingBOM createProductBox() {
        return new PackagingBOM()
                .setId("PKG-BOX-001")
                .setRevision("1.0")
                .setDescription("Product Primary Box")
                .setType(PackagingType.PRODUCT_BOX)
                .addSpecification("material", "Corrugated Cardboard")
                .addSpecification("thickness", "3mm")
                .addSpecification("dimensions", "400x300x200mm")
                .addSpecification("color", "White")
                .addSpecification("printType", "4-color offset")
                .addSpecification("certifications", "FSC");
    }

    public static PackagingBOM createProtectiveInsert() {
        return new PackagingBOM()
                .setId("PKG-INS-001")
                .setRevision("1.0")
                .setDescription("Custom Foam Insert")
                .setType(PackagingType.PROTECTIVE_INSERT)
                .addSpecification("material", "PE Foam")
                .addSpecification("density", "45kg/m³")
                .addSpecification("color", "Black")
                .addSpecification("cutouts", "Product specific")
                .addSpecification("thickness", "50mm");
    }

    public static PackagingBOM createShippingBox() {
        return new PackagingBOM()
                .setId("PKG-SHP-001")
                .setRevision("1.0")
                .setDescription("Shipping Carton")
                .setType(PackagingType.SHIPPING_BOX)
                .addSpecification("material", "Double-wall Corrugated")
                .addSpecification("thickness", "7mm")
                .addSpecification("dimensions", "600x400x300mm")
                .addSpecification("burstStrength", "14kN/m")
                .addSpecification("stackingStrength", "250kg")
                .addSpecification("certifications", "ISTA 3A");
    }

    public static PackagingBOM createDesiccant() {
        return new PackagingBOM()
                .setId("PKG-DES-001")
                .setRevision("1.0")
                .setDescription("Moisture Protection Pack")
                .setType(PackagingType.DESICCANT)
                .addSpecification("type", "Silica Gel")
                .addSpecification("units", "100g")
                .addSpecification("indicator", "Yes")
                .addSpecification("packaging", "Tyvek")
                .addSpecification("compliance", "FDA");
    }

    public static PackagingBOM createDocumentationKit() {
        return new PackagingBOM()
                .setId("PKG-DOC-001")
                .setRevision("1.0")
                .setDescription("Documentation Package")
                .setType(PackagingType.DOCUMENTATION)
                .addSpecification("contents", "Manual, Warranty, Certificates")
                .addSpecification("languages", "EN, DE, FR")
                .addSpecification("paperType", "100gsm")
                .addSpecification("printing", "Color")
                .addSpecification("binding", "Stapled");
    }

    public static PackagingBOM createLabelSet() {
        return new PackagingBOM()
                .setId("PKG-LBL-001")
                .setRevision("1.0")
                .setDescription("Product Label Set")
                .setType(PackagingType.LABELS)
                .addSpecification("type", "Serial, Warning, Shipping")
                .addSpecification("material", "Polyester")
                .addSpecification("adhesive", "Permanent")
                .addSpecification("printType", "Thermal Transfer")
                .addSpecification("waterproof", "Yes");
    }
}