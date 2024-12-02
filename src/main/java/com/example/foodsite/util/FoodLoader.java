package com.example.foodsite.util;

import com.example.foodsite.model.Food;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FoodLoader {

    public static List<Food> loadFoodItems(String resourcePath) {
        List<Food> foodItems = new ArrayList<>();

        try (Reader reader = new InputStreamReader(
                FoodLoader.class.getResourceAsStream(resourcePath), StandardCharsets.UTF_8);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                String category = csvRecord.get("Category");
                String name = csvRecord.get("Name");
                String id = csvRecord.get("ID");
                double carbohydrates = Double.parseDouble(csvRecord.get("Carbohydrates"));
                double lipids = Double.parseDouble(csvRecord.get("Lipids"));
                double proteins = Double.parseDouble(csvRecord.get("Proteins"));

                Food food = new Food(category, name, id, carbohydrates, lipids, proteins);
                foodItems.add(food);
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception or rethrow as needed
        }

        return foodItems;
    }
}
