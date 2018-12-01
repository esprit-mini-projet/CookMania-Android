package tn.duoes.esprit.cookmania.utils;

import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

public final class ListUtils {

    public interface IListUtils<T>{
        Object key(Object o);
        List<T> nested(Object o);
    }

    public static List<Object> flattenList(List<Object> list, IListUtils iListUtils){
        List<Object> flatList = new ArrayList<>();
        for (Object item : list){
            flatList.add(iListUtils.key(item));
            flatList.addAll(iListUtils.nested(item));
        }
        return flatList;
    }

}
