package edu.utsa.cs3443.mealmatch.model;

import java.util.ArrayList;

public class User {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private Integer groceryID;
    private ArrayList<Integer> favoriteDishes;
    private ArrayList<Integer> mealPlans;

    // Constructor is used in Signup Activity when creating new user
    public User(String email, String password, String firstname, String lastname){
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.groceryID = 0;
        this.favoriteDishes = new ArrayList<>();
        this.favoriteDishes.add(0);
        this.mealPlans = new ArrayList<>();
        this.mealPlans.add(0);
        // add 0 inorder to initialize enough a mount of fields
        // when writing a new user to the file
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

    public ArrayList<Integer> getFavoriteDishes() {
        return favoriteDishes;
    }

    public void setFavoriteDishes(ArrayList<Integer> favoriteDishes) {
        this.favoriteDishes = favoriteDishes;
    }

    public ArrayList<Integer> getMealPlans() {
        return mealPlans;
    }

    public void setMealPlans(ArrayList<Integer> mealPlans) {
        this.mealPlans = mealPlans;
    }
}
