package pto.Controller.ListView;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import pto.Constants.PtoSettings;
import pto.Controller.ListMenuBarController;
import pto.Controller.MusicListController;
import pto.Controller.UserController;
import pto.Manager.AppInstance;

public class MusicListCell extends ListCell<String>
{
    protected MusicListCellController cellController;
    protected MusicListTypes cellTypes;

    public MusicListCell(MusicListTypes cellTypes)
    {
        this.cellTypes = cellTypes;
        setOnMousePressed(event -> {onMousePressed(event);});
        setOnDragDetected(event -> {onDragDetected(event);});
        setOnDragOver(event -> {onDragOver(event);});
        setOnDragEntered(event -> {onDragEntered(event);});
        setOnDragExited(event -> {onDragExited(event);});
        setOnDragDropped(event -> {onDragDropped(event);});
    }

    @Override
    public void updateItem(String data, boolean isEmpty)
    {
        super.updateItem(data, isEmpty);

        if (!isEmpty)
        {
            cellController = new MusicListCellController(data, cellTypes);
            final int index = getIndex();
            setIndex(index);
            if (index % 2 == 0)
            {
                setStyle("-fx-control-inner-background: rgba(255, 255, 255, 0.25)");
            }
            else
            {
                setStyle("-fx-control-inner-background: rgba(0, 0, 0, 0.25)");
            }
            setGraphic(cellController.getEntire());
        }
        else
        {
            cellController = null;
            setGraphic(null);
        }
    }

    public void setIndex(int index)
    {
        cellController.setIndex(index);
    }

    // ----------------------------
    // Main Functions : Clicked
    // ----------------------------
    void onMousePressed(MouseEvent event)
    {
        switch (cellTypes.mode)
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
        UserController userController = AppInstance.get().getControllerManager().getController(UserController.class);
        if (!userController.isOpen())
        {
            userController.playOpenAnimation();
        }
        else
        {
            if (userController.getActiveMusicTitle().equals(cellController.getTitle()))
            {
                userController.playCloseAnimation();        
            }
        }
        userController.setMusicData(getIndex(), cellController.getTitle());
    }
    private void onClickedCellForPlayList()
    {
        ListMenuBarController controller = AppInstance.get().getControllerManager().getController(ListMenuBarController.class);
        if (controller == null)
        {
            return;
        }
        controller.setTitle(cellController.getTitle());

        /* Initialize MusicListController_PlayListPlay */
        AppInstance.get().getControllerManager().openMusicListController(PtoSettings.MLCONTROLLER_PLAYLISTPLAY, MusicListTypes.MusicListMode.PlayListPlay);
    }

    // ----------------------------
    // Main Functions : DragAndDrop
    // ----------------------------
    void onDragDetected(MouseEvent event)
    {
        if (getItem() == null)
        {
            return;
        }

        ListView<String> listView = getListView();
        Dragboard dragboard = getListView().startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.put(PtoSettings.DND_FILE_NAMES, new ArrayList<String>(listView.getSelectionModel().getSelectedItems()));

        dragboard.setContent(content);
        event.consume();

        MusicListController playListController = AppInstance.get().getControllerManager().getMusicListController();
        playListController.setIgnoreAllClose(true);
        AppInstance.get().getControllerManager().closeAllFloatingController();
        playListController.setIgnoreAllClose(false);
    }
    void onDragOver(DragEvent event)
    {
        if (event.getGestureSource() != this && hasFileNames(event))   
        {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }
    void onDragEntered(DragEvent event)
    {
        if (event.getGestureSource() != this && hasFileNames(event))
        {
            setOpacity(0.3);
        }
        event.consume();
    }
    void onDragExited(DragEvent event)
    {
        if (event.getGestureSource() != this && hasFileNames(event))
        {
            setOpacity(1);
        }
    }
    @SuppressWarnings("unchecked")
    void onDragDropped(DragEvent event)
    {
        if (event.getGestureSource() != this && hasFileNames(event))
        {
            final ListView<String> fromListView = (ListView<String>)event.getGestureSource();
            final int fromIndex = fromListView.getSelectionModel().getSelectedIndex();
            
            MusicListCell toListViewCell = (MusicListCell)event.getGestureTarget();
            ListView<String> toListView = toListViewCell.getListView();
            final int toIndex = toListViewCell.getIndex();

            //System.out.println(String.format("from %d to %d", fromIndex, toIndex));
            if (fromListView.equals(toListView))
            {
                final String fromData = toListView.getItems().get(fromIndex);
                final String toData = toListView.getItems().get(toIndex);
                toListView.getItems().set(toIndex, fromData);
                toListView.getItems().set(fromIndex, toData);

                AppInstance.get().getSoundManager().replace(fromIndex, toIndex);
                cellController.setIndex(toIndex);
                toListViewCell.setIndex(fromIndex);

                if (cellTypes.mode == MusicListTypes.MusicListMode.PlayListPlay)
                {
                    ListMenuBarController listMenuBarController = AppInstance.get().getControllerManager().getController(ListMenuBarController.class);
                    AppInstance.get().getMusicJsonManager().musicReplacePlayList(listMenuBarController.getPureTitle(), fromIndex, toIndex);
                }
            }
            event.setDropCompleted(true);
            event.consume();
            
            UserController userController = AppInstance.get().getControllerManager().getController(UserController.class);
            userController.setMusicData(toIndex, cellController.getTitle());

            getListView().getSelectionModel().clearAndSelect(getIndex());
        }
    }

    // ----------------------------
    // Main Functions : Utility
    // ----------------------------
    protected boolean hasFileNames(DragEvent event)
    {
        return event.getDragboard().hasContent(PtoSettings.DND_FILE_NAMES);
    }
    @SuppressWarnings("unchecked")
    protected <T> List<T> getFileNames(DragEvent event)
    {
        return (List<T>)event.getDragboard().getContent(PtoSettings.DND_FILE_NAMES);
    }
}
