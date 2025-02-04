package no.cantara.electronic.component.lib.specs;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;
import no.cantara.electronic.component.lib.specs.semiconductor.MOSFETSpecs;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test 2: Demonstrates creating and comparing MOSFET specifications
 */
public class MOSFETSpecsTest {

    @Test
    public void shouldCreateAndCompareCompatibleMOSFETs() {
        // Create two similar MOSFETs
        MOSFETSpecs mosfet1 = createMOSFET("IRF530", 100.0, 20.0, 10.0, 0.16);
        MOSFETSpecs mosfet2 = createMOSFET("IRF530N", 100.0, 20.0, 14.0, 0.14);

        // Compare key parameters
        assertTrue(areCompatible(mosfet1, mosfet2),
                "MOSFETs should be compatible based on voltage and current ratings");

        // Verify specific parameters
        assertTrue(isBetterRdsOn(mosfet2, mosfet1),
                "Second MOSFET should have better RdsOn");
        assertTrue(hasHigherCurrentRating(mosfet2, mosfet1),
                "Second MOSFET should have higher current rating");
    }

    @Test
    public void shouldIdentifyIncompatibleMOSFETs() {
        // Create two different MOSFETs
        MOSFETSpecs highVoltage = createMOSFET("High Voltage", 600.0, 20.0, 5.0, 1.2);
        MOSFETSpecs lowVoltage = createMOSFET("Low Voltage", 30.0, 20.0, 20.0, 0.05);

        assertFalse(areCompatible(highVoltage, lowVoltage),
                "MOSFETs should not be compatible due to different voltage ratings");
    }

    private MOSFETSpecs createMOSFET(String name, double vds, double vgs, double id, double rdson) {
        MOSFETSpecs mosfet = new MOSFETSpecs();
        mosfet.setSpec("name", new SpecValue<>(name, SpecUnit.NONE));
        mosfet.setSpec("vdsMax", new SpecValue<>(vds, SpecUnit.VOLTS));
        mosfet.setSpec("vgsMax", new SpecValue<>(vgs, SpecUnit.VOLTS));
        mosfet.setSpec("idMax", new SpecValue<>(id, SpecUnit.AMPS));
        mosfet.setSpec("rdson", new SpecValue<>(rdson, SpecUnit.OHMS));
        return mosfet;
    }

    private boolean areCompatible(MOSFETSpecs mosfet1, MOSFETSpecs mosfet2) {
        double vds1 = (Double) mosfet1.getSpec("vdsMax").getValue();
        double vds2 = (Double) mosfet2.getSpec("vdsMax").getValue();
        return Math.abs(vds1 - vds2) / Math.max(vds1, vds2) < 0.2; // Within 20%
    }

    private boolean isBetterRdsOn(MOSFETSpecs mosfet1, MOSFETSpecs mosfet2) {
        double rds1 = (Double) mosfet1.getSpec("rdson").getValue();
        double rds2 = (Double) mosfet2.getSpec("rdson").getValue();
        return rds1 < rds2;
    }

    private boolean hasHigherCurrentRating(MOSFETSpecs mosfet1, MOSFETSpecs mosfet2) {
        double id1 = (Double) mosfet1.getSpec("idMax").getValue();
        double id2 = (Double) mosfet2.getSpec("idMax").getValue();
        return id1 > id2;
    }
}