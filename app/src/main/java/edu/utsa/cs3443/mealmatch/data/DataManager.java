package edu.utsa.cs3443.mealmatch.data;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.CheckedInputStream;

import edu.utsa.cs3443.mealmatch.model.User;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.model.MealPlan;
import edu.utsa.cs3443.mealmatch.model.GroceryList;
import edu.utsa.cs3443.mealmatch.model.Task;
import edu.utsa.cs3443.mealmatch.utils.Constant;
import edu.utsa.cs3443.mealmatch.utils.HelperFunctions;

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

    public void loadAllData(Context context){
        // Load users file
        loadUsersFile(context);

        // Load dishes file
        loadDishesFile(context);
    }

    private void loadUsersFile(Context context){
        String filename = Constant.USERS_FILE;
        try {
            InputStream is = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            // Skip header
            reader.readLine();

            while((line = reader.readLine()) != null){
                String columns[] = line.split(",");

                // Parse columns
                String email = columns[0].trim();
                String password = columns[1].trim();
                String firstname = columns[2].trim();
                String lastname = columns[3].trim();
                Integer groceryID = Integer.parseInt(columns[5].trim());

                // Parse favoriteDishes
                ArrayList<Integer> favoriteDishes = HelperFunctions.parseIntegerList(columns[4].trim(), ";");

                // Parse mealPlans (splitting by ";")
                ArrayList<Integer> mealPlans = HelperFunctions.parseIntegerList(columns[6].trim(), ";");

                User user = new User(email, password, firstname, lastname, groceryID, favoriteDishes, mealPlans);
                this.Users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDishesFile(Context context) {
        String filename = Constant.DISHES_FILE;
        try {
            InputStream is = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String columns[] = line.split(",");

                // Parse columns
                int id = Integer.parseInt(columns[0].trim());
                String name = columns[1].trim();
                String description = columns[2].trim();
                String imageUrl = columns[3].trim();
                String calories = columns[4].trim();
                String protein = columns[5].trim();
                String carb = columns[6].trim();
                String fat = columns[7].trim();

                ArrayList<String> ingredients = HelperFunctions.parseStringList(columns[8].trim(), ";");

                Dish dish = new Dish(id, name, description, imageUrl, calories, protein, carb, fat, ingredients);
                this.Dishes.add(dish);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeUser(User user, Context context){
        String filename = Constant.USERS_FILE;
        String data = user.toString() + "\n";

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_APPEND)) {
            fos.write(data.getBytes());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeDish(Dish dish, Context context){
        String filename = Constant.DISHES_FILE;
        String data = dish.toString() + "\n";

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_APPEND)) {
            fos.write(data.getBytes());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeMealPlan(MealPlan mealPlan){

    }

    public void writeTask(Task task){

    }

    public void writeGroceryList(GroceryList groceryList){

    }

    public User getUserByEmail(String email){
        return null;
    }

    public Dish getDishById(int id){
        return null;
    }

    public MealPlan getMealPlanById(int id){
        return null;
    }

    public Task getTaskById(int id){
        return null;
    }

    public GroceryList getGroceryListById(int id){
        return null;
    }

    public void updateUserData(){

    }

    public void writeUserFile(){

    }

    public void writeTaskFile(){

    }

    public void writeGroceryListFile(){

    }

    public void writeMealPlannerFile(){

    }
}
