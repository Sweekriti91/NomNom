package com.nomnom.restaurant.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateRestaurantRequest {

    @NotBlank
    private String name;

    private String cuisine;

    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
