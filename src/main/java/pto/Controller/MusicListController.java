package pto.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import pto.Constants.PtoSettings;
import pto.Controller.ListView.MusicData;
import pto.Controller.ListView.MusicListCellFactory;
import pto.Controller.ListView.MusicListTypes;
import pto.Manager.AppInstance;
import pto.Utils.FileSearch;
import pto.Utils.ListUtils;

/*
 * Create per MusicListTypes.MusicListMode
 */
public class MusicListController implements IFloatingController
{
    // ---------------------------------------------------------
    // States
    // ---------------------------------------------------------
    @FXML
    protected AnchorPane listPane;
    @FXML
    protected ListView<String> musicLinkList;

    protected MusicListTypes cellTypes;

    // ---------------------------------------------------------
    // Main Functions
    // ---------------------------------------------------------
    public MusicListController(MusicListTypes cellTypes)
    {
        this.cellTypes = cellTypes;
    }
    
    @FXML
    public void initialize()
    {
        assert musicLinkList != null;

        musicLinkList.setOnScroll(
            event -> {
                onMusicListScrolling();
            }
        );
        musicLinkList.setOnScrollStarted(
            event -> {
                onMusicListScrolling();
            }
        );
        musicLinkList.setOnScrollFinished(
            event -> {
                onMusicListScrolling();
            }
        );

        reCreateList();
    }

    protected void reCreateList()
    {
        musicLinkList.getItems().clear();
        setCellFactory();
        initializeMusicList();
    }

    public void setCellTypes(MusicListTypes cellTypes)
    {
        this.cellTypes = cellTypes;
        reCreateList();
    }
    protected void setCellFactory()
    {
        musicLinkList.setCellFactory(new MusicListCellFactory(cellTypes));
    }

    protected void initializeMusicList()
    {
        try
        {
            List<MusicData> playLists = new ArrayList<>();
            switch (cellTypes.mode) {
                case PlayList:
                case AddToPlayList:
                    playLists = AppInstance.get().getMusicJsonManager().getAllPlayList();
                    append(playLists);
                    break;
                case PlayListPlay:
                    ListMenuBarController listMenuBarController = AppInstance.get().getControllerManager().getController(ListMenuBarController.class);
                    if (listMenuBarController != null)
                    {
                        playLists = AppInstance.get().getMusicJsonManager().getAllMusicList(listMenuBarController.getPureTitle());
                        append(playLists);
                    }
                    break;
                case MusicList:
                    String userName = System.getProperty("user.name");
                    // String userName = new com.sun.security.auth.module.NTSystem().getName();
                    appendFiles(FileSearch.search(String.format("C:/Users/%s/music", userName), PtoSettings.getExtensions()));
                    break;
                default:
                    break;
            }
        }
        catch (IOException e)
        {
        }
    }

    protected void onMusicListScrolling()
    {
        AppInstance.get().getControllerManager().closeAllFloatingController();
    }

    // ----------------------------
    // PlayList Functions
    // ----------------------------
    public void removePlayList(String playListName)
    {
        MusicData data = new MusicData(playListName);
        AppInstance.get().getMusicJsonManager().removePlayList(data);
        reCreateList();
    }
    // ---------------------------------------------------------
    // IFloatingController Functions
    // ---------------------------------------------------------
    public double getOpenTranslate()
    {
        return 0;//parent.getWidth() - listPane.getPrefWidth();
    }
    public double getCloseTranslate()
    {
        return -listPane.getPrefWidth();
    }
    @Override
    public boolean isOpen()
    {
        return listPane.getTranslateX() == getOpenTranslate();
    }
    @Override
    public void playOpenAnimation()
    {
        TranslateTransition openNav = new TranslateTransition(new Duration(200), listPane);
        openNav.setToX(getOpenTranslate());
        openNav.play();

        switch (cellTypes.mode) {
            case AddToPlayList:
                reCreateList();
                break;
            default:
                break;
        }
    }
    @Override
    public void playCloseAnimation()
    {
        TranslateTransition closeNav = new TranslateTransition(new Duration(100), listPane);
        closeNav.setToX(getCloseTranslate());
        closeNav.play();
    }
    @Override
    public boolean isIgnoreClose()
    {
        switch (cellTypes.mode)
        {
            case MusicList:
                return true;
            default:
                break;
        }
        return false;
    }

    // ---------------------------------------------------------
    // Utility Functions
    // ---------------------------------------------------------
    protected void appendFiles(List<File> files)
    {
        List<MusicData> datas = new ArrayList<>();
        for (File file : files)
        {
            datas.add(new MusicData(file));
        }
        append(datas);
    }
    public boolean add(MusicData data)
    {
        musicLinkList.getItems().add(data.getName());
        // musicLinkList.refresh();
        //System.out.println(String.format("add file name : %s", data));
        return true;
    }
    public boolean insert(int index, String data)
    {
        if (ListUtils.isValidIndex(musicLinkList.getItems(), index))
        {
            //System.out.println(String.format("insert file name : %s to index : %d", data, index));
            musicLinkList.getItems().add(index, data);
            musicLinkList.refresh();
            return true;
        }
        return false;
    }
    public boolean replace(int from, int to)
    {
        final String fromData = musicLinkList.getItems().get(from);
        final String toData = musicLinkList.getItems().get(to);
        musicLinkList.getItems().set(to, fromData);
        musicLinkList.getItems().set(from, toData);
        //System.out.println(String.format("replace cell!! from %d to %d.", from, to));
        musicLinkList.refresh();
        return true;
    }
    public boolean remove(int index)
    {
        if (ListUtils.isValidIndex(musicLinkList.getItems(), index))
        {
            musicLinkList.getItems().remove(index);
            musicLinkList.refresh();
            return true;
        }
        return false;
    }
    public boolean remove(String obj)
    {
        if (musicLinkList.getItems().contains(obj))
        {
            musicLinkList.getItems().remove(obj);
            musicLinkList.refresh();
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

        switch (cellTypes.mode)
        {
            case MusicList:
            case PlayListPlay:
                AppInstance.get().getSoundManager().initMusicList(datas);
                break;
            default:
                break;
        }
        return true;
    }
}