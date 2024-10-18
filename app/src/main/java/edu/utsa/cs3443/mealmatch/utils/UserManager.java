package edu.utsa.cs3443.mealmatch.utils;

import android.util.Log;

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
        if(email.equals("admin") && password.equals("123")){
            // TODO: add logic logging in user
            this.user = new User(email, password);
            loadUserData();
            return true;
        }
        else{
            return false;
        }
    }

    public void loadUserData(){
        if (this.user != null){
            // TODO: Load user data
        }
        else{
            Log.e(Constant.LOG_AUTH,"No user logged in");
        }
    }

    public void logout(){
        this.user = null;
    }
}

