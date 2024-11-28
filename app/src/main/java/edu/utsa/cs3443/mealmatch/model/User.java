package edu.utsa.cs3443.mealmatch.model;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Represents a user in the MealMatch application.
 * Each user has a unique email, a password, personal details, and preferences like
 * favorite dishes and meal plans.
 *
 * @author Felix Nguyen
 */
public class User {
    private String email; // User's unique email address.
    private String password; // User's password.
    private String firstname; // User's first name.
    private String lastname; // User's last name.
    private Integer groceryID; // ID linking the user to a grocery list.
    private HashSet<Integer> favoriteDishes; // Set of IDs for user's favorite dishes.
    private HashSet<Integer> mealPlans; // Set of IDs for user's meal plans.

    /**
     * Constructs a User object for new user sign-ups.
     *
     * @param email     the user's email address.
     * @param password  the user's password.
     * @param firstname the user's first name.
     * @param lastname  the user's last name.
     */
    public User(String email, String password, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.groceryID = 0; // Default grocery list ID.
        this.favoriteDishes = new HashSet<>();
        this.mealPlans = new HashSet<>();
        this.mealPlans.add(0); // Initializes mealPlans with a default value.
    }

    /**
     * Constructs a User object for loading stored user data.
     *
     * @param email          the user's email address.
     * @param password       the user's password.
     * @param firstname      the user's first name.
     * @param lastname       the user's last name.
     * @param groceryID      the grocery list ID associated with the user.
     * @param favoriteDishes the set of IDs for the user's favorite dishes.
     * @param mealPlans      the set of IDs for the user's meal plans.
     */
    public User(String email, String password, String firstname, String lastname, Integer groceryID, HashSet<Integer> favoriteDishes, HashSet<Integer> mealPlans) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.groceryID = groceryID;
        this.favoriteDishes = favoriteDishes;
        this.mealPlans = mealPlans;
    }

    /**
     * Retrieves the user's email.
     *
     * @return the user's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Updates the user's email.
     *
     * @param email the new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retrieves the user's password.
     *
     * @return the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the user's password.
     *
     * @param password the new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the user's first name.
     *
     * @return the user's first name.
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Updates the user's first name.
     *
     * @param firstname the new first name.
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Retrieves the user's last name.
     *
     * @return the user's last name.
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Updates the user's last name.
     *
     * @param lastname the new last name.
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Retrieves the grocery list ID associated with the user.
     *
     * @return the grocery list ID.
     */
    public Integer getGroceryID() {
        return groceryID;
    }

    /**
     * Updates the grocery list ID associated with the user.
     *
     * @param groceryID the new grocery list ID.
     */
    public void setGroceryID(Integer groceryID) {
        this.groceryID = groceryID;
    }

    /**
     * Retrieves the set of IDs for the user's favorite dishes.
     *
     * @return the set of favorite dish IDs.
     */
    public HashSet<Integer> getFavoriteDishes() {
        return favoriteDishes;
    }

    /**
     * Updates the set of IDs for the user's favorite dishes.
     *
     * @param favoriteDishes the new set of favorite dish IDs.
     */
    public void setFavoriteDishes(HashSet<Integer> favoriteDishes) {
        this.favoriteDishes = favoriteDishes;
    }

    /**
     * Retrieves the set of IDs for the user's meal plans.
     *
     * @return the set of meal plan IDs.
     */
    public HashSet<Integer> getMealPlans() {
        return mealPlans;
    }

    /**
     * Updates the set of IDs for the user's meal plans.
     *
     * @param mealPlans the new set of meal plan IDs.
     */
    public void setMealPlans(HashSet<Integer> mealPlans) {
        this.mealPlans = mealPlans;
    }

    /**
     * Returns a string representation of the user in CSV format.
     * The format includes email, password, first name, last name,
     * favorite dishes (semicolon-separated), grocery list ID, and meal plans (semicolon-separated).
     *
     * @return a CSV-formatted string representation of the user.
     */
    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String toString() {
        String favoriteDishesString = favoriteDishes.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));

        String mealPlansString = mealPlans.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));

        return String.format("%s, %s, %s, %s, %s, %d, %s",
                email, password, firstname, lastname, favoriteDishesString, groceryID, mealPlansString);
    }
}
