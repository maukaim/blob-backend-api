package com.maukaim.cryptohub.plugins.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class FileUtils {
    private static final String JAR_EXTENSION = "jar";
    public static final String CLASS_EXTENSION = "class";

    public static List<File> scanForJars(String uri) {
        File base = stringPathToFile(uri);

        if (base.isDirectory()) {
            List<File> result = new ArrayList<>();

            List<File> allFiles = List.of(Objects.requireNonNull(base.listFiles()));
            for (File f : allFiles) {
                List<File> jars = scanForJars(f.toString());
                result.addAll(jars);
            }
            return result;
        } else {
            String extension = FilenameUtils.getExtension(base.toString());
            if (extension.equalsIgnoreCase(JAR_EXTENSION)) {
                return List.of(base);
            }else{
                return Collections.emptyList();
            }
        }
    }

    public static File stringPathToFile(String uri){
        return new File(uri);
    }
}
