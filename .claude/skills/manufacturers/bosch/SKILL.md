# Bosch Manufacturer Handler Skill

## Overview
BoschHandler manages Bosch Sensortec MEMS sensors including accelerometers, gyroscopes, IMUs, magnetometers, pressure sensors, environmental sensors, and gas sensors.

## Supported Component Types
- SENSOR
- ACCELEROMETER
- ACCELEROMETER_BOSCH
- GYROSCOPE
- GYROSCOPE_BOSCH
- IMU_BOSCH
- MAGNETOMETER
- MAGNETOMETER_BOSCH
- PRESSURE_SENSOR
- PRESSURE_SENSOR_BOSCH
- HUMIDITY_SENSOR
- HUMIDITY_SENSOR_BOSCH
- TEMPERATURE_SENSOR
- TEMPERATURE_SENSOR_BOSCH
- GAS_SENSOR_BOSCH

## MPN Patterns

### Accelerometers
| Prefix | Description |
|--------|-------------|
| BMAx | Digital accelerometers |
| SMBx | Analog accelerometers |

### Gyroscopes
| Prefix | Description |
|--------|-------------|
| BMGx | Digital gyroscopes |

### IMUs (Inertial Measurement Units)
| Prefix | Description |
|--------|-------------|
| BMIx | Accelerometer + Gyroscope |

### Magnetometers
| Prefix | Description |
|--------|-------------|
| BMMx | Digital magnetometers |

### Pressure Sensors
| Prefix | Description |
|--------|-------------|
| BMPx | Barometric pressure sensors |

### Environmental Sensors
| Prefix | Description |
|--------|-------------|
| BMEx | Humidity + Pressure + Temperature |

### Gas Sensors
| Prefix | Description |
|--------|-------------|
| BME68x | Gas sensors |
| BME69x | Advanced gas sensors |

## Package Codes
| Suffix | Package |
|--------|---------|
| FB | LGA |
| FL | Ultra-small LGA |
| MI | Metal lid LGA |
| TR | LGA Tape & Reel |
| SG | SMD |
| WB | WLCSP |
| CP | CSP |

## Series Extraction
Returns base series (e.g., "BMA456", "BMI270", "BME680").

## Temperature Range Codes
| Code | Range |
|------|-------|
| -T | Standard (-40 to +85°C) |
| -H | High temp (-40 to +105°C) |
| -L | Low temp (-40 to +65°C) |
| -A | Automotive (-40 to +125°C) |

## Replacement Logic
- Same series with different packages compatible
- Interface must match (I2C, SPI, ANALOG)
- Temperature range compatibility: Automotive > High > Standard > Low
- Known compatible pairs: BMA456/BMA455, BMI270/BMI160, BMP390/BMP388, BME680/BME688

## Test Patterns
When testing BoschHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new BoschHandler()`

## Known Handler Issues
*All issues fixed in PR #87*

## Common Part Numbers
| Part Number | Description |
|-------------|-------------|
| BME280 | Environmental sensor (humidity, pressure, temp) |
| BME680 | Gas sensor with environmental sensing |
| BMI270 | 6-axis IMU |
| BMP388 | Barometric pressure sensor |
| BMA456 | 3-axis accelerometer |
