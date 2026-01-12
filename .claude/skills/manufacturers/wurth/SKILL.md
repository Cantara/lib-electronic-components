# Wurth Elektronik Manufacturer Handler Skill

## Overview
WurthHandler manages Wurth Elektronik components including pin headers, socket headers, and LEDs.

## Supported Component Types
- CONNECTOR
- CONNECTOR_WURTH
- LED
- LED_STANDARD_WURTH
- LED_RGB_WURTH
- LED_SMD_WURTH

## MPN Patterns

### Headers and Connectors
| Prefix | Description |
|--------|-------------|
| 61xxxxxxxxx | Pin headers |
| 62xxxxxxxxx | Socket headers |
| 618xxxxxxxx | Additional connectors |
| 613xxxxxxxx | WR-PHD series |
| 614xxxxxxxx | WR-BHD series |
| 615xxxxxxxx | WR-TBL series |

## MPN Structure
For headers: `6XXXX YY ZZZ V`
- 6XXXX = Series code (5 digits)
- YY = Pin count (2 digits)
- ZZZ = Pitch code (3 digits)
- V = Package/variant code (1 digit)

Example: `61300211121`
- 61300 = Series
- 02 = Pin count
- 112 = Pitch
- 1 = Variant

## Package Code Extraction
Returns last digit (variant code) from header MPN.

## Series Extraction
Returns first 5 digits as series code.

## Replacement Logic
- Same series, pin count, and pitch required
- Different variants may be compatible

## Test Patterns
When testing WurthHandler:
1. Use documentation tests for `matches()` behavior
2. Use assertions for `extractPackageCode()`, `extractSeries()`, null handling
3. Instantiate directly: `new WurthHandler()`
