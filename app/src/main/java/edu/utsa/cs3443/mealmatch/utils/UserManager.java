package edu.utsa.cs3443.mealmatch.utils;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.model.GroceryList;
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

        for (User user: DataManager.getInstance().getUsers()){
            if (user.getEmail().equals(email) && user.getPassword().equals(password)){

                // Get current user data
                User current_user = DataManager.getInstance().getUserByEmail(email);
                setUser(current_user);

                return true;
            }
        }

        return false;
    }

    public void addNewUser(String email, String password, String firstname, String lastname, Context context){
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
}

