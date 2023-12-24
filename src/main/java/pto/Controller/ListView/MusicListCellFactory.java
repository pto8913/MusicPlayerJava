package pto.Controller.ListView;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class MusicListCellFactory implements Callback<ListView<String>, javafx.scene.control.ListCell<String>>
{
    protected MusicListTypes cellTypes;

    public MusicListCellFactory(MusicListTypes cellTypes)
    {
        this.cellTypes = cellTypes;
    }

    @Override
    public ListCell<String> call(ListView<String> listView)
    {
        ListCell<String> out;
        out = new MusicListCell(cellTypes);
        return out;
    }
}
