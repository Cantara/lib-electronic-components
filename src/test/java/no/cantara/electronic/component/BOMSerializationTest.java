package no.cantara.electronic.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
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
    void shouldCreateAndSerializePCBABOM() throws Exception {
        BOM.PCBABOM pcbaBom = new BOM.PCBABOM();
        pcbaBom.setProductionNo("TSM-2024-001");
        pcbaBom.setCustomerName("Environmental Systems");
        pcbaBom.setOrderNo("ORD-2024-Q1-123");
        pcbaBom.setVersion("1.0");
        pcbaBom.setQuantity("100");

        // Create PCB Design asset
        TechnicalAsset pcbDesign = TechnicalAsset.createPCBDesign(
                "PCB-2024-001",
                "1.0",
                "gerber/TSM-2024-001_v1.0.zip"
        );
        pcbDesign.setName("Temperature Sensor Board");
        pcbDesign.getMetadata().put("layers", 4);
        pcbDesign.getMetadata().put("dimensions", Map.of(
                "width", "100mm",
                "height", "80mm"
        ));
        pcbaBom.setPcbReference(pcbDesign);

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
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(pcbaBom);
        System.out.println("Serialized PCBA BOM:");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(pcbaBom));

        // Deserialize back to PCBABOM
        BOM.PCBABOM deserializedBom = mapper.readValue(json, BOM.PCBABOM.class);

        // Verify the deserialized BOM
        assertEquals(pcbaBom.getProductionNo(), deserializedBom.getProductionNo());
        assertEquals(pcbaBom.getCustomerName(), deserializedBom.getCustomerName());
        assertEquals(pcbaBom.getOrderNo(), deserializedBom.getOrderNo());
        assertEquals(pcbaBom.getBomType(), deserializedBom.getBomType());

        // Verify PCB Design
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

    @Test
    void shouldHandleVariousAssetTypes() throws Exception {
        // PCB Design asset
        TechnicalAsset pcbDesign = TechnicalAsset.createPCBDesign(
                "PCB-2024-001",
                "1.0",
                "/gerber/PCB-2024-001_v1.0.zip"
        );
        pcbDesign.setName("Temperature Sensor Board");
        pcbDesign.getMetadata().put("layers", 4);
        pcbDesign.getMetadata().put("dimensions", Map.of(
                "width", "100mm",
                "height", "80mm"
        ));

        // CAD Model with URL
        TechnicalAsset cadModel = new TechnicalAsset();
        cadModel.setId("CAD-2024-001");
        cadModel.setType(TechnicalAsset.AssetType.CAD_MODEL);
        cadModel.setVersion("1.0");
        cadModel.setName("Enclosure Bottom");
        cadModel.setFormat("STEP");
        cadModel.setLocation(TechnicalAsset.AssetLocation.url(
                "https://cad.company.com/models/CAD-2024-001_v1.0.step"));
        cadModel.getMetadata().put("software", "Fusion 360");
        cadModel.getMetadata().put("units", "mm");

        // Specification with embedded data
        TechnicalAsset spec = new TechnicalAsset();
        spec.setId("SPEC-2024-001");
        spec.setType(TechnicalAsset.AssetType.SPECIFICATION);
        spec.setVersion("1.0");
        spec.setName("Material Requirements");
        spec.setFormat("PDF");
        spec.setLocation(TechnicalAsset.AssetLocation.embeddedData(
                "base64EncodedPDFContent...",
                "sha256:1234567890abcdef"));
        spec.getMetadata().put("pageCount", 12);
        spec.getMetadata().put("lastModified", "2024-01-15");

        // Serialize to JSON
        String json = objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(pcbDesign);
        System.out.println(json);

        // Deserialize and verify
        TechnicalAsset deserializedAsset = objectMapper.readValue(json, TechnicalAsset.class);
        assertEquals(TechnicalAsset.AssetType.PCB_DESIGN, deserializedAsset.getType());
        assertEquals("Gerber", deserializedAsset.getFormat());
        assertEquals(TechnicalAsset.AssetLocation.LocationType.FILE,
                deserializedAsset.getLocation().getType());
    }


    @Test
    void shouldSerializeAndDeserializeMechanicalBOM() throws Exception {
        // Create a Mechanical BOM for an enclosure part
        BOM.MechanicalBOM mechBom = new BOM.MechanicalBOM();
        mechBom.setProductionNo("MECH-2024-001");
        mechBom.setCustomerName("Environmental Systems");
        mechBom.setOrderNo("ORD-2024-Q1-123");
        mechBom.setVersion("1.0");
        mechBom.setQuantity("100");
        mechBom.setPlannedProductionDate(LocalDate.of(2024, 3, 15));

        // Create CAD Model asset
        TechnicalAsset cadModel = TechnicalAsset.createCADModel(
                "CAD-2024-001",
                "1.0",
                "/cad/MECH-2024-001_v1.0.step"
        );
        cadModel.setName("Enclosure Bottom");
        cadModel.setDescription("IP67 Rated Enclosure Bottom with Mounting Features");
        cadModel.setFormat("STEP");
        cadModel.getMetadata().put("software", "Fusion 360");
        cadModel.getMetadata().put("units", "mm");
        cadModel.getMetadata().put("mass", "450g");
        cadModel.getMetadata().put("volume", "166.7cm³");
        // For nested structures, use Map<String, Object>
        cadModel.getMetadata().put("dimensions", Map.of(
                "length", "200mm",
                "width", "150mm",
                "height", "50mm"
        ));
        mechBom.setCadModel(cadModel);

        // Set Material Details
        mechBom.getMaterialDetails().put("mainMaterial", "Aluminum 6061-T6");
        mechBom.getMaterialDetails().put("materialState", "T6");
        mechBom.getMaterialDetails().put("materialCertification", "Required");
        mechBom.getMaterialDetails().put("materialProperties", Map.of(
                "density", "2.7 g/cm³",
                "tensileStrength", "310 MPa",
                "yieldStrength", "276 MPa",
                "hardness", "95 HB"
        ));

        // Set Manufacturing Details
        mechBom.getManufacturingDetails().put("primaryProcess", "CNC Milling");
        mechBom.getManufacturingDetails().put("machiningSetups", List.of(
                Map.of(
                        "operation", "1",
                        "description", "Face mill top surface and rough pocket",
                        "tools", List.of("63mm Face Mill", "16mm Endmill")
                ),
                Map.of(
                        "operation", "2",
                        "description", "Finish walls and bottom features",
                        "tools", List.of("8mm Endmill", "6mm Ball Endmill")
                )
        ));

        // Set tolerance information
        Map<String, Object> tolerances = new HashMap<>();
        tolerances.put("general", "±0.1mm");
        tolerances.put("critical", "±0.05mm");
        tolerances.put("surfaceRoughness", Map.of(
                "general", "Ra 3.2",
                "sealingSurfaces", "Ra 1.6",
                "mountingSurfaces", "Ra 1.6"
        ));
        mechBom.getManufacturingDetails().put("tolerances", tolerances);

        // Set critical features
        mechBom.getManufacturingDetails().put("criticalFeatures", List.of(
                Map.of(
                        "feature", "PCB mounting bosses",
                        "dimension", "⌀4.2mm",
                        "tolerance", "±0.02mm",
                        "surfaceFinish", "Ra 1.6",
                        "measurement", "Required"
                ),
                Map.of(
                        "feature", "O-ring groove",
                        "dimensions", Map.of(
                                "depth", "2.0mm ±0.05mm",
                                "width", "2.5mm ±0.05mm"
                        ),
                        "surfaceFinish", "Ra 1.6",
                        "measurement", "Required"
                )
        ));

        // Set Finishing Details
        mechBom.getFinishingDetails().put("type", "Type III Hard Anodizing");
        mechBom.getFinishingDetails().put("specification", "MIL-A-8625F Type III Class 2");
        mechBom.getFinishingDetails().put("color", "Black");
        mechBom.getFinishingDetails().put("thickness", "50-100 µm");
        mechBom.getFinishingDetails().put("masking", Map.of(
                "required", true,
                "areas", List.of(
                        "O-ring groove surfaces",
                        "PCB mounting surfaces",
                        "Threaded holes"
                )
        ));
        mechBom.getFinishingDetails().put("sealant", "Hot water seal");

        // Add BOM entries
        List<BOMEntry> entries = new ArrayList<>();

        // Add O-ring
        BOMEntry oRingEntry = new BOMEntry();
        oRingEntry.setMpn("9452K159");
        oRingEntry.setManufacturer("McMaster-Carr");
        oRingEntry.setDescription("Oil-Resistant Buna-N O-Ring");
        oRingEntry.setPkg("AS568-152");
        oRingEntry.setQty("1");
        oRingEntry.getDesignators().add("OR1");
        oRingEntry.getSpecs().put("material", "Buna-N");
        oRingEntry.getSpecs().put("durometer", "70A");
        // For specs that are objects, use Object value type
        oRingEntry.getSpecs().put("size", Map.of(
                "id", "34.65mm",
                "width", "1.78mm"
        ));
        entries.add(oRingEntry);

        mechBom.setBomEntries(entries);

        // Serialize to JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(mechBom);
        System.out.println("Serialized Mechanical BOM:");
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mechBom));

        // Deserialize and verify
        BOM.MechanicalBOM deserializedBom = mapper.readValue(json, BOM.MechanicalBOM.class);

        // Verify core properties
        assertEquals(mechBom.getProductionNo(), deserializedBom.getProductionNo());
        assertEquals(mechBom.getBomType(), deserializedBom.getBomType());
        assertEquals(BOMType.MECHANICAL_PART, deserializedBom.getBomType());

        // Verify CAD Model
        assertNotNull(deserializedBom.getCadModel());
        assertEquals(mechBom.getCadModel().getId(), deserializedBom.getCadModel().getId());
        assertEquals(mechBom.getCadModel().getVersion(), deserializedBom.getCadModel().getVersion());

        // Verify Material Details
        assertEquals("Aluminum 6061-T6", deserializedBom.getMaterialDetails().get("mainMaterial"));

        // Verify Manufacturing Details - using proper casting
        @SuppressWarnings("unchecked")
        Map<String, Object> deserializedTolerances =
                (Map<String, Object>) deserializedBom.getManufacturingDetails().get("tolerances");
        assertEquals("±0.1mm", deserializedTolerances.get("general"));

        // Verify Finishing Details
        assertEquals("Type III Hard Anodizing", deserializedBom.getFinishingDetails().get("type"));
    }
}