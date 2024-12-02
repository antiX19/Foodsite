package com.example.foodsite;

import com.example.foodsite.model.Food;
import com.example.foodsite.util.FoodLoader;
import com.example.foodsite.util.FoodUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.pebble.PebbleTemplateEngine;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Server extends AbstractVerticle {

    private List<Food> foodItems;

    @Override
    public void start() {
        Vertx vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        PebbleTemplateEngine engine = PebbleTemplateEngine.create(vertx);

        // Load food items at startup
        foodItems = FoodLoader.loadFoodItems("/data/food.csv");

        // Route to return the number of food items
        router.get("/foodCounter").handler(ctx -> {
            ctx.response().end("Number of food items: " + foodItems.size());
        });

        // Static handler for images
        router.route("/static/*").handler(StaticHandler.create("static"));

        // Route for homepage
        router.get("/").handler(ctx -> {
            Set<String> categories = FoodUtils.extractCategories(foodItems);
            ctx.put("categories", categories);
            engine.render(ctx.data(), "templates/index.peb", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        // Route for category page
        router.get("/cat/:catName").handler(ctx -> {
            String catName = ctx.pathParam("catName");
            List<Food> categoryFoods = foodItems.stream()
                    .filter(food -> food.category().equalsIgnoreCase(catName))
                    .collect(Collectors.toList());
            ctx.put("foods", categoryFoods);
            ctx.put("category", catName);
            engine.render(ctx.data(), "templates/category.peb", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        // Route for food detail page
        router.get("/food/:foodId").handler(ctx -> {
            String foodId = ctx.pathParam("foodId");
            foodItems.stream()
                    .filter(food -> food.id().equals(foodId))
                    .findFirst()
                    .ifPresent(food -> {
                        ctx.put("food", food);
                        engine.render(ctx.data(), "templates/food.peb", res -> {
                            if (res.succeeded()) {
                                ctx.response().end(res.result());
                            } else {
                                ctx.fail(res.cause());
                            }
                        });
                    });
        });

        // Route for search page
        router.get("/search").handler(ctx -> {
            String query = ctx.queryParam("q").stream().findFirst().orElse("").toLowerCase();
            List<Food> searchResults = foodItems.stream()
                    .filter(food -> food.name().toLowerCase().contains(query))
                    .collect(Collectors.toList());
            ctx.put("results", searchResults);
            ctx.put("query", query);
            engine.render(ctx.data(), "templates/search.peb", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        // Route for nutritional ranking
        router.get("/nutritionalRanking").handler(ctx -> {
            // Example: Sort by carbohydrates
            List<Food> sortedFoods = foodItems.stream()
                    .sorted((f1, f2) -> Double.compare(f2.carbohydrates(), f1.carbohydrates()))
                    .collect(Collectors.toList());
            ctx.put("sortedFoods", sortedFoods);
            engine.render(ctx.data(), "templates/nutritional_ranking.peb", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        // Start the server
        vertx.createHttpServer().requestHandler(router).listen(8080);
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Server());
    }
}
