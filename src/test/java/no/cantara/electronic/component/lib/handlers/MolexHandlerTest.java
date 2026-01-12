package no.cantara.electronic.component.lib.handlers;

import no.cantara.electronic.component.lib.ComponentType;
import no.cantara.electronic.component.lib.PatternRegistry;
import no.cantara.electronic.component.lib.manufacturers.MolexHandler;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for MolexHandler.
 * Tests Micro-Fit 3.0, Mini-Fit Jr., PicoBlade, KK 254, PicoClasp, Nano-Fit, and Micro-Lock Plus connectors.
 */
class MolexHandlerTest {

    private static MolexHandler handler;
    private static PatternRegistry registry;

    @BeforeAll
    static void setUp() {
        handler = new MolexHandler();
        registry = new PatternRegistry();
        handler.initializePatterns(registry);
    }

    @Nested
    @DisplayName("Micro-Fit 3.0 Series Detection (3.0mm pitch)")
    class MicroFit30Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "43045-0200",
            "43045-0400",
            "43045-0600",
            "43046-0200",
            "43046-0400",
            "43046-0600"
        })
        void documentMicroFit30Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesMolex = handler.matches(mpn, ComponentType.CONNECTOR_MOLEX, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_MOLEX = " + matchesMolex);
        }
    }

    @Nested
    @DisplayName("Mini-Fit Jr. Series Detection (4.2mm pitch)")
    class MiniFitJrTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "39281-0200",
            "39281-0400",
            "39281-0600",
            "39282-0200",
            "39282-0400",
            "39282-0600"
        })
        void documentMiniFitJrDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesMolex = handler.matches(mpn, ComponentType.CONNECTOR_MOLEX, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_MOLEX = " + matchesMolex);
        }
    }

    @Nested
    @DisplayName("PicoBlade Series Detection (1.25mm pitch)")
    class PicoBladeTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "53261-0200",
            "53261-0400",
            "53261-0600",
            "53262-0200",
            "53262-0400",
            "53262-0600"
        })
        void documentPicoBladeDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesMolex = handler.matches(mpn, ComponentType.CONNECTOR_MOLEX, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_MOLEX = " + matchesMolex);
        }
    }

    @Nested
    @DisplayName("KK 254 Series Detection (2.54mm pitch)")
    class KK254Tests {
        @ParameterizedTest
        @ValueSource(strings = {
            "22057-0200",
            "22057-0400",
            "22057-0600",
            "22058-0200",
            "22058-0400",
            "22058-0600"
        })
        void documentKK254Detection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesMolex = handler.matches(mpn, ComponentType.CONNECTOR_MOLEX, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_MOLEX = " + matchesMolex);
        }
    }

    @Nested
    @DisplayName("PicoClasp Series Detection (1.0mm pitch)")
    class PicoClaspTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "51021-0200",
            "51021-0400",
            "51021-0600"
        })
        void documentPicoClaspDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesMolex = handler.matches(mpn, ComponentType.CONNECTOR_MOLEX, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_MOLEX = " + matchesMolex);
        }
    }

    @Nested
    @DisplayName("Nano-Fit Series Detection (2.5mm pitch)")
    class NanoFitTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "51047-0200",
            "51047-0400",
            "51047-0600"
        })
        void documentNanoFitDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesMolex = handler.matches(mpn, ComponentType.CONNECTOR_MOLEX, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_MOLEX = " + matchesMolex);
        }
    }

    @Nested
    @DisplayName("Micro-Lock Plus Series Detection (2.0mm pitch)")
    class MicroLockPlusTests {
        @ParameterizedTest
        @ValueSource(strings = {
            "87832-0200",
            "87832-0400",
            "87832-0600"
        })
        void documentMicroLockPlusDetection(String mpn) {
            boolean matchesConnector = handler.matches(mpn, ComponentType.CONNECTOR, registry);
            boolean matchesMolex = handler.matches(mpn, ComponentType.CONNECTOR_MOLEX, registry);
            System.out.println(mpn + " matches CONNECTOR = " + matchesConnector);
            System.out.println(mpn + " matches CONNECTOR_MOLEX = " + matchesMolex);
        }
    }

    @Nested
    @DisplayName("Package Code Extraction")
    class PackageCodeTests {
        @ParameterizedTest
        @CsvSource({
            "43045-0200, 0200",
            "43045-0401, 0401",
            "39281-0600, 0600",
            "53261-0210, 0210",
            "22057-0411, 0411",
            "51021-0302, 0302",
            "51047-0810, 0810",
            "87832-0402, 0402"
        })
        void shouldExtractPackageCode(String mpn, String expected) {
            assertEquals(expected, handler.extractPackageCode(mpn));
        }
    }

    @Nested
    @DisplayName("Series Extraction")
    class SeriesExtractionTests {
        @ParameterizedTest
        @CsvSource({
            "43045-0200, 43045",
            "43046-0400, 43046",
            "39281-0600, 39281",
            "39282-0800, 39282",
            "53261-0210, 53261",
            "53262-0410, 53262",
            "22057-0200, 22057",
            "22058-0400, 22058",
            "51021-0600, 51021",
            "51047-0800, 51047",
            "87832-0402, 87832"
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
            assertEquals("3.00", handler.getPitch("43045-0200"));
            assertEquals("3.00", handler.getPitch("43046-0400"));
            assertEquals("4.20", handler.getPitch("39281-0600"));
            assertEquals("4.20", handler.getPitch("39282-0800"));
            assertEquals("1.25", handler.getPitch("53261-0210"));
            assertEquals("1.25", handler.getPitch("53262-0410"));
            assertEquals("2.54", handler.getPitch("22057-0200"));
            assertEquals("2.54", handler.getPitch("22058-0400"));
            assertEquals("1.00", handler.getPitch("51021-0600"));
            assertEquals("2.50", handler.getPitch("51047-0800"));
            assertEquals("2.00", handler.getPitch("87832-0402"));
        }

        @Test
        void shouldGetMountingType() {
            assertEquals("THT", handler.getMountingType("43045-0201"));
            assertEquals("THT", handler.getMountingType("43045-0202"));
            assertEquals("SMT", handler.getMountingType("43045-0210"));
            assertEquals("SMT", handler.getMountingType("43045-0211"));
        }

        @Test
        void shouldGetOrientation() {
            assertEquals("Vertical", handler.getOrientation("43045-0201"));
            assertEquals("Right Angle", handler.getOrientation("43045-0211"));
            assertEquals("Right Angle Reversed", handler.getOrientation("43045-0221"));
        }

        @Test
        void shouldGetRatedCurrent() {
            assertEquals(5.0, handler.getRatedCurrent("43045-0200"));
            assertEquals(5.0, handler.getRatedCurrent("43046-0400"));
            assertEquals(9.0, handler.getRatedCurrent("39281-0600"));
            assertEquals(9.0, handler.getRatedCurrent("39282-0800"));
            assertEquals(1.0, handler.getRatedCurrent("53261-0210"));
            assertEquals(1.0, handler.getRatedCurrent("53262-0410"));
            assertEquals(3.0, handler.getRatedCurrent("22057-0200"));
            assertEquals(3.0, handler.getRatedCurrent("22058-0400"));
            assertEquals(1.0, handler.getRatedCurrent("51021-0600"));
            assertEquals(3.5, handler.getRatedCurrent("51047-0800"));
            assertEquals(3.0, handler.getRatedCurrent("87832-0402"));
        }

        @Test
        void shouldDetermineIfKeyed() {
            assertTrue(handler.isKeyed("43045-0200"));
            assertTrue(handler.isKeyed("39281-0600"));
            assertTrue(handler.isKeyed("53261-0210"));
            assertFalse(handler.isKeyed("22057-0200"));  // KK series may have unkeyed variants
        }

        @Test
        void shouldDetermineGender() {
            assertEquals("Male", handler.getGender("43045-0200"));   // Odd = Male
            assertEquals("Female", handler.getGender("43046-0400")); // Even = Female
            assertEquals("Male", handler.getGender("39281-0600"));   // Odd = Male
            assertEquals("Female", handler.getGender("39282-0800")); // Even = Female
        }

        @Test
        void shouldDetermineIfHasLatchingMechanism() {
            assertTrue(handler.hasLatchingMechanism("43045-0200"));
            assertTrue(handler.hasLatchingMechanism("39281-0600"));
            assertFalse(handler.hasLatchingMechanism("22057-0200")); // KK series may not have latching
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
        }

        @Test
        void shouldHandleEmptyString() {
            assertFalse(handler.matches("", ComponentType.CONNECTOR, registry));
            assertEquals("", handler.extractPackageCode(""));
            assertEquals("", handler.extractSeries(""));
        }

        @Test
        void shouldReturnEmptyForInvalidMpn() {
            assertEquals("", handler.extractPackageCode("INVALID-MPN"));
            assertEquals("", handler.extractSeries("INVALID-MPN"));
        }
    }

    @Nested
    @DisplayName("Replacement Logic")
    class ReplacementTests {
        @Test
        void shouldNotReplaceAcrossFamilies() {
            assertFalse(handler.isOfficialReplacement("43045-0200", "39281-0200"));
            assertFalse(handler.isOfficialReplacement("53261-0400", "22057-0400"));
        }

        @Test
        void shouldNotReplaceWithDifferentPinCount() {
            assertFalse(handler.isOfficialReplacement("43045-0200", "43045-0400"));
            assertFalse(handler.isOfficialReplacement("39281-0400", "39281-0600"));
        }

        @Test
        void documentSameFamilyVariants() {
            // Same family, same pin count, compatible mounting types
            boolean isReplacement = handler.isOfficialReplacement("43045-0201", "43045-0202");
            System.out.println("43045-0201 can replace 43045-0202 = " + isReplacement);

            isReplacement = handler.isOfficialReplacement("43045-0210", "43045-0211");
            System.out.println("43045-0210 can replace 43045-0211 = " + isReplacement);
        }

        @Test
        void documentCrossSeriesWithinFamily() {
            // 43045 and 43046 are both Micro-Fit 3.0
            boolean isReplacement = handler.isOfficialReplacement("43045-0201", "43046-0201");
            System.out.println("43045-0201 can replace 43046-0201 (same family) = " + isReplacement);
        }
    }
}
