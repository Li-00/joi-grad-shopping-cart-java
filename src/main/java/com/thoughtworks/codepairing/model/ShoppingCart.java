package com.thoughtworks.codepairing.model;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ShoppingCart {
    private List<Product> products;
    private Customer customer;

    public ShoppingCart(Customer customer, List<Product> products) {
        this.customer = customer;
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public Order checkout() {
        double totalPrice = 0;
        HashMap<Product, Integer> productDoubleHashMap = new HashMap<>();
        int loyaltyPointsEarned = 0;
        for (Product product : products) {
            double discount = 0;
            if (product.getProductCode().startsWith("DIS_10")) {
                discount = (product.getPrice() * 0.1);
                loyaltyPointsEarned += (product.getPrice() / 10);
            } else if (product.getProductCode().startsWith("DIS_15")) {
                discount = (product.getPrice() * 0.15);
                loyaltyPointsEarned += (product.getPrice() / 15);
            } else if (product.getProductCode().startsWith("DIS_20")) {
                discount = (product.getPrice() * 0.2);
                loyaltyPointsEarned += (product.getPrice() / 20);
            } else if (product.getProductCode().startsWith("BULK_BUY_2_GET_1")) {
                    productDoubleHashMap.put(product, productDoubleHashMap.getOrDefault(product, 0) + 1);

            } else {
                loyaltyPointsEarned += (product.getPrice() / 5);
            }
            
            totalPrice += product.getPrice() - discount;
        }
        for (Product product : productDoubleHashMap.keySet()) {
            //每三个为一组，通过产品（key）获取数量
            int buyTwoGetOne = productDoubleHashMap.get(product) / 3;
            totalPrice -= (product.getPrice() * buyTwoGetOne);
        }


        return new Order(totalPrice, loyaltyPointsEarned);
    }

    @Override
    public String toString() {
        return "Customer: " + customer.getName() + "\n" + "Bought:  \n" + products.stream().map(p -> "- " + p.getName()+ ", "+p.getPrice()).collect(Collectors.joining("\n"));
    }
}
