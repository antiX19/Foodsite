package com.example.foodsite.model;

public record Food(String category, String name, String id, double carbohydrates, double lipids, double proteins) {

    public double computeEnergy() {
        return (proteins * 4) + (carbohydrates * 4) + (lipids * 9);
    }
}
