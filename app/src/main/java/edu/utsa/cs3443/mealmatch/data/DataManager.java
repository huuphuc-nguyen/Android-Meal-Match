package edu.utsa.cs3443.mealmatch.data;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import edu.utsa.cs3443.mealmatch.model.User;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.model.MealPlan;
import edu.utsa.cs3443.mealmatch.model.GroceryList;
import edu.utsa.cs3443.mealmatch.model.Task;
import edu.utsa.cs3443.mealmatch.utils.Constant;
import edu.utsa.cs3443.mealmatch.utils.HelperFunctions;

public class DataManager {
    private static DataManager instance;
    private HashMap<String, User> users; // Keyed by email for quick lookup
    private HashMap<Integer, Dish> dishes; // Keyed by dish ID
    private HashMap<Integer, MealPlan> mealPlans; // Keyed by meal plan ID
    private HashMap<Integer, GroceryList> groceryLists; // Keyed by grocery list ID
    private HashMap<Integer, Task> tasks; // Keyed by task ID

    private DataManager() {
        users = new HashMap<>();
        dishes = new HashMap<>();
        mealPlans = new HashMap<>();
        groceryLists = new HashMap<>();
        tasks = new HashMap<>();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    // Getters
    public HashMap<String, User> getUsers() {
        return users;
    }

    public HashMap<Integer, Dish> getDishes() {
        return dishes;
    }

    public HashMap<Integer, MealPlan> getMealPlans() {
        return mealPlans;
    }

    public HashMap<Integer, GroceryList> getGroceryLists() {
        return groceryLists;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    // Load data functions
    public void loadAllData(Context context) {
        loadUsersFile(context);
        loadDishesFile(context);
        loadGroceryListsFile(context);
        loadTasksFile(context);
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

                // Parse favoriteDishes as HashSet
                HashSet<Integer> favoriteDishes = new HashSet<>(HelperFunctions.parseIntegerList(columns[4].trim(), ";"));

                // Parse mealPlans (splitting by ";")
                ArrayList<Integer> mealPlans = HelperFunctions.parseIntegerList(columns[6].trim(), ";");

                User user = new User(email, password, firstname, lastname, groceryID, favoriteDishes, mealPlans);
                users.put(email, user);
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
                dishes.put(id, dish);
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
                groceryLists.put(id, groceryList);
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
                tasks.put(id, task);
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
                mealPlans.put(id, mealPlan);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ADD TO ARRAYLIST FUNCTIONS

    public void addUserToList(User user, Context context){
        users.put(user.getEmail(), user);
        saveUserData(context);
    }

    public void addMealPlan(MealPlan mealPlan, Context context){
        mealPlans.put(mealPlan.getID(), mealPlan);
        saveMealPlanData(context);
    }

    public void removeTask(Task task, Context context){
        tasks.remove(task.getID());
        saveTaskData(context);
    }

    public void addTask(Task task, Context context){
        tasks.put(task.getID(), task);
        saveTaskData(context);
    }

    public void addGroceryList(GroceryList groceryList, Context context){
        groceryLists.put(groceryList.getID(), groceryList);
        saveGroceryListData(context);
    }

    // GET ELEMENT BY KEY
    public User getUserByEmail(String email) {
        return users.get(email);
    }

    public Dish getDishById(int id) {
        return dishes.get(id);
    }

    public MealPlan getMealPlanById(int id) {
        return mealPlans.get(id);
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public GroceryList getGroceryListById(int id) {
        return groceryLists.get(id);
    }

    // SAVE ARRAYLIST TO FILES
    private void saveUserData(Context context){
        String filename = Constant.USERS_FILE;

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE)) {
            StringBuilder data = new StringBuilder();

            data.append("Username, password, first name, last name, favoriteDishes, groceryListID, mealPlans").append("\n");

            // Build the data string from all users in the list
            for (User user : users.values()) {
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

            for (Task task : tasks.values()) {
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

            for (GroceryList groceryList : groceryLists.values()) {
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

            for (MealPlan mealPlan : mealPlans.values()) {
                data.append(mealPlan.toString()).append("\n");
            }

            // Write the entire string to the file at once
            fos.write(data.toString().getBytes());
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(Context context){
        saveTaskData(context);
    }

    public void updateUser(Context context){
        saveUserData(context);
    }

    public void updateGroceryList(Context context){
        saveGroceryListData(context);
    }

    public void updateMealPlan(Context context){
        saveMealPlanData(context);
    }

    // ID generator
    public int getNextGroceryListID() {
        if (groceryLists.isEmpty()) {
            return 1; // Start IDs from 1 if the list is empty
        }

        // Find the maximum ID in the current list
        return groceryLists.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    public int getNextTaskID() {
        if (tasks.isEmpty()) {
            return 1; // Start IDs from 1 if the list is empty
        }

        return tasks.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }

    public int getNextMealPlanID() {
        if (mealPlans.isEmpty()) {
            return 1; // Start IDs from 1 if the list is empty
        }

        return mealPlans.keySet().stream()
                .max(Integer::compareTo)
                .orElse(0) + 1;
    }
}
