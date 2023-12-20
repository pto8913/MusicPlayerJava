package pto.Events;

import pto.Controller.ListView.MusicListTypes;

public class MusicListTypeChangedEvent extends PtoEvent
{
    private static final long serialVersionUID = 28493290L;

    private MusicListTypes type;   
    
    public MusicListTypeChangedEvent()
    {
        super();
    }
    public MusicListTypeChangedEvent(MusicListTypes inType)
    {
        super();
        type = inType;
    }

    public void setType(MusicListTypes inType)
    {
        type = inType;
    }

    public MusicListTypes getType()
    {
        return type;
    }
}
