package pto.Constants;

import java.util.Arrays;
import java.util.List;

import javafx.scene.input.DataFormat;

public class PtoSettings {
    static boolean isDebug = true;
    static List<String> extensions = Arrays.asList(".mp3", ".m4a", ".wav");

    public static final DataFormat DND_FILE_NAMES = new DataFormat("text/pto-filenames");
    public static List<String> getExtensions()
    {
        return extensions;
    }
}
