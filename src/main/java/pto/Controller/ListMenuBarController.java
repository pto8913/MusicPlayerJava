package pto.Controller;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pto.FXMLProxy;
import pto.FXMLProxy.LoadData;
import pto.Controller.ListView.MusicData;
import pto.Controller.ListView.MusicListTypes.MusicListMode;
import pto.Events.MusicListTypeChangedEvent;
import pto.Manager.ControllerManager;
import pto.Manager.MusicJsonManager;
import pto.Manager.StageManager;
import pto.Manager.StateManager;
import pto.Utils.ButtonUtils;

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

    @FXML
    protected void initialize()
    {
        ControllerManager.addController(this);

        StateManager.bindOnMusicListTypeChanged(
            getClass().getName(),
            event -> {
                onMusicListTypeChanged(event);
            }
        );
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
                // listTitle.setText("プレイリスト");
                backToListButton.setVisible(true);
                addToListButton.setVisible(false);
                break;
            case AddToPlayList:
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
        listTitle.setText(title);
    }
    public String getTitle()
    {
        return listTitle.getText();
    }

    @FXML
    protected void onClickedAddToListButton(ActionEvent event)
    {
        switch (StateManager.getMusicListType().mode)
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
        switch (StateManager.getMusicListType().mode)
        {
            case AddToPlayList:
                StateManager.setMusicListMode(MusicListMode.MusicList);
                break;
            case PlayListPlay:
                StateManager.setMusicListMode(MusicListMode.PlayList);
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
        MusicListController musicListController = ControllerManager.getController(MusicListController.class);
        if (musicListController != null)
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Add Music");
            fileChooser.getExtensionFilters().add(new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"));
            List<File> files = fileChooser.showOpenMultipleDialog(StageManager.getStage(StageManager.Main));
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
        if (ControllerManager.getController(InputNameController.class) == null)
        {
            LoadData loadData = FXMLProxy.loadFXML("InputName");
            InputNameController inputNameController = loadData.fxmlLoader.getController();
            ControllerManager.addController(inputNameController);
            inputNameController.setOnAction(onPlayListNameAccept());

            Stage stage = new Stage(StageStyle.TRANSPARENT);
            Scene scene = new Scene(loadData.parent);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
            StageManager.addStage("inputName", stage);
        }
    }
    protected EventHandler<ActionEvent> onPlayListNameAccept()
    {
        return event -> {
            final InputNameController inputNameController = (InputNameController)ControllerManager.getController(InputNameController.class);
            addPlayList(inputNameController.getNameText());

            ControllerManager.removeController(inputNameController);

            StageManager.getStage("inputName").close();
            StageManager.removeStage("inputName");
        };
    }
    protected void addPlayList(String newPlayListName)
    {
        if (!newPlayListName.isEmpty())
        {
            MusicData data = new MusicData(newPlayListName);

            MusicListController musicListController = ControllerManager.getController(MusicListController.class);
            MusicJsonManager.addPlayList(data);
            if (musicListController != null)
            {
                musicListController.add(data);
                musicListController.reCreateList();
            }
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
