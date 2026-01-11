package no.cantara.electronic.component.lib;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Factory for creating and initializing manufacturer handlers.
 */
public class ManufacturerHandlerFactory {
    private static final String MANUFACTURERS_PACKAGE = "no.cantara.electronic.component.lib.manufacturers";
    // Use TreeSet with deterministic ordering by class name to ensure consistent iteration order
    private static final Set<ManufacturerHandler> handlers = new TreeSet<>(
            Comparator.comparing(h -> h.getClass().getName()));
    private static boolean initialized = false;

    /**
     * Initialize all manufacturer handlers from the manufacturers package.
     */
    public static synchronized Set<ManufacturerHandler> initializeHandlers() {
        if (initialized) {
            return handlers;
        }

        try {
            System.out.println("Initializing manufacturer handlers...");
            List<Class<?>> handlerClasses = findHandlerClasses();
            System.out.println("Found " + handlerClasses.size() + " handler classes");

            PatternRegistry registry = new PatternRegistry();
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
                        registry.setCurrentHandlerClass(handlerClass);
                        handler.initializePatterns(registry);
                        handlers.add(handler);
                        System.out.println("Initialized handler: " + handlerClass.getSimpleName());
                    }
                } catch (Exception e) {
                    System.err.println("Error initializing handler: " + handlerClass.getName());
                    e.printStackTrace();
                }
            }

            initialized = true;
            System.out.println("Successfully initialized " + handlers.size() + " manufacturer handlers");
            return handlers;

        } catch (Exception e) {
            System.err.println("Error initializing manufacturer handlers");
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize manufacturer handlers", e);
        }
    }

    private static List<Class<?>> findHandlerClasses() throws IOException {
        ClassLoader classLoader = ManufacturerHandlerFactory.class.getClassLoader();
        String path = MANUFACTURERS_PACKAGE.replace('.', '/');
        List<Class<?>> classes = new ArrayList<>();

        // Get all resources for the package
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            System.out.println("Scanning resource: " + resource);

            if (resource.getProtocol().equals("jar")) {
                // Handle JAR file
                handleJarResource(resource, classes);
            } else {
                // Handle filesystem directory
                handleFileResource(resource, classes);
            }
        }

        if (classes.isEmpty()) {
            // Fallback: try scanning the classpath directly
            try {
                String classPath = System.getProperty("java.class.path");
                String[] classPathEntries = classPath.split(File.pathSeparator);
                for (String entry : classPathEntries) {
                    File entryFile = new File(entry);
                    if (entryFile.isFile() && entry.toLowerCase().endsWith(".jar")) {
                        handleJarFile(entryFile, classes);
                    } else if (entryFile.isDirectory()) {
                        File packageDir = new File(entryFile, path);
                        if (packageDir.exists()) {
                            handleFileResource(packageDir.toURI().toURL(), classes);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error during classpath scanning fallback");
                e.printStackTrace();
            }
        }

        return classes;
    }

    private static void handleJarResource(URL resource, List<Class<?>> classes) {
        try {
            String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));  // strip out "jar:"
            handleJarFile(new File(URLDecoder.decode(jarPath, StandardCharsets.UTF_8)), classes);
        } catch (Exception e) {
            System.err.println("Error processing JAR resource: " + resource);
            e.printStackTrace();
        }
    }

    private static void handleJarFile(File jarFile, List<Class<?>> classes) {
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            String packagePath = MANUFACTURERS_PACKAGE.replace('.', '/');

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.startsWith(packagePath) && name.endsWith(".class")) {
                    String className = name.replace('/', '.')
                            .substring(0, name.length() - 6); // remove .class
                    try {
                        Class<?> cls = Class.forName(className);
                        if (ManufacturerHandler.class.isAssignableFrom(cls) &&
                                !Modifier.isAbstract(cls.getModifiers()) &&
                                !cls.isInterface()) {
                            classes.add(cls);
                            System.out.println("Found handler class in JAR: " + className);
                        }
                    } catch (Exception e) {
                        System.err.println("Error loading class from JAR: " + className);
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing JAR file: " + jarFile);
            e.printStackTrace();
        }
    }

    private static void handleFileResource(URL resource, List<Class<?>> classes) {
        try {
            File directory = new File(URLDecoder.decode(resource.getFile(), StandardCharsets.UTF_8));
            if (!directory.exists()) {
                System.out.println("Directory does not exist: " + directory);
                return;
            }

            File[] files = directory.listFiles((dir, name) -> name.endsWith(".class"));
            if (files == null) {
                System.out.println("No class files found in directory: " + directory);
                return;
            }

            for (File file : files) {
                String className = MANUFACTURERS_PACKAGE + '.' +
                        file.getName().substring(0, file.getName().length() - 6);
                try {
                    Class<?> cls = Class.forName(className);
                    if (ManufacturerHandler.class.isAssignableFrom(cls) &&
                            !Modifier.isAbstract(cls.getModifiers()) &&
                            !cls.isInterface()) {
                        classes.add(cls);
                        System.out.println("Found handler class in filesystem: " + className);
                    }
                } catch (Exception e) {
                    System.err.println("Error loading class from filesystem: " + className);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.err.println("Error processing filesystem resource: " + resource);
            e.printStackTrace();
        }
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