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
import pto.Utils.ListUtils;

public class MusicListCell extends ListCell<MusicData>
{
    protected MusicListCellController cellController;

    public MusicListCell()
    {
        setOnDragDetected(event -> {onDragDetected(event);});
        setOnDragOver(event -> {onDragOver(event);});
        setOnDragEntered(event -> {onDragEntered(event);});
        setOnDragExited(event -> {onDragExited(event);});
        setOnDragDropped(event -> {onDragDropped(event);});
    }
    
    @Override
    public void updateItem(MusicData data, boolean isEmpty)
    {
        super.updateItem(data, isEmpty);

        if (!isEmpty)
        {
            if (cellController == null)
            {
                cellController = new MusicListCellController(data);
            }
            if (getIndex() % 2 == 0)
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
            setGraphic(null);
        }
    }


    void onDragDetected(MouseEvent event)
    {
        if (getItem() == null)
        {
            return;
        }

        ListView<MusicData> listView = getListView();
        Dragboard dragboard = getListView().startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();
        content.put(PtoSettings.DND_FILE_NAMES, new ArrayList<MusicData>(listView.getSelectionModel().getSelectedItems()));

        dragboard.setContent(content);
        event.consume();
    }
    void onDragOver(DragEvent event)
    {
        if (event.getGestureSource() != this && ListViewHelper.hasFileNames(event))   
        {
            event.acceptTransferModes(TransferMode.MOVE);
        }
        event.consume();
    }
    void onDragEntered(DragEvent event)
    {
        if (event.getGestureSource() != this && ListViewHelper.hasFileNames(event))
        {
            setOpacity(0.3);
        }
        event.consume();
    }
    void onDragExited(DragEvent event)
    {
        if (event.getGestureSource() != this && ListViewHelper.hasFileNames(event))
        {
            setOpacity(1);
        }
    }

    @SuppressWarnings("unchecked")
    void onDragDropped(DragEvent event)
    {
        if (event.getGestureSource() != this && ListViewHelper.hasFileNames(event))
        {
            final List<MusicData> dataContent =  ListViewHelper.getFileNames(event);

            final MusicData data = dataContent.get(0);
            
            final ListView<MusicData> fromListView = (ListView<MusicData>)event.getGestureSource();
            final int fromIndex = fromListView.getSelectionModel().getSelectedIndex();
            
            MusicListCell toListViewCell = (MusicListCell)event.getGestureTarget();
            ListView<MusicData> toListView = toListViewCell.getListView();
            final int toIndex = toListViewCell.getIndex();

            //System.out.println(String.format("from %d to %d", fromIndex, toIndex));
            if (fromListView.equals(toListView))
            {
                ListViewHelper.replace(fromIndex, toIndex, toListView);
            }
            else
            {
                if (ListUtils.isValidIndex(toListView.getItems(), toIndex))
                {
                    ListViewHelper.insert(toIndex, data, toListView);
                }
                else
                {
                    ListViewHelper.add(data, toListView);
                }
            }
            event.setDropCompleted(true);
            event.consume();

            getListView().getSelectionModel().clearAndSelect(getIndex());
        }
    }
}
