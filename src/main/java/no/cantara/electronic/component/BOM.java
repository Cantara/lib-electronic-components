package no.cantara.electronic.component;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ProductionNo",
        "BOMType",
        "CustomerName",
        "OrderNo",
        "Quantity",
        "PCBReference",
        "BOMEntries"
})
public class BOM implements Serializable
{

    @JsonProperty("ProductionNo")
    private String productionNo;
    @JsonProperty("CustomerName")
    private String customerName;
    @JsonProperty("OrderNo")
    private String orderNo;
    @JsonProperty("BOMType")
    private BOMType bomType;
    @JsonProperty("Version")
    private String version;
    @JsonProperty("Quantity")
    private String quantity;
    @JsonProperty("BOMEntries")
    private List<BOMEntry> bomEntries = new ArrayList<BOMEntry>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();
    private final static long serialVersionUID = 4172683921030305406L;

    /**
     * No args constructor for use in serialization
     *
     */
    public BOM() {
    }

    /**
     *
     * @param prodNo
     * @param bom
     * @param orderNo
     * @param customerName
     */
    public BOM(String prodNo, String customerName, String orderNo, List<BOMEntry> bom) {
        super();
        this.productionNo = prodNo;
        this.customerName = customerName;
        this.orderNo = orderNo;
        this.bomEntries = bom;
    }

    @JsonProperty("ProdNo")
    public String getProductionNo() {
        return productionNo;
    }

    @JsonProperty("ProdNo")
    public void setProductionNo(String productionNo) {
        this.productionNo = productionNo;
    }

    @JsonProperty("CustomerName")
    public String getCustomerName() {
        return customerName;
    }

    @JsonProperty("CustomerName")
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @JsonProperty("OrderNo")
    public String getOrderNo() {
        return orderNo;
    }

    @JsonProperty("OrderNo")
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @JsonProperty("BOMEntry")
    public List<BOMEntry> getBomEntries() {
        return bomEntries;
    }

    @JsonProperty("BOMEntry")
    public void setBomEntries(List<BOMEntry> bomEntries) {
        this.bomEntries = bomEntries;
    }

    @JsonProperty("BOMType")
    public BOMType getBomType() {
        return bomType;
    }

    @JsonProperty("BOMType")
    public void setBomType(BOMType bomType) {
        this.bomType = bomType;
    }

    @JsonProperty("Version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("Version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("Quantity")
    public String getQuantity() {
        return quantity;
    }

    @JsonProperty("Quantity")
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }



    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


    // PCBA specific BOM
    public static class PCBABOM extends BOM {

        @JsonProperty("PCBReference")
        private PCBReference pcbReference;

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

        public void setPlannedProductionDate(LocalDate plannedProductionDate) {
            this.plannedProductionDate = plannedProductionDate;
        }

        public Map<String, Object> getTestJigDetails() {
            return testJigDetails;
        }

        public void setTestJigDetails(Map<String, Object> testJigDetails) {
            this.testJigDetails = testJigDetails;
        }

        public Map<String, Object> getAssemblyDetails() {
            return assemblyDetails;
        }

        public void setAssemblyDetails(Map<String, Object> assemblyDetails) {
            this.assemblyDetails = assemblyDetails;
        }

        @JsonProperty("PcbReference")
        public PCBReference getPcbReference() {
            return pcbReference;
        }

        @JsonProperty("PcbReference")
        public void setPcbReference(PCBReference pcbReference) {
            this.pcbReference = pcbReference;
        }
    }
}
