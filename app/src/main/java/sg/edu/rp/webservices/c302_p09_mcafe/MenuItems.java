package sg.edu.rp.webservices.c302_p09_mcafe;

import java.io.Serializable;

public class MenuItems implements Serializable {
    private String itemId;
    private String description;
    private String categoryId;
    private double unitPrice;


    public MenuItems(String itemId, String categoryId, String description, double unitPrice) {
        this.itemId = itemId;
        this.categoryId = categoryId;
        this.description = description;
        this.unitPrice = unitPrice;

    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
