package pto.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import pto.FXMLProxy;
import pto.Constants.PtoSettings;
import pto.FXMLProxy.LoadData;
import pto.Controller.ListView.MusicData;
import pto.Controller.ListView.MusicListCellFactory;
import pto.Controller.ListView.MusicListTypes.MusicListMode;
import pto.Events.MusicListTypeChangedEvent;
import pto.Manager.ControllerManager;
import pto.Manager.MusicJsonManager;
import pto.Manager.MusicManager;
import pto.Manager.StateManager;
import pto.Utils.ButtonUtils;
import pto.Utils.FileSearch;
import pto.Utils.ListUtils;

public class MusicListController
{
    // ---------------------------------------------------------
    // States
    // ---------------------------------------------------------
    @FXML
    protected ListView<MusicData> musicLinkList;
    @FXML
    protected AnchorPane menuContent;

    // ---------------------------------------------------------
    // Main Functions
    // ---------------------------------------------------------
    public MusicListController()
    {
    }
    
    @FXML
    public void initialize()
    {
        assert musicLinkList != null;

        ControllerManager.addController(this);

        LoadData loadData = FXMLProxy.loadFXML("ListMenuBar");
        addMenu(loadData.parent);

        reCreateList();

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

        StateManager.bindOnMusicListTypeChanged(
            getClass().getName(), 
            event -> {
                onMusicListTypeChanged(event);
            }
        );

        StateManager.setMusicListMode(MusicListMode.MusicList);
    }
    @Override
    public void finalize()
    {
        StateManager.unbindOnMusicListTypeChanged(getClass().getName());
    }

    public void addMenu(Node content)
    {
        menuContent.getChildren().add(content);
    }

    protected void reCreateList()
    {
        musicLinkList.getItems().clear();
        setCellFactory();
        initializeMusicList();
    }

    protected void setCellFactory()
    {
        musicLinkList.setCellFactory(new MusicListCellFactory());
    }

    protected void initializeMusicList()
    {
        try
        {
            List<MusicData> playLists = new ArrayList<>();
            switch (StateManager.getMusicListType().mode) {
                case PlayList:
                case AddToPlayList:
                    playLists = MusicJsonManager.getAllPlayList();
                    append(playLists);
                    break;
                case PlayListPlay:
                    ListMenuBarController listMenuBarController = ControllerManager.getController(ListMenuBarController.class);
                    if (listMenuBarController != null)
                    {
                        playLists = MusicJsonManager.getAllMusicList(listMenuBarController.getTitle());
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
            // reCreateList();
        }
        catch (IOException e)
        {
        }
                System.out.println(MusicManager.getMusics());

    }

    protected void onMusicListTypeChanged(MusicListTypeChangedEvent event)
    {
        reCreateList();
    }

    protected void onMusicListScrolling()
    {
        ControllerManager.closeAllFloatingController();
    }

    // ----------------------------
    // MusicList Functions
    // ----------------------------
    public void removeMusicFromList(String musicName)
    {

    }

    // ----------------------------
    // PlayList Functions
    // ----------------------------
    public void removePlayList(String playListName)
    {
        MusicData data = new MusicData(playListName);
        MusicJsonManager.removePlayList(data);
        remove(data);
        reCreateList();
    }

    // ----------------------------
    // Button Utility Functions
    // ----------------------------
    @FXML
    private void onMouseHovered(MouseEvent event)
    {
        ButtonUtils.onMouseHovered(event);
    }
    @FXML
    private void onMouseLeave(MouseEvent event)
    {
        ButtonUtils.onMouseLeave(event);
    }
    @FXML
    private void onMousePressed(MouseEvent event)
    {
        ButtonUtils.onMousePressed(event);
    }
    @FXML
    private void onMouseReleased(MouseEvent event)
    {
        ButtonUtils.onMouseReleased(event);
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
        switch (StateManager.getMusicListType().mode)
        {
            case MusicList:
                MusicManager.append(datas);
                break;
            default:
                break;
        }
        append(datas);
    }

    public boolean add(MusicData data)
    {
        musicLinkList.getItems().add(data);
        // musicLinkList.refresh();
        //System.out.println(String.format("add file name : %s", data));
        return true;
    }
    public boolean insert(int index, MusicData data)
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
        final MusicData fromData = musicLinkList.getItems().get(from);
        final MusicData toData = musicLinkList.getItems().get(to);
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
    public boolean remove(MusicData obj)
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
        MusicManager.append(datas);
        return true;
    }
}