package pto.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static boolean isExistsPath(String path)
    {
        return Files.exists(Paths.get(path));
    }

    public static String getCurrentPath()
    {
        try
        {
            return new java.io.File(".").getCanonicalPath();
        }
        catch (IOException e)
        {
            System.err.println(e);
        }
        return "";
    } 
}
