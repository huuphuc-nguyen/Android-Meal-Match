package edu.utsa.cs3443.mealmatch.model;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.util.HashSet;
import java.util.stream.Collectors;

public class User {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private Integer groceryID;
    private HashSet<Integer> favoriteDishes;
    private HashSet<Integer> mealPlans; // Changed from ArrayList to HashSet

    // This Constructor is used in Signup Activity when creating new user
    public User(String email, String password, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.groceryID = 0;
        this.favoriteDishes = new HashSet<>();
        this.mealPlans = new HashSet<>();
        this.mealPlans.add(0); // Add 0 to initialize the HashSet with a default value
    }

    // This Constructor is used in Data Manager to map stored data to object user
    public User(String email, String password, String firstname, String lastname, Integer groceryID, HashSet<Integer> favoriteDishes, HashSet<Integer> mealPlans) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.groceryID = groceryID;
        this.favoriteDishes = favoriteDishes;
        this.mealPlans = mealPlans;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getGroceryID() {
        return groceryID;
    }

    public void setGroceryID(Integer groceryID) {
        this.groceryID = groceryID;
    }

    public HashSet<Integer> getFavoriteDishes() {
        return favoriteDishes;
    }

    public void setFavoriteDishes(HashSet<Integer> favoriteDishes) {
        this.favoriteDishes = favoriteDishes;
    }

    public HashSet<Integer> getMealPlans() { // Updated getter
        return mealPlans;
    }

    public void setMealPlans(HashSet<Integer> mealPlans) { // Updated setter
        this.mealPlans = mealPlans;
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        // Join lists with semicolons for favorite dishes and meal plans
        String favoriteDishesString = favoriteDishes.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));

        String mealPlansString = mealPlans.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));

        // Format as CSV
        return String.format("%s, %s, %s, %s, %s, %d, %s",
                email, password, firstname, lastname, favoriteDishesString, groceryID, mealPlansString);
    }
}
