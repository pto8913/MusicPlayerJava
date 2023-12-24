package pto.Utils;

import java.util.List;

public class ListUtils {
    public static boolean isValidIndex(List<?> list, int index)
    {
        System.out.println(String.format("isValidIndex size %d > index %d", list.size(), index));
        return list.size() > index;
    }
}
