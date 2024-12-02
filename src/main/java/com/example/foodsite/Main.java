package com.example.foodsite;

import com.example.foodsite.model.Food;
import com.example.foodsite.util.FoodLoader;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Load food data from CSV file
        List<Food> foodItems = FoodLoader.loadFoodItems("/data/food.csv");

        // Initialize Scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Prompt user for a food name
        System.out.println("Enter the name of the food you are looking for:");
        String searchQuery = scanner.nextLine().toLowerCase();

        // Search and display matching foods
        boolean found = false;
        for (Food food : foodItems) {
            if (food.name().toLowerCase().contains(searchQuery)) {
                found = true;
                System.out.println("Food: " + food.name());
                System.out.println("Category: " + food.category());
                System.out.println("Carbohydrates: " + food.carbohydrates() + "g");
                System.out.println("Lipids: " + food.lipids() + "g");
                System.out.println("Proteins: " + food.proteins() + "g");
                System.out.println("Energy: " + food.computeEnergy() + " kcal");
                System.out.println("---------------------------");
            }
        }

        if (!found) {
            System.out.println("No matching foods found.");
        }

        // Close the scanner
        scanner.close();
    }
}
