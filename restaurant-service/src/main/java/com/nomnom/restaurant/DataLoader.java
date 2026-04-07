package com.nomnom.restaurant;

import com.nomnom.restaurant.model.MenuItem;
import com.nomnom.restaurant.model.Restaurant;
import com.nomnom.restaurant.repository.MenuItemRepository;
import com.nomnom.restaurant.repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public DataLoader(RestaurantRepository restaurantRepository,
                      MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void run(String... args) {
        // Byte Burger
        var byteBurger = new Restaurant();
        byteBurger.setName("Byte Burger");
        byteBurger.setCuisine("American");
        byteBurger.setAddress("123 Binary Blvd");
        byteBurger.setRating(4.5);
        byteBurger.setIsOpen(true);
        byteBurger = restaurantRepository.save(byteBurger);

        saveMenuItem("Classic Byte Burger", "Juicy beef patty with lettuce, tomato, and secret sauce", new BigDecimal("12.99"), "Burgers", byteBurger.getId());
        saveMenuItem("Double Stack Overflow", "Two patties, double cheese, bacon, onion rings", new BigDecimal("16.99"), "Burgers", byteBurger.getId());
        saveMenuItem("Loaded Fries 2.0", "Crispy fries with cheese sauce, jalapeños, and bacon bits", new BigDecimal("8.49"), "Sides", byteBurger.getId());
        saveMenuItem("Milkshake.exe", "Thick vanilla milkshake with whipped cream", new BigDecimal("6.99"), "Drinks", byteBurger.getId());

        // Pixel Pizza
        var pixelPizza = new Restaurant();
        pixelPizza.setName("Pixel Pizza");
        pixelPizza.setCuisine("Italian");
        pixelPizza.setAddress("456 Render Road");
        pixelPizza.setRating(4.7);
        pixelPizza.setIsOpen(true);
        pixelPizza = restaurantRepository.save(pixelPizza);

        saveMenuItem("Margherita.png", "Classic margherita with fresh mozzarella and basil", new BigDecimal("14.99"), "Pizza", pixelPizza.getId());
        saveMenuItem("Pepperoni Payload", "Loaded pepperoni with extra cheese", new BigDecimal("16.49"), "Pizza", pixelPizza.getId());
        saveMenuItem("Garlic Breadcrumbs", "Warm garlic bread with herb butter", new BigDecimal("5.99"), "Sides", pixelPizza.getId());
        saveMenuItem("Tiramisu Token", "Classic Italian tiramisu", new BigDecimal("7.99"), "Desserts", pixelPizza.getId());

        // Cache Kitchen
        var cacheKitchen = new Restaurant();
        cacheKitchen.setName("Cache Kitchen");
        cacheKitchen.setCuisine("Asian Fusion");
        cacheKitchen.setAddress("789 Memory Lane");
        cacheKitchen.setRating(4.3);
        cacheKitchen.setIsOpen(true);
        cacheKitchen = restaurantRepository.save(cacheKitchen);

        saveMenuItem("Teriyaki Thread Bowl", "Grilled chicken teriyaki over steamed rice with veggies", new BigDecimal("13.99"), "Bowls", cacheKitchen.getId());
        saveMenuItem("Kung Pao Kernel", "Spicy kung pao chicken with peanuts and peppers", new BigDecimal("14.49"), "Entrees", cacheKitchen.getId());
        saveMenuItem("Spring Roll-back", "Crispy vegetable spring rolls with sweet chili dip", new BigDecimal("6.99"), "Appetizers", cacheKitchen.getId());
    }

    private void saveMenuItem(String name, String description, BigDecimal price, String category, Long restaurantId) {
        var item = new MenuItem();
        item.setName(name);
        item.setDescription(description);
        item.setPrice(price);
        item.setCategory(category);
        item.setRestaurantId(restaurantId);
        item.setIsAvailable(true);
        menuItemRepository.save(item);
    }
}
