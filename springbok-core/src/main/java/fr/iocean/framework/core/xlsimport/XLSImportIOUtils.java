package fr.iocean.framework.core.xlsimport;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * May be useful in test classes
 */
public class XLSImportIOUtils {

    public static InputStream toInputStream(Class klass, String fileName) throws URISyntaxException, IOException {
        Path path = Paths.get(klass.getResource("/xlsimport").toURI()).resolve(fileName);
        return Files.newInputStream(path);
    }
}
