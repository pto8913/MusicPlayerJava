package pto.Manager;

import javafx.event.EventHandler;
import pto.Controller.ListView.MusicListTypes;
import pto.Controller.ListView.MusicListTypes.MusicListMode;
import pto.Events.Delegate;
import pto.Events.MulticastDelegate;
import pto.Events.MusicListTypeChangedEvent;

public class StateManager {
    private MusicListTypes musicListTypes = new MusicListTypes();
    private MulticastDelegate<MusicListTypeChangedEvent> onMusicListTypeChanged = new MulticastDelegate<MusicListTypeChangedEvent>();

    public StateManager(){}

    public void setMusicListType(MusicListTypes inTypes)
    {
        musicListTypes = inTypes;
        onMusicListTypeChanged.Broadcast(new MusicListTypeChangedEvent(inTypes));
    }
    public MusicListTypes getMusicListType()
    {
        return musicListTypes;
    }

    public void setMusicListMode(MusicListMode inMode)
    {
        musicListTypes.mode = inMode;
        setMusicListType(musicListTypes);
    }
    public MusicListMode getMusicListMode()
    {
        return musicListTypes.mode;
    }

    public void bindOnMusicListTypeChanged(String inTag, EventHandler<MusicListTypeChangedEvent> event)
    {
        onMusicListTypeChanged.Bind(inTag, new Delegate<>(event));
    }
    public void unbindOnMusicListTypeChanged(String inTag)
    {
        onMusicListTypeChanged.Unbind(inTag);
    }
}