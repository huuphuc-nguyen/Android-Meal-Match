package edu.utsa.cs3443.mealmatch;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
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

import edu.utsa.cs3443.mealmatch.adapter.HorizontalDishAdapter;
import edu.utsa.cs3443.mealmatch.data.DataManager;
import edu.utsa.cs3443.mealmatch.model.Dish;

/**
 * SearchResultActivity displays the search results for a user query. It allows users to search for dishes
 * by their names and view the results in a list. The activity also provides navigation to other parts of the app
 * such as favorite dishes, meal planner, and grocery list.
 *
 * @author Felix Nguyen
 */
public class SearchResultActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HorizontalDishAdapter dishAdapter;

    /**
     * Initializes the SearchResultActivity, sets up the UI components, handles the search bar functionality,
     * and loads dishes based on the search term.
     *
     * @param savedInstanceState The saved instance state, if the activity is being recreated.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_result);

        // Apply system bar insets to the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Get search term from intent and set the search bar text
        Intent intent = getIntent();
        String searchTerm = intent.getStringExtra("search_term");
        EditText txtSearch = findViewById(R.id.txt_search);
        txtSearch.setText(searchTerm);

        // Set up the dishes based on the search term
        setSearchDishes(searchTerm);
        setTitle(searchTerm);

        // Set up navigation buttons and other UI actions
        setupNavigationButtons();
        setButtons();

        // Set up the search bar listener to handle search actions
        searchBarHandler();
    }

    /**
     * Sets up the search bar listener to perform the search when the user presses the "Search" button or "Enter".
     */
    private void searchBarHandler() {
        EditText txtSearch = findViewById(R.id.txt_search);
        txtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                String searchTerm = txtSearch.getText().toString().trim().toLowerCase();
                if (!searchTerm.isEmpty()) {
                    setTitle(searchTerm);
                    updateSearchResult(searchTerm);
                }
                return true;
            }
            return false;
        });
    }

    /**
     * Sets up the navigation buttons for navigating to other screens of the app.
     */
    private void setupNavigationButtons() {
        findViewById(R.id.btn_favoriteDish).setOnClickListener(view ->
                startActivity(new Intent(SearchResultActivity.this, FavoriteDishesActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));

        findViewById(R.id.btn_mealPlanner).setOnClickListener(view ->
                startActivity(new Intent(SearchResultActivity.this, MealPlannerActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));

        findViewById(R.id.btn_groceryList).setOnClickListener(view ->
                startActivity(new Intent(SearchResultActivity.this, GroceryListActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)));
    }

    /**
     * Sets up the back button functionality to finish the activity when clicked.
     */
    private void setButtons() {
        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(view -> finish());
    }

    /**
     * Sets the title of the search results screen based on the search term entered by the user.
     *
     * @param searchTerm The search term entered by the user.
     */
    private void setTitle(String searchTerm) {
        TextView txtTitle = findViewById(R.id.txt_search_result_for);
        String title = "Search results for \"" + searchTerm + "\"";
        txtTitle.setText(title);
    }

    /**
     * Updates, filters and displays the list of dishes based on the search term. The dishes are displayed in a RecyclerView.
     *
     * @param searchTerm The search term used to filter dishes.
     */
    private void updateSearchResult(String searchTerm) {
        ArrayList<Dish> dishData = new ArrayList<>(DataManager.getInstance().getDishes().values());
        ArrayList<Dish> setList = new ArrayList<>();

        for (Dish dish : dishData) {
            if (dish.getName().toLowerCase().contains(searchTerm)) {
                setList.add(dish);
            }
        }

        dishAdapter.updateDishes(setList);
    }

    /**
     * Filters and displays the list of dishes based on the search term. The dishes are displayed in a RecyclerView.
     *
     * @param searchTerm The search term used to filter dishes.
     */
    private void setSearchDishes(String searchTerm) {
        // Create a list of dishes and filter those that contain the search term
        ArrayList<Dish> dishData = new ArrayList<>(DataManager.getInstance().getDishes().values());
        ArrayList<Dish> setList = new ArrayList<>();

        for (Dish dish : dishData) {
            if (dish.getName().toLowerCase().contains(searchTerm)) {
                setList.add(dish);
            }
        }

        // Initialize the RecyclerView to display the filtered dishes
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Add item decoration for vertical spacing
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10; // Top margin
                outRect.bottom = 10; // Bottom margin
            }
        });

        // Initialize the adapter with the filtered dishes and set it to the RecyclerView
        dishAdapter = new HorizontalDishAdapter(this, setList, dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra("dish_id", dish.getID());
            startActivity(intent);
        });
        recyclerView.setAdapter(dishAdapter);
    }
}