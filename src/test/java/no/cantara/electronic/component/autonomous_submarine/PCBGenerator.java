package no.cantara.electronic.component.autonomous_submarine;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.PCBABOM;
import no.cantara.electronic.component.TechnicalAsset;
import no.cantara.electronic.component.advanced.GerberAsset;

import java.util.Map;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Handles PCB and Gerber file generation for submarine components.
 * Manages the creation and configuration of PCB technical assets including
 * Gerber files, manufacturing documentation, and environmental specifications.
 */
public class PCBGenerator {
    private final SubmarineComponentSpecs specs;

    public PCBGenerator(SubmarineComponentSpecs specs) {
        this.specs = specs;
    }

    /**
     * Adds Gerber files and related specifications to a PCBA.
     *
     * @param pcba The PCBA to add Gerber files to
     * @param boardName The name of the board
     * @throws RuntimeException if Gerber file generation fails
     */
    public void addGerberFilesToPCBA(PCBABOM pcba, String boardName) {
        try {
            // Create new GerberAsset instance
            GerberAsset gerberAsset = new GerberAsset(boardName, "1.0");

            // Generate standard layers
            gerberAsset.generateStandardLayers(boardName);

            // Add manufacturing documentation
            addManufacturingDocs(gerberAsset, boardName);

            // Add environmental specifications
            addEnvironmentalMetadata(gerberAsset);

            // Set the GerberAsset as the PCB reference
            pcba.setPcbReference(gerberAsset);

            // Add environmental specs to all PCBA entries
            pcba.getBomEntries().forEach(specs::addEnvironmentalSpecs);

            // Verify the asset was set
            assertNotNull(pcba.getPcbReference(), "PCBReference should be set on PCBA");

        } catch (Exception e) {
            throw new RuntimeException("Failed to add Gerber files to PCBA: " + e.getMessage(), e);
        }
    }

    /**
     * Adds manufacturing documentation to a GerberAsset.
     *
     * @param gerberAsset The GerberAsset to add documentation to
     * @param boardName The name of the board
     */
    private void addManufacturingDocs(GerberAsset gerberAsset, String boardName) {
        gerberAsset.addManufacturingDoc("assembly", boardName + "-Assembly.pdf");
        gerberAsset.addManufacturingDoc("pickandplace", boardName + "-PnP.txt");
        gerberAsset.addManufacturingDoc("bom", boardName + "-BOM.xlsx");
        gerberAsset.addManufacturingDoc("fabrication", boardName + "-FabDrawing.pdf");
        gerberAsset.addManufacturingDoc("stencil", boardName + "-PasteStencil.gbr");
        gerberAsset.addManufacturingDoc("test_specs", boardName + "-TestSpecification.pdf");
        gerberAsset.addManufacturingDoc("dfm", boardName + "-DFMReport.pdf");
    }

    /**
     * Adds environmental metadata to a GerberAsset.
     *
     * @param gerberAsset The GerberAsset to add metadata to
     */
    private void addEnvironmentalMetadata(GerberAsset gerberAsset) {
        Map<String, Object> metadata = gerberAsset.getMetadata();

        // Environmental protection
        metadata.put("sealing", "IP68");
        metadata.put("waterproof", "Yes");
        metadata.put("protection_rating", "IP68");
        metadata.put("environmental_rating", "Submarine Grade");
        metadata.put("conformal_coating", "Type AR");
        metadata.put("pressure_rating", "300m depth");
        metadata.put("max_pressure", "31 bar");
        metadata.put("pressure_tested", "Yes");

        // Manufacturing requirements
        metadata.put("pcb_material", "High-Tg FR4");
        metadata.put("copper_weight", "2oz");
        metadata.put("surface_finish", "ENIG");
        metadata.put("min_trace_width", "0.15mm");
        metadata.put("min_spacing", "0.15mm");
        metadata.put("via_process", "Filled and capped");
        metadata.put("impedance_control", "Yes");

        // Quality requirements
        metadata.put("itar_compliance", "Required");
        metadata.put("inspection_standard", "IPC Class 3");
        metadata.put("testing_requirements", "100% E-Test");
        metadata.put("quality_system", "AS9100D");

        // Thermal specifications
        metadata.put("thermal_management", "Active liquid cooling");
        metadata.put("operating_temperature", "-40C to +85C");
        metadata.put("thermal_cycling", "Tested per IPC-TM-650");
        metadata.put("heat_spreading", "Internal copper planes");
    }

    /**
     * Creates a PCB reference for a given board.
     *
     * @param boardName The name of the board
     * @return A configured TechnicalAsset for the PCB
     */
    public TechnicalAsset createPCBReference(String boardName) {
        TechnicalAsset pcbReference = new TechnicalAsset();
        pcbReference.setName(boardName);
        pcbReference.setVersion("1.0");
        pcbReference.setType(TechnicalAsset.AssetType.PCB_DESIGN);
        pcbReference.setFormat("Gerber");
        pcbReference.setDescription("PCB for " + boardName);

        // Create and set the location
        TechnicalAsset.AssetLocation location = new TechnicalAsset.AssetLocation();
        location.setType(TechnicalAsset.AssetLocation.LocationType.FILE);
        location.setValue("/pcb/gerber/" + boardName);
        pcbReference.setLocation(location);

        // Add metadata
        Map<String, Object> metadata = new HashMap<>();
        addEnvironmentalMetadata(metadata);
        pcbReference.setMetadata(metadata);

        return pcbReference;
    }

    /**
     * Adds environmental metadata to a generic metadata map.
     *
     * @param metadata The metadata map to add to
     */
    private void addEnvironmentalMetadata(Map<String, Object> metadata) {
        metadata.put("sealing", "IP68");
        metadata.put("waterproof", "Yes");
        metadata.put("protection_rating", "IP68");
        metadata.put("environmental_rating", "Submarine Grade");
        metadata.put("conformal_coating", "Type AR");
        metadata.put("pressure_rating", "300m depth");
        metadata.put("max_pressure", "31 bar");
        metadata.put("pressure_tested", "Yes");
    }
}