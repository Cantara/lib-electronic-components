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
public class ElectronicPart<T extends ElectronicPart<T>> implements Serializable {

    @JsonProperty("mpn")
    private String mpn;

    @JsonProperty("manufacturer")
    private String manufacturer;



    @JsonProperty("specs")
    private Map<String, Object> specs = new HashMap<String, Object>();

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

    public <T extends ElectronicPart> T setMpn(String mpn) {
        this.mpn = mpn;
        return (T)self();
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public <T extends ElectronicPart> T setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        return (T)self();
    }

    public Map<String, Object> getSpecs() {
        return specs;
    }

    public void setSpecs(Map<String, Object> specs) {
        this.specs = specs;
    }

    public String getValue() {
        return value;
    }

    public <T extends ElectronicPart> T setValue(String value) {
        this.value = value;
        return (T)self();
    }

    public String getPkg() {
        return pkg;
    }


    public <T extends ElectronicPart> T setPkg(String pkg) {
        this.pkg = pkg;
        return (T)self();
    }

    public String getDescription() {
        return description;
    }

    public <T extends ElectronicPart> T setDescription(String description) {
        this.description = description;
        return (T)self();
    }

    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }


    /**
     * Adds a specification to the component
     * @param key The specification key
     * @param value The specification value
     * @return this ElectronicPart for method chaining
     */
    public ElectronicPart addSpec(String key, Object value) {
        this.specs.put(key, value);
        return this;
    }

    /**
     * Adds multiple specifications to the component
     * @param specs Map of specifications to add
     * @return this ElectronicPart for method chaining
     */
    public ElectronicPart addSpecs(Map<String, Object> specs) {
        this.specs.putAll(specs);
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

}
