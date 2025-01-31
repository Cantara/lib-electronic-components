package no.cantara.electronic.component;

import java.io.Serializable;
import java.util.*;


import com.fasterxml.jackson.annotation.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "specs",
        "package",
        "description",
        "mpn",
        "value",
        "manufacturer"
})
public class ElectronicPart implements Serializable {

    @JsonProperty("mpn")
    private String mpn;

    @JsonProperty("manufacturer")
    private String manufacturer;



    @JsonProperty("specs")
    private Map<String, String> specs = new HashMap<String, String>();

    @JsonProperty("value")
    private String value;

    @JsonProperty("package")
    private String pkg;

    @JsonProperty("description")
    private String description;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();



    private final static long serialVersionUID = 5438375525154610448L;

    public ElectronicPart() {
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Map<String, String> getSpecs() {
        return specs;
    }

    public void setSpecs(Map<String, String> specs) {
        this.specs = specs;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
