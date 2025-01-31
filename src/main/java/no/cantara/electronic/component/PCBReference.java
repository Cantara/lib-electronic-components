package no.cantara.electronic.component;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "PCB_ID",
        "Version",
        "Name",
        "GerberFileReference"
})
public class PCBReference {

    @JsonProperty("PCB_ID")
    private String id;
    @JsonProperty("Version")
    private String version;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("GerberFileReference")
    private String gerberFileReference;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();


    @JsonProperty("PCB_ID")
    public String getId() {
        return id;
    }

    @JsonProperty("PCB_ID")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("Version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("Version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("GerberFileReference")
    public String getGerberFileReference() {
        return gerberFileReference;
    }

    @JsonProperty("GerberFileReference")
    public void setGerberFileReference(String gerberFileReference) {
        this.gerberFileReference = gerberFileReference;
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
