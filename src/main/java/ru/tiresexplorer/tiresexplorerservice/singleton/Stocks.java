package ru.tiresexplorer.tiresexplorerservice.singleton;

public class Stocks {
    private static Stocks instance;
    private String stock;

    private Stocks() {}

    public static Stocks getInstance() {
        if (instance == null) {
            instance = new Stocks();
        }
        return instance;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
