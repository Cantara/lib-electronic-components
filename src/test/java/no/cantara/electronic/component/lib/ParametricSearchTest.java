package no.cantara.electronic.component.lib;

import no.cantara.electronic.component.BOMEntry;
import no.cantara.electronic.component.ElectronicPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ParametricSearch utility.
 */
class ParametricSearchTest {

    private List<ElectronicPart> capacitors;
    private List<ElectronicPart> resistors;

    @BeforeEach
    void setUp() {
        capacitors = createCapacitorTestData();
        resistors = createResistorTestData();
    }

    private List<ElectronicPart> createCapacitorTestData() {
        List<ElectronicPart> caps = new ArrayList<>();

        // 100nF 50V X7R 0402
        caps.add(new ElectronicPart()
                .setMpn("GRM155R71H104KA88D")
                .setManufacturer("Murata")
                .addSpec("capacitance", "100nF")
                .addSpec("voltage", "50V")
                .addSpec("dielectric", "X7R")
                .addSpec("package", "0402")
                .addSpec("tolerance", "10%"));

        // 10uF 25V X5R 0805
        caps.add(new ElectronicPart()
                .setMpn("GRM21BR61E106KA73L")
                .setManufacturer("Murata")
                .addSpec("capacitance", "10uF")
                .addSpec("voltage", "25V")
                .addSpec("dielectric", "X5R")
                .addSpec("package", "0805")
                .addSpec("tolerance", "10%"));

        // 1nF 100V C0G 0603
        caps.add(new ElectronicPart()
                .setMpn("GRM1885C1H102JA01D")
                .setManufacturer("Murata")
                .addSpec("capacitance", "1nF")
                .addSpec("voltage", "100V")
                .addSpec("dielectric", "C0G")
                .addSpec("package", "0603")
                .addSpec("tolerance", "5%"));

        // 22uF 16V X5R 1206
        caps.add(new ElectronicPart()
                .setMpn("GRM31CR61C226KE15L")
                .setManufacturer("Murata")
                .addSpec("capacitance", "22uF")
                .addSpec("voltage", "16V")
                .addSpec("dielectric", "X5R")
                .addSpec("package", "1206")
                .addSpec("tolerance", "10%"));

        // 100pF 50V C0G 0402
        caps.add(new ElectronicPart()
                .setMpn("GRM1555C1H101JA01D")
                .setManufacturer("Murata")
                .addSpec("capacitance", "100pF")
                .addSpec("voltage", "50V")
                .addSpec("dielectric", "C0G")
                .addSpec("package", "0402")
                .addSpec("tolerance", "5%"));

        return caps;
    }

    private List<ElectronicPart> createResistorTestData() {
        List<ElectronicPart> res = new ArrayList<>();

        // 10k 0402 1%
        res.add(new ElectronicPart()
                .setMpn("RC0402FR-0710KL")
                .setManufacturer("Yageo")
                .addSpec("resistance", "10k")
                .addSpec("package", "0402")
                .addSpec("tolerance", "1%")
                .addSpec("power", "62.5mW"));

        // 4.7k 0603 1%
        res.add(new ElectronicPart()
                .setMpn("RC0603FR-074K7L")
                .setManufacturer("Yageo")
                .addSpec("resistance", "4.7k")
                .addSpec("package", "0603")
                .addSpec("tolerance", "1%")
                .addSpec("power", "100mW"));

        // 100R 0805 5%
        res.add(new ElectronicPart()
                .setMpn("RC0805JR-07100RL")
                .setManufacturer("Yageo")
                .addSpec("resistance", "100")
                .addSpec("package", "0805")
                .addSpec("tolerance", "5%")
                .addSpec("power", "125mW"));

        // 1M 0402 1%
        res.add(new ElectronicPart()
                .setMpn("RC0402FR-071ML")
                .setManufacturer("Yageo")
                .addSpec("resistance", "1M")
                .addSpec("package", "0402")
                .addSpec("tolerance", "1%")
                .addSpec("power", "62.5mW"));

        return res;
    }

    @Nested
    @DisplayName("Value Parsing Tests")
    class ValueParsingTests {

        private void assertValueEquals(double expected, BigDecimal actual) {
            assertNotNull(actual, "Parsed value should not be null");
            assertEquals(0, BigDecimal.valueOf(expected).compareTo(actual),
                    "Expected " + expected + " but got " + actual);
        }

        @Test
        @DisplayName("Parse capacitance values with different prefixes")
        void parseCapacitanceValues() {
            assertValueEquals(1e-12, ParametricSearch.parseNumericValue("1pF"));
            assertValueEquals(100e-12, ParametricSearch.parseNumericValue("100pF"));
            assertValueEquals(1e-9, ParametricSearch.parseNumericValue("1nF"));
            assertValueEquals(100e-9, ParametricSearch.parseNumericValue("100nF"));
            assertValueEquals(1e-6, ParametricSearch.parseNumericValue("1uF"));
            assertValueEquals(10e-6, ParametricSearch.parseNumericValue("10uF"));
            assertValueEquals(22e-6, ParametricSearch.parseNumericValue("22uF"));
        }

