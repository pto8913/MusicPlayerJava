package pto.Manager;

import javafx.event.EventHandler;
import pto.Controller.ListView.MusicListTypes;
import pto.Controller.ListView.MusicListTypes.MusicListMode;
import pto.Events.Delegate;
import pto.Events.MulticastDelegate;
import pto.Events.MusicListTypeChangedEvent;

public class StateManager {
    private static MusicListTypes musicListTypes = new MusicListTypes();
    private static MulticastDelegate<MusicListTypeChangedEvent> onMusicListTypeChanged = new MulticastDelegate<MusicListTypeChangedEvent>();

    private StateManager(){}

    public static void setMusicListType(MusicListTypes inTypes)
    {
        musicListTypes = inTypes;
        onMusicListTypeChanged.Broadcast(new MusicListTypeChangedEvent(inTypes));
    }
    public static MusicListTypes getMusicListType()
    {
        return musicListTypes;
    }

    public static void setMusicListMode(MusicListMode inMode)
    {
        musicListTypes.mode = inMode;
        setMusicListType(musicListTypes);
    }

    public static void bindOnMusicListTypeChanged(String inTag, EventHandler<MusicListTypeChangedEvent> event)
    {
        Delegate<MusicListTypeChangedEvent> delegate = new Delegate<MusicListTypeChangedEvent>();
        delegate.Bind(event);
        onMusicListTypeChanged.Bind(inTag, delegate);
    }
    public static void unbindOnMusicListTypeChanged(String inTag)
    {
        onMusicListTypeChanged.Unbind(inTag);
    }
}