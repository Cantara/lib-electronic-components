package no.cantara.electronic.component;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;

public class TechnicalAsset {
    @JsonProperty("ID")
    private String id;

    @JsonProperty("Type")
    private AssetType type;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Location")
    private AssetLocation location;

    @JsonProperty("Format")
    private String format;  // e.g., "STEP", "Gerber", "PDF"

    @JsonProperty("Metadata")
    private Map<String, Object> metadata = new HashMap<>();

    public enum AssetType {
        PCB_DESIGN,
        CAD_MODEL,
        TECHNICAL_DRAWING,
        SPECIFICATION,
        TEST_PROCEDURE,
        QUALITY_DOCUMENT,
        OTHER
    }

    public static class AssetLocation {
        @JsonProperty("Type")
        private LocationType type;

        @JsonProperty("Value")
        private String value;

        @JsonProperty("Checksum")
        private String checksum;

        public enum LocationType {
            URL,     // Web URL
            FILE,    // Local or network file path
            DATA     // Base64 encoded embedded data
        }

        // Factory methods
        public static AssetLocation url(String url) {
            AssetLocation loc = new AssetLocation();
            loc.type = LocationType.URL;
            loc.value = url;
            return loc;
        }

        public static AssetLocation file(String path) {
            AssetLocation loc = new AssetLocation();
            loc.type = LocationType.FILE;
            loc.value = path;
            return loc;
        }

        public static AssetLocation embeddedData(String base64Data, String checksum) {
            AssetLocation loc = new AssetLocation();
            loc.type = LocationType.DATA;
            loc.value = base64Data;
            loc.checksum = checksum;
            return loc;
        }

        // Getters and setters
        public LocationType getType() { return type; }
        public void setType(LocationType type) { this.type = type; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
        public String getChecksum() { return checksum; }
        public void setChecksum(String checksum) { this.checksum = checksum; }
    }

    // Factory methods for common asset types
    public static TechnicalAsset createPCBDesign(String id, String version, String gerberPath) {
        TechnicalAsset asset = new TechnicalAsset();
        asset.id = id;
        asset.type = AssetType.PCB_DESIGN;
        asset.version = version;
        asset.format = "Gerber";
        asset.location = AssetLocation.file(gerberPath);
        return asset;
    }

    public static TechnicalAsset createCADModel(String id, String version, String cadPath) {
        TechnicalAsset asset = new TechnicalAsset();
        asset.id = id;
        asset.type = AssetType.CAD_MODEL;
        asset.version = version;
        asset.format = "STEP";
        asset.location = AssetLocation.file(cadPath);
        return asset;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public AssetType getType() { return type; }
    public void setType(AssetType type) { this.type = type; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public AssetLocation getLocation() { return location; }
    public void setLocation(AssetLocation location) { this.location = location; }
    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public Map<String, Object> getMetadata() { return metadata; }
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
}