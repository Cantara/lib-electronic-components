package no.cantara.electronic.component.advanced;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.MechanicalBOM;
import no.cantara.electronic.component.PCBABOM;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the PlannedProductionBatch using a realistic industrial control system scenario.
 * The product is a modular industrial controller with:
 * - Multiple PCB assemblies (control, I/O, display)
 * - DIN rail mechanical assembly
 * - Various cable assemblies
 * - Complete packaging system
 */
class PlannedProductionBatchTest {

    @Test
    void shouldCreateIndustrialControllerBatch() {
        // Create production batch
        PlannedProductionBatch batch = new PlannedProductionBatch(
                "PB2024-001",        // Batch ID
                "IC-8000",           // Industrial Controller 8000 series
                "R2.1",              // Hardware revision
                50                   // Production quantity
        );
        batch.setPlannedDate(LocalDate.of(2024, 3, 15));
        batch.setStatus(PlannedProductionBatch.BatchStatus.PLANNING);

        // ========== PCB Assemblies ==========

        // Main Control PCB Assembly
        PCBABOM mainControlBom = new PCBABOM();

        // CPU and memory
        BOMEntry processor = new BOMEntry()
                .setMpn("STM32H743ZIT6")
                .setManufacturer("STMicroelectronics")
                .setDescription("ARM Cortex-M7 MCU")
                .setPkg("LQFP144")
                .addSpec("speed", "480MHz")
                .addSpec("flash", "2MB")
                .addSpec("ram", "1MB");
        mainControlBom.getBomEntries().add(processor);

        BOMEntry ram = new BOMEntry()
                .setMpn("IS42S16160J-6TLI")
                .setManufacturer("ISSI")
                .setDescription("SDRAM 256Mbit")
                .setPkg("TSOP-54")
                .addSpec("size", "32MB")
                .addSpec("speed", "166MHz");
        mainControlBom.getBomEntries().add(ram);

        BOMEntry flash = new BOMEntry()
                .setMpn("W25Q256JVEIQ")
                .setManufacturer("Winbond")
                .setDescription("Flash Memory")
                .setPkg("SOIC-8")
                .addSpec("size", "32MB")
                .addSpec("interface", "SPI");
        mainControlBom.getBomEntries().add(flash);

        batch.addPCBA(mainControlBom);

        // I/O Interface PCB Assembly
        PCBABOM ioBom = new PCBABOM();

        BOMEntry ioController = new BOMEntry()
                .setMpn("STM32G474RET6")
                .setManufacturer("STMicroelectronics")
                .setDescription("MCU for I/O control")
                .setPkg("LQFP64")
                .addSpec("speed", "170MHz")
                .addSpec("adc", "12-bit");
        ioBom.getBomEntries().add(ioController);

        BOMEntry rs485Driver = new BOMEntry()
                .setMpn("MAX3485EESA+")
                .setManufacturer("Maxim")
                .setDescription("RS-485 Transceiver")
                .setPkg("SOIC-8")
                .addSpec("speed", "10Mbps")
                .addSpec("protection", "±15kV ESD");
        ioBom.getBomEntries().add(rs485Driver);

        batch.addPCBA(ioBom);

        // Display PCB Assembly
        PCBABOM displayBom = new PCBABOM();

        BOMEntry displayController = new BOMEntry()
                .setMpn("RA8876-FGU")
                .setManufacturer("RAiO")
                .setDescription("LCD Controller")
                .setPkg("LQFP128")
                .addSpec("resolution", "800x480")
                .addSpec("interface", "16-bit");
        displayBom.getBomEntries().add(displayController);

        batch.addPCBA(displayBom);

        // ========== Mechanical Assemblies ==========

        // Main Enclosure Assembly
        MechanicalBOM enclosureBom = new MechanicalBOM();

        BOMEntry baseEnclosure = new BOMEntry()
                .setMpn("ENC-8000-BASE")
                .setManufacturer("MetalWorks")
                .setDescription("DIN Rail Enclosure Base")
                .setPkg("Single")
                .addSpec("material", "Aluminum ADC12")
                .addSpec("finish", "Black anodized")
                .addSpec("size", "160x90x58mm");
        enclosureBom.getBomEntries().add(baseEnclosure);

        BOMEntry topCover = new BOMEntry()
                .setMpn("ENC-8000-TOP")
                .setManufacturer("MetalWorks")
                .setDescription("Enclosure Top Cover")
                .setPkg("Single")
                .addSpec("material", "Aluminum ADC12")
                .addSpec("finish", "Black anodized");
        enclosureBom.getBomEntries().add(topCover);

        batch.addMechanical(enclosureBom);

        // PCB Mounting Assembly
        MechanicalBOM mountingBom = new MechanicalBOM();

        BOMEntry pcbSupports = new BOMEntry()
                .setMpn("MNT-8000-PCB")
                .setManufacturer("MetalWorks")
                .setDescription("PCB Mounting Set")
                .setPkg("Set")
                .addSpec("material", "Brass, nickel plated")
                .addSpec("contents", "Standoffs, screws");
        mountingBom.getBomEntries().add(pcbSupports);

        batch.addMechanical(mountingBom);

        // ========== Cable Assemblies ==========

        // Internal Cable Assembly
        CableBOM internalCableBom = new CableBOM();

        BOMEntry displayCable = new BOMEntry()
                .setMpn("CBL-8000-DSP")
                .setManufacturer("CableCo")
                .setDescription("Display Cable Assembly")
                .setPkg("Assembly")
                .addSpec("length", "120mm")
                .addSpec("type", "FFC 40-pin");
        internalCableBom.getBomEntries().add(displayCable);

        BOMEntry ioCable = new BOMEntry()
                .setMpn("CBL-8000-IO")
                .setManufacturer("CableCo")
                .setDescription("I/O Board Cable")
                .setPkg("Assembly")
                .addSpec("length", "150mm")
                .addSpec("type", "20-pin IDC");
        internalCableBom.getBomEntries().add(ioCable);

        batch.addCable(internalCableBom);

        // ========== Packaging Assemblies ==========

        // Product Packaging
        PackagingBOM packagingBom = new PackagingBOM();

        BOMEntry productBox = new BOMEntry()
                .setMpn("PKG-8000-BOX")
                .setManufacturer("PackagingInc")
                .setDescription("Product Box")
                .setPkg("Single")
                .addSpec("material", "Corrugated B-flute")
                .addSpec("print", "4-color")
                .addSpec("size", "200x150x100mm");
        packagingBom.getBomEntries().add(productBox);

        BOMEntry manual = new BOMEntry()
                .setMpn("DOC-8000-MAN")
                .setManufacturer("PrintCo")
                .setDescription("User Manual")
                .setPkg("Single")
                .addSpec("pages", "64")
                .addSpec("size", "A5")
                .addSpec("languages", "EN, DE, FR");
        packagingBom.getBomEntries().add(manual);

        batch.addPackaging(packagingBom);

        // ========== Verify Production Batch ==========
        verifyBatchProperties(batch);
        verifyPCBAssemblies(batch);
        verifyMechanicalAssemblies(batch);
        verifyCableAssemblies(batch);
        verifyPackagingAssemblies(batch);
    }

