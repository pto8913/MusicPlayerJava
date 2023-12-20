package pto.Manager;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pto.Controller.ListView.MusicData;

public class MusicManager {
    private static Set<MusicData> musics = new HashSet<>();

    public static void add(MusicData data)
    {
        musics.add(data);
    }
    public static void append(List<MusicData> data)
    {
        musics.addAll(data);
    }
    public static void appendFiles(List<File> files)
    {
        for (File file : files)
        {
            musics.add(new MusicData(file));
        }
    }
    public static void remove(MusicData data)
    {
        musics.remove(data);
    }
    public static List<MusicData> getMusics()
    {
        MusicData[] out = {};
        musics.toArray(out);
        return Arrays.asList(out);
    }
    public static MusicData getMusic(final String title)
    {
        for (MusicData music : musics)
        {
            if (music.getName().equals(title))
            {
                return music;
            }
        }
        return new MusicData();
    }
}
