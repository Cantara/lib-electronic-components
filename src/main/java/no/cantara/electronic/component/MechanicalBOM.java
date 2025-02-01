package no.cantara.electronic.component;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// Inside BOM.java
public  class MechanicalBOM extends BOM {
    @JsonProperty("PlannedProductionDate")
    private LocalDate plannedProductionDate;

    @JsonProperty("CADModel")
    private TechnicalAsset cadModel;

    @JsonProperty("MaterialDetails")
    private Map<String, Object> materialDetails = new HashMap<>();

    @JsonProperty("ManufacturingDetails")
    private Map<String, Object> manufacturingDetails = new HashMap<>();

    @JsonProperty("FinishingDetails")
    private Map<String, Object> finishingDetails = new HashMap<>();

    public MechanicalBOM() {
        super();
        this.setBomType(BOMType.MECHANICAL_PART);
        this.materialDetails = new HashMap<>();
        this.manufacturingDetails = new HashMap<>();
        this.finishingDetails = new HashMap<>();
    }

    public LocalDate getPlannedProductionDate() {
        return plannedProductionDate;
    }

    public void setPlannedProductionDate(LocalDate plannedProductionDate) {
        this.plannedProductionDate = plannedProductionDate;
    }

    public TechnicalAsset getCadModel() {
        return cadModel;
    }

    public void setCadModel(TechnicalAsset cadModel) {
        this.cadModel = cadModel;
    }

    public Map<String, Object> getMaterialDetails() {
        return materialDetails;
    }

    public void setMaterialDetails(Map<String, Object> materialDetails) {
        this.materialDetails = materialDetails;
    }

    public Map<String, Object> getManufacturingDetails() {
        return manufacturingDetails;
    }

    public void setManufacturingDetails(Map<String, Object> manufacturingDetails) {
        this.manufacturingDetails = manufacturingDetails;
    }

    public Map<String, Object> getFinishingDetails() {
        return finishingDetails;
    }

    public void setFinishingDetails(Map<String, Object> finishingDetails) {
        this.finishingDetails = finishingDetails;
    }

    public void addMaterialSpec(String key, Object value) {
        materialDetails.put(key, value);
    }

    public void addManufacturingSpec(String key, Object value) {
        manufacturingDetails.put(key, value);
    }

    public void addFinishingSpec(String key, Object value) {
        finishingDetails.put(key, value);
    }
}