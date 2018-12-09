package com.target.retail.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="ProductPrice")
public class ProductPrice {

    public ProductPrice() {
    }

    public ProductPrice(int productId, double value, String currencyCode) {
        this.productId = productId;
        this.value = value;
        this.currencyCode = currencyCode;

    }
    @Id
    private String id;
    private int productId;
    private double value;
    private String currencyCode;

    public String getId() {
        return id;
    }

    @JsonIgnore
    public void setId(String id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    @JsonIgnore
    public void setProductId(int productId) {
        this.productId = productId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @JsonProperty("currency_code")
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @Override
    public String toString() {
        return " ProductPrice [productId=" + productId + ", value=" + value + ", currencyCode=" + currencyCode + "]";
    }
}
