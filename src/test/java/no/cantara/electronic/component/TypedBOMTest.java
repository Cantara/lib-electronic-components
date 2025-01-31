package no.cantara.electronic.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.*;

class TypedBOMTest {

    @Test
    void shouldHandlePCBASpecifications() {
        BOM.PCBABOM bom = new BOM.PCBABOM();
        bom.setProductionNo("TSM-2024-001");
        bom.setPlannedProductionDate(LocalDate.of(2024, 3, 15));

        // Set test jig details
        bom.getTestJigDetails().put("required", true);
        bom.getTestJigDetails().put("level", "FUNCTIONAL");
        bom.getTestJigDetails().put("testPoints", Arrays.asList(
                Map.of("id", "TP1", "type", "voltage"),
                Map.of("id", "TP2", "type", "ground")
        ));

        // Set assembly details
        bom.getAssemblyDetails().put("ipcClass", "3");
        bom.getAssemblyDetails().put("conformalCoating", Map.of(
                "required", true,
                "type", "acrylic"
        ));

        // Verify
        assertTrue((Boolean) bom.getTestJigDetails().get("required"));
        assertEquals("3", bom.getAssemblyDetails().get("ipcClass"));

        // Verify lists and nested maps
        @SuppressWarnings("unchecked")
        List<Map<String, String>> testPoints =
                (List<Map<String, String>>) bom.getTestJigDetails().get("testPoints");
        assertEquals(2, testPoints.size());
        assertEquals("voltage", testPoints.get(0).get("type"));

        @SuppressWarnings("unchecked")
        Map<String, Object> coating =
                (Map<String, Object>) bom.getAssemblyDetails().get("conformalCoating");
        assertTrue((Boolean) coating.get("required"));
        assertEquals("acrylic", coating.get("type"));
    }

    @Test
    void shouldSerializeAndDeserializePCBABOM() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        BOM.PCBABOM originalBom = new BOM.PCBABOM();
        originalBom.setProductionNo("TSM-2024-001");
        originalBom.setPlannedProductionDate(LocalDate.of(2024, 3, 15));

        // Add test jig details
        originalBom.getTestJigDetails().put("required", true);
        originalBom.getTestJigDetails().put("level", "FUNCTIONAL");
        originalBom.getTestJigDetails().put("testPoints", Arrays.asList(
                Map.of("id", "TP1", "type", "voltage"),
                Map.of("id", "TP2", "type", "ground")
        ));

        // Add assembly details
        originalBom.getAssemblyDetails().put("ipcClass", "3");
        originalBom.getAssemblyDetails().put("conformalCoating", Map.of(
                "required", true,
                "type", "acrylic"
        ));

        // Serialize to JSON
        String json = mapper.writeValueAsString(originalBom);
        System.out.println("Serialized PCBA BOM:");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(originalBom));

        // Deserialize back
        BOM.PCBABOM deserializedBom = mapper.readValue(json, BOM.PCBABOM.class);

        // Verify core properties
        assertEquals(originalBom.getProductionNo(), deserializedBom.getProductionNo());
        assertEquals(originalBom.getPlannedProductionDate(), deserializedBom.getPlannedProductionDate());

        // Verify test jig details
        assertTrue((Boolean) deserializedBom.getTestJigDetails().get("required"));
        assertEquals("FUNCTIONAL", deserializedBom.getTestJigDetails().get("level"));

        // Verify assembly details
        assertEquals("3", deserializedBom.getAssemblyDetails().get("ipcClass"));

        @SuppressWarnings("unchecked")
        Map<String, Object> coating =
                (Map<String, Object>) deserializedBom.getAssemblyDetails().get("conformalCoating");
        assertTrue((Boolean) coating.get("required"));
    }



}