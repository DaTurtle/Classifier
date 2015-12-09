package module6;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Jan-Willem on 9-12-2015.
 */
public class Utils {
    public static final Charset CHARSET = Charset.defaultCharset();

    public static String readFile(String path)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, CHARSET);
    }
}
