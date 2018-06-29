package io.nano.time;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class NativeLoader {

    private NativeLoader() {
        // can't touch this
    }

    public static void loadNativeLibrary(String name) {
        String libraryName = getLibraryName(name);
        InputStream inputStream = NativeLoader.class.getResourceAsStream(libraryName);
        if (inputStream == null) {
            throw new IllegalArgumentException("Library named '" + libraryName + "' not found in classpath");
        }

        // could extract library to java.library.path - more complex due to permissions, multiple paths etc

        //extract by copying to tmp - for now
        Path targetFile = getTargetFileName(libraryName);
        copyAndLoad(inputStream, targetFile);
    }

    private static void copyAndLoad(InputStream inputStream, Path target) {
        String targetFile = target.normalize().toString();

        // check if file already exists
        if (Files.notExists(target)) {
            // create the necessary parent directories
            try {
                Files.createDirectories(target.getParent());
            } catch (IOException e) {
                throw new IllegalArgumentException("Error creating directories " + target, e);
            }

            try {
                Files.copy(inputStream, target);
            } catch (IOException e) {
                throw new IllegalArgumentException("Error copying library to " + targetFile, e);
            }
        }

        System.out.println("loading " + targetFile);
        System.load(targetFile);
    }

    private static Path getTargetFileName(String name) {
        String tmpPath = System.getProperty("java.io.tmpdir");
        return Paths.get(tmpPath, name);
    }

    /**
     * Currently prefixes name with "/lib/" and adds the suffix .so if required - only works on linux.
     * Future enhancements could include os.name and os.arch.
     */
    private static String getLibraryName(String name) {
        String libraryName = "/lib/lib" + name;
        if (!libraryName.endsWith(".so")) {
            libraryName += ".so";
        }
        return libraryName;
    }
}