        @Test
        @DisplayName("Parse resistance values with different prefixes")
        void parseResistanceValues() {
            assertValueEquals(100, ParametricSearch.parseNumericValue("100"));
            assertValueEquals(1000, ParametricSearch.parseNumericValue("1k"));
            assertValueEquals(4700, ParametricSearch.parseNumericValue("4.7k"));
            assertValueEquals(10000, ParametricSearch.parseNumericValue("10k"));
            assertValueEquals(1000000, ParametricSearch.parseNumericValue("1M"));
        }

        @Test
        @DisplayName("Parse voltage values")
        void parseVoltageValues() {
            assertValueEquals(16, ParametricSearch.parseNumericValue("16V"));
            assertValueEquals(25, ParametricSearch.parseNumericValue("25V"));
            assertValueEquals(50, ParametricSearch.parseNumericValue("50V"));
            assertValueEquals(100, ParametricSearch.parseNumericValue("100V"));
        }

        @Test
        @DisplayName("Parse percentage values")
        void parsePercentageValues() {
            assertValueEquals(1, ParametricSearch.parseNumericValue("1%"));
            assertValueEquals(5, ParametricSearch.parseNumericValue("5%"));
            assertValueEquals(10, ParametricSearch.parseNumericValue("10%"));
        }

        @Test
        @DisplayName("Handle null and empty values")
        void handleNullAndEmpty() {
            assertNull(ParametricSearch.parseNumericValue(null));
            assertNull(ParametricSearch.parseNumericValue(""));
            assertNull(ParametricSearch.parseNumericValue("   "));
        }
    }

    @Nested
    @DisplayName("Comparison Operator Tests")
    class ComparisonOperatorTests {

        @Test
        @DisplayName("Filter capacitors >= 10nF")
        void filterCapacitorsGreaterThanOrEqual() {
            Map<String, String> requirements = Map.of("capacitance", ">= 10nF");
            List<ElectronicPart> results = ParametricSearch.filter(capacitors, requirements);

            // Should match: 100nF, 10uF, 22uF (all >= 10nF)
            assertEquals(3, results.size());
            assertTrue(results.stream().allMatch(p ->
                    p.getSpecs().get("capacitance").contains("nF") ||
                    p.getSpecs().get("capacitance").contains("uF")));
        }

        @Test
        @DisplayName("Filter capacitors < 1uF")
        void filterCapacitorsLessThan() {
            Map<String, String> requirements = Map.of("capacitance", "< 1uF");
            List<ElectronicPart> results = ParametricSearch.filter(capacitors, requirements);

            // Should match: 100nF, 1nF, 100pF (all < 1uF)
            assertEquals(3, results.size());
        }

        @Test
        @DisplayName("Filter capacitors with voltage >= 50V")
        void filterByVoltage() {
            Map<String, String> requirements = Map.of("voltage", ">= 50V");
            List<ElectronicPart> results = ParametricSearch.filter(capacitors, requirements);

            // Should match: 100nF@50V, 1nF@100V, 100pF@50V
            assertEquals(3, results.size());
        }

        @Test
        @DisplayName("Filter resistors by tolerance <= 1%")
        void filterByTolerance() {
            Map<String, String> requirements = Map.of("tolerance", "<= 1%");
            List<ElectronicPart> results = ParametricSearch.filter(resistors, requirements);

            // 10k@1%, 4.7k@1%, 1M@1%
            assertEquals(3, results.size());
        }
    }

    @Nested
    @DisplayName("Range Query Tests")
    class RangeQueryTests {

        @Test
        @DisplayName("Filter capacitors in range 10nF..1uF")
        void filterCapacitorsInRange() {
            Map<String, String> requirements = Map.of("capacitance", "10nF..1uF");
            List<ElectronicPart> results = ParametricSearch.filter(capacitors, requirements);

            // Should match: 100nF only (10nF <= 100nF <= 1uF)
            assertEquals(1, results.size());
            assertEquals("100nF", results.get(0).getSpecs().get("capacitance"));
        }

        @Test
        @DisplayName("Filter resistors in range 1k..100k")
        void filterResistorsInRange() {
            Map<String, String> requirements = Map.of("resistance", "1k..100k");
            List<ElectronicPart> results = ParametricSearch.filter(resistors, requirements);

            // Should match: 10k, 4.7k (1k <= value <= 100k)
            assertEquals(2, results.size());
        }
    }

