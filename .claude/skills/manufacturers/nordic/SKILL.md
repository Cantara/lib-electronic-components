# Nordic Semiconductor Part Number Reference

## MPN Structure Overview

Nordic Semiconductor specializes in wireless SoCs:
- **nRF51**: Legacy BLE SoCs
- **nRF52**: Current BLE/ANT SoCs
- **nRF53**: Dual-core BLE SoCs
- **nRF91**: Cellular IoT SoCs

---

## Part Number Format

```
nRF[Series][Model]-[Package][Variant]
  |    |      |       |        |
  |    |      |       |        +-- Variant code (memory, features)
  |    |      |       +-- Package code (QFAA, CIAA, etc.)
  |    |      +-- Model number (832, 840, 5340, etc.)
  |    +-- Series (51, 52, 53, 91)
  +-- Nordic prefix
```

### Example Decoding

```
nRF52840-QIAA
  |  |   | ||
  |  |   | |+-- Variant (AA)
  |  |   | +-- Package type (I = Industrial temp)
  |  |   +-- Package base (Q = QFN)
  |  +-- Model (52840)
  +-- Nordic nRF prefix
```

---

## Series Overview

### nRF51 Series (Legacy)

| Model | Core | Flash | RAM | Features |
|-------|------|-------|-----|----------|
| nRF51822 | Cortex-M0 | 256KB | 32KB | BLE |
| nRF51824 | Cortex-M0 | 256KB | 32KB | BLE + ANT |

### nRF52 Series (Current)

| Model | Core | Flash | RAM | Features |
|-------|------|-------|-----|----------|
| nRF52810 | Cortex-M4 | 192KB | 24KB | BLE |
| nRF52811 | Cortex-M4 | 192KB | 24KB | BLE + Direction Finding |
| nRF52832 | Cortex-M4F | 512KB | 64KB | BLE + ANT |
| nRF52833 | Cortex-M4F | 512KB | 128KB | BLE + ANT + Zigbee/Thread |
| nRF52840 | Cortex-M4F | 1MB | 256KB | BLE + ANT + Zigbee/Thread + USB |

### nRF53 Series (Dual-Core)

| Model | Cores | Flash | RAM | Features |
|-------|-------|-------|-----|----------|
| nRF5340 | M33 + M33 | 1MB | 512KB | BLE + Direction Finding |

### nRF91 Series (Cellular)

| Model | Core | Flash | RAM | Features |
|-------|------|-------|-----|----------|
| nRF9160 | Cortex-M33 | 1MB | 256KB | LTE-M/NB-IoT + GPS |

---

## Package Codes

| Code | Package | Temperature | Notes |
|------|---------|-------------|-------|
| QFAA | QFN48 6x6mm | -40 to +85C | Consumer |
| QFAB | QFN48 6x6mm | -40 to +85C | Variant B |
| QFAC | QFN48 6x6mm | -40 to +85C | Variant C |
| QIAA | QFN48 7x7mm | -40 to +105C | Industrial |
| CIAA | WLCSP | -40 to +85C | Chip scale |
| CAAA | WLCSP | -40 to +85C | Smaller WLCSP |
| CKAA | QFN73 | -40 to +85C | Large package |

---

## Development Kits

| Kit | SoC | Features |
|-----|-----|----------|
| nRF52840-DK | nRF52840 | Full dev board |
| nRF52833-DK | nRF52833 | Full dev board |
| nRF52-DK | nRF52832 | Full dev board |
| nRF5340-DK | nRF5340 | Dual-core dev board |
| nRF9160-DK | nRF9160 | Cellular dev board |

---

## Supported ComponentTypes

```java
ComponentType.BLUETOOTH_IC_NORDIC
```

Note: Handler registers patterns for `ComponentType.IC` but returns `BLUETOOTH_IC_NORDIC` in getSupportedTypes().

---

## Known Handler Issues

### 1. Pattern/Type Mismatch

Patterns registered for `ComponentType.IC` but getSupportedTypes() returns `BLUETOOTH_IC_NORDIC`.
This may cause detection issues.

### 2. HashSet in getSupportedTypes()

Uses mutable `HashSet` instead of `Set.of()`.

### 3. Case Sensitivity

Patterns start with lowercase "nRF" - may need case-insensitive matching.

---

## Test Patterns

### Valid SoC MPNs

```
nRF52840-QIAA
nRF52832-QFAA
nRF51822-CEAA
nRF5340-QKAA
nRF9160-SIAA
```

### Valid Dev Kit MPNs

```
nRF52840-DK
nRF5340-PDK
```

---

## Related Files

- Handler: `manufacturers/NordicHandler.java`
- Test: `handlers/NordicHandlerTest.java`

<!-- Add new learnings above this line -->
