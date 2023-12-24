package pto.Controller;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import pto.Constants.PtoSettings;
import pto.Controller.ListView.MusicData;
import pto.Controller.ListView.MusicListTypes;
import pto.Controller.ListView.MusicListTypes.MusicListMode;
import pto.Events.MusicListTypeChangedEvent;
import pto.Manager.AppInstance;
import pto.Utils.ButtonUtils;

/*
 * Create once in MainController
 */
public class ListMenuBarController
{
    @FXML
    private AnchorPane listMenuBar;
    @FXML
    private Label listTitle;
    @FXML
    private Button addToListButton;
    @FXML
    private Button backToListButton;
    private String pureTitle;

    @FXML
    protected void initialize()
    {
        AppInstance.get().getStateManager().bindOnMusicListTypeChanged(
            getClass().getName(),
            event -> {
                onMusicListTypeChanged(event);
            }
        );

        onMusicListTypeChanged(new MusicListTypeChangedEvent(AppInstance.get().getStateManager().getMusicListType()));
    }
    @Override
    protected void finalize()
    {
        AppInstance.get().getStateManager().unbindOnMusicListTypeChanged(getClass().getName());
    }

    protected void onMusicListTypeChanged(MusicListTypeChangedEvent event)
    {
        switch (event.getType().mode)
        {
            case MusicList:
                listTitle.setText("ミュージックリスト");
                backToListButton.setVisible(false);
                addToListButton.setVisible(true);
                break;
            case PlayListPlay:
                backToListButton.setVisible(true);
                addToListButton.setVisible(false);
                break;
            case AddToPlayList:
                listTitle.setText("プレイリストに追加 : " + pureTitle);
                backToListButton.setVisible(true);
                addToListButton.setVisible(false);
                break;
            default:
                listTitle.setText("プレイリスト");
                backToListButton.setVisible(false);
                addToListButton.setVisible(true);
                break;
        }
    }

    public void setTitle(String title)
    {
        pureTitle = title;
        listTitle.setText(pureTitle);
    }
    public String getTitle()
    {
        return listTitle.getText();
    }
    public String getPureTitle()
    {
        return pureTitle;
    }

    @FXML
    protected void onClickedAddToListButton(ActionEvent event)
    {
        switch (AppInstance.get().getStateManager().getMusicListMode())
        {
            case MusicList:
                addMusic();
                break;
            case PlayList:
                openInputPlayListName();
                break;
            default:
                break;
        }
    }

    @FXML
    protected void onClickedBackToListButton(ActionEvent event)
    {
        switch (AppInstance.get().getStateManager().getMusicListMode())
        {
            case AddToPlayList:
                AppInstance.get().getControllerManager().closeAllFloatingController();
                AppInstance.get().getStateManager().setMusicListMode(MusicListMode.MusicList);
                break;
            case PlayListPlay:
                AppInstance.get().getControllerManager().closeAllFloatingController();
                AppInstance.get().getControllerManager().openMusicListController(PtoSettings.MLCONTROLLER_PLAYLIST, MusicListTypes.MusicListMode.PlayList);
                AppInstance.get().getStateManager().setMusicListMode(MusicListMode.PlayList);
                break;
            default:
                break;
        }
    }

    // ----------------------------
    // MusicList Functions
    // ----------------------------
    protected void addMusic()
    {
        MusicListController musicListController = AppInstance.get().getControllerManager().getMusicListController();
        if (musicListController != null)
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Add Music");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Audio Files", PtoSettings.getExtensions()));

            List<File> files = fileChooser.showOpenMultipleDialog(AppInstance.get().getMainWindow());
            if (files != null)
            {
                musicListController.appendFiles(files);
            }
        }
    }

    // ----------------------------
    // PlayList Functions
    // ----------------------------
    protected void openInputPlayListName()
    {
        InputNameController inputNameController = AppInstance.get().getControllerManager().getController(InputNameController.class);
        if (!inputNameController.isOpen())
        {
            AppInstance.get().getControllerManager().closeAllFloatingController();
            inputNameController.playOpenAnimation();
            inputNameController.setOnAction(
                event -> {
                    final InputNameController controller = (InputNameController)AppInstance.get().getControllerManager().getController(InputNameController.class);
                    addPlayList(controller.getNameText());
                }
            );
        }
    }
    protected void addPlayList(String newPlayListName)
    {
        if (!newPlayListName.isEmpty())
        {
            MusicData data = new MusicData(newPlayListName);

            MusicListController musicListController = AppInstance.get().getControllerManager().getMusicListController();
            AppInstance.get().getMusicJsonManager().addPlayList(data);
            if (musicListController != null)
            {
                musicListController.add(data);
                musicListController.reCreateList();
            }
            AppInstance.get().getControllerManager().closeAllFloatingController();
        }
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
