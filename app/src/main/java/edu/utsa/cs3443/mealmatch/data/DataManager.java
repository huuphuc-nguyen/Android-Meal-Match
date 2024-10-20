package edu.utsa.cs3443.mealmatch.data;

import java.util.ArrayList;
import edu.utsa.cs3443.mealmatch.model.User;

public class DataManager {
    private static DataManager instance;
    private ArrayList<User> Users;

    private DataManager() {
        Users = new ArrayList<User>();
        Users.add(new User("admin", "123", "a", "b"));
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public ArrayList<User> getUsers() {
        return Users;
    }

    public void loadAllData(){

    }

    public void writeUser(User user){

    }
}