    @Nested
    @DisplayName("Set Membership Tests (IN)")
    class SetMembershipTests {

        @Test
        @DisplayName("Filter capacitors by dielectric IN(X7R, X5R)")
        void filterByDielectricIn() {
            Map<String, String> requirements = Map.of("dielectric", "IN(X7R, X5R)");
            List<ElectronicPart> results = ParametricSearch.filter(capacitors, requirements);

            // X7R: 100nF, X5R: 10uF, 22uF
            assertEquals(3, results.size());
        }

        @Test
        @DisplayName("Filter capacitors by package IN(0402, 0603)")
        void filterByPackageIn() {
            Map<String, String> requirements = Map.of("package", "IN(0402, 0603)");
            List<ElectronicPart> results = ParametricSearch.filter(capacitors, requirements);

            // 0402: 100nF, 100pF; 0603: 1nF
            assertEquals(3, results.size());
        }

        @Test
        @DisplayName("IN is case insensitive")
        void inIsCaseInsensitive() {
            Map<String, String> requirements = Map.of("dielectric", "in(x7r, X5R)");
            List<ElectronicPart> results = ParametricSearch.filter(capacitors, requirements);
            assertEquals(3, results.size());
        }
    }

    @Nested
    @DisplayName("Combined Filter Tests")
    class CombinedFilterTests {

        @Test
        @DisplayName("Filter by capacitance AND voltage AND dielectric")
        void filterByCombinedRequirements() {
            Map<String, String> requirements = Map.of(
                    "capacitance", ">= 1nF",
                    "voltage", ">= 25V",
                    "dielectric", "IN(X7R, X5R)"
            );
            List<ElectronicPart> results = ParametricSearch.filter(capacitors, requirements);

            // X7R 100nF@50V, X5R 10uF@25V match all criteria
            assertEquals(2, results.size());
        }

        @Test
        @DisplayName("Filter resistors by range AND tolerance AND package")
        void filterResistorsCombined() {
            Map<String, String> requirements = Map.of(
                    "resistance", "1k..100k",
                    "tolerance", "<= 1%",
                    "package", "IN(0402, 0603)"
            );
            List<ElectronicPart> results = ParametricSearch.filter(resistors, requirements);

            // 10k@0402@1%, 4.7k@0603@1%
            assertEquals(2, results.size());
        }
    }

    @Nested
    @DisplayName("Fluent Builder Tests")
    class FluentBuilderTests {

        @Test
        @DisplayName("Use fluent builder for search")
        void fluentBuilderSearch() {
            List<ElectronicPart> results = ParametricSearch.search(capacitors)
                    .min("capacitance", "10nF")
                    .max("voltage", "50V")
                    .in("dielectric", "X7R", "X5R")
                    .find();

            // 100nF@50V@X7R, 10uF@25V@X5R, 22uF@16V@X5R
            assertEquals(3, results.size());
        }

        @Test
        @DisplayName("Use range method")
        void fluentBuilderRange() {
            List<ElectronicPart> results = ParametricSearch.search(resistors)
                    .range("resistance", "1k", "50k")
                    .find();

            assertEquals(2, results.size());
        }

        @Test
        @DisplayName("Use equals and notEquals")
        void fluentBuilderEqualsAndNotEquals() {
            List<ElectronicPart> results = ParametricSearch.search(capacitors)
                    .equals("dielectric", "X7R")
                    .notEquals("package", "0805")
                    .find();

            // X7R is only at 0402
            assertEquals(1, results.size());
        }

        @Test
        @DisplayName("Count matching parts")
        void fluentBuilderCount() {
            long count = ParametricSearch.search(capacitors)
                    .min("voltage", "50V")
                    .count();

            assertEquals(3, count);
        }

        @Test
        @DisplayName("Check anyMatch")
        void fluentBuilderAnyMatch() {
            assertTrue(ParametricSearch.search(capacitors)
                    .equals("capacitance", "100nF")
                    .anyMatch());

            assertFalse(ParametricSearch.search(capacitors)
                    .equals("capacitance", "999uF")
                    .anyMatch());
        }

