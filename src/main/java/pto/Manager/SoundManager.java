package pto.Manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import pto.Controller.ListView.MusicData;
import pto.Events.Delegate;
import pto.Events.MulticastDelegate;
import pto.Events.SoundPlayerEvent;
import pto.Events.SoundPlayerEvent.SoundEventTypes;
import pto.Utils.ListUtils;

public class SoundManager
{
    protected Media media = null;
    protected MediaPlayer mediaPlayer = null;
    protected Duration mediaDuration = null;

    protected List<MusicData> musicList = new ArrayList<>();
    protected int activeIndex = 0;
    protected double volume = 100.0;

    protected MulticastDelegate<SoundPlayerEvent> onSoundPlayerEvent = new MulticastDelegate<>();
    protected MusicListener musicListener = new MusicListener((long)0.1);

    public static class SoundPlayerData
    {
        public MusicData musicData = new MusicData();
        public int activeIndex = 0;
        public SoundPlayerData() {}
        public SoundPlayerData(MusicData data, int index)
        {
            musicData = data;
            activeIndex = index;
        }
    }

    public void initMusicList(List<MusicData> inMusicList)
    {
        musicList = inMusicList;
    }
    public MusicData getMusicData(int index)
    {
        return musicList.get(index);
    }
    public MusicData getMusicData(String title)
    {
        for (MusicData musicData : musicList)
        {
            if (musicData.getName().equals(title))
            {
                return musicData;
            }
        }
        return new MusicData();
    }
    public int getActiveIndex()
    {
        return activeIndex;
    }
    public void setVolume(double inVolume)
    {
        volume = inVolume;
        if (isValid())
        {
            mediaPlayer.setVolume(volume / 100);
        }
    }
    public void setSoundPlayerEvent(String tag, EventHandler<SoundPlayerEvent> event)
    {
        if (!onSoundPlayerEvent.isBound(tag))
        {
            onSoundPlayerEvent.Bind(tag, new Delegate<>(event));
        }
    }

    public MusicData Play(int index)
    {
        if (isPlaying())
        {
            if (activeIndex == index)
            {
                return new MusicData();
            }
            else
            {
                Stop();
            }
        }
        activeIndex = index;
        MusicData musicData = musicList.get(activeIndex); 
        Play(musicData.getPath());
        return musicData;
    }
    protected void Play(String filePath)
    {
        if (filePath != null)
        {
            if (media != null)
            {
                if (!media.getSource().replace("file:/", "").equals(filePath))
                {
                    media = new Media(new File(filePath).toURI().toString());
                    mediaDuration = new Duration(0);
                }
            }
            else
            {
                media = new Media(new File(filePath).toURI().toString());
            }
            mediaPlayer = null;
            mediaPlayer = new MediaPlayer(media);
        }
        if (mediaPlayer != null)
        {
            if (mediaDuration != null)
            {
                mediaPlayer.setStartTime(mediaDuration);
            }
            setVolume(volume);
            musicListener.start();
            mediaPlayer.play();
        }
    }
    public void Stop()
    {
        if (isValid() && isPlaying())
        {
            mediaPlayer.pause();
            mediaDuration = mediaPlayer.getCurrentTime();
            musicListener.stop();
        }
    }
    public void setPlayTime(double percent, boolean isPlay)
    {
        if (isValid())
        {
            Stop();
            mediaDuration = media.getDuration().multiply(percent);
            if (isPlay)
            {
                Play(null);
            }
        }
    }
    protected void End()
    {
        if (isPlaying())
        {
            Stop();
            SoundPlayerEvent event = new SoundPlayerEvent();
            event.setType(SoundEventTypes.End);
            event.setMusicTitle(musicList.get(activeIndex + 1).getName());
            onSoundPlayerEvent.Broadcast(event);
            Next();
        }
    }

