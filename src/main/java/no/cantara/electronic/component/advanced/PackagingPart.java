package no.cantara.electronic.component.advanced;

import no.cantara.electronic.component.BOMEntry;

/**
 * Represents a packaging component in a BOM, extending BOMEntry to maintain
 * consistency with other component types while adding packaging-specific functionality.
 */
public class PackagingPart extends BOMEntry {

    /**
     * Common categories for packaging parts
     */
    public enum PackagingCategory {
        PRODUCT_BOX,        // Primary product packaging
        SHIPPING_BOX,       // External shipping container
        PROTECTIVE_INSERT,  // Foam, plastic inserts, cushioning
        DESICCANT,         // Moisture protection
        LABELS,            // Product and shipping labels
        DOCUMENTATION,     // Manuals, certificates, warranty cards
        ACCESSORIES,       // Additional packaged items
        CUSTOM            // Special packaging requirements
    }

    private PackagingCategory category;

    public PackagingCategory getCategory() {
        return category;
    }

    public PackagingPart setCategory(PackagingCategory category) {
        this.category = category;
        return this;
    }

    /**
     * Factory method for creating a product box
     */
    public static PackagingPart createProductBox() {
        PackagingPart part = new PackagingPart();
        part.setCategory(PackagingCategory.PRODUCT_BOX);
        return part;
    }

    /**
     * Factory method for creating a shipping box
     */
    public static PackagingPart createShippingBox() {
        PackagingPart part = new PackagingPart();
        part.setCategory(PackagingCategory.SHIPPING_BOX);
        return part;
    }

    /**
     * Factory method for creating documentation package
     */
    public static PackagingPart createDocumentation() {
        PackagingPart part = new PackagingPart();
        part.setCategory(PackagingCategory.DOCUMENTATION);
        return part;
    }
}