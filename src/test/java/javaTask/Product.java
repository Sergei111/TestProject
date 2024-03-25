package javaTask;

import org.apache.logging.log4j.core.config.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Product {
    private Long id;
    private String name;
    private String category;
    private Double price;
    private Set<Order> orders;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public static void main(String[] args) {
        List<Product> productList = new ArrayList<>(); // Assuming productList is populated with Product objects
        // Obtain a list of products belonging to category "Books" with price > 100 using stream
        List<Product> expensiveBooks = productList.stream()
                .filter(product -> product.getCategory().equals("Books") && product.getPrice() > 100)
                .collect(Collectors.toList());
        System.out.println("Expensive Books: " + expensiveBooks);
    }
}