package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.HiroseHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for HiroseHandler.
 * Tests DF13, DF14, FH12, BM, DF52, DF63, FH19, FH28, and GT17 connector series.
 *
 * Handler patterns:
 * - DF13: DF13-xP/R/S-xxx (1.25mm pitch)
 * - DF14: DF14-xP/R/S-xxx (1.25mm automotive)
 * - FH12: FH12-xS/P-xxx (0.5mm FFC/FPC)
 * - BM: BMxx-xxx (USB connectors)
 * - DF52: DF52-xS/P-xxx (0.8mm pitch)
 * - DF63: DF63-xS/P-xxx (0.5mm board-to-FPC)
 * - FH19: FH19-xS/P-xxx (0.4mm FFC/FPC)
 * - FH28: FH28-xS/P-xxx (0.4mm dual row)
 * - GT17: GT17-xS/P-xxx (0.5mm board-to-board)
 */
class HiroseHandlerTest {

    private static HiroseHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new HiroseHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("DF13 Series Detection (1.25mm pitch)")
    class DF13Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "DF13-2P-DSA",
            "DF13-3P-1V",
            "DF13-4R-H",
            "DF13-5S-C",
            "DF13-10P-V",
            "DF13-20R-DSA"
        })
        void documentDF13Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesHirose = handler.matches(mpn, ComponentType.CONNECTOR_HIROSE, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_HIROSE = " + matchesHirose);
        }
    }

    @Nested
    @DisplayName("DF14 Series Detection (1.25mm automotive)")
    class DF14Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "DF14-2P-H",
            "DF14-3R-V",
            "DF14-4S-C",
            "DF14-5P-DSA",
            "DF14-8R-H"
        })
        void documentDF14Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesHirose = handler.matches(mpn, ComponentType.CONNECTOR_HIROSE, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_HIROSE = " + matchesHirose);
        }
    }

    @Nested
    @DisplayName("FH12 Series Detection (0.5mm FFC/FPC)")
    class FH12Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FH12-6S-SH",
            "FH12-10S-05SH",
            "FH12-20P-SH",
            "FH12-24S-SH",
            "FH12-30P-SV",
            "FH12-40S-05SV"
        })
        void documentFH12Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesHirose = handler.matches(mpn, ComponentType.CONNECTOR_HIROSE, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_HIROSE = " + matchesHirose);
        }
    }

    @Nested
    @DisplayName("BM Series Detection (USB connectors)")
    class BMTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "BM02-SRSSTB",
            "BM04-SRSSTB",
            "BM10-SRSSTB",
            "BM20-19DP04V"
        })
        void documentBMDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesHirose = handler.matches(mpn, ComponentType.CONNECTOR_HIROSE, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_HIROSE = " + matchesHirose);
        }
    }

    @Nested
    @DisplayName("DF52 Series Detection (0.8mm pitch)")
    class DF52Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "DF52-2S-08H",
            "DF52-3P-08C",
            "DF52-5S-08H",
            "DF52-10P-08C"
        })
        void documentDF52Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesHirose = handler.matches(mpn, ComponentType.CONNECTOR_HIROSE, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_HIROSE = " + matchesHirose);
        }
    }

    @Nested
    @DisplayName("DF63 Series Detection (0.5mm board-to-FPC)")
    class DF63Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "DF63-2S-05V",
            "DF63-4P-05V",
            "DF63-8S-05V",
            "DF63-10P-05V"
        })
        void documentDF63Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesHirose = handler.matches(mpn, ComponentType.CONNECTOR_HIROSE, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_HIROSE = " + matchesHirose);
        }
    }

    @Nested
    @DisplayName("FH19 Series Detection (0.4mm FFC/FPC)")
    class FH19Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FH19-20S-04SH",
            "FH19-30S-04SH",
            "FH19-40P-04SH",
            "FH19-50S-04SH"
        })
        void documentFH19Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesHirose = handler.matches(mpn, ComponentType.CONNECTOR_HIROSE, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_HIROSE = " + matchesHirose);
        }
    }

    @Nested
    @DisplayName("FH28 Series Detection (0.4mm dual row)")
    class FH28Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "FH28-20S-04DS",
            "FH28-30S-04DS",
            "FH28-40P-04DS",
            "FH28-60S-04DS"
        })
        void documentFH28Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesHirose = handler.matches(mpn, ComponentType.CONNECTOR_HIROSE, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_HIROSE = " + matchesHirose);
        }
    }

    @Nested
    @DisplayName("GT17 Series Detection (0.5mm board-to-board)")
    class GT17Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "GT17-20S-05H",
            "GT17-30S-05H",
            "GT17-40P-05H",
            "GT17-60S-05H"
        })
        void documentGT17Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesHirose = handler.matches(mpn, ComponentType.CONNECTOR_HIROSE, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_HIROSE = " + matchesHirose);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "DF13-2P-DSA, DSA",
            "DF13-4P-H, H",
            "DF14-3R-V, V",
            "FH12-10S-SH, SH",
            "BM02-TB, TB"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }

        @Test
        void documentPackageCodeExtraction() {
            String[] mpns = {"DF13-2P-DSA", "DF52-5S-08H", "FH19-30S-04SH", "BM10-SRSSTB"};
            for (String mpn : mpns) {
                System.out.println(mpn + " package code = " + handler.extractPackageCode(mpn));
            }
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "DF13-2P-DSA, DF13",
            "DF14-3R-V, DF14",
            "FH12-10S-SH, FH12",
            "BM02-TB, BM",
            "DF52-5S-08H, DF52",
            "DF63-4P-05V, DF63",
            "FH19-30S-04SH, FH19",
            "FH28-40P-04DS, FH28",
            "GT17-20S-05H, GT17"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }
    }

    @Nested
    @DisplayName("Helper Methods")
    class HelperMethodTests {
        @Test
        void shouldGetPitch() {
            assertEquals("1.25", handler.getPitch("DF13-2P-DSA"));
            assertEquals("1.25", handler.getPitch("DF14-3R-V"));
            assertEquals("0.50", handler.getPitch("FH12-10S-SH"));
            assertEquals("2.00", handler.getPitch("BM02-TB"));
            assertEquals("0.80", handler.getPitch("DF52-5S-08H"));
            assertEquals("0.50", handler.getPitch("DF63-4P-05V"));
            assertEquals("0.40", handler.getPitch("FH19-30S-04SH"));
            assertEquals("0.40", handler.getPitch("FH28-40P-04DS"));
            assertEquals("0.50", handler.getPitch("GT17-20S-05H"));
        }

        @Test
        void documentMountingType() {
            // Document mounting type detection - depends on package code extraction
            String[] mpns = {"DF13-2P-DSA", "DF13-2P-SH", "FH12-10S-SH"};
            for (String mpn : mpns) {
                System.out.println(mpn + " mounting type = " + handler.getMountingType(mpn));
            }
        }

        @Test
        void shouldGetRatedCurrent() {
            assertEquals(1.0, handler.getRatedCurrent("DF13-2P-DSA"));
            assertEquals(2.0, handler.getRatedCurrent("DF14-3R-V"));
            assertEquals(0.5, handler.getRatedCurrent("FH12-10S-SH"));
            assertEquals(1.5, handler.getRatedCurrent("BM02-TB"));
        }

        @Test
        void documentShieldedDetection() {
            // Document shielded detection - depends on package code containing DS/DSS
            String[] mpns = {"DF13-2P-DSA", "FH28-40P-04DS", "DF13-4P-H"};
            for (String mpn : mpns) {
                System.out.println(mpn + " is shielded = " + handler.isShielded(mpn));
            }
        }

        @Test
        void shouldGetApplicationType() {
            assertEquals("General Purpose", handler.getApplicationType("DF13-2P-DSA"));
            assertEquals("Automotive", handler.getApplicationType("DF14-3R-V"));
            assertEquals("FFC/FPC", handler.getApplicationType("FH12-10S-SH"));
            assertEquals("FFC/FPC", handler.getApplicationType("FH19-30S-04SH"));
            assertEquals("FFC/FPC", handler.getApplicationType("FH28-40P-04DS"));
            assertEquals("USB", handler.getApplicationType("BM02-TB"));
            assertEquals("Board-to-Board", handler.getApplicationType("GT17-20S-05H"));
        }

        @Test
        void shouldDetermineLockingType() {
            assertTrue(handler.isLockingType("DF13-2P-DSA"));
            assertTrue(handler.isLockingType("DF14-3R-V"));
            assertTrue(handler.isLockingType("DF52-5S-08H"));
            assertTrue(handler.isLockingType("DF63-4P-05V"));
            assertFalse(handler.isLockingType("FH12-10S-SH"));
            assertFalse(handler.isLockingType("BM02-TB"));
        }

        @Test
        void documentContactPlating() {
            // Document contact plating detection - depends on package code ending
            String[] mpns = {"DF13-2P-G", "DF13-2P-T", "DF13-2P-H"};
            for (String mpn : mpns) {
                System.out.println(mpn + " contact plating = " + handler.getContactPlating(mpn));
            }
        }

        @Test
        void documentOrientation() {
            // Document orientation detection - depends on package code containing R
            String[] mpns = {"DF13-4R-C", "DF13-4P-H"};
            for (String mpn : mpns) {
                System.out.println(mpn + " orientation = " + handler.getOrientation(mpn));
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {
        @Test
        void shouldHandleNull() {
            assertFalse(handler.matches(null, ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(null));
            assertEquals("", handler.extractSeries(null));
            assertFalse(handler.isOfficialReplacement(null, "DF13-2P-DSA"));
            assertFalse(handler.isOfficialReplacement("DF13-2P-DSA", null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("DF13-2P-DSA", null, registry));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("DF13-2P-DSA", "DF14-2P-H"));
            assertFalse(handler.isOfficialReplacement("FH12-10S-SH", "FH19-10S-04SH"));
        }

        @Test
        void shouldNotReplaceWithDifferentPinCount() {
            assertFalse(handler.isOfficialReplacement("DF13-2P-DSA", "DF13-4P-DSA"));
            assertFalse(handler.isOfficialReplacement("FH12-10S-SH", "FH12-20S-SH"));
        }

        @Test
        void documentCompatibleMountingTypes() {
            // Same series and pin count with different package codes may be compatible
            boolean isReplacement = handler.isOfficialReplacement("DF13-4P-H", "DF13-4S-C");
            System.out.println("DF13-4P-H can replace DF13-4S-C = " + isReplacement);
        }
    }

    @Nested
    @DisplayName("Supported Types")
    class SupportedTypesTests {
        @Test
        void shouldSupportExpectedTypes() {
            var types = handler.getSupportedTypes();

            assertTrue(types.contains(ComponentType.CONNECTOR),
                    "Should support CONNECTOR");
            assertTrue(types.contains(ComponentType.CONNECTOR_HIROSE),
                    "Should support CONNECTOR_HIROSE");
        }

        @Test
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {
        @Test
        void canInstantiateDirectly() {
            HiroseHandler directHandler = new HiroseHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            // Document behavior
            boolean matches = directHandler.matches("DF13-2P-DSA", ComponentType.CONNECTOR_HIROSE, directRegistry);
            System.out.println("Direct instantiation: DF13-2P-DSA matches = " + matches);
        }

        @Test
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes);
            assertTrue(manufacturerTypes.isEmpty(),
                    "getManufacturerTypes should return empty set");
        }
    }
}
