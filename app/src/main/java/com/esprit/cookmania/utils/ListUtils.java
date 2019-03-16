package com.esprit.cookmania.utils;

import java.util.ArrayList;
import java.util.List;

public final class ListUtils {

    public interface IListUtils<T>{
        List<T> nested(Object o, int index);
    }

    public static List<Object> flattenList(List<Object> list, IListUtils iListUtils){
        List<Object> flatList = new ArrayList<>();
        int index = 0;
        for (Object item : list){
            flatList.add(item);
            List<Object> nestedObjects = iListUtils.nested(item, index);
            flatList.addAll(nestedObjects);
            index += nestedObjects.size()+1;
        }
        return flatList;
    }

}
