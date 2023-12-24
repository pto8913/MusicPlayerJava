package pto.Manager;

import javafx.stage.Stage;

public class AppInstance
{
    protected static AppInstance instance;
    protected SoundManager soundPlayer;
    protected StateManager stateManager;
    protected ControllerManager controllerManager;
    protected MusicJsonManager musicJsonManager;
    protected Stage mainWindow;

    protected AppInstance()
    {
        soundPlayer = new SoundManager();
        stateManager = new StateManager();
        controllerManager = new ControllerManager();
        musicJsonManager = new MusicJsonManager();
    }
    @Override
    public void finalize()
    {
        mainWindow.close();
    }

    public static AppInstance get()
    {
        AppInstance out = instance;
        if (out == null)
        {
            return createInstance();
        }
        return out;
    }
    protected static AppInstance createInstance()
    {
        if (instance == null)
        {
            instance = new AppInstance();
        }
        return instance;
    }

    public SoundManager getSoundManager()
    {
        return soundPlayer;
    }
    public StateManager getStateManager()
    {
        return stateManager;
    }
    public ControllerManager getControllerManager()
    {
        return controllerManager;
    }
    public MusicJsonManager getMusicJsonManager()
    {
        return musicJsonManager;
    }

    public void setMainWindow(Stage inMainWindow)
    {
        if (mainWindow == null)
        {
            mainWindow = inMainWindow;
        }
    }
    public Stage getMainWindow()
    {
        return mainWindow;
    }

}