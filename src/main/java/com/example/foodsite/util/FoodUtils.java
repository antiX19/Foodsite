package com.example.foodsite.util;

import com.example.foodsite.model.Food;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class FoodUtils {

    /**
     * Extracts and returns all unique food categories in lexicographical order.
     *
     * @param foods the list of Food objects
     * @return a set of unique categories sorted lexicographically
     */
    public static Set<String> extractCategories(List<Food> foods) {
        Set<String> categories = new TreeSet<>();
        for (Food food : foods) {
            categories.add(food.category());
        }
        return categories;
    }
}
