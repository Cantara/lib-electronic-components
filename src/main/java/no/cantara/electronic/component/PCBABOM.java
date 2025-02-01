package no.cantara.electronic.component;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// PCBA specific BOM
public  class PCBABOM extends BOM {

    @JsonProperty("PCBReference")
    private TechnicalAsset pcbReference;

    @JsonProperty("PlannedProductionDate")
    private LocalDate plannedProductionDate;

    @JsonProperty("TestJigDetails")
    private Map<String, Object> testJigDetails = new HashMap<>();

    @JsonProperty("AssemblyDetails")
    private Map<String, Object> assemblyDetails = new HashMap<>();


    public PCBABOM() {
        super();
        this.setBomType(BOMType.PCBA);
        this.testJigDetails = new HashMap<>();
        this.assemblyDetails = new HashMap<>();
    }

    public PCBABOM(String prodNo, String customerName, String orderNo, List<BOMEntry> entries) {
        super(prodNo, customerName, orderNo, entries);
        this.setBomType(BOMType.PCBA);
        this.testJigDetails = new HashMap<>();
        this.assemblyDetails = new HashMap<>();
    }

    public LocalDate getPlannedProductionDate() {
        return plannedProductionDate;
    }

    public PCBABOM setPlannedProductionDate(LocalDate plannedProductionDate) {
        this.plannedProductionDate = plannedProductionDate;
        return this;
    }

    public Map<String, Object> getTestJigDetails() {
        return testJigDetails;
    }

    public PCBABOM setTestJigDetails(Map<String, Object> testJigDetails) {
        this.testJigDetails = testJigDetails;
        return this;
    }


    public Map<String, Object> getAssemblyDetails() {
        return assemblyDetails;
    }

    public PCBABOM setAssemblyDetails(Map<String, Object> assemblyDetails) {
        this.assemblyDetails = assemblyDetails;
        return this;
    }

    @JsonProperty("PcbReference")
    public TechnicalAsset getPcbReference() {
        return pcbReference;
    }

    @JsonProperty("PcbReference")
    public PCBABOM setPcbReference(TechnicalAsset pcbReference) {
        this.pcbReference = pcbReference;
        return this;
    }

    public void addTestJigSpec(String key, Object value) {
        testJigDetails.put(key, value);
    }

    public void addAssemblySpec(String key, Object value) {
        assemblyDetails.put(key, value);
    }

    public PCBABOM setManufacturer(String manufacturer) {
        setManufacturer(manufacturer);
        return this;
    }

}