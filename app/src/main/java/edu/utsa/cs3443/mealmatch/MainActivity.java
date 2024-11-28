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
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import edu.utsa.cs3443.mealmatch.adapter.RecommendDishAdapter;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.groq.GroqApiClientImpl;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.utils.Constant;
import edu.utsa.cs3443.mealmatch.utils.UserManager;

/**
 * MainActivity serves as the primary screen where users can view recommended dishes,
 * navigate to different app features (e.g., favorite dishes, meal planner, grocery list),
 * search for dishes, and logout.
 *
 * @author Felix Nguyen
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecommendDishAdapter dishAdapter;
    private ArrayList<Dish> showDishList = new ArrayList<>();

    /**
     * Initializes the activity by setting up the UI elements, navigation buttons,
     * dish recommendations, greeting text, and search functionality.
     * @param savedInstanceState Contains data about the state of the activity, if available.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Apply system bar insets for proper padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        setupNavigationButtons();  // Set up navigation buttons (favorite dishes, meal planner, etc.)
        updateRecyclerView();      // Initialize the RecyclerView for displaying recommended dishes
        initializeRecommendDishes();  // Fetch recommended dishes based on user preferences
        searchBarHandler();        // Set up the search bar action
        setGreeting();             // Set personalized greeting message
        logoutHandler();           // Handle user logout
    }

    /**
     * Handles user logout by clearing session and navigating to the login screen.
     */
    private void logoutHandler() {
        ImageView btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener((v) -> {
            UserManager.getInstance().logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    /**
     * Sets up the navigation buttons to navigate between different app sections.
     */
    private void setupNavigationButtons() {
        findViewById(R.id.btn_favoriteDish).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, FavoriteDishesActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));
        findViewById(R.id.btn_mealPlanner).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, MealPlannerActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));
        findViewById(R.id.btn_groceryList).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, GroceryListActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));
    }

    /**
     * Fetches dish recommendations from the Groq API based on user preferences.
     * The API returns 5 dish IDs that are recommended to the user based on their favorite dishes.
     */
    @SuppressLint("CheckResult")
    private void initializeRecommendDishes() {
        // Build prompt for the Groq API using the user's dish preferences and favorite dishes
        StringBuilder prompt = new StringBuilder();
        for (Dish dish : DataManager.getInstance().getDishes().values()) {
            prompt.append(dish.toString()).append("\n");
        }

        StringBuilder fav = new StringBuilder();
        for (Integer id : UserManager.getInstance().getUser().getFavoriteDishes()) {
            fav.append(id).append(" ");
        }

        GroqApiClientImpl groqApiClient = new GroqApiClientImpl(Constant.GROQ_API_KEY);

        StringBuilder aiPrompt = new StringBuilder();
        aiPrompt.append("Here are the dish IDs: ").append(prompt)
                .append(". User's favorite dish IDs: ").append(fav)
                .append(". Recommend exactly 5 dish IDs to add to favorites. Respond with only 5 digits, separated by spaces, no additional text.");

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", aiPrompt.toString());

        JsonArray messages = new JsonArray();
        messages.add(userMessage);

        JsonObject request = new JsonObject();
        request.addProperty("model", "llama3-8b-8192");
        request.add("messages", messages);

        // Make the request to Groq API and process the response
        groqApiClient.createChatCompletionAsync(request)
                .subscribe(response -> {
                    if (response.has("choices")) {
                        String completion = response.get("choices").getAsJsonArray()
                                .get(0).getAsJsonObject()
                                .get("message").getAsJsonObject()
                                .get("content").getAsString().trim();

                        String[] parseID = completion.split("\\s+");

                        runOnUiThread(() -> {
                            showDishList = new ArrayList<>();

                            for (String id : parseID) {
                                try {
                                    int ID = Integer.parseInt(id.trim());
                                    Dish dish = DataManager.getInstance().getDishById(ID);
                                    if (dish != null) {
                                        showDishList.add(dish);
                                    } else {
                                        Log.e("GROQ", "Dish ID not found: " + ID);
                                    }
                                } catch (NumberFormatException e) {
                                    Log.e("GROQ", "Invalid ID format: " + id);
                                }
                            }

                            updateRecyclerView(); // Update the RecyclerView with the new list of recommended dishes
                        });

                    } else if (response.has("error")) {
                        String error = response.get("error").getAsJsonObject()
                                .get("message").getAsString();
                        Log.e("GROQ", "API Error: " + error);
                    } else {
                        Log.e("GROQ", "Unexpected response format.");
                    }
                }, throwable -> {
                    runOnUiThread(() -> Log.e("GROQ", "Error: " + throwable.getMessage()));
                });
    }

    /**
     * Updates the RecyclerView to display the list of recommended dishes.
     */
    private void updateRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        dishAdapter = new RecommendDishAdapter(this, showDishList, dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        });
        recyclerView.setAdapter(dishAdapter);
    }

    /**
     * Sets up the search bar functionality. It triggers a search action when the user presses enter
     * or clicks the search button.
     */
    private void searchBarHandler() {
        EditText txtSearch = findViewById(R.id.txt_search);

        txtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String searchTerm = txtSearch.getText().toString().trim().toLowerCase();
                if (!searchTerm.isEmpty()) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    intent.putExtra("search_term", searchTerm);
                    startActivity(intent);
                }
                return true;
            }
            return false;
        });
    }

    /**
     * Displays a personalized greeting message to the user.
     */
    private void setGreeting() {
        TextView txtGreeting = findViewById(R.id.txt_greeting);
        txtGreeting.setText("Welcome back, " + UserManager.getInstance().getUser().getFirstname());
    }

    /**
     * Ensures that the RecyclerView is updated with the most recent data when the activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Reload data to reflect updated favorites from search
        if (showDishList != null) {
            dishAdapter.updateData(showDishList);
        }
    }
}
