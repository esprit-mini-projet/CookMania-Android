package tn.duoes.esprit.cookmania.models;

public class Ingredient {
    private int id;
    private String name;
    private int quantity;
    private String unit;

    private int shoppingListItemIndex;

    public int getShoppingListItemIndex() {
        return shoppingListItemIndex;
    }

    public void setShoppingListItemIndex(int shoppingListItemIndex) {
        this.shoppingListItemIndex = shoppingListItemIndex;
    }

    public Ingredient() {
    }

    public Ingredient(String name, int quantity, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public Ingredient(int id, String name, int quantity, String unit) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", shoppingListItemIndex=" + shoppingListItemIndex +
                '}';
    }
}
