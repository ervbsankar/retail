package com.target.retail.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {

    private int id;
    private String name;
    @JsonProperty("current_price")
    private ProductPrice currentPrice;

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

    public ProductPrice getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(ProductPrice currentPrice) {
        this.currentPrice = currentPrice;
    }
}
