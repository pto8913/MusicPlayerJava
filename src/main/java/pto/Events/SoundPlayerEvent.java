package pto.Events;

import pto.Controller.ListView.MusicData;

public class SoundPlayerEvent extends PtoEvent
{
    private static final long serialVersionUID = 28493290L;
    private SoundEventTypes type;
    private double time;
    private double timePercent;
    private String nextMusicTitle;

    public enum SoundEventTypes
    {
        End,
        Pause,
        Play,
        NextMusic,
        PrevMusic,
        Duration,
    }

    public SoundPlayerEvent()
    {
        super();
    }
    public SoundPlayerEvent(SoundEventTypes type)
    {
        super();
        this.type = type;
    }

    public SoundEventTypes getType()
    {
        return type;
    }
    public void setType(SoundEventTypes type)
    {
        this.type = type;
    }

    public double getTime()
    {
        return time;
    }
    public void setTime(double time)
    {
        this.time = time;
    }

    public double getTimePercent()
    {
        return timePercent;
    }
    public void setTimePercent(double timePercent)
    {
        this.timePercent = timePercent;
    }

    public String getMusicTitle()
    {
        return nextMusicTitle;
    }
    public void setMusicTitle(String nextMusicTitle)
    {
        this.nextMusicTitle = nextMusicTitle;
    }
}
