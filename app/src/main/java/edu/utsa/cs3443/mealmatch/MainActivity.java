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

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecommendDishAdapter dishAdapter;
    private ArrayList<Dish> showDishList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        tempNavigationHandle();
        updateRecyclerView();

        initializeRecommendDishes();

        searchBarHandler();

        setGreeting();

        logoutHandler();
    }

    private void logoutHandler(){
        ImageView btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener((v) -> {
            UserManager.getInstance().logout();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void tempNavigationHandle(){
        ImageButton btn_fav = findViewById(R.id.btn_favoriteDish);
        ImageButton btn_plan = findViewById(R.id.btn_mealPlanner);
        ImageButton btn_list = findViewById(R.id.btn_groceryList);

        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FavoriteDishesActivity.class));
            }
        });

        btn_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MealPlannerActivity.class));
            }
        });

        btn_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GroceryListActivity.class));
            }
        });
    }

    @SuppressLint("CheckResult")
    private void initializeRecommendDishes(){

        StringBuilder prompt = new StringBuilder();
        for (Dish dish : DataManager.getInstance().getDishes().values()){
            prompt.append(dish.toString()).append("\n");
        }

        StringBuilder fav = new StringBuilder();
        for (Integer id : UserManager.getInstance().getUser().getFavoriteDishes()){
            fav.append(id).append(" ");
        }

        GroqApiClientImpl groqApiClient;
        groqApiClient = new GroqApiClientImpl(Constant.GROQ_API_KEY);

        StringBuilder aiPrompt = new StringBuilder();
        aiPrompt.append("Here are the dish IDs: ")
                .append(prompt)
                .append(". User's favorite dish IDs: ")
                .append(fav)
                .append(". Recommend exactly 5 dish IDs to add to favorites. ")
                .append("Respond with only 5 digits, separated by spaces, no additional text.");

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
                        String completion = response.get("choices").getAsJsonArray()
                                .get(0).getAsJsonObject()
                                .get("message").getAsJsonObject()
                                .get("content").getAsString().trim();

                        String[] parseID = completion.split("\\s+");
                        Log.e("TAG",completion);
                        Log.e("TAG",parseID[1]);


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

                            updateRecyclerView(); // Ensure RecyclerView is updated on the main thread
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

    private void searchBarHandler(){
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

    private void setGreeting(){
        TextView txtGreeting = findViewById(R.id.txt_greeting);

        txtGreeting.setText("Welcome back, " + UserManager.getInstance().getUser().getFirstname());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload data to reflect updated favorites from search
       // dishAdapter.updateData(showDishList);
    }
}