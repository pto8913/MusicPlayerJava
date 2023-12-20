package pto.Manager;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SoundPlayer
{
    private static Media media = null;
    private static MediaPlayer mediaPlayer = null;
    private static Duration mediaDuration = null;

    public static void Play(String filePath)
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
            mediaPlayer.play();
        }
    }

    public static void Stop()
    {
        mediaPlayer.pause();
        mediaDuration = mediaPlayer.getCurrentTime();
    }

    public static void Jump(double seconds)
    {
        Stop();
        mediaDuration = mediaDuration.add(new Duration(seconds * 1000));
        Play(null);
    }
    public static void JumpBack(double seconds)
    {
        Stop();
        mediaDuration = mediaDuration.subtract(new Duration(seconds * 1000));
        Play(null);
    }
}
