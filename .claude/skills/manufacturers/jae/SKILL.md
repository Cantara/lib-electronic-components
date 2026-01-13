# JAE Electronics Handler Skill

## Overview

JAE (Japan Aviation Electronics Industry) is a leading manufacturer of high-quality connectors, known for their reliability in aerospace, automotive, and consumer electronics applications.

## When to Use

Use the `/manufacturers/jae` skill when:
- Adding support for new JAE connector series
- Parsing JAE connector MPNs
- Extracting pin count, pitch, or series information from JAE part numbers
- Working with FPC/FFC, USB-C, board-to-board, or automotive connectors

## JAE Product Lines

### FPC/FFC Connectors (FI Series)

| Series | Pitch | Description |
|--------|-------|-------------|
| FI-RE | 0.5mm | Low profile FPC, back-lock |
| FI-X | 0.5mm | Standard FPC, sliding lock |
| FI-W | 0.5mm | High-current FPC (1.0A) |
| FI-E | 0.5mm | Slim FPC |
| FI-S | 1.0mm | 1.0mm pitch FPC |
| FI-J | 1.0mm | Wire-to-board |

### Board-to-Board Connectors (DX Series)

| Series | Pitch | Description |
|--------|-------|-------------|
| DX07 | 0.5mm | USB Type-C connectors |
| DX40 | 0.4mm | High-speed board-to-board |
| DX30 | 0.4mm | Standard board-to-board |

### Automotive Connectors (MX Series)

| Series | Pitch | Description |
|--------|-------|-------------|
| MX34 | 2.2mm | Waterproof, high-current (5A) |
| MX44 | 1.0mm | Miniature automotive |
| MX77 | 2.2mm | Waterproof, general automotive |

### Circular Connectors (IL Series)

| Series | Description |
|--------|-------------|
| IL | Industrial circular connectors |

## MPN Format

### FI-RE Series
```
FI-RE51S-HF-R1500
│  ││ │  │  └── Packaging (R1500 = 1500 pcs reel)
│  ││ │  └───── Option code (HF = halogen-free)
│  ││ └──────── Type (S = standard)
│  │└────────── Pin count (51)
│  └─────────── Series (RE)
└────────────── Family (FI = FPC/FFC)
```

### FI-X Series
```
FI-X30HL-T
│  │ ││ └── Packaging (T = tape)
│  │ │└─── Type modifier (L = lock)
│  │ └──── Type (H = horizontal)
│  └────── Pin count (30)
└───────── Family and series (FI-X)
```

### DX07 Series (USB Type-C)
```
DX07S024JA1R1500
│  │││  │ │└──── Packaging (R1500 = reel)
│  │││  │ └───── Option (1)
│  │││  └─────── Variant (JA)
│  ││└────────── Pin count (024 = 24 pins)
│  │└─────────── Mount type (S = SMT, B = through-board)
│  └──────────── Series (07 = USB-C)
└───────────────  Family (DX)
```

### MX34 Series (Automotive)
```
MX34036NF1
│  │ │  ││└── Option (1)
│  │ │  │└─── Gender (F = female)
│  │ │  └──── Variant (N = natural color)
│  │ └─────── Pin count (036 = 36 pins)
│  └───────── Series (34)
└──────────── Family (MX = automotive)
```

## Handler Implementation

### Key Methods

| Method | Purpose |
|--------|---------|
| `extractPinCount(mpn)` | Extract pin count from MPN |
| `extractSeries(mpn)` | Extract series (FI-RE, DX07, MX34, etc.) |
| `extractPackageCode(mpn)` | Extract package/option code |
| `getPitch(mpn)` | Get connector pitch in mm |
| `getRatedCurrent(mpn)` | Get rated current in Amperes |
| `getApplicationType(mpn)` | Get application type (FPC/FFC, USB Type-C, Automotive, etc.) |
| `isUSBTypeC(mpn)` | Check if USB Type-C connector |
| `isAutomotiveGrade(mpn)` | Check if automotive grade |
| `isWaterproof(mpn)` | Check if waterproof (MX34, MX77) |
| `isFPCConnector(mpn)` | Check if FPC/FFC connector |

