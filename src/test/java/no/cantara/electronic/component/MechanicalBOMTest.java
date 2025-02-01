package no.cantara.electronic.component;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.*;

class MechanicalBOMTest {

    @Test
    void shouldCreateCustomEnclosureBOM() {
        // Create a BOM for a custom CNC-machined electronics enclosure
        MechanicalBOM mechBom = new MechanicalBOM();
        mechBom.setProductionNo("MECH-2024-001");
        mechBom.setCustomerName("Industrial IoT Solutions");
        mechBom.setOrderNo("ORD-2024-M1-123");
        mechBom.setVersion("1.0");
        mechBom.setQuantity("100");
        mechBom.setPlannedProductionDate(LocalDate.of(2024, 3, 15));

        // Create CAD Model asset
        TechnicalAsset cadModel = TechnicalAsset.createCADModel(
                "CAD-2024-001",
                "1.0",
                "/cad/MECH-2024-001_v1.0.step"
        );
        cadModel.setName("IP67 Electronics Enclosure");
        cadModel.setDescription("Custom CNC-machined aluminum enclosure with integrated heat dissipation");
        cadModel.setFormat("STEP");

        // Add detailed metadata for manufacturing
        cadModel.getMetadata().put("software", "Fusion 360");
        cadModel.getMetadata().put("units", "mm");
        cadModel.getMetadata().put("mass", "450g");
        cadModel.getMetadata().put("volume", "166.7cm³");
        cadModel.getMetadata().put("surfaceFinish", "Clear Anodized");
        cadModel.getMetadata().put("length", "200mm");
        cadModel.getMetadata().put("width", "150mm");
        cadModel.getMetadata().put("height", "50mm");

        mechBom.setCadModel(cadModel);

        // Set Material Details with comprehensive specifications
        mechBom.getMaterialDetails().put("mainMaterial", "Aluminum 6061-T6");
        mechBom.getMaterialDetails().put("materialState", "T6");
        mechBom.getMaterialDetails().put("materialCertification", "Required");
        mechBom.getMaterialDetails().put("density", "2.7 g/cm³");
        mechBom.getMaterialDetails().put("tensileStrength", "310 MPa");
        mechBom.getMaterialDetails().put("yieldStrength", "276 MPa");
        mechBom.getMaterialDetails().put("hardness", "95 HB");
        mechBom.getMaterialDetails().put("thermalConductivity", "167 W/m·K");
        mechBom.getMaterialDetails().put("anodizingSpec", "MIL-A-8625 Type II Class 2");

        // Add Manufacturing Details
        mechBom.getManufacturingDetails().put("process", "CNC Machining");
        mechBom.getManufacturingDetails().put("tolerances", "±0.1mm");
        mechBom.getManufacturingDetails().put("surfaceTreatment", "Clear Anodizing");
        mechBom.getManufacturingDetails().put("threadLocking", "Helicoil inserts required");
        mechBom.getManufacturingDetails().put("qualityLevel", "Industrial");

        List<BOMEntry> entries = new ArrayList<>();

        // 1. Main O-ring seal
        BOMEntry mainSealEntry = new BOMEntry()
                .setMpn("9452K159")
                .setManufacturer("Parker")
                .setDescription("Oil-Resistant Buna-N O-Ring")
                .setPkg("AS568-152")
                .setQty("1");
        mainSealEntry.getDesignators().add("OR1");
        mainSealEntry.addSpecifications(
                "material", "Buna-N",
                "durometer", "70A",
                "innerDiameter", "34.65mm",
                "width", "1.78mm",
                "temperature", "-30°C to +100°C",
                "compression", "25%"
        );

        // 2. Helicoil thread inserts
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
        threadInsertEntry.addSpecifications(
                "material", "Stainless Steel 304",
                "threadSize", "M3",
                "length", "4.8mm",
                "installationTorque", "1.2Nm"
        );

        // 3. Thermal interface material
        BOMEntry timEntry = new BOMEntry()
                .setMpn("TG-A6200-50-50-0.5")
                .setManufacturer("t-Global Technology")
                .setDescription("Thermal Interface Pad")
                .setPkg("Custom Cut")
                .setQty("1");
        timEntry.getDesignators().add("TIM1");
        timEntry.addSpecifications(
                "thermalConductivity", "6.2 W/m·K",
                "thickness", "0.5mm",
                "size", "50mm x 50mm",
                "compression", "30%",
                "temperature", "-40°C to +200°C"
        );

        // 4. Cable gland
        BOMEntry cableGlandEntry = new BOMEntry()
                .setMpn("SKINTOP-M12")
                .setManufacturer("Lapp")
                .setDescription("IP68 Cable Gland M12")
                .setPkg("Single")
                .setQty("2");
        cableGlandEntry.getDesignators().addAll(Arrays.asList("CG1", "CG2"));
        cableGlandEntry.addSpecifications(
                "material", "Polyamide",
                "threadSize", "M12x1.5",
                "cableRange", "3-7mm",
                "protection", "IP68",
                "color", "RAL 7001",
                "torque", "0.9Nm"
        );

        // 5. Mounting hardware
        BOMEntry mountingBracketEntry = new BOMEntry()
                .setMpn("MB-AL-3030")
                .setManufacturer("Internal")
                .setDescription("Aluminum Mounting Bracket")
                .setPkg("Set")
                .setQty("4");
        mountingBracketEntry.getDesignators().addAll(Arrays.asList("MB1", "MB2", "MB3", "MB4"));
        mountingBracketEntry.addSpecifications(
                "material", "Aluminum 5052-H32",
                "thickness", "3mm",
                "finish", "Clear Anodized",
                "loadRating", "50N"
        );

        // Add all entries to BOM
        entries.addAll(Arrays.asList(
                mainSealEntry, threadInsertEntry, timEntry,
                cableGlandEntry, mountingBracketEntry
        ));
        mechBom.setBomEntries(entries);

        // Verify BOM structure
        assertEquals("MECH-2024-001", mechBom.getProductionNo());
        assertEquals(BOMType.MECHANICAL_PART, mechBom.getBomType());
        assertEquals(5, mechBom.getBomEntries().size());

        // Verify sealing components
        verifySealing(mechBom);

        // Verify thermal management
        verifyThermalManagement(mechBom);

        // Verify structural integrity
        verifyStructuralIntegrity(mechBom);

        // Verify manufacturing requirements
        verifyManufacturingRequirements(mechBom);
    }

