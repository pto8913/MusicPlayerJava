package pto.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pto.Controller.ListView.MusicData;

public class ListUtils {
    public static boolean isValidIndex(List<?> list, int index)
    {
        System.out.println(String.format("isValidIndex size %d > index %d", list.size(), index));
        return list.size() > index;
    }

    public static List<MusicData> StringToMusicList(Collection<String> keys)
    {
        List<MusicData> out = new ArrayList<>();
        for (String elem : keys)
        {
            out.add(new MusicData(elem));
        }
        return out;
    }
    public static List<MusicData> toMusicList(Collection<MusicData> datas)
    {
        List<MusicData> out = new ArrayList<>();
        for (MusicData elem : datas)
        {
            out.add(elem);
        }
        return out;
    }
}
