package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.utsa.cs3443.mealmatch.adapter.HorizontalDishAdapter;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.utils.UserManager;

/**
 * Activity for displaying the user's favorite dishes.
 * Provides functionality for searching favorite dishes and navigating between application screens.
 *
 * @author Felix Nguyen, Gabriel Reyes
 */
public class FavoriteDishesActivity extends AppCompatActivity {
    ArrayList<Dish> favDishes;
    private RecyclerView recyclerView;
    private HorizontalDishAdapter dishAdapter;

    /**
     * Called when the activity is first created.
     * Initializes the layout, UI components, and sets up event listeners.
     *
     * @param savedInstanceState a bundle containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite_dishes);

        // Adjust padding to accommodate system bars (e.g., status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Initialize navigation buttons
        setupNavigationButtons();

        // Initialize search bar functionality
        searchBarHandler();

        // Display the favorite dishes after the layout is set up
        displayFavoriteDishes();
    }

    /**
     * Sets up the search bar functionality to filter dishes based on user input.
     */
    private void searchBarHandler() {
        EditText txtSearch = findViewById(R.id.txt_search);

        // Listen for 'search' or 'enter' key events to filter the list
        txtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String searchTerm = txtSearch.getText().toString().trim().toLowerCase();
                if (!searchTerm.isEmpty()) {
                    // Pass the search term to displayFilteredFavoriteDishes method
                    displayFilteredFavoriteDishes(searchTerm);
                }
                return true;
            }
            return false;
        });
    }

    /**
     * Displays the user's favorite dishes in a RecyclerView.
     * Fetches the dishes from DataManager using the favorite dish IDs stored in UserManager.
     */
    private void displayFavoriteDishes() {
        favDishes = new ArrayList<>();

        // Fetch user's favorite dish IDs from UserManager and populate the list
        for (int id : UserManager.getInstance().getUser().getFavoriteDishes()) {
            // Fetch the dish using its ID from DataManager
            Dish getDish = DataManager.getInstance().getDishById(id);
            if (getDish != null) {
                favDishes.add(getDish);
            }
        }

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Add ItemDecoration for vertical spacing between items
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;  // Top margin for each item
                outRect.bottom = 10;  // Bottom margin for each item
            }
        });

        // Initialize Adapter and set it to the RecyclerView
        dishAdapter = new HorizontalDishAdapter(this, favDishes, dish -> {
            // On clicking a dish, start DishDetailActivity with the selected dish ID
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        });
        recyclerView.setAdapter(dishAdapter);
    }

    /**
     * Filters the displayed favorite dishes based on a search term.
     * This method is called after a user enters a search term in the search bar.
     *
     * @param searchTerm the term entered by the user to filter dishes
     */
    private void displayFilteredFavoriteDishes(String searchTerm) {
        favDishes = new ArrayList<>();

        // Fetch the user's favorite dish IDs from UserManager
        ArrayList<Integer> favoriteDishIds = new ArrayList<>(UserManager.getInstance().getUser().getFavoriteDishes());

        // Filter dishes that match the search term
        for (int id : favoriteDishIds) {
            Dish dish = DataManager.getInstance().getDishById(id);
            if (dish != null && dish.getName().toLowerCase().contains(searchTerm)) {
                favDishes.add(dish);
            }
        }

        // Reinitialize RecyclerView to update the displayed list
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Add ItemDecoration for vertical spacing
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;
                outRect.bottom = 10;
            }
        });

        // Initialize Adapter and set it to the RecyclerView
        dishAdapter = new HorizontalDishAdapter(this, favDishes, dish -> {
            // On clicking a dish, start DishDetailActivity with the selected dish ID
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        });
        recyclerView.setAdapter(dishAdapter);
    }

    /**
     * Sets up navigation buttons to allow users to navigate between different activities.
     */
    private void setupNavigationButtons() {
        // Home button to navigate to MainActivity
        findViewById(R.id.btn_home).setOnClickListener(view -> startActivity(new Intent(FavoriteDishesActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));

        // Meal Planner button to navigate to MealPlannerActivity
        findViewById(R.id.btn_mealPlanner).setOnClickListener(view -> startActivity(new Intent(FavoriteDishesActivity.this, MealPlannerActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));

        // Grocery List button to navigate to GroceryListActivity
        findViewById(R.id.btn_groceryList).setOnClickListener(view -> startActivity(new Intent(FavoriteDishesActivity.this, GroceryListActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));

        // Back button to finish the activity and return to the previous screen
        findViewById(R.id.btn_back).setOnClickListener(view -> finish());
    }
}