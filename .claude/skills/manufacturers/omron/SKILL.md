# Omron Electronic Components Skill

Use this skill when working with Omron electronic components - relays, switches, and sensors.

## Product Families

### Relays (G-series)

| Series | Type | Description |
|--------|------|-------------|
| G2R | Power Relay | General purpose, 5A-8A rating |
| G2RL | Slim Power Relay | Space-saving design, 8A rating |
| G5V | Signal Relay | Low power, 1A rating, ultra-miniature |
| G5RL | Slim Power Relay | 16A rating |
| G6K | Signal Relay | Ultra-miniature, 1A/30VDC |
| G6KU | Signal Relay | Ultra-small signal relay |
| G3MC | SSR | PCB mount solid state relay |
| G3NA | SSR | Panel mount SSR with heatsink |
| G3NE | SSR | DIN rail mount SSR |

### Switches

| Series | Type | Description |
|--------|------|-------------|
| B3F | Tact Switch | THT, various forces 1.47-6.37N |
| B3FS | Tact Switch | SMD version of B3F |
| B3U | Tact Switch | Ultra-compact SMD |
| D2F | Microswitch | Standard size |
| D2FC | Microswitch | Compact, popular for mice |
| SS | Slide Switch | Various configurations |

### Sensors

| Series | Type | Description |
|--------|------|-------------|
| EE-S/EE-SX | Optical | Slot/reflective photoelectric |
| EE-SY | Optical | Reflective type |
| D6F | Flow | MEMS air flow sensors |
| E2E | Proximity | Inductive type |
| E2K | Proximity | Capacitive type |

## MPN Structure

### Relay MPN Format
```
G[series]-[contacts]-[voltage]
```
Example: `G5V-1-DC5`
- G5V = Signal relay series
- 1 = SPDT (1 pole)
- DC5 = 5V DC coil

### SSR MPN Format
```
G3[series]-[model]-[control]
```
Example: `G3MC-201P-DC5`
- G3MC = PCB mount SSR series
- 201P = Model variant
- DC5 = 5V DC control

### Switch MPN Format
```
B3[series]-[variant]
```
Example: `B3F-1000`
- B3F = Tact switch series
- 1000 = Force (1=1.47N) / Travel / Options

### Microswitch MPN Format
```
D2F[variant]-[options]
```
Example: `D2FC-F-7N`
- D2FC = Compact microswitch
- F = Low force
- 7N = 7 million cycle model

### Sensor MPN Format
```
EE-S[variant][model]
```
Example: `EE-SX1041`
- EE = Optical sensor family
- S = Slot type
- X = SMD variant
- 1041 = Model number

## Contact Configurations (Relays)

| Code | Configuration |
|------|---------------|
| 1 | SPDT (1 pole) |
| 1A | SPST-NO |
| 2 | DPDT (2 pole) |
| 2A | DPST-NO |

## Coil Voltages

| Code | Voltage |
|------|---------|
| DC3 | 3V DC |
| DC5 | 5V DC |
| DC12 | 12V DC |
| DC24 | 24V DC |
| AC120 | 120V AC |
| AC240 | 240V AC |

## B3F Switch Force Codes

| First Digit | Actuation Force |
|-------------|-----------------|
| 1 | 1.47N (150gf) |
| 2 | 2.55N (260gf) |
| 3 | 3.43N (350gf) |
| 4 | 4.12N (420gf) |
| 5 | 6.37N (650gf) |

## Handler Usage

```java
// Initialize handler
OmronHandler handler = new OmronHandler();
PatternRegistry registry = new PatternRegistry();
handler.initializePatterns(registry);

// Check component type
handler.matches("G5V-1-DC5", ComponentType.RELAY_SIGNAL, registry);  // true
handler.matches("B3F-1000", ComponentType.SWITCH_TACT, registry);     // true
handler.matches("EE-SX1041", ComponentType.SENSOR_OPTICAL, registry); // true

// Extract information
handler.extractSeries("G5V-1-DC5");      // "G5V"
handler.extractPackageCode("G5V-1-DC5"); // "SPDT"
handler.getCoilVoltage("G5V-1-DC5");     // "5V DC"
handler.getFamily("G5V-1-DC5");          // "Signal Relay"

// Switch information
handler.getActuationForce("B3F-1000");   // "1.47N"
handler.extractPackageCode("B3F-1000");  // "THT"
handler.extractPackageCode("B3FS-1000"); // "SMD"
```

