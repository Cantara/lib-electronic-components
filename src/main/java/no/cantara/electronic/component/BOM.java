package no.cantara.electronic.component;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ProductionNo",
        "BOMType",
        "CustomerName",
        "OrderNo",
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

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }



}
