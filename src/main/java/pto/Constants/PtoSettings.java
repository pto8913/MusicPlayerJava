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

    public static String MLCONTROLLER_MUSICLIST = "MusicListController_MusicList";
    public static String MLCONTROLLER_PLAYLIST = "MusicListController_PlayList";
    public static String MLCONTROLLER_PLAYLISTPLAY = "MusicListController_PlayListPlay";
    public static String MLCONTROLLER_ADDTOPLAYLIST = "MusicListController_AddToPlayList";

}
