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
    public String manufacturer;



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

    public ElectronicPart setMpn(String mpn) {
        this.mpn = mpn;
        return this;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public ElectronicPart  setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }


    public void setSpecs(Map<String, String> specs) {
        this.specs = specs;
    }

    public String getValue() {
        return value;
    }

    public ElectronicPart setValue(String value) {
        this.value = value;
        return this;
    }

    public String getPkg() {
        return pkg;
    }


    public ElectronicPart setPkg(String pkg) {
        this.pkg = pkg;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ElectronicPart setDescription(String description) {
        this.description = description;
        return this;
    }




    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    public Map<String, String> getSpecs() {
        return specs;
    }

    public ElectronicPart addSpec(String key, String value) {
        this.specs.put(key, value);
        return this;
    }

    // Helper method for nested values
    public ElectronicPart addNestedSpec(String prefix, Map<String, String> values) {
        values.forEach((key, value) ->
                this.specs.put(prefix + "." + key, value));
        return this;
    }

    // Helper method for multiple specifications
    public ElectronicPart addSpecs(Map<String, String> specs) {
        this.specs.putAll(specs);
        return this;
    }
}
