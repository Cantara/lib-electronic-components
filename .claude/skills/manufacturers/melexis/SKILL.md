# Melexis Manufacturer Handler Skill

## Overview
MelexisHandler manages Melexis automotive and industrial sensors including Hall effect sensors, temperature sensors, current sensors, motor position sensors, and optical sensors.

## Supported Component Types
- SENSOR
- MAGNETOMETER
- TEMPERATURE_SENSOR

## MPN Patterns

### Hall Effect Sensors
| Prefix | Description |
|--------|-------------|
| MLX90242 | Hall switches |
| MLX90248 | Hall latches |
| MLX90251 | Hall current sensors |
| MLX90288 | Programmable Hall sensors |
| MLX90293 | 3D Hall sensors |
| MLX9036x | Triaxis position sensors |
| MLX9037x | 3D position sensors |
| MLX9038x | Position sensors |

### Temperature Sensors
| Prefix | Description |
|--------|-------------|
| MLX90614 | IR temperature sensors |
| MLX90632 | Medical IR sensors |
| MLX90640 | IR array sensors (32x24) |
| MLX90641 | IR array sensors (16x12) |

### Current Sensors
| Prefix | Description |
|--------|-------------|
| MLX9120x | Current sensors |

### Motor Position Sensors
| Prefix | Description |
|--------|-------------|
| MLX90380 | Motor position sensors |
| MLX90385 | Motor position sensors |
| MLX90367 | Motor position sensors |

### Optical Sensors
| Prefix | Description |
|--------|-------------|
| MLX7530x | Light sensors |

## Package Codes
| Suffix | Package |
|--------|---------|
| AAA | TO-92UA (unibody) |
| BAA | TO-92S (standard) |
| ESF | SOIC-8 |
| DCB, DCA | TO-220 |
| LUA | QFN-16 (4x4) |
| LXS | QFN-24 (4x4) |
| LQV | TQFP-44 |
| TUA, TUB | TO-18 |

## Series Extraction
Returns first 8 characters (e.g., "MLX90614", "MLX90293").

## Interface Codes
| Code | Interface |
|------|-----------|
| -I, I | I2C |
| -P, P | PWM |
| -A, A | Analog |
| -S, S | SPI |

## Temperature Sensor FOV Codes
| Code | Field of View |
|------|---------------|
| 5D | 5 degrees |
| 10D | 10 degrees |
| 35D | 35 degrees |
| 90D | 90 degrees |
| 100D | 100 degrees |

## Hall Sensor Sensitivity Codes
| Code | Sensitivity |
|------|-------------|
| HC | High |
| MC | Medium |
| LC | Low |
| ULC | Ultra-low |

## Replacement Logic
- Must be same base series
- Interface must match
- For temperature sensors: FOV must match
- For Hall sensors: Sensitivity must match
- Higher resolution can replace lower

## Test Patterns
When testing MelexisHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new MelexisHandler()`

## Known Handler Issues
*All issues fixed in PR #87*
- Package code extraction regex removes all alphanumerics, always returns empty

## Common Part Numbers
| Part Number | Description |
|-------------|-------------|
| MLX90614ESF-BCC | IR temperature sensor, SOIC-8 |
| MLX90640ESF-BAB | IR array sensor, 32x24 |
| MLX90393SLW | 3-axis magnetometer |
| MLX91208KDC | Current sensor |
