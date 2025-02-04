package no.cantara.electronic.component.lib.specs.semiconductor;

import no.cantara.electronic.component.lib.specs.base.SpecUnit;
import no.cantara.electronic.component.lib.specs.base.SpecValue;

/**
 * Integrated Circuit specifications base class
 */
public abstract class ICSpecs extends SemiconductorSpecs {
    protected ICSpecs(String componentType) {
        super(componentType);
        initializeICSpecs();
    }

    private void initializeICSpecs() {
        specs.put("vccMin", new SpecValue<>(0.0, SpecUnit.VOLTS));
        specs.put("vccMax", new SpecValue<>(0.0, SpecUnit.VOLTS));
        specs.put("iccSupply", new SpecValue<>(0.0, SpecUnit.MILLIAMPS));
        specs.put("operatingFreqMax", new SpecValue<>(0.0, SpecUnit.MEGAHERTZ));
        specs.put("pinCount", new SpecValue<>(0, SpecUnit.COUNT));
        specs.put("esd", new SpecValue<>(2000.0, SpecUnit.VOLTS));
        specs.put("latchupProtection", new SpecValue<>(false, SpecUnit.NONE));
    }
}
