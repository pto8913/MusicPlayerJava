package pto.Controller.ListView;

import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import pto.FXMLProxy;
import pto.FXMLProxy.LoadData;
import pto.Controller.ListMenuBarController;
import pto.Controller.MusicListController;
import pto.Controller.UserController;
import pto.Controller.ListView.MusicListTypes.MusicListMode;
import pto.Events.MusicListTypeChangedEvent;
import pto.Manager.ControllerManager;
import pto.Manager.MusicJsonManager;
import pto.Manager.MusicManager;
import pto.Manager.StageManager;
import pto.Manager.StateManager;
import pto.Utils.ButtonUtils;

public class MusicListCellController
{
    @FXML
    private AnchorPane entire;
    @FXML
    private Label musicTitle;

    @FXML
    private Button addToPlayListButton;
    @FXML
    private Button removeButton;

    public MusicListCellController(MusicData inData)
    {
        FXMLProxy.loadFXML("MusicListCell", this);
        setTitle(inData.getName());

        StateManager.bindOnMusicListTypeChanged(
            getClass().getName(), 
            event -> {
                onMusicListTypeChanged(event);
            }
        );
        onMusicListTypeChanged(new MusicListTypeChangedEvent(new MusicListTypes(StateManager.getMusicListType().mode)));
    }
    @Override
    public void finalize()
    {
        StateManager.unbindOnMusicListTypeChanged(getClass().getName());
    }

    public void onMusicListTypeChanged(MusicListTypeChangedEvent event)
    {
        MusicListTypes cellTypes = event.getType();
        switch (cellTypes.mode)
        {
            case MusicList:
                addToPlayListButton.setVisible(true);
                removeButton.setVisible(false);
                break;
            case PlayList:
                addToPlayListButton.setVisible(false);
                removeButton.setVisible(true);
                break;
            case AddToPlayList:
                ListMenuBarController listMenuBarController = ControllerManager.getController(ListMenuBarController.class);
                final boolean isMusicInPlayList = MusicJsonManager.containMusicInPlayList(getTitle(), listMenuBarController.getTitle());
                addToPlayListButton.setVisible(!isMusicInPlayList);
                removeButton.setVisible(isMusicInPlayList);
                break;
            case PlayListPlay:
                addToPlayListButton.setVisible(false);
                removeButton.setVisible(false);
                break;
            default:
                addToPlayListButton.setVisible(true);
                removeButton.setVisible(false);
                break;
        }
    }
    
    public void setTitle(String title)
    {
        musicTitle.setText(title);
    }
    public String getTitle()
    {
        return musicTitle.getText();
    }

    public AnchorPane getEntire()
    {
        return entire;
    }

    // ----------------------------
    // Cell clicked Functions
    // ----------------------------
    @FXML
    private void onClickedCell(MouseEvent event)
    {
        switch (StateManager.getMusicListType().mode)
        {
            case MusicList:
            case PlayListPlay:
                onClickedCellForPlay();
                break;
            case PlayList:
                onClickedCellForPlayList();
                break;
            default:
                break;
        }
    }

    private void onClickedCellForPlay()
    {
        UserController userController = ControllerManager.getController(UserController.class);
        if (userController == null)
        {
            LoadData loadData = FXMLProxy.loadFXML("UserControl");
            userController = loadData.fxmlLoader.getController();
            ControllerManager.addController(userController);
            
            final Stage mainStage = StageManager.getStage(StageManager.Main);
            final Pane rootNode = (Pane)mainStage.getScene().getRoot();
            rootNode.getChildren().add(loadData.parent);
            loadData.parent.setTranslateY(rootNode.getHeight());
        }
        if (!userController.isOpen())
        {
            userController.playOpenAnimation();
        }
        else
        {
            if (userController.getMusicData().getName().equals(musicTitle.getText()))
            {
                userController.playCloseAnimation();        
            }
        }
        userController.setMusicData(MusicManager.getMusic(musicTitle.getText()));
    }
    private void onClickedCellForPlayList()
    {
        ListMenuBarController controller = ControllerManager.getController(ListMenuBarController.class);
        if (controller == null)
        {
            return;
        }
        controller.setTitle(getTitle());
        StateManager.setMusicListMode(MusicListMode.PlayListPlay);
    }

    // ----------------------------
    // Cell Button Functions
    // ----------------------------
    @FXML
    private void onClickedAddToPlayListButton(ActionEvent event) throws FileNotFoundException
    {
        ListMenuBarController listMenuBarController = ControllerManager.getController(ListMenuBarController.class);
        switch (StateManager.getMusicListType().mode)
        {
        case MusicList:
            listMenuBarController.setTitle(getTitle());
            StateManager.setMusicListMode(MusicListMode.AddToPlayList);
            break;
        case AddToPlayList:
            final String musicTitle = listMenuBarController.getTitle();
            final MusicData musicData = MusicManager.getMusic(musicTitle);
            MusicJsonManager.musicAddToPlayList(getTitle(), musicData);
            toggleButtonVisibility();
            break;
        default:
            break;
        }
        ControllerManager.closeAllFloatingController();
    }
    @FXML
    private void onClickedRemoveButton(ActionEvent event) throws FileNotFoundException
    {
        switch (StateManager.getMusicListType().mode)
        {
        case PlayList:
            MusicListController musicListController = ControllerManager.getController(MusicListController.class);
            musicListController.removePlayList(musicTitle.getText());
            break;
        case AddToPlayList:
            ListMenuBarController listMenuBarController = ControllerManager.getController(ListMenuBarController.class);
            final String musicTitle = listMenuBarController.getTitle();
            final MusicData musicData = MusicManager.getMusic(musicTitle);
            MusicJsonManager.musicRemoveFromPlayList(getTitle(), musicData);
            toggleButtonVisibility();
            break;
        default:
            break;
        }
        ControllerManager.closeAllFloatingController();
    }
    public void toggleButtonVisibility()
    {
        addToPlayListButton.setVisible(!addToPlayListButton.isVisible());
        removeButton.setVisible(!removeButton.isVisible());
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
}
