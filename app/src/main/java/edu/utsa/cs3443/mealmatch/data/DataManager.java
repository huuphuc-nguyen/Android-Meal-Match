package edu.utsa.cs3443.mealmatch.data;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Date;
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

        // Load grocery lists file
        loadGroceryListsFile(context);

        // Load task file
        loadTasksFile(context);

        // Load meal plan file
        loadMealPlanFile(context);
    }

    // READING FILE FUNCTIONS
    private void loadUsersFile(Context context){
        String filename = Constant.USERS_FILE;
        try {
            InputStream is = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            // Skip header
            reader.readLine();

            while((line = reader.readLine()) != null){
                String[] columns = line.split(",");

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
                String[] columns = line.split(",");

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

    private void loadGroceryListsFile(Context context){
        String filename = Constant.GROCERY_LIST_FILE;
        try {
            InputStream is = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            // Skip header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");

                // Parse columns
                int id = Integer.parseInt(columns[0].trim());

                ArrayList<Integer> tasks = HelperFunctions.parseIntegerList(columns[1].trim(), ";");

                GroceryList groceryList = new GroceryList(id,tasks);
                this.GroceryLists.add(groceryList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasksFile(Context context){
        String filename = Constant.TASKS_FILE;
        try {
            InputStream is = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            // Skip header line
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");

                // Parse columns
                int id = Integer.parseInt(columns[0].trim());
                String name = columns[1].trim();
                String type = columns[2].trim();
                boolean isDone = columns[3].trim().equals("1");

                // Create Task object
                Task task = new Task(id, name, type, isDone);
                Tasks.add(task);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMealPlanFile(Context context){
        String filename = Constant.MEAL_PLANS_FILE;
        try {
            InputStream is = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;

            // Skip header line
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");

                // Parse columns
                int id = Integer.parseInt(columns[0].trim());
                Date planDate = HelperFunctions.parseDate(columns[1].trim()); // Parse date using helper
                ArrayList<Integer> dishesID = HelperFunctions.parseIntegerList(columns[2].trim(), ";");

                // Create MealPlan object
                MealPlan mealPlan = new MealPlan(id, planDate, dishesID);
                MealPlans.add(mealPlan);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ADD TO ARRAYLIST FUNCTIONS

    public void addUserToList(User user, Context context){
        this.Users.add(user);
        saveUserData(context);
    }

    public void addMealPlan(MealPlan mealPlan, Context context){
        this.MealPlans.add(mealPlan);
        saveMealPlanData(context);
    }

    public void addTask(Task task, Context context){
        this.Tasks.add(task);
        saveTaskData(context);
    }

    public void addGroceryList(GroceryList groceryList, Context context){
        this.GroceryLists.add(groceryList);
        saveGroceryListData(context);
    }

    // GET ELEMENT WITH ID
    public User getUserByEmail(String email){
        for (User user : Users) {
            if (user.getEmail().equalsIgnoreCase(email.trim())) {
                return user;
            }
        }
        return null;
    }

    public Dish getDishById(int id){
        for (Dish dish : Dishes) {
            if (dish.getID() == id) {
                return dish;
            }
        }
        return null;
    }

    public MealPlan getMealPlanById(int id){
        for (MealPlan mealPlan : MealPlans){
            if (mealPlan.getID() == id){
                return mealPlan;
            }
        }
        return null;
    }

    public Task getTaskById(int id){
        for (Task task : Tasks){
            if(task.getID() == id){
                return task;
            }
        }
        return null;
    }

    public GroceryList getGroceryListById(int id){
        for (GroceryList groceryList : GroceryLists){
            if (groceryList.getID() == id){
                return  groceryList;
            }
        }
        return null;
    }

    // SAVE ARRAYLIST TO FILES
    private void saveUserData(Context context){
        String filename = Constant.USERS_FILE;

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            StringBuilder data = new StringBuilder();

            data.append("Username, password, first name, last name, favoriteDishes, groceryListID, mealPlans").append("\n");

            // Build the data string from all users in the list
            for (User user : Users) {
                data.append(user.toString()).append("\n");
            }

            // Write the entire string to the file at once
            fos.write(data.toString().getBytes());
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTaskData(Context context){
        String filename = Constant.TASKS_FILE;

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            StringBuilder data = new StringBuilder();

            data.append("Id, name, type, isDone").append("\n");

            for (Task task : Tasks) {
                data.append(task.toString()).append("\n");
            }

            // Write the entire string to the file at once
            fos.write(data.toString().getBytes());
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveGroceryListData(Context context){
        String filename = Constant.GROCERY_LIST_FILE;

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            StringBuilder data = new StringBuilder();

            data.append("Id, TasksId").append("\n");

            for (GroceryList groceryList : GroceryLists) {
                data.append(groceryList.toString()).append("\n");
            }

            // Write the entire string to the file at once
            fos.write(data.toString().getBytes());
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveMealPlanData(Context context){
        String filename = Constant.MEAL_PLANS_FILE;

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            StringBuilder data = new StringBuilder();

            data.append("ID, planDate, dishID").append("\n");

            for (MealPlan mealPlan : MealPlans) {
                data.append(mealPlan.toString()).append("\n");
            }

            // Write the entire string to the file at once
            fos.write(data.toString().getBytes());
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ID generator

    public int getNextGroceryListID() {
        if (GroceryLists.isEmpty()) {
            return 1; // Start IDs from 1 if the list is empty
        }

        // Find the maximum ID in the current list
        return GroceryLists.stream()
                .mapToInt(GroceryList::getID)
                .max()
                .orElse(0) + 1; // Increment the maximum ID by 1
    }

    public int getNextTaskID() {
        if (Tasks.isEmpty()) {
            return 1; // Start IDs from 1 if the list is empty
        }

        return Tasks.stream()
                .mapToInt(Task::getID)
                .max()
                .orElse(0) + 1;
    }

    public int getNextMealPlanID() {
        if (MealPlans.isEmpty()) {
            return 1; // Start IDs from 1 if the list is empty
        }

        return MealPlans.stream()
                .mapToInt(MealPlan::getID)
                .max()
                .orElse(0) + 1;
    }
}
