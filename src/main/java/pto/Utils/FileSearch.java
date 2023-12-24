package pto.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileSearch
{
    /*
    * @directoryPath : like C:/music
    * @extension : e.g. mp4, mp3, exe... 
        allow ".mp4"
    */
    public static List<File> search(String directoryPath, List<String> extensions)
    {
        List<File> files = new ArrayList<>();
        for (String extension : extensions)
        {
            search(files, directoryPath, extension);
        }
        return files;// new ArrayList<>(files);
    }
    /*
    * @directoryPath : like C:/music
    * @extension : e.g. mp4, mp3, exe... 
        allow ".mp4"
    */
    private static void search(List<File> files, String directoryPath, String extension)
    {
        //System.out.println(String.format("FileSearch Directory : %s", directoryPath));
        final File directory = new File(directoryPath);
        if (!directory.isDirectory())
        {
            throw new IllegalArgumentException("directoryPath " + directoryPath + " is not directory.");
        }

        final String actualExtension = extension.replace(".", "");
        final File[] allFiles = directory.listFiles();
        if (allFiles != null)
        {
            //System.out.println(String.format("actual extension : %s", actualExtension));
            for (File file : allFiles)
            {
                if (file.isDirectory())
                {
                    search(files, file.getAbsolutePath(), extension);
                }
                else if (contain(file, actualExtension))
                {
                    //System.out.println(String.format("filename add : %s", file.getName()));
                    files.add(file);
                }
            }
        }
    }
    private static String getRegex(String extension)
    {
        return ".+\\." + extension + "$";
    }

    public static boolean contain(File file, String extension)
    {
        final String regex = getRegex(extension);
        return file.getName().matches(regex);
    }
}
