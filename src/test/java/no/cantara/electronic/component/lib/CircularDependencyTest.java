package no.cantara.electronic.component.lib;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to reproduce and prevent circular dependency between ComponentType and ComponentManufacturer.
 *
 * These tests verify that both enums can initialize in any order without causing NoClassDefFoundError.
 *
 * ## Root Cause of Circular Dependency
 *
 * The circular dependency occurs when:
 * 1. ComponentType static field MANUFACTURER_SUFFIX_MAP references ComponentManufacturer constants
 * 2. ComponentManufacturer constructor creates handler instances (e.g., new HoneywellHandler())
 * 3. Handler classes contain enum switch statements (e.g., switch(ComponentType))
 * 4. Java compiler generates synthetic switch table class (e.g., HoneywellHandler$1)
 * 5. Switch table static initializer references all ComponentType enum constants
 * 6. This triggers ComponentType to load → circular dependency
 *
 * ## Why Tests May Pass
 *
 * These tests may PASS even with the circular dependency because:
 * - Test classloader initialization order is deterministic
 * - By the time tests run, both enums are already initialized
 * - The circular dependency only manifests during FIRST initialization in production
 *
 * ## Production Error
 *
 * In production, the error manifests as:
 * ```
 * java.lang.NoClassDefFoundError: Could not initialize class ComponentType
 *     at HoneywellHandler$1.<clinit>(HoneywellHandler.java:367)
 *     at ComponentManufacturer.<init>(ComponentManufacturer.java:226)
 * ```
 *
 * ## Solution
 *
 * Implement lazy initialization:
 * 1. Lazy MANUFACTURER_SUFFIX_MAP in ComponentType
 * 2. Lazy handler initialization in ComponentManufacturer
 *
 * This test suite documents the expected behavior after the fix.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CircularDependencyTest {

    /**
     * Test 1: ComponentType initializes first.
     * This triggers: ComponentType loads → references ComponentManufacturer → creates handlers → handlers need ComponentType
     */
    @Test
    @Order(1)
    @DisplayName("ComponentType can initialize first without circular dependency")
    void testComponentTypeInitializesFirst() {
        assertDoesNotThrow(() -> {
            // Force ComponentType to initialize by accessing an enum constant
            ComponentType type = ComponentType.OPAMP_TI;
            assertNotNull(type, "ComponentType should initialize");

            // Now access getManufacturer() which references ComponentManufacturer
            ComponentManufacturer manufacturer = type.getManufacturer();
            assertNotNull(manufacturer, "Should be able to get manufacturer from ComponentType");

            // Verify the manufacturer is correct
            assertEquals(ComponentManufacturer.TI, manufacturer, "OPAMP_TI should map to TI manufacturer");
        }, "ComponentType initialization should not cause NoClassDefFoundError");
    }

    /**
     * Test 2: ComponentManufacturer initializes first.
     * This is the more common path in production (via MPNUtils).
     */
    @Test
    @Order(2)
    @DisplayName("ComponentManufacturer can initialize first without circular dependency")
    void testComponentManufacturerInitializesFirst() {
        assertDoesNotThrow(() -> {
            // Force ComponentManufacturer to initialize
            ComponentManufacturer manufacturer = ComponentManufacturer.TI;
            assertNotNull(manufacturer, "ComponentManufacturer should initialize");

            // Access handler (this creates the handler which might reference ComponentType)
            ManufacturerHandler handler = manufacturer.getHandler();
            assertNotNull(handler, "Should be able to get handler from ComponentManufacturer");

            // Now access ComponentType
            ComponentType type = ComponentType.OPAMP_TI;
            assertNotNull(type, "ComponentType should initialize after ComponentManufacturer");

        }, "ComponentManufacturer initialization should not cause NoClassDefFoundError");
    }

    /**
     * Test 3: Concurrent initialization from multiple threads.
     * Simulates production scenario where multiple threads hit different entry points simultaneously.
     */
    @Test
    @Order(3)
    @DisplayName("Concurrent initialization from multiple threads should not deadlock")
    void testConcurrentInitialization() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        // 10 threads access ComponentType first
        for (int i = 0; i < 10; i++) {
            tasks.add(() -> {
                try {
                    ComponentType type = ComponentType.OPAMP_TI;
                    ComponentManufacturer mfr = type.getManufacturer();
                    return mfr != null;
                } catch (NoClassDefFoundError e) {
                    System.err.println("NoClassDefFoundError in ComponentType-first thread: " + e.getMessage());
                    throw e;
                }
            });
        }

        // 10 threads access ComponentManufacturer first
        for (int i = 0; i < 10; i++) {
            tasks.add(() -> {
                try {
                    ComponentManufacturer mfr = ComponentManufacturer.TI;
                    ManufacturerHandler handler = mfr.getHandler();
                    return handler != null;
                } catch (NoClassDefFoundError e) {
                    System.err.println("NoClassDefFoundError in ComponentManufacturer-first thread: " + e.getMessage());
                    throw e;
                }
            });
        }

        // Execute all tasks concurrently
        List<Future<Boolean>> futures = executor.invokeAll(tasks);

        // All should complete without exception
        for (Future<Boolean> future : futures) {
            assertTrue(future.get(), "Thread should complete successfully");
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS), "Executor should shutdown cleanly");
    }

    /**
     * Test 4: MPNUtils path (production entry point).
     * This is the actual code path that fails in production.
     */
    @Test
    @Order(4)
    @DisplayName("MPNUtils initialization should not trigger circular dependency")
    void testMPNUtilsInitialization() {
        assertDoesNotThrow(() -> {
            // MPNUtils uses ManufacturerHandlerFactory which initializes handlers
            // This is the path that fails in production
            ComponentManufacturer mfr = MPNUtils.getManufacturer("LM358");
            assertNotNull(mfr, "MPNUtils.getManufacturer should work");

            // Should still be able to access ComponentType after
            ComponentType type = ComponentType.OPAMP_TI;
            assertNotNull(type, "ComponentType should be accessible after MPNUtils");

        }, "MPNUtils initialization should not cause NoClassDefFoundError");
    }

    /**
     * Test 5: Handler directly referencing ComponentType.
     * Verifies that handlers can safely access ComponentType after lazy initialization.
     */
    @Test
    @Order(5)
    @DisplayName("Handlers should be able to access ComponentType")
    void testHandlerAccessToComponentType() {
        assertDoesNotThrow(() -> {
            ComponentManufacturer ti = ComponentManufacturer.TI;
            ManufacturerHandler handler = ti.getHandler();

            // Handlers should be able to reference ComponentType in their methods
            // (Not in constructors/static blocks, but in regular methods)
            Set<ComponentType> supportedTypes = handler.getSupportedTypes();
            assertNotNull(supportedTypes, "Handler should return supported types");
            assertFalse(supportedTypes.isEmpty(), "Handler should support at least one type");

        }, "Handlers should be able to access ComponentType in their methods");
    }

    /**
     * Test 6: MANUFACTURER_SUFFIX_MAP access.
     * Specifically tests the ComponentType.getManufacturer() path that uses MANUFACTURER_SUFFIX_MAP.
     */
    @Test
    @Order(6)
    @DisplayName("ComponentType.getManufacturer() should work for suffix-based types")
    void testManufacturerSuffixMapAccess() {
        assertDoesNotThrow(() -> {
            // These types use MANUFACTURER_SUFFIX_MAP to determine manufacturer
            ComponentType onSemiType = ComponentType.MOSFET_ONSEMI;
            ComponentManufacturer onSemi = onSemiType.getManufacturer();
            assertEquals(ComponentManufacturer.ON_SEMI, onSemi, "MOSFET_ONSEMI should map to ON_SEMI");

            ComponentType silabsType = ComponentType.MICROCONTROLLER_SILABS;
            ComponentManufacturer silabs = silabsType.getManufacturer();
            assertEquals(ComponentManufacturer.SILICON_LABS, silabs, "MICROCONTROLLER_SILABS should map to SILICON_LABS");

        }, "MANUFACTURER_SUFFIX_MAP should be accessible without circular dependency");
    }

    /**
     * Test 7: Stress test - rapid repeated initialization attempts.
     * Catches race conditions that might not appear in simpler tests.
     */
    @Test
    @Order(7)
    @DisplayName("Rapid repeated initialization should be stable")
    void testRapidRepeatedInitialization() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(50);
        List<Future<Boolean>> futures = new ArrayList<>();

        // Submit 100 tasks that randomly access either enum
        for (int i = 0; i < 100; i++) {
            final int index = i;
            futures.add(executor.submit(() -> {
                try {
                    if (index % 2 == 0) {
                        ComponentType.values();
                        ComponentManufacturer.values();
                    } else {
                        ComponentManufacturer.values();
                        ComponentType.values();
                    }
                    return true;
                } catch (NoClassDefFoundError | ExceptionInInitializerError e) {
                    System.err.println("Error in rapid init test iteration " + index + ": " + e.getMessage());
                    return false;
                }
            }));
        }

        // All should succeed
        for (Future<Boolean> future : futures) {
            assertTrue(future.get(), "Rapid initialization attempt should succeed");
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS), "Executor should shutdown");
    }

    /**
     * Test 8: Fresh JVM test (if possible).
     * This test documents the limitation - we can't fully test fresh JVM init in a single test run.
     */
    @Test
    @Order(8)
    @DisplayName("Document fresh JVM initialization requirement")
    void testFreshJVMNote() {
        // This test just documents that the circular dependency might only appear
        // on the first initialization in a fresh JVM.
        //
        // To truly test this, run: mvn test -Dtest=CircularDependencyTest#testComponentTypeInitializesFirst
        // in isolation to ensure ComponentType loads first.

        assertTrue(true, "Note: Run individual tests in isolation to test fresh JVM initialization order");
    }
}
