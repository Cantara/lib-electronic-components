package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.MillMaxHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for MillMaxHandler.
 * Tests Mill-Max precision pins, sockets, and connectors.
 *
 * Handler patterns:
 * - Spring-loaded pins: 0906, 0907, 0908 series (pogo pins)
 * - IC sockets: 110, 510, 610 series
 * - Board-to-board: 850, 851, 852 series
 * - Headers: 300, 310, 311, 315, 316 series
 * - Receptacles: 800, 801 series
 *
 * MPN Format: SERIES-SPEC1-SPEC2-SPEC3-...
 * Example: 0906-0-15-20-75-14-11-0 (spring pin)
 *          310-93-108-41-001000 (header)
 */
class MillMaxHandlerTest {

    private static MillMaxHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new MillMaxHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Spring-Loaded Pin Detection (Pogo Pins)")
    class SpringPinTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "0906-0-15-20-75-14-11-0",
            "0906-1-15-20-75-14-11-0",
            "0907-0-15-20-75-14-11-0",
            "0907-2-20-25-80-16-12-1",
            "0908-0-15-20-75-14-11-0",
            "0908-1-18-22-78-15-10-0"
        })
        void shouldDetectSpringPinsAsConnector(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldIdentifySpringLoadedPins() {
            assertTrue(handler.isSpringLoaded("0906-0-15-20-75-14-11-0"));
            assertTrue(handler.isSpringLoaded("0907-0-15-20-75-14-11-0"));
            assertTrue(handler.isSpringLoaded("0908-0-15-20-75-14-11-0"));
            assertFalse(handler.isSpringLoaded("310-93-108-41-001000"));
        }

        @Test
        void springPinsShouldHaveCorrectMountingType() {
            assertEquals("THT", handler.getMountingType("0906-0-15-20-75-14-11-0"));
            assertEquals("SMT", handler.getMountingType("0907-0-15-20-75-14-11-0"));
            assertEquals("THT", handler.getMountingType("0908-0-15-20-75-14-11-0"));
        }
    }

    @Nested
    @DisplayName("IC Socket Detection")
    class ICSocketTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "110-93-308-41-001000",
            "110-87-316-41-001000",
            "510-93-024-17-001000",
            "510-87-044-17-001000",
            "610-93-168-41-001000",
            "610-87-132-41-001000"
        })
        void shouldDetectICSocketsAsConnector(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "110-93-308-41-001000",
            "510-93-024-17-001000",
            "610-93-168-41-001000"
        })
        void shouldDetectICSocketsAsIC(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.IC, registry),
                    mpn + " should match IC");
        }

        @Test
        void shouldIdentifyICSockets() {
            assertTrue(handler.isICSocket("110-93-308-41-001000"));
            assertTrue(handler.isICSocket("510-93-024-17-001000"));
            assertTrue(handler.isICSocket("610-93-168-41-001000"));
            assertFalse(handler.isICSocket("0906-0-15-20-75-14-11-0"));
        }

        @Test
        void icSocketsShouldHaveCorrectApplicationType() {
            assertEquals("DIP IC Socket", handler.getApplicationType("110-93-308-41-001000"));
            assertEquals("PLCC Socket", handler.getApplicationType("510-93-024-17-001000"));
            assertEquals("PGA Socket", handler.getApplicationType("610-93-168-41-001000"));
        }
    }

    @Nested
    @DisplayName("Board-to-Board Connector Detection")
    class BoardToBoardTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "850-10-050-20-001000",
            "850-22-100-30-002000",
            "851-10-050-20-001000",
            "851-22-064-30-001000",
            "852-10-050-20-001000",
            "852-22-100-30-002000"
        })
        void shouldDetectBoardToBoardAsConnector(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void shouldIdentifyBoardToBoardConnectors() {
            assertTrue(handler.isBoardToBoard("850-10-050-20-001000"));
            assertTrue(handler.isBoardToBoard("851-10-050-20-001000"));
            assertTrue(handler.isBoardToBoard("852-10-050-20-001000"));
            assertFalse(handler.isBoardToBoard("310-93-108-41-001000"));
        }

        @Test
        void boardToBoardShouldHaveCorrectFamily() {
            assertEquals("Board-to-Board", handler.getFamily("850-10-050-20-001000"));
            assertEquals("Board-to-Board", handler.getFamily("851-10-050-20-001000"));
            assertEquals("Board-to-Board", handler.getFamily("852-10-050-20-001000"));
        }
    }

    @Nested
    @DisplayName("Header Detection")
    class HeaderTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "300-93-108-41-001000",
            "300-47-164-41-001000",
            "310-93-108-41-001000",
            "310-87-120-41-001000",
            "311-93-108-41-001000",
            "315-93-108-41-001000",
            "316-93-108-41-001000"
        })
        void shouldDetectHeadersAsConnector(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void headersShouldHaveCorrectFamily() {
            assertEquals("Header", handler.getFamily("300-93-108-41-001000"));
            assertEquals("Header", handler.getFamily("310-93-108-41-001000"));
        }

        @Test
        void headersShouldHaveCorrectMountingType() {
            assertEquals("THT", handler.getMountingType("300-93-108-41-001000"));
            assertEquals("THT", handler.getMountingType("310-93-108-41-001000"));
            assertEquals("SMT", handler.getMountingType("311-93-108-41-001000"));
            assertEquals("SMT", handler.getMountingType("315-93-108-41-001000"));
            assertEquals("SMT", handler.getMountingType("316-93-108-41-001000"));
        }
    }

    @Nested
    @DisplayName("Receptacle Detection")
    class ReceptacleTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "800-93-108-41-001000",
            "800-47-164-41-001000",
            "801-93-108-41-001000",
            "801-87-120-41-001000"
        })
        void shouldDetectReceptaclesAsConnector(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }

        @Test
        void receptaclesShouldHaveCorrectFamily() {
            assertEquals("Receptacle", handler.getFamily("800-93-108-41-001000"));
            assertEquals("Receptacle", handler.getFamily("801-93-108-41-001000"));
        }

        @Test
        void receptaclesShouldHaveCorrectMountingType() {
            assertEquals("THT", handler.getMountingType("800-93-108-41-001000"));
            assertEquals("SMT", handler.getMountingType("801-93-108-41-001000"));
        }
    }

    @Nested
    @DisplayName("Single Row Socket Detection")
    class SingleRowSocketTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "350-10-108-00-001000",
            "350-22-164-00-001000"
        })
        void shouldDetectSingleRowSocketsAsConnector(String mpn) {
            assertTrue(handler.matches(mpn, ComponentType.CONNECTOR, registry),
                    mpn + " should match CONNECTOR");
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "0906-0-15-20-75-14-11-0, 0906",
            "0907-0-15-20-75-14-11-0, 0907",
            "0908-0-15-20-75-14-11-0, 0908",
            "110-93-308-41-001000, 110",
            "510-93-024-17-001000, 510",
            "610-93-168-41-001000, 610",
            "850-10-050-20-001000, 850",
            "851-10-050-20-001000, 851",
            "852-10-050-20-001000, 852",
            "300-93-108-41-001000, 300",
            "310-93-108-41-001000, 310",
            "800-93-108-41-001000, 800",
            "801-93-108-41-001000, 801",
            "350-10-108-00-001000, 350"
        })
        void shouldExtractSeries(String mpn, String expected) {
            assertEquals(expected, handler.extractSeries(mpn));
        }

        @Test
        void shouldHandleUnknownSeries() {
            // Should still extract numeric series even if not in known families
            assertEquals("999", handler.extractSeries("999-12-34-56-789"));
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "0906-0-15-20-75-14-11-0, THT",
            "0907-0-15-20-75-14-11-0, SMT",
            "110-93-308-41-001000, THT",
            "510-93-024-17-001000, SMT",
            "310-93-108-41-001000, THT",
            "311-93-108-41-001000, SMT"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }
    }

    @Nested
    @DisplayName("Pin Count Extraction")
    class PinCountTests {
        @Test
        void shouldExtractPinCount() {
            assertEquals(108, handler.extractPinCount("310-93-108-41-001000"));
            assertEquals(164, handler.extractPinCount("300-47-164-41-001000"));
            assertEquals(50, handler.extractPinCount("850-10-050-20-001000"));
        }
    }

    @Nested
    @DisplayName("Family Detection")
    class FamilyTests {
        @ParameterizedTest
        @CsvSource({
            "0906-0-15-20-75-14-11-0, Spring-Loaded Contact",
            "0907-0-15-20-75-14-11-0, Spring-Loaded Contact",
            "0908-0-15-20-75-14-11-0, Spring-Loaded Contact",
            "110-93-308-41-001000, IC Socket",
            "510-93-024-17-001000, IC Socket",
            "610-93-168-41-001000, IC Socket",
            "850-10-050-20-001000, Board-to-Board",
            "851-10-050-20-001000, Board-to-Board",
            "852-10-050-20-001000, Board-to-Board",
            "300-93-108-41-001000, Header",
            "310-93-108-41-001000, Header",
            "800-93-108-41-001000, Receptacle",
            "801-93-108-41-001000, Receptacle",
            "350-10-108-00-001000, Single Row Socket"
        })
        void shouldIdentifyFamily(String mpn, String expected) {
            assertEquals(expected, handler.getFamily(mpn));
        }

        @Test
        void shouldReturnUnknownForUnknownSeries() {
            assertEquals("Unknown", handler.getFamily("999-12-34-56-789"));
        }
    }

    @Nested
    @DisplayName("Mounting Type Helpers")
    class MountingTypeTests {
        @Test
        void shouldDetectSurfaceMount() {
            assertTrue(handler.isSurfaceMount("0907-0-15-20-75-14-11-0"));
            assertTrue(handler.isSurfaceMount("510-93-024-17-001000"));
            assertTrue(handler.isSurfaceMount("851-10-050-20-001000"));
            assertTrue(handler.isSurfaceMount("311-93-108-41-001000"));
            assertTrue(handler.isSurfaceMount("801-93-108-41-001000"));

            assertFalse(handler.isSurfaceMount("0906-0-15-20-75-14-11-0"));
            assertFalse(handler.isSurfaceMount("110-93-308-41-001000"));
        }

        @Test
        void shouldDetectThroughHole() {
            assertTrue(handler.isThroughHole("0906-0-15-20-75-14-11-0"));
            assertTrue(handler.isThroughHole("110-93-308-41-001000"));
            assertTrue(handler.isThroughHole("850-10-050-20-001000"));
            assertTrue(handler.isThroughHole("300-93-108-41-001000"));
            assertTrue(handler.isThroughHole("800-93-108-41-001000"));

            assertFalse(handler.isThroughHole("0907-0-15-20-75-14-11-0"));
            assertFalse(handler.isThroughHole("510-93-024-17-001000"));
        }
    }

    @Nested
    @DisplayName("Application Type")
    class ApplicationTypeTests {
        @ParameterizedTest
        @CsvSource({
            "0906-0-15-20-75-14-11-0, Test/Programming",
            "0907-0-15-20-75-14-11-0, Test/Programming",
            "0908-0-15-20-75-14-11-0, Test/Programming",
            "110-93-308-41-001000, DIP IC Socket",
            "510-93-024-17-001000, PLCC Socket",
            "610-93-168-41-001000, PGA Socket",
            "850-10-050-20-001000, High Density B2B",
            "851-10-050-20-001000, High Density B2B SMT",
            "300-93-108-41-001000, Pin Header",
            "310-93-108-41-001000, Pin Header",
            "800-93-108-41-001000, Pin Receptacle",
            "801-93-108-41-001000, Pin Receptacle SMT"
        })
        void shouldIdentifyApplicationType(String mpn, String expected) {
            assertEquals(expected, handler.getApplicationType(mpn));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossFamilies() {
            assertFalse(handler.isOfficialReplacement("0906-0-15-20-75-14-11-0", "310-93-108-41-001000"));
            assertFalse(handler.isOfficialReplacement("110-93-308-41-001000", "850-10-050-20-001000"));
        }

        @Test
        void shouldNotReplaceAcrossSeries() {
            assertFalse(handler.isOfficialReplacement("0906-0-15-20-75-14-11-0", "0907-0-15-20-75-14-11-0"));
            assertFalse(handler.isOfficialReplacement("110-93-308-41-001000", "510-93-308-41-001000"));
        }

        @Test
        void shouldHandleNullInReplacement() {
            assertFalse(handler.isOfficialReplacement(null, "0906-0-15-20-75-14-11-0"));
            assertFalse(handler.isOfficialReplacement("0906-0-15-20-75-14-11-0", null));
            assertFalse(handler.isOfficialReplacement(null, null));
        }

        @Test
        void compatibleSpringPinsShouldBeReplacements() {
            // Same series with same travel/force specs should be compatible
            // Note: This depends on the MPN encoding specifics
            assertTrue(handler.isOfficialReplacement(
                    "0906-0-15-20-75-14-11-0",
                    "0906-0-15-20-75-14-11-1"));
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
            assertEquals(0, handler.extractPinCount(null));
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
            assertEquals(0, handler.extractPinCount(""));
        }

        @Test
        void shouldHandleNullComponentType() {
            assertFalse(handler.matches("0906-0-15-20-75-14-11-0", null, registry));
        }

        @Test
        void shouldHandleInvalidMPN() {
            assertFalse(handler.matches("INVALID", ComponentType.CONNECTOR, registry));
            assertFalse(handler.matches("ABC-123", ComponentType.CONNECTOR, registry));
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
            assertTrue(types.contains(ComponentType.IC),
                    "Should support IC");
        }

        @Test
        void shouldNotHaveDuplicates() {
            var types = handler.getSupportedTypes();
            assertEquals(types.size(), types.stream().distinct().count(),
                    "Should have no duplicate types");
        }

        @Test
        void shouldUseImmutableSet() {
            var types = handler.getSupportedTypes();
            assertThrows(UnsupportedOperationException.class, () -> {
                types.add(ComponentType.RESISTOR);
            });
        }
    }

    @Nested
    @DisplayName("Handler Initialization")
    class HandlerInitializationTests {
        @Test
        void canInstantiateDirectly() {
            MillMaxHandler directHandler = new MillMaxHandler();
            assertNotNull(directHandler);

            PatternRegistry directRegistry = new PatternRegistry();
            directHandler.initializePatterns(directRegistry);

            // Verify basic functionality
            assertTrue(directHandler.matches("0906-0-15-20-75-14-11-0", ComponentType.CONNECTOR, directRegistry));
        }

        @Test
        void getManufacturerTypesReturnsEmptySet() {
            var manufacturerTypes = handler.getManufacturerTypes();
            assertNotNull(manufacturerTypes);
            assertTrue(manufacturerTypes.isEmpty(),
                    "getManufacturerTypes should return empty set");
        }
    }

    @Nested
    @DisplayName("Documentation Tests")
    class DocumentationTests {
        @Test
        void documentSpringPinSeries() {
            System.out.println("=== Mill-Max Spring-Loaded Pin Series ===");
            String[] springPins = {
                "0906-0-15-20-75-14-11-0",
                "0907-0-15-20-75-14-11-0",
                "0908-0-15-20-75-14-11-0"
            };
            for (String mpn : springPins) {
                System.out.println(mpn + ":");
                System.out.println("  Series: " + handler.extractSeries(mpn));
                System.out.println("  Family: " + handler.getFamily(mpn));
                System.out.println("  Mounting: " + handler.getMountingType(mpn));
                System.out.println("  Application: " + handler.getApplicationType(mpn));
            }
        }

        @Test
        void documentICSocketSeries() {
            System.out.println("=== Mill-Max IC Socket Series ===");
            String[] sockets = {
                "110-93-308-41-001000",
                "510-93-024-17-001000",
                "610-93-168-41-001000"
            };
            for (String mpn : sockets) {
                System.out.println(mpn + ":");
                System.out.println("  Series: " + handler.extractSeries(mpn));
                System.out.println("  Family: " + handler.getFamily(mpn));
                System.out.println("  Mounting: " + handler.getMountingType(mpn));
                System.out.println("  Application: " + handler.getApplicationType(mpn));
                System.out.println("  Is IC Socket: " + handler.isICSocket(mpn));
            }
        }

        @Test
        void documentHeaderSeries() {
            System.out.println("=== Mill-Max Header/Receptacle Series ===");
            String[] headers = {
                "300-93-108-41-001000",
                "310-93-108-41-001000",
                "311-93-108-41-001000",
                "800-93-108-41-001000",
                "801-93-108-41-001000"
            };
            for (String mpn : headers) {
                System.out.println(mpn + ":");
                System.out.println("  Series: " + handler.extractSeries(mpn));
                System.out.println("  Family: " + handler.getFamily(mpn));
                System.out.println("  Mounting: " + handler.getMountingType(mpn));
                System.out.println("  Pin Count: " + handler.extractPinCount(mpn));
            }
        }
    }
}
