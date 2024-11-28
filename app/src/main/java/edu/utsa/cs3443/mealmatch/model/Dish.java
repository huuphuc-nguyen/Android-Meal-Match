package edu.utsa.cs3443.mealmatch.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Dish {
    private int ID;
    private String name;
    private String description;
    private String imageUrl;
    private String calories;
    private String protein;
    private String carb;
    private String fat;
    private ArrayList<String> ingredients;

    public Dish(int ID, String name, String description, String imageUrl, String calories, String protein, String carb, String fat, ArrayList<String> ingredients) {
        this.ID = ID;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.calories = calories;
        this.protein = protein;
        this.carb = carb;
        this.fat = fat;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCarb() {
        return carb;
    }

    public void setCarb(String carb) {
        this.carb = carb;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        // Join ingredients with a semicolon
        String ingredientsString = String.join(";", ingredients);

        // Format the Dish information as a single string
        return String.format("%d, %s, %s, %s, %s, %s, %s, %s, %s",
                ID,
                name,
                description,
                imageUrl,
                calories,
                protein,
                carb,
                fat,
                ingredientsString);
    }

}
