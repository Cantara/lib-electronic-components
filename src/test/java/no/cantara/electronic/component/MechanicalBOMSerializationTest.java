package no.cantara.electronic.component;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.*;

class MechanicalBOMSerializationTest {

    private JsonMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Jackson 3.x has built-in Java 8 date/time support
        objectMapper = JsonMapper.builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
    }

    @Test
    void shouldSerializeAndDeserializeMechanicalBOM() throws Exception {
        // Create mechanical BOM
        MechanicalBOM mechBom = new MechanicalBOM();
        mechBom.setProductionNo("MECH-2024-001");
        mechBom.setCustomerName("Industrial IoT Solutions");
        mechBom.setOrderNo("ORD-2024-M1-123");
        mechBom.setVersion("1.0");
        mechBom.setQuantity("100");
        mechBom.setPlannedProductionDate(LocalDate.of(2024, 3, 15));

        // Create and set up CAD Model
        TechnicalAsset cadModel = TechnicalAsset.createCADModel(
                "CAD-2024-001",
                "1.0",
                "/cad/MECH-2024-001_v1.0.step"
        );
        cadModel.setName("IP67 Electronics Enclosure");
        cadModel.setDescription("Custom CNC-machined aluminum enclosure with integrated heat dissipation");
        cadModel.setFormat("STEP");

        // Add CAD metadata
        Map<String, Object> cadMetadata = new HashMap<>();
        cadMetadata.put("software", "Fusion 360");
        cadMetadata.put("units", "mm");
        cadMetadata.put("mass", "450g");
        cadMetadata.put("volume", "166.7cm³");
        cadMetadata.put("surfaceFinish", "Clear Anodized");
        cadMetadata.put("length", "200mm");
        cadMetadata.put("width", "150mm");
        cadMetadata.put("height", "50mm");
        cadModel.setMetadata(cadMetadata);

        mechBom.setCadModel(cadModel);

        // Set material details
        Map<String, Object> materialDetails = new HashMap<>();
        materialDetails.put("mainMaterial", "Aluminum 6061-T6");
        materialDetails.put("materialState", "T6");
        materialDetails.put("materialCertification", "Required");
        materialDetails.put("density", "2.7 g/cm³");
        materialDetails.put("tensileStrength", "310 MPa");
        materialDetails.put("yieldStrength", "276 MPa");
        materialDetails.put("hardness", "95 HB");
        materialDetails.put("thermalConductivity", "167 W/m·K");
        materialDetails.put("anodizingSpec", "MIL-A-8625 Type II Class 2");
        mechBom.setMaterialDetails(materialDetails);

        // Set manufacturing details
        Map<String, Object> manufacturingDetails = new HashMap<>();
        manufacturingDetails.put("process", "CNC Machining");
        manufacturingDetails.put("tolerances", "±0.1mm");
        manufacturingDetails.put("surfaceTreatment", "Clear Anodizing");
        manufacturingDetails.put("threadLocking", "Helicoil inserts required");
        manufacturingDetails.put("qualityLevel", "Industrial");
        mechBom.setManufacturingDetails(manufacturingDetails);

        // Create BOM entries
        List<BOMEntry> entries = new ArrayList<>();

        // 1. Main O-ring seal
        BOMEntry mainSealEntry = new BOMEntry()
                .setMpn("9452K159")
                .setManufacturer("Parker")
                .setDescription("Oil-Resistant Buna-N O-Ring")
                .setPkg("AS568-152")
                .setQty("1");
        mainSealEntry.getDesignators().add("OR1");
        mainSealEntry.addSpec("material", "Buna-N");
        mainSealEntry.addSpec("durometer", "70A");
        mainSealEntry.addSpec("innerDiameter", "34.65mm");
        mainSealEntry.addSpec("width", "1.78mm");
        mainSealEntry.addSpec("temperature", "-30°C to +100°C");
        mainSealEntry.addSpec("compression", "25%");
        entries.add(mainSealEntry);

        // 2. Thread inserts
        BOMEntry threadInsertEntry = new BOMEntry()
                .setMpn("1185-3CN-312")
                .setManufacturer("Böllhoff")
                .setDescription("Helicoil Thread Insert M3")
                .setPkg("Bulk")
                .setQty("12");
        threadInsertEntry.getDesignators().addAll(Arrays.asList(
                "TI1", "TI2", "TI3", "TI4", "TI5", "TI6",
                "TI7", "TI8", "TI9", "TI10", "TI11", "TI12"
        ));
        threadInsertEntry.addSpec("material", "Stainless Steel 304");
        threadInsertEntry.addSpec("threadSize", "M3");
        threadInsertEntry.addSpec("length", "4.8mm");
        threadInsertEntry.addSpec("installationTorque", "1.2Nm");
        entries.add(threadInsertEntry);

        mechBom.setBomEntries(entries);

        // Serialize to JSON
        String json = objectMapper.writeValueAsString(mechBom);

        // Print formatted JSON for inspection
        System.out.println("Serialized Mechanical BOM:");
        System.out.println(json);

        // Deserialize back to object
        MechanicalBOM deserializedBom = objectMapper.readValue(json, MechanicalBOM.class);

        // Verify core properties
        assertEquals(mechBom.getProductionNo(), deserializedBom.getProductionNo());
        assertEquals(mechBom.getCustomerName(), deserializedBom.getCustomerName());
        assertEquals(mechBom.getOrderNo(), deserializedBom.getOrderNo());
        assertEquals(mechBom.getVersion(), deserializedBom.getVersion());
        assertEquals(mechBom.getQuantity(), deserializedBom.getQuantity());
        assertEquals(mechBom.getPlannedProductionDate(), deserializedBom.getPlannedProductionDate());

        // Verify CAD Model
        assertNotNull(deserializedBom.getCadModel());
        assertEquals(mechBom.getCadModel().getId(), deserializedBom.getCadModel().getId());
        assertEquals(mechBom.getCadModel().getVersion(), deserializedBom.getCadModel().getVersion());
        assertEquals(mechBom.getCadModel().getFormat(), deserializedBom.getCadModel().getFormat());

        // Verify metadata
        Map<String, Object> deserializedMetadata = deserializedBom.getCadModel().getMetadata();
        assertEquals(cadMetadata.size(), deserializedMetadata.size());
        cadMetadata.forEach((key, value) ->
                assertEquals(value, deserializedMetadata.get(key)));

        // Verify material details
        Map<String, Object> deserializedMaterialDetails = deserializedBom.getMaterialDetails();
        assertEquals(materialDetails.size(), deserializedMaterialDetails.size());
        materialDetails.forEach((key, value) ->
                assertEquals(value, deserializedMaterialDetails.get(key)));

        // Verify manufacturing details
        Map<String, Object> deserializedManufacturingDetails = deserializedBom.getManufacturingDetails();
        assertEquals(manufacturingDetails.size(), deserializedManufacturingDetails.size());
        manufacturingDetails.forEach((key, value) ->
                assertEquals(value, deserializedManufacturingDetails.get(key)));

        // Verify BOM entries
        assertEquals(mechBom.getBomEntries().size(), deserializedBom.getBomEntries().size());

        // Verify O-ring entry
        BOMEntry deserializedORing = findEntryByMpn(deserializedBom, "9452K159");
        assertNotNull(deserializedORing);
        assertEquals(mainSealEntry.getDescription(), deserializedORing.getDescription());
        assertEquals(mainSealEntry.getManufacturer(), deserializedORing.getManufacturer());
        assertEquals(mainSealEntry.getQty(), deserializedORing.getQty());
        assertEquals(mainSealEntry.getSpecs().size(), deserializedORing.getSpecs().size());

        // Verify thread insert entry
        BOMEntry deserializedInsert = findEntryByMpn(deserializedBom, "1185-3CN-312");
        assertNotNull(deserializedInsert);
        assertEquals(threadInsertEntry.getDescription(), deserializedInsert.getDescription());
        assertEquals(threadInsertEntry.getManufacturer(), deserializedInsert.getManufacturer());
        assertEquals(threadInsertEntry.getQty(), deserializedInsert.getQty());
        assertEquals(threadInsertEntry.getDesignators().size(), deserializedInsert.getDesignators().size());
        assertEquals(threadInsertEntry.getSpecs().size(), deserializedInsert.getSpecs().size());
    }

    private BOMEntry findEntryByMpn(BOM bom, String mpn) {
        return bom.getBomEntries().stream()
                .filter(entry -> mpn.equals(entry.getMpn()))
                .findFirst()
                .orElse(null);
    }

    @Test
    void shouldHandleEmptyCollections() throws Exception {
        MechanicalBOM mechBom = new MechanicalBOM();
        mechBom.setProductionNo("TEST-001");
        mechBom.setBomEntries(new ArrayList<>());
        mechBom.setMaterialDetails(new HashMap<>());
        mechBom.setManufacturingDetails(new HashMap<>());

        String json = objectMapper.writeValueAsString(mechBom);
        MechanicalBOM deserializedBom = objectMapper.readValue(json, MechanicalBOM.class);

        assertNotNull(deserializedBom.getBomEntries());
        assertTrue(deserializedBom.getBomEntries().isEmpty());
        assertNotNull(deserializedBom.getMaterialDetails());
        assertTrue(deserializedBom.getMaterialDetails().isEmpty());
        assertNotNull(deserializedBom.getManufacturingDetails());
        assertTrue(deserializedBom.getManufacturingDetails().isEmpty());
    }

    @Test
    void shouldHandleSpecialCharacters() throws Exception {
        MechanicalBOM mechBom = new MechanicalBOM();
        mechBom.setProductionNo("TEST-001");

        BOMEntry entry = new BOMEntry()
                .setMpn("SPEC-µm-Ω-°C")
                .setManufacturer("Test & Company")
                .setDescription("Special © Characters ®")
                .setPkg("Custom™")
                .setQty("1");
        entry.addSpec("dimension", "10±0.1µm");
        entry.addSpec("temperature", "-40°C to +85°C");

        mechBom.setBomEntries(Collections.singletonList(entry));

        String json = objectMapper.writeValueAsString(mechBom);
        MechanicalBOM deserializedBom = objectMapper.readValue(json, MechanicalBOM.class);

        BOMEntry deserializedEntry = deserializedBom.getBomEntries().get(0);
        assertEquals(entry.getMpn(), deserializedEntry.getMpn());
        assertEquals(entry.getManufacturer(), deserializedEntry.getManufacturer());
        assertEquals(entry.getDescription(), deserializedEntry.getDescription());
        assertEquals(entry.getSpecs(), deserializedEntry.getSpecs());
    }
}