package no.cantara.electronic.component.advanced;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import no.cantara.electronic.component.TechnicalAsset;
import java.util.HashMap;
import java.util.Map;

@JsonTypeName("gerber")
public class GerberAsset extends TechnicalAsset {
    public enum GerberLayer {
        TOP_LAYER,
        BOTTOM_LAYER,
        TOP_SOLDER_MASK,
        BOTTOM_SOLDER_MASK,
        TOP_OVERLAY,
        BOTTOM_OVERLAY,
        KEEP_OUT_LAYER,
        TOP_PASTE,
        BOTTOM_PASTE
    }

    private Map<String, String> manufacturingDocs;
    private Map<GerberLayer, String> standardLayers;

    // Default constructor required for Jackson
    public GerberAsset() {
        super();
        this.manufacturingDocs = new HashMap<>();
        this.standardLayers = new HashMap<>();
    }

    // Primary constructor with JsonCreator annotation
    @JsonCreator
    public GerberAsset(
            @JsonProperty("Name") String name,
            @JsonProperty("Version") String version) {
        super();
        setName(name);
        setVersion(version);
        setType(AssetType.PCB_DESIGN);
        setFormat("Gerber");
        this.manufacturingDocs = new HashMap<>();
        this.standardLayers = new HashMap<>();
    }

    // Getters and setters for Jackson
    @JsonProperty("manufacturingDocs")
    public Map<String, String> getManufacturingDocs() {
        return manufacturingDocs;
    }

    @JsonProperty("manufacturingDocs")
    public void setManufacturingDocs(Map<String, String> manufacturingDocs) {
        this.manufacturingDocs = manufacturingDocs;
    }

    @JsonProperty("standardLayers")
    public Map<GerberLayer, String> getStandardLayers() {
        return standardLayers;
    }

    @JsonProperty("standardLayers")
    public void setStandardLayers(Map<GerberLayer, String> standardLayers) {
        this.standardLayers = standardLayers;
    }

    // Override parent class getters/setters with correct annotations
    @Override
    @JsonProperty("Name")
    public String getName() {
        return super.getName();
    }

    @Override
    @JsonProperty("Name")
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    @JsonProperty("Version")
    public String getVersion() {
        return super.getVersion();
    }

    @Override
    @JsonProperty("Version")
    public void setVersion(String version) {
        super.setVersion(version);
    }

    @Override
    @JsonProperty("Type")
    public AssetType getType() {
        return super.getType();
    }

    @Override
    @JsonProperty("Type")
    public void setType(AssetType type) {
        super.setType(type);
    }

    @Override
    @JsonProperty("Format")
    public String getFormat() {
        return super.getFormat();
    }

    @Override
    @JsonProperty("Format")
    public void setFormat(String format) {
        super.setFormat(format);
    }

    //@Override
    @JsonProperty("ID")
    public String getID() {
        return super.getId();
    }

    //@Override
    @JsonProperty("ID")
    public void setID(String ID) {
        super.setId(ID);
    }

    @Override
    @JsonProperty("Description")
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    @JsonProperty("Description")
    public void setDescription(String description) {
        super.setDescription(description);
    }

    @Override
    @JsonProperty("Location")
    public AssetLocation getLocation() {
        return super.getLocation();
    }

    @Override
    @JsonProperty("Location")
    public void setLocation(AssetLocation location) {
        super.setLocation(location);
    }

    @Override
    @JsonProperty("Metadata")
    public Map<String, Object> getMetadata() {
        return super.getMetadata();
    }

    @Override
    @JsonProperty("Metadata")
    public void setMetadata(Map<String, Object> metadata) {
        super.setMetadata(metadata);
    }

    public void addManufacturingDoc(String type, String filename) {
        manufacturingDocs.put(type, filename);
    }

    public void addStandardLayer(GerberLayer layer, String filename) {
        standardLayers.put(layer, filename);
    }

    public void generateStandardLayers(String baseName) {
        for (GerberLayer layer : GerberLayer.values()) {
            String filename = baseName + getExtensionForLayer(layer);
            standardLayers.put(layer, filename);
        }
    }

    private String getExtensionForLayer(GerberLayer layer) {
        switch (layer) {
            case TOP_LAYER: return ".GTL";
            case BOTTOM_LAYER: return ".GBL";
            case TOP_SOLDER_MASK: return ".GTS";
            case BOTTOM_SOLDER_MASK: return ".GBS";
            case TOP_OVERLAY: return ".GTO";
            case BOTTOM_OVERLAY: return ".GBO";
            case KEEP_OUT_LAYER: return ".GKO";
            case TOP_PASTE: return ".GPT";
            case BOTTOM_PASTE: return ".GPB";
            default: return "";
        }
    }
}