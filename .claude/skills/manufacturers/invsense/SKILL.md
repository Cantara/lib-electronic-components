# InvenSense Manufacturer Handler Skill

## Overview
InvSenseHandler manages InvenSense (now TDK) motion sensors including 6-axis IMUs, 9-axis IMUs, gyroscopes, accelerometers, and audio/motion processors.

## Supported Component Types
- SENSOR
- ACCELEROMETER
- GYROSCOPE
- MAGNETOMETER

## MPN Patterns

### 6-Axis IMUs (Accelerometer + Gyroscope)
| Prefix | Description |
|--------|-------------|
| ICM-20xxx | ICM Series IMUs |
| MPU-6xxx | MPU-6000 Series IMUs |

### 9-Axis IMUs (Accelerometer + Gyroscope + Magnetometer)
| Prefix | Description |
|--------|-------------|
| ICM-209xx | ICM-209xx Series |
| MPU-9xxx | MPU-9000 Series |

### Gyroscopes
| Prefix | Description |
|--------|-------------|
| ITG-3xxx | ITG Series |
| IVS-4xxx | IVS Series |

### Accelerometers
| Prefix | Description |
|--------|-------------|
| IAM-2xxx | IAM Series |
| IIM-4xxx | IIM Series |

### Audio/Motion Processors
| Prefix | Description |
|--------|-------------|
| ICS-4xxxx | Audio ICs |
| IAC-5xxxx | Audio/Motion ICs |

## Package Codes
| Suffix | Package |
|--------|---------|
| QFN | QFN |
| LGA | LGA |
| BGA | BGA |
| WLCSP | Wafer Level CSP |
| CSP | Chip Scale Package |
| Q | QFN (suffix) |
| L | LGA (suffix) |
| B | BGA (suffix) |
| C | CSP (suffix) |

## Series Extraction
Returns family + model number (e.g., "ICM-20948", "MPU-6050").

## Interface Codes
| Code | Interface |
|------|-----------|
| -I2C, I | I2C |
| -SPI, S | SPI |
| -COMBO, C | I2C + SPI |

## Replacement Logic
- Same series compatible with different packages
- Interface must match (I2C, SPI, COMBO)
- Voltage compatibility (1.8V parts work at higher voltages)
- Known compatible pairs:
  - MPU-6050 / ICM-20600
  - MPU-9250 / ICM-20948
  - ITG-3200 / IVS-4200
  - IAM-2000 / IIM-4000

## Test Patterns
When testing InvSenseHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new InvSenseHandler()`

## Known Handler Issues
*All issues fixed in PR #88*

## Common Part Numbers
| Part Number | Description |
|-------------|-------------|
| MPU-6050 | 6-axis IMU (legacy, popular) |
| ICM-20948 | 9-axis IMU |
| ICM-42688-P | High performance 6-axis IMU |
| ICM-20600 | 6-axis IMU |
| MPU-9250 | 9-axis IMU |
