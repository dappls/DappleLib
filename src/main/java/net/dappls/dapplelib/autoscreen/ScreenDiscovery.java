package net.dappls.dapplelib.autoscreen;

import net.minecraft.screen.ScreenHandler;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public final class ScreenDiscovery {

    private static final String SCREEN_ANNOTATION_DESC = Type.getDescriptor(Screen.class);

    private ScreenDiscovery() {}

    public static List<DiscoveredScreen> discover(String basePackage, String modId) {
        List<Class<?>> candidates = findClassesInPackage(basePackage);

        List<DiscoveredScreen> result = new ArrayList<>();
        for (Class<?> clazz : candidates) {
            Screen annotation = clazz.getAnnotation(Screen.class);
            if (annotation == null) continue;

            if (!ScreenHandler.class.isAssignableFrom(clazz)) {
                throw new IllegalStateException("@Screen must be on a ScreenHandler class: " + clazz.getName());
            }

            @SuppressWarnings("unchecked")
            Class<? extends ScreenHandler> handlerClass = (Class<? extends ScreenHandler>) clazz;

            result.add(new DiscoveredScreen(
                    modId,
                    annotation.id(),
                    handlerClass,
                    annotation.screen()
            ));
        }

        return result;
    }

    private static List<Class<?>> findClassesInPackage(String basePackage) {
        String packagePath = basePackage.replace('.', '/');
        List<Class<?>> classes = new ArrayList<>();

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            var resources = classLoader.getResources(packagePath);

            while (resources.hasMoreElements()) {
                var resource = resources.nextElement();
                URI uri = resource.toURI();

                if (uri.getScheme().equals("jar")) {
                    classes.addAll(scanJar(uri, packagePath, classLoader));
                } else {
                    classes.addAll(scanDirectory(Paths.get(uri), packagePath, classLoader));
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to scan package: " + basePackage, e);
        }

        return classes;
    }

    private static List<Class<?>> scanDirectory(Path directory, String packagePath, ClassLoader classLoader) throws IOException {
        List<Class<?>> classes = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(directory)) {
            paths.filter(path -> path.toString().endsWith(".class"))
                    .forEach(path -> {
                        try {
                            String relativePath = directory.relativize(path).toString();
                            String className = packagePath + "/" + relativePath;
                            className = className.replace('/', '.').replace('\\', '.');
                            className = className.substring(0, className.length() - 6);

                            if (hasScreenAnnotation(path)) {
                                Class<?> clazz = classLoader.loadClass(className);
                                classes.add(clazz);
                            }
                        } catch (ClassNotFoundException | IOException e) {
                            // Skip classes that can't be loaded
                        }
                    });
        }

        return classes;
    }

    private static List<Class<?>> scanJar(URI jarUri, String packagePath, ClassLoader classLoader) throws IOException {
        List<Class<?>> classes = new ArrayList<>();

        String[] parts = jarUri.toString().split("!");

        try (FileSystem fs = FileSystems.newFileSystem(URI.create(parts[0]), Collections.emptyMap())) {
            Path root = fs.getPath(packagePath);
            if (!Files.exists(root)) {
                return classes;
            }

            try (Stream<Path> paths = Files.walk(root)) {
                paths.filter(path -> path.toString().endsWith(".class"))
                        .forEach(path -> {
                            try {
                                String pathStr = path.toString();
                                if (pathStr.startsWith("/")) {
                                    pathStr = pathStr.substring(1);
                                }
                                String className = pathStr.replace('/', '.').replace('\\', '.');
                                className = className.substring(0, className.length() - 6);

                                if (hasScreenAnnotation(path)) {
                                    Class<?> clazz = classLoader.loadClass(className);
                                    classes.add(clazz);
                                }
                            } catch (ClassNotFoundException | IOException e) {
                                // Skip classes that can't be loaded
                            }
                        });
            }
        }

        return classes;
    }

    private static boolean hasScreenAnnotation(Path classFile) throws IOException {
        try (InputStream is = Files.newInputStream(classFile)) {
            ClassReader reader = new ClassReader(is);
            ClassNode node = new ClassNode();
            reader.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            if (node.visibleAnnotations == null) {
                return false;
            }

            for (AnnotationNode ann : node.visibleAnnotations) {
                if (ann.desc.equals(SCREEN_ANNOTATION_DESC)) {
                    return true;
                }
            }
        }
        return false;
    }
}

