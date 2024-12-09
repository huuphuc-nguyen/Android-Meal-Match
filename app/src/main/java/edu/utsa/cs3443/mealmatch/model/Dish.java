package edu.utsa.cs3443.mealmatch.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Represents a Dish entity with various properties such as name, description, image, nutritional information,
 * and a list of ingredients.
 *
 * @author Felix Nguyen
 */
public class Dish {
    private int ID; // Unique identifier for the dish
    private String name; // Name of the dish
    private String description; // Description of the dish
    private String imageUrl; // URL of the dish image
    private String calories; // Caloric value of the dish
    private String protein; // Protein content of the dish
    private String carb; // Carbohydrate content of the dish
    private String fat; // Fat content of the dish
    private ArrayList<String> ingredients; // List of ingredients in the dish

    /**
     * Constructs a Dish object with specified attributes.
     *
     * @param ID          The unique identifier of the dish.
     * @param name        The name of the dish.
     * @param description A brief description of the dish.
     * @param imageUrl    The URL of the image representing the dish.
     * @param calories    The caloric value of the dish.
     * @param protein     The protein content of the dish.
     * @param carb        The carbohydrate content of the dish.
     * @param fat         The fat content of the dish.
     * @param ingredients A list of ingredients used in the dish.
     */
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

    /**
     * Gets the unique identifier of the dish.
     *
     * @return The dish ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the unique identifier of the dish.
     *
     * @param ID The dish ID to set.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * Gets the name of the dish.
     *
     * @return The name of the dish.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the dish.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the dish.
     *
     * @return The description of the dish.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the dish.
     *
     * @param description The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the URL of the dish's image.
     *
     * @return The URL of the image.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the URL of the dish's image.
     *
     * @param imageUrl The URL of the image to set.
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Gets the list of ingredients used in the dish.
     *
     * @return An ArrayList of ingredients.
     */
    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    /**
     * Sets the list of ingredients for the dish.
     *
     * @param ingredients An ArrayList of ingredients to set.
     */
    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Gets the caloric value of the dish.
     *
     * @return The caloric value as a String.
     */
    public String getCalories() {
        return calories;
    }

    /**
     * Sets the caloric value of the dish.
     *
     * @param calories The caloric value to set.
     */
    public void setCalories(String calories) {
        this.calories = calories;
    }

    /**
     * Gets the protein content of the dish.
     *
     * @return The protein content as a String.
     */
    public String getProtein() {
        return protein;
    }

    /**
     * Sets the protein content of the dish.
     *
     * @param protein The protein content to set.
     */
    public void setProtein(String protein) {
        this.protein = protein;
    }

    /**
     * Gets the carbohydrate content of the dish.
     *
     * @return The carbohydrate content as a String.
     */
    public String getCarb() {
        return carb;
    }

    /**
     * Sets the carbohydrate content of the dish.
     *
     * @param carb The carbohydrate content to set.
     */
    public void setCarb(String carb) {
        this.carb = carb;
    }

    /**
     * Gets the fat content of the dish.
     *
     * @return The fat content as a String.
     */
    public String getFat() {
        return fat;
    }

    /**
     * Sets the fat content of the dish.
     *
     * @param fat The fat content to set.
     */
    public void setFat(String fat) {
        this.fat = fat;
    }

    /**
     * Returns a string representation of the Dish object, with its attributes
     * formatted in a comma-separated format.
     *
     * @return A string containing all the dish attributes.
     */
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