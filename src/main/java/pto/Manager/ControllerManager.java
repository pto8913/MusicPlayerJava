package pto.Manager;

import java.util.HashMap;

import pto.FXMLProxy;
import pto.FXMLProxy.LoadData;
import pto.Constants.PtoSettings;
import pto.Controller.IFloatingController;
import pto.Controller.MainController;
import pto.Controller.MusicListController;
import pto.Controller.ListView.MusicListTypes;

public class ControllerManager
{
    private HashMap<String, Object> controllers = new HashMap<String, Object>();

    public void addController(Object controller)
    {
        controllers.put(controller.getClass().getName(), controller);
    }
    public <T> T getController(Class type)
    {
        return (T)controllers.get(type.getName());
    }
    public void removeController(Class type)
    {
        controllers.remove(type.getName());
    }
    public void removeController(Object controller)
    {
        controllers.remove(controller.getClass().getName());
    }

    public void addController(String inTag, Object controller)
    {
        controllers.put(inTag, controller);
    }
    public <T> T getController(String inTag)
    {
        return (T)controllers.get(inTag);
    }
    public void removeController(String inTag)
    {
        controllers.remove(inTag);
    }

    public void closeAllFloatingController()
    {
        for (Object obj : controllers.values())
        {
            if (IFloatingController.class.isInstance(obj))
            {
                IFloatingController floatingController = (IFloatingController)obj;
                if (floatingController.isIgnoreClose())
                {
                    continue;
                }
                floatingController.playCloseAnimation();
            }
        }
    }

    public MusicListController getMusicListController()
    {
        switch (AppInstance.get().getStateManager().getMusicListMode())
        {
            case PlayListPlay:
                return AppInstance.get().getControllerManager().getController(PtoSettings.MLCONTROLLER_PLAYLISTPLAY);
            case PlayList:
                return AppInstance.get().getControllerManager().getController(PtoSettings.MLCONTROLLER_PLAYLIST);
            case AddToPlayList:
                return AppInstance.get().getControllerManager().getController(PtoSettings.MLCONTROLLER_ADDTOPLAYLIST);
            default:
                break;
        }
        return AppInstance.get().getControllerManager().getController(PtoSettings.MLCONTROLLER_MUSICLIST);
    }
    public void openMusicListController(String tag, MusicListTypes.MusicListMode mode)
    {
        MusicListController playListController = AppInstance.get().getControllerManager().getController(tag);
        if (playListController == null)
        {
            LoadData loadData = FXMLProxy.loadFXML("MusicList", new MusicListTypes(mode));
            playListController = loadData.fxmlLoader.getController();
            AppInstance.get().getControllerManager().addController(tag, playListController);
            
            MainController mainController = AppInstance.get().getControllerManager().getController(MainController.class);
            if (!mainController.containMainContent(loadData.parent))
            {
                mainController.addMainContent(loadData.parent);
                loadData.parent.setTranslateX(playListController.getCloseTranslate());
            }
        }
        if (!playListController.isOpen())
        {
            AppInstance.get().getControllerManager().closeAllFloatingController();
            playListController.playOpenAnimation();
            AppInstance.get().getStateManager().setMusicListMode(mode);
        }
    }
}
