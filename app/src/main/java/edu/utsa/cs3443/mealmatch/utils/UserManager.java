package edu.utsa.cs3443.mealmatch.utils;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import edu.utsa.cs3443.mealmatch.data.DataManager;
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

        DataManager.getInstance().addUserToList(newUser);
        DataManager.getInstance().saveUserData(context);
    }

    public void logout(){
        this.user = null;
    }
}

