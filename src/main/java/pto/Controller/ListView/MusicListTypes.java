package pto.Controller.ListView;

public class MusicListTypes {
    public MusicListMode mode = MusicListMode.MusicList;

    public MusicListTypes()
    {
    }

    public MusicListTypes(MusicListMode inMode)
    {
        mode = inMode;
    }

    public enum MusicListMode
    {
        MusicList, /* no button */
        AddToPlayList, /* + or x */
        PlayList, /* only e x */
        PlayListPlay, /* no button */
    }
}
