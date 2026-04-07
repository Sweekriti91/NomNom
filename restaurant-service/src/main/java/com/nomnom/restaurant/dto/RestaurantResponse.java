package com.nomnom.restaurant.dto;

import java.util.List;

public class RestaurantResponse {

    private Long id;
    private String name;
    private String cuisine;
    private String address;
    private Double rating;
    private Boolean isOpen;
    private List<MenuItemResponse> menuItems;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public List<MenuItemResponse> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemResponse> menuItems) {
        this.menuItems = menuItems;
    }
}