### Pattern Registration

```java
// FI series (FPC/FFC)
registry.addPattern(ComponentType.CONNECTOR, "^FI-RE[0-9]+.*");
registry.addPattern(ComponentType.CONNECTOR_JAE, "^FI-RE[0-9]+.*");
registry.addPattern(ComponentType.CONNECTOR, "^FI-X[0-9]+.*");
registry.addPattern(ComponentType.CONNECTOR_JAE, "^FI-X[0-9]+.*");
// ... more FI patterns

// DX series (board-to-board, USB)
registry.addPattern(ComponentType.CONNECTOR, "^DX07[A-Z][0-9]{3}.*");
registry.addPattern(ComponentType.CONNECTOR_JAE, "^DX07[A-Z][0-9]{3}.*");
// ... more DX patterns

// MX series (automotive)
registry.addPattern(ComponentType.CONNECTOR, "^MX34[0-9]{3}.*");
registry.addPattern(ComponentType.CONNECTOR_JAE, "^MX34[0-9]{3}.*");
// ... more MX patterns

// IL series (circular)
registry.addPattern(ComponentType.CONNECTOR, "^IL-[A-Z0-9]+-[0-9]+.*");
registry.addPattern(ComponentType.CONNECTOR_JAE, "^IL-[A-Z0-9]+-[0-9]+.*");
```

## Usage Examples

### Basic Pattern Matching

```java
JAEHandler handler = new JAEHandler();
PatternRegistry registry = new PatternRegistry();
handler.initializePatterns(registry);

// Check if MPN matches
boolean isJAE = handler.matches("FI-RE51S-HF-R1500", ComponentType.CONNECTOR_JAE, registry);
// isJAE = true

// Extract pin count
int pins = handler.extractPinCount("FI-RE51S-HF-R1500");
// pins = 51

// Get application type
String app = handler.getApplicationType("FI-RE51S-HF-R1500");
// app = "FPC/FFC"
```

### USB Type-C Detection

```java
// Check for USB Type-C connectors
boolean isUSBC = handler.isUSBTypeC("DX07S024JA1R1500");
// isUSBC = true

int pins = handler.extractPinCount("DX07S024JA1R1500");
// pins = 24

double current = handler.getRatedCurrent("DX07S024JA1R1500");
// current = 5.0 (USB-C can carry up to 5A)
```

### Automotive Connector Handling

```java
// Check automotive grade and waterproof status
boolean isAuto = handler.isAutomotiveGrade("MX34036NF1");
// isAuto = true

boolean isWP = handler.isWaterproof("MX34036NF1");
// isWP = true

String pitch = handler.getPitch("MX34036NF1");
// pitch = "2.20"
```

## Testing

Run JAE handler tests:
```bash
mvn test -Dtest=JAEHandlerTest
```

## Related Files

| File | Description |
|------|-------------|
| `JAEHandler.java` | Main handler implementation |
| `JAEHandlerTest.java` | Comprehensive test suite |
| `ComponentType.java` | Contains `CONNECTOR_JAE` enum |
| `ComponentManufacturer.java` | Contains `JAE` enum entry |

## Learnings & Quirks

### Pin Count Extraction

- FI series: Pin count follows series code (FI-RE**51**S)
- DX07 series: Pin count is 3 digits after mount type (DX07S**024**JA1)
- MX series: Pin count is 3 digits after series number (MX34**036**NF1)

### Series Detection Order

For FI series, check specific series before generic:
```java
// Specific before generic
if (mpn.startsWith("FI-RE")) return "FI-RE";
if (mpn.startsWith("FI-X")) return "FI-X";
// Don't do: if (mpn.startsWith("FI-")) - too generic
```

### Packaging Codes

- `R1500` - 1500 pieces on reel
- `R3000` - 3000 pieces on reel
- `T` - Tape packaging
- `-HF` - Halogen-free option

### Automotive vs Consumer Grade

- MX34, MX77: Waterproof automotive grade
- MX44: Miniature automotive (not waterproof)
- FI, DX: Consumer/industrial grade

<!-- Add new learnings above this line -->
