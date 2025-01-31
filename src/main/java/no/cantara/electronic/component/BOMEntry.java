package no.cantara.electronic.component;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BOMEntry extends ElectronicPart<BOMEntry> implements Serializable {
    @JsonProperty("quantity")
    private String qty;

    @JsonProperty("designators")
    private List<String> designators = new ArrayList<>();

    private final static long serialVersionUID = 5438375525154610448L;

    public BOMEntry() {
    }

    public String getQty() {
        return qty;
    }

    public BOMEntry setQty(String qty) {
        this.qty = qty;
        return this;
    }

    public List<String> getDesignators() {
        return designators;
    }

    public BOMEntry setDesignators(List<String> designators) {
        this.designators = designators;
        return this;
    }

    @Override
    public BOMEntry setMpn(String mpn) {
        super.setMpn(mpn);
        return this;
    }

    @Override
    public BOMEntry setManufacturer(String manufacturer) {
        super.setManufacturer(manufacturer);
        return this;
    }

    @Override
    public BOMEntry setValue(String value) {
        super.setValue(value);
        return this;
    }

    @Override
    public BOMEntry setPkg(String pkg) {
        super.setPkg(pkg);
        return this;
    }

    @Override
    public BOMEntry setDescription(String description) {
        super.setDescription(description);
        return this;
    }

    @Override
    public BOMEntry addSpec(String key, String value) {
        super.addSpec(key, value);
        return this;
    }

    @Override
    public BOMEntry addSpecs(Map<String, String> specs) {
        super.addSpecs(specs);
        return this;
    }

    @Override
    protected BOMEntry self() {
        return this;
    }

    public BOMEntry addSpecifications(String... keyValues) {
        if (keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("Must provide key-value pairs");
        }

        for (int i = 0; i < keyValues.length; i += 2) {
            addSpec(keyValues[i], keyValues[i + 1]);
        }
        return this;
    }
}