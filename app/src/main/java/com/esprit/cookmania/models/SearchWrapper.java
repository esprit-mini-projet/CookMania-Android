package com.esprit.cookmania.models;

import java.util.ArrayList;
import java.util.List;

public class SearchWrapper {
    private List<String> labels;
    private String searchText;
    private int servingsMin, servingsMax;
    private String calories;

    public SearchWrapper(){
        labels = new ArrayList<>();
        searchText = "";
        servingsMin = 1;
        servingsMax = 10;
        calories = "All";
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public int getServingsMin() {
        return servingsMin;
    }

    public void setServingsMin(int servingsMin) {
        this.servingsMin = servingsMin;
    }

    public int getServingsMax() {
        return servingsMax;
    }

    public void setServingsMax(int servingsMax) {
        this.servingsMax = servingsMax;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "SearchWrapper{" +
                "labels=" + labels +
                ", searchText='" + searchText + '\'' +
                ", servingsMin=" + servingsMin +
                ", servingsMax=" + servingsMax +
                ", calories='" + calories + '\'' +
                '}';
    }
}
