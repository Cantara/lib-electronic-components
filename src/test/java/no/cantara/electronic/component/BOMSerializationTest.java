package no.cantara.electronic.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class BOMSerializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Test
    void shouldSerializeAndDeserializeBasicBOM() throws Exception {
        // Create a basic BOM
        BOM bom = new BOM();
        bom.setProductionNo("TSM-2024-001");
        bom.setCustomerName("Environmental Systems");
        bom.setOrderNo("ORD-2024-Q1-123");
        bom.setBomType(BOMType.PLANNING);  // Using PLANNING for basic BOM
        bom.setVersion("1.0");
        bom.setQuantity("100");

        // Add components
        List<BOMEntry> entries = new ArrayList<>();

        // Add MCU
        BOMEntry mcuEntry = new BOMEntry();
        mcuEntry.setMpn("ATTINY84A-SSU");
        mcuEntry.setManufacturer("Microchip");
        mcuEntry.setDescription("MCU 8-bit ATtiny AVR RISC 8KB Flash");
        mcuEntry.setPkg("SOIC-14");
        mcuEntry.setQty("1");
        mcuEntry.getDesignators().add("U1");
        mcuEntry.getSpecs().put("Core", "AVR");
        mcuEntry.getSpecs().put("Flash", "8KB");
        entries.add(mcuEntry);

        bom.setBomEntries(entries);

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(bom);
        System.out.println("Serialized Basic BOM:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bom));

        // Deserialize back to BOM
        BOM deserializedBom = objectMapper.readValue(json, BOM.class);

        // Verify the deserialized BOM
        assertEquals(bom.getProductionNo(), deserializedBom.getProductionNo());
        assertEquals(bom.getCustomerName(), deserializedBom.getCustomerName());
        assertEquals(bom.getOrderNo(), deserializedBom.getOrderNo());
        assertEquals(bom.getBomType(), deserializedBom.getBomType());
        assertEquals(bom.getBomEntries().size(), deserializedBom.getBomEntries().size());
    }

    @Test
    void shouldSerializeAndDeserializePCBABOM() throws Exception {
        // Create a PCBA BOM
        BOM.PCBABOM pcbaBom = new BOM.PCBABOM();
        pcbaBom.setProductionNo("TSM-2024-001");
        pcbaBom.setCustomerName("Environmental Systems");
        pcbaBom.setOrderNo("ORD-2024-Q1-123");
        pcbaBom.setVersion("1.0");
        pcbaBom.setQuantity("100");

        // Set PCB Reference
        PCBReference pcbRef = new PCBReference();
        pcbRef.setId("PCB-2024-001");
        pcbRef.setVersion("1.0");
        pcbRef.setName("Temperature Sensor");
        pcbRef.setGerberFileReference("gerber/PCB-2024-001_v1.0.zip");
        pcbaBom.setPcbReference(pcbRef);

        // Add test jig details
        pcbaBom.getTestJigDetails().put("required", true);
        pcbaBom.getTestJigDetails().put("level", "FUNCTIONAL");
        pcbaBom.getTestJigDetails().put("testPoints", Arrays.asList(
                Map.of("id", "TP1", "type", "voltage"),
                Map.of("id", "TP2", "type", "ground")
        ));

        // Add assembly details
        pcbaBom.getAssemblyDetails().put("ipcClass", "3");
        pcbaBom.getAssemblyDetails().put("conformalCoating", Map.of(
                "required", true,
                "type", "acrylic"
        ));

        // Add components
        List<BOMEntry> entries = new ArrayList<>();

        // Add MCU
        BOMEntry mcuEntry = new BOMEntry();
        mcuEntry.setMpn("ATTINY84A-SSU");
        mcuEntry.setManufacturer("Microchip");
        mcuEntry.setDescription("MCU 8-bit ATtiny AVR RISC 8KB Flash");
        mcuEntry.setPkg("SOIC-14");
        mcuEntry.setQty("1");
        mcuEntry.getDesignators().add("U1");
        mcuEntry.getSpecs().put("Core", "AVR");
        mcuEntry.getSpecs().put("Flash", "8KB");
        entries.add(mcuEntry);

        pcbaBom.setBomEntries(entries);

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(pcbaBom);
        System.out.println("Serialized PCBA BOM:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pcbaBom));

        // Deserialize back to PCBABOM
        BOM.PCBABOM deserializedBom = objectMapper.readValue(json, BOM.PCBABOM.class);

        // Verify the deserialized BOM
        assertEquals(pcbaBom.getProductionNo(), deserializedBom.getProductionNo());
        assertEquals(pcbaBom.getCustomerName(), deserializedBom.getCustomerName());
        assertEquals(pcbaBom.getOrderNo(), deserializedBom.getOrderNo());
        assertEquals(pcbaBom.getBomType(), deserializedBom.getBomType());

        // Verify PCB Reference
        assertNotNull(deserializedBom.getPcbReference());
        assertEquals(pcbaBom.getPcbReference().getId(), deserializedBom.getPcbReference().getId());
        assertEquals(pcbaBom.getPcbReference().getVersion(), deserializedBom.getPcbReference().getVersion());

        // Verify Test Jig Details
        assertTrue((Boolean) deserializedBom.getTestJigDetails().get("required"));
        assertEquals("FUNCTIONAL", deserializedBom.getTestJigDetails().get("level"));

        // Verify Assembly Details
        assertEquals("3", deserializedBom.getAssemblyDetails().get("ipcClass"));

        @SuppressWarnings("unchecked")
        Map<String, Object> coating =
                (Map<String, Object>) deserializedBom.getAssemblyDetails().get("conformalCoating");
        assertTrue((Boolean) coating.get("required"));
        assertEquals("acrylic", coating.get("type"));
    }

    @Test
    void shouldHandleNullValues() throws Exception {
        BOM bom = new BOM();
        bom.setProductionNo("TEST-001");
        // Leave other fields null

        String json = objectMapper.writeValueAsString(bom);
        BOM deserializedBom = objectMapper.readValue(json, BOM.class);

        assertEquals(bom.getProductionNo(), deserializedBom.getProductionNo());
        assertNull(deserializedBom.getCustomerName());
        assertNull(deserializedBom.getOrderNo());
    }

    @Test
    void shouldHandleEmptyCollections() throws Exception {
        BOM bom = new BOM();
        bom.setProductionNo("TEST-001");
        bom.setBomEntries(new ArrayList<>());

        String json = objectMapper.writeValueAsString(bom);
        BOM deserializedBom = objectMapper.readValue(json, BOM.class);

        assertNotNull(deserializedBom.getBomEntries());
        assertTrue(deserializedBom.getBomEntries().isEmpty());
    }
}