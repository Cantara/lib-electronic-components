package no.cantara.electronic.component.lib;

/**
 * Class for detailed component analysis
 */
public class ComponentAnalysis {
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