package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import edu.utsa.cs3443.mealmatch.adapter.AddMealAdapter;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;
import edu.utsa.cs3443.mealmatch.utils.UserManager;

/**
 * Activity for adding meals to a user's meal plan.
 * Provides functionality for recommending dishes, searching for dishes,
 * and navigating between application screens.
 *
 * @author Felix Nguyen, Isaac Caldera
 */
public class AddMealActivity extends AppCompatActivity {
    private RecyclerView recyclerView; // RecyclerView to display the list of dishes.
    private AddMealAdapter dishAdapter; // Adapter for managing the dish list.
    private ArrayList<Dish> showDishList; // List of dishes to display in the recommendation section.
    private int currentMealID; // ID of the current meal plan being modified.

    /**
     * Called when the activity is created.
     * Sets up the UI, handles navigation buttons, and initializes recommendations and search functionality.
     *
     * @param savedInstanceState the saved instance state for restoring the activity's previous state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enables edge-to-edge display for the activity.
        setContentView(R.layout.activity_add_meal);

        // Adjusts padding for system bars.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        currentMealID = getIntent().getIntExtra("meal_id", 0); // Retrieves the current meal plan ID.
        setGreeting(); // Displays a personalized greeting.
        setupNavigationButtons(); // Sets up navigation buttons for the app.
        recommendDishes(); // Displays recommended dishes.
        searchBarHandler(); // Handles search functionality.
        setButtons(); // Sets up additional UI buttons.
    }

    /**
     * Configures the search bar to handle user input and filter dishes based on search terms.
     */
    private void searchBarHandler() {
        EditText txtSearch = findViewById(R.id.txt_search);

        txtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String searchTerm = txtSearch.getText().toString().trim().toLowerCase();
                if (!searchTerm.isEmpty()) {
                    setSearchDishes(searchTerm);
                }
                return true;
            }
            return false;
        });
    }

    /**
     * Configures the back button to return to the previous screen when clicked.
     */
    private void setButtons() {
        ImageView btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(view -> finish());
    }

    /**
     * Updates the RecyclerView with dishes matching the search term.
     *
     * @param searchTerm the term used to filter dishes by name.
     */
    private void setSearchDishes(String searchTerm) {
        ArrayList<Dish> dishData = new ArrayList<>(DataManager.getInstance().getDishes().values());
        ArrayList<Dish> setList = new ArrayList<>();

        // Filter dishes based on the search term.
        for (Dish dish : dishData) {
            if (dish.getName().toLowerCase().contains(searchTerm)) {
                setList.add(dish);
            }
        }

        // Initialize RecyclerView with the filtered list of dishes.
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Add spacing between items.
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;
                outRect.bottom = 10;
            }
        });

        // Set the adapter for the RecyclerView.
        dishAdapter = new AddMealAdapter(this, setList, dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        }, currentMealID);
        recyclerView.setAdapter(dishAdapter);
    }

    /**
     * Displays a list of recommended dishes to the user.
     * Selects five random dishes from the available data.
     */
    private void recommendDishes() {
        ArrayList<Dish> dishData = new ArrayList<>(DataManager.getInstance().getDishes().values());
        ArrayList<Dish> setList = new ArrayList<>(dishData);
        Collections.shuffle(setList); // Shuffle the list for randomness.
        showDishList = new ArrayList<>(setList.subList(0, 5)); // Pick the first five dishes.

        // Initialize RecyclerView with recommended dishes.
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Add spacing between items.
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;
                outRect.bottom = 10;
            }
        });

        // Set the adapter for the RecyclerView.
        dishAdapter = new AddMealAdapter(this, showDishList, dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        }, currentMealID);
        recyclerView.setAdapter(dishAdapter);
    }

    /**
     * Displays a greeting message with the user's first name.
     */
    private void setGreeting() {
        TextView txtGreeting = findViewById(R.id.txt_greeting);
        String name = UserManager.getInstance().getUser().getFirstname(); // Retrieves the user's first name.
        txtGreeting.setText("Hello " + name + ",\n search meals to add");
    }

    /**
     * Configures navigation buttons to navigate between screens in the app.
     */
    private void setupNavigationButtons() {
        findViewById(R.id.btn_favoriteDish).setOnClickListener(view ->
                startActivity(new Intent(AddMealActivity.this, FavoriteDishesActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));

        findViewById(R.id.btn_home).setOnClickListener(view ->
                startActivity(new Intent(AddMealActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));

        findViewById(R.id.btn_groceryList).setOnClickListener(view ->
                startActivity(new Intent(AddMealActivity.this, GroceryListActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));
    }
}