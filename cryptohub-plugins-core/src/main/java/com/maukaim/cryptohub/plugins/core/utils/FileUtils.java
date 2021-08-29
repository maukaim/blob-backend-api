package com.maukaim.cryptohub.plugins.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FileUtils {
    private static final String JAR_EXTENSION = "jar";

    public static Set<File> scanForJars(List<Path> paths) {
        return paths.stream()
                .map(FileUtils::scanForJars)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public static Set<File> scanForJars(Path path) {
        File base = stringPathToFile(path.toUri());

        if (base.isDirectory()) {
            Set<File> result = new HashSet<>();

            Set<File> allFiles = Set.of(Objects.requireNonNull(base.listFiles()));
            for (File f : allFiles) {
                Set<File> jars = scanForJars(f.toPath());
                result.addAll(jars);
            }
            return result;
        } else {
            String extension = FilenameUtils.getExtension(base.toString());
            if (extension.equalsIgnoreCase(JAR_EXTENSION)) {
                return Set.of(base);
            } else {
                return Collections.emptySet();
            }
        }
    }

    public static File stringPathToFile(URI uri) {
        return new File(uri);
    }
}
