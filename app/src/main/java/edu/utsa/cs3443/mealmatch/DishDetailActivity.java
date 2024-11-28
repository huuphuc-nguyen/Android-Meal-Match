package edu.utsa.cs3443.mealmatch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import edu.utsa.cs3443.mealmatch.adapter.IngredientAdapter;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.groq.GroqApiClientImpl;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.model.GroceryList;
import edu.utsa.cs3443.mealmatch.model.Task;
import edu.utsa.cs3443.mealmatch.model.User;
import edu.utsa.cs3443.mealmatch.utils.Constant;
import edu.utsa.cs3443.mealmatch.utils.UserManager;

/**
 * Activity for displaying detailed information about a dish.
 * Provides functionality for adding ingredients to the grocery list and querying the AI model for cooking instructions.
 * Also allows users to navigate between application screens.
 *
 * @author Felix Nguyen
 */
public class DishDetailActivity extends AppCompatActivity {

    private Dish dish;

    /**
     * Called when the activity is first created.
     * Initializes the UI elements and sets up necessary actions for the activity.
     *
     * @param savedInstanceState a bundle containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dish_detail);

        // Adjust padding to accommodate system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Retrieve the dish_id passed from the previous activity
        int dishId = getIntent().getIntExtra("dish_id", -1);
        dish = DataManager.getInstance().getDishById(dishId);

        // Display dish name and description, and ingredients
        displayDishNameAndDescription(dish);
        displayIngredientList(dish);

        // Set up button actions
        setUpButtons();

        // Set up the user input field for AI query handling
        EditText txt_userPrompt = findViewById(R.id.txt_user_prompt);
        txt_userPrompt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String searchTerm = txt_userPrompt.getText().toString().trim().toLowerCase();
                AIHandler(searchTerm);
                return true;
            }
            return false;
        });
    }

    /**
     * Adds a given ingredient to the grocery list. If the ingredient already exists, it marks it as undone.
     *
     * @param ingredient the ingredient to add to the grocery list
     */
    private void addIngredientToGroceryList(String ingredient) {
        boolean isAlreadyInGroceryList = false;
        int existedTaskID = 0;

        // Check if the ingredient already exists in the grocery list
        for (Task task : DataManager.getInstance().getTasks().values()) {
            if (task.getName().equals(ingredient) && task.getType().equals(dish.getName())) {
                isAlreadyInGroceryList = true;
                existedTaskID = task.getID();
                break;
            }
        }

        if (!isAlreadyInGroceryList) {
            // If ingredient doesn't exist, create a new task and add it
            Task newTask = new Task(DataManager.getInstance().getNextTaskID(), ingredient, dish.getName(), false);
            DataManager.getInstance().addTask(newTask, this);

            // Add the task to the user's grocery list
            GroceryList userGroceryList = DataManager.getInstance().getGroceryListById(UserManager.getInstance().getUser().getGroceryID());
            userGroceryList.addTask(newTask);

            // Update the grocery list file
            DataManager.getInstance().updateGroceryList(this);
        } else {
            // If the ingredient is already in the list, mark it as undone
            Task checkTask = DataManager.getInstance().getTaskById(existedTaskID);
            checkTask.setDone(false);
            DataManager.getInstance().updateTask(this);
        }
    }

    /**
     * Displays the name and description of the dish in the UI.
     *
     * @param dish the dish object containing the name, description, and image URL
     */
    private void displayDishNameAndDescription(Dish dish) {
        TextView dishNameTextView = findViewById(R.id.txt_dish_name);
        ImageView dishImageView = findViewById(R.id.topImage);
        TextView dishDescriptionTextView = findViewById(R.id.txt_dish_description);

        dishNameTextView.setText(dish.getName());
        dishDescriptionTextView.setText(dish.getDescription());

        // Load the dish image using Glide
        Glide.with(this).load(dish.getImageUrl()).into(dishImageView);
    }

    /**
     * Displays a list of ingredients for the dish using a RecyclerView.
     *
     * @param dish the dish object whose ingredients are to be displayed
     */
    private void displayIngredientList(Dish dish) {
        ArrayList<String> ingredients = dish.getIngredients();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        IngredientAdapter adapter = new IngredientAdapter(this, ingredients, ingredient -> {
            addIngredientToGroceryList(ingredient);
            Toast.makeText(this, ingredient + " added to grocery list.", Toast.LENGTH_SHORT).show();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    /**
     * Sets up the action buttons for navigating between activities and performing actions.
     */
    private void setUpButtons() {
        // Add all ingredients to grocery list
        findViewById(R.id.layout_add_all_ingredient).setOnClickListener(view -> {
            for (String ingredient : dish.getIngredients()) {
                addIngredientToGroceryList(ingredient);
            }
            Toast.makeText(DishDetailActivity.this, "All ingredients added to grocery list.", Toast.LENGTH_SHORT).show();
        });

        // Set up navigation buttons
        findViewById(R.id.btn_favoriteDish).setOnClickListener(view -> startActivity(new Intent(DishDetailActivity.this, FavoriteDishesActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));
        findViewById(R.id.btn_mealPlanner).setOnClickListener(view -> startActivity(new Intent(DishDetailActivity.this, MealPlannerActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));
        findViewById(R.id.btn_groceryList).setOnClickListener(view -> startActivity(new Intent(DishDetailActivity.this, GroceryListActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));

        // Back button
        findViewById(R.id.btn_back).setOnClickListener(view -> finish());
    }

    /**
     * Handles user input for the AI feature, sending the query to the API and displaying the response.
     *
     * @param userInput the text entered by the user to modify the AI request
     */
    @SuppressLint("CheckResult")
    private void AIHandler(String userInput) {
        GroqApiClientImpl groqApiClient = new GroqApiClientImpl(Constant.GROQ_API_KEY);

        // Construct the prompt for the AI model
        StringBuilder aiPrompt = new StringBuilder();
        aiPrompt.append("Here is the dish and its ingredient list: ")
                .append(dish.toString())
                .append(". Provide detailed step-by-step instructions on how to cook it, including exact times for each step. ")
                .append("The response must be purely in HTML format. Use appropriate HTML tags like <b> for bold and <ul>/<li> for lists. ")
                .append("Do not use markdown or any other format.")
                .append("Additionally, please ")
                .append(userInput)
                .append(".");

        // Create the request JSON for the API
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", aiPrompt.toString());

        JsonArray messages = new JsonArray();
        messages.add(userMessage);

        JsonObject request = new JsonObject();
        request.addProperty("model", "llama3-8b-8192");
        request.add("messages", messages);

        // Send the request to the API and handle the response
        groqApiClient.createChatCompletionAsync(request)
                .subscribe(response -> {
                    if (response.has("choices")) {
                        // Extract the AI's response and display it in the UI
                        String completion = response.get("choices").getAsJsonArray()
                                .get(0).getAsJsonObject()
                                .get("message").getAsJsonObject()
                                .get("content").getAsString();
                        runOnUiThread(() -> {
                            TextView txtResponse = findViewById(R.id.txt_ai_response);
                            txtResponse.setText(Html.fromHtml(completion));
                        });
                    } else if (response.has("error")) {
                        String error = response.get("error").getAsJsonObject()
                                .get("message").getAsString();
                        Log.e("GROQ", "API Error: " + error);
                    } else {
                        Log.e("GROQ", "Unexpected response format.");
                    }
                }, throwable -> {
                    // Handle errors in the API request
                    runOnUiThread(() -> Log.e("GROQ", "Error: " + throwable.getMessage()));
                });
    }
}
