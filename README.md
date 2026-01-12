![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/Cantara/lib-electronic-components) ![Build Status](https://jenkins.cantara.no/buildStatus/icon?job=/Cantara%20lib-electronic-components) ![GitHub commit activity](https://img.shields.io/github/commit-activity/y/Cantara/lib-electronic-components)   [![Project Status: Active – The project has reached a stable, usable state and is being actively developed.](http://www.repostatus.org/badges/latest/active.svg)](http://www.repostatus.org/#active) 
[![Known Vulnerabilities](https://snyk.io/test/github/Cantara/lib-electronic-components/badge.svg)](https://snyk.io/test/github/Cantara/lib-electronic-components)


# lib-electronic-components

A start of a library to make it a bit more meaningful to work digitally with electronic components.

## Features

- **Component Validation**: Verify specifications and requirements
- **Standardization**: Normalize component data across manufacturers
- **Alternative Finding**: Identify compatible component alternatives
- **Similarity Analysis**: Find similar components based on specifications
- **Categorization**: Organize components by type and characteristics
- **Type Detection**: Automatic component classification
- **Manufacturer Data**: Standard component data structures

### Installation

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>no.cantara.electronic</groupId>
    <artifactId>lib-electronic-components</artifactId>
    <version>[latest version]</version>
</dependency>
```

## Using the Electronics BOM System

### Core Classes
#### BOMEntry
Basic building block for components:
```java
// Creating a basic component
BOMEntry entry = new BOMEntry();
entry.setMpn("LTC7132");  // Manufacturer Part Number
entry.setDescription("Power Distribution Controller");
entry.addSpec("power_type", "Marine grade");
entry.addSpec("redundant_power", "yes");
```

#### PCBABOM & MechanicalBOM
Assemblies for electronic and mechanical components:
```java
// Creating a PCBA assembly
List<BOMEntry> pcbaEntries = new ArrayList<>();
// ... add entries ...
PCBABOM pcba = new PCBABOM("PWR-MAIN-01", "Power Management Board", "R1.0", pcbaEntries);

// Creating a mechanical assembly
List<BOMEntry> mechEntries = new ArrayList<>();
// ... add entries ...
MechanicalBOM mech = new MechanicalBOM();
mech.setBomEntries(mechEntries);
mech.setProductionNo("PWR-MECH-01");
```

#### PlannedProductionBatch
Container for complete systems:
```java
// Creating a production batch
PlannedProductionBatch batch = new PlannedProductionBatch(
    "BATCH-2024-001",  // Batch ID
    "SUBMARINE-V1",    // Product ID
    "R1.0",           // Revision
    1                 // Quantity
);

// Adding assemblies
batch.addPCBA(powerBoard);
batch.addMechanical(powerMechanicals);
```

#### Example: Complete System Creation
See full example as JUnit test
```java
// Create entries with required specs
BOMEntry powerIC = new BOMEntry()
    .setMpn("BQ40Z80RSMR")
    .setDescription("Battery Management IC")
    .addSpec("power_type", "Marine grade")
    .addSpec("redundant_power", "yes")
    .addSpec("waterproof", "yes")
    .addSpec("protection_rating", "IP68");

// Create PCBA with entries
List<BOMEntry> entries = new ArrayList<>();
entries.add(powerIC);
PCBABOM powerBoard = new PCBABOM("PWR-01", "Power Board", "R1.0", entries);

// Create production batch
PlannedProductionBatch batch = new PlannedProductionBatch(
    "BATCH-2024-001",
    "SUBMARINE-V1",
    "R1.0",
    1
);
batch.addPCBA(powerBoard);

// Validate the batch
SubmarineSystemValidator validator = new SubmarineSystemValidator();
validator.validate(batch);
if (!validator.getValidationErrors().isEmpty()) {
    // Handle validation errors
    validator.getValidationErrors().forEach(System.out::println);
}
```


### MPNUtils
MPNUtils is a utility class for handling Manufacturer Part Numbers (MPNs) in electronic component management. It provides functionality for normalization, type detection, similarity comparison, and manufacturer identification.

#### Key Features
- MPN normalization
- Component type detection
- Similarity calculation between MPNs
- Manufacturer detection
- MOSFET and IC type validation

#### Usage Examples
```java
// Normalize MPNs by removing special characters and converting to uppercase
String normalized1 = MPNUtils.normalize("LM358-N");     // Returns "LM358N"
String normalized2 = MPNUtils.normalize("2N2222A/TO");  // Returns "2N2222ATO"
String normalized3 = MPNUtils.normalize("GRM188R71H104KA93D"); // Returns "GRM188R71H104KA93D"

// Parse and standardize MPNs
String standardizedMpn = MPNUtils.standardize("LM317-T");  // Returns "LM317T"
boolean isValid = MPNUtils.isValidMpn("LM317T");          // Basic MPN validation

// Check if an MPN matches a specific component type
boolean isOpAmp = MPNUtils.matchesType("LM358N", ComponentType.OPAMP);  // true
boolean isResistor = MPNUtils.matchesType("RC0603FR-0710KL", ComponentType.RESISTOR);  // true
boolean isMosfet = MPNUtils.matchesType("IRF530N", ComponentType.MOSFET);  // true

// Compare similar components
double similarity;
// Op-amps from different manufacturers
similarity = MPNUtils.calculateSimilarity("LM358", "MC1458");  // 0.9 (equivalent parts)

// MOSFETs
similarity = MPNUtils.calculateSimilarity("IRF530", "IRF530N");  // 0.9 (same family)
similarity = MPNUtils.calculateSimilarity("IRF530", "IRF540");   // 0.3 (different ratings)

// Resistors
similarity = MPNUtils.calculateSimilarity(
    "CRCW060310K0FKEA",  // Vishay 10kΩ 0603
            "RC0603FR-0710KL"    // Yageo 10kΩ 0603
);  // 0.8 (equivalent parts)

// Capacitors
similarity = MPNUtils.calculateSimilarity(
    "GRM188R71H104KA93D",  // Murata 0.1µF 50V 0603
            "C0603C104K5RACTU"     // KEMET 0.1µF 50V 0603
);  // 0.9 (equivalent parts)

// Identify manufacturer from MPN
ComponentManufacturer mfr1 = MPNUtils.getManufacturer("STM32F103C8T6");  // STMicroelectronics
ComponentManufacturer mfr2 = MPNUtils.getManufacturer("ATMEGA328P-PU");  // Atmel
ComponentManufacturer mfr3 = MPNUtils.getManufacturer("GRM188R71H104KA93D");  // Murata

// Check if MPN is from specific manufacturer
boolean isSTMicro = MPNUtils.isFromManufacturer("STM32F103", ComponentManufacturer.ST);  // true
boolean isTI = MPNUtils.isFromManufacturer("LM358", ComponentManufacturer.TI);  // true

// Compare MPNs
boolean areEqual = MPNUtils.compareMpn("LM317-T", "LM317T");  // True
String normalized = MPNUtils.normalizeMpn("LM317-T");         // Removes special chars

// MOSFET validation
boolean isMosfet1 = MPNUtils.isMosfet("IRF530N");  // true
boolean isMosfet2 = MPNUtils.isMosfet("2N2222");   // false

// Analog IC detection
boolean isAnalog1 = MPNUtils.isAnalogIC("LM358");   // true (op-amp)
boolean isAnalog2 = MPNUtils.isAnalogIC("74HC00");  // false (digital IC)

// Digital IC detection
boolean isDigital1 = MPNUtils.isDigitalIC("74LS00");  // true
boolean isDigital2 = MPNUtils.isDigitalIC("LM317");   // false
```


### ComponentType
```java
ComponentType type = ComponentType.INTEGRATED_CIRCUIT;
ComponentType.Category category = type.getCategory();  // ACTIVE

// Component classification
boolean isActive = type.isActiveComponent();     // True
boolean isPassive = type.isPassiveComponent();   // False
boolean isDiscrete = type.isDiscreteComponent(); // False

// Grouping and categorization
List<ComponentType> activeTypes = ComponentType.getTypesByCategory(Category.ACTIVE);
```
### ComponentTypeDetector
```java
 
ComponentTypeDetector detector = new ComponentTypeDetector();

// Detect component types
boolean isProcessor = detector.isProcessorComponent(component);
boolean isPower = detector.isPowerComponent(component);
boolean isSensor = detector.isSensorComponent(component);

// Get detailed classification
ComponentType detectedType = detector.detectType(component);
String category = detector.getComponentCategory(component);
```

### ElectronicComponentManufacturer
Represents and manages manufacturer information:
```java
ElectronicComponentManufacturer manufacturer = new ElectronicComponentManufacturer();
manufacturer.setName("Texas Instruments");
manufacturer.setPrefix("TI");
manufacturer.setMpnPattern("^(LM|TPS|TMS|CC)\\d+.*");

// Validate manufacturer-specific MPNs
boolean isValidMpn = manufacturer.isValidMpn("TPS65281");

// Get standardized manufacturer names
String stdName = manufacturer.getStandardizedName();
```




## Manufacturer Coverage

The library supports 55+ manufacturers with dedicated handlers for MPN parsing, type detection, and component matching:

| Category | Manufacturers |
|----------|---------------|
| **MCUs & Processors** | TI, ST, Microchip, Atmel, NXP, Renesas, Silicon Labs, Espressif, Nordic, Cypress, Infineon |
| **Analog & Discrete** | Analog Devices, Maxim, ON Semi, Toshiba, ROHM, Broadcom |
| **Memory** | Micron, Winbond, ISSI |
| **Passives** | Murata, KEMET, Samsung, TDK, Panasonic, AVX, Nichicon, Vishay, Yageo, Bourns |
| **Connectors** | TE Connectivity, Amphenol, Wurth, Molex, Hirose, JST |
| **RF** | Qorvo, Skyworks |
| **Sensors** | Bosch, InvenSense, Melexis |

Each handler provides:
- MPN pattern matching and validation
- Package code extraction
- Series identification
- Replacement/equivalent detection

As the market changes daily, this will never be 100% - but we believe that it
should bring value to systems dealing with electronic components.

