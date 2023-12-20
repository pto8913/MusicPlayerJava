package pto.Controller.ListView;

import java.util.List;

import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import pto.Constants.PtoSettings;
import pto.Utils.ListUtils;

public class ListViewHelper
{
    public static boolean hasFileNames(DragEvent event)
    {
        return event.getDragboard().hasContent(PtoSettings.DND_FILE_NAMES);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getFileNames(DragEvent event)
    {
        return (List<T>)event.getDragboard().getContent(PtoSettings.DND_FILE_NAMES);
    }

    public static <T> boolean add(T data, ListView<T> listView)
    {
        listView.getItems().add(data);
        //System.out.println(String.format("add file name : %s", data));
        return true;
    }
    public static <T> boolean insert(int index, T data, ListView<T> listView)
    {
        if (ListUtils.isValidIndex(listView.getItems(), index))
        {
            //System.out.println(String.format("insert file name : %s to index : %d", data, index));
            listView.getItems().add(index, data);
            return true;
        }
        return false;
    }
    public static <T> boolean replace(int from, int to, ListView<T> listView)
    {
        final T fromData = listView.getItems().get(from);
        final T toData = listView.getItems().get(to);
        listView.getItems().set(to, fromData);
        listView.getItems().set(from, toData);
        //System.out.println(String.format("replace cell!! from %d to %d.", from, to));
        return true;
    }
    public static <T> boolean remove(int index, ListView<T> listView)
    {
        if (ListUtils.isValidIndex(listView.getItems(), index))
        {
            listView.getItems().remove(index);
            return true;
        }
        return false;
    }
    public static <T> boolean append(List<T> datas, ListView<T> listView)
    {
        if (datas == null || datas.size() == 0)
        {
            //System.err.println("append but datas is null");
            return false;
        }
        for (T data : datas)
        {
            add(data, listView);
        }
        return true;
    }
}