    private void verifySealing(MechanicalBOM bom) {
        // Verify O-ring specifications
        BOMEntry oring = findComponentByType(bom, "O-Ring");
        assertNotNull(oring, "O-ring must be present");

        String material = oring.getSpecs().get("material");
        assertTrue(material != null && material.contains("Buna-N"),
                "O-ring must be made of appropriate material for environmental sealing");

        // Verify IP rating components
        long sealingComponentCount = bom.getBomEntries().stream()
                .filter(e -> e.getDescription() != null &&
                        (e.getDescription().contains("O-Ring") ||
                                e.getDescription().contains("Cable Gland")))
                .count();
        assertTrue(sealingComponentCount >= 2,
                "Must have adequate sealing components for IP67 rating");
    }

    private void verifyThermalManagement(MechanicalBOM bom) {
        // Verify thermal interface material
        BOMEntry tim = findComponentByType(bom, "Thermal Interface");
        assertNotNull(tim, "Thermal interface material must be present");

        String thermalConductivity = tim.getSpecs().get("thermalConductivity");
        assertNotNull(thermalConductivity, "Thermal conductivity must be specified");

        double conductivity = Double.parseDouble(
                thermalConductivity.replaceAll("[^0-9.]", ""));
        assertTrue(conductivity >= 5.0,
                "Thermal conductivity must be adequate for heat dissipation");

        // Verify enclosure thermal properties
        String enclosureMaterial = bom.getMaterialDetails().get("mainMaterial").toString();
        assertTrue(enclosureMaterial != null && enclosureMaterial.contains("Aluminum"),
                "Enclosure material must provide adequate thermal conductivity");
    }

    private void verifyStructuralIntegrity(MechanicalBOM bom) {
        // Verify thread inserts
        BOMEntry threadInserts = findComponentByType(bom, "Thread Insert");
        assertNotNull(threadInserts, "Thread inserts must be present");

        int insertCount = Integer.parseInt(threadInserts.getQty());
        assertTrue(insertCount >= 8,
                "Must have sufficient thread inserts for structural integrity");

        // Verify mounting provisions
        long mountingComponentCount = bom.getBomEntries().stream()
                .filter(e -> e.getDescription() != null &&
                        e.getDescription().contains("Mounting"))
                .count();
        assertTrue(mountingComponentCount > 0,
                "Must have mounting provisions");
    }

    private void verifyManufacturingRequirements(MechanicalBOM bom) {
        // Verify material specifications
        assertNotNull(bom.getMaterialDetails().get("materialCertification"),
                "Material certification requirements must be specified");

        // Verify surface treatment
        String surfaceTreatment = bom.getManufacturingDetails().get("surfaceTreatment").toString();
        assertNotNull(surfaceTreatment,
                "Surface treatment must be specified");

        // Verify tolerances
        String tolerances = bom.getManufacturingDetails().get("tolerances").toString();
        assertNotNull(tolerances,
                "Manufacturing tolerances must be specified");
    }

    private BOMEntry findComponentByType(BOM bom, String type) {
        return bom.getBomEntries().stream()
                .filter(e -> e.getDescription() != null &&
                        e.getDescription().contains(type))
                .findFirst()
                .orElse(null);
    }
}