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

### Basic Usage
```java
// Create a BOM entry
BOMEntry component = new BOMEntry();
component.setMpn("LM317T");
component.setDescription("Voltage Regulator");

// Add specifications
component.addSpec("package", "TO-220");
component.addSpec("output_type", "Adjustable");
component.addSpec("voltage_range", "1.2V to 37V");

// Create a PCBA BOM
PCBABOM pcba = new PCBABOM(
    "POWER-01",          // Production number
    "Power Supply",      // Customer name
    "ORDER-123",         // Order number
    Arrays.asList(component)  // Components
);

// Add technical documentation
TechnicalAsset pcbSpec = new TechnicalAsset();
pcbSpec.setName("POWER-01-PCB");
pcbSpec.setType(TechnicalAsset.AssetType.PCB_DESIGN);
pcbSpec.setFormat("Gerber");
pcba.setPcbReference(pcbSpec);
```

As the market changes daily, this will never be 100% - but we believe that it 
should bring value to systems dealing with electronic components.

