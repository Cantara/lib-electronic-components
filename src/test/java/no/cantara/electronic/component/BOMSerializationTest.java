package no.cantara.electronic.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BOMSerializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSerializeAndDeserializeBOM() throws Exception {
        // Create a test BOM
        BOM originalBom = createTestBOM();

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(originalBom);
        System.out.println("Serialized JSON:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(originalBom));

        // Deserialize back to BOM
        BOM deserializedBom = objectMapper.readValue(json, BOM.class);

        // Verify the deserialized BOM matches the original
        assertBOMsEqual(originalBom, deserializedBom);
    }

    private BOM createTestBOM() {
        BOM bom = new BOM(
                "TSM-2024-001",           // Production number
                "Environmental Systems",   // Customer name
                "ORD-2024-Q1-123",        // Order number
                new ArrayList<>()          // We'll add entries
        );
        bom.setBomType(BOMType.PCBA);
        bom.setVersion("1.0");
        bom.setQuantity("100");

        // Add PCB Reference
        PCBReference pcbRef = new PCBReference();
        pcbRef.setId("PCB-2024-001");        // PCB_ID
        pcbRef.setVersion("1.0");            // Version
        pcbRef.setName("Temperature Sensor"); // Name
        pcbRef.setGerberFileReference("gerber/PCB-2024-001_v1.0.zip"); // GerberFileReference
        bom.setPcbReference(pcbRef);

        // Add components
        List<BOMEntry> entries = new ArrayList<>();

        // 1. Microcontroller
        BOMEntry mcuEntry = new BOMEntry();
        mcuEntry.setMpn("ATTINY84A-SSU");
        mcuEntry.setManufacturer("Microchip");
        mcuEntry.setDescription("MCU 8-bit ATtiny AVR RISC 8KB Flash");
        mcuEntry.setPkg("SOIC-14");
        mcuEntry.setQty("1");
        mcuEntry.getDesignators().add("U1");
        mcuEntry.getSpecs().put("Core", "AVR");
        mcuEntry.getSpecs().put("Flash", "8KB");
        mcuEntry.getSpecs().put("Voltage", "1.8-5.5V");
        entries.add(mcuEntry);

        // 2. Temperature Sensor
        BOMEntry sensorEntry = new BOMEntry();
        sensorEntry.setMpn("SHT31-DIS-B");
        sensorEntry.setManufacturer("Sensirion");
        sensorEntry.setDescription("Temperature/Humidity Sensor Digital");
        sensorEntry.setPkg("DFN-8");
        sensorEntry.setQty("1");
        sensorEntry.getDesignators().add("U2");
        sensorEntry.getSpecs().put("Interface", "I2C");
        sensorEntry.getSpecs().put("Accuracy", "±0.2°C");
        sensorEntry.getSpecs().put("Supply", "2.4-5.5V");
        entries.add(sensorEntry);

        // Add all entries to BOM
        bom.setBomEntries(entries);

        // Add some additional properties
        bom.setAdditionalProperty("project", "Temperature Monitor");
        bom.setAdditionalProperty("designer", "John Doe");
        bom.setAdditionalProperty("review_date", "2024-01-15");

        return bom;
    }

    private void assertBOMsEqual(BOM expected, BOM actual) {
        // Basic BOM properties
        assertEquals(expected.getProductionNo(), actual.getProductionNo(), "Production number should match");
        assertEquals(expected.getCustomerName(), actual.getCustomerName(), "Customer name should match");
        assertEquals(expected.getOrderNo(), actual.getOrderNo(), "Order number should match");
        assertEquals(expected.getBomType(), actual.getBomType(), "BOM type should match");
        assertEquals(expected.getVersion(), actual.getVersion(), "Version should match");
        assertEquals(expected.getQuantity(), actual.getQuantity(), "Quantity should match");

        // PCB Reference
        assertPCBReferenceEqual(expected.getPcbReference(), actual.getPcbReference());

        // BOM Entries
        assertEquals(expected.getBomEntries().size(), actual.getBomEntries().size(), "Number of BOM entries should match");
        for (int i = 0; i < expected.getBomEntries().size(); i++) {
            assertBOMEntryEqual(expected.getBomEntries().get(i), actual.getBomEntries().get(i));
        }

        // Additional Properties
        assertEquals(expected.getAdditionalProperties(), actual.getAdditionalProperties(), "Additional properties should match");
    }

    private void assertPCBReferenceEqual(PCBReference expected, PCBReference actual) {
        if (expected == null && actual == null) return;
        assertNotNull(expected, "Expected PCB reference should not be null");
        assertNotNull(actual, "Actual PCB reference should not be null");
        assertEquals(expected.getId(), actual.getId(), "PCB ID should match");
        assertEquals(expected.getVersion(), actual.getVersion(), "PCB version should match");
        assertEquals(expected.getName(), actual.getName(), "PCB name should match");
        assertEquals(expected.getGerberFileReference(), actual.getGerberFileReference(), "Gerber file reference should match");
    }




    private void assertBOMEntryEqual(BOMEntry expected, BOMEntry actual) {
        assertEquals(expected.getMpn(), actual.getMpn(), "MPN should match");
        assertEquals(expected.getManufacturer(), actual.getManufacturer(), "Manufacturer should match");
        assertEquals(expected.getDescription(), actual.getDescription(), "Description should match");
        assertEquals(expected.getPkg(), actual.getPkg(), "Package should match");
        assertEquals(expected.getQty(), actual.getQty(), "Quantity should match");
        assertEquals(expected.getValue(), actual.getValue(), "Value should match");

        // Compare designators
        assertEquals(
                new HashSet<>(expected.getDesignators()),
                new HashSet<>(actual.getDesignators()),
                "Designators should match"
        );

        // Compare specifications
        assertEquals(expected.getSpecs(), actual.getSpecs(), "Specifications should match");

        // Compare additional properties
        assertEquals(
                expected.getAdditionalProperties(),
                actual.getAdditionalProperties(),
                "Additional properties should match"
        );
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

    @Test
    void shouldPreserveSpecialCharacters() throws Exception {
        BOM bom = new BOM();
        bom.setProductionNo("TEST-001");

        BOMEntry entry = new BOMEntry();
        entry.setDescription("Resistor ±1% μΩ");
        entry.getSpecs().put("tolerance", "±1%");
        entry.getSpecs().put("temperature", "25°C");

        bom.setBomEntries(Collections.singletonList(entry));

        String json = objectMapper.writeValueAsString(bom);
        BOM deserializedBom = objectMapper.readValue(json, BOM.class);

        assertEquals("Resistor ±1% μΩ", deserializedBom.getBomEntries().get(0).getDescription());
        assertEquals("±1%", deserializedBom.getBomEntries().get(0).getSpecs().get("tolerance"));
        assertEquals("25°C", deserializedBom.getBomEntries().get(0).getSpecs().get("temperature"));
    }

    @Test
    void shouldHandleAdditionalProperties() throws Exception {
        // Create a BOM with additional properties
        BOM bom = new BOM();
        bom.setProductionNo("TSM-2024-001");
        bom.setCustomerName("Environmental Systems");
        bom.setOrderNo("ORD-2024-Q1-123");
        bom.setBomType(BOMType.PCBA);
        bom.setVersion("1.0");
        bom.setQuantity("100");

        // Add additional properties
        bom.setAdditionalProperty("projectName", "Smart Temperature Monitor");
        bom.setAdditionalProperty("designRevision", "A1");
        bom.setAdditionalProperty("designer", "John Doe");
        bom.setAdditionalProperty("reviewDate", "2024-01-15");
        bom.setAdditionalProperty("approvedBy", "Jane Smith");
        bom.setAdditionalProperty("estimatedCost", 156.78);
        bom.setAdditionalProperty("assemblyNotes", Arrays.asList(
                "Use lead-free solder",
                "Check component orientation",
                "Handle sensors with ESD protection"
        ));

        // Add nested additional property
        Map<String, Object> environmentalSpecs = new HashMap<>();
        environmentalSpecs.put("operatingTemp", "-40°C to +85°C");
        environmentalSpecs.put("humidity", "0-95% non-condensing");
        environmentalSpecs.put("ipRating", "IP67");
        bom.setAdditionalProperty("environmentalSpecs", environmentalSpecs);

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(bom);
        System.out.println("Serialized JSON with additional properties:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bom));

        // Deserialize back to BOM
        BOM deserializedBom = objectMapper.readValue(json, BOM.class);

        // Verify basic properties
        assertEquals(bom.getProductionNo(), deserializedBom.getProductionNo());
        assertEquals(bom.getBomType(), deserializedBom.getBomType());

        // Verify additional properties
        Map<String, Object> originalAdditional = bom.getAdditionalProperties();
        Map<String, Object> deserializedAdditional = deserializedBom.getAdditionalProperties();

        assertEquals(originalAdditional.size(), deserializedAdditional.size(),
                "Should have same number of additional properties");

        assertEquals("Smart Temperature Monitor", deserializedAdditional.get("projectName"));
        assertEquals("A1", deserializedAdditional.get("designRevision"));
        assertEquals("John Doe", deserializedAdditional.get("designer"));
        assertEquals("2024-01-15", deserializedAdditional.get("reviewDate"));
        assertEquals("Jane Smith", deserializedAdditional.get("approvedBy"));

        // Verify number value (Note: JSON deserialization might return different numeric types)
        assertTrue(deserializedAdditional.get("estimatedCost") instanceof Number);
        assertEquals(156.78, ((Number) deserializedAdditional.get("estimatedCost")).doubleValue(), 0.001);

        // Verify list
        @SuppressWarnings("unchecked")
        List<String> deserializedNotes = (List<String>) deserializedAdditional.get("assemblyNotes");
        assertEquals(3, deserializedNotes.size());
        assertTrue(deserializedNotes.contains("Use lead-free solder"));
        assertTrue(deserializedNotes.contains("Check component orientation"));
        assertTrue(deserializedNotes.contains("Handle sensors with ESD protection"));

        // Verify nested map
        @SuppressWarnings("unchecked")
        Map<String, String> deserializedEnvSpecs =
                (Map<String, String>) deserializedAdditional.get("environmentalSpecs");
        assertEquals("-40°C to +85°C", deserializedEnvSpecs.get("operatingTemp"));
        assertEquals("0-95% non-condensing", deserializedEnvSpecs.get("humidity"));
        assertEquals("IP67", deserializedEnvSpecs.get("ipRating"));
    }

    @Test
    void shouldHandleComplexAdditionalProperties() throws Exception {
        BOM bom = new BOM();
        bom.setProductionNo("TSM-2024-002");
        bom.setBomType(BOMType.PCBA);

        // Add a complex nested structure as additional properties
        Map<String, Object> testSpecs = new HashMap<>();

        // Add test requirements
        List<Map<String, Object>> testRequirements = new ArrayList<>();
        Map<String, Object> tempTest = new HashMap<>();
        tempTest.put("name", "Temperature Cycle");
        tempTest.put("min", -40);
        tempTest.put("max", 85);
        tempTest.put("cycles", 100);
        tempTest.put("required", true);
        testRequirements.add(tempTest);

        Map<String, Object> vibeTest = new HashMap<>();
        vibeTest.put("name", "Vibration Test");
        vibeTest.put("frequency", "10-500Hz");
        vibeTest.put("duration", 30);
        vibeTest.put("required", true);
        testRequirements.add(vibeTest);

        testSpecs.put("requirements", testRequirements);

        // Add test conditions
        Map<String, List<String>> testConditions = new HashMap<>();
        testConditions.put("pre", Arrays.asList("Visual inspection", "Functional test"));
        testConditions.put("post", Arrays.asList("Visual inspection", "Full functional test", "Report generation"));
        testSpecs.put("conditions", testConditions);

        bom.setAdditionalProperty("testSpecification", testSpecs);

        // Serialize and deserialize
        String json = objectMapper.writeValueAsString(bom);
        System.out.println("\nSerialized Complex JSON:");
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bom));

        BOM deserializedBom = objectMapper.readValue(json, BOM.class);

        // Verify complex nested structure
        @SuppressWarnings("unchecked")
        Map<String, Object> deserializedTestSpecs =
                (Map<String, Object>) deserializedBom.getAdditionalProperties().get("testSpecification");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> deserializedRequirements =
                (List<Map<String, Object>>) deserializedTestSpecs.get("requirements");

        // Verify temperature test
        Map<String, Object> deserializedTempTest = deserializedRequirements.get(0);
        assertEquals("Temperature Cycle", deserializedTempTest.get("name"));
        assertEquals(-40, ((Number) deserializedTempTest.get("min")).intValue());
        assertEquals(85, ((Number) deserializedTempTest.get("max")).intValue());
        assertEquals(100, ((Number) deserializedTempTest.get("cycles")).intValue());
        assertTrue((Boolean) deserializedTempTest.get("required"));

        // Verify test conditions
        @SuppressWarnings("unchecked")
        Map<String, List<String>> deserializedConditions =
                (Map<String, List<String>>) deserializedTestSpecs.get("conditions");
        assertEquals(2, deserializedConditions.get("pre").size());
        assertEquals(3, deserializedConditions.get("post").size());
        assertTrue(deserializedConditions.get("post").contains("Report generation"));
    }
}