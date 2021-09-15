package com.maukaim.cryptohub.plugins.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class FileSystemUtils {
    private static final String JAR_EXTENSION = "jar";
    private static final String WHITE_SPACE_REGEX = "\\s+";
    private static final String ENABLED_PLUGINS_INDEX_NAME = "enabled-plugins.idx";

    public static Set<File> scanForJars(List<Path> paths) {
        return paths.stream()
                .map(FileSystemUtils::scanForJars)
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

    public static Set<Path> getEnabledPluginFilesPaths(Path path) {
        if (Files.isDirectory(path)) {
            return Stream.of(Objects.requireNonNull(new File(path.toUri()).listFiles()))
                    .map(file -> getEnabledPluginFilesPaths(file.toPath()))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());
        } else {
            if (path.endsWith(ENABLED_PLUGINS_INDEX_NAME)) {
                return Set.of(path);
            }
            return Collections.emptySet();
        }
    }

    public static Set<String> getEnabledPluginIds(Path path) {
        Set<Path> enabledPluginFilesPaths = getEnabledPluginFilesPaths(path);
        return enabledPluginFilesPaths.stream()
                .map(p -> {
                    try {
                        List<String> allLines = Files.readAllLines(p);
                        return allLines.stream().map(FileSystemUtils::noWhiteSpace).collect(Collectors.toSet());
                    } catch (IOException e) {
                        return Collections.<String>emptySet();
                    }
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

    }

    private static String noWhiteSpace(String s) {
        return Pattern.compile(WHITE_SPACE_REGEX).matcher(s).replaceAll("");
    }

    public static void writeEnabledPluginIfAbsent(String pluginId, Path pluginDirectoryPath) {
        if (Files.isDirectory(pluginDirectoryPath) || pluginDirectoryPath.endsWith(ENABLED_PLUGINS_INDEX_NAME)) {
            Path enabledPluginsFilePath;
            if (Files.isDirectory(pluginDirectoryPath)) {
                enabledPluginsFilePath = pluginDirectoryPath.resolve(ENABLED_PLUGINS_INDEX_NAME);
            } else {
                enabledPluginsFilePath = pluginDirectoryPath;
            }
            if (Files.notExists(enabledPluginsFilePath)) {
                try {
                    Files.createFile(enabledPluginsFilePath);
                } catch (IOException ignored) {
                }
            }
            appendEnabledPluginIfAbsent(pluginId, enabledPluginsFilePath);
        }
    }

    private static void appendEnabledPluginIfAbsent(String pluginId, Path autoEnableFile) {
        Set<String> pluginIdsEnabled = getEnabledPluginIds(autoEnableFile);
        if (!pluginIdsEnabled.contains(pluginId)) {
            try (BufferedWriter bufferedWriter = Files.newBufferedWriter(autoEnableFile)) {
                bufferedWriter.newLine();
                bufferedWriter.append(pluginId);
            } catch (IOException ignored) {
            }
        }

    }

    public static void removePluginIdFromFile(String pluginId, Path filePath) {
        try {
            List<String> lines = Files.readAllLines(filePath);
            Set<String> linesRemaining = lines.stream()
                    .map(FileSystemUtils::noWhiteSpace)
                    .filter(line -> !line.equalsIgnoreCase(pluginId))
                    .collect(Collectors.toSet());
            if(!linesRemaining.containsAll(lines)){
                Files.write(filePath,linesRemaining);
            }
        } catch (IOException e) {
            log.warn("Impossible to read or write on {}", filePath.toString());
        }
    }
}
