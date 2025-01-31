package no.cantara.electronic.component;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "designator",
        "specs",
        "quantity",
        "package",
        "description",
        "mpn",
        "value",
        "manufacturer"
})
public class BOMEntry extends ElectronicPart implements Serializable
{



    @JsonProperty("quantity")
    private String qty;

    @JsonProperty("specs")
    private Map<String, String> specs = new HashMap<String, String>();



    @JsonProperty("designators")
    private List<String> designators = new ArrayList<String>();


    private final static long serialVersionUID = 5438375525154610448L;

    public BOMEntry() {
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    @Override
    public Map<String, String> getSpecs() {
        return specs;
    }

    @Override
    public void setSpecs(Map<String, String> specs) {
        this.specs = specs;
    }

    public List<String> getDesignators() {
        return designators;
    }

    public void setDesignators(List<String> designators) {
        this.designators = designators;
    }
}