    private void verifyBatchProperties(PlannedProductionBatch batch) {
        assertEquals("PB2024-001", batch.getBatchId(), "Batch ID should match");
        assertEquals("IC-8000", batch.getProductId(), "Product ID should match");
        assertEquals("R2.1", batch.getRevision(), "Revision should match");
        assertEquals(50, batch.getQuantity(), "Quantity should match");
        assertEquals(LocalDate.of(2024, 3, 15), batch.getPlannedDate(), "Date should match");
        assertEquals(PlannedProductionBatch.BatchStatus.PLANNING, batch.getStatus(), "Status should match");
    }

    private void verifyPCBAssemblies(PlannedProductionBatch batch) {
        var pcbAssemblies = batch.getPCBAStructure().getAssemblies();
        assertEquals(3, pcbAssemblies.size(), "Should have three PCB assemblies");

        // Verify each PCB has its core components
        for (PCBABOM pcbBom : pcbAssemblies) {
            assertFalse(pcbBom.getBomEntries().isEmpty(), "PCB assembly should have components");
        }
    }

    private void verifyMechanicalAssemblies(PlannedProductionBatch batch) {
        var mechAssemblies = batch.getMechanicalStructure().getAssemblies();
        assertEquals(2, mechAssemblies.size(), "Should have two mechanical assemblies");

        // Verify enclosure parts
        boolean hasEnclosure = mechAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getMpn().startsWith("ENC-8000"));
        assertTrue(hasEnclosure, "Should have enclosure components");
    }

    private void verifyCableAssemblies(PlannedProductionBatch batch) {
        var cableAssemblies = batch.getCableStructure().getAssemblies();
        assertEquals(1, cableAssemblies.size(), "Should have one cable assembly");

        // Verify cable components
        boolean hasInternalCables = cableAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getMpn().startsWith("CBL-8000"));
        assertTrue(hasInternalCables, "Should have internal cables");
    }

    private void verifyPackagingAssemblies(PlannedProductionBatch batch) {
        var packagingAssemblies = batch.getPackagingStructure().getAssemblies();
        assertEquals(1, packagingAssemblies.size(), "Should have one packaging assembly");

        // Verify packaging components
        boolean hasProductBox = packagingAssemblies.stream()
                .flatMap(bom -> bom.getBomEntries().stream())
                .anyMatch(entry -> entry.getMpn().startsWith("PKG-8000"));
        assertTrue(hasProductBox, "Should have product packaging");
    }
}