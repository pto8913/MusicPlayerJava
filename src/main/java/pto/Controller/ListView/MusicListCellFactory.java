package pto.Controller.ListView;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class MusicListCellFactory implements Callback<ListView<MusicData>, javafx.scene.control.ListCell<MusicData>>
{
    @Override
    public ListCell<MusicData> call(ListView<MusicData> listView)
    {
        ListCell<MusicData> out;
        out = new MusicListCell();
        return out;
    }
}
