package no.cantara.electronic.component;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BOMEntry extends ElectronicPart implements Serializable {
    @JsonProperty("quantity")
    private String qty;

    @JsonProperty("designators")
    private List<String> designators = new ArrayList<String>();

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
}