    public void Jump(double seconds)
    {
        if (isValid() && isPlaying())
        {
            Stop();
            mediaDuration = mediaDuration.add(new Duration(seconds * 1000));
            Play(null);
        }
    }
    public void JumpBack(double seconds)
    {
        if (isValid() && isPlaying())
        {
            Stop();
            mediaDuration = mediaDuration.subtract(new Duration(seconds * 1000));
            Play(null);
        }
    }
    public SoundPlayerData Next()
    {
        if (isValid() && isPlaying())
        {
            Stop();
            activeIndex += 1;
            if (activeIndex > musicList.size())
            {
                activeIndex = 0;
            }
            return new SoundPlayerData(Play(activeIndex), activeIndex);
        }
        return new SoundPlayerData();
    }
    public SoundPlayerData Prev()
    {
        if (isValid() && isPlaying())
        {
            Stop();
            activeIndex -= 1;
            if (activeIndex < 0)
            {
                activeIndex = musicList.size();
            }
            return new SoundPlayerData(Play(activeIndex), activeIndex);
        }
        return new SoundPlayerData();
    }

    public boolean isPlaying()
    {
        return musicListener.running;
    }
    protected boolean isValid()
    {
        return mediaPlayer != null;
    }

    public boolean add(MusicData data)
    {
        musicList.add(data);
        //System.out.println(String.format("add file name : %s", data));
        return true;
    }
    public boolean insert(int index, MusicData data)
    {
        if (ListUtils.isValidIndex(musicList, index))
        {
            //System.out.println(String.format("insert file name : %s to index : %d", data, index));
            musicList.add(index, data);
            return true;
        }
        return false;
    }
    public boolean replace(int from, int to)
    {
        final MusicData fromData = musicList.get(from);
        final MusicData toData = musicList.get(to);
        musicList.set(to, fromData);
        musicList.set(from, toData);
        //System.out.println(String.format("replace cell!! from %d to %d.", from, to));
        return true;
    }
    public boolean remove(int index)
    {
        if (ListUtils.isValidIndex(musicList, index))
        {
            musicList.remove(index);
            return true;
        }
        return false;
    }
    public boolean append(List<MusicData> datas)
    {
        if (datas == null || datas.size() == 0)
        {
            //System.err.println("append but datas is null");
            return false;
        }
        for (MusicData data : datas)
        {
            add(data);
        }
        return true;
    }

    protected class MusicListener implements Runnable 
    {
        private Thread worker;
        private long interval = 1;
        private boolean running = false;
        private boolean forceStop = false;

        public MusicListener() {}
        public MusicListener(long interval)
        {
            this.interval = interval;
        }
        public void start()
        {
            running = true;
            worker = new Thread(this);
            worker.start();
        }
        public void stop()
        {
            running = false;
        }
        public void forceStop()
        {
            forceStop = true;
        }

        public void interrupt()
        {
            running = false;
            worker.interrupt();
        }

        public void run()
        {
            forceStop = false;
            SoundPlayerEvent event = new SoundPlayerEvent();
            event.setType(SoundEventTypes.Duration);
            Duration duration;
            while (true) {
                try
                {
                    if (running)
                    {
                        duration = mediaPlayer.getCurrentTime();

                        final double durationSeconds = duration.toSeconds();
                        event.setTime(durationSeconds);
                        final double durationPercent = durationSeconds / media.getDuration().toSeconds();
                        event.setTimePercent(durationPercent);

                        onSoundPlayerEvent.Broadcast(event);

                        if (durationPercent == 1.0)
                        {
                            forceStop();
                            Platform.runLater(
                                () -> {
                                    End();
                                }
                            );
                        }
                        else
                        {
                            worker.sleep(interval);
                        }
                    }
                    if (forceStop)
                    {
                        break;
                    }
                }
                catch (InterruptedException e)
                {
                    worker.currentThread().interrupt();
                    System.err.println("Thread was interrupted : " + e);
                }
            }
        }
    }
}
