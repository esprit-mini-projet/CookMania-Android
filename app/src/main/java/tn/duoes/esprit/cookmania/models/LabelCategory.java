package tn.duoes.esprit.cookmania.models;

import java.util.List;

public class LabelCategory {
    private String category;
    private List<String> labels;

    public LabelCategory(String category, List<String> labels) {
        this.category = category;
        this.labels = labels;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "LabelCategory{" +
                "category='" + category + '\'' +
                ", labels=" + labels +
                '}';
    }
}
