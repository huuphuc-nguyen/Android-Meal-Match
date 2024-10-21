package edu.utsa.cs3443.mealmatch.data;

import java.util.ArrayList;
import edu.utsa.cs3443.mealmatch.model.User;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.model.MealPlan;
import edu.utsa.cs3443.mealmatch.model.GroceryList;
import edu.utsa.cs3443.mealmatch.model.Task;

public class DataManager {
    private static DataManager instance;
    private ArrayList<User> Users;
    private ArrayList<Dish> Dishes;
    private ArrayList<MealPlan> MealPlans;
    private ArrayList<GroceryList> GroceryLists;
    private ArrayList<Task> Tasks;

    private DataManager() {
        Users = new ArrayList<User>();
        Dishes = new ArrayList<Dish>();
        MealPlans = new ArrayList<MealPlan>();
        GroceryLists = new ArrayList<GroceryList>();
        Tasks = new ArrayList<Task>();
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

    public ArrayList<Dish> getDishes(){
        return Dishes;
    }

    public ArrayList<MealPlan> getMealPlans(){
        return MealPlans;
    }

    public ArrayList<GroceryList> getGroceryLists(){
        return GroceryLists;
    }

    public ArrayList<Task> getTasks(){
        return Tasks;
    }

    public void loadAllData(){

    }

    public void writeUser(User user){
    }

    public void writeDish(Dish dish){

    }

    public void writeMealPlan(MealPlan mealPlan){

    }

    public void writeTask(Task task){

    }

    public void writeGroceryList(GroceryList groceryList){

    }

    public User getUserByEmail(String email){

    }

    public Dish getDishById(int id){

    }

    public MealPlan getMealPlanById(int id){

    }

    public Task getTaskById(int id){

    }

    public GroceryList getGroceryListById(int id){

    }
}
