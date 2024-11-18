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

public class DishDetailActivity extends AppCompatActivity {
    private Dish dish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dish_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Retrieve the dish_id passed from MainActivity
        int dishId = getIntent().getIntExtra("dish_id", -1);
        dish = DataManager.getInstance().getDishById(dishId);

        displayDishNameAndDescription(dish);
        displayIngredientList(dish);

        setUpButtons();

        // AI Handler
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

    private void addIngredientToGroceryList(String ingredient){
        // Check if the task is existed before adding to grocery list
        boolean isAlreadyInGroceryList = false;
        int existedTaskID = 0;

        for (Task task : DataManager.getInstance().getTasks().values()){
            // Check to avoid adding the same task of the same ingredient
            if (task.getName().equals(ingredient) && task.getType().equals(dish.getName())){
                isAlreadyInGroceryList = true;
                existedTaskID = task.getID();
                break;
            }
        }

        if (!isAlreadyInGroceryList){
            // Create a new task
            Task newTask = new Task(DataManager.getInstance().getNextTaskID(), ingredient , dish.getName(), false);

            // Add new task to file
            DataManager.getInstance().addTask(newTask, this);

            // Add new task to Grocery list
            GroceryList userGroceryList = DataManager.getInstance().getGroceryListById(UserManager.getInstance().getUser().getGroceryID());
            userGroceryList.addTask(newTask);

            // Update grocery list to file
            DataManager.getInstance().updateGroceryList(this);
        }
        else { // If this ingredient is in the list, mark it as undone in case it is done
            Task checkTask = DataManager.getInstance().getTaskById(existedTaskID);
            checkTask.setDone(false);
            DataManager.getInstance().updateTask(this);
        }
    }
    private void displayDishNameAndDescription(Dish dish) {
        // Update the UI with dish details
        TextView dishNameTextView = findViewById(R.id.txt_dish_name);
        ImageView dishImageView = findViewById(R.id.topImage);
        TextView dishDescriptionTextView = findViewById(R.id.txt_dish_description);

        dishNameTextView.setText(dish.getName());
        dishDescriptionTextView.setText(dish.getDescription());

        // Load dish image using Glide
        Glide.with(this)
                .load(dish.getImageUrl())
                .into(dishImageView);
    }
    private void displayIngredientList(Dish dish){
        ArrayList<String> ingredients = dish.getIngredients();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        IngredientAdapter adapter = new IngredientAdapter(this, ingredients, ingredient -> {
            addIngredientToGroceryList(ingredient);
            Toast.makeText(this, ingredient + " added to grocery list.", Toast.LENGTH_SHORT).show();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    private void setUpButtons(){
        ImageView btn_back = findViewById(R.id.btn_back);
        ImageButton btn_fav = findViewById(R.id.btn_favoriteDish);
        ImageButton btn_plan = findViewById(R.id.btn_mealPlanner);
        ImageButton btn_list = findViewById(R.id.btn_groceryList);
        LinearLayout btn_add_all = findViewById(R.id.layout_add_all_ingredient);

        btn_add_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String ingredient : dish.getIngredients()){
                    addIngredientToGroceryList(ingredient);
                }
                Toast.makeText(DishDetailActivity.this, "All ingredients added to grocery list.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DishDetailActivity.this, FavoriteDishesActivity.class));
            }
        });

        btn_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DishDetailActivity.this, MealPlannerActivity.class));
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DishDetailActivity.this, GroceryListActivity.class));
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void AIHandler(String userInput){
        GroqApiClientImpl groqApiClient;
        groqApiClient = new GroqApiClientImpl(Constant.GROQ_API_KEY);

        StringBuilder aiPrompt = new StringBuilder();
        aiPrompt
                .append("Here is the dish and its ingredient list: ")
                .append(dish.toString())
                .append(". Provide detailed step-by-step instructions on how to cook it, including exact times for each step. ")
                .append("The response must be purely in HTML format. Use appropriate HTML tags like <b> for bold and <ul>/<li> for lists. ")
                .append("Do not use markdown or any other format.")
                .append("Additionally, please ")
                .append(userInput)
                .append(".");

        // Create the request JSON
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", aiPrompt.toString());

        JsonArray messages = new JsonArray();
        messages.add(userMessage);

        JsonObject request = new JsonObject();
        request.addProperty("model", "llama3-8b-8192");
        request.add("messages", messages);

        // Make the request
        groqApiClient.createChatCompletionAsync(request)
                .subscribe(response -> {
                    if (response.has("choices")) {
                        // Extract the content of the first message from the response
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
                    // Handle error
                    runOnUiThread(() -> Log.e("GROQ", "Error: " + throwable.getMessage()));
                });
    }
}