## ComponentType Mappings

### Relays
- `ComponentType.RELAY` - All relays
- `ComponentType.RELAY_OMRON` - All Omron relays
- `ComponentType.RELAY_SIGNAL` - G5V, G6K series
- `ComponentType.RELAY_POWER` - G2R, G2RL, G5RL series
- `ComponentType.RELAY_SSR` - G3MC, G3NA, G3NE series

### Switches
- `ComponentType.SWITCH` - All switches
- `ComponentType.SWITCH_OMRON` - All Omron switches
- `ComponentType.SWITCH_TACT` - B3F, B3FS, B3U series
- `ComponentType.SWITCH_MICRO` - D2F, D2FC series
- `ComponentType.SWITCH_SLIDE` - SS series

### Sensors
- `ComponentType.SENSOR` - All sensors
- `ComponentType.SENSOR_OMRON` - All Omron sensors
- `ComponentType.SENSOR_OPTICAL` / `SENSOR_OPTICAL_OMRON` - EE-S series
- `ComponentType.SENSOR_FLOW` / `SENSOR_FLOW_OMRON` - D6F series
- `ComponentType.SENSOR_PROXIMITY` / `SENSOR_PROXIMITY_OMRON` - E2E, E2K series

## Common Part Numbers

### Signal Relays
- `G5V-1-DC5` - 5V SPDT signal relay (most common)
- `G5V-2-DC12` - 12V DPDT signal relay
- `G6K-2-DC5` - 5V ultra-miniature relay

### Power Relays
- `G2R-1-DC12` - 12V 5A general purpose
- `G2R-2-DC24` - 24V DPDT power relay
- `G5RL-1-E-DC12` - 12V slim 16A relay

### SSRs
- `G3MC-201P-DC5` - 5V control, 2A AC output
- `G3NA-210B-DC5-24` - Panel mount, 10A output

### Tact Switches
- `B3F-1000` - Standard THT, 1.47N
- `B3F-1002` - Standard with ground terminal
- `B3FS-1000` - SMD version

### Microswitches
- `D2FC-F-7N` - Popular mouse switch (7M cycles)
- `D2FC-F-7N-20M` - Extended life (20M cycles)
- `D2F-01` - Standard microswitch

### Optical Sensors
- `EE-SX1041` - Slot type, NPN output
- `EE-SX4009-P1` - Subminiature slot

### Flow Sensors
- `D6F-01N1-110` - 0-1 L/min air flow

### Proximity Sensors
- `E2E-X1R5E1` - 1.5mm sensing, M12 connector
- `E2E-X2D1-N` - 2mm sensing, NPN output

## Replacement Guidelines

### Compatible Series (same voltage/configuration required)
- G5V ↔ G6K (signal relays, different sizes)
- G3MC ↔ G3NA (SSRs, different mounting)
- B3F ↔ B3FS (tact switches, THT vs SMD)

### Not Compatible
- Different coil voltages (DC5 ≠ DC12)
- Different contact configurations (SPDT ≠ DPDT)
- Relays and switches (different component types)

## Testing

```bash
# Run Omron handler tests
mvn test -Dtest=OmronHandlerTest

# Run all handler tests
mvn test -Dtest="*HandlerTest"
```

## Learnings & Quirks

### MPN Parsing
- Relay coil voltage is typically the last segment after hyphen (DC5, DC12, DC24)
- Switch force code is the first digit in the variant number
- Microswitch "-F" suffix indicates low force variant

### Pattern Matching
- G-series relays use digit after G (G2R, G5V, G6K)
- G3-series are always SSRs (G3MC, G3NA, G3NE)
- B3-series are always tactile switches
- D2F is microswitch, not to be confused with D6F (flow sensor)

### Handler Notes
- Handler uses explicit pattern matching in `matches()` for performance
- Uses Set.of() for getSupportedTypes() (immutable, no duplicates)
- Voltage extraction handles both DC and AC formats

---

## Related Skills

- `/component` - Base component handling
- `/semiconductor` - For general semiconductor work
- `/similarity-connector` - For connector similarity calculations
