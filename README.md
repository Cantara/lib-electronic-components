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
Utility class for handling Manufacturing Part Numbers (MPNs):
```java
// Parse and standardize MPNs
String standardizedMpn = MPNUtils.standardize("LM317-T");  // Returns "LM317T"
boolean isValid = MPNUtils.isValidMpn("LM317T");          // Basic MPN validation

// Compare MPNs
boolean areEqual = MPNUtils.compareMpn("LM317-T", "LM317T");  // True
String normalized = MPNUtils.normalizeMpn("LM317-T");         // Removes special chars
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




As the market changes daily, this will never be 100% - but we believe that it 
should bring value to systems dealing with electronic components.

