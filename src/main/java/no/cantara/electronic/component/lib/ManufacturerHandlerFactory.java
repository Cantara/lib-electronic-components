package no.cantara.electronic.component.lib;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.lang.reflect.Modifier;

/**
 * Factory for creating and initializing manufacturer handlers.
 */
public class ManufacturerHandlerFactory {
    private static final String MANUFACTURERS_PACKAGE = "no.cantara.electronic.component.lib.manufacturers";
    private static final Set<ManufacturerHandler> handlers = new HashSet<>();
    private static boolean initialized = false;

    /**
     * Initialize all manufacturer handlers from the manufacturers package.
     */
    public static synchronized Set<ManufacturerHandler> initializeHandlers() {
        if (initialized) {
            return handlers;
        }

        try {
            // Find all handler classes in the package
            Class<?>[] handlerClasses = findHandlerClasses();

            // Create instances and initialize patterns
            PatternRegistry registry = new PatternRegistry();

// Debug: Print found handler classes
            System.out.println("Found handler classes: " + Arrays.toString(handlerClasses));
            for (Class<?> handlerClass : handlerClasses) {
                try {
                    // Skip abstract classes and interfaces
                    if (Modifier.isAbstract(handlerClass.getModifiers()) ||
                            handlerClass.isInterface()) {
                        continue;
                    }

                    // Create instance if it implements ManufacturerHandler
                    if (ManufacturerHandler.class.isAssignableFrom(handlerClass)) {
                        ManufacturerHandler handler =
                                (ManufacturerHandler) handlerClass.getDeclaredConstructor().newInstance();

                        // Initialize patterns
                        registry.setCurrentHandlerClass(handlerClass);
                        handler.initializePatterns(registry);

                        handlers.add(handler);

                        // Debug output
                        System.out.println("Initialized handler: " + handlerClass.getSimpleName());
                    }
                } catch (Exception e) {
                    System.err.println("Error initializing handler: " + handlerClass.getName());
                    e.printStackTrace();
                }
            }

            initialized = true;
            System.out.println("Initialized " + handlers.size() + " manufacturer handlers");

            return handlers;

        } catch (Exception e) {
            System.err.println("Error initializing manufacturer handlers");
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize manufacturer handlers", e);
        }
    }

    private static Class<?>[] findHandlerClasses() throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        String path = MANUFACTURERS_PACKAGE.replace('.', '/');

        // Get the ClassLoader's resource URL for the package
        ClassLoader classLoader = ManufacturerHandlerFactory.class.getClassLoader();
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());

            if (directory.exists()) {
                // Get all .class files in the directory
                String[] files = directory.list((dir, name) -> name.endsWith(".class"));
                if (files != null) {
                    for (String file : files) {
                        String className = MANUFACTURERS_PACKAGE + '.' +
                                file.substring(0, file.length() - 6);
                        try {
                            Class<?> cls = Class.forName(className);
                            classes.add(cls);
                        } catch (Exception e) {
                            System.err.println("Error loading class: " + className);
                        }
                    }
                }
            }
        }

        return classes.toArray(new Class<?>[0]);
    }

    /**
     * Get all initialized handlers.
     */
    public static Set<ManufacturerHandler> getHandlers() {
        if (!initialized) {
            initializeHandlers();
        }
        return Collections.unmodifiableSet(handlers);
    }
}