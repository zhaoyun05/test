package com.example.commodity_management;

public class Commodity {
    private String name;
    private String model;
    private String factory;
    private String address;
    private String original_price;
    private String discount_price;
    private String introduction;
    private String Details;
    private String uri;
    public Commodity() {
        this.name = name;
    }


    public Commodity(String name, String model, String factory, String address, String original_price, String discount_price, String introduction, String uri,String details) {
        this.name = name;
        this.model = model;
        this.factory = factory;
        this.address = address;
        this.original_price = original_price;
        this.discount_price = discount_price;
        this.introduction = introduction;
        this.uri = uri;
        Details = details;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", factory='" + factory + '\'' +
                ", address='" + address + '\'' +
                ", original_price='" + original_price + '\'' +
                ", discount_price='" + discount_price + '\'' +
                ", introduction='" + introduction + '\'' +
                ", uri='" + uri + '\'' +
                ", Details='" + Details + '\'' +
                '}';
    }
}
