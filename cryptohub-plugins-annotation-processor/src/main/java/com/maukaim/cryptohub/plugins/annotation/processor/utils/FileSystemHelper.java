package com.maukaim.cryptohub.plugins.annotation.processor.utils;

import com.maukaim.cryptohub.plugins.annotation.processor.ModuleIndexNotFoundException;
import com.maukaim.cryptohub.plugins.annotation.processor.ModuleIndexNotReadableException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Pattern;

public class FileSystemHelper {
    public static final String MODULE_INDEX_RESOURCE = "META-INF/services/com.maukaim.cryptohub.plugins.api.plugin.Module";
    private static final String WHITE_SPACE_REGEX = "\\s+";
    private static final String CLASS_NAME_REGEX = "^(?:\\w+|\\w+\\.\\w+)+$";

    public static Set<String> readModuleIndex(ClassLoader loader) throws ModuleIndexNotFoundException, ModuleIndexNotReadableException {
        InputStream resourceAsStream = loader.getResourceAsStream(MODULE_INDEX_RESOURCE) ;
        if (Objects.isNull(resourceAsStream)){
            throw new ModuleIndexNotFoundException(MODULE_INDEX_RESOURCE);
        }

        try{
            return readInputStreamLines(resourceAsStream);

        } catch (IOException e) {
            throw new ModuleIndexNotReadableException(e, MODULE_INDEX_RESOURCE);
        }
    }

    private static String noWhiteSpace(String s){
        return Pattern.compile(WHITE_SPACE_REGEX).matcher(s).replaceAll("");
    }

    public static Set<String> readInputStreamLines(InputStream stream) throws IOException{
        try(Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            try (BufferedReader bufferedReader = new BufferedReader(reader)) {
                String line;
                Set<String> lines = new HashSet<>();
                while ((line = bufferedReader.readLine()) != null) {
                    if(noWhiteSpace(line).matches(CLASS_NAME_REGEX)){
                        lines.add(line);
                    }
                }
                return lines;
            }
        }
    }

    public static void writeLinesAndClose(OutputStream out, Set<String> allModuleServiceProviders) throws IOException {
        try (out; BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
            for (String moduleServiceProviders : allModuleServiceProviders) {
                writer.write(moduleServiceProviders);
                writer.newLine();
            }
            writer.flush();
        }
    }
}
