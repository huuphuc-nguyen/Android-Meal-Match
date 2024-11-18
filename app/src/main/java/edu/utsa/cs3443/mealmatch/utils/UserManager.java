package edu.utsa.cs3443.mealmatch.utils;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.model.GroceryList;
import edu.utsa.cs3443.mealmatch.model.MealPlan;
import edu.utsa.cs3443.mealmatch.model.User;

public class UserManager {
    private static UserManager instance;
    private User user;

    private UserManager() {}

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

        public boolean login(String email, String password){
            User user = DataManager.getInstance().getUserByEmail(email);

            if (user != null && user.getPassword().equals(password)) {
                // Set the current user if email and password match
                setUser(user);
                return true;
            }

            return false;
        }

    public boolean addNewUser(String email, String password, String firstname, String lastname, Context context){
        // Check if the email already exists in DataManager
        if (DataManager.getInstance().getUserByEmail(email) != null) {
            return false; // Exit the method if the email is already in use
        }

        User newUser = new User(email, password, firstname, lastname);

        // Create new grocery list for new account
        int newID = DataManager.getInstance().getNextGroceryListID();
        ArrayList<Integer> tasks = new ArrayList<>();
        tasks.add(0);
        GroceryList newList = new GroceryList(newID, tasks);
        newUser.setGroceryID(newID);

        // Update data and save to file
        DataManager.getInstance().addGroceryList(newList, context);
        DataManager.getInstance().addUserToList(newUser, context);

        return true;
    }

    public void logout(){
        this.user = null;
    }

    public void addFavoriteDish(int id, Context context){
        user.getFavoriteDishes().add(id);
        DataManager.getInstance().updateUser(context);
    }
    public void removeFavoriteDish(int id, Context context){
        user.getFavoriteDishes().remove(Integer.valueOf(id));
        DataManager.getInstance().updateUser(context);
    }

    public void addMealPlan(int dishID, int mealPlanID, Context context) {
        MealPlan mealPlan = DataManager.getInstance().getMealPlanById(mealPlanID);

        // If the meal plan doesn't exist, create a new one
        if (mealPlan == null) {
            mealPlan = new MealPlan(mealPlanID, new Date(), new ArrayList<>());
            DataManager.getInstance().addMealPlan(mealPlan, context);
            user.getMealPlans().add(mealPlanID);
            DataManager.getInstance().updateUser(context);
        }
        if (!mealPlan.getDishes().contains(dishID)) {
            mealPlan.getDishes().add(dishID);
            DataManager.getInstance().updateMealPlan(context);
        }
    }

    public void removeDishFromMealPlan(int dishID, int mealPlanID, Context context) {
        // Retrieve the meal plan by its ID
        MealPlan mealPlan = DataManager.getInstance().getMealPlanById(mealPlanID);

        if (mealPlan != null) {
            // Check if the dish exists in the meal plan
            if (mealPlan.getDishes().contains(dishID)) {
                // Remove the dish from the meal plan
                mealPlan.getDishes().remove(Integer.valueOf(dishID));

                // Save changes to storage
                DataManager.getInstance().updateMealPlan(context);

                // Log the operation for debugging purposes
                Log.i("UserManager", "Dish removed from meal plan.");
            } else {
                Log.i("UserManager", "Dish not found in the meal plan.");
            }
        } else {
            Log.e("UserManager", "Meal plan does not exist.");
        }
    }
}

