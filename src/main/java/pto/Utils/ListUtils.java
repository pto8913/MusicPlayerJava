package pto.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListUtils {
    public static boolean isValidIndex(List<?> list, int index)
    {
        System.out.println(String.format("isValidIndex size %d > index %d", list.size(), index));
        return list.size() > index;
    }
    public static <T> List<String> toString(List<T> list)
    {
        List<String> out = new ArrayList<>();
        for (T elem : list)
        {
            out.add(elem.toString());
        }
        return out;
    }
}