        @Test
        @DisplayName("FindFirst returns Optional")
        void fluentBuilderFindFirst() {
            Optional<ElectronicPart> result = ParametricSearch.search(capacitors)
                    .equals("dielectric", "C0G")
                    .min("voltage", "100V")
                    .findFirst();

            assertTrue(result.isPresent());
            assertEquals("1nF", result.get().getSpecs().get("capacitance"));
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Empty collection returns empty list")
        void emptyCollectionReturnsEmpty() {
            List<ElectronicPart> results = ParametricSearch.filter(
                    Collections.emptyList(),
                    Map.of("capacitance", ">= 10nF"));
            assertTrue(results.isEmpty());
        }

        @Test
        @DisplayName("Null collection returns empty list")
        void nullCollectionReturnsEmpty() {
            List<ElectronicPart> results = ParametricSearch.filter(
                    null,
                    Map.of("capacitance", ">= 10nF"));
            assertTrue(results.isEmpty());
        }

        @Test
        @DisplayName("Empty requirements returns all parts")
        void emptyRequirementsReturnsAll() {
            List<ElectronicPart> results = ParametricSearch.filter(
                    capacitors,
                    Collections.emptyMap());
            assertEquals(capacitors.size(), results.size());
        }

        @Test
        @DisplayName("Null requirements returns all parts")
        void nullRequirementsReturnsAll() {
            List<ElectronicPart> results = ParametricSearch.filter(capacitors, null);
            assertEquals(capacitors.size(), results.size());
        }

        @Test
        @DisplayName("Missing spec returns no match for numeric comparison")
        void missingSpecNoMatch() {
            Map<String, String> requirements = Map.of("nonexistent", ">= 10");
            List<ElectronicPart> results = ParametricSearch.filter(capacitors, requirements);
            assertTrue(results.isEmpty());
        }

        @Test
        @DisplayName("Parts with missing specs are filtered out")
        void partsWithMissingSpecsFilteredOut() {
            List<ElectronicPart> parts = new ArrayList<>(capacitors);
            parts.add(new ElectronicPart()
                    .setMpn("INCOMPLETE")
                    .setManufacturer("Test"));

            Map<String, String> requirements = Map.of("capacitance", ">= 1nF");
            List<ElectronicPart> results = ParametricSearch.filter(parts, requirements);

            // Should not include the incomplete part
            assertFalse(results.stream().anyMatch(p -> "INCOMPLETE".equals(p.getMpn())));
        }
    }

    @Nested
    @DisplayName("Single Part Check Tests")
    class SinglePartCheckTests {

        @Test
        @DisplayName("Check single part meets requirement")
        void checkSinglePartMeetsRequirement() {
            ElectronicPart cap = capacitors.get(0); // 100nF 50V X7R

            assertTrue(ParametricSearch.meets(cap, "capacitance", ">= 10nF"));
            assertTrue(ParametricSearch.meets(cap, "voltage", ">= 50V"));
            assertTrue(ParametricSearch.meets(cap, "dielectric", "= X7R"));

            assertFalse(ParametricSearch.meets(cap, "capacitance", ">= 1uF"));
            assertFalse(ParametricSearch.meets(cap, "voltage", "> 50V"));
        }
    }

    @Nested
    @DisplayName("BOMEntry Compatibility Tests")
    class BOMEntryTests {

        @Test
        @DisplayName("Filter works with BOMEntry subclass")
        void filterWorkWithBOMEntry() {
            List<BOMEntry> bomEntries = new ArrayList<>();
            bomEntries.add(new BOMEntry()
                    .setMpn("CAP001")
                    .setManufacturer("Test")
                    .setQty("10")
                    .addSpec("capacitance", "100nF")
                    .addSpec("voltage", "50V"));
            bomEntries.add(new BOMEntry()
                    .setMpn("CAP002")
                    .setManufacturer("Test")
                    .setQty("5")
                    .addSpec("capacitance", "10uF")
                    .addSpec("voltage", "25V"));

            List<ElectronicPart> results = ParametricSearch.filter(
                    bomEntries,
                    Map.of("capacitance", ">= 1uF"));

            assertEquals(1, results.size());
            assertEquals("CAP002", results.get(0).getMpn());
        }
    }

    @Nested
    @DisplayName("Built-in Field Access Tests")
    class BuiltInFieldTests {

        @Test
        @DisplayName("Filter by manufacturer field")
        void filterByManufacturer() {
            List<ElectronicPart> parts = new ArrayList<>();
            parts.add(new ElectronicPart().setMpn("A").setManufacturer("Murata"));
            parts.add(new ElectronicPart().setMpn("B").setManufacturer("TDK"));
            parts.add(new ElectronicPart().setMpn("C").setManufacturer("Murata"));

            List<ElectronicPart> results = ParametricSearch.filter(
                    parts,
                    Map.of("manufacturer", "= Murata"));

            assertEquals(2, results.size());
        }

        @Test
        @DisplayName("Filter by package field (pkg)")
        void filterByPackage() {
            List<ElectronicPart> parts = new ArrayList<>();
            parts.add(new ElectronicPart().setMpn("A").setPkg("0402"));
            parts.add(new ElectronicPart().setMpn("B").setPkg("0603"));
            parts.add(new ElectronicPart().setMpn("C").setPkg("0402"));

            List<ElectronicPart> results = ParametricSearch.search(parts)
                    .in("package", "0402")
                    .find();

            assertEquals(2, results.size());
        }
    }
}
