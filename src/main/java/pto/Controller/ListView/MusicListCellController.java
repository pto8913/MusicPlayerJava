package pto.Controller.ListView;

import java.io.FileNotFoundException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import pto.FXMLProxy;
import pto.Constants.PtoSettings;
import pto.Controller.ListMenuBarController;
import pto.Controller.MusicListController;
import pto.Controller.ListView.MusicListTypes.MusicListMode;
import pto.Manager.AppInstance;
import pto.Utils.ButtonUtils;

public class MusicListCellController
{
    @FXML
    protected AnchorPane entire;
    @FXML
    protected Label musicTitle;

    @FXML
    protected Button addToPlayListButton;
    @FXML
    protected Button removeButton;

    protected int index;
    protected MusicListTypes cellTypes;

    public MusicListCellController(String title, MusicListTypes cellTypes)
    {
        this.cellTypes = cellTypes;

        FXMLProxy.loadFXML("MusicListCell", this);
        setTitle(title);

        setMusicListType(cellTypes);
    }

    public void setIndex(int index)
    {
        this.index = index;
    }
    public int getIndex()
    {
        return index;
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

    public void setMusicListType(MusicListTypes cellTypes)
    {
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
                ListMenuBarController listMenuBarController = AppInstance.get().getControllerManager().getController(ListMenuBarController.class);
                final boolean isMusicInPlayList = AppInstance.get().getMusicJsonManager().containMusicInPlayList(getTitle(), listMenuBarController.getPureTitle());
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

    // ----------------------------
    // Cell Button Functions
    // ----------------------------
    @FXML
    private void onClickedAddToPlayListButton(ActionEvent event) throws FileNotFoundException
    {
        ListMenuBarController listMenuBarController = AppInstance.get().getControllerManager().getController(ListMenuBarController.class);
        switch (cellTypes.mode)
        {
        case MusicList:
            listMenuBarController.setTitle(getTitle());
            /* Initialize MusicListController_AddToPlayList */
            {
                AppInstance.get().getControllerManager().openMusicListController(PtoSettings.MLCONTROLLER_ADDTOPLAYLIST, MusicListTypes.MusicListMode.AddToPlayList);
            }
            AppInstance.get().getStateManager().setMusicListMode(MusicListMode.AddToPlayList);
            break;
        case AddToPlayList:
            AppInstance.get().getMusicJsonManager().musicAddToPlayList(
                getTitle(),
                AppInstance.get().getSoundManager().getMusicData(listMenuBarController.getPureTitle())
            );
            toggleButtonVisibility();
            break;
        default:
            break;
        }
    }
    @FXML
    private void onClickedRemoveButton(ActionEvent event) throws FileNotFoundException
    {
        switch (cellTypes.mode)
        {
        case PlayList:
            MusicListController musicListController = AppInstance.get().getControllerManager().getMusicListController();
            musicListController.removePlayList(musicTitle.getText());
            break;
        case AddToPlayList:
            AppInstance.get().getMusicJsonManager().musicRemoveFromPlayList(
                getTitle(), 
                AppInstance.get().getSoundManager().getMusicData(getIndex())
            );
            toggleButtonVisibility();
            break;
        default:
            break;
        }
        AppInstance.get().getControllerManager().closeAllFloatingController();
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
